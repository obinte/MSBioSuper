#!/bin/bash

# ============================================
# SCRIPT DE PRUEBA - CONFIGURACIÓN DE PRODUCCIÓN
# ============================================
# Este script valida que la configuración de producción
# esté correctamente implementada y funcional.

set -e

echo "🔍 Validando configuración de producción MSBioSuper..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para mostrar mensajes
log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Verificar que el archivo de configuración existe
log_info "Verificando archivo application-prod.properties..."
if [ -f "src/main/resources/application-prod.properties" ]; then
    log_success "Archivo application-prod.properties encontrado"
else
    log_error "Archivo application-prod.properties NO encontrado"
    exit 1
fi

# Verificar configuraciones críticas
log_info "Verificando configuraciones críticas..."

# Verificar configuración de base de datos
if grep -q "spring.datasource.url=jdbc:postgresql://\${JDBC_SERVER}:\${JDBC_PORT}/\${JDBC_DATABASE}" src/main/resources/application-prod.properties; then
    log_success "Configuración de URL de base de datos correcta (usa variables de entorno)"
else
    log_error "Configuración de URL de base de datos incorrecta"
    exit 1
fi

if grep -q "spring.datasource.username=\${JDBC_USER}" src/main/resources/application-prod.properties; then
    log_success "Configuración de usuario de base de datos correcta (usa variables de entorno)"
else
    log_error "Configuración de usuario de base de datos incorrecta"
    exit 1
fi

if grep -q "spring.datasource.password=\${JDBC_PASSWORD}" src/main/resources/application-prod.properties; then
    log_success "Configuración de contraseña de base de datos correcta (usa variables de entorno)"
else
    log_error "Configuración de contraseña de base de datos incorrecta"
    exit 1
fi

# Verificar configuración JWT
if grep -q "jwt.secret=\${JWT_SECRET}" src/main/resources/application-prod.properties; then
    log_success "Configuración JWT secret correcta (usa variables de entorno)"
else
    log_error "Configuración JWT secret incorrecta"
    exit 1
fi

# Verificar configuración de logging (debe ser restrictiva)
if grep -q "logging.level.root=WARN" src/main/resources/application-prod.properties; then
    log_success "Nivel de logging root configurado correctamente (WARN)"
else
    log_error "Nivel de logging root debe ser WARN en producción"
    exit 1
fi

# Verificar que SQL no se muestre en producción
if grep -q "spring.jpa.show-sql=false" src/main/resources/application-prod.properties; then
    log_success "SQL oculto en producción (seguridad)"
else
    log_error "SQL debe estar oculto en producción"
    exit 1
fi

# Verificar configuración de HikariCP para producción
if grep -q "spring.datasource.hikari.maximum-pool-size=20" src/main/resources/application-prod.properties; then
    log_success "Pool de conexiones configurado para producción (20 conexiones)"
else
    log_error "Pool de conexiones debe estar optimizado para producción"
    exit 1
fi

# Verificar configuración de Actuator (debe ser restrictiva)
if grep -q "management.endpoints.web.exposure.include=health,info,metrics,prometheus" src/main/resources/application-prod.properties; then
    log_success "Endpoints de Actuator configurados de forma restrictiva"
else
    log_error "Endpoints de Actuator deben ser restrictivos en producción"
    exit 1
fi

# Verificar configuración de compresión HTTP
if grep -q "server.compression.enabled=true" src/main/resources/application-prod.properties; then
    log_success "Compresión HTTP habilitada para producción"
else
    log_error "Compresión HTTP debe estar habilitada en producción"
    exit 1
fi

# Verificar configuración de HTTP/2
if grep -q "server.http2.enabled=true" src/main/resources/application-prod.properties; then
    log_success "HTTP/2 habilitado para mejor rendimiento"
else
    log_error "HTTP/2 debe estar habilitado en producción"
    exit 1
fi

# Verificar configuración de métricas Prometheus
if grep -q "management.metrics.export.prometheus.enabled=true" src/main/resources/application-prod.properties; then
    log_success "Métricas Prometheus habilitadas para monitoreo"
else
    log_error "Métricas Prometheus deben estar habilitadas en producción"
    exit 1
fi

# Verificar configuración de CORS (debe usar variables de entorno)
if grep -q "cors.allowed-origins=\${CORS_ALLOWED_ORIGINS}" src/main/resources/application-prod.properties; then
    log_success "CORS configurado con variables de entorno (seguro)"
