# Guía de Perfiles de Entorno - MSBioSuper

## Descripción General

MSBioSuper utiliza **Spring Profiles** para gestionar configuraciones específicas por entorno. Esto permite mantener una configuración base compartida y sobrescribir valores específicos según el entorno de ejecución.

## Perfiles Disponibles

### 1. Perfil Base (application.properties)

**Archivo**: `src/main/resources/application.properties`

**Propósito**: Configuración compartida por todos los entornos.

**Características**:
- Configuración de base de datos con variables de entorno
- Configuración de HikariCP
- Configuración de JPA/Hibernate
- Configuración de logging base
- Configuración de Actuator
- Configuración de JWT
- Configuración de CORS
- Configuración de OpenAPI/Swagger

**Uso**: Se carga automáticamente siempre.

---

### 2. Perfil de Desarrollo (application-dev.properties)

**Archivo**: `src/main/resources/application-dev.properties`

**Propósito**: Configuración optimizada para desarrollo local.

**Características**:
- Base de datos local (`localhost:5432/msbiosuper_dev`)
- Credenciales de desarrollo incluidas (para facilitar setup)
- Pool de conexiones reducido (10 conexiones)
- Logging en modo DEBUG
- SQL visible en consola
- Estadísticas de Hibernate habilitadas
- Hot reload habilitado
- Todos los endpoints de Actuator expuestos
- CORS abierto para desarrollo frontend
- Swagger UI habilitado
- Token JWT con expiración larga (7 días)

**Activación**:

```bash
# Opción 1: Línea de comandos
java -jar msbiosuper.jar --spring.profiles.active=dev

# Opción 2: Variable de entorno
export SPRING_PROFILES_ACTIVE=dev
java -jar msbiosuper.jar

# Opción 3: Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Opción 4: En IDE (IntelliJ IDEA)
Run > Edit Configurations > Active profiles: dev
```

**Variables de Entorno Opcionales**:
```bash
# Sobrescribir configuración de base de datos
export JDBC_SERVER=localhost
export JDBC_PORT=5432
export JDBC_DATABASE=msbiosuper_dev
export JDBC_USER=dev_user
export JDBC_PASSWORD=dev_password
```

---

### 3. Perfil de Pruebas (application-test.properties)

**Archivo**: `src/main/resources/application-test.properties`

**Propósito**: Configuración optimizada para ejecución de pruebas automatizadas.

**Características**:
- Base de datos H2 en memoria (por defecto)
- Compatible con Testcontainers para pruebas de integración
- Pool de conexiones mínimo (5 conexiones)
- Logging reducido (WARN/INFO)
- Puerto aleatorio para evitar conflictos
- Actuator deshabilitado (excepto health)
- Swagger deshabilitado
- Transacciones con rollback automático
- Token JWT con expiración corta (1 hora)
- Sin caché

**Activación**:

```bash
# Opción 1: Maven (automático en tests)
mvn test

# Opción 2: Gradle (automático en tests)
./gradlew test

# Opción 3: Manual
java -jar msbiosuper.jar --spring.profiles.active=test
```

**Nota**: Este perfil se activa automáticamente cuando se ejecutan pruebas con `@SpringBootTest`.

---

### 4. Perfil de Producción (application-prod.properties)

**Archivo**: `src/main/resources/application-prod.properties`

**Propósito**: Configuración optimizada y segura para producción.

**Características**:
- Todas las credenciales mediante variables de entorno (OBLIGATORIO)
- Pool de conexiones optimizado (20 conexiones)
- Logging mínimo (WARN/INFO)
- SQL NO visible (seguridad)
- Estadísticas deshabilitadas (rendimiento)
- Actuator con endpoints limitados
- Swagger UI deshabilitado (seguridad)
- CORS restrictivo
- Compresión HTTP habilitada
- Graceful shutdown habilitado
- Configuración de SSL preparada
- Token JWT con expiración estándar (24 horas)
- Logs en archivo con rotación

**Activación**:

```bash
# Opción 1: Línea de comandos
java -jar msbiosuper.jar --spring.profiles.active=prod

# Opción 2: Variable de entorno
export SPRING_PROFILES_ACTIVE=prod
java -jar msbiosuper.jar

# Opción 3: Docker
docker run -e SPRING_PROFILES_ACTIVE=prod msbiosuper:latest
```

