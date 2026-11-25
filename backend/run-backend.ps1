
<#
Run-backend.ps1
Keeps the Java backend running; restarts automatically if it exits.
Usage: Right-click -> Run with PowerShell, or run from a PowerShell console.
#>
$ErrorActionPreference = 'Stop'
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $scriptDir

if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Error "Java not found in PATH. Please install Java and ensure 'java' is available in PATH."
    exit 1
}

# If backend responds to /api/health, assume it's already running and keep script alive
function Test-BackendAlive() {
    try {
        $resp = Invoke-RestMethod -Uri "http://localhost:8080/api/health" -ErrorAction Stop
        if ($null -ne $resp -and $resp.status -eq 'ok') { return $true }
        return $false
    } catch {
        return $false
    }
}

if (Test-BackendAlive) {
    Write-Host "Backend health OK on http://localhost:8080/api/health — assuming running. Keeping script alive."
    while ($true) { Start-Sleep -Seconds 60 }
}

while ($true) {
    Write-Host "Starting Multi-Agent backend at $(Get-Date) ..."
    & java -cp "$scriptDir\bin" app.Main
    $exitCode = $LASTEXITCODE
    Write-Warning "Backend process exited with code $exitCode. Restarting in 2 seconds..."
    Start-Sleep -Seconds 2
}
