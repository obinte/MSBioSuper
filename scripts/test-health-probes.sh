#!/bin/bash

# ============================================
# SCRIPT DE VERIFICACIÓN DE HEALTH CHECKS
# MSBioSuper Java 21 Migration
# ============================================
# Este script verifica que los health checks y métricas
# estén funcionando correctamente.

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuración
APP_URL=${APP_URL:-"http://localhost:8080"}
TIMEOUT=${TIMEOUT:-30}

print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "\n${BLUE}============================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}============================================${NC}\n"
}

# Función para verificar si la aplicación está ejecutándose
check_app_running() {
    print_status "Verificando si la aplicación está ejecutándose..."
    
    if curl -s -f "$APP_URL/actuator/health" > /dev/null 2>&1; then
        print_success "✓ Aplicación está ejecutándose en $APP_URL"
        return 0
    else
        print_error "✗ Aplicación no está ejecutándose en $APP_URL"
        print_status "Para ejecutar la aplicación:"
        print_status "mvn spring-boot:run -Dspring-boot.run.profiles=prod"
        return 1
    fi
}

# Función para verificar health check general
check_health_general() {
    print_status "Verificando health check general..."
    
    local response=$(curl -s "$APP_URL/actuator/health" 2>/dev/null)
    local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    
    if [ "$status" = "UP" ]; then
        print_success "✓ Health check general: UP"
        return 0
    else
        print_error "✗ Health check general: $status"
        echo "$response" | jq . 2>/dev/null || echo "$response"
        return 1
    fi
}

# Función para verificar liveness probe
check_liveness() {
    print_status "Verificando liveness probe..."
    
    local response=$(curl -s "$APP_URL/actuator/health/liveness" 2>/dev/null)
    local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    
    if [ "$status" = "UP" ]; then
        print_success "✓ Liveness probe: UP"
        return 0
    else
        print_error "✗ Liveness probe: $status"
        echo "$response" | jq . 2>/dev/null || echo "$response"
        return 1
    fi
}

# Función para verificar readiness probe
check_readiness() {
    print_status "Verificando readiness probe..."
    
    local response=$(curl -s "$APP_URL/actuator/health/readiness" 2>/dev/null)
    local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    
    if [ "$status" = "UP" ]; then
        print_success "✓ Readiness probe: UP"
        return 0
    else
        print_warning "⚠ Readiness probe: $status (puede ser normal si la BD no está disponible)"
        echo "$response" | jq . 2>/dev/null || echo "$response"
        return 1
    fi
}

# Función para verificar métricas
check_metrics() {
    print_status "Verificando endpoint de métricas..."
    
    if curl -s -f "$APP_URL/actuator/metrics" > /dev/null 2>&1; then
        print_success "✓ Endpoint de métricas disponible"
        
        # Verificar algunas métricas específicas
        local jvm_memory=$(curl -s "$APP_URL/actuator/metrics/jvm.memory.used" 2>/dev/null)
        if echo "$jvm_memory" | grep -q "measurements"; then
            print_success "  ✓ Métricas JVM disponibles"
        fi
        
        local http_requests=$(curl -s "$APP_URL/actuator/metrics/http.server.requests" 2>/dev/null)
        if echo "$http_requests" | grep -q "measurements"; then
            print_success "  ✓ Métricas HTTP disponibles"
        fi
        
        return 0
    else
        print_error "✗ Endpoint de métricas no disponible"
        return 1
    fi
}

# Función para verificar Prometheus
check_prometheus() {
    print_status "Verificando endpoint de Prometheus..."
    
    local response=$(curl -s "$APP_URL/actuator/prometheus" 2>/dev/null)
    
    if echo "$response" | grep -q "jvm_memory_used_bytes"; then
        print_success "✓ Métricas Prometheus disponibles"
        
        # Contar métricas disponibles
        local metric_count=$(echo "$response" | grep -c "^[a-zA-Z]" || echo "0")
        print_status "  Métricas disponibles: $metric_count"
        
        return 0
    else
        print_error "✗ Métricas Prometheus no disponibles"
        return 1
    fi
}

# Función para verificar info endpoint
check_info() {
    print_status "Verificando endpoint de información..."
    
    local response=$(curl -s "$APP_URL/actuator/info" 2>/dev/null)
    
    if [ -n "$response" ] && [ "$response" != "{}" ]; then
        print_success "✓ Endpoint de información disponible"
        echo "$response" | jq . 2>/dev/null || echo "$response"
        return 0
    else
        print_warning "⚠ Endpoint de información vacío o no disponible"
        return 1
    fi
}

# Función para verificar endpoints específicos de la aplicación
check_app_endpoints() {
    print_status "Verificando endpoints de la aplicación..."
    
    # Verificar endpoint de documentación API
    if curl -s -f "$APP_URL/v3/api-docs" > /dev/null 2>&1; then
        print_success "✓ Documentación OpenAPI disponible"
    else
        print_warning "⚠ Documentación OpenAPI no disponible"
    fi
    
    # Verificar Swagger UI
    if curl -s -f "$APP_URL/swagger-ui.html" > /dev/null 2>&1; then
        print_success "✓ Swagger UI disponible"
    else
        print_warning "⚠ Swagger UI no disponible"
    fi
}

# Función principal
main() {
    print_header "VERIFICACIÓN DE HEALTH CHECKS Y MÉTRICAS"
    print_status "URL de la aplicación: $APP_URL"
    print_status "Timeout: $TIMEOUT segundos"
    
    local errors=0
    local warnings=0
    
    # Verificar si la aplicación está ejecutándose
    if ! check_app_running; then
        print_error "No se puede continuar sin la aplicación ejecutándose"
        exit 1
    fi
    
    print_header "1. HEALTH CHECKS"
    
    # Health check general
    if ! check_health_general; then
        errors=$((errors + 1))
    fi
    
    # Liveness probe
    if ! check_liveness; then
        errors=$((errors + 1))
    fi
    
    # Readiness probe
    if ! check_readiness; then
        warnings=$((warnings + 1))
    fi
    
    print_header "2. MÉTRICAS"
    
    # Métricas generales
    if ! check_metrics; then
        errors=$((errors + 1))
    fi
    
    # Prometheus
    if ! check_prometheus; then
        errors=$((errors + 1))
    fi
    
    print_header "3. INFORMACIÓN"
    
    # Info endpoint
    if ! check_info; then
        warnings=$((warnings + 1))
    fi
    
    print_header "4. ENDPOINTS DE APLICACIÓN"
    
    # Endpoints específicos
    check_app_endpoints
    
    print_header "RESUMEN"
    
    echo -e "Errores: ${RED}$errors${NC}"
    echo -e "Advertencias: ${YELLOW}$warnings${NC}"
    
    if [ $errors -eq 0 ]; then
        if [ $warnings -eq 0 ]; then
            print_success "🎉 ¡Todas las verificaciones pasaron exitosamente!"
            exit 0
        else
            print_warning "⚠️ Verificaciones completadas con advertencias"
            exit 0
        fi
    else
        print_error "❌ Verificaciones fallaron. Revisar errores."
        exit 1
    fi
}

# Verificar dependencias
if ! command -v curl &> /dev/null; then
    print_error "curl no está instalado. Por favor instalar curl."
    exit 1
fi

# Ejecutar función principal
main "$@"