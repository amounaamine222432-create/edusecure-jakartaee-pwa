
param(
    [string]$RunId = "45018032070"
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

$warFiles = @(Get-ChildItem $apiDir -Filter "*.war" -File)

if ($warFiles.Count -ne 1) {
    throw "Un seul WAR est attendu"
}

$war = $warFiles[0].FullName

if (-not (Test-Path (Join-Path $pwaDir "index.html"))) {
    throw "PWA incomplete : index.html absent"
}

if (-not (Test-Path (Join-Path $pwaDir "manifest.json"))) {
    throw "PWA incomplete : manifest.json absent"
}

$warHash = (Get-FileHash $war -Algorithm SHA256).Hash

Write-Host "WAR : $($warFiles[0].Name)"
Write-Host "SHA256 local : $warHash"

Write-Host "[3/5] Chargement de la configuration DEV"

if (-not (Test-Path $configFile)) {
    throw "Configuration DEV introuvable"
}

$config = @{}

Get-Content $configFile | ForEach-Object {
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

if (($pwaTarget -replace '\\','/') -ne "C:/edusecure/pwa") {
    throw "Destination PWA non autorisee"
}

$managementPort = $config["wildfly.management.port"]
$httpPort = $config["wildfly.http.port"]

if ($managementPort -notmatch '^\d+$' -or
    $httpPort -notmatch '^\d+$') {
    throw "Ports WildFly invalides"
}

Write-Host "[4/5] Deploiement WildFly DEV"

New-Item -ItemType Directory -Force $pwaTarget | Out-Null

Copy-Item -Path (Join-Path $pwaDir "*") -Destination $pwaTarget -Recurse -Force

$cli = Join-Path $wildfly "bin\jboss-cli.bat"

if (-not (Test-Path $cli)) {
    throw "WildFly CLI introuvable"
}


# Copier le WAR dans un chemin sans espaces
$stagingDir = "C:\edusecure\deploy"

New-Item -ItemType Directory -Force $stagingDir | Out-Null

$stagedWar = Join-Path $stagingDir "edusecure-api-1.0.0-SNAPSHOT.war"

Copy-Item -LiteralPath $war -Destination $stagedWar -Force

# Verifier que la copie est identique
$stagedHash = (Get-FileHash $stagedWar -Algorithm SHA256).Hash

if ($stagedHash -ne $warHash) {
    throw "Erreur d'integrite pendant la copie du WAR"
}

Write-Host "Integrite de la copie WAR verifiee"

# Deploiement du WAR depuis un chemin simple
$cliWarPath = $stagedWar.Replace('\', '/')

& $cli "--connect" "--controller=127.0.0.1:$managementPort" "--command=deploy $cliWarPath --force"

if ($LASTEXITCODE -ne 0) {
    throw "Echec du deploiement WAR"
}


Write-Host "[5/5] Verification des services"

$apiUrl = "http://api.edusecure.local:$httpPort/api/health"
$pwaUrl = "http://app.edusecure.local:$httpPort/"

& curl.exe --silent --show-error --fail --noproxy "*" $apiUrl

if ($LASTEXITCODE -ne 0) {
    throw "API indisponible"
}

& curl.exe --silent --show-error --fail --noproxy "*" -o NUL $pwaUrl

if ($LASTEXITCODE -ne 0) {
    throw "PWA indisponible"
}

Write-Host ""
Write-Host "DEPLOIEMENT DEPUIS CI REUSSI"
