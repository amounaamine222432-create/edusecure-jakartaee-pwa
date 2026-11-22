
param(
    [Parameter(Mandatory=$true)][string]$RunId,
    [switch]$SimulateFailure
)

$ErrorActionPreference = "Stop"

$repo = "amounaamine222432-create/edusecure-jakartaee-pwa"
$root = (Resolve-Path (Join-Path $PSScriptRoot "..\..\..")).Path
$configFile = Join-Path $root "deployment\wildfly\dev\edusecure-dev.properties"
$artifactRoot = Join-Path $root "deployment\artifacts\run-$RunId"
$apiDir = Join-Path $artifactRoot "api"
$pwaDir = Join-Path $artifactRoot "pwa"
$wildfly = "C:\wildfly-42.0.0.Final"

Write-Host "[1/5] Verification du workflow GitHub"

$runJson = & gh run view $RunId --repo $repo --json conclusion,status,event,headBranch,headSha
if ($LASTEXITCODE -ne 0) {
    throw "Impossible de verifier GitHub Actions"
}

$run = $runJson | ConvertFrom-Json
if ($run.status -ne "completed" -or $run.conclusion -ne "success") {
    throw "Le workflow GitHub n'a pas reussi"
}
if ($run.event -ne "push" -or $run.headBranch -ne "main") {
    throw "Source du workflow non autorisee"
}

$localSha = (& git -C $root rev-parse HEAD).Trim()
if ($LASTEXITCODE -ne 0 -or $localSha -ne $run.headSha) {
    throw "Le commit local ne correspond pas au workflow valide"
}
Write-Host "Workflow valide : $($run.headSha)"

Write-Host "[2/5] Verification des artefacts"
Write-Host "Recuperation des artefacts GitHub Actions..."

New-Item -ItemType Directory -Force $apiDir | Out-Null
New-Item -ItemType Directory -Force $pwaDir | Out-Null

Get-ChildItem -LiteralPath $apiDir -Force | Remove-Item -Recurse -Force
Get-ChildItem -LiteralPath $pwaDir -Force | Remove-Item -Recurse -Force

& gh run download $RunId --repo $repo --name "edusecure-api-war" --dir $apiDir
if ($LASTEXITCODE -ne 0) {
    throw "Echec du telechargement du WAR"
}
& gh run download $RunId --repo $repo --name "edusecure-pwa" --dir $pwaDir
if ($LASTEXITCODE -ne 0) {
    throw "Echec du telechargement de la PWA"
}
Write-Host "Artefacts CI telecharges"

$warFiles = @(Get-ChildItem -LiteralPath $apiDir -Filter "*.war" -File)
if ($warFiles.Count -ne 1) {
    throw "Un seul WAR est attendu"
}
$war = $warFiles[0].FullName

if (-not (Test-Path (Join-Path $pwaDir "index.html") -PathType Leaf)) {
    throw "PWA incomplete : index.html absent"
}
if (-not (Test-Path (Join-Path $pwaDir "manifest.json") -PathType Leaf)) {
    throw "PWA incomplete : manifest.json absent"
}

# Verifier les fichiers PWA contre le commit Git autorise
Write-Host "Verification de l'integrite PWA..."

$trackedFiles = @(& git -C $root ls-files -- frontend/pwa)
if ($LASTEXITCODE -ne 0 -or $trackedFiles.Count -eq 0) {
    throw "Impossible de recuperer les fichiers PWA suivis par Git"
}

$expected = @{}

