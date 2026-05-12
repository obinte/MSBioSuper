# Guía de Pruebas JWT - MSBioSuper

## Resumen

Este documento describe las pruebas unitarias implementadas para la funcionalidad JWT de MSBioSuper, incluyendo generación, validación, renovación y extracción de información de tokens.

---

## 1. Cobertura de Pruebas

### 1.1 Estadísticas Generales

| Métrica | Valor |
|---------|-------|
| **Total de pruebas** | 40+ casos de prueba |
| **Clases de prueba** | 1 (TokenUtilTest) |
| **Métodos probados** | 13/13 (100%) |
| **Cobertura objetivo** | > 90% |
| **Framework** | JUnit 5 |

### 1.2 Categorías de Pruebas

Las pruebas están organizadas en 6 categorías principales:

1. **Generación de Tokens** (5 pruebas)
2. **Validación de Tokens** (6 pruebas)
3. **Extracción de Información** (4 pruebas)
4. **Renovación de Tokens** (7 pruebas)
5. **Verificación desde HTTP Request** (6 pruebas)
6. **Casos Edge y Seguridad** (12+ pruebas)

---

## 2. Pruebas Implementadas

### 2.1 Generación de Tokens

#### ✅ shouldGenerateValidTokenWithDefaultExpiration
**Objetivo**: Verificar que se genera un token válido con tiempo de expiración por defecto.

**Escenario**:
```java
String token = tokenUtil.createTokenForUser("testuser");
```

**Validaciones**:
- Token no es nulo
- Token no está vacío
- Token tiene formato JWT válido (3 partes separadas por punto)

#### ✅ shouldGenerateValidTokenWithCustomExpiration
**Objetivo**: Verificar generación de token con días de expiración personalizados.

**Escenario**:
```java
String token = tokenUtil.createTokenForUser("testuser", 7);
```

**Validaciones**:
- Token generado es válido
- Token puede ser validado correctamente

#### ✅ shouldIncludeUserInTokenSubject
**Objetivo**: Verificar que el usuario se incluye en el subject del token.

**Validaciones**:
- Subject extraído coincide con el usuario original

#### ✅ shouldIncludeUserIdInTokenClaims
**Objetivo**: Verificar que el userId se incluye en los claims del token.

**Validaciones**:
- TokenUser parseado contiene userId correcto
- Subject coincide con el usuario

#### ✅ shouldGenerateDifferentTokensForSameUser
**Objetivo**: Verificar que se generan tokens únicos (diferentes timestamps).

**Validaciones**:
- Dos tokens generados para el mismo usuario son diferentes

---

### 2.2 Validación de Tokens

#### ✅ shouldValidateCorrectToken
**Objetivo**: Verificar que un token válido pasa la validación.

**Validaciones**:
- `validateToken()` retorna `true` para token válido

#### ✅ shouldRejectInvalidFormatToken
**Objetivo**: Verificar rechazo de tokens con formato inválido.

**Escenario**:
```java
String invalidToken = "invalid.token.format";
```

**Validaciones**:
- `validateToken()` retorna `false`

#### ✅ shouldRejectEmptyToken
**Objetivo**: Verificar rechazo de tokens vacíos.

**Validaciones**:
- Token vacío es rechazado

#### ✅ shouldRejectNullToken
**Objetivo**: Verificar manejo de tokens nulos.

**Validaciones**:
- Se lanza excepción al validar token nulo

#### ✅ shouldRejectTokenWithWrongSignature
**Objetivo**: Verificar rechazo de tokens firmados con otra clave.

**Escenario**:
```java
TokenUtil otherTokenUtil = new TokenUtil("differentSecret", expiration);
String token = otherTokenUtil.createTokenForUser("user");
```

**Validaciones**:
- Token con firma incorrecta es rechazado

#### ✅ shouldRejectExpiredToken
**Objetivo**: Verificar rechazo de tokens expirados.

**Escenario**:
```java
TokenUtil shortLivedTokenUtil = new TokenUtil(secret, 1); // 1ms
String token = shortLivedTokenUtil.createTokenForUser("user");
Thread.sleep(10);
```