**Variables de Entorno REQUERIDAS**:

```bash
# Base de datos
export JDBC_SERVER=db.production.com
export JDBC_PORT=5432
export JDBC_DATABASE=msbiosuper_prod
export JDBC_USER=prod_user
export JDBC_PASSWORD=<contraseña-segura>

# Seguridad JWT
export JWT_SECRET=<clave-base64-256bits>
export JWT_EXPIRATION=86400000  # 24 horas en ms

# CORS
export CORS_ALLOWED_ORIGINS=https://app.production.com,https://admin.production.com

# Servidor (opcional)
export SERVER_PORT=8080
```

**Generar JWT Secret**:

```bash
# Usar el script proporcionado
./scripts/generate-jwt-secret.sh

# O manualmente con OpenSSL
openssl rand -base64 32
```

---

## Uso de Múltiples Perfiles

Spring permite activar múltiples perfiles simultáneamente. Los valores se sobrescriben en orden de prioridad.

### Ejemplo: Desarrollo con configuración personalizada

```bash
# Activar dev + custom
export SPRING_PROFILES_ACTIVE=dev,custom
java -jar msbiosuper.jar
```

**Orden de carga**:
1. `application.properties` (base)
2. `application-dev.properties` (sobrescribe base)
3. `application-custom.properties` (sobrescribe dev)

---

## Verificación del Perfil Activo

### Opción 1: Logs de inicio

Al iniciar la aplicación, Spring Boot muestra el perfil activo:

```
The following 1 profile is active: "dev"
```

### Opción 2: Endpoint de Actuator

```bash
curl http://localhost:8080/actuator/info
```

### Opción 3: Endpoint de Environment

```bash
curl http://localhost:8080/actuator/env
```

---

## Mejores Prácticas

### 1. Desarrollo Local

✅ **Hacer**:
- Usar perfil `dev`
- Mantener credenciales de desarrollo en el archivo
- Habilitar logging detallado
- Usar base de datos local

❌ **No hacer**:
- Usar credenciales de producción
- Deshabilitar logging
- Conectar a base de datos de producción

### 2. Pruebas Automatizadas

✅ **Hacer**:
- Usar perfil `test`
- Usar H2 en memoria o Testcontainers
- Minimizar logging
- Usar transacciones con rollback

❌ **No hacer**:
- Conectar a base de datos compartida
- Usar datos de producción
- Deshabilitar rollback de transacciones

### 3. Producción

✅ **Hacer**:
- Usar perfil `prod`
- Todas las credenciales en variables de entorno
- Usar gestión de secretos (Vault, AWS Secrets Manager)
- Habilitar HTTPS
- Configurar monitoreo
- Implementar backups automáticos
- Configurar alertas

❌ **No hacer**:
- Incluir credenciales en archivos
- Exponer Swagger UI públicamente
- Usar logging DEBUG
- Permitir CORS abierto
- Deshabilitar health checks

---

## Configuración en Docker

### Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre-alpine

# Crear usuario no-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR
COPY target/msbiosuper-*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Punto de entrada
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Docker Compose - Desarrollo

```yaml
version: '3.8'

services:
  msbiosuper:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - JDBC_SERVER=postgres
      - JDBC_PORT=5432
      - JDBC_DATABASE=msbiosuper_dev
      - JDBC_USER=dev_user
      - JDBC_PASSWORD=dev_password
    depends_on:
      - postgres
  
  postgres:
    image: postgres:15-alpine
    environment:
      - POSTGRES_DB=msbiosuper_dev
      - POSTGRES_USER=dev_user
      - POSTGRES_PASSWORD=dev_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

### Docker Compose - Producción

```yaml
version: '3.8'

