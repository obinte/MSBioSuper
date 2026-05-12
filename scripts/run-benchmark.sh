#!/bin/bash

###############################################################################
# Script de Ejecución de Benchmarks de Rendimiento - MSBioSuper
#
# Este script ejecuta benchmarks de rendimiento y recopila métricas del sistema
# para establecer líneas base de rendimiento.
#
# Uso:
#   ./scripts/run-benchmark.sh [opciones]
#
# Opciones:
#   --users <número>      Número de usuarios concurrentes (default: 50)
#   --duration <segundos> Duración del benchmark en segundos (default: 300)
#   --token <jwt>         Token JWT para autenticación
#   --base-url <url>      URL base de la API (default: http://localhost:8080)
#   --collect-metrics     Recopilar métricas del sistema durante la prueba
#   --help                Mostrar esta ayuda
#
# Ejemplos:
#   ./scripts/run-benchmark.sh
#   ./scripts/run-benchmark.sh --users 100 --duration 600
#   ./scripts/run-benchmark.sh --collect-metrics --token "eyJhbGc..."
#
###############################################################################

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuración por defecto
USERS=50
DURATION=300
BASE_URL="http://localhost:8080"
JWT_TOKEN=""
COLLECT_METRICS=false
FECHA_INICIO="2024-01-01"
FECHA_FIN="2024-12-31"
EMPRESA_CODIGO="001"

# Función para mostrar ayuda
show_help() {
    head -n 30 "$0" | grep "^#" | sed 's/^# //' | sed 's/^#//'
    exit 0
}

# Función para logging
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Parsear argumentos
while [[ $# -gt 0 ]]; do
    case $1 in
        --users)
            USERS="$2"
            shift 2
            ;;
        --duration)
            DURATION="$2"
            shift 2
            ;;
        --token)
            JWT_TOKEN="$2"
            shift 2
            ;;
        --base-url)
            BASE_URL="$2"
            shift 2
            ;;
        --collect-metrics)
            COLLECT_METRICS=true
            shift
            ;;
        --help)
            show_help
            ;;
        *)
            log_error "Opción desconocida: $1"
            show_help
            ;;
    esac
done

# Banner
echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         Benchmark de Rendimiento - MSBioSuper Java 21         ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Verificar que la aplicación está corriendo
log_info "Verificando que la aplicación está disponible..."
if ! curl -s -f "${BASE_URL}/actuator/health" > /dev/null 2>&1; then
    log_error "La aplicación no está disponible en ${BASE_URL}"
    log_error "Por favor, inicie la aplicación antes de ejecutar el benchmark"
    exit 1
fi
log_success "Aplicación disponible"

# Generar token JWT si no se proporcionó
if [ -z "$JWT_TOKEN" ]; then
    log_warning "No se proporcionó token JWT"
    log_info "Intentando generar token JWT..."
    
    # Intentar autenticación (ajustar credenciales según sea necesario)
    AUTH_RESPONSE=$(curl -s -X POST "${BASE_URL}/api/v1/security/authenticate" \
        -H "Content-Type: application/json" \
        -d '{"username": "testuser", "password": "testpass123"}' 2>/dev/null || echo "")
    
    if [ -n "$AUTH_RESPONSE" ]; then
        JWT_TOKEN=$(echo "$AUTH_RESPONSE" | grep -o '"token":"[^"]*' | sed 's/"token":"//')
        if [ -n "$JWT_TOKEN" ]; then
            log_success "Token JWT generado exitosamente"
        else
            log_error "No se pudo extraer el token de la respuesta"
            log_error "Por favor, proporcione un token JWT válido con --token"
            exit 1
        fi
    else
        log_error "No se pudo autenticar"
        log_error "Por favor, proporcione un token JWT válido con --token"
        exit 1
    fi
fi

# Mostrar configuración
echo ""
log_info "Configuración del Benchmark:"
echo "  - Usuarios concurrentes: ${USERS}"
echo "  - Duración: ${DURATION} segundos ($(($DURATION / 60)) minutos)"
echo "  - URL base: ${BASE_URL}"
echo "  - Recopilar métricas: ${COLLECT_METRICS}"
echo "  - Fecha inicio: ${FECHA_INICIO}"
echo "  - Fecha fin: ${FECHA_FIN}"
echo "  - Empresa código: ${EMPRESA_CODIGO}"
echo ""

# Crear directorio para resultados
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
RESULTS_DIR="benchmark-results/${TIMESTAMP}"
mkdir -p "${RESULTS_DIR}"

