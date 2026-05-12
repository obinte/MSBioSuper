# Guía de Configuración - MSBioSuper

## Descripción General

Este documento describe la configuración de la aplicación MSBioSuper para Spring Boot 3 y Java 21.

## Archivos de Configuración

### application.properties (Base)
Configuración base compartida por todos los entornos. Incluye:
- Configuración de base de datos PostgreSQL
- Pool de conexiones HikariCP
- Propiedades JPA/Hibernate 6
- Configuración de logging
- Configuración de seguridad JWT
- Configuración de CORS
- Configuración de OpenAPI/Swagger

### application-dev.properties (Desarrollo)
Configuración optimizada para desarrollo local:
- SQL visible en consola
- Logging detallado (DEBUG)
- Estadísticas de Hibernate habilitadas
- Pool de conexiones reducido
- Todos los endpoints de Actuator expuestos
- CORS permisivo

### application-test.properties (Pruebas)
Configuración para ejecución de pruebas:
- Base de datos de prueba (Testcontainers)
- Pool de conexiones mínimo
- Logging reducido
- Sin estadísticas de Hibernate

### application-prod.properties (Producción)
Configuración optimizada para producción:
- Sin SQL en logs
- Logging mínimo (WARN/INFO)
- Pool de conexiones optimizado
- Endpoints de Actuator restringidos
- CORS restrictivo
- Compresión HTTP habilitada
- HTTP/2 habilitado
- Información de error oculta

**📖 Para documentación completa sobre configuración de producción, consultar: [PRODUCTION-CONFIGURATION.md](./PRODUCTION-CONFIGURATION.md)**

## Variables de Entorno

**📖 Para documentación completa y detallada sobre variables de entorno, consultar: [ENVIRONMENT-VARIABLES.md](./ENVIRONMENT-VARIABLES.md)**

### Variables Requeridas (Obligatorias)

⚠️ **IMPORTANTE**: Las siguientes variables son **OBLIGATORIAS** y la aplicación no iniciará sin ellas.

#### Base de Datos
```bash
JDBC_DATABASE=msbiosuper_db    # Nombre de la base de datos (OBLIGATORIO)
JDBC_USER=msbiosuper_user      # Usuario de base de datos (OBLIGATORIO)
JDBC_PASSWORD=secure_password  # Contraseña de base de datos (OBLIGATORIO)
```

#### Seguridad JWT
```bash
JWT_SECRET=<generar_con_openssl>  # Clave secreta para firmar tokens JWT (OBLIGATORIO)
                                   # Mínimo 256 bits, generar con: openssl rand -base64 64
```

### Variables Opcionales (Con Valores por Defecto)

```bash
JDBC_SERVER=localhost          # Servidor PostgreSQL (por defecto: localhost)
JDBC_PORT=5432                 # Puerto PostgreSQL (por defecto: 5432)
JWT_EXPIRATION=86400000        # Tiempo de expiración en ms (por defecto: 24 horas)
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200  # Orígenes permitidos
SERVER_PORT=8080               # Puerto del servidor (por defecto: 8080)
SPRING_PROFILES_ACTIVE=dev     # Perfil activo (dev, test, prod)
```

### Cambios de Seguridad (Java 21 Migration)

🔒 **Mejoras de Seguridad Implementadas**:
- ✅ Credenciales de base de datos **ya no tienen valores por defecto**
- ✅ JWT secret **ya no tiene valor por defecto**
- ✅ Todas las credenciales sensibles deben ser proporcionadas explícitamente
- ✅ Documentación completa en [ENVIRONMENT-VARIABLES.md](./ENVIRONMENT-VARIABLES.md)

## Perfiles de Spring

### Activar un Perfil

#### Mediante Variable de Entorno
```bash
export SPRING_PROFILES_ACTIVE=dev
java -jar msbiosuper.jar
```

#### Mediante Argumento de Línea de Comandos
```bash
java -jar msbiosuper.jar --spring.profiles.active=dev
```

#### Mediante Maven
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Configuración de HikariCP

### Parámetros del Pool de Conexiones

| Parámetro | Desarrollo | Producción | Descripción |
|-----------|------------|------------|-------------|
| `maximum-pool-size` | 10 | 20 | Número máximo de conexiones |
| `minimum-idle` | 2 | 5 | Conexiones mínimas en idle |
| `connection-timeout` | 30000 ms | 20000 ms | Timeout para obtener conexión |
| `idle-timeout` | 600000 ms | 300000 ms | Timeout de inactividad |
| `max-lifetime` | 1800000 ms | 1800000 ms | Vida máxima de una conexión |
| `leak-detection-threshold` | 2000 ms | 2000 ms | Detección de fugas |

### Recomendaciones

- **Desarrollo**: Pool pequeño (10 conexiones) para reducir carga en base de datos local
- **Producción**: Pool optimizado (20 conexiones) basado en carga esperada
- **Fórmula**: `connections = ((core_count * 2) + effective_spindle_count)`

## Configuración de JPA/Hibernate

### Propiedades Importantes