foreach ($trackedFile in $trackedFiles) {
    if (-not $trackedFile.StartsWith("frontend/pwa/")) {
        throw "Chemin PWA inattendu : $trackedFile"
    }

    $relative = $trackedFile.Substring("frontend/pwa/".Length)
    $expected[$relative] = $true
    $downloaded = Join-Path $pwaDir ($relative.Replace('/', '\'))

    if (-not (Test-Path -LiteralPath $downloaded -PathType Leaf)) {
        throw "SECURITE : fichier PWA manquant : $relative"
    }

    $expectedHash = (& git -C $root rev-parse "HEAD:$trackedFile").Trim()
    if ($LASTEXITCODE -ne 0 -or
        $expectedHash -notmatch '^(?:[0-9a-fA-F]{40}|[0-9a-fA-F]{64})$') {
        throw "SECURITE : objet Git invalide : $relative"
    }

    # Appliquer les filtres Git, notamment la normalisation LF/CRLF
    $actualHash = (& git -C $root hash-object "--path=$trackedFile" -- $downloaded).Trim()
    if ($LASTEXITCODE -ne 0 -or $actualHash -ne $expectedHash) {
        throw "SECURITE : fichier PWA modifie : $relative"
    }
}

foreach ($file in (Get-ChildItem -LiteralPath $pwaDir -Recurse -File)) {
    $relative = $file.FullName.Substring($pwaDir.Length + 1).Replace('\', '/')
    if (-not $expected.ContainsKey($relative)) {
        throw "SECURITE : fichier PWA supplementaire : $relative"
    }
}

Write-Host "Integrite PWA validee"

$warHash = (Get-FileHash -LiteralPath $war -Algorithm SHA256).Hash
Write-Host "WAR : $($warFiles[0].Name)"
Write-Host "SHA256 local : $warHash"

# Verifier l'attestation SLSA du WAR
Write-Host "Verification de l'attestation GitHub..."

& gh attestation verify $war `
    --repo $repo `
    --source-ref "refs/heads/main" `
    --source-digest $run.headSha `
    --signer-workflow "$repo/.github/workflows/ci.yml"

if ($LASTEXITCODE -ne 0) {
    throw "SECURITE : attestation WAR invalide - deploiement interdit"
}
Write-Host "Attestation WAR validee"

Write-Host "[3/5] Chargement de la configuration DEV"

if (-not (Test-Path -LiteralPath $configFile -PathType Leaf)) {
    throw "Configuration DEV introuvable"
}

$config = @{}

Get-Content -LiteralPath $configFile | ForEach-Object {
    $line = $_.Trim().TrimStart([char]0xFEFF)

    if ($line -match '^([^#=\s]+)\s*=(.*)$') {
        $config[$matches[1]] = $matches[2].Trim()
    }
}

if ($config["wildfly.host"] -ne "127.0.0.1") {
    throw "Seul WildFly local est autorise"
}
if ($config["api.context"] -ne "/") {
    throw "Contexte API incorrect"
}
if ($config["api.aliases"] -ne "api.edusecure.local" -or
    $config["host.aliases"] -ne "app.edusecure.local") {
    throw "Domaines DEV incorrects"
}

$pwaTarget = $config["host.path"]
if (($pwaTarget -replace '\\', '/') -ne "C:/edusecure/pwa") {
    throw "Destination PWA non autorisee"
}

$managementPort = $config["wildfly.management.port"]
$httpPort = $config["wildfly.http.port"]

if ($managementPort -notmatch '^\d+$' -or $httpPort -notmatch '^\d+$') {
    throw "Ports WildFly invalides"
}

Write-Host "[4/5] Sauvegarde et deploiement WildFly DEV"

$cli = Join-Path $wildfly "bin\jboss-cli.bat"
if (-not (Test-Path -LiteralPath $cli -PathType Leaf)) {
    throw "WildFly CLI introuvable"
}

$stagingDir = "C:\edusecure\deploy"
$backupRoot = "C:\edusecure\backups"
$stagedWar = Join-Path $stagingDir "edusecure-api-1.0.0-SNAPSHOT.war"

New-Item -ItemType Directory -Force $stagingDir | Out-Null
New-Item -ItemType Directory -Force $backupRoot | Out-Null

if (-not (Test-Path -LiteralPath $stagedWar -PathType Leaf)) {
    throw "Ancien WAR absent : rollback impossible"
}
if (-not (Test-Path (Join-Path $pwaTarget "index.html") -PathType Leaf)) {
    throw "Ancienne PWA absente : rollback impossible"
}

# Creer une sauvegarde unique
$backupId = (Get-Date -Format "yyyyMMdd-HHmmss") + "-" +
    [guid]::NewGuid().ToString("N").Substring(0, 8)

$backupDir = Join-Path $backupRoot $backupId
$backupPwa = Join-Path $backupDir "pwa"
$backupWar = Join-Path $backupDir "edusecure-api-previous.war"

New-Item -ItemType Directory -Force $backupPwa | Out-Null

Copy-Item -LiteralPath $stagedWar -Destination $backupWar -Force
Copy-Item -Path (Join-Path $pwaTarget "*") `
    -Destination $backupPwa -Recurse -Force

$previousHash = (Get-FileHash -LiteralPath $stagedWar -Algorithm SHA256).Hash
$backupHash = (Get-FileHash -LiteralPath $backupWar -Algorithm SHA256).Hash

if ($previousHash -ne $backupHash) {
    throw "Integrite de la sauvegarde WAR invalide"
}

Write-Host "Sauvegarde creee : $backupDir"

function Test-EduSecure {
    param([string]$Port)

    $apiUrl = "https://api.edusecure.local:$Port/api/health"
    $pwaUrl = "https://app.edusecure.local:$Port/"

    $response = & curl.exe --silent --show-error --fail `
        --noproxy "*" --cacert "C:\edusecure\tls\edusecure-dev.crt" --tlsv1.3 --tls-max 1.3 $apiUrl

    if ($LASTEXITCODE -ne 0) {
        throw "API indisponible"
    }

    $health = $response | ConvertFrom-Json

    if ($health.status -ne "UP" -or
        $health.application -ne "EduSecure") {
        throw "Reponse API incorrecte"
    }

    & curl.exe --silent --show-error --fail `
        --noproxy "*" --cacert "C:\edusecure\tls\edusecure-dev.crt" --tlsv1.3 --tls-max 1.3 -o NUL $pwaUrl

    if ($LASTEXITCODE -ne 0) {
        throw "PWA indisponible"
    }
}

$deploymentStarted = $false

try {
    $deploymentStarted = $true

    # Installer la nouvelle PWA
    New-Item -ItemType Directory -Force $pwaTarget | Out-Null

    Get-ChildItem -LiteralPath $pwaTarget -Force |
        Remove-Item -Recurse -Force

    Copy-Item -Path (Join-Path $pwaDir "*") `
        -Destination $pwaTarget -Recurse -Force

    # Copier le WAR dans un chemin sans espaces
    Copy-Item -LiteralPath $war -Destination $stagedWar -Force

    $stagedHash = (Get-FileHash -LiteralPath $stagedWar -Algorithm SHA256).Hash

    if ($stagedHash -ne $warHash) {
        throw "Integrite du WAR apres copie invalide"
    }

    Write-Host "Integrite de la copie WAR verifiee"

    # Deployer le WAR sur WildFly
    $cliWarPath = $stagedWar.Replace('\', '/')

    & $cli "--connect" `
        "--controller=127.0.0.1:$managementPort" `
        "--command=deploy $cliWarPath --force"

    if ($LASTEXITCODE -ne 0) {
        throw "Deploiement WAR echoue"
    }

    # UNIQUE simulation d'echec, apres le deploiement
    if ($SimulateFailure.IsPresent) {
        Write-Host "TEST : declenchement volontaire du rollback"
        throw "TEST : echec simule apres deploiement"
    }

    Write-Host "[5/5] Verification des services"
    Test-EduSecure -Port 8443

    Write-Host ""
    Write-Host "DEPLOIEMENT DEPUIS CI REUSSI"
    Write-Host "Sauvegarde precedente : $backupDir"
}
catch {
    $initialError = $_.Exception.Message
    Write-Host "ECHEC : $initialError"

    if ($deploymentStarted) {
        Write-Host "ROLLBACK EN COURS..."

        try {
            # Restaurer la PWA precedente
            Get-ChildItem -LiteralPath $pwaTarget -Force |
                Remove-Item -Recurse -Force

            Copy-Item -Path (Join-Path $backupPwa "*") `
                -Destination $pwaTarget -Recurse -Force

            # Restaurer le WAR precedent
            Copy-Item -LiteralPath $backupWar `
                -Destination $stagedWar -Force

            $restoreHash = (Get-FileHash -LiteralPath $stagedWar -Algorithm SHA256).Hash

            if ($restoreHash -ne $backupHash) {
                throw "Integrite du WAR restaure invalide"
            }

            $restorePath = $stagedWar.Replace('\', '/')

            & $cli "--connect" `
                "--controller=127.0.0.1:$managementPort" `
                "--command=deploy $restorePath --force"

            if ($LASTEXITCODE -ne 0) {
                throw "Restauration WildFly echouee"
            }

            Test-EduSecure -Port 8443
            Write-Host "ROLLBACK REUSSI"
        }
        catch {
            Write-Host "ROLLBACK ECHEC : $($_.Exception.Message)"
            throw "Deploiement et restauration echoues"
        }
    }

    throw "Deploiement annule : $initialError"
}
