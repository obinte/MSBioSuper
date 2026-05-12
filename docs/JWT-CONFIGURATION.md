# Configuración JWT - MSBioSuper

## Descripción General

Este documento describe la configuración de autenticación JWT (JSON Web Token) en la aplicación MSBioSuper, incluyendo la generación de claves secretas seguras y las mejores prácticas de seguridad.

---

## 1. Propiedades de Configuración

### 1.1 Propiedades Disponibles

La aplicación utiliza las siguientes propiedades para configurar JWT:

| Propiedad | Descripción | Valor por Defecto | Variable de Entorno |
|-----------|-------------|-------------------|---------------------|
| `jwt.secret` | Clave secreta para firmar tokens JWT | `mySecretKeyForJWTTokenGenerationAndValidation2024` | `JWT_SECRET` |
| `jwt.expiration` | Tiempo de expiración del token en milisegundos | `86400000` (24 horas) | `JWT_EXPIRATION` |

### 1.2 Ubicación de Configuración

Las propiedades se definen en:
- **Desarrollo**: `src/main/resources/application.properties`
- **Producción**: Variables de entorno del sistema

---

## 2. Generación de Clave Secreta Segura

### 2.1 Requisitos de Seguridad

Para garantizar la seguridad de los tokens JWT, la clave secreta debe cumplir con los siguientes requisitos:

- **Longitud mínima**: 256 bits (32 bytes) para HMAC-SHA256
- **Longitud recomendada**: 512 bits (64 bytes) para mayor seguridad
- **Aleatoriedad**: Generada con un generador criptográficamente seguro
- **Complejidad**: Mezcla de caracteres alfanuméricos y especiales
- **Unicidad**: Diferente para cada entorno (dev, test, prod)

### 2.2 Métodos de Generación

#### Opción 1: Usando OpenSSL (Recomendado)

```bash
# Generar clave de 256 bits (32 bytes) en Base64
openssl rand -base64 32

# Generar clave de 512 bits (64 bytes) en Base64
openssl rand -base64 64

# Ejemplo de salida:
# 8xK9mP2vN5wQ7rT4yU6iO1pL3sA0dF8gH2jK5mN9qR7tV4wX6zC1bE3nM8pS0uY2
```

#### Opción 2: Usando Java

```java
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JwtSecretGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Generar clave de 256 bits
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        
        // Codificar en Base64
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("JWT Secret Key: " + encodedKey);
    }
}
```

#### Opción 3: Usando Python

```python
import secrets
import base64

# Generar 32 bytes aleatorios (256 bits)
secret_bytes = secrets.token_bytes(32)

# Codificar en Base64
secret_base64 = base64.b64encode(secret_bytes).decode('utf-8')

print(f"JWT Secret Key: {secret_base64}")
```

#### Opción 4: Usando Node.js

```javascript
const crypto = require('crypto');

// Generar 32 bytes aleatorios (256 bits)
const secret = crypto.randomBytes(32).toString('base64');

console.log(`JWT Secret Key: ${secret}`);
```

#### Opción 5: Herramienta Online (Solo para desarrollo)

⚠️ **ADVERTENCIA**: No usar para producción

