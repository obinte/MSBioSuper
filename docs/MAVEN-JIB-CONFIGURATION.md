# Configuración Maven y Jib para MSBioSuper

## Resumen

Este documento explica la configuración de Maven con el plugin Jib para builds Docker optimizados y los diferentes perfiles disponibles para distintos entornos.

## Plugin Jib Maven

### ¿Qué es Jib?

Jib es una herramienta de Google que permite construir imágenes Docker optimizadas para aplicaciones Java sin necesidad de un Dockerfile. Ofrece las siguientes ventajas:

- **Sin Docker daemon**: No requiere Docker instalado para construir imágenes
- **Builds reproducibles**: Genera imágenes consistentes
- **Optimización de capas**: Separa automáticamente dependencias, recursos y clases
- **Builds rápidos**: Aprovecha el caché de capas de manera inteligente
- **Seguridad**: Usa imágenes base distroless por defecto

### Configuración Implementada

#### Imagen Base
```xml
<from>
    <image>eclipse-temurin:21-jre-alpine</image>
</from>
```
- Misma imagen base que el Dockerfile para consistencia
- Soporte multi-arquitectura (amd64, arm64)

#### Configuración del Contenedor
- **Usuario no-root**: 1001:1001 para seguridad
- **Puertos expuestos**: 8080 (aplicación), 8081 (actuator)
- **Variables de entorno**: Configuradas para producción
- **Directorio de trabajo**: /app
- **Formato**: OCI (Open Container Initiative)

## Perfiles Maven

### Perfiles de Empaquetado

#### 1. Perfil JAR (por defecto)
```bash
mvn clean package
```
- Empaquetado JAR para Docker y ejecución standalone
- Tomcat embebido incluido
- Activado por defecto

#### 2. Perfil WAR
```bash
mvn clean package -P war
```
- Empaquetado WAR para Tomcat externo
- Tomcat con scope 'provided'
- Para despliegues tradicionales

### Perfiles de Entorno

#### 3. Perfil DESARROLLO (dev)
```bash
mvn spring-boot:run -P dev
mvn jib:dockerBuild -P dev,jib-local
```

**Características:**
- Spring profile: `dev`
- DevTools habilitado para hot reload
- Memoria JVM reducida: 128-256MB
- Tags Docker: `dev`, `dev-{version}`

**Variables de entorno Jib:**
- `SPRING_PROFILES_ACTIVE=dev`
- `JAVA_OPTS=-Xms128m -Xmx256m -XX:+UseZGC`

#### 4. Perfil TEST
```bash
mvn clean test -P test
mvn clean verify -P test
```

**Características:**
- Spring profile: `test`
- Todas las pruebas habilitadas
- Pruebas unitarias (Surefire) e integración (Failsafe)
- Memoria JVM: 128-256MB
- Tags Docker: `test`, `test-{version}`

**Plugins adicionales:**
- Maven Surefire: Pruebas unitarias
- Maven Failsafe: Pruebas de integración

#### 5. Perfil PRODUCCIÓN (prod)
```bash
mvn clean package -P prod
mvn jib:build -P prod,jib
```

**Características:**
- Spring profile: `prod`
- Pruebas omitidas para builds rápidos
- Memoria JVM optimizada: 512MB-1GB
- ZGC habilitado para mejor rendimiento
- Tags Docker: `prod`, `prod-{version}`, `latest`

**Optimizaciones:**
- Executable JAR habilitado
- Layers habilitadas para mejor caché
- JVM arguments optimizados para producción

### Perfiles de Jib

#### 6. Perfil JIB
```bash
mvn jib:build -P jib
mvn clean package -P jib
```
- Build y push a registro remoto
- Tags: `jib-latest`, `jib-{version}`
- Ejecuta automáticamente en fase `package`

#### 7. Perfil JIB-LOCAL
```bash
mvn jib:dockerBuild -P jib-local
mvn clean package -P jib-local
```
- Build local al daemon Docker
- Tags: `local-latest`, `local-{version}`
- Para desarrollo y testing local

## Comandos de Uso

### Builds Tradicionales con Dockerfile

```bash
# Build local con Docker
docker build -t msbiosuper:latest .

# Build con Docker Compose
docker-compose build
```

### Builds con Jib

#### Desarrollo Local
```bash
# Build imagen local con Jib
mvn jib:dockerBuild -P dev,jib-local

# Build y ejecutar con perfil dev
mvn jib:dockerBuild -P dev,jib-local
docker run -p 8080:8080 -p 8081:8081 msbiosuper-local:local-latest
```

#### Testing
```bash
# Build imagen para testing
mvn clean verify -P test
mvn jib:dockerBuild -P test,jib-local

# Ejecutar pruebas en contenedor
docker run --rm msbiosuper-local:test-latest
```

#### Producción
```bash
# Build y push a registro (requiere configuración de registro)
mvn jib:build -P prod,jib -Djib.to.image=registry.acosux.com/msbiosuper

# Build local para producción
mvn jib:dockerBuild -P prod,jib-local
```

### Combinación de Perfiles

```bash
# Desarrollo con Jib local
mvn clean package -P dev,jib-local

# Testing completo con Jib
mvn clean verify -P test,jib-local

# Producción con Jib remoto
mvn clean package -P prod,jib -Djib.to.image=myregistry/msbiosuper
```

## Configuración de Registro Docker

