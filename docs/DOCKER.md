# Documentación Docker - MSBioSuper

## Descripción General

Este documento describe la configuración de Docker para MSBioSuper, incluyendo el Dockerfile multi-etapa, mejores prácticas de seguridad y optimización.

---

## Dockerfile Multi-Etapa

### Arquitectura

El Dockerfile implementa un **build multi-etapa** con dos etapas principales:

```
┌─────────────────────────────────────────┐
│  ETAPA 1: BUILD (builder)               │
│  - Imagen: eclipse-temurin:21-jdk-alpine│
│  - Propósito: Compilar la aplicación    │
│  - Tamaño: ~400MB (no incluido en final)│
└─────────────────────────────────────────┘
                    │
                    │ Copia JAR compilado
                    ▼
┌─────────────────────────────────────────┐
│  ETAPA 2: RUNTIME (runtime)             │
│  - Imagen: eclipse-temurin:21-jre-alpine│
│  - Propósito: Ejecutar la aplicación    │
│  - Tamaño: ~150-200MB (imagen final)    │
└─────────────────────────────────────────┘
```

### Ventajas del Build Multi-Etapa

1. **Tamaño Reducido**: La imagen final solo contiene el JRE y el JAR, no las herramientas de build
2. **Seguridad**: Menos superficie de ataque al no incluir compiladores y herramientas de desarrollo
3. **Caché Eficiente**: Las dependencias de Maven se cachean en una capa separada
4. **Reproducibilidad**: Build consistente en cualquier entorno

---

## Construcción de la Imagen

### Opción 1: Script Automatizado (Recomendado)

```bash
# Build con tag latest
./scripts/build-docker-image.sh

# Build con tag específico
./scripts/build-docker-image.sh 1.0.0

# Build con tag de versión y latest
./scripts/build-docker-image.sh 1.0.0 && \
docker tag msbiosuper:1.0.0 msbiosuper:latest
```

### Opción 2: Comando Docker Directo

```bash
# Build básico
docker build -t msbiosuper:latest .

# Build con argumentos personalizados
docker build \
  --tag msbiosuper:1.0.0 \
  --build-arg BUILD_DATE="$(date -u +'%Y-%m-%dT%H:%M:%SZ')" \
  --build-arg VCS_REF="$(git rev-parse --short HEAD)" \
  .

# Build solo de la etapa builder (para debugging)
docker build --target builder -t msbiosuper:builder .
```

### Verificar la Construcción

```bash
# Listar imágenes
docker images msbiosuper

# Inspeccionar la imagen
docker inspect msbiosuper:latest

# Ver historial de capas
docker history msbiosuper:latest
```

---

## Ejecución del Contenedor

### Ejecución Básica

```bash
# Ejecutar en primer plano
docker run -p 8080:8080 msbiosuper:latest

# Ejecutar en segundo plano (detached)
docker run -d -p 8080:8080 --name msbiosuper msbiosuper:latest

# Ver logs
docker logs -f msbiosuper
```

### Ejecución con Variables de Entorno

```bash
docker run -d \
  -p 8080:8080 \
  -p 8081:8081 \
  --name msbiosuper \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=postgres \
  -e DB_PORT=5432 \
  -e DB_NAME=msbiosuper \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=secret \
  -e JWT_SECRET=your-secret-key \
  msbiosuper:latest
```

### Ejecución con Archivo .env

```bash
# Crear archivo .env con variables
cat > .env << EOF
SPRING_PROFILES_ACTIVE=prod
DB_HOST=postgres
DB_PORT=5432
DB_NAME=msbiosuper
DB_USERNAME=postgres
DB_PASSWORD=secret
JWT_SECRET=your-secret-key
EOF

# Ejecutar con archivo .env
docker run -d \
  -p 8080:8080 \
  --name msbiosuper \
  --env-file .env \
  msbiosuper:latest
```

### Ejecución con Volúmenes

```bash
# Montar directorio de logs
docker run -d \
  -p 8080:8080 \
  --name msbiosuper \
  -v $(pwd)/logs:/app/logs \
  msbiosuper:latest

# Montar configuración externa
docker run -d \
  -p 8080:8080 \
  --name msbiosuper \
  -v $(pwd)/config/application.properties:/app/config/application.properties \
  -e SPRING_CONFIG_LOCATION=file:/app/config/application.properties \
  msbiosuper:latest
```

---

## Configuración de la Imagen

### Variables de Entorno

