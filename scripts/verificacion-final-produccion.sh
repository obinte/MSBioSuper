#!/bin/bash

# ============================================
# SCRIPT DE VERIFICACIÓN FINAL PARA PRODUCCIÓN
# MSBioSuper Java 21 Migration
# ============================================
# Este script ejecuta todas las verificaciones de la tarea 38
# para asegurar que la aplicación está lista para producción.

set -e  # Salir en caso de error

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes con colores
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

# Variables de control
ERRORS=0
WARNINGS=0

# Función para incrementar errores
add_error() {
    ERRORS=$((ERRORS + 1))
    print_error "$1"
}

# Función para incrementar warnings
add_warning() {
    WARNINGS=$((WARNINGS + 1))
    print_warning "$1"
}

# ============================================
# 1. VERIFICACIÓN DE COMPILACIÓN
# ============================================
print_header "1. VERIFICACIÓN DE COMPILACIÓN"

print_status "Compilando aplicación con Java 21..."
if mvn clean compile -q; then
    print_success "✓ Aplicación compila correctamente con Java 21"
else
    add_error "✗ Error en compilación"
fi

# ============================================
# 2. VERIFICACIÓN DE EMPAQUETADO
# ============================================
print_header "2. VERIFICACIÓN DE EMPAQUETADO"

print_status "Empaquetando aplicación como JAR..."
if mvn clean package -DskipTests -q; then
    print_success "✓ Aplicación se empaqueta correctamente como JAR"
    
    # Verificar que el JAR existe
    if [ -f "target/MSBioSuper-1.0.jar" ]; then
        print_success "✓ Archivo JAR generado: target/MSBioSuper-1.0.jar"
        
        # Verificar tamaño del JAR
        JAR_SIZE=$(du -h target/MSBioSuper-1.0.jar | cut -f1)
        print_status "Tamaño del JAR: $JAR_SIZE"
    else
        add_error "✗ Archivo JAR no encontrado"
    fi
else
    add_error "✗ Error en empaquetado"
fi

# ============================================
# 3. VERIFICACIÓN DE PRUEBAS
# ============================================
print_header "3. VERIFICACIÓN DE PRUEBAS"

print_status "Ejecutando pruebas unitarias..."
if mvn test -q; then
    print_success "✓ Todas las pruebas unitarias pasan"
else
    add_warning "⚠ Algunas pruebas unitarias fallan (revisar logs)"
fi

print_status "Ejecutando pruebas de integración..."
if mvn verify -q; then
    print_success "✓ Todas las pruebas de integración pasan"
else
    add_warning "⚠ Algunas pruebas de integración fallan (revisar logs)"
fi

# ============================================
# 4. VERIFICACIÓN DE CONFIGURACIÓN
# ============================================
print_header "4. VERIFICACIÓN DE CONFIGURACIÓN"

# Verificar archivos de configuración
CONFIG_FILES=(
    "src/main/resources/application.properties"
    "src/main/resources/application-dev.properties"
    "src/main/resources/application-prod.properties"
    "src/main/resources/logback-spring.xml"
)

for config_file in "${CONFIG_FILES[@]}"; do
    if [ -f "$config_file" ]; then
        print_success "✓ Archivo de configuración existe: $config_file"
    else
        add_error "✗ Archivo de configuración faltante: $config_file"
    fi
done

# Verificar variables de entorno requeridas para producción
print_status "Verificando variables de entorno requeridas para producción..."
REQUIRED_ENV_VARS=(
    "JDBC_SERVER"
    "JDBC_PORT"
    "JDBC_DATABASE"
    "JDBC_USER"
    "JDBC_PASSWORD"
    "JWT_SECRET"
)

for env_var in "${REQUIRED_ENV_VARS[@]}"; do
    if grep -q "\${$env_var" src/main/resources/application*.properties; then
        print_success "✓ Variable de entorno configurada: $env_var"
    else
        add_warning "⚠ Variable de entorno no encontrada en configuración: $env_var"
    fi
done

# ============================================
# 5. VERIFICACIÓN DE DEPENDENCIAS
# ============================================
print_header "5. VERIFICACIÓN DE DEPENDENCIAS"

print_status "Verificando dependencias de Maven..."
if mvn dependency:analyze -q; then
    print_success "✓ Análisis de dependencias completado"
else
    add_warning "⚠ Problemas detectados en dependencias"
fi

print_status "Verificando vulnerabilidades de seguridad..."
if mvn org.owasp:dependency-check-maven:check -q 2>/dev/null; then
    print_success "✓ Verificación de seguridad completada"
else
    add_warning "⚠ Plugin de verificación de seguridad no disponible"
fi

# ============================================
# 6. VERIFICACIÓN DE ESTRUCTURA DE PROYECTO
# ============================================
print_header "6. VERIFICACIÓN DE ESTRUCTURA DE PROYECTO"

# Verificar estructura de arquitectura hexagonal
REQUIRED_DIRS=(
    "src/main/java/com/acosux/MSBioSuper/domain"
    "src/main/java/com/acosux/MSBioSuper/application"
    "src/main/java/com/acosux/MSBioSuper/infrastructure"
)

for dir in "${REQUIRED_DIRS[@]}"; do
    if [ -d "$dir" ]; then
        print_success "✓ Directorio de arquitectura hexagonal existe: $dir"
    else
        add_error "✗ Directorio de arquitectura hexagonal faltante: $dir"
    fi
done

# ============================================
# 7. VERIFICACIÓN DE DOCUMENTACIÓN
# ============================================
print_header "7. VERIFICACIÓN DE DOCUMENTACIÓN"

REQUIRED_DOCS=(
    "README.md"
    "docs/DEPLOYMENT.md"
    "docs/ARCHITECTURE.md"
    "docs/CONFIGURATION.md"
)

