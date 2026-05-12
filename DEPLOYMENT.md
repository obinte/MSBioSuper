# Guía de Despliegue - MSBioSuper Java 21

## 📋 Información General

- **Proyecto**: MSBioSuper - Migración a Java 21
- **Versión**: 1.0
- **Arquitectura**: Microservicio con arquitectura hexagonal
- **Runtime**: Java 21 LTS + Spring Boot 3.3.5
- **Base de Datos**: PostgreSQL 16+
- **Contenedorización**: Docker + Docker Compose

---

## 🎯 Resumen Ejecutivo

Esta guía proporciona instrucciones completas para desplegar MSBioSuper en diferentes entornos, desde desarrollo local hasta producción. El sistema utiliza contenedorización Docker para garantizar consistencia entre entornos.

### Características del Despliegue

- **🚀 Despliegue con un comando**: `docker-compose up -d`
- **🔒 Seguridad integrada**: JWT, HTTPS, usuario no-root
- **📊 Observabilidad completa**: Health checks, métricas Prometheus, logs estructurados
- **🔄 Rollback automático**: Procedimientos documentados de recuperación
- **⚡ Alta disponibilidad**: Health checks y reinicio automático

---

## 📋 Prerequisitos

### Software Requerido

| Componente | Versión Mínima | Recomendada | Notas |
|------------|----------------|-------------|-------|
| **Docker** | 20.10+ | 24.0+ | Para contenedorización |
| **Docker Compose** | 2.0+ | 2.20+ | Orquestación de servicios |
| **Java** | 21 | 21 LTS | Solo para desarrollo local sin Docker |
| **PostgreSQL** | 14+ | 16+ | Base de datos (incluida en Docker Compose) |

### Verificación de Prerequisitos

```bash
# Verificar Docker
docker --version
docker-compose --version

# Verificar que Docker está corriendo
docker info

# Verificar Java (opcional, solo para desarrollo sin Docker)
java -version
```

### Recursos del Sistema

| Entorno | CPU | RAM | Disco | Red |
|---------|-----|-----|-------|-----|
| **Desarrollo** | 2 cores | 4GB | 10GB | 100Mbps |
| **Staging** | 2 cores | 8GB | 20GB | 1Gbps |
| **Producción** | 4+ cores | 16GB+ | 50GB+ | 1Gbps+ |

---

## 🔧 Configuración de Variables de Entorno

### Variables Obligatorias

Estas variables **DEBEN** configurarse antes del despliegue:

```bash
# Configuración de Base de Datos
JDBC_SERVER=localhost          # Servidor PostgreSQL
JDBC_PORT=5432                # Puerto PostgreSQL
JDBC_DATABASE=cipolliny_Abril_2026  # Nombre de la base de datos
JDBC_USER=administradores     # Usuario de base de datos
JDBC_PASSWORD=tu_password_seguro    # Contraseña de base de datos

# Seguridad JWT (CRÍTICO)
JWT_SECRET=tu_clave_jwt_base64_de_64_caracteres
JWT_EXPIRATION=86400000       # 24 horas en milisegundos

# CORS (Producción)
CORS_ALLOWED_ORIGINS=https://tu-dominio.com,https://app.tu-dominio.com
```

### Variables Opcionales

```bash
# Configuración del Servidor
SERVER_PORT=8080              # Puerto de la aplicación
MANAGEMENT_SERVER_PORT=8081   # Puerto de Actuator/métricas

# Perfil de Spring Boot
SPRING_PROFILES_ACTIVE=prod   # dev, test, prod

# Configuración de Swagger (Producción: false)
SWAGGER_ENABLED=false         # true solo en desarrollo

# Configuración de Aplicación
APP_VERSION=1.0.0
API_DEV_URL=http://localhost:8080
API_STAGING_URL=https://staging-api.tu-dominio.com
API_PROD_URL=https://api.tu-dominio.com
```

### Generar Clave JWT Segura

**⚠️ CRÍTICO**: La clave JWT debe ser segura y única por entorno.

