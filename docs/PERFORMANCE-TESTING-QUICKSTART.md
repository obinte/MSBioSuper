# Guía Rápida: Benchmarks de Rendimiento

## Inicio Rápido en 5 Minutos

Esta guía te ayudará a ejecutar los benchmarks de rendimiento base para MSBioSuper en menos de 5 minutos.

---

## Prerequisitos

✅ **Antes de comenzar, asegúrate de tener**:

1. **Aplicación ejecutándose**: MSBioSuper debe estar corriendo en `http://localhost:8080`
2. **Base de datos con datos**: PostgreSQL con datos de prueba
3. **Maven instalado**: Maven 3.9+ configurado
4. **Java 21**: JDK 21 instalado

---

## Paso 1: Verificar que la Aplicación Está Corriendo

```bash
# Verificar que la aplicación responde
curl http://localhost:8080/actuator/health

# Deberías ver algo como:
# {"status":"UP"}
```

Si la aplicación no está corriendo:

```bash
# Iniciar la aplicación
mvn spring-boot:run

# O si tienes el JAR compilado
java -jar target/MSBioSuper-1.0.jar
```

---

## Paso 2: Generar Token JWT

```bash
# Generar token de autenticación
curl -X POST http://localhost:8080/api/v1/security/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "testpass123"}'

# Copiar el token de la respuesta
# Ejemplo: {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."}
```

**Guardar el token** para usarlo en el siguiente paso.

---

## Paso 3: Ejecutar Benchmark Base

### Opción A: Usando el Script (Recomendado)

```bash
# Ejecutar con configuración por defecto (50 usuarios, 5 minutos)
./scripts/run-benchmark.sh --token "TU_TOKEN_JWT_AQUI"

# Con recopilación de métricas del sistema
./scripts/run-benchmark.sh --token "TU_TOKEN_JWT_AQUI" --collect-metrics

# Personalizar usuarios y duración
./scripts/run-benchmark.sh \
  --token "TU_TOKEN_JWT_AQUI" \
  --users 100 \
  --duration 600 \
  --collect-metrics
```

### Opción B: Usando Maven Directamente

```bash
# Ejecutar benchmark con Maven
mvn gatling:test \
  -Dgatling.simulationClass=msbiosuper.BenchmarkSimulation \
  -Dbenchmark.users=50 \
  -Dbenchmark.duration=300 \
  -Dtest.jwt.token="TU_TOKEN_JWT_AQUI"
```

---

## Paso 4: Ver los Resultados

### Reporte de Gatling (Interactivo)

```bash
# El reporte HTML se genera automáticamente en:
# target/gatling/benchmarksimulation-[timestamp]/index.html

# Abrir en el navegador (macOS)
open target/gatling/benchmarksimulation-*/index.html

# Abrir en el navegador (Linux)
xdg-open target/gatling/benchmarksimulation-*/index.html
```

### Métricas del Sistema

Si usaste el script con `--collect-metrics`:

```bash
# Ver resultados en el directorio generado
ls -la benchmark-results/[timestamp]/

# Ver métricas del sistema
cat benchmark-results/[timestamp]/system-metrics.csv

# Ver reporte de Gatling
open benchmark-results/[timestamp]/gatling-report/index.html
```

---

## Paso 5: Interpretar los Resultados

### ✅ Indicadores de Éxito

El benchmark es exitoso si cumple estos objetivos:

| Métrica | Objetivo | Crítico |
|---------|----------|---------|
| **p50** (mediana) | < 200ms | < 500ms |
| **p95** | < 500ms | < 1000ms |
| **p99** | < 1000ms | < 2000ms |
| **Tasa de éxito** | > 95% | > 90% |
| **Throughput** | > 50 req/s | > 25 req/s |

### 📊 Qué Revisar en el Reporte

1. **Global Statistics**: Resumen general de todas las peticiones
2. **Response Time Distribution**: Gráfico de distribución de tiempos
3. **Requests per Second**: Throughput del sistema
4. **Response Time Percentiles**: p50, p75, p95, p99
5. **Details by Request**: Métricas individuales por endpoint

### 🔍 Identificar Problemas

