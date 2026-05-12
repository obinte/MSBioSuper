# ID de Correlación para Rastreo de Solicitudes

## Descripción General

El sistema MSBioSuper implementa un mecanismo de **ID de correlación** para rastrear solicitudes HTTP a través de toda la aplicación. Esto facilita la depuración, el monitoreo y el análisis de logs al permitir seguir todas las operaciones relacionadas con una solicitud específica.

## ¿Qué es un ID de Correlación?

Un **ID de correlación** es un identificador único (UUID) que se asigna a cada solicitud HTTP que ingresa al sistema. Este ID se propaga a través de todos los componentes de la aplicación y se incluye en todos los logs generados durante el procesamiento de esa solicitud.

### Beneficios

1. **Rastreo de Solicitudes**: Permite seguir el flujo completo de una solicitud a través de múltiples capas de la aplicación
2. **Depuración Simplificada**: Facilita la identificación de errores al poder filtrar logs por ID de correlación
3. **Análisis de Rendimiento**: Permite medir el tiempo total de procesamiento de una solicitud específica
4. **Integración con Sistemas Externos**: Los clientes pueden proporcionar su propio ID para rastreo end-to-end
5. **Auditoría**: Facilita la auditoría de operaciones al poder rastrear todas las acciones de una solicitud

## Implementación Técnica

### Componentes

#### 1. CorrelationIdFilter

Filtro de Spring que se ejecuta con la prioridad más alta (`Ordered.HIGHEST_PRECEDENCE`) para asegurar que el ID de correlación esté disponible para todos los demás filtros.

**Ubicación**: `com.acosux.MSBiosuper.infrastructure.security.CorrelationIdFilter`

**Funcionalidad**:
- Genera un UUID único si no existe un ID en el header de la solicitud
- Extrae el ID del header `X-Correlation-ID` si está presente
- Agrega el ID al MDC (Mapped Diagnostic Context) de Logback
- Incluye el ID en la respuesta HTTP
- Limpia el MDC después de procesar la solicitud

#### 2. Configuración de Logback

El archivo `logback-spring.xml` está configurado para incluir automáticamente el ID de correlación en todos los logs.

**Formato JSON**:
```json
{
  "timestamp": "2026-04-23T10:30:45.123-05:00",
  "level": "INFO",
  "logger": "com.acosux.MSBiosuper.application.usecase.GetResumenPescaUseCase",
  "message": "Obteniendo resumen de pesca",
  "mdc": {
    "correlationId": "550e8400-e29b-41d4-a716-446655440000"
  },
  "application": "msbiosuper",
  "environment": "prod"
}
```

**Formato Legible (desarrollo)**:
```
2026-04-23 10:30:45.123 [http-nio-8080-exec-1] [550e8400-e29b-41d4-a716-446655440000] INFO  c.a.M.a.u.GetResumenPescaUseCase - Obteniendo resumen de pesca
```

## Uso

### Para Clientes de la API

#### Proporcionar un ID de Correlación

Los clientes pueden proporcionar su propio ID de correlación en el header de la solicitud:

```bash
curl -X POST https://api.msbiosuper.com/api/v1/resumen-pesca \
  -H "Content-Type: application/json" \
  -H "X-Correlation-ID: mi-id-personalizado-12345" \
  -H "Authorization: Bearer <token>" \
  -d '{"empresa": "001", "fechaInicio": "2026-01-01", "fechaFin": "2026-01-31"}'
```

#### Recibir el ID de Correlación

El servidor siempre incluye el ID de correlación en la respuesta:

```http
HTTP/1.1 200 OK
Content-Type: application/json
X-Correlation-ID: 550e8400-e29b-41d4-a716-446655440000

{
  "data": [...],
  "status": "success"
}
```

### Para Desarrolladores

#### Obtener el ID de Correlación en el Código

```java
import com.acosux.MSBiosuper.infrastructure.security.CorrelationIdFilter;

public class MiServicio {
    
    public void miMetodo() {
        // Obtener el ID de correlación actual
        String correlationId = CorrelationIdFilter.getCurrentCorrelationId();
        
        // Usar el ID para logging o procesamiento
        logger.info("Procesando solicitud con ID: {}", correlationId);
    }
}
```

#### Agregar el ID a Logs Personalizados

El ID de correlación se incluye automáticamente en todos los logs gracias al MDC de Logback. No es necesario agregarlo manualmente:

```java
// ✅ CORRECTO - El ID se incluye automáticamente
logger.info("Procesando resumen de pesca");

// ❌ INCORRECTO - No es necesario agregar el ID manualmente
logger.info("Procesando resumen de pesca con ID: {}", CorrelationIdFilter.getCurrentCorrelationId());
```

#### Propagar el ID a Servicios Externos

Si necesitas llamar a servicios externos, puedes propagar el ID de correlación:

```java
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;

public class ServicioExterno {
    
    private final RestTemplate restTemplate;
    
    public void llamarServicioExterno() {
        // Obtener el ID de correlación actual
        String correlationId = CorrelationIdFilter.getCurrentCorrelationId();
        
        // Crear headers con el ID de correlación
        HttpHeaders headers = new HttpHeaders();
        headers.set(CorrelationIdFilter.CORRELATION_ID_HEADER, correlationId);
        
        // Realizar la llamada
        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange("https://api.externa.com/endpoint", 
                            HttpMethod.GET, entity, String.class);
    }
}
```

