# Configuración de Producción - MSBioSuper

## Descripción General

Este documento describe la configuración específica de producción para MSBioSuper, implementada en el archivo `application-prod.properties`. Esta configuración está optimizada para seguridad, rendimiento y observabilidad en entornos productivos.

## Archivo de Configuración

### Ubicación
```
src/main/resources/application-prod.properties
```

### Activación
Para activar el perfil de producción, usar una de las siguientes opciones:

```bash
# Variable de entorno (recomendado)
export SPRING_PROFILES_ACTIVE=prod

# Argumento de línea de comandos
java -jar msbiosuper.jar --spring.profiles.active=prod

# Variable de sistema
-Dspring.profiles.active=prod
```

## Variables de Entorno Requeridas

### Variables Obligatorias

Estas variables **DEBEN** estar configuradas para que la aplicación inicie en producción:

```bash
# Base de datos PostgreSQL
export JDBC_SERVER=prod-db.acosux.com
export JDBC_PORT=5432
export JDBC_DATABASE=msbiosuper_prod
export JDBC_USER=msbiosuper_prod_user
export JDBC_PASSWORD=<contraseña_segura>

# Seguridad JWT
export JWT_SECRET=<clave_base64_512_bits>
```

### Variables Opcionales

```bash
# Configuración del servidor
export SERVER_PORT=8080

# Configuración JWT
export JWT_EXPIRATION=3600000  # 1 hora en ms

# CORS (dominios autorizados)
export CORS_ALLOWED_ORIGINS=https://app.acosux.com,https://admin.acosux.com

# Swagger (deshabilitado por defecto en producción)
export SWAGGER_ENABLED=false

# Información de la aplicación
export APP_VERSION=1.0.0
export API_PROD_URL=https://api.acosux.com
```

## Características de Seguridad

### 1. Externalización de Secretos
- ✅ **Todas las credenciales** se obtienen de variables de entorno
- ✅ **No hay valores por defecto** para credenciales sensibles
- ✅ **JWT secret** debe ser proporcionado externamente
- ✅ **Contraseñas de base de datos** externalizadas

### 2. Logging Restrictivo
```properties
# Solo errores y warnings por defecto
logging.level.root=WARN

# Información importante de la aplicación
logging.level.com.acosux.MSBiosuper=INFO

# SQL oculto en producción (seguridad)
spring.jpa.show-sql=false
```

### 3. Endpoints Actuator Limitados
```properties
# Solo endpoints esenciales expuestos
management.endpoints.web.exposure.include=health,info,metrics,prometheus

# Detalles de health solo con autorización
management.endpoint.health.show-details=when-authorized
```

### 4. CORS Restrictivo
```properties
# Solo dominios autorizados
cors.allowed-origins=${CORS_ALLOWED_ORIGINS}

# Headers específicos permitidos
cors.allowed-headers=Content-Type,Authorization,X-Requested-With,Accept,Origin
```

### 5. Configuración de Sesiones Seguras
```properties
# Sesiones más cortas en producción
server.servlet.session.timeout=15m

# Cookies seguras
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.same-site=strict
```

## Optimizaciones de Rendimiento

### 1. Pool de Conexiones HikariCP
```properties
# Pool optimizado para alta carga
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# Timeouts optimizados
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1800000
```

### 2. Compresión HTTP
```properties
# Compresión habilitada para reducir ancho de banda
server.compression.enabled=true
server.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json,application/xml
```

### 3. HTTP/2
```properties
# HTTP/2 habilitado para mejor rendimiento
server.http2.enabled=true
```

### 4. Configuración de Tomcat
```properties
# Optimizado para alta concurrencia
server.tomcat.max-threads=200
server.tomcat.min-spare-threads=10
server.tomcat.max-connections=8192
server.tomcat.accept-count=100
```

### 5. Caché Habilitado
```properties
# Caché Caffeine para mejor rendimiento
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=1000,expireAfterWrite=10m
```

### 6. Optimizaciones de Hibernate
```properties
# Batch processing optimizado
spring.jpa.properties.hibernate.jdbc.batch_size=25
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Caché de segundo nivel habilitado
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.use_query_cache=true
```

## Configuración de Observabilidad

