# Cargar variables del archivo .env a la sesión actual de PowerShell
if (Test-Path .env) {
    Get-Content .env | Where-Object { $_ -notmatch "^#" -and $_.Trim() -ne "" } | ForEach-Object {
        $key, $value = $_ -split '=', 2
        $key = $key.Trim()
        $value = $value.Trim()
        if ($value -match '^"(.*)"$') { $value = $matches[1] }
        if ($value -match "^'(.*)'$") { $value = $matches[1] }
        [System.Environment]::SetEnvironmentVariable($key, $value, "Process")
    }
    Write-Host "Variables de entorno cargadas desde .env" -ForegroundColor Green
} else {
    Write-Host "Archivo .env no encontrado" -ForegroundColor Red
}

# Configurar JAVA_HOME si es necesario (ajustar ruta según tu instalación)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

# Ejecutar Spring Boot
.\mvnw spring-boot:run