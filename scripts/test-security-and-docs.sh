#!/bin/bash

# ============================================
# Script de Verificación de Seguridad y Documentación
# MSBioSuper - Java 21 Migration
# ============================================
#
# Este script verifica:
# 1. Flujo de autenticación JWT
# 2. Validación de entrada
# 3. Documentación OpenAPI
# 4. Swagger UI
# 5. Endpoints con autenticación
# 6. Health checks
#
# Uso: ./scripts/test-security-and-docs.sh
#
# Requisitos:
# - La aplicación debe estar corriendo en http://localhost:8081
# - curl debe estar instalado
# - jq debe estar instalado (opcional, para formatear JSON)
#
# ============================================

# Colores para output
RED='\033[0:31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuración
BASE_URL="http://localhost:8081"
API_URL="${BASE_URL}/api/v1"

# Contadores
TESTS_PASSED=0
TESTS_FAILED=0

# Función para imprimir encabezados
print_header() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

# Función para imprimir resultados
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ PASS${NC}: $2"
        ((TESTS_PASSED++))
    else
        echo -e "${RED}✗ FAIL${NC}: $2"
        ((TESTS_FAILED++))
    fi
}

# Función para verificar si la aplicación está corriendo
check_app_running() {
    print_header "1. Verificando que la aplicación esté corriendo"
    
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}/actuator/health")
    
    if [ "$HTTP_CODE" -eq 200 ] || [ "$HTTP_CODE" -eq 401 ]; then
        print_result 0 "Aplicación está corriendo en ${BASE_URL}"
        return 0
    else
        print_result 1 "Aplicación NO está corriendo en ${BASE_URL} (HTTP ${HTTP_CODE})"
        echo -e "${YELLOW}Por favor, inicia la aplicación antes de ejecutar este script${NC}"
        exit 1
    fi
}

# Función para verificar health check
check_health() {
    print_header "2. Verificando Health Check"
    
    RESPONSE=$(curl -s "${BASE_URL}/actuator/health")
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}/actuator/health")
    
    if [ "$HTTP_CODE" -eq 200 ]; then
        print_result 0 "Health check accesible (HTTP 200)"
        echo "Respuesta: $RESPONSE"
    else
        print_result 1 "Health check no accesible (HTTP ${HTTP_CODE})"
    fi
}

# Función para verificar OpenAPI docs
check_openapi() {
    print_header "3. Verificando Documentación OpenAPI"
    
    # Verificar /v3/api-docs
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}/v3/api-docs")
    
    if [ "$HTTP_CODE" -eq 200 ]; then
        print_result 0 "OpenAPI JSON accesible en /v3/api-docs (HTTP 200)"
        
        # Verificar que contiene información básica
        RESPONSE=$(curl -s "${BASE_URL}/v3/api-docs")
        
        if echo "$RESPONSE" | grep -q "MSBioSuper API"; then
            print_result 0 "Documento OpenAPI contiene título correcto"
        else
            print_result 1 "Documento OpenAPI no contiene título esperado"
        fi
        
        if echo "$RESPONSE" | grep -q "bearerAuth"; then
            print_result 0 "Esquema de seguridad JWT configurado"
        else
            print_result 1 "Esquema de seguridad JWT no encontrado"
        fi
    else
        print_result 1 "OpenAPI JSON no accesible (HTTP ${HTTP_CODE})"
    fi
}

# Función para verificar Swagger UI
check_swagger_ui() {
    print_header "4. Verificando Swagger UI"
    
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}/swagger-ui.html")
    
    if [ "$HTTP_CODE" -eq 200 ] || [ "$HTTP_CODE" -eq 302 ]; then
        print_result 0 "Swagger UI accesible en /swagger-ui.html (HTTP ${HTTP_CODE})"
    else
        print_result 1 "Swagger UI no accesible (HTTP ${HTTP_CODE})"
    fi
    
    # Verificar redirección a /swagger-ui/index.html
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "${BASE_URL}/swagger-ui/index.html")
    
    if [ "$HTTP_CODE" -eq 200 ]; then
        print_result 0 "Swagger UI index accesible (HTTP 200)"
    else
        print_result 1 "Swagger UI index no accesible (HTTP ${HTTP_CODE})"
    fi
}

