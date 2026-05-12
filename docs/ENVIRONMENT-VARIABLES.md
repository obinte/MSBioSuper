# Variables de Entorno - MSBioSuper

## Descripción General

Este documento describe todas las variables de entorno requeridas y opcionales para ejecutar la aplicación MSBioSuper. Las variables de entorno permiten configurar la aplicación sin modificar el código fuente, facilitando el despliegue en diferentes entornos (desarrollo, pruebas, producción).

---

## Variables Obligatorias

Estas variables **DEBEN** estar configuradas para que la aplicación inicie correctamente.

### Base de Datos PostgreSQL

#### `JDBC_DATABASE`
- **Descripción**: Nombre de la base de datos PostgreSQL
- **Tipo**: String
- **Ejemplo**: `msbiosuper_db`
- **Requerido**: ✅ Sí
- **Valor por defecto**: Ninguno

#### `JDBC_USER`
- **Descripción**: Usuario de PostgreSQL con permisos de lectura/escritura
- **Tipo**: String
- **Ejemplo**: `msbiosuper_user`
- **Requerido**: ✅ Sí
- **Valor por defecto**: Ninguno
- **Permisos necesarios**: SELECT, INSERT, UPDATE, DELETE en todas las tablas

#### `JDBC_PASSWORD`
- **Descripción**: Contraseña del usuario de base de datos
- **Tipo**: String (sensible)
- **Ejemplo**: `P@ssw0rd!Secur3`
- **Requerido**: ✅ Sí
- **Valor por defecto**: Ninguno
- **Requisitos de seguridad**:
  - Mínimo 12 caracteres
  - Combinación de mayúsculas, minúsculas, números y símbolos
  - No usar contraseñas comunes o predecibles
  - Rotar cada 90 días en producción

### Seguridad JWT

#### `JWT_SECRET`
- **Descripción**: Clave secreta para firmar y validar tokens JWT
- **Tipo**: String (sensible, Base64)
- **Ejemplo**: `dGhpc2lzYXZlcnlzZWN1cmVzZWNyZXRrZXlmb3Jqd3R0b2tlbnM=`
- **Requerido**: ✅ Sí
- **Valor por defecto**: Ninguno
- **Requisitos**:
  - Mínimo 256 bits (32 bytes)
  - Recomendado 512 bits (64 bytes)
  - Codificada en Base64
  - Generada con método criptográficamente seguro

**Cómo generar una clave segura**:

```bash
# Opción 1: OpenSSL (Linux/Mac)
openssl rand -base64 64

# Opción 2: Python
python3 scripts/generate-jwt-secret.py

# Opción 3: Bash
bash scripts/generate-jwt-secret.sh
```

**Mejores prácticas**:
- Usar clave diferente para cada entorno (dev, test, prod)
- NUNCA commitear la clave en Git
- Rotar claves cada 90 días
- Mantener backup seguro de claves antiguas para validar tokens existentes
- Usar gestor de secretos en producción (Vault, AWS Secrets Manager, etc.)

---

## Variables Opcionales

Estas variables tienen valores por defecto y pueden ser omitidas.

### Conexión a Base de Datos

#### `JDBC_SERVER`
- **Descripción**: Hostname o IP del servidor PostgreSQL
- **Tipo**: String
- **Ejemplo**: `db.acosux.com` o `192.168.1.100`
- **Requerido**: ❌ No
- **Valor por defecto**: `localhost`

#### `JDBC_PORT`
- **Descripción**: Puerto del servidor PostgreSQL
- **Tipo**: Integer
- **Ejemplo**: `5432`
- **Requerido**: ❌ No
- **Valor por defecto**: `5432`
- **Rango válido**: 1-65535

### Seguridad JWT

#### `JWT_EXPIRATION`
- **Descripción**: Tiempo de expiración del token JWT en milisegundos
- **Tipo**: Long
- **Ejemplo**: `3600000` (1 hora)
- **Requerido**: ❌ No
- **Valor por defecto**: `86400000` (24 horas)
- **Valores comunes**:
  - `3600000` = 1 hora (recomendado para producción)
  - `86400000` = 24 horas (desarrollo)
  - `604800000` = 7 días (aplicaciones móviles)

### CORS (Cross-Origin Resource Sharing)

#### `CORS_ALLOWED_ORIGINS`
- **Descripción**: Lista de orígenes permitidos para peticiones CORS (separados por coma)
- **Tipo**: String (lista separada por comas)
- **Ejemplo**: `https://app.acosux.com,https://admin.acosux.com`
- **Requerido**: ❌ No
- **Valor por defecto**: `http://localhost:3000,http://localhost:4200`
- **Formato**: URLs completas separadas por comas, sin espacios

### Servidor

#### `SERVER_PORT`
- **Descripción**: Puerto HTTP en el que escucha la aplicación
- **Tipo**: Integer
- **Ejemplo**: `8080`
- **Requerido**: ❌ No
- **Valor por defecto**: `8080`
- **Rango válido**: 1024-65535 (puertos no privilegiados)

