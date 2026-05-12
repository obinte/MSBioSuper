# MSBioSuper - Microservicio Java 21

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Hibernate](https://img.shields.io/badge/Hibernate-6.5.3-blue.svg)](https://hibernate.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-Proprietary-red.svg)]()

## 📋 Descripción

MSBioSuper es un microservicio moderno desarrollado con **Java 21** y **Spring Boot 3** que proporciona APIs REST para la gestión de operaciones acuícolas. El sistema maneja información sobre pesca, siembra, liquidaciones, productos y cuentas por cobrar/pagar.

### 🎯 Características Principales

- **🚀 Java 21 LTS**: Aprovecha las últimas características como Virtual Threads y Records
- **🏗️ Arquitectura Hexagonal**: Separación clara entre dominio, aplicación e infraestructura
- **🔒 Seguridad JWT**: Autenticación moderna con JJWT 0.12.x
- **📊 Observabilidad**: Métricas Prometheus, health checks y logging estructurado
- **🐳 Containerización**: Docker y Docker Compose para desarrollo y despliegue
- **📚 Documentación API**: OpenAPI 3.0 con Swagger UI interactivo
- **🧪 Testing**: Cobertura completa con JUnit 5, Mockito y Testcontainers

---

## 🛠️ Prerequisitos

### Software Requerido

| Herramienta | Versión Mínima | Recomendada | Notas |
|-------------|----------------|-------------|-------|
| **Java** | 21 | 21 LTS | OpenJDK o Oracle JDK |
| **Maven** | 3.9.0 | 3.9.6+ | Para compilación y gestión de dependencias |
| **PostgreSQL** | 14 | 16+ | Base de datos principal |
| **Docker** | 20.10 | 24.0+ | Para contenedorización |
| **Docker Compose** | 2.0 | 2.20+ | Para orquestación de servicios |

### Verificar Instalación

```bash
# Verificar Java 21
java -version
# Debe mostrar: openjdk version "21.x.x"

# Verificar Maven
mvn -version
# Debe mostrar: Apache Maven 3.9.x

# Verificar Docker
docker --version && docker-compose --version
```

### Instalación de Prerequisitos

#### Java 21 (Ubuntu/Debian)
```bash
# Instalar OpenJDK 21
sudo apt update
sudo apt install openjdk-21-jdk

# Configurar JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' >> ~/.bashrc
```

#### Java 21 (macOS)
```bash
# Con Homebrew
brew install openjdk@21

# Configurar JAVA_HOME
echo 'export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home' >> ~/.zshrc
```

#### Maven (Ubuntu/Debian)
```bash
sudo apt install maven
```

#### Maven (macOS)
```bash
brew install maven
```

#### Docker (Ubuntu/Debian)
```bash
# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Instalar Docker Compose
sudo apt install docker-compose-plugin
```

---

## 🚀 Inicio Rápido

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd msbiosuper
```

### 2. Configurar Variables de Entorno

```bash
# Copiar archivo de ejemplo
cp .env.example .env

# Editar configuración (especialmente JWT_SECRET)
nano .env
```

**⚠️ IMPORTANTE**: Debe generar una clave JWT segura:

```bash
# Generar clave JWT segura (Linux/macOS)
openssl rand -base64 64

# O usar el script incluido
bash scripts/generate-jwt-secret.sh
```

Copie la clave generada y configúrela en el archivo `.env`:
```bash
JWT_SECRET=<clave-generada-aquí>
```

### 3. Iniciar con Docker Compose (Recomendado)

```bash
# Iniciar todos los servicios
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f

# Verificar que los servicios están corriendo
docker-compose ps
```

### 4. Verificar la Instalación

```bash
# Health check de la aplicación
curl http://localhost:8081/actuator/health

# Acceder a Swagger UI
open http://localhost:8080/swagger-ui.html
```

---

## 🔧 Configuración de Desarrollo Local

### Opción 1: Desarrollo con Docker (Recomendado)

Esta opción incluye **hot reload** y **debug remoto** automáticamente configurados.

```bash
# Iniciar servicios de desarrollo
docker-compose up -d

# La aplicación se reinicia automáticamente al cambiar código
# Debug remoto disponible en puerto 5005
```

**Características incluidas:**
- ✅ Hot reload con Spring Boot DevTools
- ✅ Debug remoto en puerto 5005
- ✅ LiveReload para navegador
- ✅ Logging detallado
- ✅ Base de datos PostgreSQL

**Configurar Debug en IDE:**

#### IntelliJ IDEA
1. Run → Edit Configurations
2. Add New Configuration → Remote JVM Debug
3. Host: `localhost`, Port: `5005`

#### VS Code
Crear `.vscode/launch.json`:
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Debug MSBioSuper",
      "request": "attach",
      "hostName": "localhost",
      "port": 5005
    }
  ]
}
```

### Opción 2: Desarrollo Nativo

Si prefiere ejecutar la aplicación directamente en su máquina:

#### 1. Configurar PostgreSQL Local

```bash
# Instalar PostgreSQL (Ubuntu/Debian)
sudo apt install postgresql postgresql-contrib

# Crear base de datos y usuario
sudo -u postgres psql
CREATE DATABASE cipolliny_abril_2026;
CREATE USER administradores WITH PASSWORD '@dm1n$t0d0c0mPU$';
GRANT ALL PRIVILEGES ON DATABASE cipolliny_abril_2026 TO administradores;
\q
```

#### 2. Configurar Variables de Entorno

```bash
export JDBC_SERVER=localhost
export JDBC_PORT=5432
export JDBC_DATABASE=cipolliny_abril_2026
export JDBC_USER=administradores
export JDBC_PASSWORD=@dm1n$t0d0c0mPU$
export JWT_SECRET=<su-clave-jwt-segura>
export SPRING_PROFILES_ACTIVE=dev
```

#### 3. Compilar y Ejecutar

```bash
# Compilar la aplicación
mvn clean compile

# Ejecutar con Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# O compilar JAR y ejecutar
mvn clean package -DskipTests
java -jar target/MSBioSuper-1.0.jar
```

---

## 🐳 Ejecución con Docker

### Construcción de Imagen Docker

#### Opción 1: Dockerfile Tradicional

```bash
# Construir imagen
docker build -t msbiosuper:latest .

# Ejecutar contenedor
docker run -d \
  --name msbiosuper \
  -p 8080:8080 \
  -p 8081:8081 \
  --env-file .env \
  msbiosuper:latest
```

#### Opción 2: Maven Jib (Sin Dockerfile)

```bash
# Construir imagen local con Jib
mvn jib:dockerBuild -P jib-local

# Ejecutar imagen construida con Jib
docker run -d \
  --name msbiosuper-jib \
  -p 8080:8080 \
  -p 8081:8081 \
  --env-file .env \
  msbiosuper-local:local-latest
```

### Docker Compose Completo

El archivo `docker-compose.yml` incluye:

- **MSBioSuper**: Aplicación principal
- **PostgreSQL**: Base de datos con persistencia
- **Redes**: Comunicación segura entre servicios
- **Volúmenes**: Persistencia de datos y logs
- **Health Checks**: Monitoreo automático de servicios

```bash
# Iniciar stack completo
docker-compose up -d

# Escalar aplicación (múltiples instancias)
docker-compose up -d --scale msbiosuper=3

# Ver logs de todos los servicios
docker-compose logs -f

# Detener y limpiar
docker-compose down -v
```

### Comandos Docker Útiles

```bash
# Ver estado de contenedores
docker-compose ps

# Ejecutar comando en contenedor
docker-compose exec msbiosuper sh

# Conectar a PostgreSQL
docker-compose exec postgres psql -U administradores -d cipolliny_abril_2026

# Ver uso de recursos
docker stats

# Backup de base de datos
docker-compose exec postgres pg_dump -U administradores cipolliny_abril_2026 > backup.sql

# Restaurar backup
docker-compose exec -T postgres psql -U administradores cipolliny_abril_2026 < backup.sql
```

---

## 📚 Documentación de API

### Swagger UI (Recomendado)

Acceda a la documentación interactiva en:
**http://localhost:8080/swagger-ui.html**

### OpenAPI Specification

La especificación OpenAPI 3.0 está disponible en:
**http://localhost:8080/v3/api-docs**

### Endpoints Principales

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/api/v1/security/authenticate` | POST | Autenticación JWT |
| `/api/v1/resumen-pesca` | POST | Obtener resumen de pesca |
| `/api/v1/resumen-siembra` | POST | Obtener resumen de siembra |
| `/api/v1/consumos-diarios` | POST | Obtener consumos diarios |
| `/api/v1/liquidaciones` | POST | Obtener liquidaciones |
| `/api/v1/productos` | POST | Obtener productos |
| `/api/v1/cuentas-cobrar` | POST | Cuentas por cobrar |
| `/api/v1/cuentas-pagar` | POST | Cuentas por pagar |

### Ejemplo de Autenticación

```bash
# 1. Obtener token JWT
curl -X POST http://localhost:8080/api/v1/security/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "testpass123"
  }'

