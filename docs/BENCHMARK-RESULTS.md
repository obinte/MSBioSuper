# Resultados de Benchmarks de Rendimiento - MSBioSuper

## Información del Documento

- **Proyecto**: MSBioSuper Java 21 Migration
- **Versión**: 1.0
- **Fecha de Creación**: 2026-04-24
- **Última Actualización**: 2026-04-24
- **Autor**: Josias Rubén Aldaz Arcalles

---

## 1. Resumen Ejecutivo

Este documento registra los resultados de los benchmarks de rendimiento base para el microservicio MSBioSuper. Los benchmarks establecen líneas base de rendimiento que se utilizarán para:

1. Validar que el sistema cumple con los objetivos de rendimiento (TR-22, NFR-1, NFR-2)
2. Detectar regresiones de rendimiento en futuras versiones
3. Identificar cuellos de botella y oportunidades de optimización
4. Planificar capacidad y escalamiento

### Objetivos de Rendimiento

Según los requisitos TR-22, NFR-1 y NFR-2:

| Métrica | Objetivo | Crítico |
|---------|----------|---------|
| Tiempo de respuesta API (p50) | < 200ms | < 500ms |
| Tiempo de respuesta API (p95) | < 500ms | < 1000ms |
| Tiempo de respuesta API (p99) | < 1000ms | < 2000ms |
| Throughput | > 1000 req/sec | > 500 req/sec |
| Tiempo de consulta DB (p95) | < 100ms | < 200ms |
| Uso de memoria bajo carga | < 512MB | < 1GB |
| Tasa de éxito | > 95% | > 90% |

---

## 2. Configuración del Entorno de Prueba

### 2.1 Hardware

**Servidor de Aplicación**:
- CPU: [Especificar modelo y cores]
- RAM: [Especificar cantidad]
- Disco: [Especificar tipo y capacidad]
- Sistema Operativo: [Especificar OS y versión]

**Servidor de Base de Datos**:
- CPU: [Especificar modelo y cores]
- RAM: [Especificar cantidad]
- Disco: [Especificar tipo y capacidad]
- Sistema Operativo: [Especificar OS y versión]

**Cliente de Pruebas (Gatling)**:
- CPU: [Especificar modelo y cores]
- RAM: [Especificar cantidad]
- Sistema Operativo: [Especificar OS y versión]

### 2.2 Software

**Aplicación**:
- Java Version: 21 (OpenJDK/Oracle JDK)
- Spring Boot Version: 3.3.x
- Hibernate Version: 6.5.x
- Configuración JVM: `-Xmx512m -Xms256m`

**Base de Datos**:
- PostgreSQL Version: [Especificar versión]
- Configuración: [Especificar parámetros relevantes]
- Pool de Conexiones: HikariCP (max: 20, min: 5)

**Herramienta de Pruebas**:
- Gatling Version: [Especificar versión]
- Maven Version: 3.9.x

### 2.3 Configuración de Red

- Latencia de red: [Especificar latencia entre cliente y servidor]
- Ancho de banda: [Especificar ancho de banda disponible]
- Topología: [Especificar si es local, LAN, WAN, etc.]

### 2.4 Datos de Prueba

- Volumen de datos en BD: [Especificar cantidad de registros por tabla]
- Rango de fechas: 2024-01-01 a 2024-12-31
- Empresas: [Especificar cantidad]
- Piscinas: [Especificar cantidad]

---

## 3. Resultados de Benchmark Base

### 3.1 Ejecución del Benchmark

**Fecha de Ejecución**: [YYYY-MM-DD HH:MM:SS]  
**Duración**: 5 minutos (300 segundos)  
**Usuarios Concurrentes**: 50  
**Comando de Ejecución**:
```bash
mvn gatling:test -Dgatling.simulationClass=msbiosuper.BenchmarkSimulation \
  -Dbenchmark.users=50 \
  -Dbenchmark.duration=300 \
  -Dtest.jwt.token="<token>"
```

### 3.2 Métricas Globales

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total de Requests | [Completar] | - | - |
| Requests Exitosos | [Completar] | - | > 95% |
| Requests Fallidos | [Completar] | - | < 5% |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Throughput (req/sec) | [Completar] | [✅/⚠️/❌] | > 50 |
| Tiempo de Respuesta Mínimo | [Completar]ms | - | - |
| Tiempo de Respuesta Máximo | [Completar]ms | [✅/⚠️/❌] | < 5000ms |
| Tiempo de Respuesta Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Tiempo de Respuesta p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| Tiempo de Respuesta p75 | [Completar]ms | - | - |
| Tiempo de Respuesta p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| Tiempo de Respuesta p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |

**Leyenda**:
- ✅ PASS: Cumple con el objetivo
- ⚠️ WARNING: Entre objetivo y crítico
- ❌ FAIL: No cumple con el umbral crítico

