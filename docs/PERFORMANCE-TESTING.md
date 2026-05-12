# Guía de Pruebas de Rendimiento - MSBioSuper

## Tabla de Contenidos

1. [Introducción](#introducción)
2. [Requisitos](#requisitos)
3. [Configuración](#configuración)
4. [Tipos de Pruebas](#tipos-de-pruebas)
5. [Ejecución de Pruebas](#ejecución-de-pruebas)
6. [Interpretación de Resultados](#interpretación-de-resultados)
7. [Objetivos de Rendimiento](#objetivos-de-rendimiento)
8. [Mejores Prácticas](#mejores-prácticas)
9. [Troubleshooting](#troubleshooting)

---

## Introducción

Este documento describe cómo ejecutar y analizar pruebas de rendimiento para el microservicio MSBioSuper utilizando **Gatling**, una herramienta moderna de pruebas de carga basada en Scala.

Las pruebas de rendimiento son esenciales para:
- Verificar que el sistema cumple con los objetivos de rendimiento (TR-22)
- Identificar cuellos de botella antes de producción
- Establecer líneas base de rendimiento
- Detectar regresiones de rendimiento
- Validar la capacidad del sistema bajo diferentes cargas

---

## Requisitos

### Software Requerido

- **Java 21**: Runtime de la aplicación
- **Maven 3.9+**: Para compilar y ejecutar las pruebas
- **PostgreSQL**: Base de datos con datos de prueba
- **MSBioSuper**: Aplicación ejecutándose localmente o en entorno de pruebas

### Hardware Recomendado

Para ejecutar las pruebas de rendimiento:
- **CPU**: 4+ cores
- **RAM**: 8GB+ disponible
- **Disco**: 10GB+ espacio libre para reportes

Para el servidor bajo prueba:
- **CPU**: 2+ cores
- **RAM**: 2GB+ disponible
- **Disco**: SSD recomendado

---

## Configuración

### 1. Configurar Variables de Entorno

Antes de ejecutar las pruebas, configure las variables de entorno necesarias:

```bash
# URL base de la API
export BASE_URL=http://localhost:8080

# Token JWT para autenticación
export JWT_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# Parámetros de prueba
export TEST_FECHA_INICIO=2024-01-01
export TEST_FECHA_FIN=2024-12-31
export TEST_EMPRESA_CODIGO=001
```

### 2. Generar Token JWT

Si no tiene un token JWT, puede generarlo usando el endpoint de autenticación:

```bash
curl -X POST http://localhost:8080/api/v1/security/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "testpass123"
  }'
```

Copie el token de la respuesta y configúrelo en la variable de entorno `JWT_TOKEN`.

### 3. Preparar Datos de Prueba

Asegúrese de que la base de datos contiene datos suficientes para las pruebas:
- Al menos 1 año de datos históricos
- Múltiples empresas y piscinas
- Datos de pesca, siembra, liquidaciones, productos, etc.

### 4. Configurar Parámetros de Prueba

Edite el archivo `src/test/resources/gatling-test.properties` para ajustar los parámetros:

```properties
# URL base
baseUrl=http://localhost:8080

# Configuración de carga
load.users=100
load.rampDuration=60
load.testDuration=300

# Configuración de estrés
stress.users=500
stress.rampDuration=120
stress.testDuration=600
```

---

## Tipos de Pruebas

### 1. Prueba de Carga (Load Test)

**Objetivo**: Verificar el comportamiento del sistema bajo carga normal esperada.

**Características**:
- 100 usuarios concurrentes (configurable)
- Rampa de 60 segundos
- Duración: 5 minutos
- Prueba todos los 12 endpoints principales

**Cuándo ejecutar**:
- Después de cambios significativos en el código
- Antes de cada release
- Como parte del pipeline CI/CD

**Clase**: `LoadTestSimulation`

### 2. Prueba de Estrés (Stress Test)

**Objetivo**: Identificar el punto de quiebre del sistema y verificar degradación elegante.

**Características**:
- 500 usuarios concurrentes (configurable)
- Rampa de 120 segundos
- Duración: 10 minutos
- Carga progresiva hasta el límite

**Cuándo ejecutar**:
- Para planificación de capacidad
- Antes de eventos de alto tráfico
- Para identificar límites del sistema

**Clase**: `StressTestSimulation`

### 3. Prueba de Pico (Spike Test)

**Objetivo**: Verificar la respuesta ante incrementos súbitos de tráfico.

**Características**:
- Carga base: 50 usuarios
- Pico: 1000 usuarios durante 30 segundos
- Verificación de recuperación post-pico

**Cuándo ejecutar**:
- Para validar auto-scaling
- Antes de campañas de marketing
- Para verificar rate limiting

**Clase**: `SpikeTestSimulation`

### 4. Prueba de Resistencia (Soak Test)

**Objetivo**: Detectar problemas de estabilidad a largo plazo (fugas de memoria, etc.).

**Características**:
- 100 usuarios concurrentes constantes
- Duración: 1 hora (configurable hasta 24 horas)
- Monitoreo de métricas de sistema

**Cuándo ejecutar**:
- Antes de releases mayores
- Para validar estabilidad
- En entorno de staging, no en desarrollo local

**Clase**: `SoakTestSimulation`

---

## Ejecución de Pruebas

### Método 1: Script de Ejecución (Recomendado)

El script `scripts/run-performance-tests.sh` facilita la ejecución de pruebas:

```bash
# Prueba de carga con configuración por defecto
./scripts/run-performance-tests.sh load

# Prueba de estrés con 1000 usuarios
./scripts/run-performance-tests.sh stress --users 1000

# Prueba de pico con duración personalizada
./scripts/run-performance-tests.sh spike --duration 60

# Prueba de resistencia (1 hora)
./scripts/run-performance-tests.sh soak

# Ejecutar todas las pruebas (excepto soak)
./scripts/run-performance-tests.sh all

# Con token JWT personalizado
./scripts/run-performance-tests.sh load --token "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Método 2: Maven Directo

También puede ejecutar las pruebas directamente con Maven:

```bash
# Prueba de carga
mvn gatling:test -Dgatling.simulationClass=msbiosuper.LoadTestSimulation

# Prueba de estrés con parámetros personalizados
mvn gatling:test \
  -Dgatling.simulationClass=msbiosuper.StressTestSimulation \
  -Dstress.users=1000 \
  -Dstress.rampDuration=180 \
  -Dstress.testDuration=900

# Prueba de pico
mvn gatling:test -Dgatling.simulationClass=msbiosuper.SpikeTestSimulation

# Prueba de resistencia
mvn gatling:test -Dgatling.simulationClass=msbiosuper.SoakTestSimulation
```

### Método 3: Desde IDE

1. Abra el proyecto en su IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Navegue a `src/test/scala/msbiosuper/`
3. Haga clic derecho en la simulación deseada
4. Seleccione "Run" o "Debug"

---

## Interpretación de Resultados

### Reportes de Gatling

Después de cada ejecución, Gatling genera un reporte HTML interactivo en:
```
target/gatling/<simulation-name>-<timestamp>/index.html
```

Abra el reporte en su navegador para ver:

#### 1. Resumen Global

- **Total de peticiones**: Número total de requests ejecutados
- **Peticiones exitosas**: Porcentaje de respuestas 2xx
- **Peticiones fallidas**: Porcentaje de respuestas 4xx/5xx
- **Tiempo de respuesta**:
  - Mínimo, máximo, media
  - Percentiles: p50, p75, p95, p99

#### 2. Gráficos de Rendimiento

- **Requests por segundo**: Throughput del sistema
- **Respuestas por segundo**: Capacidad de procesamiento
- **Tiempos de respuesta**: Distribución temporal
- **Usuarios activos**: Carga concurrente

#### 3. Detalles por Endpoint

Para cada endpoint, el reporte muestra:
- Estadísticas de tiempo de respuesta
- Distribución de códigos de estado HTTP
- Gráficos de latencia
- Percentiles específicos

### Métricas Clave

#### Tiempo de Respuesta

- **p50 (mediana)**: 50% de las peticiones responden en este tiempo o menos
- **p95**: 95% de las peticiones responden en este tiempo o menos
- **p99**: 99% de las peticiones responden en este tiempo o menos

**Objetivo TR-22**: p95 < 500ms

#### Throughput

- **Requests/segundo**: Número de peticiones procesadas por segundo
- **Objetivo TR-22**: > 1000 req/sec (bajo carga máxima)

#### Tasa de Éxito

- **Porcentaje de respuestas exitosas** (2xx)
- **Objetivo**: > 95% en pruebas de carga, > 80% en pruebas de estrés

#### Uso de Recursos

Monitoree con Prometheus/Grafana o Actuator:
- **Memoria JVM**: < 512MB bajo carga (objetivo TR-22)
- **CPU**: < 80% bajo carga normal
- **Conexiones DB**: < 20 activas (configuración HikariCP)
- **Tiempo de consulta DB**: p95 < 100ms (objetivo TR-22)

---

## Objetivos de Rendimiento

Según el requisito **TR-22** del documento de requirements:

| Métrica | Objetivo | Crítico |
|---------|----------|---------|
| Tiempo de respuesta API (p95) | < 500ms | < 1000ms |
| Throughput | > 1000 req/sec | > 500 req/sec |
| Tiempo de consulta DB (p95) | < 100ms | < 200ms |
| Uso de memoria bajo carga | < 512MB | < 1GB |
| Tasa de éxito | > 95% | > 90% |

### Interpretación de Resultados

✅ **PASS**: Todas las métricas cumplen los objetivos  
⚠️ **WARNING**: Algunas métricas entre objetivo y crítico  
❌ **FAIL**: Métricas por debajo del umbral crítico

---

## Mejores Prácticas

### Antes de Ejecutar Pruebas

1. **Entorno Aislado**: Ejecute pruebas en un entorno dedicado, no en producción
2. **Datos Realistas**: Use datos de prueba representativos del uso real
3. **Configuración Consistente**: Use la misma configuración que producción
4. **Monitoreo Activo**: Configure Prometheus/Grafana antes de las pruebas
5. **Baseline**: Establezca una línea base de rendimiento inicial

### Durante las Pruebas

1. **Monitoreo Continuo**: Observe métricas de sistema en tiempo real
2. **Logs**: Revise logs de aplicación para errores
3. **Base de Datos**: Monitoree conexiones, queries lentas, locks
4. **Red**: Verifique latencia de red, timeouts
5. **No Interrumpir**: Deje que las pruebas completen su ciclo

### Después de las Pruebas

1. **Análisis de Reportes**: Revise todos los reportes de Gatling
2. **Comparación**: Compare con resultados anteriores
3. **Identificar Cuellos de Botella**: Use profilers si es necesario
4. **Documentar**: Registre resultados y observaciones
5. **Optimizar**: Implemente mejoras basadas en hallazgos

### Optimizaciones Comunes

Si las pruebas no cumplen objetivos, considere:

1. **Base de Datos**:
   - Agregar índices a columnas frecuentemente consultadas
   - Optimizar queries N+1
   - Aumentar pool de conexiones (HikariCP)
   - Habilitar caché de segundo nivel

2. **Aplicación**:
   - Habilitar Virtual Threads (Java 21)
   - Optimizar serialización JSON
   - Implementar caché de aplicación (Redis, Caffeine)
   - Reducir llamadas a servicios externos

3. **Infraestructura**:
   - Aumentar recursos (CPU, RAM)
   - Configurar auto-scaling
   - Usar CDN para contenido estático
   - Implementar load balancer

---

## Troubleshooting

### Problema: Pruebas Fallan con Error 401 (Unauthorized)

**Causa**: Token JWT inválido o expirado

**Solución**:
```bash
# Generar nuevo token
curl -X POST http://localhost:8080/api/v1/security/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "testpass123"}'

# Configurar token
export JWT_TOKEN="<nuevo-token>"

# Ejecutar prueba
./scripts/run-performance-tests.sh load --token "$JWT_TOKEN"
```

### Problema: Pruebas Fallan con Error 500 (Internal Server Error)

**Causa**: Error en la aplicación o base de datos

**Solución**:
1. Revise logs de aplicación: `tail -f logs/application.log`
2. Verifique conexión a base de datos
3. Revise métricas de Actuator: `curl http://localhost:8080/actuator/health`
4. Reduzca carga de prueba: `--users 10`

### Problema: Tiempos de Respuesta Muy Altos

**Causa**: Cuellos de botella en aplicación, DB o red

**Solución**:
1. Revise métricas de Prometheus: `http://localhost:8080/actuator/prometheus`
2. Identifique queries lentas en PostgreSQL
3. Verifique uso de CPU/memoria con `top` o `htop`
4. Use profiler Java (JProfiler, YourKit, VisualVM)

### Problema: Errores de Conexión a Base de Datos

**Causa**: Pool de conexiones agotado

**Solución**:
```properties
# Aumentar tamaño del pool en application.properties
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10
```

### Problema: OutOfMemoryError Durante Pruebas

**Causa**: Fuga de memoria o heap insuficiente

**Solución**:
```bash
# Aumentar heap de JVM
export MAVEN_OPTS="-Xmx2g -Xms1g"

# O en application.properties para la app
java -Xmx1g -Xms512m -jar MSBioSuper-1.0.jar
```

### Problema: Compilación de Scala Falla

**Causa**: Plugin de Scala no configurado correctamente

**Solución**:
```bash
# Limpiar y recompilar
mvn clean compile

# Verificar versión de Scala
mvn dependency:tree | grep scala
```

---

## Recursos Adicionales

### Documentación

- [Gatling Documentation](https://gatling.io/docs/gatling/)
- [Gatling Maven Plugin](https://gatling.io/docs/gatling/reference/current/extensions/maven_plugin/)
- [Performance Testing Best Practices](https://gatling.io/docs/gatling/tutorials/advanced/)

### Herramientas de Monitoreo

- **Prometheus**: Métricas de aplicación (`/actuator/prometheus`)
- **Grafana**: Visualización de métricas
- **Spring Boot Actuator**: Health checks y métricas (`/actuator`)
- **PostgreSQL pg_stat_statements**: Análisis de queries

### Contacto

Para preguntas o problemas con las pruebas de rendimiento:
- Equipo de Desarrollo: dev@acosux.com
- Documentación del Proyecto: `docs/README.md`

---

**Última actualización**: 2026-04-23  
**Versión**: 1.0  
**Autor**: Josias Rubén Aldaz Arcalles
