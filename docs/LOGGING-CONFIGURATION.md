# Configuración de Logging - MSBioSuper

## Descripción General

Este documento describe la configuración de logging implementada en MSBioSuper, incluyendo los niveles de log por paquete, configuración por entorno, y mejores prácticas.

## Arquitectura de Logging

MSBioSuper utiliza **Logback** con **logstash-logback-encoder** para generar logs estructurados en formato JSON, facilitando la agregación y análisis en sistemas como ELK Stack, Splunk, o servicios de logging en la nube.

### Componentes Principales

1. **Logback**: Framework de logging principal
2. **SLF4J**: API de logging utilizada en el código
3. **Logstash Encoder**: Generación de logs en formato JSON
4. **MDC (Mapped Diagnostic Context)**: Contexto para correlation ID

## Niveles de Log

### Jerarquía de Niveles

De menor a mayor severidad:

1. **TRACE**: Información muy detallada, útil para debugging profundo
2. **DEBUG**: Información de debugging, útil para desarrollo
3. **INFO**: Mensajes informativos sobre el flujo normal de la aplicación
4. **WARN**: Situaciones potencialmente problemáticas
5. **ERROR**: Errores que permiten que la aplicación continúe ejecutándose
6. **FATAL**: Errores críticos que pueden causar que la aplicación se detenga

## Configuración por Paquete

### Aplicación MSBioSuper

| Paquete | Desarrollo | Pruebas | Producción | Descripción |
|---------|-----------|---------|------------|-------------|
| `com.acosux.MSBiosuper` | DEBUG | INFO | INFO | Nivel general de la aplicación |
| `com.acosux.MSBiosuper.domain` | DEBUG | INFO | INFO | Capa de dominio (lógica de negocio) |
| `com.acosux.MSBiosuper.application` | DEBUG | INFO | INFO | Capa de aplicación (casos de uso) |
| `com.acosux.MSBiosuper.infrastructure` | DEBUG | INFO | INFO | Capa de infraestructura |
| `com.acosux.MSBiosuper.infrastructure.rest` | DEBUG | INFO | INFO | Controladores REST |
| `com.acosux.MSBiosuper.infrastructure.security` | DEBUG | INFO | INFO | Seguridad (JWT, filtros) |
| `com.acosux.MSBiosuper.infrastructure.config` | DEBUG | INFO | INFO | Configuración de Spring |

### Spring Framework

| Paquete | Desarrollo | Pruebas | Producción | Descripción |
|---------|-----------|---------|------------|-------------|
| `org.springframework` | INFO | WARN | WARN | Framework Spring general |
| `org.springframework.boot` | INFO | WARN | WARN | Spring Boot |
| `org.springframework.web` | DEBUG | WARN | WARN | Spring Web MVC |
| `org.springframework.web.servlet.mvc` | DEBUG | WARN | WARN | Controladores y handlers |
| `org.springframework.security` | DEBUG | WARN | WARN | Spring Security |
| `org.springframework.security.web` | DEBUG | WARN | WARN | Filtros de seguridad |
| `org.springframework.data.jpa` | DEBUG | WARN | WARN | Spring Data JPA |
| `org.springframework.transaction` | DEBUG | WARN | WARN | Gestión de transacciones |
| `org.springframework.jdbc` | DEBUG | WARN | WARN | Operaciones JDBC |
| `org.springframework.context` | INFO | WARN | WARN | Contexto de aplicación |

### Hibernate

| Paquete | Desarrollo | Pruebas | Producción | Descripción |
|---------|-----------|---------|------------|-------------|
| `org.hibernate` | INFO | WARN | WARN | Hibernate general |
| `org.hibernate.SQL` | DEBUG | WARN | WARN | SQL generado |
| `org.hibernate.type.descriptor.sql.BasicBinder` | TRACE | WARN | WARN | Valores de parámetros SQL |
| `org.hibernate.type.descriptor.sql.BasicExtractor` | TRACE | WARN | WARN | Valores extraídos de SQL |
| `org.hibernate.engine.transaction.internal.TransactionImpl` | DEBUG | WARN | WARN | Transacciones |
| `org.hibernate.cache` | DEBUG | WARN | WARN | Caché de Hibernate |
| `org.hibernate.stat` | DEBUG | WARN | WARN | Estadísticas |
| `org.hibernate.engine` | INFO | WARN | WARN | Motor interno |

### HikariCP (Pool de Conexiones)