# 2. Usar token en requests
curl -X POST http://localhost:8080/api/v1/resumen-pesca \
  -H "Authorization: Bearer <token-jwt>" \
  -H "Content-Type: application/json" \
  -d '{
    "fechaInicio": "2024-01-01",
    "fechaFin": "2024-12-31",
    "empresaCodigo": "001"
  }'
```

---

## 🏗️ Arquitectura

### Arquitectura Hexagonal (Ports & Adapters)

```
┌─────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │ REST        │  │ JPA         │  │ Security            │ │
│  │ Controllers │  │ Repositories│  │ Configuration       │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                    APPLICATION LAYER                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │ Use Cases   │  │ DTOs        │  │ Mappers             │ │
│  │             │  │             │  │                     │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │ Entities    │  │ Repository  │  │ Domain              │ │
│  │             │  │ Interfaces  │  │ Services            │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Estructura de Paquetes

```
com.acosux.MSBiosuper/
├── application/           # Casos de uso, DTOs, Mappers
│   ├── dto/              # Data Transfer Objects
│   ├── mapper/           # Conversores DTO ↔ Entity
│   └── usecase/          # Casos de uso de negocio
├── domain/               # Lógica de negocio pura
│   ├── model/            # Entidades de dominio
│   ├── repository/       # Interfaces de repositorio
│   └── service/          # Servicios de dominio
├── infrastructure/       # Adaptadores técnicos
│   ├── config/           # Configuración Spring
│   ├── persistence/      # Implementaciones JPA
│   ├── rest/             # Controladores REST
│   └── security/         # Configuración de seguridad
└── shared/               # Utilidades compartidas
    ├── exception/        # Excepciones personalizadas
    └── util/             # Utilidades generales
```

