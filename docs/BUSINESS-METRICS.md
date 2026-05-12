# Métricas de Negocio - MSBioSuper

## Descripción General

Este documento describe las métricas de negocio personalizadas implementadas en MSBioSuper para monitorear operaciones críticas y el rendimiento de los endpoints API.

Las métricas se exponen a través de Prometheus en el endpoint `/actuator/prometheus` y pueden ser visualizadas en Grafana u otras herramientas de monitoreo.

---

## Arquitectura de Métricas

### Componentes

1. **BusinessMetricsService**: Servicio central que registra todas las métricas de negocio
2. **ApiMetricsAspect**: Aspecto AOP que intercepta automáticamente las llamadas a los controladores
3. **MetricsConfig**: Configuración de Micrometer y tags comunes

### Flujo de Captura de Métricas

```
Cliente → Controlador → ApiMetricsAspect (intercepta) → Caso de Uso
                              ↓
                    BusinessMetricsService (registra métricas)
                              ↓
                    Micrometer → Prometheus
```

---

## Métricas Disponibles

### 1. Contadores de Operaciones

Estas métricas cuentan el número total de invocaciones de cada operación de negocio.

| Métrica | Descripción | Tags |
|---------|-------------|------|
| `msbiosuper.business.resumen_pesca.count` | Consultas de resumen de pesca | `operation=resumen_pesca` |
| `msbiosuper.business.resumen_siembra.count` | Consultas de resumen de siembra | `operation=resumen_siembra` |
| `msbiosuper.business.consumos_diarios.count` | Consultas de consumos diarios | `operation=consumos_diarios` |
| `msbiosuper.business.liquidaciones.count` | Consultas de liquidaciones | `operation=liquidaciones` |
| `msbiosuper.business.ventas.count` | Consultas de ventas | `operation=ventas` |
| `msbiosuper.business.cuentas_por_cobrar.count` | Consultas de cuentas por cobrar | `operation=cuentas_por_cobrar` |
| `msbiosuper.business.cuentas_por_pagar.count` | Consultas de cuentas por pagar | `operation=cuentas_por_pagar` |
| `msbiosuper.business.productos.count` | Consultas de productos | `operation=productos` |

**Ejemplo de uso en Prometheus**:
```promql
# Tasa de consultas de resumen de pesca por segundo
rate(msbiosuper_business_resumen_pesca_count_total[5m])

# Total de consultas en las últimas 24 horas
increase(msbiosuper_business_resumen_pesca_count_total[24h])
```

---

### 2. Métricas de Latencia (Timers)

Estas métricas miden el tiempo de ejecución de cada operación de negocio.

| Métrica | Descripción | Tags |
|---------|-------------|------|
| `msbiosuper.business.resumen_pesca.duration` | Tiempo de ejecución de resumen de pesca | `operation=resumen_pesca` |
| `msbiosuper.business.resumen_siembra.duration` | Tiempo de ejecución de resumen de siembra | `operation=resumen_siembra` |
| `msbiosuper.business.consumos_diarios.duration` | Tiempo de ejecución de consumos diarios | `operation=consumos_diarios` |
| `msbiosuper.business.liquidaciones.duration` | Tiempo de ejecución de liquidaciones | `operation=liquidaciones` |
| `msbiosuper.business.ventas.duration` | Tiempo de ejecución de ventas | `operation=ventas` |
| `msbiosuper.business.cuentas_por_cobrar.duration` | Tiempo de ejecución de cuentas por cobrar | `operation=cuentas_por_cobrar` |
| `msbiosuper.business.cuentas_por_pagar.duration` | Tiempo de ejecución de cuentas por pagar | `operation=cuentas_por_pagar` |

**Métricas generadas automáticamente por cada timer**:
- `_count`: Número de mediciones
- `_sum`: Suma total de todas las duraciones
- `_max`: Duración máxima registrada
- Percentiles: p50, p95, p99 (si están configurados)