```bash
# Método 1: OpenSSL (recomendado)
openssl rand -base64 64

# Método 2: Script incluido
bash scripts/generate-jwt-secret.sh

# Método 3: Python
python3 -c "import secrets; print(secrets.token_urlsafe(64))"
```

### Configurar Variables de Entorno

#### Opción 1: Archivo .env (Desarrollo)

```bash
# Copiar plantilla
cp .env.example .env

# Editar configuración
nano .env
```

#### Opción 2: Variables del Sistema (Producción)

```bash
# Configurar en el sistema
export JWT_SECRET="tu_clave_generada_aqui"
export JDBC_PASSWORD="tu_password_seguro"
export CORS_ALLOWED_ORIGINS="https://tu-dominio.com"

# Hacer permanente (Ubuntu/Debian)
echo 'export JWT_SECRET="tu_clave_generada_aqui"' >> ~/.bashrc
```

#### Opción 3: Docker Secrets (Producción Avanzada)

```bash
# Crear secretos Docker
echo "tu_password_seguro" | docker secret create db_password -
echo "tu_clave_jwt" | docker secret create jwt_secret -
```

---

## 🚀 Despliegue por Entornos

### 🔧 Desarrollo Local

#### Opción A: Docker Compose (Recomendado)

```bash
# 1. Clonar repositorio
git clone <repository-url>
cd msbiosuper

# 2. Configurar variables de entorno
cp .env.example .env
# Editar .env con tu configuración

# 3. Iniciar servicios
docker-compose up -d

# 4. Verificar despliegue
curl http://localhost:8080/actuator/health
```

#### Opción B: Desarrollo Nativo (Sin Docker)

```bash
# 1. Configurar PostgreSQL local
sudo -u postgres createdb cipolliny_Abril_2026
sudo -u postgres createuser administradores

# 2. Configurar variables de entorno
export JDBC_SERVER=localhost
export JDBC_PASSWORD=tu_password
export JWT_SECRET=tu_clave_jwt

# 3. Compilar y ejecutar
mvn clean package -DskipTests
java -jar target/MSBioSuper-1.0.jar
```

### 🧪 Staging/Testing

```bash
# 1. Preparar entorno
export SPRING_PROFILES_ACTIVE=test
export JDBC_SERVER=staging-db.tu-dominio.com
export CORS_ALLOWED_ORIGINS=https://staging.tu-dominio.com

# 2. Usar configuración de staging
docker-compose -f docker-compose.yml -f docker-compose.staging.yml up -d

# 3. Ejecutar pruebas de integración
docker-compose exec msbiosuper mvn test -Dspring.profiles.active=test
```

### 🏭 Producción

#### Preparación Pre-Despliegue

```bash
# 1. Crear respaldo de base de datos
pg_dump -h $JDBC_SERVER -U $JDBC_USER $JDBC_DATABASE > backup_$(date +%Y%m%d_%H%M%S).sql

# 2. Verificar configuración
docker-compose config

# 3. Construir imagen de producción
docker build -t msbiosuper:$(git rev-parse --short HEAD) .
```

#### Despliegue en Producción

```bash
# 1. Configurar variables de producción
export SPRING_PROFILES_ACTIVE=prod
export JDBC_SERVER=prod-db.tu-dominio.com
export CORS_ALLOWED_ORIGINS=https://tu-dominio.com
export SWAGGER_ENABLED=false

# 2. Desplegar con configuración de producción
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

# 3. Verificar despliegue
./scripts/verificacion-final-produccion.sh
```

---

## 🔍 Verificación del Despliegue

### Health Checks Básicos

```bash
# 1. Verificar que la aplicación está corriendo
curl -f http://localhost:8080/actuator/health

# Respuesta esperada:
# {"status":"UP","components":{"db":{"status":"UP"},"diskSpace":{"status":"UP"}}}

# 2. Verificar conectividad a base de datos
curl -s http://localhost:8080/actuator/health | jq '.components.db.status'
# Debe retornar: "UP"

# 3. Verificar métricas
curl -f http://localhost:8080/actuator/prometheus | head -10
```