### 3.3 Métricas por Endpoint

#### 3.3.1 Resumen de Pesca

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.2 Resumen de Siembra

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.3 Consumos Diarios

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.4 Liquidación Detalle Producto

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.5 Listar Liquidaciones Detalle

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.6 Costos por Piscina

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.7 Obtener Gramajes

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.8 Compras Detalle Producto

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.9 Listar Ventas BioSuper

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.10 Productos General

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.11 Cuentas por Cobrar

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

#### 3.3.12 Cuentas por Pagar

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Total Requests | [Completar] | - | - |
| Tasa de Éxito | [Completar]% | [✅/⚠️/❌] | > 95% |
| Tiempo Medio | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| p50 | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| p95 | [Completar]ms | [✅/⚠️/❌] | < 500ms |
| p99 | [Completar]ms | [✅/⚠️/❌] | < 1000ms |
| Requests/sec | [Completar] | - | - |

**Observaciones**: [Agregar observaciones específicas]

---

## 4. Métricas de Base de Datos

### 4.1 Rendimiento de Consultas

**Fuente**: Métricas de Actuator (`/actuator/metrics/hikaricp.*`) y logs de PostgreSQL

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Conexiones Activas (promedio) | [Completar] | [✅/⚠️/❌] | < 20 |
| Conexiones Activas (máximo) | [Completar] | [✅/⚠️/❌] | < 20 |
| Tiempo de Espera de Conexión (p95) | [Completar]ms | [✅/⚠️/❌] | < 50ms |
| Tiempo de Consulta (p50) | [Completar]ms | - | - |
| Tiempo de Consulta (p95) | [Completar]ms | [✅/⚠️/❌] | < 100ms |
| Tiempo de Consulta (p99) | [Completar]ms | [✅/⚠️/❌] | < 200ms |
| Queries Lentas (> 1s) | [Completar] | [✅/⚠️/❌] | 0 |

### 4.2 Top 5 Consultas Más Lentas

| Ranking | Query | Tiempo Promedio | Ejecuciones | Observaciones |
|---------|-------|-----------------|-------------|---------------|
| 1 | [Completar] | [Completar]ms | [Completar] | [Completar] |
| 2 | [Completar] | [Completar]ms | [Completar] | [Completar] |
| 3 | [Completar] | [Completar]ms | [Completar] | [Completar] |
| 4 | [Completar] | [Completar]ms | [Completar] | [Completar] |
| 5 | [Completar] | [Completar]ms | [Completar] | [Completar] |

**Recomendaciones de Optimización**:
- [Agregar recomendaciones basadas en los resultados]

---

## 5. Métricas de Recursos del Sistema

### 5.1 Uso de Memoria JVM

**Fuente**: Métricas de Actuator (`/actuator/metrics/jvm.memory.*`)

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| Heap Usado (promedio) | [Completar]MB | [✅/⚠️/❌] | < 512MB |
| Heap Usado (máximo) | [Completar]MB | [✅/⚠️/❌] | < 512MB |
| Heap Comprometido | [Completar]MB | - | - |
| Non-Heap Usado | [Completar]MB | - | - |
| Garbage Collections (count) | [Completar] | - | - |
| GC Time (total) | [Completar]ms | - | - |
| GC Time (promedio por colección) | [Completar]ms | [✅/⚠️/❌] | < 100ms |

### 5.2 Uso de CPU

**Fuente**: Métricas del sistema operativo

| Métrica | Valor | Estado | Objetivo |
|---------|-------|--------|----------|
| CPU Uso (promedio) | [Completar]% | [✅/⚠️/❌] | < 50% |
| CPU Uso (máximo) | [Completar]% | [✅/⚠️/❌] | < 80% |
| Load Average (1 min) | [Completar] | - | - |
| Load Average (5 min) | [Completar] | - | - |
| Load Average (15 min) | [Completar] | - | - |

### 5.3 Threads

**Fuente**: Métricas de Actuator (`/actuator/metrics/jvm.threads.*`)

| Métrica | Valor | Observaciones |
|---------|-------|---------------|
| Threads Activos | [Completar] | - |
| Threads Daemon | [Completar] | - |
| Threads Peak | [Completar] | - |
| Threads Bloqueados | [Completar] | Debe ser 0 o muy bajo |
| Threads Esperando | [Completar] | - |

---

## 6. Análisis y Conclusiones

### 6.1 Resumen de Cumplimiento de Objetivos

| Categoría | Objetivos Cumplidos | Objetivos Totales | Porcentaje |
|-----------|---------------------|-------------------|------------|
| Tiempos de Respuesta | [Completar] | [Completar] | [Completar]% |
| Throughput | [Completar] | [Completar] | [Completar]% |
| Base de Datos | [Completar] | [Completar] | [Completar]% |
| Recursos del Sistema | [Completar] | [Completar] | [Completar]% |
| **TOTAL** | [Completar] | [Completar] | [Completar]% |