```properties
# Dialecto de PostgreSQL
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# No modificar esquema automáticamente
spring.jpa.hibernate.ddl-auto=none

# Optimizaciones de batch
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

### Estrategias de DDL

| Valor | Descripción | Uso Recomendado |
|-------|-------------|-----------------|
| `none` | No hace cambios | **Producción** |
| `validate` | Valida esquema | Pruebas |
| `update` | Actualiza esquema | Desarrollo (con cuidado) |
| `create` | Crea esquema | Nunca en producción |
| `create-drop` | Crea y elimina | Solo pruebas |

## Configuración de Logging

### Niveles de Logging

| Nivel | Descripción | Uso |
|-------|-------------|-----|
| `TRACE` | Muy detallado | Debugging profundo |
| `DEBUG` | Información de debug | Desarrollo |
| `INFO` | Información general | Producción |
| `WARN` | Advertencias | Producción |
| `ERROR` | Errores | Siempre |

### Configuración por Paquete

```properties
# Aplicación
logging.level.com.acosux.MSBiosuper=INFO

# HikariCP
logging.level.com.zaxxer.hikari=INFO

# Hibernate
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Spring
logging.level.org.springframework.web=INFO
logging.level.org.springframework.security=INFO
```

## Configuración de Actuator

### Endpoints Disponibles

| Endpoint | Descripción | Producción |
|----------|-------------|------------|
| `/actuator/health` | Estado de salud | ✅ Sí |
| `/actuator/info` | Información de la app | ✅ Sí |
| `/actuator/metrics` | Métricas de la app | ✅ Sí |
| `/actuator/prometheus` | Métricas Prometheus | ✅ Sí |
| `/actuator/env` | Variables de entorno | ❌ No |
| `/actuator/beans` | Beans de Spring | ❌ No |
| `/actuator/mappings` | Mappings de endpoints | ❌ No |

### Health Checks

```bash
# Health check básico
curl http://localhost:8080/actuator/health

# Health check detallado (requiere autorización)
curl -H "Authorization: Bearer <token>" http://localhost:8080/actuator/health
```

## Configuración de Seguridad

### Generación de Clave Secreta JWT

La clave secreta JWT debe ser una cadena aleatoria de al menos 256 bits (32 caracteres).

**📖 Para información detallada sobre configuración JWT, consultar: [JWT-CONFIGURATION.md](./JWT-CONFIGURATION.md)**

#### Generar con OpenSSL
```bash
openssl rand -base64 32
```

#### Generar con Python
```python
import secrets
print(secrets.token_urlsafe(32))
```

### Configuración de CORS

```properties
# Orígenes permitidos (separados por coma)
cors.allowed-origins=http://localhost:3000,http://localhost:4200

# Métodos permitidos
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS

# Headers permitidos
cors.allowed-headers=*

# Permitir credenciales
cors.allow-credentials=true
```

## Configuración de OpenAPI/Swagger

### Acceso a la Documentación

- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

### Deshabilitar en Producción (Opcional)

```properties
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
```

## Ejemplo de Configuración Completa

### Desarrollo Local

```bash
# .env file
JDBC_SERVER=localhost
JDBC_PORT=5433
JDBC_DATABASE=cipollini
JDBC_USER=administradores
JDBC_PASSWORD=mypassword
JWT_SECRET=mySecretKeyForDevelopment123456789
JWT_EXPIRATION=86400000
CORS_ALLOWED_ORIGINS=http://localhost:3000
SPRING_PROFILES_ACTIVE=dev
```

### Producción

```bash
# Variables de entorno en servidor
export JDBC_SERVER=prod-db.acosux.com
export JDBC_PORT=5432
export JDBC_DATABASE=cipollini_prod
export JDBC_USER=app_user
export JDBC_PASSWORD=<secure-password>
export JWT_SECRET=<generated-secure-key>
export JWT_EXPIRATION=3600000
export CORS_ALLOWED_ORIGINS=https://app.acosux.com
export SPRING_PROFILES_ACTIVE=prod
```

## Troubleshooting

### Problema: Conexión a Base de Datos Falla

**Solución**:
1. Verificar que PostgreSQL está corriendo
2. Verificar credenciales en variables de entorno
3. Verificar conectividad de red
4. Revisar logs de HikariCP

```bash
# Ver logs de HikariCP
logging.level.com.zaxxer.hikari=DEBUG
```

### Problema: Pool de Conexiones Agotado

**Solución**:
1. Aumentar `maximum-pool-size`
2. Reducir `connection-timeout`
3. Verificar fugas de conexión con `leak-detection-threshold`
4. Revisar transacciones no cerradas

### Problema: JWT Token Inválido

**Solución**:
1. Verificar que `JWT_SECRET` es el mismo en todos los nodos
2. Verificar que el token no ha expirado
3. Verificar formato del token (Bearer <token>)

### Problema: CORS Bloqueado

**Solución**:
1. Verificar `cors.allowed-origins` incluye el origen del cliente
2. Verificar que el servidor responde a OPTIONS (preflight)
3. Revisar logs de Spring Security

## Referencias

- [Spring Boot Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby)
- [Hibernate Properties](https://docs.jboss.org/hibernate/orm/6.5/userguide/html_single/Hibernate_User_Guide.html#configurations)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

---

**Última actualización**: 2026-04-22  
**Versión**: 1.0