### 1. Métricas Prometheus
```properties
# Métricas habilitadas para monitoreo
management.metrics.export.prometheus.enabled=true
management.metrics.export.prometheus.step=30s

# Métricas JVM, sistema y HTTP habilitadas
management.metrics.enable.jvm=true
management.metrics.enable.system=true
management.metrics.enable.http=true
```

### 2. Health Checks
```properties
# Health checks para Kubernetes
management.endpoint.health.probes.enabled=true
management.health.livenessState.enabled=true
management.health.readinessState.enabled=true

# Grupos de health checks
management.endpoint.health.group.liveness.include=livenessState
management.endpoint.health.group.readiness.include=readinessState,db,diskSpace
```

### 3. Logging Estructurado
```properties
# Archivos de log con rotación
logging.file.name=logs/msbiosuper.log
logging.file.max-size=100MB
logging.file.max-history=30
logging.file.total-size-cap=1GB

# Patrón con correlation ID
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{correlationId:-}] %logger{36} - %msg%n
```

### 4. Tags de Métricas
```properties
# Tags para identificación en monitoreo
management.metrics.tags.application=${spring.application.name}
management.metrics.tags.environment=prod
management.metrics.tags.version=${app.version}
```

## Configuración de Despliegue

### 1. Docker
```dockerfile
# Dockerfile
FROM eclipse-temurin:21-jre-alpine

# Variables de entorno
ENV SPRING_PROFILES_ACTIVE=prod
ENV JDBC_SERVER=prod-db.acosux.com
ENV JDBC_PORT=5432
ENV JDBC_DATABASE=msbiosuper_prod

# Secrets como variables de entorno
ENV JDBC_USER=${JDBC_USER}
ENV JDBC_PASSWORD=${JDBC_PASSWORD}
ENV JWT_SECRET=${JWT_SECRET}

COPY target/msbiosuper.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 2. Docker Compose
```yaml
# docker-compose.prod.yml
version: '3.8'
services:
  msbiosuper:
    image: msbiosuper:latest
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JDBC_SERVER=postgres
      - JDBC_PORT=5432
      - JDBC_DATABASE=msbiosuper_prod
      - JDBC_USER=${JDBC_USER}
      - JDBC_PASSWORD=${JDBC_PASSWORD}
      - JWT_SECRET=${JWT_SECRET}
      - CORS_ALLOWED_ORIGINS=https://app.acosux.com
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health/liveness"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s

  postgres:
    image: postgres:15-alpine
    environment:
      - POSTGRES_DB=msbiosuper_prod
      - POSTGRES_USER=${JDBC_USER}
      - POSTGRES_PASSWORD=${JDBC_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

volumes:
  postgres_data:
```

### 3. Kubernetes
```yaml
# deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: msbiosuper
  labels:
    app: msbiosuper
spec:
  replicas: 3
  selector:
    matchLabels:
      app: msbiosuper
  template:
    metadata:
      labels:
        app: msbiosuper
    spec:
      containers:
      - name: msbiosuper
        image: msbiosuper:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: JDBC_SERVER
          value: "postgres-service"
        - name: JDBC_PORT
          value: "5432"
        - name: JDBC_DATABASE
          value: "msbiosuper_prod"
        - name: JDBC_USER
          valueFrom:
            secretKeyRef:
              name: msbiosuper-secrets
              key: jdbc-user
        - name: JDBC_PASSWORD
          valueFrom:
            secretKeyRef:
              name: msbiosuper-secrets
              key: jdbc-password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: msbiosuper-secrets
              key: jwt-secret
        - name: CORS_ALLOWED_ORIGINS
          value: "https://app.acosux.com,https://admin.acosux.com"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
```

## Generación de Secretos

### JWT Secret
```bash
# Generar clave JWT segura (512 bits)
openssl rand -base64 64

# O usar el script proporcionado
./scripts/generate-jwt-secret.sh
```

### Contraseña de Base de Datos
```bash
# Generar contraseña segura
openssl rand -base64 32 | tr -d "=+/" | cut -c1-25
```

## Validación de Configuración

### Script de Validación
```bash
# Ejecutar script de validación
./scripts/test-prod-config.sh
```

### Verificación Manual
```bash
# Verificar variables de entorno
env | grep -E 'JDBC_|JWT_|CORS_'

# Probar conexión a base de datos
psql -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE

# Verificar que la aplicación inicia
java -jar target/msbiosuper.jar --spring.profiles.active=prod
```

## Monitoreo en Producción

### Endpoints de Monitoreo
```bash
# Health check
curl http://localhost:8080/actuator/health

# Métricas Prometheus
curl http://localhost:8080/actuator/prometheus

# Información de la aplicación
curl http://localhost:8080/actuator/info
```

### Métricas Clave
- **JVM Memory**: `jvm_memory_used_bytes`
- **HTTP Requests**: `http_server_requests_seconds`
- **Database Connections**: `hikaricp_connections_active`
- **GC Performance**: `jvm_gc_pause_seconds`

### Alertas Recomendadas
```yaml
# Prometheus alerts
groups:
- name: msbiosuper
  rules:
  - alert: MSBioSuperDown
    expr: up{job="msbiosuper"} == 0
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "MSBioSuper is down"

  - alert: HighMemoryUsage
    expr: jvm_memory_used_bytes{job="msbiosuper"} / jvm_memory_max_bytes{job="msbiosuper"} > 0.8
    for: 5m
    labels:
      severity: warning
    annotations:
      summary: "High memory usage in MSBioSuper"

  - alert: DatabaseConnectionsHigh
    expr: hikaricp_connections_active{job="msbiosuper"} > 15
    for: 2m
    labels:
      severity: warning
    annotations:
      summary: "High database connections in MSBioSuper"
```

## Troubleshooting

### Problemas Comunes

#### 1. Error: "Failed to configure a DataSource"
**Causa**: Variables de entorno de base de datos no configuradas
**Solución**:
```bash
export JDBC_DATABASE=msbiosuper_prod
export JDBC_USER=msbiosuper_user
export JDBC_PASSWORD=secure_password
```

#### 2. Error: "JWT secret key is not configured"
**Causa**: Variable JWT_SECRET no configurada
**Solución**:
```bash
export JWT_SECRET=$(openssl rand -base64 64)
```

#### 3. Error: "Connection refused"
**Causa**: No se puede conectar a PostgreSQL
**Solución**:
```bash
# Verificar conectividad
telnet $JDBC_SERVER $JDBC_PORT

# Verificar que PostgreSQL está corriendo
pg_isready -h $JDBC_SERVER -p $JDBC_PORT
```

#### 4. Error: "Access denied"
**Causa**: Credenciales incorrectas o permisos insuficientes
**Solución**:
```bash
# Probar credenciales manualmente
psql -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE

# Verificar permisos del usuario en PostgreSQL
```

### Logs de Diagnóstico
```bash
# Ver logs de la aplicación
tail -f logs/msbiosuper.log

# Filtrar errores
grep ERROR logs/msbiosuper.log

# Ver logs de HikariCP
grep -i hikari logs/msbiosuper.log
```

## Mejores Prácticas

### 1. Gestión de Secretos
- ✅ Usar gestores de secretos (Vault, AWS Secrets Manager)
- ✅ Rotar credenciales cada 90 días
- ✅ Usar diferentes credenciales por entorno
- ❌ Nunca commitear secretos en Git

### 2. Monitoreo
- ✅ Configurar alertas para métricas críticas
- ✅ Monitorear logs de errores
- ✅ Configurar dashboards de Grafana
- ✅ Implementar health checks

### 3. Seguridad
- ✅ Mantener CORS restrictivo
- ✅ Usar HTTPS en producción
- ✅ Configurar firewall apropiadamente
- ✅ Auditar acceso a logs y métricas

### 4. Rendimiento
- ✅ Monitorear pool de conexiones
- ✅ Configurar caché apropiadamente
- ✅ Optimizar consultas SQL
- ✅ Usar compresión HTTP

## Referencias

- [Spring Boot Production Features](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby)
- [Prometheus Metrics](https://micrometer.io/docs/registry/prometheus)
- [JWT Best Practices](https://tools.ietf.org/html/rfc8725)

---

**Última actualización**: 2026-04-24  
**Versión**: 1.0  
**Mantenedor**: Equipo de Desarrollo MSBioSuper