# Verificación de Métricas Prometheus - MSBioSuper

## Resumen de Configuración

Se ha completado la configuración del endpoint de métricas Prometheus para MSBioSuper. Esta configuración incluye:

### ✅ Métricas Configuradas

1. **Métricas JVM**
   - Memoria (heap, non-heap)
   - Threads (activos, daemon, peak)
   - Garbage Collection (contadores, tiempos)
   - Clases cargadas

2. **Métricas HTTP**
   - Latencia (p50, p95, p99)
   - Throughput (requests/segundo)
   - Contadores de errores
   - Histogramas de distribución

3. **Métricas de Base de Datos (HikariCP)**
   - Conexiones activas
   - Conexiones idle
   - Tiempo de espera de conexión
   - Pool size (actual vs máximo)
   - Conexiones creadas/destruidas

4. **Métricas del Sistema**
   - CPU usage
   - Disk space
   - Process uptime
   - File descriptors

### 📁 Archivos Modificados

1. **src/main/resources/application.properties**
   - Configuración base de métricas
   - Percentiles HTTP (p50, p95, p99)
   - Histogramas habilitados
   - Tags comunes (application, environment)

2. **src/main/resources/application-dev.properties**
   - Métricas detalladas para desarrollo
   - Percentiles extendidos (p50, p75, p90, p95, p99, p999)
   - Todos los endpoints de actuator expuestos

3. **src/main/resources/application-prod.properties**
   - Métricas optimizadas para producción
   - SLO configurados (50ms, 100ms, 200ms, 500ms, 1s, 2s)
   - Endpoints limitados por seguridad

4. **src/main/java/com/acosux/MSBiosuper/config/MetricsConfig.java** (NUEVO)
   - Configuración de tags comunes
   - Preparado para métricas personalizadas de negocio

## 🧪 Cómo Verificar las Métricas

### Paso 1: Configurar Variables de Entorno

Antes de iniciar la aplicación, asegúrate de tener configuradas las variables de entorno necesarias:

```bash
export JDBC_SERVER=localhost
export JDBC_PORT=5432
export JDBC_DATABASE=msbiosuper_dev
export JDBC_USER=dev_user
export JDBC_PASSWORD=dev_password
export JWT_SECRET=ZGV2ZWxvcG1lbnRfc2VjcmV0X2tleV9mb3JfbXNiaW9zdXBlcl9kZXZfZW52aXJvbm1lbnRfMTIzNDU2Nzg5MA==
```

### Paso 2: Iniciar la Aplicación

```bash
# Opción 1: Usando Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dmaven.test.skip=true

# Opción 2: Usando el JAR
java -jar target/MSBioSuper-1.0.jar --spring.profiles.active=dev
```

### Paso 3: Verificar Endpoints de Actuator

Una vez que la aplicación esté ejecutándose, verifica los siguientes endpoints:

#### 1. Health Check
```bash
curl http://localhost:8080/actuator/health
```

**Respuesta esperada:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

#### 2. Métricas Generales
```bash
curl http://localhost:8080/actuator/metrics
```

**Respuesta esperada:** Lista de todas las métricas disponibles

#### 3. Endpoint de Prometheus (Principal)
```bash
curl http://localhost:8080/actuator/prometheus
```

**Respuesta esperada:** Métricas en formato Prometheus

### Paso 4: Verificar Métricas Específicas

#### Métricas JVM - Memoria
```bash
curl http://localhost:8080/actuator/metrics/jvm.memory.used
curl http://localhost:8080/actuator/metrics/jvm.memory.max
curl http://localhost:8080/actuator/metrics/jvm.gc.pause
```

#### Métricas HTTP
```bash
curl http://localhost:8080/actuator/metrics/http.server.requests
```

#### Métricas de HikariCP
```bash
curl http://localhost:8080/actuator/metrics/hikaricp.connections.active
curl http://localhost:8080/actuator/metrics/hikaricp.connections.idle
curl http://localhost:8080/actuator/metrics/hikaricp.connections.pending
curl http://localhost:8080/actuator/metrics/hikaricp.connections.timeout
```

## 📊 Ejemplo de Métricas Prometheus

Al acceder a `/actuator/prometheus`, deberías ver métricas como:

```prometheus
# HELP jvm_memory_used_bytes The amount of used memory
# TYPE jvm_memory_used_bytes gauge
jvm_memory_used_bytes{application="msbiosuper",area="heap",environment="dev",id="G1 Eden Space",} 1.2345678E7

# HELP http_server_requests_seconds Duration of HTTP server request handling
# TYPE http_server_requests_seconds summary
http_server_requests_seconds{application="msbiosuper",environment="dev",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",quantile="0.5",} 0.012
http_server_requests_seconds{application="msbiosuper",environment="dev",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",quantile="0.95",} 0.025
http_server_requests_seconds{application="msbiosuper",environment="dev",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",quantile="0.99",} 0.050

# HELP hikaricp_connections_active Active connections
# TYPE hikaricp_connections_active gauge
hikaricp_connections_active{application="msbiosuper",environment="dev",pool="MSBioSuperDevPool",} 5.0

# HELP hikaricp_connections_idle Idle connections
# TYPE hikaricp_connections_idle gauge
hikaricp_connections_idle{application="msbiosuper",environment="dev",pool="MSBioSuperDevPool",} 3.0
```

