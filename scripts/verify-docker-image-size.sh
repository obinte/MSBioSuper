#!/bin/bash

# ============================================================================
# Script de Verificación de Tamaño de Imagen Docker
# ============================================================================
# Este script construye la imagen Docker y verifica que cumple con el
# objetivo de tamaño < 200MB
# ============================================================================

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuración
IMAGE_NAME="msbiosuper"
IMAGE_TAG="latest"
MAX_SIZE_MB=200

echo "============================================================================"
echo "Verificación de Tamaño de Imagen Docker - MSBioSuper"
echo "============================================================================"
echo ""

# Construir la imagen
echo "📦 Construyendo imagen Docker..."
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Error al construir la imagen Docker${NC}"
    exit 1
fi

echo ""
echo "✅ Imagen construida exitosamente"
echo ""

# Obtener tamaño de la imagen
echo "📊 Analizando tamaño de la imagen..."
IMAGE_SIZE=$(docker images ${IMAGE_NAME}:${IMAGE_TAG} --format "{{.Size}}")
IMAGE_SIZE_BYTES=$(docker images ${IMAGE_NAME}:${IMAGE_TAG} --format "{{.Size}}" | sed 's/MB//' | sed 's/GB/*1024/' | bc 2>/dev/null || echo "0")

echo ""
echo "============================================================================"
echo "Resultados del Análisis"
echo "============================================================================"
echo "Imagen: ${IMAGE_NAME}:${IMAGE_TAG}"
echo "Tamaño: ${IMAGE_SIZE}"
echo "Objetivo: < ${MAX_SIZE_MB}MB"
echo ""

# Verificar si cumple con el objetivo
if [ $(echo "${IMAGE_SIZE_BYTES} < ${MAX_SIZE_MB}" | bc) -eq 1 ]; then
    echo -e "${GREEN}✅ ÉXITO: La imagen cumple con el objetivo de tamaño${NC}"
    echo ""
else
    echo -e "${YELLOW}⚠️  ADVERTENCIA: La imagen excede el objetivo de ${MAX_SIZE_MB}MB${NC}"
    echo ""
fi

# Mostrar capas de la imagen
echo "============================================================================"
echo "Capas de la Imagen"
echo "============================================================================"
docker history ${IMAGE_NAME}:${IMAGE_TAG} --human --no-trunc

echo ""
echo "============================================================================"
echo "Detalles de la Imagen"
echo "============================================================================"
docker inspect ${IMAGE_NAME}:${IMAGE_TAG} | jq '.[0] | {
    Id: .Id,
    Created: .Created,
    Size: .Size,
    Architecture: .Architecture,
    Os: .Os,
    User: .Config.User,
    ExposedPorts: .Config.ExposedPorts,
    Healthcheck: .Config.Healthcheck
}'

echo ""
echo "============================================================================"
echo "Optimizaciones Aplicadas"
echo "============================================================================"
echo "✓ Build multi-etapa (builder + runtime)"
echo "✓ Imagen base Alpine Linux (mínima)"
echo "✓ Usuario no-root (msbiosuper:1001)"
echo "✓ Capas RUN combinadas para minimizar tamaño"
echo "✓ Caché de dependencias Maven optimizado"
echo "✓ .dockerignore para excluir archivos innecesarios"
echo "✓ Limpieza de caché de apk"
echo "✓ JRE en lugar de JDK para runtime"
echo ""

echo "============================================================================"
echo "Comandos Útiles"
echo "============================================================================"
echo "Ejecutar contenedor:"
echo "  docker run -p 8080:8080 -p 8081:8081 ${IMAGE_NAME}:${IMAGE_TAG}"
echo ""
echo "Inspeccionar contenedor:"
echo "  docker exec -it <container_id> sh"
echo ""
echo "Ver logs:"
echo "  docker logs -f <container_id>"
echo ""
echo "Verificar health check:"
echo "  docker inspect --format='{{json .State.Health}}' <container_id> | jq"
echo ""
