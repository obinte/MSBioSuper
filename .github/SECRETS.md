# Configuración de Secrets para CI/CD

Este documento describe los secrets necesarios para el funcionamiento completo del workflow de CI/CD.

## 🔐 Secrets Requeridos

### Secrets Obligatorios

Estos secrets son necesarios para el funcionamiento básico del workflow:

#### `GITHUB_TOKEN`
- **Descripción**: Token automático de GitHub Actions
- **Uso**: Autenticación con GitHub Container Registry, subida de reportes de seguridad
- **Configuración**: Se configura automáticamente por GitHub Actions
- **Permisos requeridos**: `contents: read`, `packages: write`, `security-events: write`

### Secrets Opcionales

Estos secrets mejoran la funcionalidad pero no son obligatorios:

#### `SONAR_TOKEN`
- **Descripción**: Token de autenticación para SonarCloud
- **Uso**: Análisis de calidad de código con SonarCloud
- **Obtención**: 
  1. Crear cuenta en [SonarCloud](https://sonarcloud.io)
  2. Crear proyecto para el repositorio
  3. Generar token en Account > Security > Generate Tokens
- **Configuración**: 
  ```bash
  # En GitHub: Settings > Secrets and variables > Actions > New repository secret
  Name: SONAR_TOKEN
  Value: <tu-token-sonarcloud>
  ```

#### `SNYK_TOKEN`
- **Descripción**: Token de autenticación para Snyk
- **Uso**: Escaneo de vulnerabilidades de dependencias
- **Obtención**:
  1. Crear cuenta en [Snyk](https://snyk.io)
  2. Ir a Account Settings > API Token
  3. Generar nuevo token
- **Configuración**:
  ```bash
  Name: SNYK_TOKEN
  Value: <tu-token-snyk>
  ```

#### `GITLEAKS_LICENSE`
- **Descripción**: Licencia para GitLeaks Pro (opcional)
- **Uso**: Funcionalidades avanzadas de detección de secretos
- **Obtención**: Comprar licencia en [GitLeaks](https://gitleaks.io)
- **Configuración**:
  ```bash
  Name: GITLEAKS_LICENSE
  Value: <tu-licencia-gitleaks>
  ```

#### `SLACK_WEBHOOK_URL`
- **Descripción**: Webhook URL para notificaciones Slack
- **Uso**: Enviar notificaciones de build a Slack
- **Obtención**:
  1. Crear Slack App en tu workspace
  2. Habilitar Incoming Webhooks
  3. Crear webhook para el canal deseado
- **Configuración**:
  ```bash
  Name: SLACK_WEBHOOK_URL
  Value: https://hooks.slack.com/services/YOUR_WORKSPACE_ID/YOUR_CHANNEL_ID/YOUR_SECRET_TOKEN
  ```

## 🔧 Configuración de Secrets

### Método 1: GitHub Web UI

1. Ir a tu repositorio en GitHub
2. Navegar a **Settings** > **Secrets and variables** > **Actions**
3. Hacer clic en **New repository secret**
4. Ingresar el nombre y valor del secret
5. Hacer clic en **Add secret**

### Método 2: GitHub CLI

```bash
# Configurar SONAR_TOKEN
gh secret set SONAR_TOKEN --body "tu-token-sonarcloud"

# Configurar SNYK_TOKEN
gh secret set SNYK_TOKEN --body "tu-token-snyk"

# Configurar SLACK_WEBHOOK_URL
gh secret set SLACK_WEBHOOK_URL --body "https://hooks.slack.com/services/YOUR_WORKSPACE/YOUR_CHANNEL/YOUR_TOKEN"
```

### Método 3: API de GitHub

```bash
# Usando curl (requiere token de GitHub con permisos de repo)
curl -X PUT \
  -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github.v3+json" \
  https://api.github.com/repos/OWNER/REPO/actions/secrets/SONAR_TOKEN \
  -d '{"encrypted_value":"encrypted-value","key_id":"key-id"}'
```

## 🔍 Verificación de Secrets

### Verificar que los secrets están configurados

```bash
# Listar secrets configurados (no muestra valores)
gh secret list
```

### Probar secrets en el workflow

El workflow incluye verificaciones condicionales:

```yaml
# SonarCloud solo se ejecuta si el token está disponible
- name: 🔍 Análisis SonarCloud
  if: ${{ env.SONAR_TOKEN != '' }}
  env:
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

## 🛡️ Mejores Prácticas de Seguridad

### Rotación de Tokens

- **Frecuencia**: Rotar tokens cada 90 días
- **Proceso**:
  1. Generar nuevo token en el servicio
  2. Actualizar secret en GitHub
  3. Verificar que el workflow funciona
  4. Revocar token anterior

### Principio de Menor Privilegio

- **SonarCloud**: Solo permisos de análisis, no administración
- **Snyk**: Solo permisos de escaneo, no modificación de políticas
- **GitHub Token**: Usar permisos mínimos necesarios

### Monitoreo de Uso

- Revisar logs de GitHub Actions regularmente
- Configurar alertas para fallos de autenticación
- Monitorear uso de tokens en los servicios externos

## 🚨 Troubleshooting

### Error: "SONAR_TOKEN not found"

```yaml
# Verificar que el secret está configurado
- name: Debug
  run: echo "SONAR_TOKEN configured: ${{ secrets.SONAR_TOKEN != '' }}"
```

### Error: "Docker login failed"

```bash
# Verificar permisos del GITHUB_TOKEN
permissions:
  contents: read
  packages: write  # Necesario para GHCR
```

### Error: "Snyk authentication failed"

1. Verificar que el token es válido en Snyk
2. Verificar que el token tiene permisos de escaneo
3. Verificar que el secret está configurado correctamente

## 📋 Checklist de Configuración

### Configuración Mínima (Funcionalidad Básica)
- [ ] `GITHUB_TOKEN` (automático)
- [ ] Permisos del workflow configurados
- [ ] Workflow file en `.github/workflows/ci.yml`

### Configuración Completa (Todas las Funcionalidades)
- [ ] `SONAR_TOKEN` configurado
- [ ] `SNYK_TOKEN` configurado
- [ ] `GITLEAKS_LICENSE` configurado (opcional)
- [ ] `SLACK_WEBHOOK_URL` configurado (opcional)
- [ ] Proyecto SonarCloud creado y configurado
- [ ] Cuenta Snyk configurada
- [ ] Webhook Slack configurado

### Verificación
- [ ] Workflow ejecuta sin errores
- [ ] Reportes de calidad se generan
- [ ] Escaneos de seguridad funcionan
- [ ] Imagen Docker se construye
- [ ] Notificaciones llegan (si configuradas)

## 📞 Soporte

Si tienes problemas configurando los secrets:

1. **Verificar documentación** del servicio específico
2. **Revisar logs** del workflow en GitHub Actions
3. **Probar tokens** manualmente con curl/CLI
4. **Consultar issues** en los repositorios de las herramientas
5. **Crear issue** en este repositorio con detalles del problema

---

**Última actualización**: 2026-04-24  
**Autor**: Josias Rubén Aldaz Arcalles