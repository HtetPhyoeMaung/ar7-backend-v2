# PowerShell script to fix Docker proxy issues

Write-Host "Checking Docker Desktop status..." -ForegroundColor Yellow

# Check if Docker is running
try {
    docker ps | Out-Null
    Write-Host "✓ Docker is running" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker is not running. Please start Docker Desktop first." -ForegroundColor Red
    exit 1
}

Write-Host "`nTo fix the proxy/TLS issue:" -ForegroundColor Cyan
Write-Host "1. Open Docker Desktop" -ForegroundColor White
Write-Host "2. Go to Settings (gear icon) → Resources → Proxies" -ForegroundColor White
Write-Host "3. Disable 'Manual proxy configuration' if enabled" -ForegroundColor White
Write-Host "4. Or configure proxy correctly if you need it" -ForegroundColor White
Write-Host "5. Click 'Apply & Restart'" -ForegroundColor White
Write-Host "`nAlternatively, you can:" -ForegroundColor Cyan
Write-Host "- Open Docker Desktop → Settings → Docker Engine" -ForegroundColor White
Write-Host "- Add or modify DNS settings:" -ForegroundColor White
Write-Host '  {"dns": ["8.8.8.8", "8.8.4.4"]}' -ForegroundColor Gray
Write-Host "- Click 'Apply & Restart'" -ForegroundColor White

Write-Host "`nAfter fixing, try:" -ForegroundColor Yellow
Write-Host "  docker pull mysql:8.0" -ForegroundColor Gray
Write-Host "  docker pull redis:7-alpine" -ForegroundColor Gray
Write-Host '  docker-compose up -d --build' -ForegroundColor Gray
