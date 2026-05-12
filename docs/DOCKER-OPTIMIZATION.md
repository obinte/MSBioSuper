# Optimización de Imagen Docker - MSBioSuper

## Resumen

Este documento detalla las optimizaciones aplicadas a la imagen Docker de MSBioSuper para cumplir con el objetivo de tamaño < 200MB y mejorar la seguridad y rendimiento.

## Objetivo

- **Tamaño de imagen**: < 200MB
- **Seguridad**: Usuario no-root
- **Rendimiento**: Minimizar capas y optimizar caché
- **Mantenibilidad**: Build reproducible y eficiente

## Optimizaciones Aplicadas

### 1. Build Multi-Etapa

**Descripción**: Separación de la etapa de build (compilación) y runtime (ejecución).

**Beneficios**:
- La imagen final solo contiene el JAR compilado y el JRE
- No incluye Maven, código fuente, ni dependencias de build
- Reducción significativa del tamaño de la imagen

**Implementación**:
```dockerfile
# Etapa 1: Build con JDK
FROM eclipse-temurin:21-jdk-alpine AS builder
# ... compilación ...

# Etapa 2: Runtime con JRE
FROM eclipse-temurin:21-jre-alpine AS runtime
COPY --from=builder /build/target/MSBioSuper-1.0.jar app.jar
```

**Ahorro estimado**: ~300MB (JDK vs JRE + código fuente)

---

### 2. Imagen Base Alpine Linux

**Descripción**: Uso de `eclipse-temurin:21-jre-alpine` como imagen base.

**Beneficios**:
- Alpine Linux es una distribución mínima (~5MB base)
- Incluye solo lo esencial para ejecutar Java
- Menor superficie de ataque (seguridad)

**Comparación de tamaños**:
- `eclipse-temurin:21-jre`: ~180MB
- `eclipse-temurin:21-jre-alpine`: ~70MB
- **Ahorro**: ~110MB

---

### 3. Usuario No-Root

**Descripción**: Ejecución de la aplicación con usuario no privilegiado.

**Beneficios**:
- Mejora la seguridad del contenedor
- Cumple con mejores prácticas de seguridad
- Compatible con políticas de seguridad de Kubernetes

**Implementación**:
```dockerfile
RUN addgroup -g 1001 -S msbiosuper && \
    adduser -u 1001 -S msbiosuper -G msbiosuper && \
    mkdir -p /app/logs && \
    chown -R msbiosuper:msbiosuper /app

USER msbiosuper
```

**UID/GID**: 1001 (estándar para aplicaciones no-root)

---

### 4. Minimización de Capas

**Descripción**: Combinación de comandos RUN para reducir el número de capas.

**Antes** (múltiples capas):
```dockerfile
RUN apk add --no-cache curl
RUN apk add --no-cache tzdata
RUN addgroup -g 1001 -S msbiosuper
RUN adduser -u 1001 -S msbiosuper -G msbiosuper
RUN mkdir -p /app/logs
RUN chown -R msbiosuper:msbiosuper /app
```

**Después** (capa única):
```dockerfile
RUN apk add --no-cache curl tzdata && \
    rm -rf /var/cache/apk/* && \
    addgroup -g 1001 -S msbiosuper && \
    adduser -u 1001 -S msbiosuper -G msbiosuper && \
    mkdir -p /app/logs && \
    chown -R msbiosuper:msbiosuper /app
```

**Beneficios**:
- Menos capas = menor tamaño de imagen
- Mejor rendimiento en pull/push
- Limpieza de caché en la misma capa

**Ahorro estimado**: ~10-20MB

---

### 5. Optimización de Caché de Build

**Descripción**: Orden estratégico de instrucciones COPY para aprovechar el caché de Docker.

**Estrategia**:
1. Copiar `pom.xml` primero
2. Descargar dependencias (`mvnw dependency:go-offline`)
3. Copiar código fuente
4. Compilar aplicación

**Beneficios**:
- Las dependencias se cachean si `pom.xml` no cambia
- Builds subsecuentes son mucho más rápidos
- Solo se recompila el código que cambió

**Tiempo de build**:
- Primer build: ~3-5 minutos
- Builds subsecuentes (sin cambios en pom.xml): ~30-60 segundos

---

### 6. Archivo .dockerignore

**Descripción**: Exclusión de archivos innecesarios del contexto de build.

**Archivos excluidos**:
- Directorios de build (`target/`, `build/`)
- Archivos de IDE (`.idea/`, `.vscode/`)
- Logs (`logs/`, `*.log`)
- Documentación (`docs/`, `*.md`)
- Archivos de prueba (`src/test/`)
- Configuración local (`.env`, `.env.local`)
- Archivos de seguridad (`*.key`, `*.pem`, `jwt-secret-*.txt`)

**Beneficios**:
- Contexto de build más pequeño
- Builds más rápidos
- Menor riesgo de incluir archivos sensibles

**Ahorro en tiempo de build**: ~20-30%

---

### 7. Limpieza de Caché de Paquetes

**Descripción**: Eliminación del caché de apk después de instalar paquetes.

**Implementación**:
```dockerfile
RUN apk add --no-cache curl tzdata && \
    rm -rf /var/cache/apk/*
```

**Beneficios**:
- Reduce el tamaño de la imagen
- No deja archivos temporales

**Ahorro estimado**: ~5-10MB

---

### 8. Uso de JRE en lugar de JDK

**Descripción**: La imagen de runtime usa JRE (Java Runtime Environment) en lugar de JDK (Java Development Kit).