- [RandomKeygen](https://randomkeygen.com/)
- [1Password Password Generator](https://1password.com/password-generator/)

---

## 3. Configuración por Entorno

### 3.1 Entorno de Desarrollo

**Archivo**: `src/main/resources/application.properties`

```properties
# Clave de desarrollo (NO USAR EN PRODUCCIÓN)
jwt.secret=mySecretKeyForJWTTokenGenerationAndValidation2024
jwt.expiration=86400000
```

### 3.2 Entorno de Pruebas

**Archivo**: `src/main/resources/application-test.properties`

```properties
# Clave de pruebas (diferente a desarrollo)
jwt.secret=${JWT_SECRET:testSecretKeyForJWTTokenGenerationAndValidation2024}
jwt.expiration=3600000
```

### 3.3 Entorno de Producción

**Variables de Entorno**:

```bash
# Linux/macOS
export JWT_SECRET="8xK9mP2vN5wQ7rT4yU6iO1pL3sA0dF8gH2jK5mN9qR7tV4wX6zC1bE3nM8pS0uY2"
export JWT_EXPIRATION="86400000"

# Windows (PowerShell)
$env:JWT_SECRET="8xK9mP2vN5wQ7rT4yU6iO1pL3sA0dF8gH2jK5mN9qR7tV4wX6zC1bE3nM8pS0uY2"
$env:JWT_EXPIRATION="86400000"

# Docker
docker run -e JWT_SECRET="..." -e JWT_EXPIRATION="86400000" msbiosuper:latest

# Kubernetes Secret
kubectl create secret generic jwt-secret \
  --from-literal=JWT_SECRET="8xK9mP2vN5wQ7rT4yU6iO1pL3sA0dF8gH2jK5mN9qR7tV4wX6zC1bE3nM8pS0uY2" \
  --from-literal=JWT_EXPIRATION="86400000"
```

---

## 4. Configuración en Docker

### 4.1 Docker Compose

**Archivo**: `docker-compose.yml`

```yaml
version: '3.8'

services:
  msbiosuper:
    image: msbiosuper:latest
    environment:
      - JWT_SECRET=${JWT_SECRET}
      - JWT_EXPIRATION=${JWT_EXPIRATION:-86400000}
    env_file:
      - .env
```

**Archivo**: `.env`

```bash
# NO COMMITEAR ESTE ARCHIVO EN GIT
JWT_SECRET=8xK9mP2vN5wQ7rT4yU6iO1pL3sA0dF8gH2jK5mN9qR7tV4wX6zC1bE3nM8pS0uY2
JWT_EXPIRATION=86400000
```

### 4.2 Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre-alpine

# Variables de entorno con valores por defecto (solo para desarrollo)
ENV JWT_SECRET="" \
    JWT_EXPIRATION="86400000"

COPY target/msbiosuper-*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---

## 5. Configuración en Kubernetes

### 5.1 Secret

**Archivo**: `k8s/jwt-secret.yaml`

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: jwt-secret
  namespace: msbiosuper
type: Opaque
stringData:
  JWT_SECRET: "8xK9mP2vN5wQ7rT4yU6iO1pL3sA0dF8gH2jK5mN9qR7tV4wX6zC1bE3nM8pS0uY2"
  JWT_EXPIRATION: "86400000"
```

### 5.2 Deployment

**Archivo**: `k8s/deployment.yaml`

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: msbiosuper
  namespace: msbiosuper
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
        env:
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: jwt-secret
              key: JWT_SECRET
        - name: JWT_EXPIRATION
          valueFrom:
            secretKeyRef:
              name: jwt-secret
              key: JWT_EXPIRATION
```

---

## 6. Mejores Prácticas de Seguridad

### 6.1 Gestión de Secretos

✅ **HACER**:
- Usar variables de entorno en producción
- Generar claves diferentes para cada entorno
- Rotar claves periódicamente (cada 90 días)
- Usar gestores de secretos (HashiCorp Vault, AWS Secrets Manager)
- Limitar acceso a las claves (principio de mínimo privilegio)
- Auditar acceso a las claves

❌ **NO HACER**:
- Hardcodear claves en el código fuente
- Commitear claves en Git
- Compartir claves por email o chat
- Usar la misma clave en todos los entornos
- Usar claves débiles o predecibles
- Loggear claves en archivos de log

### 6.2 Rotación de Claves

Para rotar la clave JWT sin interrumpir el servicio:

1. **Generar nueva clave**:
   ```bash
   openssl rand -base64 64
   ```

2. **Configurar período de transición**:
   - Mantener ambas claves activas temporalmente
   - Firmar nuevos tokens con la nueva clave
   - Validar tokens con ambas claves

3. **Implementar rotación gradual**:
   ```java
   // Ejemplo de validación con múltiples claves
   public boolean validateToken(String token) {
       try {
           // Intentar con clave nueva
           jwtParserNew.parseSignedClaims(token);
           return true;
       } catch (JwtException e) {
           try {
               // Intentar con clave antigua
               jwtParserOld.parseSignedClaims(token);
               return true;
           } catch (JwtException ex) {
               return false;
           }
       }
   }
   ```

4. **Eliminar clave antigua**:
   - Después del período de expiración de tokens antiguos
   - Monitorear logs para detectar tokens antiguos

### 6.3 Tiempo de Expiración

Recomendaciones según el tipo de aplicación:

| Tipo de Aplicación | Tiempo de Expiración Recomendado |
|-------------------|----------------------------------|
| API pública | 15 minutos - 1 hora |
| Aplicación web interna | 8 - 24 horas |
| Aplicación móvil | 7 - 30 días |
| Microservicios internos | 5 - 15 minutos |

**Configuración recomendada para MSBioSuper**:
```properties
# 1 hora para mayor seguridad
jwt.expiration=3600000
```

### 6.4 Monitoreo y Alertas

Configurar alertas para:
- Intentos de validación de tokens fallidos (posible ataque)
- Tokens expirados siendo utilizados
- Patrones anormales de generación de tokens
- Acceso no autorizado a endpoints protegidos

---

## 7. Verificación de Configuración

### 7.1 Verificar Variables de Entorno

```bash
# Linux/macOS
echo $JWT_SECRET
echo $JWT_EXPIRATION

# Windows (PowerShell)
echo $env:JWT_SECRET
echo $env:JWT_EXPIRATION
```

### 7.2 Verificar Configuración en la Aplicación

```bash
# Iniciar aplicación y verificar logs
./mvnw spring-boot:run

# Buscar en logs:
# "TokenUtil initialized with secret key length: XX bytes"
```

### 7.3 Probar Generación de Token

```bash
# Endpoint de autenticación
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# Respuesta esperada:
# {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."}
```

### 7.4 Probar Validación de Token

```bash
# Endpoint protegido
curl -X GET http://localhost:8080/api/v1/resumen-pesca \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# Respuesta esperada: 200 OK con datos
```

---

## 8. Troubleshooting

### 8.1 Error: "JWT secret key is too short"

**Causa**: La clave secreta tiene menos de 256 bits (32 bytes).

**Solución**:
```bash
# Generar nueva clave de 256 bits mínimo
openssl rand -base64 32
```

### 8.2 Error: "JWT signature does not match"

**Causa**: La clave secreta usada para firmar es diferente a la usada para validar.

**Solución**:
- Verificar que `JWT_SECRET` es la misma en todos los servicios
- Verificar que no hay espacios o caracteres especiales en la variable de entorno

### 8.3 Error: "JWT token expired"

**Causa**: El token ha superado el tiempo de expiración configurado.

**Solución**:
- Implementar mecanismo de refresh token
- Ajustar `JWT_EXPIRATION` según necesidades
- Verificar sincronización de relojes entre servidores

### 8.4 Error: "Cannot read JWT_SECRET environment variable"

**Causa**: La variable de entorno no está configurada.

**Solución**:
```bash
# Configurar variable de entorno
export JWT_SECRET="tu-clave-secreta-aqui"

# O usar valor por defecto en application.properties
jwt.secret=${JWT_SECRET:valor-por-defecto}
```

---

## 9. Referencias

### 9.1 Documentación Oficial

- [JJWT Documentation](https://github.com/jwtk/jjwt)
- [JWT.io](https://jwt.io/)
- [RFC 7519 - JSON Web Token](https://tools.ietf.org/html/rfc7519)
- [Spring Security JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)

### 9.2 Herramientas Útiles

- [JWT Debugger](https://jwt.io/#debugger)
- [JWT Inspector (Chrome Extension)](https://chrome.google.com/webstore/detail/jwt-inspector)
- [Postman JWT Authentication](https://learning.postman.com/docs/sending-requests/authorization/#jwt-bearer)

### 9.3 Seguridad

- [OWASP JWT Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/JSON_Web_Token_for_Java_Cheat_Sheet.html)
- [NIST Digital Identity Guidelines](https://pages.nist.gov/800-63-3/)

---

## 10. Checklist de Implementación

- [x] Clave secreta externalizada a `application.properties`
- [x] Soporte para variable de entorno `JWT_SECRET`
- [x] Soporte para variable de entorno `JWT_EXPIRATION`
- [x] Documentación de generación de claves seguras
- [x] Configuración por entorno (dev, test, prod)
- [x] Ejemplos de configuración en Docker
- [x] Ejemplos de configuración en Kubernetes
- [x] Mejores prácticas de seguridad documentadas
- [x] Guía de troubleshooting
- [ ] Implementar rotación de claves (opcional)
- [ ] Integrar con gestor de secretos (opcional)

---

**Última actualización**: 2026-04-23  
**Versión del documento**: 1.0  
**Autor**: Josias Rubén Aldaz Arcalles