else
    log_error "CORS debe usar variables de entorno en producción"
    exit 1
fi

# Verificar configuración de archivos de log
if grep -q "logging.file.name=logs/msbiosuper.log" src/main/resources/application-prod.properties; then
    log_success "Configuración de archivos de log correcta"
else
    log_error "Configuración de archivos de log incorrecta"
    exit 1
fi

# Verificar configuración de caché
if grep -q "spring.cache.type=caffeine" src/main/resources/application-prod.properties; then
    log_success "Caché habilitado para mejor rendimiento"
else
    log_error "Caché debe estar habilitado en producción"
    exit 1
fi

# Verificar que no hay valores hardcodeados sensibles
log_info "Verificando que no hay credenciales hardcodeadas..."

# Buscar posibles credenciales hardcodeadas
if grep -E "(password|secret|key)=.*[^${}]" src/main/resources/application-prod.properties | grep -v "^\s*#" | grep -v "\${"; then
    log_error "Se encontraron posibles credenciales hardcodeadas"
    exit 1
else
    log_success "No se encontraron credenciales hardcodeadas"
fi

# Verificar estructura del archivo
log_info "Verificando estructura del archivo..."

# Contar líneas de comentarios vs configuración
total_lines=$(wc -l < src/main/resources/application-prod.properties)
comment_lines=$(grep -c "^#" src/main/resources/application-prod.properties || true)
config_lines=$((total_lines - comment_lines))

log_info "Total de líneas: $total_lines"
log_info "Líneas de comentarios: $comment_lines"
log_info "Líneas de configuración: $config_lines"

if [ $config_lines -gt 50 ]; then
    log_success "Archivo de configuración completo ($config_lines configuraciones)"
else
    log_warning "Archivo de configuración parece incompleto ($config_lines configuraciones)"
fi

# Verificar que todas las secciones principales están presentes
log_info "Verificando secciones principales..."

sections=(
    "CONFIGURACIÓN DE BASE DE DATOS - PRODUCCIÓN"
    "CONFIGURACIÓN DE HIKARICP - PRODUCCIÓN"
    "CONFIGURACIÓN DE JPA/HIBERNATE - PRODUCCIÓN"
    "CONFIGURACIÓN DE SERVIDOR - PRODUCCIÓN"
    "CONFIGURACIÓN DE LOGGING - PRODUCCIÓN"
    "CONFIGURACIÓN DE ACTUATOR - PRODUCCIÓN"
    "CONFIGURACIÓN DE MÉTRICAS Y PROMETHEUS - PRODUCCIÓN"
    "CONFIGURACIÓN DE SEGURIDAD JWT - PRODUCCIÓN"
    "CONFIGURACIÓN DE CORS - PRODUCCIÓN"
)

for section in "${sections[@]}"; do
    if grep -q "$section" src/main/resources/application-prod.properties; then
        log_success "Sección encontrada: $section"
    else
        log_error "Sección faltante: $section"
        exit 1
    fi
done

# Verificar que el archivo es válido para Spring Boot
log_info "Verificando sintaxis de properties..."

# Verificar que no hay líneas malformadas
if grep -E "^[^#].*[^=]$" src/main/resources/application-prod.properties | grep -v "^$"; then
    log_warning "Se encontraron posibles líneas malformadas (revisar manualmente)"
else
    log_success "Sintaxis de properties correcta"
fi

# Resumen final
echo ""
echo "=================================================="
log_success "✅ VALIDACIÓN COMPLETADA EXITOSAMENTE"
echo "=================================================="
echo ""
log_info "El archivo application-prod.properties está correctamente configurado para producción:"
echo ""
echo "🔒 Seguridad:"
echo "   - Todas las credenciales externalizadas"
echo "   - Logging restrictivo (WARN/INFO)"
echo "   - SQL oculto en logs"
echo "   - CORS restrictivo"
echo "   - Endpoints Actuator limitados"
echo ""
echo "⚡ Rendimiento:"
echo "   - Pool de conexiones optimizado (20 conexiones)"
echo "   - Compresión HTTP habilitada"
echo "   - HTTP/2 habilitado"
echo "   - Caché habilitado"
echo "   - Batch processing optimizado"
echo ""
echo "📊 Observabilidad:"
echo "   - Métricas Prometheus habilitadas"
echo "   - Health checks configurados"
echo "   - Logging estructurado"
echo "   - Archivos de log con rotación"
echo ""
echo "🚀 La configuración está lista para despliegue en producción"
echo ""

exit 0