| Paquete | Desarrollo | Pruebas | Producción | Descripción |
|---------|-----------|---------|------------|-------------|
| `com.zaxxer.hikari` | DEBUG | WARN | WARN | HikariCP general |
| `com.zaxxer.hikari.HikariConfig` | DEBUG | WARN | WARN | Configuración del pool |
| `com.zaxxer.hikari.pool` | DEBUG | WARN | WARN | Operaciones del pool |
| `com.zaxxer.hikari.pool.ProxyLeakTask` | WARN | WARN | WARN | Detección de fugas |

### Otras Librerías

| Paquete | Desarrollo | Pruebas | Producción | Descripción |
|---------|-----------|---------|------------|-------------|
| `org.postgresql` | INFO | WARN | WARN | Driver PostgreSQL |
| `com.fasterxml.jackson` | INFO | WARN | WARN | Serialización JSON |
| `org.apache.catalina` | INFO | WARN | WARN | Tomcat embebido |
| `org.apache.coyote` | INFO | WARN | WARN | Conector HTTP |
| `org.springdoc` | DEBUG | WARN | WARN | OpenAPI/Swagger |
| `io.micrometer` | DEBUG | WARN | WARN | Métricas |
| `org.springframework.boot.actuate` | DEBUG | WARN | WARN | Actuator |

## Configuración por Entorno

### Desarrollo (dev)

**Objetivo**: Máxima visibilidad para debugging

**Características**:
- Nivel DEBUG para la aplicación
- Nivel DEBUG para Spring Web y Security
- Nivel DEBUG para SQL de Hibernate
- Nivel TRACE para parámetros SQL
- Formato de consola con colores
- Logs legibles en texto plano

**Activación**:
```bash
--spring.profiles.active=dev
# o
export SPRING_PROFILES_ACTIVE=dev
```

### Pruebas (test)

**Objetivo**: Minimizar ruido en logs de pruebas

**Características**:
- Nivel WARN para root
- Nivel INFO para la aplicación
- Nivel WARN para frameworks
- Formato JSON para integración con CI/CD
- Sin colores en consola

**Activación**:
```bash
--spring.profiles.active=test
# o
export SPRING_PROFILES_ACTIVE=test
```

### Producción (prod)

**Objetivo**: Logs mínimos con información crítica

**Características**:
- Nivel WARN para root
- Nivel INFO para la aplicación
- Nivel WARN para frameworks
- Formato JSON estructurado
- Rotación de archivos de log
- Logs en `/var/log/msbiosuper/application.log`

**Activación**:
```bash
--spring.profiles.active=prod
# o
export SPRING_PROFILES_ACTIVE=prod
```

## Archivos de Configuración

### Ubicación

```
src/main/resources/
├── logback-spring.xml              # Configuración principal de Logback
├── application.properties          # Configuración base de logging
├── application-dev.properties      # Configuración de desarrollo
├── application-test.properties     # Configuración de pruebas
└── application-prod.properties     # Configuración de producción
```

### Precedencia

La configuración se aplica en el siguiente orden (de menor a mayor prioridad):

1. `application.properties` (base)
2. `application-{profile}.properties` (específico del perfil)
3. Variables de entorno
4. Argumentos de línea de comandos

## Formato de Logs

### Formato JSON (Producción y Pruebas)

```json
{
  "timestamp": "2026-04-24T10:30:45.123-05:00",
  "level": "INFO",
  "thread": "http-nio-8080-exec-1",
  "logger": "com.acosux.MSBiosuper.infrastructure.rest.MSBioSuperController",
  "message": "Processing request for resumen pesca",
  "application": "msbiosuper",
  "environment": "prod",
  "mdc": {
    "correlationId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
  }
}
```

### Formato Texto (Desarrollo)

```
2026-04-24 10:30:45.123  INFO 12345 --- [nio-8080-exec-1] c.a.M.i.r.MSBioSuperController : Processing request for resumen pesca
```

## Correlation ID

Cada request HTTP recibe un **Correlation ID** único que se propaga a través de todos los logs relacionados con ese request.

### Implementación

El `CorrelationIdFilter` genera o extrae el correlation ID del header `X-Correlation-ID`:

```java
String correlationId = request.getHeader("X-Correlation-ID");
if (correlationId == null || correlationId.isEmpty()) {
    correlationId = UUID.randomUUID().toString();
}
MDC.put("correlationId", correlationId);
```

### Uso en Logs

El correlation ID se incluye automáticamente en todos los logs:

```json
{
  "mdc": {
    "correlationId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
  }
}
```

## Rotación de Logs

### Configuración (Producción)