**Tiempos de respuesta altos** (p95 > 500ms):
- Revisar queries de base de datos lentas
- Verificar uso de CPU y memoria
- Identificar endpoints específicos con problemas

**Tasa de éxito baja** (< 95%):
- Revisar logs de aplicación para errores
- Verificar conexiones a base de datos
- Comprobar timeouts de red

**Throughput bajo** (< 50 req/s):
- Verificar cuellos de botella en CPU
- Revisar pool de conexiones de BD
- Comprobar configuración de threads

---

## Comandos Útiles

### Consultar Métricas de Actuator

```bash
# Memoria JVM
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Threads activos
curl http://localhost:8080/actuator/metrics/jvm.threads.live

# Conexiones de BD activas
curl http://localhost:8080/actuator/metrics/hikaricp.connections.active

# Métricas de HTTP requests
curl http://localhost:8080/actuator/metrics/http.server.requests
```

### Monitorear Durante el Benchmark

```bash
# En una terminal separada, monitorear métricas en tiempo real
watch -n 2 'curl -s http://localhost:8080/actuator/metrics/jvm.memory.used | jq'

# O usar el endpoint de Prometheus
curl http://localhost:8080/actuator/prometheus | grep jvm_memory
```

### Limpiar Resultados Antiguos

```bash
# Limpiar reportes de Gatling
rm -rf target/gatling/*

# Limpiar resultados de benchmarks
rm -rf benchmark-results/*
```

---

## Troubleshooting Rápido

### ❌ Error: "Application not available"

**Problema**: La aplicación no está corriendo

**Solución**:
```bash
# Verificar que la aplicación está corriendo
curl http://localhost:8080/actuator/health

# Si no está corriendo, iniciarla
mvn spring-boot:run
```

### ❌ Error: "401 Unauthorized"

**Problema**: Token JWT inválido o expirado

**Solución**:
```bash
# Generar nuevo token
curl -X POST http://localhost:8080/api/v1/security/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "testpass123"}'

# Usar el nuevo token en el benchmark
```

### ❌ Error: "Connection refused to database"

**Problema**: PostgreSQL no está disponible

**Solución**:
```bash
# Verificar que PostgreSQL está corriendo
pg_isready -h localhost -p 5432

# Iniciar PostgreSQL si es necesario
# macOS: brew services start postgresql
# Linux: sudo systemctl start postgresql
```

### ❌ Tiempos de respuesta muy altos

**Problema**: Sistema sobrecargado o datos insuficientes

**Solución**:
```bash
# Reducir carga del benchmark
./scripts/run-benchmark.sh --users 10 --duration 60

# Verificar que hay datos en la BD
psql -U postgres -d msbiosuper -c "SELECT COUNT(*) FROM resumen_pesca;"
```

---

## Próximos Pasos

Después de ejecutar el benchmark base:

1. **Documentar resultados**: Actualizar `docs/BENCHMARK-RESULTS.md` con las métricas obtenidas
2. **Comparar con objetivos**: Verificar si se cumplen los requisitos TR-22, NFR-1, NFR-2
3. **Identificar optimizaciones**: Analizar endpoints lentos y cuellos de botella
4. **Ejecutar pruebas adicionales**: Load test, stress test, spike test (ver `docs/PERFORMANCE-TESTING.md`)
5. **Configurar monitoreo**: Establecer alertas en Prometheus/Grafana

---

## Recursos Adicionales

- **Documentación completa**: `docs/PERFORMANCE-TESTING.md`
- **Resultados de benchmarks**: `docs/BENCHMARK-RESULTS.md`
- **Simulaciones de Gatling**: `src/test/scala/msbiosuper/`
- **Scripts de pruebas**: `scripts/run-performance-tests.sh`

---

## Ayuda

Si tienes problemas o preguntas:

1. Revisar la documentación completa en `docs/PERFORMANCE-TESTING.md`
2. Consultar los logs de aplicación en `logs/application.log`
3. Verificar la configuración en `src/main/resources/application.properties`
4. Contactar al equipo de desarrollo

---

**¡Listo!** Ahora tienes benchmarks de rendimiento base para MSBioSuper. 🚀