| Variable | Descripción | Valor por Defecto | Requerido |
|----------|-------------|-------------------|-----------|
| `SPRING_PROFILES_ACTIVE` | Perfil de Spring Boot | `prod` | No |
| `SERVER_PORT` | Puerto de la aplicación | `8080` | No |
| `MANAGEMENT_SERVER_PORT` | Puerto de Actuator | `8081` | No |
| `JAVA_OPTS` | Opciones de JVM | `-Xms256m -Xmx512m` | No |
| `DB_HOST` | Host de PostgreSQL | - | Sí |
| `DB_PORT` | Puerto de PostgreSQL | `5432` | No |
| `DB_NAME` | Nombre de la base de datos | - | Sí |
| `DB_USERNAME` | Usuario de la base de datos | - | Sí |
| `DB_PASSWORD` | Contraseña de la base de datos | - | Sí |
| `JWT_SECRET` | Clave secreta para JWT | - | Sí |
| `TZ` | Zona horaria | `America/Guayaquil` | No |

### Puertos Expuestos

- **8080**: Puerto principal de la aplicación (API REST)
- **8081**: Puerto de Actuator (health checks, métricas)

### Health Check

El contenedor incluye un health check automático:

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health/liveness || exit 1
```

**Parámetros**:
- `interval`: Verifica cada 30 segundos
- `timeout`: Espera 3 segundos por respuesta
- `start-period`: Da 60 segundos para que la app inicie
- `retries`: 3 intentos antes de marcar como unhealthy

**Verificar estado de salud**:
```bash
docker inspect --format='{{.State.Health.Status}}' msbiosuper
```

---

## Optimizaciones de JVM

### Configuración de Memoria

El Dockerfile incluye optimizaciones para contenedores:

```bash
-XX:+UseContainerSupport      # Detecta límites de memoria del contenedor
-XX:MaxRAMPercentage=75.0     # Usa máximo 75% de RAM disponible
```

**Ejemplo de límites de memoria**:
```bash
# Limitar a 512MB de RAM
docker run -d \
  -p 8080:8080 \
  --name msbiosuper \
  --memory=512m \
  --memory-swap=512m \
  msbiosuper:latest
```

### Garbage Collector

Se usa **ZGC (Z Garbage Collector)** para baja latencia:

```bash
-XX:+UseZGC          # Habilita ZGC
-XX:+ZGenerational   # Usa ZGC generacional (Java 21)
```

**Ventajas de ZGC**:
- Pausas de GC < 1ms
- Escalable hasta terabytes de heap
- Ideal para aplicaciones de baja latencia

### Virtual Threads (Java 21)

La aplicación está configurada para usar Virtual Threads:

```properties
spring.threads.virtual.enabled=true
```

**Beneficios**:
- Miles de threads concurrentes con bajo overhead
- Mejor rendimiento en operaciones I/O
- Simplifica código asíncrono

---

## Seguridad

### Usuario No-Root

El contenedor ejecuta la aplicación como usuario no-root:

```dockerfile
RUN addgroup -g 1001 -S msbiosuper && \
    adduser -u 1001 -S msbiosuper -G msbiosuper
USER msbiosuper
```

**Verificar usuario**:
```bash
docker exec msbiosuper whoami
# Output: msbiosuper
```

### Escaneo de Vulnerabilidades

```bash
# Escanear con Docker Scout (recomendado)
docker scout cves msbiosuper:latest

# Escanear con Trivy
trivy image msbiosuper:latest

# Escanear con Snyk
snyk container test msbiosuper:latest
```

### Mejores Prácticas de Seguridad

1. **No incluir secretos en la imagen**
   - Usar variables de entorno
   - Usar Docker secrets o Kubernetes secrets

2. **Mantener imagen actualizada**
   - Usar imagen base oficial
   - Actualizar regularmente

3. **Minimizar superficie de ataque**
   - Imagen Alpine (mínima)
   - Solo dependencias necesarias

4. **Escanear regularmente**
   - CI/CD con escaneo automático
   - Alertas de vulnerabilidades

---

## Troubleshooting

### Problema: Contenedor no inicia

```bash
# Ver logs detallados
docker logs msbiosuper

# Ver eventos del contenedor
docker events --filter container=msbiosuper

# Ejecutar shell en el contenedor
docker exec -it msbiosuper sh
```

### Problema: Health check falla

```bash
# Verificar estado de salud
docker inspect --format='{{json .State.Health}}' msbiosuper | jq

# Probar health check manualmente
docker exec msbiosuper curl -f http://localhost:8081/actuator/health/liveness
```

### Problema: Conexión a base de datos falla

```bash
# Verificar variables de entorno
docker exec msbiosuper env | grep DB_

# Probar conectividad desde el contenedor
docker exec msbiosuper ping postgres
docker exec msbiosuper nc -zv postgres 5432
```

### Problema: Memoria insuficiente

```bash
# Ver uso de memoria
docker stats msbiosuper

# Aumentar límite de memoria
docker update --memory=1g msbiosuper

# O recrear con más memoria
docker run -d \
  -p 8080:8080 \
  --name msbiosuper \
  --memory=1g \
  -e JAVA_OPTS="-Xms512m -Xmx768m" \
  msbiosuper:latest
```

---

## Comandos Útiles

### Gestión de Contenedores

```bash
# Listar contenedores
docker ps -a