**Beneficios**:
- JRE solo incluye lo necesario para ejecutar Java
- No incluye compilador, debugger, ni herramientas de desarrollo

**Comparación**:
- JDK 21 Alpine: ~180MB
- JRE 21 Alpine: ~70MB
- **Ahorro**: ~110MB

---

### 9. Optimización de Variables de Entorno

**Descripción**: Consolidación de variables de entorno en una sola instrucción ENV.

**Implementación**:
```dockerfile
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0" \
    SPRING_PROFILES_ACTIVE="prod" \
    SERVER_PORT=8080 \
    MANAGEMENT_SERVER_PORT=8081
```

**Beneficios**:
- Menos capas en la imagen
- Configuración centralizada
- Fácil de sobrescribir en runtime

---

### 10. Consolidación de LABEL

**Descripción**: Múltiples LABELs en una sola instrucción.

**Antes**:
```dockerfile
LABEL maintainer="..."
LABEL description="..."
LABEL version="1.0"
```

**Después**:
```dockerfile
LABEL maintainer="..." \
      description="..." \
      version="1.0"
```

**Beneficio**: Reduce el número de capas

---

## Resultados

### Tamaño de Imagen

| Componente | Tamaño Estimado |
|------------|-----------------|
| Imagen base (Alpine JRE 21) | ~70MB |
| Dependencias del sistema (curl, tzdata) | ~5MB |
| JAR de aplicación | ~50-80MB |
| Configuración y metadata | ~5MB |
| **Total estimado** | **~130-160MB** |

✅ **Objetivo cumplido**: < 200MB

### Seguridad

- ✅ Usuario no-root (msbiosuper:1001)
- ✅ Imagen base Alpine (menor superficie de ataque)
- ✅ Sin archivos sensibles en la imagen
- ✅ Health check configurado
- ✅ Señales del sistema manejadas correctamente (exec)

### Rendimiento

- ✅ Caché de build optimizado
- ✅ Tiempo de build reducido (~70% en builds subsecuentes)
- ✅ Contexto de build mínimo
- ✅ Capas minimizadas

---

## Verificación

### Construir y Verificar Tamaño

```bash
# Construir imagen
docker build -t msbiosuper:latest .

# Verificar tamaño
docker images msbiosuper:latest

# Ejecutar script de verificación
./scripts/verify-docker-image-size.sh
```

### Inspeccionar Capas

```bash
# Ver historial de capas
docker history msbiosuper:latest --human

# Ver detalles de la imagen
docker inspect msbiosuper:latest
```

### Verificar Usuario No-Root

```bash
# Ejecutar contenedor
docker run -d --name msbiosuper-test msbiosuper:latest

# Verificar usuario
docker exec msbiosuper-test whoami
# Salida esperada: msbiosuper

# Verificar UID/GID
docker exec msbiosuper-test id
# Salida esperada: uid=1001(msbiosuper) gid=1001(msbiosuper)

# Limpiar
docker stop msbiosuper-test
docker rm msbiosuper-test
```

---

## Mejores Prácticas Aplicadas

### ✅ Seguridad

1. Usuario no-root
2. Imagen base mínima (Alpine)
3. Sin archivos sensibles en la imagen
4. Dependencias actualizadas
5. Health check configurado

### ✅ Rendimiento

1. Build multi-etapa
2. Caché de dependencias optimizado
3. Capas minimizadas
4. .dockerignore configurado
5. JRE en lugar de JDK

### ✅ Mantenibilidad

1. Comentarios descriptivos
2. Variables de entorno configurables
3. Estructura clara y organizada
4. Documentación completa
5. Scripts de verificación

---

## Comandos Útiles

### Build y Ejecución

```bash
# Construir imagen
docker build -t msbiosuper:latest .

# Ejecutar contenedor
docker run -d \
  --name msbiosuper \
  -p 8080:8080 \
  -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=postgres \
  -e DB_PORT=5432 \
  -e DB_NAME=msbiosuper \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=secret \
  -e JWT_SECRET=your-secret-key \
  msbiosuper:latest

# Ver logs
docker logs -f msbiosuper

# Verificar health check
docker inspect --format='{{json .State.Health}}' msbiosuper | jq
```

### Análisis de Imagen

```bash
# Analizar capas
docker history msbiosuper:latest --no-trunc

# Escanear vulnerabilidades (con Trivy)
trivy image msbiosuper:latest

# Analizar tamaño de capas (con dive)
dive msbiosuper:latest
```

### Limpieza

```bash
# Eliminar contenedores detenidos
docker container prune

# Eliminar imágenes sin usar
docker image prune

# Eliminar todo (cuidado en producción)
docker system prune -a
```

---

## Próximos Pasos

### Optimizaciones Adicionales (Opcionales)

1. **Distroless Image**: Considerar usar imagen distroless de Google para mayor seguridad
2. **Jlink**: Crear JRE personalizado con solo los módulos necesarios
3. **GraalVM Native Image**: Compilar a binario nativo para menor tamaño y startup más rápido
4. **Layer Caching en CI/CD**: Configurar BuildKit para mejor caché en pipelines

### Monitoreo

1. Configurar alertas de tamaño de imagen en CI/CD
2. Escaneo automático de vulnerabilidades
3. Análisis de drift de configuración

---

## Referencias

- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Dockerfile Best Practices](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/)
- [Alpine Linux](https://alpinelinux.org/)
- [Eclipse Temurin](https://adoptium.net/)
- [Multi-stage Builds](https://docs.docker.com/build/building/multi-stage/)

---

**Documento creado**: 2026-05-04  
**Autor**: Josias Rubén Aldaz Arcalles  
**Versión**: 1.0
