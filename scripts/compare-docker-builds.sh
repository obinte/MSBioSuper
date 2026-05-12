#!/bin/bash

# ============================================================================
# Script de Comparación: Dockerfile vs Jib
# ============================================================================
# Este script compara los builds usando Dockerfile tradicional vs Jib
# y muestra métricas de rendimiento, tamaño y tiempo de construcción
#
# Uso:
#   ./scripts/compare-docker-builds.sh [OPCIONES]
#
# Opciones:
#   --clean    - Limpiar imágenes antes de la comparación
#   --verbose  - Output detallado
# ============================================================================

set -e

# ============================================================================
# CONFIGURACIÓN
# ============================================================================
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# Nombres de imágenes
DOCKERFILE_IMAGE="msbiosuper-dockerfile"
JIB_IMAGE="msbiosuper-jib"

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# Variables de control
CLEAN_IMAGES=false
VERBOSE=false

# ============================================================================
# FUNCIONES
# ============================================================================

print_usage() {
    echo "Uso: $0 [OPCIONES]"
    echo ""
    echo "OPCIONES:"
    echo "  --clean    - Limpiar imágenes antes de la comparación"
    echo "  --verbose  - Output detallado"
    echo "  --help     - Mostrar esta ayuda"
    echo ""
    echo "Este script compara:"
    echo "  - Tiempo de build"
    echo "  - Tamaño de imagen"
    echo "  - Número de capas"
    echo "  - Reproducibilidad"
}

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

log_section() {
    echo -e "${CYAN}============================================================================${NC}"
    echo -e "${CYAN}$1${NC}"
    echo -e "${CYAN}============================================================================${NC}"
}

check_prerequisites() {
    log_info "Verificando prerequisitos..."
    
    # Verificar Docker
    if ! command -v docker &> /dev/null; then
        log_error "Docker no está instalado o no está en PATH"
        exit 1
    fi
    
    # Verificar Maven
    if ! command -v mvn &> /dev/null; then
        log_error "Maven no está instalado o no está en PATH"
        exit 1
    fi
    
    # Verificar que Docker daemon esté corriendo
    if ! docker info &> /dev/null; then
        log_error "Docker daemon no está corriendo"
        exit 1
    fi
    
    log_success "Prerequisitos verificados"
}

clean_images() {
    if [ "$CLEAN_IMAGES" = "true" ]; then
        log_info "Limpiando imágenes existentes..."
        
        # Eliminar imágenes si existen
        docker rmi "$DOCKERFILE_IMAGE:latest" 2>/dev/null || true
        docker rmi "$JIB_IMAGE:latest" 2>/dev/null || true
        docker rmi "msbiosuper-local:local-latest" 2>/dev/null || true
        
        # Limpiar build cache
        docker builder prune -f &> /dev/null || true
        
        log_success "Imágenes limpiadas"
    fi
}

build_with_dockerfile() {
    log_section "BUILD CON DOCKERFILE"
    
    local start_time=$(date +%s)
    
    log_info "Construyendo imagen con Dockerfile..."
    
    if [ "$VERBOSE" = "true" ]; then
        docker build -t "$DOCKERFILE_IMAGE:latest" .
    else
        docker build -t "$DOCKERFILE_IMAGE:latest" . > /dev/null 2>&1
    fi
    
    local end_time=$(date +%s)
    local build_time=$((end_time - start_time))
    
    log_success "Build con Dockerfile completado en ${build_time}s"
    
    echo "$build_time"
}

build_with_jib() {
    log_section "BUILD CON JIB"
    
    local start_time=$(date +%s)
    
    log_info "Construyendo imagen con Jib..."
    
    if [ "$VERBOSE" = "true" ]; then
        mvn jib:dockerBuild -P prod,jib-local -Djib.to.image="$JIB_IMAGE"
    else
        mvn jib:dockerBuild -P prod,jib-local -Djib.to.image="$JIB_IMAGE" > /dev/null 2>&1
    fi
    
    local end_time=$(date +%s)
    local build_time=$((end_time - start_time))
    
    log_success "Build con Jib completado en ${build_time}s"
    
    echo "$build_time"
}