# Detener contenedor
docker stop msbiosuper

# Iniciar contenedor
docker start msbiosuper

# Reiniciar contenedor
docker restart msbiosuper

# Eliminar contenedor
docker rm -f msbiosuper

# Ver logs en tiempo real
docker logs -f msbiosuper

# Ver estadísticas de recursos
docker stats msbiosuper
```

### Gestión de Imágenes

```bash
# Listar imágenes
docker images

# Eliminar imagen
docker rmi msbiosuper:latest

# Limpiar imágenes no usadas
docker image prune -a

# Exportar imagen
docker save msbiosuper:latest | gzip > msbiosuper-latest.tar.gz

# Importar imagen
docker load < msbiosuper-latest.tar.gz
```

### Debugging

```bash
# Ejecutar shell en contenedor
docker exec -it msbiosuper sh

# Copiar archivos desde contenedor
docker cp msbiosuper:/app/logs/msbiosuper.log ./

# Inspeccionar configuración
docker inspect msbiosuper

# Ver procesos en el contenedor
docker top msbiosuper
```

---

## Docker Compose para Desarrollo

### Configuración de Desarrollo con Override

El proyecto incluye `docker-compose.override.yml` que proporciona configuración específica para desarrollo:

#### Características de Desarrollo

1. **Hot Reload con Spring Boot DevTools**
   - Reinicio automático al detectar cambios
   - LiveReload para recarga automática del navegador

2. **Debug Remoto**
   - Puerto 5005 expuesto para depuración con IDE
   - Configuración JDWP habilitada

3. **Logging Detallado**
   - Nivel DEBUG para paquetes de aplicación
   - SQL queries visibles en consola
   - Formato de log legible (no JSON)

4. **Actuator Completo**
   - Todos los endpoints expuestos
   - Detalles completos de health checks

#### Uso de Docker Compose

```bash
# Iniciar servicios (aplica automáticamente el override)
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f msbiosuper

# Reiniciar solo la aplicación
docker-compose restart msbiosuper

# Detener servicios
docker-compose down

# Reconstruir y reiniciar
docker-compose up -d --build
```

#### Configuración de Debug Remoto en IDE

**IntelliJ IDEA**:
1. Run > Edit Configurations
2. Add New Configuration > Remote JVM Debug
3. Host: `localhost`, Port: `5005`
4. Click OK y luego Debug

**Visual Studio Code**:
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

**Eclipse**:
1. Run > Debug Configurations
2. Remote Java Application > New
3. Host: `localhost`, Port: `5005`
4. Click Debug

#### Puertos de Desarrollo

| Puerto | Servicio | Descripción |
|--------|----------|-------------|
| 8080 | API REST | Endpoints de la aplicación |
| 8081 | Actuator | Health checks y métricas |
| 5005 | JDWP | Debug remoto |
| 35729 | LiveReload | Recarga automática del navegador |
| 5432 | PostgreSQL | Base de datos |

#### Variables de Entorno de Desarrollo

El archivo override configura automáticamente:
- `SPRING_PROFILES_ACTIVE=dev`
- `SPRING_DEVTOOLS_RESTART_ENABLED=true`
- `LOGGING_LEVEL_COM_ACOSUX=DEBUG`
- `SPRING_JPA_SHOW_SQL=true`
- `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=*`

#### Acceso a Herramientas

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8081/actuator/health
- **Metrics**: http://localhost:8081/actuator/prometheus
- **All Actuator**: http://localhost:8081/actuator

### Diferencias entre Producción y Desarrollo

| Aspecto | Producción | Desarrollo |
|---------|-----------|------------|
| Perfil Spring | `prod` | `dev` |
| Logging | JSON estructurado | Formato legible |
| Nivel de log | INFO | DEBUG |
| SQL visible | No | Sí |
| Debug remoto | No | Sí (puerto 5005) |
| DevTools | Deshabilitado | Habilitado |
| Actuator | Endpoints limitados | Todos los endpoints |
| CSRF | Habilitado | Deshabilitado |
| Cache | Habilitado | Deshabilitado |

## Próximos Pasos

1. **Docker Compose**: Ver `docker-compose.yml` y `docker-compose.override.yml` para desarrollo local
2. **Kubernetes**: Ver `k8s/` para despliegue en Kubernetes
3. **CI/CD**: Ver `.github/workflows/` para pipeline automatizado
4. **Monitoreo**: Configurar Prometheus y Grafana para métricas

---

## Referencias

- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Multi-stage Builds](https://docs.docker.com/build/building/multi-stage/)
- [Eclipse Temurin Images](https://hub.docker.com/_/eclipse-temurin)
- [Spring Boot Docker](https://spring.io/guides/topicals/spring-boot-docker/)
- [Java 21 in Containers](https://docs.oracle.com/en/java/javase/21/docs/specs/man/java.html)

---

**Última actualización**: 2026-05-04  
**Versión**: 1.0