## Búsqueda y Filtrado de Logs

### Buscar por ID de Correlación en Logs JSON

Si estás usando un sistema de agregación de logs como ELK Stack, Splunk, o CloudWatch:

```json
// Elasticsearch Query
{
  "query": {
    "match": {
      "mdc.correlationId": "550e8400-e29b-41d4-a716-446655440000"
    }
  }
}
```

### Buscar en Archivos de Log Locales

```bash
# Buscar en logs JSON
grep "550e8400-e29b-41d4-a716-446655440000" logs/msbiosuper.log

# Buscar en logs legibles (desarrollo)
grep "\[550e8400-e29b-41d4-a716-446655440000\]" logs/msbiosuper.log
```

## Ejemplos de Uso

### Ejemplo 1: Rastrear una Solicitud Completa

1. Cliente envía solicitud con ID personalizado:
```bash
curl -X POST https://api.msbiosuper.com/api/v1/resumen-pesca \
  -H "X-Correlation-ID: solicitud-cliente-001" \
  ...
```

2. Todos los logs de esa solicitud incluyen el ID:
```
2026-04-23 10:30:45.123 [http-nio-8080-exec-1] [solicitud-cliente-001] INFO  MSBioSuperController - Recibida solicitud de resumen de pesca
2026-04-23 10:30:45.234 [http-nio-8080-exec-1] [solicitud-cliente-001] INFO  GetResumenPescaUseCase - Ejecutando caso de uso
2026-04-23 10:30:45.456 [http-nio-8080-exec-1] [solicitud-cliente-001] DEBUG ResumenPescaJpaRepository - Consultando base de datos
2026-04-23 10:30:45.789 [http-nio-8080-exec-1] [solicitud-cliente-001] INFO  MSBioSuperController - Respuesta enviada exitosamente
```

3. Cliente recibe el mismo ID en la respuesta:
```http
HTTP/1.1 200 OK
X-Correlation-ID: solicitud-cliente-001
```

### Ejemplo 2: Depurar un Error

1. Cliente reporta un error y proporciona el ID de correlación de la respuesta
2. Desarrollador busca en los logs por ese ID:
```bash
grep "550e8400-e29b-41d4-a716-446655440000" logs/msbiosuper.log
```

3. Encuentra todos los logs relacionados con esa solicitud, incluyendo el stack trace del error

### Ejemplo 3: Análisis de Rendimiento

1. Buscar todos los logs de una solicitud específica
2. Calcular el tiempo entre el primer y último log
3. Identificar cuellos de botella en el procesamiento

## Configuración

### Variables de Entorno

No se requieren variables de entorno adicionales. El filtro está configurado automáticamente.

### Personalización

Si necesitas personalizar el nombre del header o la clave del MDC, puedes modificar las constantes en `CorrelationIdFilter`:

```java
public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
public static final String CORRELATION_ID_MDC_KEY = "correlationId";
```

## Consideraciones de Rendimiento

- **Overhead Mínimo**: La generación de UUID y el manejo del MDC tienen un overhead insignificante (< 1ms)
- **Thread-Safe**: El MDC es thread-local, por lo que no hay problemas de concurrencia
- **Limpieza Automática**: El filtro limpia el MDC automáticamente después de cada solicitud

## Mejores Prácticas

1. **Siempre Proporcionar ID en Solicitudes de Clientes**: Facilita el rastreo end-to-end
2. **Incluir ID en Respuestas de Error**: Ayuda a los clientes a reportar problemas
3. **Propagar ID a Servicios Externos**: Mantiene la trazabilidad en arquitecturas distribuidas
4. **No Modificar el ID Durante el Procesamiento**: El ID debe ser inmutable durante toda la solicitud
5. **Usar el ID para Auditoría**: Registra el ID en tablas de auditoría para rastreo histórico

## Troubleshooting

### El ID no aparece en los logs

**Problema**: Los logs no incluyen el ID de correlación.

**Solución**:
1. Verificar que `includeMdc` esté configurado en `true` en `logback-spring.xml`
2. Verificar que el filtro `CorrelationIdFilter` esté registrado como `@Component`
3. Verificar que el perfil de Spring Boot esté configurado correctamente

### El ID se pierde entre hilos

**Problema**: El ID de correlación no está disponible en hilos secundarios.

**Solución**: Si usas procesamiento asíncrono, necesitas propagar el MDC manualmente:

```java
import org.slf4j.MDC;
import java.util.Map;
import java.util.concurrent.Callable;

public class MDCCallable<T> implements Callable<T> {
    private final Callable<T> callable;
    private final Map<String, String> contextMap;

    public MDCCallable(Callable<T> callable) {
        this.callable = callable;
        this.contextMap = MDC.getCopyOfContextMap();
    }

    @Override
    public T call() throws Exception {
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
        try {
            return callable.call();
        } finally {
            MDC.clear();
        }
    }
}
```

## Referencias

- [SLF4J MDC Documentation](http://www.slf4j.org/manual.html#mdc)
- [Logback MDC](https://logback.qos.ch/manual/mdc.html)
- [Spring Boot Logging](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging)
- [Distributed Tracing Best Practices](https://opentelemetry.io/docs/concepts/observability-primer/)

---

**Versión**: 1.0  
**Fecha**: 2026-04-23  
**Autor**: Josias Rubén Aldaz Arcalles