**Ejemplo de uso en Prometheus**:
```promql
# Latencia promedio de resumen de pesca
rate(msbiosuper_business_resumen_pesca_duration_sum[5m]) / 
rate(msbiosuper_business_resumen_pesca_duration_count[5m])

# Percentil 95 de latencia
histogram_quantile(0.95, rate(msbiosuper_business_resumen_pesca_duration_bucket[5m]))
```

---

### 3. Métricas de Resultados

| Métrica | Descripción | Tags |
|---------|-------------|------|
| `msbiosuper.business.result_count` | Número de resultados devueltos por operación | `operation=<nombre>` |

**Ejemplo de uso en Prometheus**:
```promql
# Promedio de resultados por consulta de resumen de pesca
rate(msbiosuper_business_result_count_total{operation="resumen_pesca"}[5m]) /
rate(msbiosuper_business_resumen_pesca_count_total[5m])
```

---

### 4. Métricas de Errores

| Métrica | Descripción | Tags |
|---------|-------------|------|
| `msbiosuper.business.errors` | Errores en operaciones de negocio | `operation=<nombre>`, `error_type=<tipo>` |

**Ejemplo de uso en Prometheus**:
```promql
# Tasa de errores por operación
rate(msbiosuper_business_errors_total[5m])

# Errores por tipo
sum by (error_type) (rate(msbiosuper_business_errors_total[5m]))
```

---

### 5. Tags Comunes

Todas las métricas incluyen los siguientes tags comunes:

| Tag | Valor | Descripción |
|-----|-------|-------------|
| `application` | `msbiosuper` | Nombre de la aplicación |
| `version` | `1.0` | Versión de la aplicación |
| `team` | `acosux` | Equipo responsable |

---

## Configuración

### Habilitar Métricas en application.properties

```properties
# Exponer endpoint de Prometheus
management.endpoints.web.exposure.include=health,info,prometheus,metrics

# Habilitar métricas de JVM
management.metrics.enable.jvm=true

# Habilitar métricas de HTTP
management.metrics.enable.http=true

# Habilitar métricas de base de datos
management.metrics.enable.jdbc=true

# Configurar percentiles para timers (opcional)
management.metrics.distribution.percentiles-histogram.http.server.requests=true
management.metrics.distribution.percentiles.http.server.requests=0.5,0.95,0.99
```

---

## Integración con Prometheus

### Configuración de Prometheus (prometheus.yml)

```yaml
scrape_configs:
  - job_name: 'msbiosuper'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    static_configs:
      - targets: ['localhost:8080']
```

### Verificar Métricas

```bash
# Verificar que el endpoint está disponible
curl http://localhost:8080/actuator/prometheus

# Filtrar métricas de negocio
curl http://localhost:8080/actuator/prometheus | grep msbiosuper_business
```

---

## Dashboards de Grafana

### Panel de Operaciones de Negocio

**Consultas recomendadas**:

1. **Tasa de Solicitudes por Operación**:
```promql
sum by (operation) (rate(msbiosuper_business_resumen_pesca_count_total[5m]))
```

2. **Latencia Promedio por Operación**:
```promql
sum by (operation) (
  rate(msbiosuper_business_resumen_pesca_duration_sum[5m]) /
  rate(msbiosuper_business_resumen_pesca_duration_count[5m])
)
```

3. **Tasa de Errores**:
```promql
sum by (operation) (rate(msbiosuper_business_errors_total[5m]))
```

4. **Distribución de Latencia (Heatmap)**:
```promql
sum(rate(msbiosuper_business_resumen_pesca_duration_bucket[5m])) by (le)
```

---

## Alertas Recomendadas

### 1. Alta Latencia

```yaml
- alert: HighApiLatency
  expr: |
    histogram_quantile(0.95, 
      rate(msbiosuper_business_resumen_pesca_duration_bucket[5m])
    ) > 500
  for: 5m
  labels:
    severity: warning
  annotations:
    summary: "Alta latencia en endpoint de resumen de pesca"
    description: "El percentil 95 de latencia es {{ $value }}ms"
```

