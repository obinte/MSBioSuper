# Guía de Uso - Perfiles Maven y Jib

## Resumen

Esta guía explica cómo usar los diferentes perfiles Maven configurados para MSBioSuper y cómo aprovechar el plugin Jib para builds Docker optimizados.

## Perfiles Disponibles

### 1. Perfiles de Empaquetado

#### JAR (por defecto)
```bash
# Build JAR estándar
mvn clean package

# Equivalente explícito
mvn clean package -P jar
```
- **Uso**: Docker, ejecución standalone
- **Tomcat**: Embebido
- **Activado**: Por defecto

#### WAR
```bash
# Build WAR para Tomcat externo
mvn clean package -P war
```
- **Uso**: Despliegue en Tomcat externo
- **Tomcat**: Scope 'provided'
- **Archivo**: `MSBioSuper-1.0.war`

### 2. Perfiles de Entorno

#### DESARROLLO (dev)
```bash
# Ejecutar aplicación en modo desarrollo
mvn spring-boot:run -P dev

# Build JAR con configuración de desarrollo
mvn clean package -P dev
```

**Características:**
- Spring profile: `dev`
- DevTools habilitado (hot reload)
- Configuración de memoria reducida
- Logging detallado

#### TEST
```bash
# Ejecutar todas las pruebas
mvn clean test -P test

# Ejecutar pruebas de integración
mvn clean verify -P test
```

**Características:**
- Spring profile: `test`
- Todas las pruebas habilitadas
- Surefire + Failsafe configurados
- Base de datos H2 en memoria

#### PRODUCCIÓN (prod)
```bash
# Build optimizado para producción
mvn clean package -P prod
```

**Características:**
- Spring profile: `prod`
- Pruebas omitidas (build rápido)
- Optimizaciones JVM
- Layers habilitadas

### 3. Perfiles de Jib

#### JIB (build remoto)
```bash
# Build y push a registro remoto
mvn jib:build -P jib

# Con imagen personalizada
mvn jib:build -P jib -Djib.to.image=registry.acosux.com/msbiosuper
```

#### JIB-LOCAL (build local)
```bash
# Build imagen local
mvn jib:dockerBuild -P jib-local

# Build con perfil específico
mvn jib:dockerBuild -P dev,jib-local
```

## Comandos Comunes

### Desarrollo Local

```bash
# 1. Ejecutar aplicación con hot reload
mvn spring-boot:run -P dev

# 2. Build imagen Docker local para desarrollo
mvn clean package -P dev,jib-local

# 3. Ejecutar contenedor de desarrollo
docker run -p 8080:8080 -p 8081:8081 msbiosuper-local:local-latest
```

### Testing

```bash
# 1. Ejecutar todas las pruebas
mvn clean verify -P test

# 2. Build imagen para testing
mvn clean package -P test,jib-local

# 3. Ejecutar pruebas en contenedor
docker run --rm msbiosuper-test:test-latest
```

### Producción

```bash
# 1. Build optimizado
mvn clean package -P prod

# 2. Build imagen Docker para producción
mvn jib:build -P prod,jib -Djib.to.image=myregistry/msbiosuper:v1.0.0

# 3. Deploy a Kubernetes
kubectl set image deployment/msbiosuper msbiosuper=myregistry/msbiosuper:v1.0.0
```

## Combinación de Perfiles

### Ejemplos Válidos

```bash
# Desarrollo con Jib local
mvn clean package -P dev,jib-local

# Testing completo con imagen
mvn clean verify -P test,jib-local

# Producción con push remoto
mvn clean package -P prod,jib

# WAR para desarrollo
mvn clean package -P war,dev
```

### Combinaciones No Recomendadas

```bash
# ❌ JAR y WAR al mismo tiempo
mvn clean package -P jar,war

# ❌ Múltiples entornos
mvn clean package -P dev,prod

# ❌ JIB sin perfil de entorno específico
mvn jib:build -P jib  # Mejor: -P prod,jib
```

## Scripts de Automatización

### Script de Build
```bash
# Usar script personalizado
./scripts/build-jib.sh dev
./scripts/build-jib.sh prod registry.acosux.com/msbiosuper --push
./scripts/build-jib.sh test --local
```

