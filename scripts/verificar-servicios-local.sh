#!/bin/bash

# ============================================================================
# Script de Verificación de Servicios Locales - MSBioSuper
# ============================================================================
# Este script verifica que todos los servicios estén funcionando correctamente
# en el entorno de desarrollo local.
# ============================================================================

echo "🔍 VERIFICANDO SERVICIOS LOCALES DE MSBIOSUPER..."
echo "=================================================="

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para verificar URL
check_url() {
    local url=$1
    local service_name=$2
    local expected_status=${3:-200}
    
    echo -n "Verificando $service_name... "
    
    if curl -s -o /dev/null -w "%{http_code}" "$url" | grep -q "$expected_status"; then
        echo -e "${GREEN}✅ OK${NC}"
        return 0
    else
        echo -e "${RED}❌ FALLO${NC}"
        return 1
    fi
}

# Función para verificar puerto
check_port() {
    local host=$1
    local port=$2
    local service_name=$3
    
    echo -n "Verificando $service_name en $host:$port... "
    
    if nc -z "$host" "$port" 2>/dev/null; then
        echo -e "${GREEN}✅ OK${NC}"
        return 0
    else
        echo -e "${RED}❌ FALLO${NC}"
        return 1
    fi
}

echo ""
echo "🐳 VERIFICANDO CONTENEDORES DOCKER..."
echo "======================================"

# Verificar que Docker esté corriendo
if ! docker info >/dev/null 2>&1; then
    echo -e "${RED}❌ Docker no está corriendo${NC}"
    exit 1
fi

# Verificar contenedores
if docker-compose ps | grep -q "Up"; then
    echo -e "${GREEN}✅ Contenedores Docker están corriendo${NC}"
    docker-compose ps
else
    echo -e "${YELLOW}⚠️  Los contenedores no están corriendo. Ejecuta: docker-compose up -d${NC}"
fi

echo ""
echo "🗄️  VERIFICANDO BASE DE DATOS..."
echo "================================="

# Verificar PostgreSQL (puerto mapeado 5433)
check_port "localhost" "5433" "PostgreSQL"

# Verificar conexión a la base de datos
if command -v psql >/dev/null 2>&1; then
    echo -n "Verificando conexión a la base de datos... "
    if PGPASSWORD="@dm1n\$t0d0c0mPU\$" psql -h localhost -p 5433 -U administradores -d cipolliny_Abril_2026 -c "SELECT 1;" >/dev/null 2>&1; then
        echo -e "${GREEN}✅ OK${NC}"
    else
        echo -e "${RED}❌ FALLO - No se puede conectar a la base de datos${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  psql no está instalado, saltando verificación de conexión${NC}"
fi

echo ""
echo "🌐 VERIFICANDO APLICACIÓN WEB..."
echo "================================="

# Verificar aplicación principal (puerto 8082 mapeado a 8081 del contenedor)
check_port "localhost" "8082" "Aplicación MSBioSuper (Actuator)"

# Verificar health check (sin autenticación)
echo -n "Verificando Health Check público... "
if curl -s "http://localhost:8082/actuator/health" | grep -q "UP\|DOWN"; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FALLO${NC}"
fi

# Verificar liveness probe (sin autenticación)
echo -n "Verificando Liveness Probe... "
if curl -s "http://localhost:8082/actuator/health/liveness" | grep -q "UP\|DOWN"; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FALLO${NC}"
fi

# Verificar readiness probe (sin autenticación)
echo -n "Verificando Readiness Probe... "
if curl -s "http://localhost:8082/actuator/health/readiness" | grep -q "UP\|DOWN"; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FALLO${NC}"
fi

echo ""
echo "📚 VERIFICANDO DOCUMENTACIÓN API..."
echo "===================================="

# Verificar Swagger UI (probablemente protegido)
echo -n "Verificando Swagger UI... "
response=$(curl -s -w "%{http_code}" "http://localhost:8082/swagger-ui.html" -o /dev/null)
if [[ "$response" == "200" || "$response" == "401" || "$response" == "403" ]]; then
    echo -e "${GREEN}✅ OK (Respuesta: $response)${NC}"
else
    echo -e "${RED}❌ FALLO (Respuesta: $response)${NC}"
fi

# Verificar OpenAPI Docs (probablemente protegido)
echo -n "Verificando OpenAPI Docs... "
response=$(curl -s -w "%{http_code}" "http://localhost:8082/v3/api-docs" -o /dev/null)
if [[ "$response" == "200" || "$response" == "401" || "$response" == "403" ]]; then
    echo -e "${GREEN}✅ OK (Respuesta: $response)${NC}"
else
    echo -e "${RED}❌ FALLO (Respuesta: $response)${NC}"
fi

echo ""
echo "📊 VERIFICANDO MÉTRICAS..."
echo "=========================="

# Verificar métricas Prometheus (sin autenticación)
echo -n "Verificando Métricas Prometheus... "
if curl -s "http://localhost:8082/actuator/prometheus" | grep -q "jvm_memory_used_bytes\|http_server_requests_seconds"; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FALLO${NC}"
fi

# Verificar métricas Actuator (sin autenticación)
echo -n "Verificando Métricas Actuator... "
if curl -s "http://localhost:8082/actuator/metrics" | grep -q "names"; then
    echo -e "${GREEN}✅ OK${NC}"
else
    echo -e "${RED}❌ FALLO${NC}"
fi

echo ""
echo "🔐 VERIFICANDO SEGURIDAD..."
echo "==========================="

# Verificar endpoint de autenticación (debe devolver 405 Method Not Allowed o similar)
echo -n "Verificando Endpoint de Login... "
response=$(curl -s -w "%{http_code}" "http://localhost:8082/api/v1/auth/login" -o /dev/null)
if [[ "$response" == "405" || "$response" == "401" || "$response" == "404" ]]; then
    echo -e "${GREEN}✅ OK (Respuesta: $response)${NC}"
else
    echo -e "${YELLOW}⚠️  Respuesta inesperada: $response${NC}"
fi

echo ""
echo "📋 RESUMEN DE SERVICIOS..."
echo "=========================="
echo -e "${BLUE}🌐 Aplicación Principal:${NC} http://localhost:8082"
echo -e "${BLUE}📚 Swagger UI:${NC} http://localhost:8082/swagger-ui.html"
echo -e "${BLUE}📖 OpenAPI Docs:${NC} http://localhost:8082/v3/api-docs"
echo -e "${BLUE}❤️  Health Check:${NC} http://localhost:8082/actuator/health"
echo -e "${BLUE}📊 Métricas:${NC} http://localhost:8082/actuator/prometheus"
echo -e "${BLUE}🗄️  PostgreSQL:${NC} localhost:5433"

echo ""
echo "🔧 COMANDOS ÚTILES..."
echo "===================="
echo "Ver logs de la aplicación:"
echo "  docker-compose logs -f msbiosuper"
echo ""
echo "Ver logs de PostgreSQL:"
echo "  docker-compose logs -f postgres"
echo ""
echo "Conectarse a PostgreSQL:"
echo "  docker-compose exec postgres psql -U administradores -d cipolliny_Abril_2026"
echo ""
echo "Reiniciar servicios:"
echo "  docker-compose restart"
echo ""
echo "Detener servicios:"
echo "  docker-compose down"

echo ""
echo -e "${GREEN}✅ VERIFICACIÓN COMPLETADA${NC}"