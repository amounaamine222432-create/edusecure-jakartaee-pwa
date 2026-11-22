
$ErrorActionPreference = "Stop"

# Racine du projet
$root = (Resolve-Path (Join-Path $PSScriptRoot "..\..\..")).Path

# Charger la configuration DEV
$configFile = Join-Path $root "deployment\wildfly\dev\edusecure-dev.properties"

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

# Parametres WildFly
$wildfly = "C:\wildfly-42.0.0.Final"
$hostAddress = $config["wildfly.host"]
$managementPort = $config["wildfly.management.port"]
$httpPort = $config["wildfly.http.port"]

# Parametres applications
$apiHost = $config["api.aliases"]
$pwaHost = $config["host.aliases"]
$pwaTarget = $config["host.path"]

# Garde-fous DEV
if ($hostAddress -ne "127.0.0.1") {
    throw "Le deploiement DEV doit rester local"
}

if ($apiHost -ne "api.edusecure.local" -or
    $pwaHost -ne "app.edusecure.local") {
    throw "Domaines DEV inattendus"
}

if ($config["api.context"] -ne "/") {
    throw "Contexte API incorrect"
}

if ($pwaTarget -replace '\\','/' -ne "C:/edusecure/pwa") {
    throw "Chemin PWA DEV inattendu"
}

if ($managementPort -notmatch '^\d+$' -or
    $httpPort -notmatch '^\d+$') {
    throw "Ports invalides"
}

$pwaSource = Join-Path $root "frontend\pwa"

$war = Join-Path $root "backend\edusecure-api\target\edusecure-api-1.0.0-SNAPSHOT.war"

Write-Host "[1/4] Compilation Maven et tests"

& mvn -f (Join-Path $root "backend\pom.xml") -pl edusecure-api -am clean verify

if ($LASTEXITCODE -ne 0) {
    throw "Echec Maven"
}

Write-Host "[2/4] Deploiement PWA"

if (-not (Test-Path $pwaSource)) {
    throw "Dossier PWA introuvable"
}

New-Item -ItemType Directory -Force $pwaTarget | Out-Null

Copy-Item -Path (Join-Path $pwaSource "*") -Destination $pwaTarget -Recurse -Force

Write-Host "[3/4] Deploiement API WildFly"

if (-not (Test-Path $war)) {
    throw "WAR introuvable"
}

Push-Location $root

try {
    & (Join-Path $wildfly "bin\jboss-cli.bat") "--connect" "--controller=$($hostAddress):$managementPort" "--command=deploy backend/edusecure-api/target/edusecure-api-1.0.0-SNAPSHOT.war --force"

    if ($LASTEXITCODE -ne 0) {
        throw "Echec du deploiement WildFly"
    }
}
finally {
    Pop-Location
}

Write-Host "[4/4] Tests HTTP"

$apiUrl = "http://$($apiHost):$httpPort/api/health"
$pwaUrl = "http://$($pwaHost):$httpPort/"

& curl.exe --silent --show-error --fail --noproxy "*" $apiUrl

if ($LASTEXITCODE -ne 0) {
    throw "API indisponible"
}

& curl.exe --silent --show-error --fail --noproxy "*" -o NUL $pwaUrl

if ($LASTEXITCODE -ne 0) {
    throw "PWA indisponible"
}

Write-Host ""
Write-Host "DEPLOIEMENT DEV REUSSI"
