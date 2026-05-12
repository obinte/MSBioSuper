#!/bin/bash

# ============================================================================
# Script de Build con Jib para MSBioSuper
# ============================================================================
# Este script automatiza el proceso de build usando Jib con diferentes perfiles
#
# Uso:
#   ./scripts/build-jib.sh [PERFIL] [IMAGEN] [OPCIONES]
#
# Ejemplos:
#   ./scripts/build-jib.sh dev
#   ./scripts/build-jib.sh prod msbiosuper:v1.0.0
#   ./scripts/build-jib.sh test --push
# ============================================================================

set -e  # Salir en caso de error

# ============================================================================
# CONFIGURACIÓN
# ============================================================================
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
DEFAULT_IMAGE="msbiosuper"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# ============================================================================
# FUNCIONES
# ============================================================================

print_usage() {
    echo "Uso: $0 [PERFIL] [IMAGEN] [OPCIONES]"
    echo ""
    echo "PERFILES disponibles:"
    echo "  dev     - Desarrollo local (imagen local)"
    echo "  test    - Testing con pruebas completas"
    echo "  prod    - Producción optimizada"
    echo ""
    echo "IMAGEN (opcional):"
    echo "  Nombre de la imagen Docker (default: $DEFAULT_IMAGE)"
    echo ""
    echo "OPCIONES:"
    echo "  --push     - Push a registro remoto (solo prod)"
    echo "  --local    - Forzar build local"
    echo "  --help     - Mostrar esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0 dev"
    echo "  $0 prod registry.acosux.com/msbiosuper --push"
    echo "  $0 test --local"
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

check_prerequisites() {
    log_info "Verificando prerequisitos..."
    
    # Verificar Maven
    if ! command -v mvn &> /dev/null; then
        log_error "Maven no está instalado o no está en PATH"
        exit 1
    fi
    
    # Verificar Java
    if ! command -v java &> /dev/null; then
        log_error "Java no está instalado o no está en PATH"
        exit 1
    fi
    
    # Verificar versión de Java
    JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 21 ]; then
        log_error "Se requiere Java 21 o superior. Versión actual: $JAVA_VERSION"
        exit 1
    fi
    
    log_success "Prerequisitos verificados"
}

build_dev() {
    local image_name="$1"
    
    log_info "Construyendo imagen para DESARROLLO..."
    log_info "Perfil: dev + jib-local"
    log_info "Imagen: $image_name-local:local-latest"
    
    # Build con perfil dev y jib-local
    mvn clean package -P dev,jib-local \
        -Djib.to.image="$image_name-local" \
        -Djib.container.environment.SPRING_PROFILES_ACTIVE=dev
    
    log_success "Imagen de desarrollo construida: $image_name-local:local-latest"
    
    # Mostrar información de la imagen
    if command -v docker &> /dev/null; then
        echo ""
        log_info "Información de la imagen:"
        docker images "$image_name-local" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"
    fi
}

build_test() {
    local image_name="$1"
    local use_local="$2"
    
    log_info "Construyendo imagen para TESTING..."
    
    if [ "$use_local" = "true" ]; then
        log_info "Perfil: test + jib-local"
        log_info "Imagen: $image_name-test:test-latest"
        
        # Ejecutar pruebas y build local
        mvn clean verify -P test,jib-local \
            -Djib.to.image="$image_name-test"
    else
        log_info "Perfil: test + jib"
        log_info "Imagen: $image_name:test-latest"
        
        # Ejecutar pruebas y build
        mvn clean verify -P test,jib \
            -Djib.to.image="$image_name"
    fi
    
    log_success "Imagen de testing construida"
}

