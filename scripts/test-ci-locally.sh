#!/bin/bash

# ============================================
# Script para probar CI/CD localmente
# ============================================
# 
# Este script simula los pasos del workflow de GitHub Actions
# para permitir pruebas locales antes de hacer push.
#
# Uso: ./scripts/test-ci-locally.sh [job]
# 
# Jobs disponibles:
#   - build: Solo compilación y empaquetado
#   - test: Pruebas unitarias e integración
#   - quality: Análisis de calidad de código
#   - security: Escaneo de seguridad
#   - docker: Build de imagen Docker
#   - all: Todos los jobs (default)
#
# Autor: Josias Rubén Aldaz Arcalles
# Fecha: 2026-04-24
# ============================================

set -e  # Salir en caso de error

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Variables de configuración
JAVA_VERSION="21"
MAVEN_OPTS="-Xmx1024m -XX:+UseZGC"
SPRING_PROFILES_ACTIVE="test"
PROJECT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

# Funciones de utilidad
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

log_step() {
    echo -e "\n${BLUE}🔄 $1${NC}"
    echo "=================================================="
}

# Verificar prerequisitos
check_prerequisites() {
    log_step "Verificando prerequisitos"
    
    # Verificar Java
    if ! command -v java &> /dev/null; then
        log_error "Java no está instalado"
        exit 1
    fi
    
    JAVA_VER=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VER" != "$JAVA_VERSION" ]; then
        log_warning "Java $JAVA_VER detectado, se requiere Java $JAVA_VERSION"
    else
        log_success "Java $JAVA_VERSION detectado"
    fi
    
    # Verificar Maven
    if ! command -v mvn &> /dev/null; then
        log_error "Maven no está instalado"
        exit 1
    fi
    log_success "Maven detectado: $(mvn -version | head -n 1)"
    
    # Verificar Docker (opcional)
    if command -v docker &> /dev/null; then
        log_success "Docker detectado: $(docker --version)"
    else
        log_warning "Docker no detectado (requerido para job docker)"
    fi
    
    # Verificar estructura del proyecto
    if [ ! -f "pom.xml" ]; then
        log_error "pom.xml no encontrado. Ejecutar desde la raíz del proyecto."
        exit 1
    fi
    
    log_success "Prerequisitos verificados"
}

# Job 1: Build
run_build() {
    log_step "JOB 1: Build y Compilación"
    
    log_info "Limpiando proyecto..."
    mvn clean -q
    
    log_info "Compilando proyecto..."
    mvn compile -DskipTests
    
    log_info "Empaquetando aplicación..."
    mvn package -DskipTests -P jar
    
    # Verificar JAR
    JAR_FILE="target/MSBioSuper-${PROJECT_VERSION}.jar"
    if [ -f "$JAR_FILE" ]; then
        log_success "JAR creado: $JAR_FILE ($(du -h "$JAR_FILE" | cut -f1))"
    else
        log_error "JAR no encontrado"
        return 1
    fi
    
    log_success "Build completado exitosamente"
}

# Job 2: Test
run_test() {
    log_step "JOB 2: Pruebas y Cobertura"
    
    # Verificar si PostgreSQL está disponible
    if command -v pg_isready &> /dev/null; then
        if pg_isready -h localhost -p 5432 &> /dev/null; then
            log_success "PostgreSQL detectado en localhost:5432"
            DB_AVAILABLE=true
        else
            log_warning "PostgreSQL no disponible, usando H2 en memoria"
            DB_AVAILABLE=false
        fi
    else
        log_warning "pg_isready no disponible, asumiendo H2 en memoria"
        DB_AVAILABLE=false
    fi
    
    log_info "Ejecutando pruebas unitarias..."
    if [ "$DB_AVAILABLE" = true ]; then
        mvn test -P test \
            -Dspring.profiles.active=test \
            -Dspring.datasource.url=jdbc:postgresql://localhost:5432/msbiosuper_test \
            -Dspring.datasource.username=test_user \
            -Dspring.datasource.password=test_password
    else
        mvn test -P test -Dspring.profiles.active=test
    fi
    
    log_info "Ejecutando pruebas de integración..."
    mvn verify -P test -DskipUTs=true
    
    log_info "Generando reporte de cobertura..."
    mvn jacoco:report
    
    # Verificar cobertura
    if [ -f "target/site/jacoco/index.html" ]; then
        log_success "Reporte de cobertura generado: target/site/jacoco/index.html"
    fi
    
    log_success "Pruebas completadas exitosamente"
}

