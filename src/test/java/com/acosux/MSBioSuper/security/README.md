# Pruebas de Seguridad JWT

## Descripción

Este directorio contiene las pruebas unitarias para la funcionalidad de autenticación JWT de MSBioSuper.

## Pruebas Disponibles

### TokenUtilTest

Pruebas completas para la clase `TokenUtil` que maneja la generación, validación y renovación de tokens JWT.

**Cobertura de pruebas**:
- ✅ Generación de tokens con tiempo de expiración por defecto
- ✅ Generación de tokens con días de expiración personalizados
- ✅ Inclusión de usuario en subject y claims
- ✅ Generación de tokens únicos
- ✅ Validación de tokens correctos
- ✅ Rechazo de tokens con formato inválido
- ✅ Rechazo de tokens vacíos y nulos
- ✅ Rechazo de tokens con firma incorrecta
- ✅ Rechazo de tokens expirados
- ✅ Extracción de subject del token
- ✅ Extracción de fecha de expiración
- ✅ Parseo de usuario desde token
- ✅ Manejo de errores al parsear tokens inválidos
- ✅ Renovación de tokens
- ✅ Mantenimiento de usuario al renovar
- ✅ Extensión de fecha de expiración al renovar
- ✅ Detección de tokens que necesitan renovación
- ✅ Verificación de tokens desde HTTP request
- ✅ Extracción de tokens desde header Authorization
- ✅ Manejo de requests sin header Authorization
- ✅ Manejo de headers sin prefijo Bearer
- ✅ Manejo de tokens inválidos en headers
- ✅ Manejo de espacios en blanco en tokens
- ✅ Manejo de usuarios con caracteres especiales
- ✅ Manejo de usuarios con espacios
- ✅ Manejo de usuarios con caracteres Unicode
- ✅ Múltiples verificaciones del mismo token
- ✅ Validación de longitud mínima de clave secreta
- ✅ Unicidad de tokens por timestamp
- ✅ Protección contra modificación de tokens

**Total de pruebas**: 40+ casos de prueba

## Ejecutar las Pruebas

### Ejecutar todas las pruebas de seguridad

```bash
./mvnw test -Dtest=TokenUtilTest
```

### Ejecutar una prueba específica

```bash
./mvnw test -Dtest=TokenUtilTest#shouldGenerateValidTokenWithDefaultExpiration
```

### Ejecutar con cobertura de código

```bash
./mvnw test jacoco:report -Dtest=TokenUtilTest
```

Ver reporte en: `target/site/jacoco/index.html`

### Ejecutar en modo verbose

```bash
./mvnw test -Dtest=TokenUtilTest -X
```

## Estructura de las Pruebas

Las pruebas están organizadas en clases anidadas usando `@Nested` para mejor organización:

```
TokenUtilTest
├── TokenGeneration (Generación de Tokens)
│   ├── shouldGenerateValidTokenWithDefaultExpiration
│   ├── shouldGenerateValidTokenWithCustomExpiration
│   ├── shouldIncludeUserInTokenSubject
│   ├── shouldIncludeUserIdInTokenClaims
│   └── shouldGenerateDifferentTokensForSameUser
│
├── TokenValidation (Validación de Tokens)
│   ├── shouldValidateCorrectToken
│   ├── shouldRejectInvalidFormatToken
│   ├── shouldRejectEmptyToken
│   ├── shouldRejectNullToken
│   ├── shouldRejectTokenWithWrongSignature
│   └── shouldRejectExpiredToken
│
├── TokenInformationExtraction (Extracción de Información)
│   ├── shouldExtractSubjectCorrectly
│   ├── shouldExtractExpirationDateCorrectly
│   ├── shouldParseUserFromTokenCorrectly
│   └── shouldThrowExceptionWhenParsingInvalidToken
│
├── TokenRefresh (Renovación de Tokens)
│   ├── shouldRefreshTokenCorrectly
│   ├── shouldMaintainSameUserWhenRefreshing
│   ├── shouldExtendExpirationWhenRefreshing
│   ├── shouldIndicateWhenTokenNeedsRefresh
│   ├── shouldNotIndicateRefreshForNewToken
│   ├── shouldReturnFalseWhenCheckingRefreshForInvalidToken
│   └── shouldThrowExceptionWhenRefreshingInvalidToken
│
├── HttpRequestVerification (Verificación desde HTTP Request)
│   ├── shouldVerifyTokenFromValidAuthorizationHeader
│   ├── shouldReturnEmptyForRequestWithoutAuthorizationHeader
│   ├── shouldReturnEmptyForAuthorizationHeaderWithoutBearerPrefix
│   ├── shouldReturnEmptyForInvalidTokenInHeader
│   ├── shouldReturnEmptyForEmptyAuthorizationHeader
│   └── shouldHandleWhitespaceInToken
│
├── EdgeCasesAndErrorHandling (Casos Edge y Manejo de Errores)
│   ├── shouldHandleUserWithSpecialCharacters
│   ├── shouldHandleUserWithSpaces
│   ├── shouldHandleUserWithUnicodeCharacters
│   ├── shouldGenerateTokenWithZeroDaysExpiration
│   └── shouldHandleMultipleVerificationsOfSameToken
│
└── Security (Seguridad)
    ├── shouldUseSecretKeyOfAtLeast256Bits
    ├── shouldGenerateUniqueTokensAtDifferentTimes
    └── shouldNotBeAbleToModifyTokenWithoutInvalidating
```