build_prod() {
    local image_name="$1"
    local push_remote="$2"
    local use_local="$3"
    
    log_info "Construyendo imagen para PRODUCCIÓN..."
    
    if [ "$use_local" = "true" ]; then
        log_info "Perfil: prod + jib-local"
        log_info "Imagen: $image_name-prod:prod-latest"
        
        mvn clean package -P prod,jib-local \
            -Djib.to.image="$image_name-prod"
            
    elif [ "$push_remote" = "true" ]; then
        log_info "Perfil: prod + jib (con push remoto)"
        log_info "Imagen: $image_name"
        
        # Verificar si se puede hacer push
        if [[ "$image_name" == *"localhost"* ]] || [[ "$image_name" == *"127.0.0.1"* ]]; then
            log_warning "Imagen parece ser local, pero se solicitó push remoto"
        fi
        
        mvn clean package -P prod,jib \
            -Djib.to.image="$image_name"
            
    else
        log_info "Perfil: prod + jib-local"
        log_info "Imagen: $image_name-prod:prod-latest"
        
        mvn clean package -P prod,jib-local \
            -Djib.to.image="$image_name-prod"
    fi
    
    log_success "Imagen de producción construida"
}

# ============================================================================
# MAIN
# ============================================================================

main() {
    # Cambiar al directorio del proyecto
    cd "$PROJECT_DIR"
    
    # Parsear argumentos
    PROFILE="${1:-dev}"
    IMAGE_NAME="${2:-$DEFAULT_IMAGE}"
    PUSH_REMOTE=false
    USE_LOCAL=false
    
    # Procesar opciones
    shift 2 2>/dev/null || shift $# 2>/dev/null
    while [[ $# -gt 0 ]]; do
        case $1 in
            --push)
                PUSH_REMOTE=true
                shift
                ;;
            --local)
                USE_LOCAL=true
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
    
    # Mostrar ayuda si no se proporciona perfil válido
    case $PROFILE in
        dev|test|prod)
            ;;
        *)
            log_error "Perfil inválido: $PROFILE"
            print_usage
            exit 1
            ;;
    esac
    
    # Banner
    echo "============================================================================"
    echo "                    MSBioSuper - Build con Jib"
    echo "============================================================================"
    echo "Perfil: $PROFILE"
    echo "Imagen: $IMAGE_NAME"
    echo "Push remoto: $PUSH_REMOTE"
    echo "Forzar local: $USE_LOCAL"
    echo "============================================================================"
    echo ""
    
    # Verificar prerequisitos
    check_prerequisites
    
    # Ejecutar build según perfil
    case $PROFILE in
        dev)
            build_dev "$IMAGE_NAME"
            ;;
        test)
            build_test "$IMAGE_NAME" "$USE_LOCAL"
            ;;
        prod)
            build_prod "$IMAGE_NAME" "$PUSH_REMOTE" "$USE_LOCAL"
            ;;
    esac
    
    echo ""
    log_success "Build completado exitosamente!"
    
    # Mostrar comandos útiles
    echo ""
    echo "============================================================================"
    echo "COMANDOS ÚTILES:"
    echo "============================================================================"
    
    case $PROFILE in
        dev)
            echo "# Ejecutar contenedor:"
            echo "docker run -d --name msbiosuper-dev -p 8080:8080 -p 8081:8081 $IMAGE_NAME-local:local-latest"
            ;;
        test)
            if [ "$USE_LOCAL" = "true" ]; then
                echo "# Ejecutar pruebas en contenedor:"
                echo "docker run --rm $IMAGE_NAME-test:test-latest"
            else
                echo "# Pull y ejecutar imagen de test:"
                echo "docker pull $IMAGE_NAME:test-latest"
                echo "docker run --rm $IMAGE_NAME:test-latest"
            fi
            ;;
        prod)
            if [ "$USE_LOCAL" = "true" ]; then
                echo "# Ejecutar contenedor de producción:"
                echo "docker run -d --name msbiosuper-prod -p 8080:8080 -p 8081:8081 $IMAGE_NAME-prod:prod-latest"
            else
                echo "# Deploy a Kubernetes:"
                echo "kubectl set image deployment/msbiosuper msbiosuper=$IMAGE_NAME:latest"
            fi
            ;;
    esac
    
    echo ""
    echo "# Ver logs:"
    echo "docker logs -f [CONTAINER_NAME]"
    echo ""
    echo "# Health check:"
    echo "curl http://localhost:8081/actuator/health"
    echo "============================================================================"
}

# Ejecutar función main con todos los argumentos
main "$@"