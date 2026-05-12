# Workflows de CI/CD - MSBioSuper

Este directorio contiene los workflows de GitHub Actions para el proyecto MSBioSuper Java 21 Migration.

## 📋 Workflows Disponibles

### 🔄 ci.yml - Pipeline Principal de CI/CD

Workflow completo que incluye:

- **🔨 Build**: Compilación y empaquetado con Maven
- **🧪 Test**: Pruebas unitarias e integración con PostgreSQL
- **🔍 Quality**: Análisis de calidad de código (SpotBugs, PMD, Checkstyle, SonarCloud)
- **🛡️ Security**: Escaneo de seguridad (Trivy, OWASP, GitLeaks)
- **🐳 Docker**: Build de imagen Docker multi-arquitectura
- **🚀 Deploy**: Despliegue automático (staging/production)

#### Triggers

- **Push** a ramas: `main`, `develop`, `feature/**`, `hotfix/**`, `release/**`
- **Pull Request** hacia: `main`, `develop`
- **Manual** con parámetros opcionales

#### Variables de Entorno Requeridas

```bash
# Configuración básica (automática)
JAVA_VERSION=21
MAVEN_OPTS=-Xmx1024m -XX:+UseZGC
SPRING_PROFILES_ACTIVE=test

# Base de datos para pruebas (automática)
DB_HOST=localhost
DB_PORT=5432
DB_NAME=msbiosuper_test
DB_USER=test_user
DB_PASSWORD=test_password
```

#### Secrets Opcionales

```bash
# Para análisis de calidad
SONAR_TOKEN=<token-sonarcloud>

# Para escaneo de seguridad
SNYK_TOKEN=<token-snyk>
GITLEAKS_LICENSE=<licencia-gitleaks>

# Para notificaciones
SLACK_WEBHOOK_URL=<webhook-slack>
```

## 🔧 Configuración

### Archivos de Configuración

- **`ci-config.yml`**: Configuraciones adicionales y variables por entorno
- **`owasp-suppressions.xml`**: Supresiones para falsos positivos de OWASP
- **`performance-tests.yml.example`**: Ejemplo de workflow para pruebas de rendimiento

### Personalización

#### Habilitar SonarCloud

