
$ErrorActionPreference = "Stop"

$root = (Resolve-Path (Join-Path $PSScriptRoot "..\..\..")).Path
$wildfly = "C:\wildfly-42.0.0.Final"
$pwaSource = Join-Path $root "frontend\pwa"
$pwaTarget = "C:\edusecure\pwa"
$war = Join-Path $root "backend\edusecure-api\target\edusecure-api-1.0.0-SNAPSHOT.war"

Write-Host "[1/4] Compilation et tests Maven"
& mvn -f (Join-Path $root "backend\pom.xml") -pl edusecure-api -am clean verify
if ($LASTEXITCODE -ne 0) { throw "Echec Maven" }

Write-Host "[2/4] Mise a jour des fichiers PWA"
if (-not (Test-Path $pwaSource)) { throw "Source PWA introuvable" }
New-Item -ItemType Directory -Force $pwaTarget | Out-Null
Copy-Item -Path (Join-Path $pwaSource "*") -Destination $pwaTarget -Recurse -Force

Write-Host "[3/4] Deploiement WildFly"
if (-not (Test-Path $war)) { throw "WAR introuvable" }

Push-Location $root
try {
    & (Join-Path $wildfly "bin\jboss-cli.bat") "--connect" "--controller=127.0.0.1:9990" "--command=deploy backend/edusecure-api/target/edusecure-api-1.0.0-SNAPSHOT.war --force"
    if ($LASTEXITCODE -ne 0) { throw "Echec du deploiement WildFly" }
}
finally {
    Pop-Location
}

Write-Host "[4/4] Tests HTTP"

& curl.exe --silent --show-error --fail --noproxy "*" "http://api.edusecure.local:8180/api/health"
if ($LASTEXITCODE -ne 0) { throw "API indisponible" }

& curl.exe --silent --show-error --fail --noproxy "*" -o NUL "http://app.edusecure.local:8180/"
if ($LASTEXITCODE -ne 0) { throw "PWA indisponible" }

Write-Host "DEPLOIEMENT DEV REUSSI"