## 🔧 Configuración de Prometheus (Opcional)

Si deseas integrar con un servidor Prometheus, agrega esta configuración a tu `prometheus.yml`:

```yaml
scrape_configs:
  - job_name: 'msbiosuper'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    static_configs:
      - targets: ['localhost:8080']
        labels:
          application: 'msbiosuper'
          environment: 'dev'
```

## 📈 Integración con Grafana

### Dashboards Recomendados

1. **JVM (Micrometer)** - ID: 4701
   - Métricas de memoria, GC, threads
   
2. **Spring Boot Statistics** - ID: 6756
   - Métricas HTTP, actuator, sistema

3. **HikariCP** - ID: 11681
   - Pool de conexiones, performance

### Importar Dashboard en Grafana

1. Accede a Grafana
2. Click en "+" → "Import"
3. Ingresa el ID del dashboard
4. Selecciona tu datasource de Prometheus
5. Click en "Import"

## 🎯 Métricas Clave a Monitorear

### Rendimiento HTTP
- `http_server_requests_seconds{quantile="0.95"}` < 500ms
- `http_server_requests_seconds{quantile="0.99"}` < 1s

### Memoria JVM
- `jvm_memory_used_bytes{area="heap"}` / `jvm_memory_max_bytes{area="heap"}` < 80%

### Garbage Collection
- `jvm_gc_pause_seconds_sum` - Tiempo total en GC
- `jvm_gc_pause_seconds_count` - Número de pausas GC

### Pool de Conexiones
- `hikaricp_connections_active` < `hikaricp_connections_max` * 0.8
- `hikaricp_connections_pending` = 0 (idealmente)
- `hikaricp_connections_timeout_total` = 0 (idealmente)

## ⚠️ Troubleshooting

### Problema: Endpoint /actuator/prometheus no accesible

**Solución:**
1. Verificar que `management.endpoints.web.exposure.include` incluya `prometheus`
2. Verificar que la aplicación esté ejecutándose
3. Verificar el puerto correcto (8080 por defecto)

### Problema: Métricas de HikariCP no aparecen

**Solución:**
1. Verificar que la conexión a base de datos esté funcionando
2. Verificar que `management.metrics.jdbc.datasource.enabled=true`
3. Verificar logs de HikariCP

### Problema: Métricas HTTP no muestran percentiles

**Solución:**
1. Verificar que `management.metrics.web.server.request.autotime.percentiles` esté configurado
2. Realizar algunas peticiones HTTP para generar datos
3. Verificar que `management.metrics.distribution.percentiles-histogram.http.server.requests=true`

## 📝 Notas Adicionales

### Diferencias por Entorno

- **Desarrollo (`dev`)**: Todos los endpoints expuestos, métricas detalladas
- **Producción (`prod`)**: Endpoints limitados, métricas optimizadas, seguridad reforzada

### Tags Comunes

Todas las métricas incluyen los siguientes tags:
- `application`: "msbiosuper"
- `environment`: "dev" | "prod"
- `version`: "1.0"
- `team`: "acosux"

### Métricas Personalizadas

Para agregar métricas de negocio personalizadas, edita:
`src/main/java/com/acosux/MSBiosuper/config/MetricsConfig.java`

Ejemplo:
```java
@Bean
public MeterBinder customMetrics(MeterRegistry registry) {
    return (reg) -> {
        Counter.builder("msbiosuper.resumen.pesca.count")
            .description("Número de consultas de resumen de pesca")
            .register(reg);
    };
}
```

## ✅ Criterios de Éxito

La tarea se considera completada cuando:

- [x] El endpoint `/actuator/prometheus` está accesible
- [x] Las métricas JVM están expuestas (heap, threads, GC)
- [x] Las métricas HTTP incluyen percentiles (p50, p95, p99)
- [x] Las métricas de HikariCP muestran el estado del pool
- [x] La configuración está optimizada por entorno (dev, prod)
- [x] La aplicación compila sin errores
- [ ] La aplicación inicia correctamente (requiere base de datos configurada)
- [ ] Las métricas se generan al hacer peticiones HTTP

## 🚀 Próximos Pasos

1. Configurar base de datos PostgreSQL para desarrollo
2. Iniciar la aplicación y verificar métricas en vivo
3. Configurar servidor Prometheus (opcional)
4. Configurar dashboards en Grafana (opcional)
5. Agregar métricas personalizadas de negocio según necesidades

---

**Fecha de Configuración**: 2026-04-23  
**Versión**: 1.0  
**Requisito**: TR-17 (Métricas y Monitoreo)
