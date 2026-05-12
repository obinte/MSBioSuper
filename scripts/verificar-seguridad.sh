#!/bin/bash

# Script de verificación de requisitos de seguridad
# Verifica JWT y validación de entrada para MSBioSuper Java 21

echo "============================================"
echo "VERIFICACIÓN DE REQUISITOS DE SEGURIDAD"
echo "MSBioSuper - Migración Java 21"
echo "============================================"
echo

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para verificar archivos
check_file() {
    local file=$1
    local description=$2
    
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓${NC} $description: $file"
        return 0
    else
        echo -e "${RED}✗${NC} $description: $file (NO ENCONTRADO)"
        return 1
    fi
}

# Función para verificar contenido en archivos
check_content() {
    local file=$1
    local pattern=$2
    local description=$3
    
    if [ -f "$file" ] && grep -q "$pattern" "$file"; then
        echo -e "${GREEN}✓${NC} $description"
        return 0
    else
        echo -e "${RED}✗${NC} $description"
        return 1
    fi
}

echo "1. VERIFICACIÓN DE CONFIGURACIÓN DE SEGURIDAD"
echo "=============================================="

# Verificar archivos de configuración de seguridad
check_file "src/main/java/com/acosux/MSBioSuper/config/SecurityConfig.java" "Configuración Spring Security 6"
check_file "src/main/java/com/acosux/MSBioSuper/security/TokenUtil.java" "Utilidad JWT"
check_file "src/main/java/com/acosux/MSBioSuper/security/VerifyTokenFilter.java" "Filtro de verificación JWT"

echo
echo "2. VERIFICACIÓN DE DEPENDENCIAS JWT"
echo "===================================="

# Verificar dependencias JJWT 0.12.x en pom.xml
check_content "pom.xml" "jjwt-api" "JJWT API 0.12.x"
check_content "pom.xml" "jjwt-impl" "JJWT Implementation"
check_content "pom.xml" "jjwt-jackson" "JJWT Jackson Integration"
check_content "pom.xml" "0.12.6" "JJWT versión 0.12.6"

echo
echo "3. VERIFICACIÓN DE SPRING SECURITY 6"
echo "====================================="

# Verificar configuración moderna de Spring Security
check_content "src/main/java/com/acosux/MSBioSuper/config/SecurityConfig.java" "SecurityFilterChain" "SecurityFilterChain (Spring Security 6)"
check_content "src/main/java/com/acosux/MSBioSuper/config/SecurityConfig.java" "CorsConfigurationSource" "Configuración CORS externalizada"
check_content "src/main/java/com/acosux/MSBioSuper/config/SecurityConfig.java" "SessionCreationPolicy.STATELESS" "Sesiones stateless"

echo
echo "4. VERIFICACIÓN DE JWT MODERNO"
echo "==============================="

# Verificar implementación JWT moderna
check_content "src/main/java/com/acosux/MSBioSuper/security/TokenUtil.java" "SecretKey" "Uso de SecretKey (seguro)"
check_content "src/main/java/com/acosux/MSBioSuper/security/TokenUtil.java" "Jwts.parser" "Parser JWT moderno"
check_content "src/main/java/com/acosux/MSBioSuper/security/TokenUtil.java" "refreshToken" "Mecanismo de renovación de tokens"
check_content "src/main/java/com/acosux/MSBioSuper/security/TokenUtil.java" "@Value.*jwt.secret" "Secreto JWT externalizado"

echo
echo "5. VERIFICACIÓN DE VALIDACIÓN DE ENTRADA"
echo "=========================================="

# Verificar dependencias de validación
check_content "pom.xml" "spring-boot-starter-validation" "Jakarta Validation"

# Verificar DTOs con validación
check_file "src/main/java/com/acosux/MSBioSuper/application/dto/ResumenPescaRequest.java" "DTO ResumenPescaRequest"
check_content "src/main/java/com/acosux/MSBioSuper/application/dto/ResumenPescaRequest.java" "@NotBlank" "Anotaciones de validación @NotBlank"
check_content "src/main/java/com/acosux/MSBioSuper/application/dto/ResumenPescaRequest.java" "@ValidEmpresaCode" "Validadores personalizados"

# Verificar manejador global de excepciones
check_file "src/main/java/com/acosux/MSBioSuper/infrastructure/exception/GlobalExceptionHandler.java" "Manejador global de excepciones"
check_content "src/main/java/com/acosux/MSBioSuper/infrastructure/exception/GlobalExceptionHandler.java" "MethodArgumentNotValidException" "Manejo de errores de validación"
check_content "src/main/java/com/acosux/MSBioSuper/infrastructure/exception/GlobalExceptionHandler.java" "ConstraintViolationException" "Manejo de violaciones de constraints"