services:
  msbiosuper:
    image: msbiosuper:1.0.0
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JDBC_SERVER=${JDBC_SERVER}
      - JDBC_PORT=${JDBC_PORT}
      - JDBC_DATABASE=${JDBC_DATABASE}
      - JDBC_USER=${JDBC_USER}
      - JDBC_PASSWORD=${JDBC_PASSWORD}
      - JWT_SECRET=${JWT_SECRET}
      - CORS_ALLOWED_ORIGINS=${CORS_ALLOWED_ORIGINS}
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
```

---

## Configuración en Kubernetes

### ConfigMap

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: msbiosuper-config
data:
  SPRING_PROFILES_ACTIVE: "prod"
  JDBC_SERVER: "postgres-service"
  JDBC_PORT: "5432"
  JDBC_DATABASE: "msbiosuper_prod"
  CORS_ALLOWED_ORIGINS: "https://app.production.com"
```

### Secret

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: msbiosuper-secrets
type: Opaque
stringData:
  JDBC_USER: "prod_user"
  JDBC_PASSWORD: "<contraseña-segura>"
  JWT_SECRET: "<clave-base64-256bits>"
```

### Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: msbiosuper
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
        image: msbiosuper:1.0.0
        ports:
        - containerPort: 8080
        envFrom:
        - configMapRef:
            name: msbiosuper-config
        - secretRef:
            name: msbiosuper-secrets
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 20
          periodSeconds: 5
```

---

## Troubleshooting

### Problema: Perfil no se activa

**Síntoma**: La aplicación usa configuración incorrecta.

**Solución**:
```bash
# Verificar variable de entorno
echo $SPRING_PROFILES_ACTIVE

# Verificar en logs de inicio
# Buscar: "The following 1 profile is active"

# Forzar perfil en línea de comandos
java -jar msbiosuper.jar --spring.profiles.active=dev
```

### Problema: Variables de entorno no se cargan

**Síntoma**: Error de conexión a base de datos.

**Solución**:
```bash
# Verificar que las variables están definidas
env | grep JDBC

# Exportar variables antes de ejecutar
export JDBC_SERVER=localhost
export JDBC_DATABASE=msbiosuper_dev
# ... etc

# O usar archivo .env con Docker Compose
docker-compose --env-file .env up
```

### Problema: Conflicto entre perfiles

**Síntoma**: Configuración inesperada.

**Solución**:
```bash
# Activar solo un perfil
export SPRING_PROFILES_ACTIVE=dev

# O especificar orden de prioridad
export SPRING_PROFILES_ACTIVE=dev,local
# local sobrescribe dev
```

---

## Health Checks y Sondas de Kubernetes

### Endpoints de Health Disponibles

MSBioSuper expone los siguientes endpoints de health a través de Spring Boot Actuator:

| Endpoint | Propósito | Uso |
|----------|-----------|-----|
| `/actuator/health` | Health check general | Monitoreo general |
| `/actuator/health/liveness` | Sonda de liveness | Kubernetes liveness probe |
| `/actuator/health/readiness` | Sonda de readiness | Kubernetes readiness probe |

### Configuración de Sondas

Las sondas de liveness y readiness están configuradas en `application.properties`:

```properties
# Habilitar sondas de Kubernetes
management.endpoint.health.probes.enabled=true
management.health.livenessState.enabled=true
management.health.readinessState.enabled=true

# Configuración de grupos de health checks
management.endpoint.health.group.liveness.include=livenessState
management.endpoint.health.group.readiness.include=readinessState,db,diskSpace
```

### Verificación de Health Checks

```bash
# Health general
curl http://localhost:8080/actuator/health

# Liveness probe
curl http://localhost:8080/actuator/health/liveness

# Readiness probe
curl http://localhost:8080/actuator/health/readiness

# Usar script de verificación
./scripts/test-health-probes.sh
```

### Configuración en Kubernetes

Ejemplo de configuración de sondas en un Deployment:

```yaml
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8080
  initialDelaySeconds: 60
  periodSeconds: 10
  timeoutSeconds: 5
  failureThreshold: 3

readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 5
  timeoutSeconds: 3
  failureThreshold: 3
```

**Documentación completa**: Ver `HEALTH-CHECKS.md`

---

## Referencias

- [Spring Boot Profiles Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
- [Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Configuration Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)
- [Spring Boot Actuator Health](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.health)
- [Kubernetes Probes](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)

---

**Versión**: 1.1  
**Fecha**: 2026-04-23  
**Autor**: Josias Rubén Aldaz Arcalles