#### `SPRING_PROFILES_ACTIVE`
- **Descripción**: Perfil de Spring Boot activo
- **Tipo**: String
- **Ejemplo**: `prod`
- **Requerido**: ❌ No
- **Valor por defecto**: Ninguno (usa configuración base)
- **Valores válidos**:
  - `dev`: Desarrollo local con logging detallado
  - `test`: Pruebas automatizadas
  - `prod`: Producción con configuración optimizada

---

## Configuración por Entorno

### Desarrollo Local

Crear archivo `.env` en la raíz del proyecto:

```bash
# Base de datos local
JDBC_SERVER=localhost
JDBC_PORT=5432
JDBC_DATABASE=msbiosuper_dev
JDBC_USER=dev_user
JDBC_PASSWORD=dev_password

# JWT con expiración larga para desarrollo
JWT_SECRET=$(openssl rand -base64 64)
JWT_EXPIRATION=86400000

# CORS permisivo para desarrollo
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200,http://localhost:8081

# Puerto personalizado si 8080 está ocupado
SERVER_PORT=8080

# Perfil de desarrollo
SPRING_PROFILES_ACTIVE=dev
```

### Pruebas (Testing)

```bash
# Base de datos de pruebas
JDBC_SERVER=localhost
JDBC_PORT=5432
JDBC_DATABASE=msbiosuper_test
JDBC_USER=test_user
JDBC_PASSWORD=test_password

# JWT con expiración corta para pruebas
JWT_SECRET=$(openssl rand -base64 64)
JWT_EXPIRATION=3600000

# CORS restrictivo
CORS_ALLOWED_ORIGINS=http://localhost:3000

# Perfil de pruebas
SPRING_PROFILES_ACTIVE=test
```

### Producción

**⚠️ IMPORTANTE**: En producción, usar un gestor de secretos (HashiCorp Vault, AWS Secrets Manager, Azure Key Vault, etc.)

```bash
# Base de datos de producción
JDBC_SERVER=prod-db.acosux.com
JDBC_PORT=5432
JDBC_DATABASE=msbiosuper_prod
JDBC_USER=prod_user
JDBC_PASSWORD=<OBTENER_DE_GESTOR_DE_SECRETOS>

# JWT con clave rotada regularmente
JWT_SECRET=<OBTENER_DE_GESTOR_DE_SECRETOS>
JWT_EXPIRATION=3600000

# CORS restrictivo solo dominios autorizados
CORS_ALLOWED_ORIGINS=https://app.acosux.com,https://admin.acosux.com

# Puerto estándar
SERVER_PORT=8080

# Perfil de producción
SPRING_PROFILES_ACTIVE=prod
```

---

## Métodos de Configuración

### 1. Archivo .env (Desarrollo Local)

Crear archivo `.env` en la raíz del proyecto:

```bash
cp .env.example .env
# Editar .env con valores reales
```

**Ventajas**:
- Fácil de usar en desarrollo
- No requiere configuración del sistema

**Desventajas**:
- No es estándar de Spring Boot (requiere librería adicional)
- Riesgo de commitear accidentalmente

### 2. Variables de Entorno del Sistema (Recomendado)

#### Linux/Mac

```bash
# Temporal (solo sesión actual)
export JDBC_DATABASE=msbiosuper_db
export JDBC_USER=msbiosuper_user
export JDBC_PASSWORD=secure_password
export JWT_SECRET=$(openssl rand -base64 64)

# Permanente (agregar a ~/.bashrc o ~/.zshrc)
echo 'export JDBC_DATABASE=msbiosuper_db' >> ~/.bashrc
```

#### Windows

```cmd
# Temporal (solo sesión actual)
set JDBC_DATABASE=msbiosuper_db
set JDBC_USER=msbiosuper_user
set JDBC_PASSWORD=secure_password

# Permanente (variables de entorno del sistema)
setx JDBC_DATABASE "msbiosuper_db"
```

### 3. Docker / Docker Compose

```yaml
# docker-compose.yml
services:
  msbiosuper:
    image: msbiosuper:latest
    environment:
      - JDBC_SERVER=postgres
      - JDBC_PORT=5432
      - JDBC_DATABASE=msbiosuper_db
      - JDBC_USER=msbiosuper_user
      - JDBC_PASSWORD=${JDBC_PASSWORD}
      - JWT_SECRET=${JWT_SECRET}
      - JWT_EXPIRATION=3600000
      - CORS_ALLOWED_ORIGINS=https://app.acosux.com
      - SERVER_PORT=8080
      - SPRING_PROFILES_ACTIVE=prod
```

### 4. Kubernetes Secrets

