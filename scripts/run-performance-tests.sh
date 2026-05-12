#!/bin/bash

#############################################
# Script de ejecución de pruebas de rendimiento
# para MSBioSuper usando Gatling
#############################################

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes
print_info() {
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

# Función para mostrar uso
show_usage() {
    cat << EOF
Uso: $0 [TIPO_PRUEBA] [OPCIONES]

TIPOS DE PRUEBA:
    load        Prueba de carga (100 usuarios, 5 minutos)
    stress      Prueba de estrés (500 usuarios, 10 minutos)
    spike       Prueba de pico (1000 usuarios, 30 segundos)
    soak        Prueba de resistencia (100 usuarios, 1 hora)
    all         Ejecutar todas las pruebas secuencialmente

OPCIONES:
    -u, --users NUM         Número de usuarios concurrentes
    -d, --duration SEC      Duración de la prueba en segundos
    -t, --token TOKEN       Token JWT para autenticación
    -h, --help              Mostrar esta ayuda

EJEMPLOS:
    # Ejecutar prueba de carga con configuración por defecto
    $0 load

    # Ejecutar prueba de estrés con 1000 usuarios
    $0 stress --users 1000

    # Ejecutar prueba de pico con token JWT personalizado
    $0 spike --token "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

    # Ejecutar todas las pruebas
    $0 all

REQUISITOS:
    - La aplicación MSBioSuper debe estar ejecutándose en http://localhost:8080
    - Debe tener un token JWT válido (o configurarlo con --token)
    - Maven debe estar instalado

EOF
}

# Verificar que Maven está instalado
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven no está instalado. Por favor, instale Maven primero."
        exit 1
    fi
}

# Verificar que la aplicación está ejecutándose
check_app_running() {
    local base_url="${BASE_URL:-http://localhost:8080}"
    print_info "Verificando que la aplicación está ejecutándose en $base_url..."
    
    if curl -s -f -o /dev/null "$base_url/actuator/health"; then
        print_success "Aplicación está ejecutándose correctamente"
    else
        print_error "La aplicación no está ejecutándose en $base_url"
        print_info "Por favor, inicie la aplicación antes de ejecutar las pruebas"
        exit 1
    fi
}

# Ejecutar prueba de Gatling
run_gatling_test() {
    local simulation_class=$1
    local test_name=$2
    shift 2
    local extra_args="$@"
    
    print_info "Ejecutando prueba: $test_name"
    print_info "Clase de simulación: $simulation_class"
    
    mvn gatling:test \
        -Dgatling.simulationClass="msbiosuper.$simulation_class" \
        $extra_args
    
    if [ $? -eq 0 ]; then
        print_success "Prueba $test_name completada exitosamente"
        print_info "Reporte disponible en: target/gatling/*/index.html"
    else
        print_error "Prueba $test_name falló"
        return 1
    fi
}

# Parsear argumentos
TEST_TYPE=""
USERS=""
DURATION=""
JWT_TOKEN=""

while [[ $# -gt 0 ]]; do
    case $1 in
        load|stress|spike|soak|all)
            TEST_TYPE="$1"
            shift
            ;;
        -u|--users)
            USERS="$2"
            shift 2
            ;;
        -d|--duration)
            DURATION="$2"
            shift 2
            ;;
        -t|--token)
            JWT_TOKEN="$2"
            shift 2
            ;;
        -h|--help)
            show_usage
            exit 0
            ;;
        *)
            print_error "Opción desconocida: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Verificar que se especificó un tipo de prueba
if [ -z "$TEST_TYPE" ]; then
    print_error "Debe especificar un tipo de prueba"
    show_usage
    exit 1
fi

# Verificar requisitos
check_maven
check_app_running

# Construir argumentos adicionales
EXTRA_ARGS=""
if [ -n "$USERS" ]; then
    EXTRA_ARGS="$EXTRA_ARGS -Dload.users=$USERS -Dstress.users=$USERS -Dspike.users=$USERS"
fi
if [ -n "$DURATION" ]; then
    EXTRA_ARGS="$EXTRA_ARGS -Dload.testDuration=$DURATION -Dstress.testDuration=$DURATION -Dspike.duration=$DURATION"
fi
if [ -n "$JWT_TOKEN" ]; then
    EXTRA_ARGS="$EXTRA_ARGS -Dtest.jwt.token=$JWT_TOKEN"
fi

# Ejecutar pruebas según el tipo
case $TEST_TYPE in
    load)
        run_gatling_test "LoadTestSimulation" "Prueba de Carga" $EXTRA_ARGS
        ;;
    stress)
        run_gatling_test "StressTestSimulation" "Prueba de Estrés" $EXTRA_ARGS
        ;;
    spike)
        run_gatling_test "SpikeTestSimulation" "Prueba de Pico" $EXTRA_ARGS
        ;;
    soak)
        run_gatling_test "SoakTestSimulation" "Prueba de Resistencia" $EXTRA_ARGS
        ;;
    all)
        print_info "Ejecutando todas las pruebas secuencialmente..."
        run_gatling_test "LoadTestSimulation" "Prueba de Carga" $EXTRA_ARGS
        sleep 30  # Pausa entre pruebas
        run_gatling_test "StressTestSimulation" "Prueba de Estrés" $EXTRA_ARGS
        sleep 30
        run_gatling_test "SpikeTestSimulation" "Prueba de Pico" $EXTRA_ARGS
        print_warning "Prueba de resistencia (soak) omitida en modo 'all' debido a su larga duración"
        print_info "Para ejecutar la prueba de resistencia, use: $0 soak"
        ;;
esac

print_success "Todas las pruebas completadas"
print_info "Los reportes están disponibles en: target/gatling/"