log_info "Los resultados se guardarán en: ${RESULTS_DIR}"

# Función para recopilar métricas del sistema
collect_system_metrics() {
    local output_file="$1"
    
    while true; do
        TIMESTAMP=$(date +%Y-%m-%d\ %H:%M:%S)
        
        # Métricas de Actuator
        HEAP_USED=$(curl -s "${BASE_URL}/actuator/metrics/jvm.memory.used?tag=area:heap" | grep -o '"value":[0-9.]*' | head -1 | cut -d: -f2)
        HEAP_MAX=$(curl -s "${BASE_URL}/actuator/metrics/jvm.memory.max?tag=area:heap" | grep -o '"value":[0-9.]*' | head -1 | cut -d: -f2)
        THREADS=$(curl -s "${BASE_URL}/actuator/metrics/jvm.threads.live" | grep -o '"value":[0-9.]*' | cut -d: -f2)
        CONNECTIONS=$(curl -s "${BASE_URL}/actuator/metrics/hikaricp.connections.active" | grep -o '"value":[0-9.]*' | cut -d: -f2)
        
        # Convertir bytes a MB
        HEAP_USED_MB=$(echo "scale=2; ${HEAP_USED:-0} / 1048576" | bc)
        HEAP_MAX_MB=$(echo "scale=2; ${HEAP_MAX:-0} / 1048576" | bc)
        
        # CPU (si está disponible)
        CPU_USAGE=$(top -bn1 | grep "Cpu(s)" | sed "s/.*, *\([0-9.]*\)%* id.*/\1/" | awk '{print 100 - $1}' 2>/dev/null || echo "N/A")
        
        echo "${TIMESTAMP},${HEAP_USED_MB},${HEAP_MAX_MB},${THREADS},${CONNECTIONS},${CPU_USAGE}" >> "${output_file}"
        
        sleep 5
    done
}

# Iniciar recopilación de métricas si está habilitado
if [ "$COLLECT_METRICS" = true ]; then
    log_info "Iniciando recopilación de métricas del sistema..."
    METRICS_FILE="${RESULTS_DIR}/system-metrics.csv"
    echo "timestamp,heap_used_mb,heap_max_mb,threads,db_connections,cpu_usage" > "${METRICS_FILE}"
    collect_system_metrics "${METRICS_FILE}" &
    METRICS_PID=$!
    log_success "Recopilación de métricas iniciada (PID: ${METRICS_PID})"
fi

# Guardar métricas iniciales
log_info "Recopilando métricas iniciales..."
curl -s "${BASE_URL}/actuator/health" > "${RESULTS_DIR}/health-before.json"
curl -s "${BASE_URL}/actuator/metrics" > "${RESULTS_DIR}/metrics-before.json"

# Ejecutar benchmark
log_info "Ejecutando benchmark de Gatling..."
echo ""

mvn gatling:test \
    -Dgatling.simulationClass=msbiosuper.BenchmarkSimulation \
    -Dbenchmark.users="${USERS}" \
    -Dbenchmark.duration="${DURATION}" \
    -DbaseUrl="${BASE_URL}" \
    -Dtest.jwt.token="${JWT_TOKEN}" \
    -Dtest.fechaInicio="${FECHA_INICIO}" \
    -Dtest.fechaFin="${FECHA_FIN}" \
    -Dtest.empresaCodigo="${EMPRESA_CODIGO}" \
    2>&1 | tee "${RESULTS_DIR}/benchmark-output.log"

BENCHMARK_EXIT_CODE=${PIPESTATUS[0]}

# Detener recopilación de métricas
if [ "$COLLECT_METRICS" = true ]; then
    log_info "Deteniendo recopilación de métricas..."
    kill ${METRICS_PID} 2>/dev/null || true
    wait ${METRICS_PID} 2>/dev/null || true
    log_success "Recopilación de métricas detenida"
fi

# Guardar métricas finales
log_info "Recopilando métricas finales..."
curl -s "${BASE_URL}/actuator/health" > "${RESULTS_DIR}/health-after.json"
curl -s "${BASE_URL}/actuator/metrics" > "${RESULTS_DIR}/metrics-after.json"

# Recopilar métricas específicas
log_info "Recopilando métricas detalladas..."

# Memoria JVM
curl -s "${BASE_URL}/actuator/metrics/jvm.memory.used" > "${RESULTS_DIR}/jvm-memory-used.json"
curl -s "${BASE_URL}/actuator/metrics/jvm.memory.max" > "${RESULTS_DIR}/jvm-memory-max.json"

