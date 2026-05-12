#!/bin/bash

# ============================================================================
# Script de Pruebas del Microservicio MSBioSuper
# ============================================================================
# Este script realiza pruebas completas del microservicio incluyendo:
# - Verificación de servicios básicos
# - Autenticación JWT
# - Pruebas de endpoints de API
# - Verificación de documentación
# ============================================================================

echo "🧪 INICIANDO PRUEBAS DEL MICROSERVICIO MSBIOSUPER..."
echo "====================================================="

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Variables de configuración
BASE_URL="http://localhost:8082"
API_BASE="$BASE_URL/api/v1"

# Función para mostrar respuesta JSON formateada
show_json() {
    local response=$1
    if command -v jq >/dev/null 2>&1; then
        echo "$response" | jq '.'
    else
        echo "$response"
    fi
}

# Función para hacer petición HTTP con manejo de errores
make_request() {
    local method=$1
    local url=$2
    local data=$3
    local headers=$4
    local description=$5
    
    echo -e "\n${CYAN}🔍 $description${NC}"
    echo "   Método: $method"
    echo "   URL: $url"
    
    if [[ -n "$data" ]]; then
        echo "   Datos: $data"
    fi
    
    local curl_cmd="curl -s -w '\n%{http_code}' -X $method '$url'"
    
    if [[ -n "$headers" ]]; then
        curl_cmd="$curl_cmd -H '$headers'"
    fi
    
    if [[ -n "$data" ]]; then
        curl_cmd="$curl_cmd -d '$data'"
    fi
    
    echo "   Comando: $curl_cmd"
    echo ""
    
    local response=$(eval $curl_cmd)
    local http_code=$(echo "$response" | tail -n1)
    local body=$(echo "$response" | head -n -1)
    
    echo -e "   ${BLUE}Código HTTP: $http_code${NC}"
    
    if [[ $http_code -ge 200 && $http_code -lt 300 ]]; then
        echo -e "   ${GREEN}✅ ÉXITO${NC}"
    elif [[ $http_code -ge 400 && $http_code -lt 500 ]]; then
        echo -e "   ${YELLOW}⚠️  ERROR DEL CLIENTE${NC}"
    else
        echo -e "   ${RED}❌ ERROR DEL SERVIDOR${NC}"
    fi
    
    if [[ -n "$body" ]]; then
        echo -e "   ${BLUE}Respuesta:${NC}"
        show_json "$body" | sed 's/^/   /'
    fi
    
    echo ""
    echo "----------------------------------------"
    
    # Retornar el token si es una respuesta de login exitosa
    if [[ $http_code -eq 200 && "$url" == *"/auth/login"* ]]; then
        echo "$body" | jq -r '.token // .access_token // .jwt // empty' 2>/dev/null
    fi
}

echo ""
echo "🔧 VERIFICACIÓN INICIAL DE SERVICIOS..."
echo "========================================"

# Verificar que los contenedores estén corriendo
if ! docker-compose ps | grep -q "Up"; then
    echo -e "${RED}❌ Los contenedores no están corriendo${NC}"
    echo "Ejecuta: docker-compose up -d"
    exit 1
fi

echo -e "${GREEN}✅ Contenedores están corriendo${NC}"

echo ""
echo "❤️  VERIFICANDO HEALTH CHECKS..."
echo "================================="

# Health check general
make_request "GET" "$BASE_URL/actuator/health" "" "" "Health Check General"

# Liveness probe
make_request "GET" "$BASE_URL/actuator/health/liveness" "" "" "Liveness Probe"

# Readiness probe
make_request "GET" "$BASE_URL/actuator/health/readiness" "" "" "Readiness Probe"

echo ""
echo "📊 VERIFICANDO MÉTRICAS..."
echo "=========================="

# Métricas Prometheus
make_request "GET" "$BASE_URL/actuator/prometheus" "" "" "Métricas Prometheus"

# Métricas Actuator
make_request "GET" "$BASE_URL/actuator/metrics" "" "" "Lista de Métricas Actuator"

echo ""
echo "📚 VERIFICANDO DOCUMENTACIÓN API..."
echo "===================================="

# OpenAPI Docs
make_request "GET" "$BASE_URL/v3/api-docs" "" "" "Especificación OpenAPI"

# Swagger UI
make_request "GET" "$BASE_URL/swagger-ui.html" "" "" "Swagger UI"

echo ""
echo "🔐 PRUEBAS DE AUTENTICACIÓN..."
echo "==============================="

# Intentar acceso sin token
make_request "GET" "$API_BASE/biosuperController/health" "" "" "Acceso sin token (debe fallar)"