### Tecnologías Utilizadas

| Capa | Tecnología | Propósito |
|------|------------|-----------|
| **Runtime** | Java 21 LTS | Virtual Threads, Records, Pattern Matching |
| **Framework** | Spring Boot 3.3.5 | Configuración automática, Jakarta EE |
| **Persistencia** | Hibernate 6.5.3 | ORM moderno, JPA 3.1 |
| **Base de Datos** | PostgreSQL 16 | Base de datos relacional |
| **Pool Conexiones** | HikariCP | Pool de conexiones de alto rendimiento |
| **Seguridad** | Spring Security 6 | Autenticación y autorización |
| **JWT** | JJWT 0.12.6 | Tokens JWT seguros |
| **Documentación** | SpringDoc OpenAPI | Documentación API automática |
| **Testing** | JUnit 5 + Mockito | Pruebas unitarias e integración |
| **Contenedores** | Docker + Compose | Contenedorización y orquestación |
| **Métricas** | Micrometer + Prometheus | Observabilidad y monitoreo |

---

## 🧪 Testing

### Ejecutar Pruebas

```bash
# Todas las pruebas
mvn test

# Solo pruebas unitarias
mvn test -Dtest="*Test"

# Solo pruebas de integración
mvn test -Dtest="*IT"

# Con cobertura de código
mvn test jacoco:report

# Ver reporte de cobertura
open target/site/jacoco/index.html
```