### Verificación de Autenticación

```bash
# 1. Obtener token JWT
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/security/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpass123"}' | jq -r '.token')

# 2. Probar endpoint protegido
curl -H "Authorization: Bearer $TOKEN" \
  -X POST http://localhost:8080/api/v1/resumen-pesca \
  -H "Content-Type: application/json" \
  -d '{"fechaInicio":"2024-01-01","fechaFin":"2024-01-31","empresaCodigo":"001"}'
```

### Verificación de Endpoints

```bash
# Probar todos los endpoints principales
endpoints=(
  "resumen-pesca"
  "resumen-siembra"
  "consumos-diarios"
  "liquidaciones"
  "productos"
  "cuentas-cobrar"
  "cuentas-pagar"
)

for endpoint in "${endpoints[@]}"; do
  echo "Probando $endpoint..."
  curl -s -H "Authorization: Bearer $TOKEN" \
    -X POST "http://localhost:8080/api/v1/$endpoint" \
    -H "Content-Type: application/json" \
    -d '{"empresa":"TEST"}' \
    -w "Status: %{http_code}\n"
done
```

### Verificación de Documentación

```bash
# 1. OpenAPI Specification
curl -f http://localhost:8080/v3/api-docs | jq '.info.title'

# 2. Swagger UI (si está habilitado)
curl -f http://localhost:8080/swagger-ui.html
```

---

## 📊 Monitoreo y Observabilidad

### Métricas Disponibles

```bash
# Métricas de aplicación
curl http://localhost:8080/actuator/prometheus | grep -E "(http_requests|jvm_memory|hikaricp_connections)"

# Métricas específicas
curl http://localhost:8080/actuator/metrics/jvm.memory.used
curl http://localhost:8080/actuator/metrics/http.server.requests
curl http://localhost:8080/actuator/metrics/jdbc.connections.active
```

### Health Checks para Kubernetes

```yaml
# Liveness Probe
livenessProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 8081
  initialDelaySeconds: 60
  periodSeconds: 30

# Readiness Probe
readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 8081
  initialDelaySeconds: 30
  periodSeconds: 10
```

### Logs Estructurados

```bash
# Ver logs en formato JSON
docker-compose logs msbiosuper | tail -10

# Filtrar logs por nivel
docker-compose logs msbiosuper | jq 'select(.level == "ERROR")'

# Buscar por correlation ID
docker-compose logs msbiosuper | jq 'select(.correlationId == "abc123")'
```

---

## 🔄 Procedimientos de Rollback

### Criterios para Rollback

Ejecutar rollback inmediatamente si:

- ❌ La aplicación no inicia después de 5 minutos
- ❌ Health checks fallan consistentemente (>3 intentos)
- ❌ Errores en >50% de las requests durante 5 minutos
- ❌ Pérdida de conectividad a base de datos
- ❌ Uso de memoria >90% sostenido por 10 minutos
- ❌ Cualquier error que afecte funcionalidad crítica

### Rollback Automático (Docker)

```bash
# 1. Detener versión actual
docker-compose down

# 2. Restaurar versión anterior
docker tag msbiosuper:previous msbiosuper:latest

# 3. Reiniciar con versión anterior
docker-compose up -d

# 4. Verificar rollback
curl -f http://localhost:8080/actuator/health
```

### Rollback Manual de Base de Datos

```bash
# Solo si hay corrupción de datos confirmada
# 1. Detener aplicación
docker-compose down

# 2. Restaurar base de datos
psql -h $JDBC_SERVER -U $JDBC_USER -d $JDBC_DATABASE < backup_YYYYMMDD_HHMMSS.sql

# 3. Reiniciar aplicación
docker-compose up -d
```

### Verificación Post-Rollback

```bash
# Ejecutar todas las verificaciones de despliegue
./scripts/verificacion-final-produccion.sh

# Monitorear por 30 minutos
watch -n 30 'curl -s http://localhost:8080/actuator/health | jq .status'
```

---

## 🐳 Configuración Docker Avanzada