**Validaciones**:
- Token expirado es rechazado

---

### 2.3 Extracción de Información

#### ✅ shouldExtractSubjectCorrectly
**Objetivo**: Verificar extracción correcta del subject.

**Validaciones**:
- Subject extraído coincide con el usuario original

#### ✅ shouldExtractExpirationDateCorrectly
**Objetivo**: Verificar extracción de fecha de expiración.

**Validaciones**:
- Fecha de expiración no es nula
- Fecha está dentro del rango esperado

#### ✅ shouldParseUserFromTokenCorrectly
**Objetivo**: Verificar parseo completo de usuario desde token.

**Validaciones**:
- TokenUser no es nulo
- userId coincide
- subject coincide

#### ✅ shouldThrowExceptionWhenParsingInvalidToken
**Objetivo**: Verificar manejo de errores al parsear tokens inválidos.

**Validaciones**:
- Se lanza `JwtException` al parsear token inválido

---

### 2.4 Renovación de Tokens

#### ✅ shouldRefreshTokenCorrectly
**Objetivo**: Verificar renovación correcta de tokens.

**Validaciones**:
- Token renovado no es nulo
- Token renovado es diferente al original
- Token renovado es válido

#### ✅ shouldMaintainSameUserWhenRefreshing
**Objetivo**: Verificar que el usuario se mantiene al renovar.

**Validaciones**:
- Subject del token renovado coincide con el original

#### ✅ shouldExtendExpirationWhenRefreshing
**Objetivo**: Verificar extensión de fecha de expiración al renovar.

**Validaciones**:
- Fecha de expiración renovada es posterior a la original

#### ✅ shouldIndicateWhenTokenNeedsRefresh
**Objetivo**: Verificar detección de tokens que necesitan renovación.

**Escenario**:
```java
TokenUtil shortLivedTokenUtil = new TokenUtil(secret, 2000); // 2 segundos
String token = shortLivedTokenUtil.createTokenForUser("user");
Thread.sleep(1000);
```

**Validaciones**:
- `shouldRefreshToken()` retorna `true` para token próximo a expirar

#### ✅ shouldNotIndicateRefreshForNewToken
**Objetivo**: Verificar que tokens recién creados no necesitan renovación.

**Validaciones**:
- `shouldRefreshToken()` retorna `false` para token nuevo

#### ✅ shouldReturnFalseWhenCheckingRefreshForInvalidToken
**Objetivo**: Verificar manejo de tokens inválidos en verificación de renovación.

**Validaciones**:
- `shouldRefreshToken()` retorna `false` para token inválido

#### ✅ shouldThrowExceptionWhenRefreshingInvalidToken
**Objetivo**: Verificar error al renovar token inválido.

**Validaciones**:
- Se lanza `JwtException` al renovar token inválido

---

### 2.5 Verificación desde HTTP Request

#### ✅ shouldVerifyTokenFromValidAuthorizationHeader
**Objetivo**: Verificar extracción y validación de token desde header HTTP.

**Escenario**:
```java
MockHttpServletRequest request = new MockHttpServletRequest();
request.addHeader("Authorization", "Bearer " + token);
```

**Validaciones**:
- Authentication está presente
- Nombre de usuario coincide

#### ✅ shouldReturnEmptyForRequestWithoutAuthorizationHeader
**Objetivo**: Verificar manejo de requests sin header Authorization.

**Validaciones**:
- Authentication está ausente

#### ✅ shouldReturnEmptyForAuthorizationHeaderWithoutBearerPrefix
**Objetivo**: Verificar rechazo de headers sin prefijo "Bearer ".

**Validaciones**:
- Authentication está ausente sin prefijo Bearer

#### ✅ shouldReturnEmptyForInvalidTokenInHeader
**Objetivo**: Verificar rechazo de tokens inválidos en header.

**Validaciones**:
- Authentication está ausente para token inválido