1. Crear proyecto en [SonarCloud](https://sonarcloud.io)
2. Agregar `SONAR_TOKEN` a los secrets del repositorio
3. Actualizar configuración en `ci-config.yml`:

```yaml
quality_tools:
  sonarcloud:
    enabled: true
    project_key: "tu-proyecto-key"
    organization: "tu-organizacion"
```

#### Configurar Notificaciones Slack

1. Crear webhook en Slack
2. Agregar `SLACK_WEBHOOK_URL` a los secrets
3. Habilitar en `ci-config.yml`:

```yaml
notifications:
  slack:
    enabled: true
    channels:
      success: "#deployments"
      failure: "#alerts"
```

## 🚀 Uso

### Ejecución Automática

El workflow se ejecuta automáticamente en:
- Push a ramas principales
- Pull requests
- Merge a main/develop

### Ejecución Manual

```bash
# Desde GitHub UI: Actions > CI/CD Pipeline > Run workflow
# O usando GitHub CLI:
gh workflow run ci.yml \
  --field skip_tests=false \
  --field environment=staging
```

### Parámetros de Ejecución Manual

- **`skip_tests`**: Omitir pruebas (solo para desarrollo)
  - `false` (default): Ejecutar todas las pruebas
  - `true`: Omitir pruebas (solo build)

- **`environment`**: Entorno de despliegue
  - `staging` (default): Desplegar a staging
  - `production`: Desplegar a producción

## 📊 Artefactos Generados

### Build Artifacts
- **JAR**: `msbiosuper-{version}.jar`
- **Logs**: Logs de compilación Maven

### Test Reports
- **JUnit**: Reportes XML de pruebas unitarias
- **JaCoCo**: Reportes de cobertura de código
- **Failsafe**: Reportes de pruebas de integración

### Quality Reports
- **SpotBugs**: Análisis de bugs potenciales
- **PMD**: Análisis de código estático
- **Checkstyle**: Verificación de estilo de código
- **OWASP**: Reporte de vulnerabilidades de dependencias

### Security Reports
- **Trivy**: Escaneo de vulnerabilidades (filesystem + Docker)
- **GitLeaks**: Detección de secretos
- **SBOM**: Software Bill of Materials

### Docker Artifacts
- **Imagen**: Multi-arquitectura (amd64, arm64)
- **Tags**: `latest`, `{version}`, `{branch}-{sha}`
- **SBOM**: Lista de componentes de software

## 🔍 Monitoreo y Debugging

### Ver Estado del Workflow

```bash
# Listar workflows
gh workflow list

# Ver ejecuciones recientes
gh run list --workflow=ci.yml

# Ver detalles de una ejecución
gh run view <run-id>

# Ver logs de un job específico
gh run view <run-id> --log --job=build
```

### Debugging Común

#### ❌ Fallo en Compilación
```bash
# Verificar logs del job build
gh run view <run-id> --log --job=build

# Verificar dependencias Maven
mvn dependency:tree
```

#### ❌ Fallo en Pruebas
```bash
# Verificar logs del job test
gh run view <run-id> --log --job=test

# Ejecutar pruebas localmente
mvn clean test -P test
```

#### ❌ Fallo en Docker Build
```bash
# Verificar logs del job docker
gh run view <run-id> --log --job=docker

# Probar build local
docker build -t msbiosuper:test .
```

## 📈 Métricas y Reportes

### Cobertura de Código
- **Objetivo**: 80% mínimo
- **Herramienta**: JaCoCo
- **Reporte**: `target/site/jacoco/index.html`

### Calidad de Código
- **SpotBugs**: 0 bugs críticos
- **PMD**: 0 violaciones críticas
- **Checkstyle**: 0 errores de estilo

### Seguridad
- **OWASP**: CVSS < 7.0
- **Trivy**: 0 vulnerabilidades HIGH/CRITICAL
- **GitLeaks**: 0 secretos detectados

## 🔄 Flujo de Trabajo Recomendado

### Para Desarrollo
1. Crear feature branch: `git checkout -b feature/nueva-funcionalidad`
2. Desarrollar y hacer commits
3. Push: `git push origin feature/nueva-funcionalidad`
4. Crear Pull Request hacia `develop`
5. El workflow ejecuta automáticamente todos los checks
6. Merge después de aprobación y checks exitosos

### Para Release
1. Crear release branch: `git checkout -b release/v1.1.0`
2. Actualizar versión en `pom.xml`
3. Push y crear PR hacia `main`
4. El workflow ejecuta build completo + deploy a staging
5. Validar en staging
6. Merge a `main` → deploy automático a producción

## 🛠️ Mantenimiento

### Actualizar Dependencias del Workflow

```yaml
# Actualizar actions en ci.yml
- uses: actions/checkout@v4        # → v5
- uses: actions/setup-java@v4      # → v5
- uses: docker/build-push-action@v5 # → v6
```

### Agregar Nuevos Checks

1. Editar `ci.yml`
2. Agregar nuevo step en el job correspondiente
3. Actualizar configuración en `ci-config.yml`
4. Probar en feature branch

### Optimizar Performance

- **Cache**: Verificar que los caches de Maven y Docker estén funcionando
- **Paralelización**: Los jobs independientes se ejecutan en paralelo
- **Timeouts**: Ajustar timeouts en `ci-config.yml` según necesidad

## 📞 Soporte

Para problemas con el workflow:

1. **Revisar logs** en GitHub Actions
2. **Ejecutar localmente** los comandos que fallan
3. **Consultar documentación** de las actions utilizadas
4. **Crear issue** en el repositorio con logs y contexto

---

**Autor**: Josias Rubén Aldaz Arcalles  
**Fecha**: 2026-04-24  
**Versión**: 1.0