### Variables de Sistema
```bash
# Configurar imagen destino
mvn jib:build -Djib.to.image=registry.acosux.com/msbiosuper:latest

# Configurar credenciales
mvn jib:build -Djib.to.auth.username=myuser -Djib.to.auth.password=mypass

# Usar archivo de credenciales
mvn jib:build -Djib.to.credHelper=docker-credential-helper
```

### Configuración en pom.xml
```xml
<configuration>
    <to>
        <image>registry.acosux.com/msbiosuper</image>
        <auth>
            <username>${docker.registry.username}</username>
            <password>${docker.registry.password}</password>
        </auth>
    </to>
</configuration>
```

### Variables de Entorno
```bash
export DOCKER_REGISTRY_USERNAME=myuser
export DOCKER_REGISTRY_PASSWORD=mypass
mvn jib:build -P prod,jib
```

## Comparación: Dockerfile vs Jib

| Aspecto | Dockerfile | Jib |
|---------|------------|-----|
| **Requisitos** | Docker daemon | Solo Maven/Java |
| **Velocidad** | Moderada | Rápida (caché inteligente) |
| **Reproducibilidad** | Depende del entorno | Alta |
| **Optimización de capas** | Manual | Automática |
| **Seguridad** | Configuración manual | Mejores prácticas por defecto |
| **Flexibilidad** | Alta | Moderada |
| **Curva de aprendizaje** | Docker + Dockerfile | Maven + configuración XML |

## Optimizaciones Aplicadas

### 1. Separación de Capas
Jib separa automáticamente:
- **Capa de dependencias**: JAR de librerías (cambia poco)
- **Capa de recursos**: application.properties, etc.
- **Capa de clases**: Código compilado de la aplicación

### 2. Caché Inteligente
- Solo reconstruye capas que cambiaron
- Aprovecha caché de registro Docker
- Builds incrementales muy rápidos

### 3. Configuración por Entorno
- Variables de entorno específicas por perfil
- Configuración de memoria adaptada al entorno
- Tags descriptivos para identificación

### 4. Seguridad
- Usuario no-root por defecto
- Imagen base Alpine (superficie de ataque mínima)
- Sin archivos innecesarios en la imagen

## Troubleshooting

### Problemas Comunes

#### Error: "Cannot connect to Docker daemon"
```bash
# Usar dockerBuild en lugar de build para daemon local
mvn jib:dockerBuild -P jib-local
```

#### Error: "Unauthorized" al hacer push
```bash
# Verificar credenciales
docker login registry.acosux.com

# O usar credenciales en comando
mvn jib:build -Djib.to.auth.username=user -Djib.to.auth.password=pass
```

#### Imagen muy grande
```bash
# Verificar capas
docker history msbiosuper:latest

# Usar perfil prod para optimizaciones
mvn jib:build -P prod,jib
```

### Debug de Configuración

```bash
# Ver configuración efectiva de Jib
mvn jib:_skaffold-files-v2 -P jib

# Verbose output
mvn jib:build -P jib -X

# Dry run (no build)
mvn jib:build -P jib -Djib.serialize=true
```

## Scripts de Automatización

### Build Script
```bash
#!/bin/bash
# scripts/build-jib.sh

PROFILE=${1:-dev}
IMAGE_NAME=${2:-msbiosuper}

echo "Building with profile: $PROFILE"

case $PROFILE in
    dev)
        mvn clean package -P dev,jib-local
        ;;
    test)
        mvn clean verify -P test,jib-local
        ;;
    prod)
        mvn clean package -P prod,jib -Djib.to.image=$IMAGE_NAME
        ;;
    *)
        echo "Usage: $0 {dev|test|prod} [image-name]"
        exit 1
        ;;
esac
```

### Deployment Script
```bash
#!/bin/bash
# scripts/deploy-jib.sh

ENVIRONMENT=${1:-dev}
VERSION=${2:-latest}

# Build imagen
./scripts/build-jib.sh $ENVIRONMENT

# Deploy según entorno
case $ENVIRONMENT in
    dev)
        docker run -d --name msbiosuper-dev -p 8080:8080 msbiosuper-local:local-latest
        ;;
    prod)
        kubectl set image deployment/msbiosuper msbiosuper=registry.acosux.com/msbiosuper:$VERSION
        ;;
esac
```

## Mejores Prácticas

### 1. Versionado
- Usar tags semánticos: `v1.0.0`, `v1.0.0-rc1`
- Incluir información de build: `v1.0.0-build123`
- Mantener tag `latest` solo para producción estable

### 2. Seguridad
- Escanear imágenes regularmente: `trivy image msbiosuper:latest`
- Usar imágenes base actualizadas
- No incluir secretos en la imagen

### 3. Performance
- Aprovechar caché de capas
- Minimizar cambios en dependencias
- Usar perfiles específicos por entorno

### 4. CI/CD Integration
```yaml
# .github/workflows/build.yml
- name: Build with Jib
  run: mvn jib:build -P prod,jib -Djib.to.image=${{ env.REGISTRY }}/msbiosuper:${{ github.sha }}
```

---

## Referencias

- [Jib Maven Plugin Documentation](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin)
- [Maven Profiles Guide](https://maven.apache.org/guides/introduction/introduction-to-profiles.html)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)

---

**Documento creado**: 2026-05-04  
**Autor**: Josias Rubén Aldaz Arcalles  
**Versión**: 1.0