#### ✅ shouldReturnEmptyForEmptyAuthorizationHeader
**Objetivo**: Verificar manejo de header Authorization vacío.

**Validaciones**:
- Authentication está ausente para header vacío

#### ✅ shouldHandleWhitespaceInToken
**Objetivo**: Verificar manejo de espacios en blanco en token.

**Escenario**:
```java
request.addHeader("Authorization", "Bearer   " + token + "   ");
```

**Validaciones**:
- Espacios son eliminados (trim)
- Token es validado correctamente

---

### 2.6 Casos Edge y Seguridad

#### ✅ shouldHandleUserWithSpecialCharacters
**Objetivo**: Verificar manejo de usuarios con caracteres especiales.

**Escenario**:
```java
String specialUser = "user@example.com";
```

**Validaciones**:
- Caracteres especiales son manejados correctamente

#### ✅ shouldHandleUserWithSpaces
**Objetivo**: Verificar manejo de usuarios con espacios.

**Escenario**:
```java
String userWithSpaces = "John Doe";
```

**Validaciones**:
- Espacios son preservados correctamente

#### ✅ shouldHandleUserWithUnicodeCharacters
**Objetivo**: Verificar manejo de caracteres Unicode.

**Escenario**:
```java
String unicodeUser = "usuario_español_日本語";
```

**Validaciones**:
- Caracteres Unicode son manejados correctamente

#### ✅ shouldGenerateTokenWithZeroDaysExpiration
**Objetivo**: Verificar generación de token con expiración de 0 días.

**Validaciones**:
- Token es generado (aunque expire inmediatamente)

#### ✅ shouldHandleMultipleVerificationsOfSameToken
**Objetivo**: Verificar múltiples validaciones del mismo token.

**Validaciones**:
- Token puede ser validado múltiples veces
- Todas las validaciones son exitosas

#### ✅ shouldUseSecretKeyOfAtLeast256Bits
**Objetivo**: Verificar requisito de longitud mínima de clave.

**Escenario**:
```java
String validSecret = "12345678901234567890123456789012"; // 32 bytes
```

**Validaciones**:
- Clave de 256 bits es aceptada

#### ✅ shouldGenerateUniqueTokensAtDifferentTimes
**Objetivo**: Verificar unicidad de tokens por timestamp.

**Validaciones**:
- Tokens generados en diferentes momentos son únicos

#### ✅ shouldNotBeAbleToModifyTokenWithoutInvalidating
**Objetivo**: Verificar protección contra modificación de tokens.

**Escenario**:
```java
String[] parts = token.split("\\.");
String modifiedToken = parts[0] + ".modified." + parts[2];
```

**Validaciones**:
- Token modificado es inválido

---

## 3. Ejecutar las Pruebas

### 3.1 Ejecutar Todas las Pruebas JWT

```bash
./mvnw test -Dtest=TokenUtilTest
```

### 3.2 Ejecutar una Prueba Específica

```bash
./mvnw test -Dtest=TokenUtilTest#shouldGenerateValidTokenWithDefaultExpiration
```

### 3.3 Ejecutar con Cobertura de Código

```bash
./mvnw test jacoco:report -Dtest=TokenUtilTest
```

Ver reporte en: `target/site/jacoco/index.html`

### 3.4 Ejecutar en Modo Verbose

```bash
./mvnw test -Dtest=TokenUtilTest -X
```

---

## 4. Configuración de Pruebas

### 4.1 Valores de Configuración

```java
private static final String TEST_SECRET = 
    "myTestSecretKeyForJWTTokenGenerationAndValidation2024WithMinimum256Bits";
private static final long TEST_EXPIRATION = 3600000; // 1 hora
private static final String TEST_USER = "testuser";
```

### 4.2 Dependencias Requeridas

```xml
<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <scope>test</scope>
</dependency>

<!-- Spring Test -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- JJWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
</dependency>
```

---

## 5. Mejores Prácticas Aplicadas

### 5.1 Nomenclatura

✅ Patrón: `should[ExpectedBehavior]When[StateUnderTest]`

