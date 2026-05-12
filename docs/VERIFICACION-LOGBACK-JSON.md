# Verificación de Configuración de Logback con JSON Encoder

## Resumen de la Tarea 27.1

Se ha completado exitosamente la configuración de Logback con codificador JSON para logging estructurado en MSBioSuper.

## Cambios Realizados

### 1. Dependencia Agregada al pom.xml

```xml
<!-- Logstash Logback Encoder para logging estructurado en JSON -->
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>8.0</version>
</dependency>
```

**Versión**: 8.0 (última versión estable compatible con Logback 1.4.x y Spring Boot 3.3.x)

### 2. Archivo logback-spring.xml Creado

**Ubicación**: `src/main/resources/logback-spring.xml`

**Características Implementadas**:

#### A. Appenders Configurados

1. **CONSOLE_JSON**: Appender de consola con formato JSON
   - Usa `LogstashEncoder` para formato JSON estructurado
   - Incluye campos personalizados: `application` y `environment`
   - Incluye caller data (clase y método)
   - Incluye MDC (Mapped Diagnostic Context) para correlationId y userId
   - Timestamp en formato ISO 8601

2. **FILE_JSON**: Appender de archivo con formato JSON
   - Rotación de archivos por tamaño (10MB) y tiempo (diario)
   - Mantiene logs de los últimos 30 días
   - Tamaño total máximo: 1GB
   - Compresión GZIP de archivos rotados

3. **CONSOLE_PLAIN**: Appender de consola con formato legible
   - Para desarrollo local (perfil dev)
   - Formato tradicional: `timestamp [thread] level logger - message`

#### B. Campos de Log en Formato JSON

Los logs en formato JSON incluyen los siguientes campos (según requisito TR-18):

```json
{
  "timestamp": "2026-04-23T15:30:45.123-05:00",
  "level": "INFO",
  "logger": "com.acosux.MSBiosuper.application.usecase.AuthenticateUserUseCase",
  "thread": "http-nio-8080-exec-1",
  "message": "Usuario autenticado exitosamente",
  "application": "msbiosuper",
  "environment": "prod",
  "correlationId": "abc-123-def-456",
  "userId": "user@example.com",
  "exception": "stack trace completo si hay error"
}
```

#### C. Niveles de Log por Paquete

| Paquete | Nivel | Descripción |
|---------|-------|-------------|
| `com.acosux.MSBiosuper` | INFO | Aplicación principal |
| `org.springframework` | INFO | Framework Spring |
| `org.springframework.web` | INFO | Spring Web |
| `org.springframework.security` | INFO | Spring Security |
| `org.hibernate.SQL` | DEBUG | Consultas SQL de Hibernate |
| `org.hibernate.type.descriptor.sql.BasicBinder` | TRACE | Parámetros SQL |
| `com.zaxxer.hikari` | INFO | Pool de conexiones HikariCP |
| `com.zaxxer.hikari.HikariConfig` | DEBUG | Configuración de HikariCP |

#### D. Configuración por Perfil (Profile)

**Perfil DEV (desarrollo)**:
- Usa `CONSOLE_PLAIN` (formato legible)
- Nivel DEBUG para la aplicación
- Ideal para desarrollo local

**Perfil TEST (pruebas)**:
- Usa `CONSOLE_JSON` (formato JSON)
- Nivel DEBUG para la aplicación
- Ideal para pruebas automatizadas

**Perfil PROD (producción)**:
- Usa `CONSOLE_JSON` y `FILE_JSON`
- Nivel INFO para la aplicación
- Nivel WARN para frameworks (reduce ruido)
- Ideal para producción con agregación de logs

#### E. Configuración de Stack Traces

- Máxima profundidad por excepción: 30 niveles
- Longitud máxima: 4096 caracteres
- Nombres de clase acortados a 30 caracteres
- Excluye paquetes de reflexión de Java
- Muestra causa raíz primero

## Requisitos Cumplidos

### TR-18: Structured Logging ✅

- [x] Logback configurado con codificador JSON
- [x] Dependencia logstash-logback-encoder agregada (versión 8.0)
- [x] Archivo logback-spring.xml creado
- [x] Formato JSON para logs estructurados
- [x] Niveles de log configurables por paquete
- [x] Preparado para agregación de logs (ELK, Splunk)

### Campos de Log Esperados ✅

- [x] `timestamp`: Formato ISO 8601 (`yyyy-MM-dd'T'HH:mm:ss.SSSXXX`)
- [x] `level`: DEBUG, INFO, WARN, ERROR
- [x] `logger`: Nombre de la clase
- [x] `message`: Mensaje del log
- [x] `correlationId`: ID de rastreo de solicitud (vía MDC)
- [x] `userId`: Usuario autenticado si aplica (vía MDC)
- [x] `exception`: Stack trace completo si es error

### Características Adicionales ✅

- [x] Rotación de archivos de log
- [x] Compresión de archivos antiguos
- [x] Configuración por perfil (dev, test, prod)
- [x] Formato legible para desarrollo
- [x] Formato JSON para producción
- [x] Campos personalizados (application, environment)
- [x] Inclusión de MDC para contexto adicional

## Cómo Usar

### 1. Activar Perfil de Desarrollo (logs legibles)