### Tipos de Pruebas

#### 1. Pruebas Unitarias
- **Ubicación**: `src/test/java/**/*Test.java`
- **Framework**: JUnit 5 + Mockito
- **Cobertura**: > 80% (objetivo)

#### 2. Pruebas de Integración
- **Ubicación**: `src/test/java/**/*IT.java`
- **Framework**: Spring Boot Test + Testcontainers
- **Base de Datos**: PostgreSQL en contenedor

#### 3. Pruebas de API
- **Framework**: RestAssured + MockMvc
- **Cobertura**: Todos los endpoints REST

#### 4. Pruebas de Rendimiento
- **Framework**: Gatling
- **Objetivo**: p95 < 500ms, > 1000 req/sec

```bash
# Ejecutar pruebas de rendimiento
./scripts/run-performance-tests.sh load

# Ver reporte de rendimiento
open target/gatling/*/index.html
```

---

## 📊 Monitoreo y Observabilidad

### Health Checks

```bash
# Health check básico
curl http://localhost:8081/actuator/health

# Health check detallado
curl http://localhost:8081/actuator/health | jq

# Liveness probe (Kubernetes)
curl http://localhost:8081/actuator/health/liveness

# Readiness probe (Kubernetes)
curl http://localhost:8081/actuator/health/readiness
```

### Métricas Prometheus

```bash
# Todas las métricas
curl http://localhost:8081/actuator/prometheus

# Métricas específicas
curl http://localhost:8081/actuator/metrics/jvm.memory.used
curl http://localhost:8081/actuator/metrics/http.server.requests
```

### Endpoints de Actuator

| Endpoint | Descripción | Producción |
|----------|-------------|------------|
| `/actuator/health` | Estado de salud | ✅ |
| `/actuator/info` | Información de la app | ✅ |
| `/actuator/metrics` | Métricas de la app | ✅ |
| `/actuator/prometheus` | Métricas Prometheus | ✅ |
| `/actuator/env` | Variables de entorno | ❌ |
| `/actuator/loggers` | Configuración de logs | ❌ |

### Logging Estructurado

Los logs se generan en formato JSON para facilitar el análisis:

```json
{
  "timestamp": "2024-04-24T10:30:00.123Z",
  "level": "INFO",
  "logger": "com.acosux.MSBiosuper.application.usecase.GetResumenPescaUseCase",
  "message": "Procesando solicitud de resumen de pesca",
  "correlationId": "abc123-def456",
  "userId": "user123",
  "duration": 150
}
```

---

## 🔒 Seguridad

### Configuración JWT

La aplicación utiliza JWT para autenticación. **Es crítico configurar una clave secreta segura**.

#### Generar Clave Secreta

```bash
# Método 1: OpenSSL (recomendado)
openssl rand -base64 64

# Método 2: Script incluido
bash scripts/generate-jwt-secret.sh

# Método 3: Python
python3 -c "import secrets; print(secrets.token_urlsafe(64))"
```

#### Configurar en Producción

```bash
# Variable de entorno (recomendado)
export JWT_SECRET="<clave-generada-de-64-caracteres>"

# O en application-prod.properties
jwt.secret=${JWT_SECRET}
```

### Mejores Prácticas de Seguridad