### 6.2 Endpoints con Mejor Rendimiento

1. [Endpoint]: [Métrica destacada]
2. [Endpoint]: [Métrica destacada]
3. [Endpoint]: [Métrica destacada]

### 6.3 Endpoints que Requieren Optimización

1. [Endpoint]: [Problema identificado] - [Recomendación]
2. [Endpoint]: [Problema identificado] - [Recomendación]
3. [Endpoint]: [Problema identificado] - [Recomendación]

### 6.4 Cuellos de Botella Identificados

1. **[Categoría]**: [Descripción del cuello de botella]
   - **Impacto**: [Alto/Medio/Bajo]
   - **Causa Raíz**: [Descripción]
   - **Recomendación**: [Acción sugerida]

2. **[Categoría]**: [Descripción del cuello de botella]
   - **Impacto**: [Alto/Medio/Bajo]
   - **Causa Raíz**: [Descripción]
   - **Recomendación**: [Acción sugerida]

### 6.5 Recomendaciones de Optimización

#### Prioridad Alta
1. [Recomendación con impacto significativo]
2. [Recomendación con impacto significativo]

#### Prioridad Media
1. [Recomendación con impacto moderado]
2. [Recomendación con impacto moderado]

#### Prioridad Baja
1. [Recomendación con impacto menor]
2. [Recomendación con impacto menor]

---

## 7. Comparación con Versión Anterior (Java 8)

### 7.1 Mejoras de Rendimiento

| Métrica | Java 8 | Java 21 | Mejora |
|---------|--------|---------|--------|
| Tiempo de Respuesta p95 | [Completar]ms | [Completar]ms | [Completar]% |
| Throughput | [Completar] req/s | [Completar] req/s | [Completar]% |
| Uso de Memoria | [Completar]MB | [Completar]MB | [Completar]% |
| Tiempo de Startup | [Completar]s | [Completar]s | [Completar]% |

### 7.2 Análisis de Mejoras

[Agregar análisis detallado de las mejoras observadas con la migración a Java 21]

---

## 8. Próximos Pasos

### 8.1 Acciones Inmediatas

- [ ] [Acción 1]
- [ ] [Acción 2]
- [ ] [Acción 3]

### 8.2 Monitoreo Continuo

- [ ] Configurar alertas de rendimiento en Prometheus/Grafana
- [ ] Establecer dashboard de métricas en tiempo real
- [ ] Programar ejecución semanal de benchmarks
- [ ] Configurar detección automática de regresiones

### 8.3 Optimizaciones Planificadas

- [ ] [Optimización 1] - [Fecha estimada]
- [ ] [Optimización 2] - [Fecha estimada]
- [ ] [Optimización 3] - [Fecha estimada]

---

## 9. Anexos

### 9.1 Enlaces a Reportes Completos

- Reporte Gatling HTML: `target/gatling/benchmarksimulation-[timestamp]/index.html`
- Logs de Aplicación: `logs/application-[date].log`
- Métricas Prometheus: [URL del endpoint]
- Dashboard Grafana: [URL del dashboard]

### 9.2 Comandos de Ejecución

```bash
# Generar token JWT
curl -X POST http://localhost:8080/api/v1/security/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "testpass123"}'

# Ejecutar benchmark
mvn gatling:test -Dgatling.simulationClass=msbiosuper.BenchmarkSimulation \
  -Dbenchmark.users=50 \
  -Dbenchmark.duration=300 \
  -Dtest.jwt.token="<token>"

# Consultar métricas de Actuator
curl http://localhost:8080/actuator/metrics/hikaricp.connections.active
curl http://localhost:8080/actuator/metrics/jvm.memory.used
curl http://localhost:8080/actuator/metrics/http.server.requests
```

### 9.3 Configuración de PostgreSQL

```sql
-- Habilitar pg_stat_statements para análisis de queries
CREATE EXTENSION IF NOT EXISTS pg_stat_statements;

-- Consultar queries más lentas
SELECT 
  query,
  calls,
  total_exec_time,
  mean_exec_time,
  max_exec_time
FROM pg_stat_statements
ORDER BY mean_exec_time DESC
LIMIT 10;
```

---

## 10. Historial de Cambios

| Fecha | Versión | Autor | Cambios |
|-------|---------|-------|---------|
| 2026-04-24 | 1.0 | Josias Rubén Aldaz Arcalles | Creación inicial del documento |

---

**Nota**: Este documento debe actualizarse después de cada ejecución de benchmarks para mantener un registro histórico del rendimiento del sistema.