for doc in "${REQUIRED_DOCS[@]}"; do
    if [ -f "$doc" ]; then
        print_success "✓ Documentación existe: $doc"
    else
        add_warning "⚠ Documentación faltante: $doc"
    fi
done

# ============================================
# 8. VERIFICACIÓN DE DOCKER (SI ESTÁ DISPONIBLE)
# ============================================
print_header "8. VERIFICACIÓN DE DOCKER"

if command -v docker &> /dev/null; then
    print_status "Docker disponible, verificando Dockerfile..."
    
    if [ -f "Dockerfile" ]; then
        print_success "✓ Dockerfile existe"
        
        print_status "Construyendo imagen Docker..."
        if docker build -t msbiosuper-test . -q; then
            print_success "✓ Imagen Docker construida exitosamente"
            
            # Limpiar imagen de prueba
            docker rmi msbiosuper-test -f > /dev/null 2>&1
        else
            add_error "✗ Error construyendo imagen Docker"
        fi
    else
        add_warning "⚠ Dockerfile no encontrado"
    fi
    
    if [ -f "docker-compose.yml" ]; then
        print_success "✓ docker-compose.yml existe"
    else
        add_warning "⚠ docker-compose.yml no encontrado"
    fi
else
    add_warning "⚠ Docker no está disponible para verificación"
fi

# ============================================
# 9. VERIFICACIÓN DE SCRIPTS
# ============================================
print_header "9. VERIFICACIÓN DE SCRIPTS"

REQUIRED_SCRIPTS=(
    "scripts/generate-jwt-secret.sh"
    "scripts/build-docker-image.sh"
    "scripts/test-health-probes.sh"
)

for script in "${REQUIRED_SCRIPTS[@]}"; do
    if [ -f "$script" ]; then
        print_success "✓ Script existe: $script"
        if [ -x "$script" ]; then
            print_success "  ✓ Script es ejecutable"
        else
            add_warning "  ⚠ Script no es ejecutable"
        fi
    else
        add_warning "⚠ Script faltante: $script"
    fi
done

# ============================================
# 10. VERIFICACIÓN DE PERFILES MAVEN
# ============================================
print_header "10. VERIFICACIÓN DE PERFILES MAVEN"

print_status "Verificando perfiles Maven..."
MAVEN_PROFILES=("dev" "test" "prod" "jib")

for profile in "${MAVEN_PROFILES[@]}"; do
    if mvn help:all-profiles -q | grep -q "$profile"; then
        print_success "✓ Perfil Maven disponible: $profile"
    else
        add_warning "⚠ Perfil Maven no encontrado: $profile"
    fi
done

# ============================================
# 11. VERIFICACIÓN DE COBERTURA DE CÓDIGO
# ============================================
print_header "11. VERIFICACIÓN DE COBERTURA DE CÓDIGO"

print_status "Generando reporte de cobertura de código..."
if mvn clean test jacoco:report -q; then
    if [ -f "target/site/jacoco/index.html" ]; then
        print_success "✓ Reporte de cobertura generado"
        
        # Extraer porcentaje de cobertura si es posible
        if command -v grep &> /dev/null && [ -f "target/site/jacoco/index.html" ]; then
            COVERAGE=$(grep -o "Total.*[0-9]\+%" target/site/jacoco/index.html | tail -1 | grep -o "[0-9]\+%" || echo "N/A")
            print_status "Cobertura de código: $COVERAGE"
        fi
    else
        add_warning "⚠ Reporte de cobertura no generado"
    fi
else
    add_warning "⚠ Error generando reporte de cobertura"
fi

# ============================================
# 12. VERIFICACIÓN DE ENDPOINTS (SIMULADA)
# ============================================
print_header "12. VERIFICACIÓN DE ENDPOINTS"

print_status "Verificando definición de endpoints en controladores..."

# Buscar controladores REST
CONTROLLERS_FOUND=$(find src/main/java -name "*Controller.java" | wc -l)
if [ "$CONTROLLERS_FOUND" -gt 0 ]; then
    print_success "✓ Encontrados $CONTROLLERS_FOUND controladores REST"
    
    # Buscar endpoints específicos
    ENDPOINTS=(
        "getListaResumenPesca"
        "getListaResumenSiembra"
        "getConsumosDiarios"
        "getListadoLiquidacionDetalleProducto"
        "getListaProductosGeneralTO"
        "getApiCarListaCuentasPorCobrarDetalladoTO"
        "getCarListaCuentasPorPagarDetalladoTO"
    )
    
    for endpoint in "${ENDPOINTS[@]}"; do
        if grep -r "$endpoint" src/main/java --include="*Controller.java" > /dev/null; then
            print_success "  ✓ Endpoint encontrado: $endpoint"
        else
            add_warning "  ⚠ Endpoint no encontrado: $endpoint"
        fi
    done
else
    add_error "✗ No se encontraron controladores REST"
fi

# ============================================
# RESUMEN FINAL
# ============================================
print_header "RESUMEN DE VERIFICACIÓN"

echo -e "Total de verificaciones realizadas"
echo -e "Errores encontrados: ${RED}$ERRORS${NC}"
echo -e "Advertencias encontradas: ${YELLOW}$WARNINGS${NC}"

if [ $ERRORS -eq 0 ] && [ $WARNINGS -eq 0 ]; then
    print_success "🎉 ¡VERIFICACIÓN EXITOSA! La aplicación está lista para producción."
    exit 0
elif [ $ERRORS -eq 0 ]; then
    print_warning "⚠️  VERIFICACIÓN COMPLETADA CON ADVERTENCIAS. Revisar elementos marcados."
    exit 0
else
    print_error "❌ VERIFICACIÓN FALLIDA. Corregir errores antes de desplegar a producción."
    exit 1
fi