```yaml
# secret.yaml
apiVersion: v1
kind: Secret
metadata:
  name: msbiosuper-secrets
type: Opaque
stringData:
  jdbc-password: secure_password
  jwt-secret: base64_encoded_secret

---
# deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: msbiosuper
spec:
  template:
    spec:
      containers:
      - name: msbiosuper
        env:
        - name: JDBC_DATABASE
          value: "msbiosuper_db"
        - name: JDBC_USER
          value: "msbiosuper_user"
        - name: JDBC_PASSWORD
          valueFrom:
            secretKeyRef:
              name: msbiosuper-secrets
              key: jdbc-password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: msbiosuper-secrets
              key: jwt-secret
```

### 5. Argumentos de Línea de Comandos

```bash
java -jar msbiosuper.jar \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/msbiosuper_db \
  --spring.datasource.username=msbiosuper_user \
  --spring.datasource.password=secure_password \
  --jwt.secret=base64_encoded_secret
```

---

## Validación de Configuración

### Verificar Variables Configuradas

```bash
# Linux/Mac
env | grep -E 'JDBC_|JWT_|CORS_|SERVER_'

# Windows
set | findstr /I "JDBC_ JWT_ CORS_ SERVER_"
```

### Probar Conexión a Base de Datos

```bash
# Usando psql
psql -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE

# Usando Docker
docker run --rm -it postgres:15 psql -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE
```

### Verificar Inicio de Aplicación

```bash
# Ejecutar aplicación y verificar logs
java -jar target/msbiosuper-1.0.0.jar

# Buscar en logs:
# ✅ "Started MSBioSuperApplication"
# ✅ "HikariPool-1 - Start completed"
# ❌ "Failed to configure a DataSource"
# ❌ "JWT secret key is not configured"
```

---

## Solución de Problemas

### Error: "Failed to configure a DataSource"

**Causa**: Variables de base de datos no configuradas

**Solución**:
```bash
# Verificar que todas las variables obligatorias estén configuradas
echo $JDBC_DATABASE
echo $JDBC_USER
echo $JDBC_PASSWORD

# Si están vacías, configurarlas
export JDBC_DATABASE=msbiosuper_db
export JDBC_USER=msbiosuper_user
export JDBC_PASSWORD=secure_password
```

### Error: "JWT secret key is not configured"

**Causa**: Variable JWT_SECRET no configurada

**Solución**:
```bash
# Generar y configurar clave JWT
export JWT_SECRET=$(openssl rand -base64 64)
```

### Error: "Connection refused" o "Connection timeout"

**Causa**: No se puede conectar a PostgreSQL

**Solución**:
```bash
# Verificar que PostgreSQL esté ejecutándose
pg_isready -h $JDBC_SERVER -p $JDBC_PORT

# Verificar conectividad de red
telnet $JDBC_SERVER $JDBC_PORT

# Verificar firewall y reglas de seguridad
```

### Error: "Authentication failed for user"

**Causa**: Credenciales incorrectas

**Solución**:
```bash
# Verificar credenciales manualmente
psql -h $JDBC_SERVER -p $JDBC_PORT -U $JDBC_USER -d $JDBC_DATABASE

# Si falla, verificar:
# 1. Usuario existe en PostgreSQL
# 2. Contraseña es correcta
# 3. Usuario tiene permisos en la base de datos
```

---

## Mejores Prácticas de Seguridad

### 1. Gestión de Secretos

✅ **HACER**:
- Usar gestores de secretos en producción (Vault, AWS Secrets Manager, etc.)
- Rotar credenciales regularmente (cada 90 días)
- Usar diferentes credenciales por entorno
- Limitar acceso a secretos (principio de mínimo privilegio)
- Auditar acceso a secretos

❌ **NO HACER**:
- Commitear secretos en Git
- Compartir secretos por email o chat
- Usar mismas credenciales en todos los entornos
- Hardcodear secretos en código
- Usar contraseñas débiles o predecibles

### 2. Permisos de Archivos

```bash
# Proteger archivo .env
chmod 600 .env

# Verificar permisos
ls -la .env
# Debe mostrar: -rw------- (solo propietario puede leer/escribir)
```

### 3. Auditoría

```bash
# Registrar quién accede a secretos
# Usar herramientas como:
# - AWS CloudTrail
# - Azure Monitor
# - Google Cloud Audit Logs
# - HashiCorp Vault Audit Logs
```

### 4. Rotación de Secretos

```bash
# Proceso de rotación de JWT_SECRET:
# 1. Generar nueva clave
NEW_JWT_SECRET=$(openssl rand -base64 64)

# 2. Configurar aplicación para aceptar ambas claves (vieja y nueva)
#    durante período de transición

# 3. Desplegar nueva versión

# 4. Esperar que todos los tokens antiguos expiren

# 5. Remover clave antigua
```

---

## Referencias

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [12-Factor App: Config](https://12factor.net/config)
- [OWASP Secrets Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html)
- [JWT Best Practices](https://tools.ietf.org/html/rfc8725)

---

**Última actualización**: 2026-04-23  
**Versión del documento**: 1.0  
**Mantenedor**: Equipo de Desarrollo MSBioSuper
