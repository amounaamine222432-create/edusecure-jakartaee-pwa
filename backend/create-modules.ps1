
# EduSecure - Creation des POM Maven enfants
$ErrorActionPreference = "Stop"

$modules = @(
    "edusecure-common",
    "edusecure-crypto",
    "edusecure-watermark",
    "edusecure-fingerprint",
    "edusecure-steganography",
    "edusecure-persistence",
    "edusecure-security",
    "edusecure-api"
)

foreach ($module in $modules) {

    $pomPath = Join-Path $module "pom.xml"

    if (Test-Path $pomPath) {
        Write-Host "EXISTE : $pomPath - aucune modification"
        continue
    }

    $packaging = if ($module -eq "edusecure-api") {
        "war"
    } else {
        "jar"
    }

    $content = @"
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>tn.supcom.edusecure</groupId>
        <artifactId>edusecure-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>$module</artifactId>
    <packaging>$packaging</packaging>

    <name>EduSecure - $module</name>

</project>
"@

    $utf8 = [System.Text.UTF8Encoding]::new($false)
    $fullPath = Join-Path (Get-Location).Path $pomPath
    [System.IO.File]::WriteAllText($fullPath, $content, $utf8)

    Write-Host "CREE : $pomPath"
}

Write-Host "Creation des modules terminee."