1. **🔑 Secretos**: Nunca hardcodear secretos en código
2. **🔄 Rotación**: Rotar claves JWT cada 90 días
3. **⏰ Expiración**: Configurar expiración apropiada (1-24 horas)
4. **🌐 CORS**: Configurar orígenes permitidos específicos
5. **🛡️ HTTPS**: Usar HTTPS en producción
6. **📝 Auditoría**: Habilitar logging de seguridad

### Variables de Entorno Sensibles

```bash
# OBLIGATORIAS (aplicación no inicia sin ellas)
JDBC_PASSWORD=<contraseña-segura>
JWT_SECRET=<clave-jwt-64-caracteres>

# RECOMENDADAS
JDBC_USER=<usuario-db>
JDBC_DATABASE=<nombre-db>
CORS_ALLOWED_ORIGINS=https://app.acosux.com
```

---

## 🚀 Despliegue en Producción

### Preparación

1. **Configurar Variables de Entorno**:
```bash
export SPRING_PROFILES_ACTIVE=prod
export JDBC_SERVER=prod-db.acosux.com
export JDBC_PASSWORD=<contraseña-producción>
export JWT_SECRET=<clave-jwt-producción>
export CORS_ALLOWED_ORIGINS=https://app.acosux.com
```

2. **Construir Imagen de Producción**:
```bash
# Con Dockerfile
docker build -t msbiosuper:1.0.0 .

# Con Jib (recomendado)
mvn jib:build -P prod
```

3. **Ejecutar en Producción**:
```bash
docker run -d \
  --name msbiosuper-prod \
  -p 8080:8080 \
  -p 8081:8081 \
  --env-file .env.prod \
  --restart unless-stopped \
  msbiosuper:1.0.0
```

### Kubernetes

```yaml
# deployment.yaml
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
```

### Monitoreo en Producción

1. **Prometheus + Grafana**: Para métricas y alertas
2. **ELK Stack**: Para análisis de logs
3. **APM**: New Relic, Datadog o similar
4. **Alertas**: Configurar alertas para errores críticos

---

## 🛠️ Desarrollo y Contribución

### Configuración del Entorno de Desarrollo

1. **Clonar y configurar**:
```bash
git clone <repository-url>
cd msbiosuper
cp .env.example .env
# Editar .env con configuración local
```

2. **Instalar hooks de Git**:
```bash
# Pre-commit hooks para calidad de código
cp scripts/pre-commit .git/hooks/
chmod +x .git/hooks/pre-commit
```

3. **Configurar IDE**:
   - **IntelliJ IDEA**: Importar como proyecto Maven
   - **VS Code**: Instalar extensión Java Extension Pack
   - **Eclipse**: Importar como proyecto Maven existente

### Comandos de Desarrollo

```bash
# Compilar sin ejecutar pruebas
mvn compile

# Compilar y ejecutar pruebas
mvn test

# Ejecutar con hot reload
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Generar reporte de cobertura
mvn test jacoco:report

# Verificar calidad de código
mvn checkstyle:check spotbugs:check

# Construir JAR final
mvn clean package
```

### Estructura de Commits