get_image_info() {
    local image="$1"
    local info_type="$2"
    
    case "$info_type" in
        "size")
            docker images "$image:latest" --format "{{.Size}}"
            ;;
        "size_bytes")
            docker inspect "$image:latest" --format='{{.Size}}' 2>/dev/null || echo "0"
            ;;
        "layers")
            docker history "$image:latest" --quiet | wc -l
            ;;
        "created")
            docker inspect "$image:latest" --format='{{.Created}}' 2>/dev/null || echo "N/A"
            ;;
    esac
}

compare_images() {
    log_section "COMPARACIÓN DE IMÁGENES"
    
    # Obtener información de las imágenes
    local dockerfile_size=$(get_image_info "$DOCKERFILE_IMAGE" "size")
    local jib_size=$(get_image_info "$JIB_IMAGE" "size")
    
    local dockerfile_size_bytes=$(get_image_info "$DOCKERFILE_IMAGE" "size_bytes")
    local jib_size_bytes=$(get_image_info "$JIB_IMAGE" "size_bytes")
    
    local dockerfile_layers=$(get_image_info "$DOCKERFILE_IMAGE" "layers")
    local jib_layers=$(get_image_info "$JIB_IMAGE" "layers")
    
    # Mostrar tabla comparativa
    printf "%-20s %-20s %-20s\n" "MÉTRICA" "DOCKERFILE" "JIB"
    printf "%-20s %-20s %-20s\n" "--------------------" "--------------------" "--------------------"
    printf "%-20s %-20s %-20s\n" "Tamaño" "$dockerfile_size" "$jib_size"
    printf "%-20s %-20s %-20s\n" "Número de capas" "$dockerfile_layers" "$jib_layers"
    
    # Calcular diferencia de tamaño
    if [ "$dockerfile_size_bytes" -gt 0 ] && [ "$jib_size_bytes" -gt 0 ]; then
        local size_diff=$((jib_size_bytes - dockerfile_size_bytes))
        local size_diff_percent=$(( (size_diff * 100) / dockerfile_size_bytes ))
        
        echo ""
        if [ "$size_diff" -gt 0 ]; then
            log_warning "Jib es $(numfmt --to=iec $size_diff) (${size_diff_percent}%) más grande"
        elif [ "$size_diff" -lt 0 ]; then
            local abs_diff=$((-size_diff))
            local abs_percent=$((-size_diff_percent))
            log_success "Jib es $(numfmt --to=iec $abs_diff) (${abs_percent}%) más pequeño"
        else
            log_info "Ambas imágenes tienen el mismo tamaño"
        fi
    fi
}

test_images() {
    log_section "PRUEBAS DE FUNCIONALIDAD"
    
    log_info "Probando imagen Dockerfile..."
    
    # Probar imagen Dockerfile
    local dockerfile_container="test-dockerfile-$$"
    docker run -d --name "$dockerfile_container" -p 18080:8080 -p 18081:8081 \
        -e SPRING_PROFILES_ACTIVE=prod \
        -e JDBC_SERVER=localhost \
        -e JDBC_PORT=5432 \
        -e JDBC_DATABASE=test \
        -e JDBC_USER=test \
        -e JDBC_PASSWORD=test \
        -e JWT_SECRET=dGVzdC1zZWNyZXQtZm9yLWp3dC10b2tlbi1zaWduaW5nLXRoaXMtaXMtYS10ZXN0LXNlY3JldA== \
        "$DOCKERFILE_IMAGE:latest" > /dev/null
    
    # Esperar a que inicie
    sleep 10
    
    # Verificar health check
    local dockerfile_health="FAIL"
    if curl -f http://localhost:18081/actuator/health/liveness &> /dev/null; then
        dockerfile_health="OK"
    fi
    
    # Limpiar contenedor
    docker stop "$dockerfile_container" > /dev/null 2>&1 || true
    docker rm "$dockerfile_container" > /dev/null 2>&1 || true
    
    log_info "Probando imagen Jib..."
    
    # Probar imagen Jib
    local jib_container="test-jib-$$"
    docker run -d --name "$jib_container" -p 19080:8080 -p 19081:8081 \
        -e SPRING_PROFILES_ACTIVE=prod \
        -e JDBC_SERVER=localhost \
        -e JDBC_PORT=5432 \
        -e JDBC_DATABASE=test \
        -e JDBC_USER=test \
        -e JDBC_PASSWORD=test \
        -e JWT_SECRET=dGVzdC1zZWNyZXQtZm9yLWp3dC10b2tlbi1zaWduaW5nLXRoaXMtaXMtYS10ZXN0LXNlY3JldA== \
        "$JIB_IMAGE:latest" > /dev/null
    
    # Esperar a que inicie
    sleep 10
    
    # Verificar health check
    local jib_health="FAIL"
    if curl -f http://localhost:19081/actuator/health/liveness &> /dev/null; then
        jib_health="OK"
    fi
    
    # Limpiar contenedor
    docker stop "$jib_container" > /dev/null 2>&1 || true
    docker rm "$jib_container" > /dev/null 2>&1 || true
    
    # Mostrar resultados
    printf "%-20s %-20s %-20s\n" "PRUEBA" "DOCKERFILE" "JIB"
    printf "%-20s %-20s %-20s\n" "--------------------" "--------------------" "--------------------"
    printf "%-20s %-20s %-20s\n" "Health Check" "$dockerfile_health" "$jib_health"
}