### Build Multi-Etapa

```dockerfile
# Optimizado para producción
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
RUN adduser -D -s /bin/sh msbiosuper
USER msbiosuper
COPY --from=builder /build/target/MSBioSuper-1.0.jar app.jar
EXPOSE 8080 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose para Producción

```yaml
# docker-compose.prod.yml
version: '3.8'
services:
  msbiosuper:
    image: msbiosuper:latest
    restart: unless-stopped
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JAVA_OPTS=-Xms512m -Xmx1g -XX:+UseZGC
    deploy:
      resources:
        limits:
          cpus: '2.0'
          memory: 2G
        reservations:
          cpus: '1.0'
          memory: 1G
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8081/actuator/health/liveness"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s
```

### Optimización de Imagen

```bash
# Construir imagen optimizada
docker build --target runtime -t msbiosuper:optimized .

# Verificar tamaño (objetivo: <200MB)
docker images msbiosuper:optimized

# Escanear vulnerabilidades
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
  aquasec/trivy image msbiosuper:optimized
```

---

## ☸️ Despliegue en Kubernetes

### Deployment Manifest

```yaml
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
        - containerPort: 8081
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: msbiosuper-secrets
              key: jwt-secret
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8081
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8081
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

### Service y Ingress

```yaml
---
apiVersion: v1
kind: Service
metadata:
  name: msbiosuper-service
spec:
  selector:
    app: msbiosuper
  ports:
  - name: http
    port: 80
    targetPort: 8080
  - name: management
    port: 8081
    targetPort: 8081

---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: msbiosuper-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: api.tu-dominio.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: msbiosuper-service
            port:
              number: 80
```

---

## 🔐 Seguridad en Producción

### Configuración HTTPS

```bash
# Generar certificados SSL (Let's Encrypt)
certbot certonly --standalone -d api.tu-dominio.com

# Configurar en reverse proxy (Nginx)
server {
    listen 443 ssl;
    server_name api.tu-dominio.com;
    
    ssl_certificate /etc/letsencrypt/live/api.tu-dominio.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.tu-dominio.com/privkey.pem;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### Hardening de Seguridad

```bash
# 1. Ejecutar como usuario no-root
docker run --user 1001:1001 msbiosuper:latest

# 2. Configurar firewall
ufw allow 80/tcp
ufw allow 443/tcp
ufw deny 8080/tcp  # Solo acceso interno

# 3. Configurar fail2ban para proteger endpoints
cat > /etc/fail2ban/jail.d/msbiosuper.conf << EOF
[msbiosuper]
enabled = true
port = 80,443
filter = msbiosuper
logpath = /var/log/nginx/access.log
maxretry = 5
bantime = 3600
EOF
```

### Rotación de Secretos

```bash
# Script para rotar JWT secret
#!/bin/bash
NEW_SECRET=$(openssl rand -base64 64)

# Actualizar en variables de entorno
export JWT_SECRET="$NEW_SECRET"

# Reiniciar aplicación con rolling update
docker-compose up -d --force-recreate msbiosuper

# Verificar que funciona
sleep 60
curl -f http://localhost:8080/actuator/health
```

---

## 📈 Escalabilidad y Performance

### Configuración de Performance

```bash
# Variables de entorno para optimización
export JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseZGC -XX:+ZGenerational"
export HIKARI_MAXIMUM_POOL_SIZE=20
export HIKARI_MINIMUM_IDLE=5
```

### Load Balancing

```yaml
# docker-compose con múltiples instancias
version: '3.8'
services:
  msbiosuper:
    image: msbiosuper:latest
    deploy:
      replicas: 3
    ports:
      - "8080-8082:8080"
  
  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
```

### Monitoreo de Performance

```bash
# Métricas de rendimiento
curl -s http://localhost:8080/actuator/prometheus | grep -E "(http_request_duration|jvm_gc_pause)"

# Pruebas de carga básicas
ab -n 1000 -c 10 http://localhost:8080/actuator/health
```

---

## 🛠️ Troubleshooting

### Problemas Comunes

#### 1. Aplicación no inicia

```bash
# Verificar logs
docker-compose logs msbiosuper

