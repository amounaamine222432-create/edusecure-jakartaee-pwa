
$ErrorActionPreference = "Stop"

$secret = [Environment]::GetEnvironmentVariable(
    "EDUSECURE_CS_MASTER", "User"
)

if ([string]::IsNullOrWhiteSpace($secret)) {
    throw "Secret maitre EduSecure absent"
}

if ([Convert]::FromBase64String($secret).Length -ne 32) {
    throw "Secret maitre EduSecure invalide"
}

$env:EDUSECURE_CS_MASTER = $secret

$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-25.0.5.21-hotspot"

$wildfly = "C:\wildfly-42.0.0.Final"

Write-Host "Java 25 configure"
Write-Host "Secret Elytron disponible pour WildFly"
Write-Host "Demarrage WildFly DEV..."

& "$wildfly\bin\standalone.bat" "-Djboss.http.port=8180"

if ($LASTEXITCODE -ne 0) {
    throw "WildFly s'est termine avec une erreur"
}