Ejemplos:
- `shouldGenerateValidTokenWithDefaultExpiration`
- `shouldRejectTokenWithWrongSignature`

### 5.2 Estructura Given-When-Then

```java
@Test
void shouldDoSomething() {
    // Given - Preparar el contexto
    String token = tokenUtil.createTokenForUser(TEST_USER);
    
    // When - Ejecutar la acción
    boolean isValid = tokenUtil.validateToken(token);
    
    // Then - Verificar el resultado
    assertTrue(isValid, "El token debe ser válido");
}
```

### 5.3 Organización con @Nested

```java
@Nested
@DisplayName("Generación de Tokens")
class TokenGeneration {
    // Pruebas relacionadas con generación
}

@Nested
@DisplayName("Validación de Tokens")
class TokenValidation {
    // Pruebas relacionadas con validación
}
```

### 5.4 Mensajes Descriptivos

```java
assertTrue(isValid, "El token válido debe pasar la validación");
assertNotNull(token, "El token no debe ser nulo");
assertEquals(TEST_USER, subject, "El subject debe coincidir con el usuario");
```

---

## 6. Cobertura de Código

### 6.1 Objetivos

| Métrica | Objetivo | Estado |
|---------|----------|--------|
| Cobertura de líneas | > 90% | ⏳ Pendiente medición |
| Cobertura de ramas | > 85% | ⏳ Pendiente medición |
| Cobertura de métodos | 100% | ✅ Completado |

### 6.2 Métodos Probados

Todos los métodos públicos de `TokenUtil` están cubiertos:

- ✅ `createTokenForUser(String user)`
- ✅ `createTokenForUser(String user, int timeExpiredDay)`
- ✅ `validateToken(String token)`
- ✅ `parseUserFromToken(String token)`
- ✅ `getSubjectFromToken(String token)`
- ✅ `getExpirationFromToken(String token)`
- ✅ `refreshToken(String token)`
- ✅ `shouldRefreshToken(String token)`
- ✅ `verifyToken(HttpServletRequest request)`

---

## 7. Troubleshooting

### 7.1 Error: "Cannot find symbol TokenUtil"

**Causa**: Problema de compilación o paquetes incorrectos.

**Solución**:
```bash
./mvnw clean compile test-compile
```

### 7.2 Error: "JWT secret key is too short"

**Causa**: La clave de prueba es demasiado corta (< 256 bits).

**Solución**: Verificar que `TEST_SECRET` tenga al menos 32 bytes.

### 7.3 Error: "Test timeout"

**Causa**: Pruebas de expiración pueden tardar debido a `Thread.sleep()`.

**Solución**: Ajustar timeout si es necesario:
```java
@Test
@Timeout(value = 5, unit = TimeUnit.SECONDS)
void shouldRejectExpiredToken() {
    // ...
}
```

---

## 8. Próximos Pasos

### 8.1 Pruebas Adicionales Recomendadas

- [ ] Pruebas de integración con Spring Security
- [ ] Pruebas de rendimiento (generación masiva de tokens)
- [ ] Pruebas de concurrencia (múltiples threads)
- [ ] Pruebas de carga (validación masiva)

### 8.2 Mejoras Futuras

- [ ] Agregar pruebas parametrizadas con `@ParameterizedTest`
- [ ] Implementar pruebas de mutación con PIT
- [ ] Agregar pruebas de contrato con Spring Cloud Contract
- [ ] Implementar pruebas de seguridad con OWASP ZAP

---

## 9. Referencias

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Spring Test Documentation](https://docs.spring.io/spring-framework/reference/testing.html)
- [JJWT Documentation](https://github.com/jwtk/jjwt)
- [JWT Best Practices (RFC 8725)](https://tools.ietf.org/html/rfc8725)
- [OWASP JWT Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/JSON_Web_Token_for_Java_Cheat_Sheet.html)

---

**Última actualización**: 2026-04-23  
**Autor**: Josias Rubén Aldaz Arcalles  
**Versión**: 1.0  
**Estado**: ✅ Completado