# Función para verificar autenticación JWT
check_jwt_auth() {
    print_header "5. Verificando Autenticación JWT"
    
    # Intentar acceder a un endpoint protegido sin token
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "${API_URL}/biosuperController/getListaResumenPesca")
    
    if [ "$HTTP_CODE" -eq 401 ]; then
        print_result 0 "Endpoint protegido rechaza peticiones sin token (HTTP 401)"
    else
        print_result 1 "Endpoint protegido no rechaza peticiones sin token (HTTP ${HTTP_CODE})"
    fi
    
    # Intentar acceder con token inválido
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" \
        -H "Authorization: Bearer invalid_token_12345" \
        "${API_URL}/biosuperController/getListaResumenPesca")
    
    if [ "$HTTP_CODE" -eq 401 ]; then
        print_result 0 "Endpoint protegido rechaza tokens inválidos (HTTP 401)"
    else
        print_result 1 "Endpoint protegido no rechaza tokens inválidos (HTTP ${HTTP_CODE})"
    fi
    
    echo -e "\n${YELLOW}Nota: Para probar con token válido, necesitas:${NC}"
    echo "1. Obtener un token JWT del endpoint de autenticación"
    echo "2. Ejecutar: curl -H \"Authorization: Bearer \$TOKEN\" ${API_URL}/biosuperController/getListaResumenPesca"
}

# Función para verificar validación de entrada
check_input_validation() {
    print_header "6. Verificando Validación de Entrada"
    
    echo -e "${YELLOW}Nota: Esta verificación requiere un token JWT válido${NC}"
    echo "Para probar validación de entrada manualmente:"
    echo ""
    echo "1. Obtener token JWT"
    echo "2. Enviar petición con datos inválidos:"
    echo ""
    echo "curl -X POST ${API_URL}/biosuperController/getListaResumenPesca \\"
    echo "  -H \"Authorization: Bearer \$TOKEN\" \\"
    echo "  -H \"Content-Type: application/json\" \\"
    echo "  -d '{\"empresa\": \"\", \"fechaDesde\": \"invalid\", \"fechaHasta\": \"2024-01-01\"}'"
    echo ""
    echo "Debería retornar HTTP 400 con mensajes de validación"
}

# Función para verificar CORS
check_cors() {
    print_header "7. Verificando Configuración CORS"
    
    # Verificar preflight request
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" \
        -X OPTIONS \
        -H "Origin: http://localhost:3000" \
        -H "Access-Control-Request-Method: POST" \
        -H "Access-Control-Request-Headers: Authorization,Content-Type" \
        "${API_URL}/biosuperController/getListaResumenPesca")
    
    if [ "$HTTP_CODE" -eq 200 ] || [ "$HTTP_CODE" -eq 204 ]; then
        print_result 0 "Preflight CORS request exitoso (HTTP ${HTTP_CODE})"
    else
        print_result 1 "Preflight CORS request falló (HTTP ${HTTP_CODE})"
    fi
    
    # Verificar headers CORS
    CORS_HEADERS=$(curl -s -I \
        -X OPTIONS \
        -H "Origin: http://localhost:3000" \
        -H "Access-Control-Request-Method: POST" \
        "${API_URL}/biosuperController/getListaResumenPesca" | grep -i "access-control")
    
    if [ -n "$CORS_HEADERS" ]; then
        print_result 0 "Headers CORS presentes en respuesta"
        echo "$CORS_HEADERS"
    else
        print_result 1 "Headers CORS no encontrados en respuesta"
    fi
}

# Función para imprimir resumen
print_summary() {
    print_header "RESUMEN DE VERIFICACIÓN"
    
    TOTAL_TESTS=$((TESTS_PASSED + TESTS_FAILED))
    
    echo -e "Total de pruebas: ${BLUE}${TOTAL_TESTS}${NC}"
    echo -e "Pruebas exitosas: ${GREEN}${TESTS_PASSED}${NC}"
    echo -e "Pruebas fallidas: ${RED}${TESTS_FAILED}${NC}"
    
    if [ $TESTS_FAILED -eq 0 ]; then
        echo -e "\n${GREEN}✓ Todas las verificaciones pasaron exitosamente${NC}"
        exit 0
    else
        echo -e "\n${RED}✗ Algunas verificaciones fallaron${NC}"
        echo -e "${YELLOW}Revisa el archivo VERIFICACION-SEGURIDAD-DOCUMENTACION.md para más detalles${NC}"
        exit 1
    fi
}

# ============================================
# EJECUCIÓN PRINCIPAL
# ============================================

echo -e "${BLUE}"
echo "╔════════════════════════════════════════════════════════════╗"
echo "║  Verificación de Seguridad y Documentación - MSBioSuper   ║"
echo "║  Java 21 Migration                                         ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo -e "${NC}"

# Ejecutar verificaciones
check_app_running
check_health
check_openapi
check_swagger_ui
check_jwt_auth
check_input_validation
check_cors

# Imprimir resumen
print_summary
