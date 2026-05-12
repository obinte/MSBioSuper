#!/bin/bash

# ============================================================================
# Script de Build de Imagen Docker para MSBioSuper
# ============================================================================
# Este script construye la imagen Docker de MSBioSuper con las mejores
# prácticas de seguridad y optimización.
#
# Uso:
#   ./scripts/build-docker-image.sh [TAG]
#
# Ejemplo:
#   ./scripts/build-docker-image.sh 1.0.0
#   ./scripts/build-docker-image.sh latest
# ============================================================================

set -e  # Salir si hay algún error

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para imprimir mensajes
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Verificar que Docker está instalado
if ! command -v docker &> /dev/null; then
    print_error "Docker no está instalado. Por favor instala Docker primero."
    exit 1
fi

# Obtener el tag de la imagen (por defecto: latest)
IMAGE_TAG=${1:-latest}
IMAGE_NAME="msbiosuper"
FULL_IMAGE_NAME="${IMAGE_NAME}:${IMAGE_TAG}"

print_info "Construyendo imagen Docker: ${FULL_IMAGE_NAME}"

# Construir la imagen
print_info "Iniciando build multi-etapa..."
docker build \
    --tag "${FULL_IMAGE_NAME}" \
    --build-arg BUILD_DATE="$(date -u +'%Y-%m-%dT%H:%M:%SZ')" \
    --build-arg VCS_REF="$(git rev-parse --short HEAD 2>/dev/null || echo 'unknown')" \
    .

# Verificar que la imagen se creó correctamente
if [ $? -eq 0 ]; then
    print_info "✓ Imagen construida exitosamente: ${FULL_IMAGE_NAME}"
    
    # Mostrar información de la imagen
    print_info "Información de la imagen:"
    docker images "${IMAGE_NAME}" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"
    
    # Verificar el tamaño de la imagen
    IMAGE_SIZE=$(docker images "${FULL_IMAGE_NAME}" --format "{{.Size}}")
    print_info "Tamaño de la imagen: ${IMAGE_SIZE}"
    
    # Advertencia si la imagen es muy grande
    SIZE_MB=$(docker images "${FULL_IMAGE_NAME}" --format "{{.Size}}" | sed 's/MB//' | sed 's/GB/*1024/' | bc 2>/dev/null || echo "0")
    if (( $(echo "$SIZE_MB > 200" | bc -l 2>/dev/null || echo "0") )); then
        print_warning "La imagen es mayor a 200MB. Considera optimizar."
    fi
    
    print_info ""
    print_info "Comandos útiles:"
    print_info "  - Ejecutar contenedor: docker run -p 8080:8080 ${FULL_IMAGE_NAME}"
    print_info "  - Ver logs: docker logs <container-id>"
    print_info "  - Inspeccionar imagen: docker inspect ${FULL_IMAGE_NAME}"
    print_info "  - Escanear vulnerabilidades: docker scan ${FULL_IMAGE_NAME}"
    
else
    print_error "✗ Error al construir la imagen"
    exit 1
fi
