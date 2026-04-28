# Run from repo root or anywhere. Publishes CMP fork from D:\Lenovo then builds Notey.
$ErrorActionPreference = "Stop"
$cmpRoot = "D:\Lenovo\compose-multiplatform"
$components = Join-Path $cmpRoot "components"
$gradlePlugins = Join-Path $cmpRoot "gradle-plugins"
$notey = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path

function Invoke-Gradle {
    param(
        [string]$Dir,
        [string[]]$GradleArgs
    )
    Push-Location $Dir
    try {
        & .\gradlew.bat @GradleArgs
        if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
    } finally {
        Pop-Location
    }
}

Write-Host "== components-resources: publishToMavenLocal =="
Invoke-Gradle -Dir $components -GradleArgs @(":resources:library:publishToMavenLocal", "--no-configuration-cache")

Write-Host "== compose Gradle plugin: publishToMavenLocal =="
Invoke-Gradle -Dir $gradlePlugins -GradleArgs @(":compose:publishToMavenLocal", "--no-configuration-cache")

Write-Host "== Notey: clean assembleDebug =="
Invoke-Gradle -Dir $notey -GradleArgs @(":composeApp:clean", ":composeApp:assembleDebug", "--no-configuration-cache")
