# Configuración de HikariCP - Pool de Conexiones

## Resumen

Se ha configurado HikariCP como pool de conexiones para la aplicación MSBioSuper, cumpliendo con los requisitos TR-3 y TR-5 de la migración a Java 21.

## Configuración Implementada

### 1. Propiedades en `application.properties`

```properties
# Configuración de HikariCP - Pool de conexiones
# Tamaño máximo del pool (20 conexiones)
spring.datasource.hikari.maximum-pool-size=20

# Tamaño mínimo del pool en estado idle
spring.datasource.hikari.minimum-idle=5

# Timeout de conexión (30 segundos = 30000 ms)
spring.datasource.hikari.connection-timeout=30000

# Timeout de inactividad (10 minutos = 600000 ms)
spring.datasource.hikari.idle-timeout=600000

# Tiempo máximo de vida de una conexión (30 minutos = 1800000 ms)
spring.datasource.hikari.max-lifetime=1800000

# Habilitar detección de fugas de conexión (2 segundos = 2000 ms)
spring.datasource.hikari.leak-detection-threshold=2000

# Nombre del pool para identificación en logs
spring.datasource.hikari.pool-name=MSBioSuperHikariPool
```

### 2. Parámetros Configurados

| Parámetro | Valor | Descripción |
|-----------|-------|-------------|
| `maximum-pool-size` | 20 | Número máximo de conexiones en el pool |
| `minimum-idle` | 5 | Número mínimo de conexiones inactivas mantenidas |
| `connection-timeout` | 30000 ms (30 seg) | Tiempo máximo de espera para obtener una conexión |
| `idle-timeout` | 600000 ms (10 min) | Tiempo máximo que una conexión puede estar inactiva |
| `max-lifetime` | 1800000 ms (30 min) | Tiempo máximo de vida de una conexión |
| `leak-detection-threshold` | 2000 ms (2 seg) | Umbral para detectar fugas de conexión |
| `pool-name` | MSBioSuperHikariPool | Nombre del pool para logs |

### 3. Detección de Fugas de Conexión

La detección de fugas está habilitada con un umbral de 2 segundos. Esto significa que:

- Si una conexión no se devuelve al pool después de 2 segundos, HikariCP registrará un warning en los logs
- Esto ayuda a identificar código que no cierra correctamente las conexiones
- Los logs incluirán el stack trace de dónde se obtuvo la conexión

**Ejemplo de log de fuga:**

```
WARN com.zaxxer.hikari.pool.ProxyLeakTask - Connection leak detection triggered for connection, stack trace follows
```

### 4. Logging de HikariCP

Se ha configurado el nivel de logging para monitorear el pool:

```properties
logging.level.com.zaxxer.hikari=DEBUG
```

Esto permite ver:
- Inicialización del pool
- Obtención y liberación de conexiones
- Detección de fugas
- Estadísticas del pool

### 5. Integración con JpaConfig

La clase `JpaConfig` ha sido simplificada para usar la configuración automática de Spring Boot:

```java
@Bean
public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    // El DataSource se inyecta automáticamente con HikariCP configurado
    LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactory.setDataSource(dataSource);
    // ... resto de la configuración
}
```

## Ventajas de HikariCP

1. **Alto Rendimiento**: HikariCP es el pool de conexiones más rápido disponible
2. **Bajo Overhead**: Mínimo uso de memoria y CPU
3. **Detección de Fugas**: Identifica conexiones no cerradas
4. **Configuración Simple**: Integración nativa con Spring Boot 3
5. **Monitoreo**: Métricas expuestas para monitoreo

## Verificación

Para verificar que HikariCP está funcionando correctamente:

1. **Iniciar la aplicación** y revisar los logs:
   ```
   INFO com.zaxxer.hikari.HikariDataSource - MSBioSuperHikariPool - Starting...
   INFO com.zaxxer.hikari.HikariDataSource - MSBioSuperHikariPool - Start completed.
   ```

2. **Verificar el pool** en los logs de debug:
   ```
   DEBUG com.zaxxer.hikari.pool.HikariPool - MSBioSuperHikariPool - Pool stats (total=5, active=0, idle=5, waiting=0)
   ```

3. **Monitorear fugas** revisando los logs por warnings de leak detection

## Configuración por Entorno

Para diferentes entornos, se pueden crear archivos específicos:

- `application-dev.properties`: Configuración de desarrollo (pool pequeño)
- `application-test.properties`: Configuración de pruebas (pool mínimo)
- `application-prod.properties`: Configuración de producción (pool optimizado)

**Ejemplo para producción:**

```properties
spring.datasource.hikari.maximum-pool-size=50
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.leak-detection-threshold=60000
logging.level.com.zaxxer.hikari=INFO
```

## Troubleshooting

### Problema: "Connection is not available"

**Causa**: El pool está agotado (todas las conexiones en uso)

**Solución**:
1. Aumentar `maximum-pool-size`
2. Revisar código que no cierra conexiones
3. Verificar fugas con `leak-detection-threshold`

### Problema: "Connection has been closed"

**Causa**: Conexión cerrada por timeout o por la base de datos

**Solución**:
1. Ajustar `max-lifetime` (debe ser menor que el timeout de PostgreSQL)
2. Verificar configuración de red y firewall

### Problema: Muchas conexiones idle

**Causa**: `minimum-idle` muy alto para la carga actual

**Solución**:
1. Reducir `minimum-idle`
2. Ajustar `idle-timeout` para cerrar conexiones inactivas más rápido

## Referencias

- [HikariCP GitHub](https://github.com/brettwooldridge/HikariCP)
- [Spring Boot DataSource Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.data)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby)

---

**Fecha de Configuración**: 2026-04-22  
**Versión de HikariCP**: Incluida en Spring Boot 3.3.5  
**Estado**: ✅ Configurado y Documentado
