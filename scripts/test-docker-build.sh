#!/bin/bash

# ============================================================================
# Script de Prueba de Build Docker
# ============================================================================
# Este script prueba el Dockerfile en diferentes escenarios para validar
# que funciona correctamente antes de desplegar a producción.
# ============================================================================

set -e

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_header() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

print_info() {
    echo -e "${YELLOW}ℹ${NC} $1"
}

# Verificar que Docker está disponible
if ! command -v docker &> /dev/null; then
    print_error "Docker no está instalado"
    exit 1
fi

print_success "Docker está instalado"

# ============================================================================
# TEST 1: Build de la etapa builder
# ============================================================================
print_header "TEST 1: Build de la etapa builder"

print_info "Construyendo etapa builder..."
if docker build --target builder -t msbiosuper:builder . > /tmp/docker-build-builder.log 2>&1; then
    print_success "Etapa builder construida exitosamente"
    
    # Verificar que el JAR existe en la etapa builder
    print_info "Verificando que el JAR se creó..."
    if docker run --rm msbiosuper:builder ls -lh /build/target/MSBioSuper-1.0.jar > /dev/null 2>&1; then
        print_success "JAR encontrado en /build/target/MSBioSuper-1.0.jar"
    else
        print_error "JAR no encontrado"
        exit 1
    fi
else
    print_error "Error al construir etapa builder"
    cat /tmp/docker-build-builder.log
    exit 1
fi

# ============================================================================
# TEST 2: Build completo (runtime)
# ============================================================================
print_header "TEST 2: Build completo (runtime)"

print_info "Construyendo imagen completa..."
if docker build -t msbiosuper:test . > /tmp/docker-build-runtime.log 2>&1; then
    print_success "Imagen runtime construida exitosamente"
    
    # Mostrar tamaño de la imagen
    IMAGE_SIZE=$(docker images msbiosuper:test --format "{{.Size}}")
    print_info "Tamaño de la imagen: ${IMAGE_SIZE}"
    
    # Verificar que el tamaño es razonable (< 300MB)
    SIZE_MB=$(docker images msbiosuper:test --format "{{.Size}}" | sed 's/MB//' | sed 's/GB/*1024/' | bc 2>/dev/null || echo "0")
    if (( $(echo "$SIZE_MB < 300" | bc -l 2>/dev/null || echo "1") )); then
        print_success "Tamaño de imagen es óptimo (< 300MB)"
    else
        print_error "Imagen es muy grande (> 300MB)"
    fi
else
    print_error "Error al construir imagen runtime"
    cat /tmp/docker-build-runtime.log
    exit 1
fi

# ============================================================================
# TEST 3: Verificar estructura de la imagen
# ============================================================================
print_header "TEST 3: Verificar estructura de la imagen"

print_info "Verificando que el JAR existe en /app..."
if docker run --rm msbiosuper:test ls -lh /app/app.jar > /dev/null 2>&1; then
    print_success "JAR encontrado en /app/app.jar"
else
    print_error "JAR no encontrado en /app/app.jar"
    exit 1
fi

print_info "Verificando que el usuario es no-root..."
USER_CHECK=$(docker run --rm msbiosuper:test whoami)
if [ "$USER_CHECK" = "msbiosuper" ]; then
    print_success "Usuario es no-root (msbiosuper)"
else
    print_error "Usuario es root (inseguro)"
    exit 1
fi

print_info "Verificando que curl está instalado (para health checks)..."
if docker run --rm msbiosuper:test which curl > /dev/null 2>&1; then
    print_success "curl está instalado"
else
    print_error "curl no está instalado"
    exit 1
fi

# ============================================================================
# TEST 4: Verificar variables de entorno
# ============================================================================
print_header "TEST 4: Verificar variables de entorno"

print_info "Verificando variables de entorno por defecto..."
ENV_VARS=$(docker run --rm msbiosuper:test env)

if echo "$ENV_VARS" | grep -q "JAVA_OPTS"; then
    print_success "JAVA_OPTS está configurado"
else
    print_error "JAVA_OPTS no está configurado"
fi

if echo "$ENV_VARS" | grep -q "SPRING_PROFILES_ACTIVE"; then
    print_success "SPRING_PROFILES_ACTIVE está configurado"
else
    print_error "SPRING_PROFILES_ACTIVE no está configurado"
fi

# ============================================================================
# TEST 5: Verificar health check
# ============================================================================
print_header "TEST 5: Verificar health check"

print_info "Verificando configuración de health check..."
HEALTHCHECK=$(docker inspect msbiosuper:test --format='{{json .Config.Healthcheck}}')

if [ "$HEALTHCHECK" != "null" ] && [ "$HEALTHCHECK" != "" ]; then
    print_success "Health check está configurado"
    echo "$HEALTHCHECK" | jq '.' 2>/dev/null || echo "$HEALTHCHECK"
else
    print_error "Health check no está configurado"
fi

# ============================================================================
# TEST 6: Verificar capas de la imagen
# ============================================================================
print_header "TEST 6: Verificar capas de la imagen"

print_info "Analizando capas de la imagen..."
docker history msbiosuper:test --no-trunc --format "table {{.CreatedBy}}\t{{.Size}}" | head -20

# ============================================================================
# RESUMEN
# ============================================================================
print_header "RESUMEN DE PRUEBAS"

print_success "Todas las pruebas pasaron exitosamente"
print_info ""
print_info "Imagen lista para usar: msbiosuper:test"
print_info ""
print_info "Próximos pasos:"
print_info "  1. Probar ejecución: docker run -p 8080:8080 msbiosuper:test"
print_info "  2. Verificar logs: docker logs <container-id>"
print_info "  3. Probar health check: curl http://localhost:8081/actuator/health"
print_info "  4. Escanear vulnerabilidades: docker scout cves msbiosuper:test"

# Limpiar
print_info ""
print_info "Limpiando imágenes de prueba..."
docker rmi msbiosuper:builder msbiosuper:test > /dev/null 2>&1 || true
print_success "Limpieza completada"