# Problemas comunes:
# - JWT_SECRET no configurado
# - Base de datos no accesible
# - Puerto ya en uso
```

#### 2. Error de conexión a base de datos

```bash
# Verificar conectividad
docker-compose exec msbiosuper ping postgres

# Verificar credenciales
docker-compose exec postgres psql -U $JDBC_USER -d $JDBC_DATABASE -c "SELECT 1;"
```

#### 3. Problemas de memoria

```bash
# Verificar uso de memoria
docker stats msbiosuper

# Ajustar límites
export JAVA_OPTS="-Xmx512m"
docker-compose up -d --force-recreate
```

#### 4. Problemas de autenticación JWT

```bash
# Verificar configuración JWT
curl -s http://localhost:8080/actuator/configprops | jq '.jwt'

# Regenerar secret
./scripts/generate-jwt-secret.sh
```

### Logs de Diagnóstico

```bash
# Habilitar debug logging
export LOGGING_LEVEL_COM_ACOSUX_MSBIOSUPER=DEBUG

# Ver logs estructurados
docker-compose logs msbiosuper | jq 'select(.level == "ERROR" or .level == "WARN")'
```

### Herramientas de Diagnóstico

```bash
# Health check detallado
curl -s http://localhost:8080/actuator/health | jq

# Información de la aplicación
curl -s http://localhost:8080/actuator/info | jq

# Métricas de JVM
curl -s http://localhost:8080/actuator/metrics/jvm.memory.used | jq
```

---

## 📚 Scripts de Automatización

### Script de Despliegue Completo

```bash
#!/bin/bash
# deploy.sh - Script de despliegue automatizado

set -e

echo "🚀 Iniciando despliegue de MSBioSuper..."

# 1. Verificar prerequisitos
./scripts/check-prerequisites.sh

# 2. Crear respaldo
./scripts/backup-database.sh

# 3. Construir imagen
docker build -t msbiosuper:latest .

# 4. Desplegar
docker-compose up -d

# 5. Verificar despliegue
./scripts/verify-deployment.sh

echo "✅ Despliegue completado exitosamente"
```

### Script de Verificación

```bash
#!/bin/bash
# verify-deployment.sh

echo "🔍 Verificando despliegue..."

# Health check
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ Health check: OK"
else
    echo "❌ Health check: FAIL"
    exit 1
fi

# Autenticación
if curl -f -X POST http://localhost:8080/api/v1/security/authenticate \
    -H "Content-Type: application/json" \
    -d '{"username":"test","password":"test"}' > /dev/null 2>&1; then
    echo "✅ Autenticación: OK"
else
    echo "⚠️  Autenticación: No se pudo probar (normal si no hay usuarios de prueba)"
fi

echo "✅ Verificación completada"
```

---

## 📞 Soporte y Contactos

### Equipo de Desarrollo

- **Desarrollador Principal**: Josias Rubén Aldaz Arcalles
- **Email**: dev@acosux.com
- **Repositorio**: [URL del repositorio]

### Documentación Adicional

- **[README.md](./README.md)**: Guía de desarrollo y configuración
- **[ARCHITECTURE.md](./ARCHITECTURE.md)**: Documentación de arquitectura hexagonal
- **[docs/](./docs/)**: Documentación técnica detallada

### Recursos Útiles

- **Health Checks**: http://localhost:8080/actuator/health
- **Métricas**: http://localhost:8080/actuator/prometheus
- **API Docs**: http://localhost:8080/v3/api-docs
- **Swagger UI**: http://localhost:8080/swagger-ui.html (solo desarrollo)

---

## 📝 Historial de Versiones

| Versión | Fecha | Cambios |
|---------|-------|---------|
| 1.0 | 2026-01-09 | Versión inicial - Migración Java 21 completa |

---

**¡Gracias por usar MSBioSuper! 🚀**

Para soporte adicional, consulte la documentación en el directorio `docs/` o contacte al equipo de desarrollo.