### 2. Alta Tasa de Errores

```yaml
- alert: HighErrorRate
  expr: |
    rate(msbiosuper_business_errors_total[5m]) > 0.05
  for: 5m
  labels:
    severity: critical
  annotations:
    summary: "Alta tasa de errores en operaciones de negocio"
    description: "Tasa de errores: {{ $value }} errores/segundo"
```

### 3. Bajo Rendimiento

```yaml
- alert: LowThroughput
  expr: |
    sum(rate(msbiosuper_business_resumen_pesca_count_total[5m])) < 0.1
  for: 10m
  labels:
    severity: warning
  annotations:
    summary: "Bajo rendimiento en operaciones de negocio"
    description: "Tasa de solicitudes: {{ $value }} req/segundo"
```

---

## Métricas Estándar de Spring Boot

Además de las métricas de negocio personalizadas, Spring Boot Actuator expone automáticamente:

### Métricas de JVM
- `jvm.memory.used`: Memoria utilizada
- `jvm.memory.max`: Memoria máxima
- `jvm.threads.live`: Hilos activos
- `jvm.gc.pause`: Pausas de garbage collection

### Métricas de HTTP
- `http.server.requests`: Solicitudes HTTP
- `http.server.requests.duration`: Duración de solicitudes

### Métricas de Base de Datos
- `hikaricp.connections.active`: Conexiones activas
- `hikaricp.connections.idle`: Conexiones inactivas
- `hikaricp.connections.pending`: Conexiones pendientes

---

## Mejores Prácticas

1. **Monitorear Latencia**: Establecer alertas para latencias > 500ms (p95)
2. **Monitorear Errores**: Alertar cuando la tasa de errores > 5%
3. **Monitorear Throughput**: Detectar caídas en el volumen de solicitudes
4. **Revisar Regularmente**: Analizar métricas semanalmente para identificar tendencias
5. **Correlacionar Métricas**: Relacionar métricas de negocio con métricas de infraestructura

---

## Extensión de Métricas

Para agregar nuevas métricas de negocio:

1. **Agregar contador en BusinessMetricsService**:
```java
private final Counter nuevaOperacionCounter;

// En el constructor
this.nuevaOperacionCounter = Counter.builder("msbiosuper.business.nueva_operacion.count")
    .description("Descripción de la métrica")
    .tag("operation", "nueva_operacion")
    .register(meterRegistry);
```

2. **Agregar método público**:
```java
public void incrementNuevaOperacionCount() {
    nuevaOperacionCounter.increment();
}
```

3. **Actualizar ApiMetricsAspect** para interceptar el nuevo método:
```java
case "nuevaOperacion":
    metricsService.incrementNuevaOperacionCount();
    break;
```

---

## Troubleshooting

### Las métricas no aparecen en Prometheus

1. Verificar que el endpoint está habilitado:
```bash
curl http://localhost:8080/actuator/prometheus
```

2. Verificar configuración en application.properties:
```properties
management.endpoints.web.exposure.include=prometheus
```

3. Verificar que Prometheus está scrapeando correctamente:
```bash
# En Prometheus UI
up{job="msbiosuper"}
```

### Las métricas de negocio no se registran

1. Verificar que AspectJ está habilitado (dependencia spring-boot-starter-aop)
2. Verificar logs para errores en ApiMetricsAspect
3. Verificar que BusinessMetricsService está siendo inyectado correctamente

---

## Referencias

- [Micrometer Documentation](https://micrometer.io/docs)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Prometheus Query Language](https://prometheus.io/docs/prometheus/latest/querying/basics/)
- [Grafana Dashboards](https://grafana.com/docs/grafana/latest/dashboards/)

---

**Última actualización**: 2024-04-23  
**Versión**: 1.0  
**Autor**: MSBioSuper Team
