#!/bin/bash

# ============================================
# SCRIPT DE VERIFICACIÓN DE HEALTH CHECKS Y MÉTRICAS
# MSBioSuper - Java 21 Migration
# ============================================

echo "🔍 Verificando Health Checks y Métricas de MSBioSuper"
echo "======================================================"

BASE_URL="http://localhost:8081"

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para verificar endpoint
check_endpoint() {
    local endpoint=$1
    local description=$2
    
    echo -e "\n${BLUE}🔍 Verificando: ${description}${NC}"
    echo "Endpoint: ${BASE_URL}${endpoint}"
    
    response=$(curl -s -w "HTTPSTATUS:%{http_code}" "${BASE_URL}${endpoint}")
    http_code=$(echo $response | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
    body=$(echo $response | sed -e 's/HTTPSTATUS\:.*//g')
    
    if [ "$http_code" -eq 200 ]; then
        echo -e "${GREEN}✅ OK (HTTP $http_code)${NC}"
        if [ ${#body} -gt 200 ]; then
            echo "Respuesta (primeros 200 caracteres): ${body:0:200}..."
        else
            echo "Respuesta: $body"
        fi
    else
        echo -e "${RED}❌ ERROR (HTTP $http_code)${NC}"
        echo "Respuesta: $body"
    fi
}

# Función para verificar métrica específica
check_metric() {
    local metric=$1
    local description=$2
    
    echo -e "\n${BLUE}📊 Verificando métrica: ${description}${NC}"
    echo "Métrica: $metric"
    
    response=$(curl -s -w "HTTPSTATUS:%{http_code}" "${BASE_URL}/actuator/metrics/${metric}")
    http_code=$(echo $response | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
    body=$(echo $response | sed -e 's/HTTPSTATUS\:.*//g')
    
    if [ "$http_code" -eq 200 ]; then
        echo -e "${GREEN}✅ OK (HTTP $http_code)${NC}"
        # Extraer el valor de la métrica
        value=$(echo $body | grep -o '"value":[0-9.E-]*' | head -1 | cut -d':' -f2)
        if [ ! -z "$value" ]; then
            echo "Valor actual: $value"
        fi
    else
        echo -e "${RED}❌ ERROR (HTTP $http_code)${NC}"
        echo "Respuesta: $body"
    fi
}

echo -e "\n${YELLOW}=== HEALTH CHECKS ===${NC}"

# Health checks principales
check_endpoint "/actuator/health" "Health Check Principal"
check_endpoint "/actuator/health/liveness" "Liveness Probe (Kubernetes)"
check_endpoint "/actuator/health/readiness" "Readiness Probe (Kubernetes)"

echo -e "\n${YELLOW}=== INFORMACIÓN DE LA APLICACIÓN ===${NC}"

# Información de la aplicación
check_endpoint "/actuator/info" "Información de la Aplicación"

echo -e "\n${YELLOW}=== MÉTRICAS GENERALES ===${NC}"

# Métricas generales
check_endpoint "/actuator/metrics" "Lista de Métricas Disponibles"
check_endpoint "/actuator/prometheus" "Métricas en formato Prometheus"

echo -e "\n${YELLOW}=== MÉTRICAS ESPECÍFICAS ===${NC}"

# Métricas específicas importantes
check_metric "jvm.memory.used" "Memoria JVM Utilizada"
check_metric "jvm.threads.live" "Threads Activos JVM"
check_metric "hikaricp.connections.active" "Conexiones Activas HikariCP"
check_metric "hikaricp.connections.idle" "Conexiones Idle HikariCP"
check_metric "system.cpu.usage" "Uso de CPU del Sistema"
check_metric "disk.free" "Espacio Libre en Disco"
check_metric "http.server.requests" "Peticiones HTTP del Servidor"

echo -e "\n${YELLOW}=== MÉTRICAS DE NEGOCIO PERSONALIZADAS ===${NC}"

# Métricas de negocio personalizadas
check_metric "msbiosuper.business.resumen_pesca.count" "Consultas de Resumen de Pesca"
check_metric "msbiosuper.business.resumen_siembra.count" "Consultas de Resumen de Siembra"
check_metric "msbiosuper.business.liquidaciones.count" "Consultas de Liquidaciones"

echo -e "\n${YELLOW}=== RESUMEN DE VERIFICACIÓN ===${NC}"

# Verificar si la aplicación está completamente saludable
health_response=$(curl -s "${BASE_URL}/actuator/health")
overall_status=$(echo $health_response | grep -o '"status":"[^"]*' | head -1 | cut -d':' -f2 | tr -d '"')

if [ "$overall_status" = "UP" ]; then
    echo -e "${GREEN}🎉 APLICACIÓN COMPLETAMENTE SALUDABLE${NC}"
    echo -e "${GREEN}✅ Todos los health checks están funcionando correctamente${NC}"
    echo -e "${GREEN}✅ Las métricas están siendo recolectadas${NC}"
    echo -e "${GREEN}✅ Prometheus puede consumir las métricas${NC}"
else
    echo -e "${YELLOW}⚠️  APLICACIÓN PARCIALMENTE SALUDABLE${NC}"
    echo -e "${YELLOW}Estado general: $overall_status${NC}"
    echo -e "${YELLOW}Algunos componentes pueden tener problemas${NC}"
fi

echo -e "\n${BLUE}📋 ENDPOINTS DISPONIBLES:${NC}"
echo "• Health Check Principal: ${BASE_URL}/actuator/health"
echo "• Liveness Probe: ${BASE_URL}/actuator/health/liveness"
echo "• Readiness Probe: ${BASE_URL}/actuator/health/readiness"
echo "• Métricas: ${BASE_URL}/actuator/metrics"
echo "• Prometheus: ${BASE_URL}/actuator/prometheus"
echo "• Información: ${BASE_URL}/actuator/info"

echo -e "\n${BLUE}🔧 PARA MONITOREO EN PRODUCCIÓN:${NC}"
echo "• Configurar Prometheus para scraping: ${BASE_URL}/actuator/prometheus"
echo "• Configurar Kubernetes liveness: ${BASE_URL}/actuator/health/liveness"
echo "• Configurar Kubernetes readiness: ${BASE_URL}/actuator/health/readiness"

echo -e "\n✅ Verificación completada"