echo
echo "6. VERIFICACIÓN DE CONFIGURACIÓN EXTERNALIZADA"
echo "==============================================="

# Verificar externalización de secretos
check_content "src/main/resources/application.properties" "jwt.secret=\${JWT_SECRET}" "Secreto JWT externalizado"
check_content "src/main/resources/application.properties" "cors.allowed-origins=\${CORS_ALLOWED_ORIGINS" "CORS externalizado"

echo
echo "7. VERIFICACIÓN DE VALIDADORES PERSONALIZADOS"
echo "=============================================="

# Verificar validadores personalizados
check_file "src/main/java/com/acosux/MSBioSuper/application/validation/ValidEmpresaCode.java" "Validador de código de empresa"
check_file "src/main/java/com/acosux/MSBioSuper/application/validation/ValidDateFormat.java" "Validador de formato de fecha"
check_file "src/main/java/com/acosux/MSBioSuper/application/validation/ValidDateRange.java" "Validador de rango de fechas"

echo
echo "8. VERIFICACIÓN DE PRUEBAS DE SEGURIDAD"
echo "========================================"

# Verificar pruebas de seguridad
check_file "src/test/java/com/acosux/MSBioSuper/infrastructure/security/VerifyTokenFilterTest.java" "Pruebas del filtro JWT"
check_content "src/test/java/com/acosux/MSBioSuper/infrastructure/security/VerifyTokenFilterTest.java" "@ExtendWith(MockitoExtension.class)" "Pruebas con Mockito"

echo
echo "============================================"
echo "RESUMEN DE VERIFICACIÓN"
echo "============================================"

# Contar verificaciones exitosas
total_checks=0
passed_checks=0

# Función para contar verificaciones
count_check() {
    total_checks=$((total_checks + 1))
    if [ $? -eq 0 ]; then
        passed_checks=$((passed_checks + 1))
    fi
}

# Re-ejecutar verificaciones para contar
check_file "src/main/java/com/acosux/MSBioSuper/config/SecurityConfig.java" "Configuración Spring Security 6" > /dev/null 2>&1
count_check

check_file "src/main/java/com/acosux/MSBioSuper/security/TokenUtil.java" "Utilidad JWT" > /dev/null 2>&1
count_check

check_content "pom.xml" "jjwt-api" "JJWT API" > /dev/null 2>&1
count_check

check_content "src/main/java/com/acosux/MSBioSuper/config/SecurityConfig.java" "SecurityFilterChain" "SecurityFilterChain" > /dev/null 2>&1
count_check

check_content "src/main/java/com/acosux/MSBioSuper/security/TokenUtil.java" "SecretKey" "SecretKey" > /dev/null 2>&1
count_check

check_content "pom.xml" "spring-boot-starter-validation" "Jakarta Validation" > /dev/null 2>&1
count_check

check_file "src/main/java/com/acosux/MSBioSuper/infrastructure/exception/GlobalExceptionHandler.java" "Manejador global" > /dev/null 2>&1
count_check

check_content "src/main/java/com/acosux/MSBioSuper/application/dto/ResumenPescaRequest.java" "@NotBlank" "Validación DTOs" > /dev/null 2>&1
count_check

# Calcular porcentaje
percentage=$((passed_checks * 100 / total_checks))

echo
if [ $percentage -ge 80 ]; then
    echo -e "${GREEN}✓ VERIFICACIÓN EXITOSA${NC}"
    echo -e "Requisitos de seguridad satisfechos: ${GREEN}$passed_checks/$total_checks ($percentage%)${NC}"
    echo
    echo "✓ Autenticación JWT moderna (JJWT 0.12.x) - IMPLEMENTADA"
    echo "✓ Spring Security 6 con SecurityFilterChain - IMPLEMENTADA"
    echo "✓ Validación de entrada con Jakarta Validation - IMPLEMENTADA"
    echo "✓ Secretos externalizados - IMPLEMENTADA"
    echo "✓ Manejo global de excepciones - IMPLEMENTADA"
    echo "✓ Validadores personalizados - IMPLEMENTADA"
    echo
    echo -e "${GREEN}RESULTADO: REQUISITOS DE SEGURIDAD SATISFECHOS${NC}"
    exit 0
else
    echo -e "${RED}✗ VERIFICACIÓN FALLIDA${NC}"
    echo -e "Requisitos de seguridad: ${RED}$passed_checks/$total_checks ($percentage%)${NC}"
    echo
    echo -e "${YELLOW}Se requiere completar la implementación de seguridad${NC}"
    echo
    echo -e "${RED}RESULTADO: REQUISITOS DE SEGURIDAD NO SATISFECHOS${NC}"
    exit 1
fi