# Threads
curl -s "${BASE_URL}/actuator/metrics/jvm.threads.live" > "${RESULTS_DIR}/jvm-threads-live.json"
curl -s "${BASE_URL}/actuator/metrics/jvm.threads.peak" > "${RESULTS_DIR}/jvm-threads-peak.json"

# HikariCP
curl -s "${BASE_URL}/actuator/metrics/hikaricp.connections.active" > "${RESULTS_DIR}/hikaricp-connections-active.json"
curl -s "${BASE_URL}/actuator/metrics/hikaricp.connections.max" > "${RESULTS_DIR}/hikaricp-connections-max.json"

# HTTP requests
curl -s "${BASE_URL}/actuator/metrics/http.server.requests" > "${RESULTS_DIR}/http-server-requests.json"

# Buscar el reporte HTML de Gatling más reciente
GATLING_REPORT=$(find target/gatling -name "index.html" -type f -printf '%T@ %p\n' | sort -n | tail -1 | cut -f2- -d" ")

if [ -n "$GATLING_REPORT" ]; then
    GATLING_DIR=$(dirname "$GATLING_REPORT")
    log_success "Reporte de Gatling generado: ${GATLING_REPORT}"
    
    # Copiar reporte a directorio de resultados
    cp -r "${GATLING_DIR}" "${RESULTS_DIR}/gatling-report"
    log_success "Reporte copiado a: ${RESULTS_DIR}/gatling-report"
fi

# Generar resumen
log_info "Generando resumen de resultados..."

cat > "${RESULTS_DIR}/README.md" << EOF
# Resultados del Benchmark - ${TIMESTAMP}

## Configuración

- **Fecha de Ejecución**: $(date +"%Y-%m-%d %H:%M:%S")
- **Usuarios Concurrentes**: ${USERS}
- **Duración**: ${DURATION} segundos ($(($DURATION / 60)) minutos)
- **URL Base**: ${BASE_URL}
- **Rango de Fechas**: ${FECHA_INICIO} a ${FECHA_FIN}
- **Empresa**: ${EMPRESA_CODIGO}

## Archivos Generados

- \`benchmark-output.log\`: Log completo de la ejecución
- \`gatling-report/index.html\`: Reporte interactivo de Gatling
- \`health-before.json\`: Estado de salud antes del benchmark
- \`health-after.json\`: Estado de salud después del benchmark
- \`metrics-before.json\`: Métricas antes del benchmark
- \`metrics-after.json\`: Métricas después del benchmark
- \`system-metrics.csv\`: Métricas del sistema durante la ejecución (si se habilitó)
- Métricas específicas: \`jvm-*.json\`, \`hikaricp-*.json\`, \`http-*.json\`

## Cómo Ver los Resultados

1. Abrir el reporte de Gatling:
   \`\`\`bash
   open ${RESULTS_DIR}/gatling-report/index.html
   \`\`\`

2. Revisar métricas del sistema:
   \`\`\`bash
   cat ${RESULTS_DIR}/system-metrics.csv
   \`\`\`

3. Comparar métricas antes/después:
   \`\`\`bash
   diff ${RESULTS_DIR}/metrics-before.json ${RESULTS_DIR}/metrics-after.json
   \`\`\`

## Próximos Pasos

1. Revisar el reporte de Gatling para identificar cuellos de botella
2. Analizar las métricas del sistema para detectar problemas de recursos
3. Actualizar el documento \`docs/BENCHMARK-RESULTS.md\` con los resultados
4. Comparar con benchmarks anteriores para detectar regresiones
5. Implementar optimizaciones basadas en los hallazgos

EOF

echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                    Benchmark Completado                        ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

if [ ${BENCHMARK_EXIT_CODE} -eq 0 ]; then
    log_success "Benchmark ejecutado exitosamente"
else
    log_error "Benchmark falló con código de salida: ${BENCHMARK_EXIT_CODE}"
fi

log_info "Resultados guardados en: ${RESULTS_DIR}"

if [ -n "$GATLING_REPORT" ]; then
    echo ""
    log_info "Para ver el reporte de Gatling, ejecute:"
    echo "  open ${RESULTS_DIR}/gatling-report/index.html"
fi

echo ""
log_info "Para actualizar el documento de resultados:"
echo "  vim docs/BENCHMARK-RESULTS.md"

echo ""
log_info "Resumen de archivos generados:"
ls -lh "${RESULTS_DIR}"

exit ${BENCHMARK_EXIT_CODE}