Usar [Conventional Commits](https://www.conventionalcommits.org/):

```bash
# Ejemplos
git commit -m "feat: agregar endpoint de resumen de pesca"
git commit -m "fix: corregir validación de fechas"
git commit -m "docs: actualizar README con instrucciones Docker"
git commit -m "test: agregar pruebas unitarias para casos de uso"
```

### Pull Requests

1. Crear rama desde `main`: `git checkout -b feature/nueva-funcionalidad`
2. Implementar cambios con pruebas
3. Verificar que todas las pruebas pasan: `mvn test`
4. Crear PR con descripción detallada
5. Esperar revisión y aprobación

---

## 📖 Documentación Adicional

### Documentación Principal

- **[DEPLOYMENT.md](./DEPLOYMENT.md)**: Guía completa de despliegue en todos los entornos
- **[ARCHITECTURE.md](./ARCHITECTURE.md)**: Documentación de arquitectura hexagonal
- **[docs/README.md](docs/README.md)**: Índice de documentación técnica detallada

### Documentos Técnicos

- **[Configuración Detallada](docs/CONFIGURATION.md)**: Variables de entorno y configuración
- **[Desarrollo con Docker](docs/DOCKER-DEVELOPMENT.md)**: Guía completa de Docker
- **[Pruebas de Rendimiento](docs/PERFORMANCE-TESTING.md)**: Gatling y benchmarks
- **[Configuración JWT](docs/JWT-CONFIGURATION.md)**: Seguridad y autenticación
- **[Variables de Entorno](docs/ENVIRONMENT-VARIABLES.md)**: Referencia completa

### Arquitectura y Diseño

- **[Arquitectura Hexagonal](docs/VERIFICACION-ARQUITECTURA-HEXAGONAL.md)**: Principios y verificación
- **[Verificación de Seguridad](docs/VERIFICACION-SECURITY-CONFIG.md)**: Configuración de seguridad
- **[Métricas Prometheus](docs/VERIFICACION-METRICAS-PROMETHEUS.md)**: Observabilidad

### Scripts Útiles

| Script | Descripción |
|--------|-------------|
| `scripts/generate-jwt-secret.sh` | Generar clave JWT segura |
| `scripts/run-performance-tests.sh` | Ejecutar pruebas de rendimiento |
| `scripts/build-docker-image.sh` | Construir imagen Docker |
| `scripts/test-health-probes.sh` | Verificar health checks |

---

## ❓ Troubleshooting

### Problemas Comunes

#### 1. Error: "JWT_SECRET is required"
```bash
# Solución: Configurar clave JWT
openssl rand -base64 64
export JWT_SECRET="<clave-generada>"
```

#### 2. Error de conexión a PostgreSQL
```bash
# Verificar que PostgreSQL está corriendo
docker-compose ps postgres

# Verificar logs
docker-compose logs postgres

# Probar conexión
docker-compose exec postgres psql -U administradores -d cipolliny_abril_2026
```

#### 3. Puerto 8080 ya en uso
```bash
# Cambiar puerto en .env
echo "SERVER_PORT=8090" >> .env

# O detener proceso que usa el puerto
sudo lsof -ti:8080 | xargs kill -9
```

#### 4. OutOfMemoryError
```bash
# Aumentar memoria de JVM
export JAVA_OPTS="-Xmx1g -Xms512m"

# O en Docker
docker run -e JAVA_OPTS="-Xmx1g" msbiosuper:latest
```

### Logs y Debugging

```bash
# Ver logs de aplicación
tail -f logs/msbiosuper.log

# Ver logs con Docker Compose
docker-compose logs -f msbiosuper

# Habilitar debug SQL
export LOGGING_LEVEL_ORG_HIBERNATE_SQL=DEBUG

# Conectar debugger remoto (puerto 5005)
# Configurar en IDE: localhost:5005
```

### Obtener Ayuda

1. **Documentación**: Revisar archivos en `docs/`
2. **Issues**: Crear issue en el repositorio
3. **Logs**: Incluir logs relevantes en reportes de bugs
4. **Configuración**: Verificar variables de entorno

---

## 📄 Licencia

Este proyecto es propiedad de **Acosux** y está protegido por derechos de autor. El uso, distribución o modificación requiere autorización explícita.

---

## 👥 Equipo de Desarrollo

- **Autor Principal**: Josias Rubén Aldaz Arcalles
- **Empresa**: Acosux
- **Proyecto**: Migración MSBioSuper a Java 21
- **Fecha**: Abril 2026

---

## 📞 Contacto

- **Email**: dev@acosux.com
- **Documentación**: `docs/README.md`
- **Issues**: Crear issue en el repositorio del proyecto

---

**¡Gracias por usar MSBioSuper! 🚀**