# Datos de prueba para login (ajustar según tu implementación)
LOGIN_DATA='{
    "username": "admin",
    "password": "admin123"
}'

# Intentar login
echo -e "\n${CYAN}🔑 Intentando autenticación...${NC}"
JWT_TOKEN=$(make_request "POST" "$API_BASE/auth/login" "$LOGIN_DATA" "Content-Type: application/json" "Login de usuario")

if [[ -n "$JWT_TOKEN" && "$JWT_TOKEN" != "null" ]]; then
    echo -e "${GREEN}✅ Token JWT obtenido exitosamente${NC}"
    echo "Token: ${JWT_TOKEN:0:50}..."
    
    # Probar endpoint con token
    make_request "GET" "$API_BASE/biosuperController/health" "" "Authorization: Bearer $JWT_TOKEN" "Acceso con token JWT"
    
else
    echo -e "${YELLOW}⚠️  No se pudo obtener token JWT${NC}"
    echo "Esto puede ser normal si:"
    echo "- Las credenciales de prueba no son correctas"
    echo "- El endpoint de login tiene una ruta diferente"
    echo "- Se requiere configuración adicional"
fi

echo ""
echo "🌐 PRUEBAS DE ENDPOINTS DE API..."
echo "================================="

# Lista de endpoints para probar (ajustar según tu API)
declare -a endpoints=(
    "GET:/biosuperController/health:Health check de la API"
    "POST:/biosuperController/getListaResumenPesca:Resumen de pesca"
    "POST:/biosuperController/getListaResumenSiembra:Resumen de siembra"
    "POST:/biosuperController/getConsumosDiarios:Consumos diarios"
)

# Datos de prueba genéricos
TEST_DATA='{
    "fechaInicio": "2026-01-01",
    "fechaFin": "2026-05-07",
    "empresa": "ACOSUX"
}'

for endpoint_info in "${endpoints[@]}"; do
    IFS=':' read -r method path description <<< "$endpoint_info"
    
    if [[ -n "$JWT_TOKEN" && "$JWT_TOKEN" != "null" ]]; then
        headers="Authorization: Bearer $JWT_TOKEN"
        if [[ "$method" == "POST" ]]; then
            headers="$headers, Content-Type: application/json"
            make_request "$method" "$API_BASE$path" "$TEST_DATA" "$headers" "$description (con token)"
        else
            make_request "$method" "$API_BASE$path" "" "$headers" "$description (con token)"
        fi
    else
        if [[ "$method" == "POST" ]]; then
            make_request "$method" "$API_BASE$path" "$TEST_DATA" "Content-Type: application/json" "$description (sin token)"
        else
            make_request "$method" "$API_BASE$path" "" "" "$description (sin token)"
        fi
    fi
done

echo ""
echo "🔍 INFORMACIÓN ADICIONAL..."
echo "==========================="

echo -e "${BLUE}📋 Endpoints disponibles:${NC}"
echo "   🌐 Aplicación: $BASE_URL"
echo "   📚 Swagger UI: $BASE_URL/swagger-ui.html"
echo "   📖 OpenAPI: $BASE_URL/v3/api-docs"
echo "   ❤️  Health: $BASE_URL/actuator/health"
echo "   📊 Métricas: $BASE_URL/actuator/prometheus"
echo "   🔧 Debug: puerto 5005"
echo "   🔄 LiveReload: puerto 35729"

echo ""
echo -e "${BLUE}🔧 Comandos útiles:${NC}"
echo "   Ver logs: docker-compose logs -f msbiosuper"
echo "   Reiniciar: docker-compose restart"
echo "   Detener: docker-compose down"

echo ""
echo -e "${BLUE}🧪 Herramientas de prueba recomendadas:${NC}"
echo "   • Postman: Importar desde $BASE_URL/v3/api-docs"
echo "   • Insomnia: Importar especificación OpenAPI"
echo "   • curl: Usar ejemplos de este script"
echo "   • Swagger UI: Interfaz web interactiva"

echo ""
echo -e "${GREEN}✅ PRUEBAS COMPLETADAS${NC}"
echo ""
echo -e "${CYAN}💡 PRÓXIMOS PASOS SUGERIDOS:${NC}"
echo "1. Revisar la documentación en Swagger UI"
echo "2. Configurar credenciales de prueba si es necesario"
echo "3. Probar endpoints específicos con datos reales"
echo "4. Verificar logs para cualquier error"
echo "5. Configurar herramientas de desarrollo (Postman, etc.)"