### Script de Comparación
```bash
# Comparar Dockerfile vs Jib
./scripts/compare-docker-builds.sh --clean --verbose
```

## Configuración de Variables

### Variables de Sistema
```bash
# Configurar imagen destino
export JIB_TO_IMAGE=registry.acosux.com/msbiosuper

# Configurar credenciales
export DOCKER_REGISTRY_USERNAME=myuser
export DOCKER_REGISTRY_PASSWORD=mypass

# Usar variables
mvn jib:build -P prod,jib
```

### Variables Maven
```bash
# Sobrescribir configuración
mvn jib:build -P jib \
  -Djib.to.image=myregistry/msbiosuper:latest \
  -Djib.to.auth.username=myuser \
  -Djib.to.auth.password=mypass
```

## Troubleshooting

### Problemas Comunes

#### Error: "Docker daemon not running"
```bash
# Solución: Usar build local en lugar de remoto
mvn jib:dockerBuild -P jib-local  # En lugar de jib:build
```

#### Error: "Unauthorized" en registry
```bash
# Solución: Configurar credenciales
docker login registry.acosux.com
# O usar variables de entorno
export DOCKER_REGISTRY_USERNAME=user
export DOCKER_REGISTRY_PASSWORD=pass
```

#### Error: "Profile not found"
```bash
# Verificar perfiles disponibles
mvn help:all-profiles

# Usar perfiles correctos
mvn clean package -P dev,jib-local  # ✅
mvn clean package -P development    # ❌
```

### Debug de Configuración

```bash
# Ver configuración efectiva
mvn help:effective-pom -P dev

# Ver propiedades activas
mvn help:evaluate -Dexpression=spring.profiles.active -P dev

# Debug de Jib
mvn jib:build -P jib -X  # Verbose output
```

## Mejores Prácticas

### 1. Desarrollo
- Usar perfil `dev` para desarrollo local
- Habilitar DevTools para hot reload
- Usar `jib-local` para testing de contenedores

### 2. CI/CD
```yaml
# GitHub Actions ejemplo
- name: Build and Test
  run: mvn clean verify -P test

- name: Build Production Image
  run: mvn jib:build -P prod,jib -Djib.to.image=${{ env.REGISTRY }}/msbiosuper:${{ github.sha }}
```

### 3. Producción
- Usar perfil `prod` para optimizaciones
- Configurar registry remoto
- Usar tags semánticos: `v1.0.0`, `latest`

### 4. Seguridad
- No incluir credenciales en pom.xml
- Usar variables de entorno o archivos de credenciales
- Escanear imágenes regularmente

## Configuración IDE

### IntelliJ IDEA
1. Ir a **Run/Debug Configurations**
2. Crear nueva configuración **Maven**
3. Configurar:
   - **Command line**: `spring-boot:run`
   - **Profiles**: `dev`
   - **Environment variables**: Según necesidad

### VS Code
```json
// .vscode/launch.json
{
    "type": "java",
    "name": "MSBioSuper Dev",
    "request": "launch",
    "mainClass": "com.acosux.MSBiosuper.MSBioSuperApplication",
    "env": {
        "SPRING_PROFILES_ACTIVE": "dev"
    }
}
```

### Eclipse
1. **Run As** → **Maven build...**
2. **Goals**: `spring-boot:run`
3. **Profiles**: `dev`

## Monitoreo y Métricas

### Health Checks
```bash
# Verificar salud de la aplicación
curl http://localhost:8081/actuator/health

# Verificar métricas
curl http://localhost:8081/actuator/metrics

# Métricas Prometheus
curl http://localhost:8081/actuator/prometheus
```

### Logs
```bash
# Ver logs del contenedor
docker logs -f msbiosuper-local

# Logs estructurados en JSON
docker logs msbiosuper-local | jq '.'
```

---

## Referencias

- [Maven Profiles Documentation](https://maven.apache.org/guides/introduction/introduction-to-profiles.html)
- [Jib Maven Plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin)
- [Spring Boot Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)

---

**Documento creado**: 2026-05-04  
**Autor**: Josias Rubén Aldaz Arcalles  
**Versión**: 1.0