# Job 3: Quality
run_quality() {
    log_step "JOB 3: Análisis de Calidad de Código"
    
    log_info "Ejecutando análisis SpotBugs..."
    mvn spotbugs:check || log_warning "SpotBugs encontró issues"
    
    log_info "Ejecutando análisis PMD..."
    mvn pmd:check || log_warning "PMD encontró issues"
    mvn pmd:cpd-check || log_warning "PMD CPD encontró duplicación"
    
    log_info "Ejecutando análisis Checkstyle..."
    mvn checkstyle:check || log_warning "Checkstyle encontró issues"
    
    # SonarCloud (solo si está configurado)
    if [ -n "$SONAR_TOKEN" ]; then
        log_info "Ejecutando análisis SonarCloud..."
        mvn sonar:sonar \
            -Dsonar.projectKey=msbiosuper-java21 \
            -Dsonar.organization=acosux \
            -Dsonar.host.url=https://sonarcloud.io \
            -Dsonar.login=$SONAR_TOKEN
    else
        log_warning "SONAR_TOKEN no configurado, omitiendo SonarCloud"
    fi
    
    log_success "Análisis de calidad completado"
}

# Job 4: Security
run_security() {
    log_step "JOB 4: Escaneo de Seguridad"
    
    log_info "Ejecutando OWASP Dependency Check..."
    mvn org.owasp:dependency-check-maven:check \
        -DfailBuildOnCVSS=8 \
        -DsuppressionsLocation=.github/owasp-suppressions.xml \
        -Dformats=HTML,JSON || log_warning "OWASP encontró vulnerabilidades"
    
    # Trivy (si está instalado)
    if command -v trivy &> /dev/null; then
        log_info "Ejecutando escaneo Trivy..."
        trivy fs . --format table || log_warning "Trivy encontró vulnerabilidades"
    else
        log_warning "Trivy no instalado, omitiendo escaneo"
    fi
    
    # GitLeaks (si está instalado)
    if command -v gitleaks &> /dev/null; then
        log_info "Ejecutando escaneo GitLeaks..."
        gitleaks detect --source . --verbose || log_warning "GitLeaks encontró secretos"
    else
        log_warning "GitLeaks no instalado, omitiendo escaneo"
    fi
    
    log_success "Escaneo de seguridad completado"
}

# Job 5: Docker
run_docker() {
    log_step "JOB 5: Build Docker"
    
    if ! command -v docker &> /dev/null; then
        log_error "Docker no está instalado"
        return 1
    fi
    
    log_info "Building imagen Docker..."
    docker build -t msbiosuper:local-test \
        --build-arg VERSION=$PROJECT_VERSION \
        --build-arg BUILD_DATE=$(date -u +"%Y-%m-%dT%H:%M:%SZ") \
        --build-arg VCS_REF=$(git rev-parse HEAD) \
        .
    
    log_info "Verificando imagen creada..."
    docker images msbiosuper:local-test
    
    # Escaneo de seguridad de imagen (si Trivy está disponible)
    if command -v trivy &> /dev/null; then
        log_info "Escaneando imagen Docker con Trivy..."
        trivy image msbiosuper:local-test || log_warning "Trivy encontró vulnerabilidades en la imagen"
    fi
    
    log_success "Build Docker completado"
}

# Función principal
main() {
    local job=${1:-all}
    
    echo "============================================"
    echo "🚀 Test CI/CD Local - MSBioSuper"
    echo "============================================"
    echo "Job: $job"
    echo "Versión: $PROJECT_VERSION"
    echo "Java: $JAVA_VERSION"
    echo "============================================"
    
    check_prerequisites
    
    case $job in
        "build")
            run_build
            ;;
        "test")
            run_build
            run_test
            ;;
        "quality")
            run_build
            run_quality
            ;;
        "security")
            run_build
            run_security
            ;;
        "docker")
            run_build
            run_docker
            ;;
        "all")
            run_build
            run_test
            run_quality
            run_security
            run_docker
            ;;
        *)
            log_error "Job desconocido: $job"
            echo "Jobs disponibles: build, test, quality, security, docker, all"
            exit 1
            ;;
    esac
    
    echo ""
    log_success "🎉 Test CI/CD completado exitosamente!"
    echo "============================================"
}

# Ejecutar función principal con argumentos
main "$@"