# Pruebas de Rendimiento con Gatling

Este directorio contiene las simulaciones de Gatling para pruebas de rendimiento de MSBioSuper.

## Estructura

```
src/test/scala/msbiosuper/
├── BaseSimulation.scala           # Clase base con configuración común
├── LoadTestSimulation.scala       # Prueba de carga (100 usuarios, 5 min)
├── StressTestSimulation.scala     # Prueba de estrés (500 usuarios, 10 min)
├── SpikeTestSimulation.scala      # Prueba de pico (1000 usuarios, 30 seg)
└── SoakTestSimulation.scala       # Prueba de resistencia (100 usuarios, 1 hora)
```

## Inicio Rápido

### 1. Preparación

Asegúrese de que la aplicación MSBioSuper está ejecutándose:

```bash
# Iniciar la aplicación
mvn spring-boot:run

# Verificar que está ejecutándose
curl http://localhost:8080/actuator/health
```

### 2. Generar Token JWT

```bash
# Autenticarse y obtener token
curl -X POST http://localhost:8080/api/v1/security/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "testpass123"}'

# Copiar el token de la respuesta
export JWT_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 3. Ejecutar Prueba

```bash
# Opción 1: Usar script (recomendado)
./scripts/run-performance-tests.sh load --token "$JWT_TOKEN"

# Opción 2: Maven directo
mvn gatling:test \
  -Dgatling.simulationClass=msbiosuper.LoadTestSimulation \
  -Dtest.jwt.token="$JWT_TOKEN"
```

### 4. Ver Resultados

Los reportes se generan en `target/gatling/`. Abra el archivo `index.html` en su navegador.

## Simulaciones Disponibles

### LoadTestSimulation

Prueba de carga estándar que simula uso normal del sistema.

**Características**:
- 100 usuarios concurrentes
- Prueba todos los 12 endpoints principales
- Duración: 5 minutos
- Objetivo: p95 < 500ms

**Ejecución**:
```bash
mvn gatling:test -Dgatling.simulationClass=msbiosuper.LoadTestSimulation
```

### StressTestSimulation

Prueba de estrés para identificar límites del sistema.

**Características**:
- 500 usuarios concurrentes
- Carga progresiva
- Duración: 10 minutos
- Objetivo: Identificar punto de quiebre

**Ejecución**:
```bash
mvn gatling:test \
  -Dgatling.simulationClass=msbiosuper.StressTestSimulation \
  -Dstress.users=500
```

### SpikeTestSimulation

Prueba de pico para validar respuesta ante tráfico súbito.

**Características**:
- Pico de 1000 usuarios durante 30 segundos
- Verificación de recuperación
- Duración total: 5 minutos

**Ejecución**:
```bash
mvn gatling:test \
  -Dgatling.simulationClass=msbiosuper.SpikeTestSimulation \
  -Dspike.users=1000
```

### SoakTestSimulation

Prueba de resistencia para detectar problemas de estabilidad.

**Características**:
- 100 usuarios concurrentes constantes
- Duración: 1 hora (configurable)
- Objetivo: Detectar fugas de memoria

**Ejecución**:
```bash
mvn gatling:test \
  -Dgatling.simulationClass=msbiosuper.SoakTestSimulation \
  -Dload.testDuration=3600
```

## Configuración

### Variables de Sistema

Puede personalizar las pruebas con variables de sistema:

```bash
mvn gatling:test \
  -Dgatling.simulationClass=msbiosuper.LoadTestSimulation \
  -DbaseUrl=http://localhost:8080 \
  -Dtest.jwt.token="$JWT_TOKEN" \
  -Dload.users=200 \
  -Dload.rampDuration=120 \
  -Dload.testDuration=600 \
  -Dtest.fechaInicio=2024-01-01 \
  -Dtest.fechaFin=2024-12-31 \
  -Dtest.empresaCodigo=001
```

### Archivo de Propiedades

Edite `src/test/resources/gatling-test.properties` para configuración persistente:

```properties
baseUrl=http://localhost:8080
load.users=100
load.rampDuration=60
load.testDuration=300
test.fechaInicio=2024-01-01
test.fechaFin=2024-12-31
```

## Endpoints Probados

Las simulaciones prueban los siguientes endpoints de MSBioSuper:

1. `POST /api/v1/biosuperController/getListaResumenPesca` - Resumen de pesca
2. `POST /api/v1/biosuperController/getListaResumenSiembra` - Resumen de siembra
3. `POST /api/v1/biosuperController/getConsumosDiarios` - Consumos diarios
4. `POST /api/v1/biosuperController/getListadoLiquidacionDetalleProducto` - Liquidaciones
5. `POST /api/v1/biosuperController/listarLiquidacionesDetalle` - Liquidaciones detalle
6. `POST /api/v1/biosuperController/listarCostosPorPiscina` - Costos por piscina
7. `POST /api/v1/biosuperController/obtenerListadoGramajes` - Gramajes
8. `POST /api/v1/biosuperController/getListadoComprasDetalleProducto` - Compras
9. `POST /api/v1/biosuperController/listarVentasBioSuper` - Ventas
10. `POST /api/v1/biosuperController/getListaProductosGeneralTO` - Productos
11. `POST /api/v1/biosuperController/getApiCarListaCuentasPorCobrarDetalladoTO` - Cuentas por cobrar
12. `POST /api/v1/biosuperController/getCarListaCuentasPorPagarDetalladoTO` - Cuentas por pagar

## Objetivos de Rendimiento (TR-22)

| Métrica | Objetivo |
|---------|----------|
| Tiempo de respuesta API (p95) | < 500ms |
| Throughput | > 1000 req/sec |
| Tiempo de consulta DB (p95) | < 100ms |
| Uso de memoria bajo carga | < 512MB |
| Tasa de éxito | > 95% |

## Interpretación de Resultados

### Métricas Clave

- **p50 (mediana)**: 50% de peticiones responden en este tiempo o menos
- **p95**: 95% de peticiones responden en este tiempo o menos
- **p99**: 99% de peticiones responden en este tiempo o menos
- **Throughput**: Requests procesados por segundo
- **Tasa de éxito**: Porcentaje de respuestas 2xx

### Criterios de Éxito

✅ **PASS**: p95 < 500ms, tasa de éxito > 95%  
⚠️ **WARNING**: p95 < 1000ms, tasa de éxito > 90%  
❌ **FAIL**: p95 > 1000ms o tasa de éxito < 90%

## Troubleshooting

### Error 401 (Unauthorized)

Token JWT inválido o expirado. Genere un nuevo token:

```bash
curl -X POST http://localhost:8080/api/v1/security/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "testpass123"}'
```

### Error de Compilación Scala

Limpie y recompile:

```bash
mvn clean compile
```

### Tiempos de Respuesta Altos

1. Verifique métricas de Actuator: `curl http://localhost:8080/actuator/metrics`
2. Revise logs de aplicación
3. Monitoree base de datos PostgreSQL
4. Use profiler Java si es necesario

## Recursos

- [Documentación Completa](../../docs/PERFORMANCE-TESTING.md)
- [Gatling Documentation](https://gatling.io/docs/gatling/)
- [Script de Ejecución](../../scripts/run-performance-tests.sh)

---

**Nota**: Para documentación detallada, consulte `docs/PERFORMANCE-TESTING.md`