```properties
# Archivo de logs
logging.file.name=/var/log/msbiosuper/application.log

# Tamaño máximo por archivo
logging.file.max-size=10MB

# Días de retención
logging.file.max-history=30

# Tamaño total máximo
logging.file.total-size-cap=1GB
```

### Política de Rotación

- **Rotación por tamaño**: Cuando un archivo alcanza 10MB
- **Rotación por tiempo**: Diariamente
- **Retención**: 30 días
- **Compresión**: Archivos rotados se comprimen con gzip
- **Límite total**: 1GB de logs totales

### Archivos Generados

```
/var/log/msbiosuper/
├── application.log                    # Archivo actual
├── application-2026-04-23.0.log.gz   # Rotado por fecha
├── application-2026-04-23.1.log.gz   # Rotado por tamaño
├── application-2026-04-22.0.log.gz
└── ...
```

## Mejores Prácticas

### 1. Uso de Niveles de Log

```java
// ✅ CORRECTO: Usar nivel apropiado
logger.info("User {} logged in successfully", username);
logger.debug("Processing request with parameters: {}", params);
logger.warn("Connection pool is running low: {} connections available", available);
logger.error("Failed to process payment for order {}", orderId, exception);

// ❌ INCORRECTO: Nivel inapropiado
logger.error("User logged in"); // Debería ser INFO
logger.info("NullPointerException occurred", ex); // Debería ser ERROR
```

### 2. Uso de Placeholders

```java
// ✅ CORRECTO: Usar placeholders
logger.info("Processing order {} for user {}", orderId, userId);

// ❌ INCORRECTO: Concatenación de strings
logger.info("Processing order " + orderId + " for user " + userId);
```

### 3. Logging de Excepciones

```java
// ✅ CORRECTO: Incluir excepción como último parámetro
logger.error("Failed to process payment for order {}", orderId, exception);

// ❌ INCORRECTO: No incluir excepción
logger.error("Failed to process payment: " + exception.getMessage());
```

### 4. Información Sensible

```java
// ✅ CORRECTO: Enmascarar información sensible
logger.info("User {} authenticated with token ***", username);

// ❌ INCORRECTO: Exponer información sensible
logger.info("User {} authenticated with token {}", username, jwtToken);
```

### 5. Logging en Loops

```java
// ✅ CORRECTO: Log agregado fuera del loop
logger.info("Processed {} orders successfully", processedCount);

// ❌ INCORRECTO: Log en cada iteración
for (Order order : orders) {
    logger.info("Processing order {}", order.getId()); // Genera demasiados logs
}
```

## Monitoreo y Análisis

### Herramientas Recomendadas

1. **ELK Stack** (Elasticsearch, Logstash, Kibana)
   - Agregación de logs
   - Búsqueda y análisis
   - Visualización de dashboards

2. **Splunk**
   - Análisis avanzado de logs
   - Alertas en tiempo real
   - Correlación de eventos

3. **Grafana Loki**
   - Agregación de logs ligera
   - Integración con Prometheus
   - Consultas con LogQL

### Queries Útiles

#### Buscar por Correlation ID
```
correlationId:"a1b2c3d4-e5f6-7890-abcd-ef1234567890"
```

#### Buscar errores en producción
```
level:ERROR AND environment:prod
```

#### Buscar logs de un usuario específico
```
message:*user123* AND level:INFO
```

## Troubleshooting

### Problema: No se generan logs

**Solución**:
1. Verificar que el perfil esté activo: `echo $SPRING_PROFILES_ACTIVE`
2. Verificar permisos del directorio de logs: `ls -la /var/log/msbiosuper/`
3. Verificar configuración de Logback: `src/main/resources/logback-spring.xml`

### Problema: Demasiados logs en producción

**Solución**:
1. Verificar niveles de log en `application-prod.properties`
2. Cambiar nivel root a ERROR: `logging.level.root=ERROR`
3. Reducir nivel de frameworks: `logging.level.org.springframework=ERROR`

### Problema: Logs no tienen correlation ID

**Solución**:
1. Verificar que `CorrelationIdFilter` esté registrado
2. Verificar que MDC esté configurado en Logback
3. Verificar que `includeMdc=true` en `logback-spring.xml`

## Referencias

- [Logback Documentation](https://logback.qos.ch/documentation.html)
- [SLF4J Manual](https://www.slf4j.org/manual.html)
- [Logstash Logback Encoder](https://github.com/logfellow/logstash-logback-encoder)
- [Spring Boot Logging](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging)

---

**Versión**: 1.0  
**Fecha**: 2026-04-24  
**Autor**: Josias Rubén Aldaz Arcalles