## Configuración de Pruebas

Las pruebas utilizan los siguientes valores de configuración:

```java
private static final String TEST_SECRET = "myTestSecretKeyForJWTTokenGenerationAndValidation2024WithMinimum256Bits";
private static final long TEST_EXPIRATION = 3600000; // 1 hora
private static final String TEST_USER = "testuser";
```

## Dependencias Requeridas

Las pruebas requieren las siguientes dependencias (ya incluidas en `pom.xml`):

- `junit-jupiter-api` - Framework de pruebas JUnit 5
- `junit-jupiter-engine` - Motor de ejecución de JUnit 5
- `spring-test` - Utilidades de prueba de Spring (MockHttpServletRequest)
- `jjwt-api` - API de JJWT para JWT
- `jjwt-impl` - Implementación de JJWT
- `jjwt-jackson` - Integración de JJWT con Jackson

## Mejores Prácticas

### Nomenclatura de Pruebas

Las pruebas siguen el patrón: `should[ExpectedBehavior]When[StateUnderTest]`

Ejemplos:
- `shouldGenerateValidTokenWithDefaultExpiration`
- `shouldRejectTokenWithWrongSignature`
- `shouldExtractSubjectCorrectly`

### Estructura Given-When-Then

Cada prueba sigue la estructura:

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

### Anotaciones @DisplayName

Todas las pruebas y clases anidadas tienen anotaciones `@DisplayName` descriptivas en español para mejor legibilidad en los reportes.

## Cobertura de Código

Objetivo de cobertura para TokenUtil: **> 90%**

Métricas actuales:
- Cobertura de líneas: Pendiente de medición
- Cobertura de ramas: Pendiente de medición
- Cobertura de métodos: 100% (todos los métodos públicos probados)

## Troubleshooting

### Error: "Cannot find symbol TokenUtil"

**Causa**: Problema de compilación o paquetes incorrectos.

**Solución**:
```bash
./mvnw clean compile test-compile
```

### Error: "JWT secret key is too short"

**Causa**: La clave de prueba es demasiado corta.

**Solución**: Verificar que `TEST_SECRET` tenga al menos 256 bits (32 bytes).

### Error: "Test timeout"

**Causa**: Pruebas de expiración de tokens pueden tardar.

**Solución**: Ajustar el timeout en `@Test(timeout = 5000)` si es necesario.

## Contribuir

Al agregar nuevas pruebas:

1. Seguir la estructura de clases anidadas existente
2. Usar nomenclatura consistente
3. Agregar `@DisplayName` descriptivo
4. Seguir patrón Given-When-Then
5. Documentar casos edge importantes
6. Mantener cobertura > 90%

## Referencias

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Spring Test Documentation](https://docs.spring.io/spring-framework/reference/testing.html)
- [JJWT Documentation](https://github.com/jwtk/jjwt)
- [JWT Best Practices](https://tools.ietf.org/html/rfc8725)

---

**Última actualización**: 2026-04-23  
**Autor**: Josias Rubén Aldaz Arcalles  
**Versión**: 1.0
