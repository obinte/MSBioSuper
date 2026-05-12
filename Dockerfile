# ============================================================================
# Dockerfile Multi-Etapa para MSBioSuper
# ============================================================================
# Este Dockerfile implementa un build multi-etapa para optimizar el tamaño
# de la imagen final y mejorar la seguridad.
#
# Etapa 1: Build - Compila la aplicación con Maven
# Etapa 2: Runtime - Ejecuta la aplicación con JRE mínimo
# ============================================================================

# ============================================================================
# ETAPA 1: BUILD
# ============================================================================
# Usa Maven con JDK 21 para compilar la aplicación
FROM eclipse-temurin:21-jdk-alpine AS builder

# Metadata de la imagen
LABEL stage=builder
LABEL description="Etapa de construcción para MSBioSuper"

# Establecer directorio de trabajo
WORKDIR /build

# Copiar archivos de configuración de Maven primero (para aprovechar caché de Docker)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Hacer ejecutable el Maven Wrapper y descargar dependencias
# Esta capa se cachea si pom.xml no cambia, optimizando builds subsecuentes
RUN chmod +x ./mvnw && \
    ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación en una sola capa RUN para minimizar capas
# - clean package: Limpia y empaqueta
# - -DskipTests: Omite pruebas (ya ejecutadas en CI/CD)
# - -B: Modo batch sin output interactivo
RUN ./mvnw clean package -DskipTests -B && \
    ls -lh /build/target/*.jar

# ============================================================================
# ETAPA 2: RUNTIME
# ============================================================================
# Usa JRE 21 Alpine para una imagen mínima y segura
FROM eclipse-temurin:21-jre-alpine AS runtime

# Metadata de la imagen
LABEL maintainer="Josias Rubén Aldaz Arcalles" \
      description="MSBioSuper Microservice - Java 21 Migration" \
      version="1.0"

# Instalar dependencias del sistema en una sola capa para minimizar tamaño
# - curl: Para health checks
# - tzdata: Para configuración de zona horaria
# - Limpiar caché de apk para reducir tamaño
RUN apk add --no-cache curl tzdata && \
    rm -rf /var/cache/apk/*

# Configurar zona horaria (ajustar según necesidad)
ENV TZ=America/Guayaquil

# Crear usuario no-root para seguridad en una sola capa RUN
# - Evita ejecutar la aplicación como root
# - Mejora la seguridad del contenedor
# - UID/GID 1001 para compatibilidad con Kubernetes
RUN addgroup -g 1001 -S msbiosuper && \
    adduser -u 1001 -S msbiosuper -G msbiosuper && \
    mkdir -p /app/logs && \
    chown -R msbiosuper:msbiosuper /app

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR compilado desde la etapa de build
# Usar --chown para establecer permisos en la copia (más eficiente)
COPY --from=builder --chown=msbiosuper:msbiosuper /build/target/MSBioSuper-1.0.jar app.jar

# Cambiar al usuario no-root ANTES de EXPOSE para mayor seguridad
USER msbiosuper

# Exponer puertos de la aplicación
EXPOSE 8080 8081

# Variables de entorno optimizadas para contenedores
# - Memoria ajustada para objetivo < 200MB de imagen
# - Configuración de JVM para contenedores
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0" \
    SPRING_PROFILES_ACTIVE="prod" \
    SERVER_PORT=8080 \
    MANAGEMENT_SERVER_PORT=8081

# Health check para verificar que la aplicación está funcionando
# - interval: Cada 30 segundos
# - timeout: Esperar 3 segundos por respuesta
# - start-period: Dar 60 segundos para que la app inicie
# - retries: 3 intentos antes de marcar como unhealthy
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:${MANAGEMENT_SERVER_PORT}/actuator/health/liveness || exit 1

# Comando de inicio de la aplicación
# - Usa exec para que Java reciba señales del sistema correctamente
# - Habilita Virtual Threads de Java 21
# - Usa ZGC para mejor rendimiento en contenedores
ENTRYPOINT ["sh", "-c", "exec java \
    ${JAVA_OPTS} \
    -XX:+UseZGC \
    -XX:+ZGenerational \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
    -jar app.jar"]