show_layer_analysis() {
    log_section "ANÁLISIS DE CAPAS"
    
    echo "DOCKERFILE - Historial de capas:"
    docker history "$DOCKERFILE_IMAGE:latest" --human --format "table {{.CreatedBy}}\t{{.Size}}"
    
    echo ""
    echo "JIB - Historial de capas:"
    docker history "$JIB_IMAGE:latest" --human --format "table {{.CreatedBy}}\t{{.Size}}"
}

generate_report() {
    local dockerfile_time="$1"
    local jib_time="$2"
    
    log_section "REPORTE FINAL"
    
    echo "RESUMEN DE COMPARACIÓN:"
    echo "======================="
    
    printf "%-25s %-15s %-15s\n" "MÉTRICA" "DOCKERFILE" "JIB"
    printf "%-25s %-15s %-15s\n" "-------------------------" "---------------" "---------------"
    printf "%-25s %-15s %-15s\n" "Tiempo de build" "${dockerfile_time}s" "${jib_time}s"
    
    # Determinar ganador de tiempo
    if [ "$dockerfile_time" -lt "$jib_time" ]; then
        local time_diff=$((jib_time - dockerfile_time))
        echo ""
        log_info "Dockerfile es ${time_diff}s más rápido en build"
    elif [ "$jib_time" -lt "$dockerfile_time" ]; then
        local time_diff=$((dockerfile_time - jib_time))
        echo ""
        log_success "Jib es ${time_diff}s más rápido en build"
    else
        echo ""
        log_info "Ambos métodos tienen el mismo tiempo de build"
    fi
    
    echo ""
    echo "RECOMENDACIONES:"
    echo "================"
    echo "• Dockerfile: Mejor para configuraciones complejas y control total"
    echo "• Jib: Mejor para builds reproducibles y optimización automática"
    echo "• Para desarrollo: Usar Jib con perfil dev"
    echo "• Para producción: Ambos son válidos, Jib ofrece mejor caché"
    echo "• Para CI/CD: Jib es más rápido en builds incrementales"
}

# ============================================================================
# MAIN
# ============================================================================

main() {
    # Parsear argumentos
    while [[ $# -gt 0 ]]; do
        case $1 in
            --clean)
                CLEAN_IMAGES=true
                shift
                ;;
            --verbose)
                VERBOSE=true
                shift
                ;;
            --help)
                print_usage
                exit 0
                ;;
            *)
                log_error "Opción desconocida: $1"
                print_usage
                exit 1
                ;;
        esac
    done
    
    # Cambiar al directorio del proyecto
    cd "$PROJECT_DIR"
    
    # Banner
    log_section "COMPARACIÓN: DOCKERFILE VS JIB"
    echo "Proyecto: MSBioSuper"
    echo "Fecha: $(date)"
    echo ""
    
    # Verificar prerequisitos
    check_prerequisites
    
    # Limpiar imágenes si se solicita
    clean_images
    
    # Realizar builds y medir tiempo
    dockerfile_time=$(build_with_dockerfile)
    jib_time=$(build_with_jib)
    
    # Comparar imágenes
    compare_images
    
    # Probar funcionalidad
    test_images
    
    # Mostrar análisis de capas si es verbose
    if [ "$VERBOSE" = "true" ]; then
        show_layer_analysis
    fi
    
    # Generar reporte final
    generate_report "$dockerfile_time" "$jib_time"
    
    log_success "Comparación completada!"
}

# Ejecutar función main con todos los argumentos
main "$@"