```bash
# En línea de comandos
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# O con variable de entorno
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

### 2. Activar Perfil de Producción (logs JSON)

```bash
# En línea de comandos
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# O con variable de entorno
export SPRING_PROFILES_ACTIVE=prod
mvn spring-boot:run
```

### 3. Agregar Correlation ID en el Código

```java
import org.slf4j.MDC;

// Al inicio de una solicitud HTTP
MDC.put("correlationId", UUID.randomUUID().toString());
MDC.put("userId", authenticatedUser.getEmail());

try {
    // Lógica de negocio
    logger.info("Procesando solicitud");
} finally {
    // Limpiar MDC al final
    MDC.clear();
}
```

### 4. Ejemplo de Uso en Código

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyService {
    private static final Logger logger = LoggerFactory.getLogger(MyService.class);
    
    public void processData(String data) {
        logger.debug("Iniciando procesamiento de datos");
        logger.info("Datos recibidos: {}", data);
        
        try {
            // Procesar datos
            logger.info("Procesamiento completado exitosamente");
        } catch (Exception e) {
            logger.error("Error al procesar datos", e);
        }
    }
}
```

## Integración con Sistemas de Agregación de Logs

### ELK Stack (Elasticsearch, Logstash, Kibana)

Los logs en formato JSON están listos para ser consumidos por Logstash:

```conf
# logstash.conf
input {
  file {
    path => "/var/log/msbiosuper/*.log"
    codec => json
  }
}

filter {
  # Los logs ya están en JSON, no necesitan parsing adicional
}

output {
  elasticsearch {
    hosts => ["localhost:9200"]
    index => "msbiosuper-logs-%{+YYYY.MM.dd}"
  }
}
```

### Splunk

Los logs JSON pueden ser ingeridos directamente por Splunk:

```conf
# inputs.conf
[monitor:///var/log/msbiosuper/*.log]
sourcetype = _json
index = msbiosuper
```

### CloudWatch Logs (AWS)

Los logs JSON son compatibles con CloudWatch Logs Insights:

```sql
fields @timestamp, level, logger, message, correlationId, userId
| filter level = "ERROR"
| sort @timestamp desc
| limit 100
```

## Archivos de Log Generados

### Ubicación

Por defecto, los logs se guardan en:
- **Directorio**: `logs/` (relativo al directorio de ejecución)
- **Archivo principal**: `logs/msbiosuper.log`
- **Archivos rotados**: `logs/msbiosuper-YYYY-MM-DD.N.log.gz`

### Configurar Ubicación Personalizada

```bash
# Variable de entorno
export LOG_PATH=/var/log/msbiosuper
mvn spring-boot:run
```

## Verificación

### 1. Compilación Exitosa

```bash
mvn clean compile -DskipTests
```

**Resultado**: ✅ BUILD SUCCESS

### 2. Dependencia Disponible

La clase `LogstashEncoder` está disponible en el classpath:

```bash
mvn dependency:tree | grep logstash
```

**Resultado**: 
```
[INFO] +- net.logstash.logback:logstash-logback-encoder:jar:8.0:compile
```

### 3. Archivo de Configuración

El archivo `logback-spring.xml` existe en `src/main/resources/`:

```bash
ls -la src/main/resources/logback-spring.xml
```

**Resultado**: ✅ Archivo existe (2.7 KB)

## Tests Creados

### 1. LogbackConfigurationTest.java

Test unitario que verifica:
- LoggerContext está configurado
- Root logger tiene appenders
- Logger de aplicación está configurado
- Logs se escriben sin errores
- LogstashEncoder está disponible

### 2. LogbackManualTest.java

Programa de prueba manual que genera:
- Logs en diferentes niveles (DEBUG, INFO, WARN, ERROR)
- Logs con MDC (correlationId, userId)
- Logs con excepciones
- Logs con parámetros

**Ejecutar**:
```bash
mvn test-compile exec:java \
  -Dexec.mainClass="com.acosux.MSBiosuper.config.LogbackManualTest" \
  -Dexec.classpathScope=test
```

## Próximos Pasos

### Tarea 27.2: Agregar ID de Correlación

Implementar filtro HTTP para:
- Generar correlationId automáticamente para cada solicitud
- Agregar correlationId al MDC
- Incluir correlationId en headers de respuesta
- Propagar correlationId a servicios externos

### Tarea 27.3: Configurar Niveles de Log por Paquete

Ajustar niveles de log según necesidades:
- Reducir logs de frameworks en producción
- Aumentar logs de aplicación en desarrollo
- Configurar logs específicos para debugging

## Conclusión

La tarea 27.1 se ha completado exitosamente. La configuración de Logback con codificador JSON está lista para:

1. ✅ Generar logs estructurados en formato JSON
2. ✅ Configurar niveles de log por paquete
3. ✅ Soportar diferentes perfiles (dev, test, prod)
4. ✅ Rotar y comprimir archivos de log
5. ✅ Integrarse con sistemas de agregación de logs (ELK, Splunk)
6. ✅ Incluir contexto adicional (correlationId, userId) vía MDC

**Estado**: ✅ COMPLETADO

**Requisito**: TR-18 (Structured Logging)

**Fecha**: 2026-04-23
