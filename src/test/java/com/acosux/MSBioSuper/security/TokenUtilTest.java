package com.acosux.MSBioSuper.security;

import com.acosux.MSBioSuper.security.TokenUtil;
import com.acosux.MSBioSuper.security.TokenUser;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para TokenUtil
 * 
 * Cobertura:
 * - Generación de tokens
 * - Validación de tokens
 * - Expiración de tokens
 * - Renovación de tokens
 * - Extracción de información de tokens
 * - Manejo de errores
 */
@DisplayName("TokenUtil - Pruebas de Funcionalidad JWT")
class TokenUtilTest {

    private TokenUtil tokenUtil;
    private static final String TEST_SECRET = "myTestSecretKeyForJWTTokenGenerationAndValidation2024WithMinimum256Bits";
    private static final long TEST_EXPIRATION = 3600000; // 1 hora
    private static final String TEST_USER = "testuser";

    @BeforeEach
    void setUp() {
        tokenUtil = new TokenUtil(TEST_SECRET, TEST_EXPIRATION);
    }

    @Nested
    @DisplayName("Generación de Tokens")
    class TokenGeneration {

        @Test
        @DisplayName("Debe generar token válido con tiempo de expiración por defecto")
        void shouldGenerateValidTokenWithDefaultExpiration() {
            // When
            String token = tokenUtil.createTokenForUser(TEST_USER);

            // Then
            assertNotNull(token, "El token no debe ser nulo");
            assertFalse(token.isEmpty(), "El token no debe estar vacío");
            assertTrue(token.split("\\.").length == 3, "El token debe tener 3 partes separadas por punto");
        }

        @Test
        @DisplayName("Debe generar token válido con días de expiración personalizados")
        void shouldGenerateValidTokenWithCustomExpiration() {
            // Given
            int expirationDays = 7;

            // When
            String token = tokenUtil.createTokenForUser(TEST_USER, expirationDays);

            // Then
            assertNotNull(token, "El token no debe ser nulo");
            assertFalse(token.isEmpty(), "El token no debe estar vacío");
            
            // Verificar que el token es válido
            assertTrue(tokenUtil.validateToken(token), "El token generado debe ser válido");
        }

        @Test
        @DisplayName("Debe incluir el usuario en el subject del token")
        void shouldIncludeUserInTokenSubject() {
            // When
            String token = tokenUtil.createTokenForUser(TEST_USER);
            String subject = tokenUtil.getSubjectFromToken(token);

            // Then
            assertEquals(TEST_USER, subject, "El subject debe coincidir con el usuario");
        }

        @Test
        @DisplayName("Debe incluir el userId en los claims del token")
        void shouldIncludeUserIdInTokenClaims() {
            // When
            String token = tokenUtil.createTokenForUser(TEST_USER);
            TokenUser tokenUser = tokenUtil.parseUserFromToken(token);

            // Then
            assertNotNull(tokenUser, "TokenUser no debe ser nulo");
            assertEquals(TEST_USER, tokenUser.getUserId(), "El userId debe coincidir");
            assertEquals(TEST_USER, tokenUser.getSubject(), "El subject debe coincidir");
        }

        @Test
        @DisplayName("Debe generar tokens diferentes para el mismo usuario")
        void shouldGenerateDifferentTokensForSameUser() {
            // When
            String token1 = tokenUtil.createTokenForUser(TEST_USER);
            String token2 = tokenUtil.createTokenForUser(TEST_USER);

            // Then
            assertNotEquals(token1, token2, "Los tokens deben ser diferentes (diferente timestamp)");
        }
    }

    @Nested
    @DisplayName("Validación de Tokens")
    class TokenValidation {

        @Test
        @DisplayName("Debe validar token correcto como válido")
        void shouldValidateCorrectToken() {
            // Given
            String token = tokenUtil.createTokenForUser(TEST_USER);

            // When
            boolean isValid = tokenUtil.validateToken(token);

            // Then
            assertTrue(isValid, "El token válido debe pasar la validación");
        }

        @Test
        @DisplayName("Debe rechazar token con formato inválido")
        void shouldRejectInvalidFormatToken() {
            // Given
            String invalidToken = "invalid.token.format";

            // When
            boolean isValid = tokenUtil.validateToken(invalidToken);

            // Then
            assertFalse(isValid, "El token con formato inválido debe ser rechazado");
        }

        @Test
        @DisplayName("Debe rechazar token vacío")
        void shouldRejectEmptyToken() {
            // Given
            String emptyToken = "";

            // When
            boolean isValid = tokenUtil.validateToken(emptyToken);

            // Then
            assertFalse(isValid, "El token vacío debe ser rechazado");
        }

        @Test
        @DisplayName("Debe rechazar token nulo")
        void shouldRejectNullToken() {
            // When & Then
            assertThrows(Exception.class, () -> {
                tokenUtil.validateToken(null);
            }, "El token nulo debe lanzar excepción");
        }

        @Test
        @DisplayName("Debe rechazar token con firma incorrecta")
        void shouldRejectTokenWithWrongSignature() {
            // Given - Token generado con otra clave
            TokenUtil otherTokenUtil = new TokenUtil("differentSecretKeyWith256BitsMinimumLengthRequired", TEST_EXPIRATION);
            String tokenWithWrongSignature = otherTokenUtil.createTokenForUser(TEST_USER);

            // When
            boolean isValid = tokenUtil.validateToken(tokenWithWrongSignature);

            // Then
            assertFalse(isValid, "El token con firma incorrecta debe ser rechazado");
        }

        @Test
        @DisplayName("Debe rechazar token expirado")
        void shouldRejectExpiredToken() {
            // Given - Token con expiración de 1 milisegundo
            TokenUtil shortLivedTokenUtil = new TokenUtil(TEST_SECRET, 1);
            String token = shortLivedTokenUtil.createTokenForUser(TEST_USER);

            // Esperar a que expire
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // When
            boolean isValid = tokenUtil.validateToken(token);

            // Then
            assertFalse(isValid, "El token expirado debe ser rechazado");
        }
    }

    @Nested
    @DisplayName("Extracción de Información")
    class TokenInformationExtraction {

        @Test
        @DisplayName("Debe extraer subject correctamente")
        void shouldExtractSubjectCorrectly() {
            // Given
            String token = tokenUtil.createTokenForUser(TEST_USER);

            // When
            String subject = tokenUtil.getSubjectFromToken(token);

            // Then
            assertEquals(TEST_USER, subject, "El subject extraído debe coincidir");
        }

        @Test
        @DisplayName("Debe extraer fecha de expiración correctamente")
        void shouldExtractExpirationDateCorrectly() {
            // Given
            String token = tokenUtil.createTokenForUser(TEST_USER);
            long beforeExpiration = System.currentTimeMillis() + TEST_EXPIRATION;

            // When
            Date expirationDate = tokenUtil.getExpirationFromToken(token);

            // Then
            assertNotNull(expirationDate, "La fecha de expiración no debe ser nula");
            assertTrue(expirationDate.getTime() <= beforeExpiration + 1000, 
                "La fecha de expiración debe estar dentro del rango esperado");
        }

        @Test
        @DisplayName("Debe parsear usuario desde token correctamente")
        void shouldParseUserFromTokenCorrectly() {
            // Given
            String token = tokenUtil.createTokenForUser(TEST_USER);

            // When
            TokenUser tokenUser = tokenUtil.parseUserFromToken(token);

            // Then
            assertNotNull(tokenUser, "TokenUser no debe ser nulo");
            assertEquals(TEST_USER, tokenUser.getUserId(), "El userId debe coincidir");
            assertEquals(TEST_USER, tokenUser.getSubject(), "El subject debe coincidir");
        }

        @Test
        @DisplayName("Debe lanzar excepción al parsear token inválido")
        void shouldThrowExceptionWhenParsingInvalidToken() {
            // Given
            String invalidToken = "invalid.token.format";

            // When & Then
            assertThrows(JwtException.class, () -> {
                tokenUtil.parseUserFromToken(invalidToken);
            }, "Debe lanzar JwtException al parsear token inválido");
        }
    }

    @Nested
    @DisplayName("Renovación de Tokens")
    class TokenRefresh {

        @Test
        @DisplayName("Debe renovar token correctamente")
        void shouldRefreshTokenCorrectly() {
            // Given
            String originalToken = tokenUtil.createTokenForUser(TEST_USER);

            // When
            String refreshedToken = tokenUtil.refreshToken(originalToken);

            // Then
            assertNotNull(refreshedToken, "El token renovado no debe ser nulo");
            assertNotEquals(originalToken, refreshedToken, "El token renovado debe ser diferente al original");
            assertTrue(tokenUtil.validateToken(refreshedToken), "El token renovado debe ser válido");
        }

        @Test
        @DisplayName("Debe mantener el mismo usuario al renovar token")
        void shouldMaintainSameUserWhenRefreshing() {
            // Given
            String originalToken = tokenUtil.createTokenForUser(TEST_USER);

            // When
            String refreshedToken = tokenUtil.refreshToken(originalToken);
            String originalSubject = tokenUtil.getSubjectFromToken(originalToken);
            String refreshedSubject = tokenUtil.getSubjectFromToken(refreshedToken);

            // Then
            assertEquals(originalSubject, refreshedSubject, "El subject debe mantenerse al renovar");
        }

        @Test
        @DisplayName("Debe extender la fecha de expiración al renovar")
        void shouldExtendExpirationWhenRefreshing() {
            // Given
            String originalToken = tokenUtil.createTokenForUser(TEST_USER);
            Date originalExpiration = tokenUtil.getExpirationFromToken(originalToken);

            // When
            String refreshedToken = tokenUtil.refreshToken(originalToken);
            Date refreshedExpiration = tokenUtil.getExpirationFromToken(refreshedToken);

            // Then
            assertTrue(refreshedExpiration.after(originalExpiration), 
                "La fecha de expiración renovada debe ser posterior a la original");
        }

        @Test
        @DisplayName("Debe indicar cuando un token necesita renovación")
        void shouldIndicateWhenTokenNeedsRefresh() {
            // Given - Token con expiración corta (2 segundos)
            TokenUtil shortLivedTokenUtil = new TokenUtil(TEST_SECRET, 2000);
            String token = shortLivedTokenUtil.createTokenForUser(TEST_USER);

            // Esperar 1 segundo (dentro del umbral de renovación de 1 hora)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // When
            boolean shouldRefresh = shortLivedTokenUtil.shouldRefreshToken(token);

            // Then
            assertTrue(shouldRefresh, "El token próximo a expirar debe indicar que necesita renovación");
        }

        @Test
        @DisplayName("No debe indicar renovación para token recién creado")
        void shouldNotIndicateRefreshForNewToken() {
            // Given
            String token = tokenUtil.createTokenForUser(TEST_USER);

            // When
            boolean shouldRefresh = tokenUtil.shouldRefreshToken(token);

            // Then
            assertFalse(shouldRefresh, "El token recién creado no debe necesitar renovación");
        }

        @Test
        @DisplayName("Debe retornar false al verificar renovación de token inválido")
        void shouldReturnFalseWhenCheckingRefreshForInvalidToken() {
            // Given
            String invalidToken = "invalid.token.format";

            // When
            boolean shouldRefresh = tokenUtil.shouldRefreshToken(invalidToken);

            // Then
            assertFalse(shouldRefresh, "El token inválido no debe indicar renovación");
        }

        @Test
        @DisplayName("Debe lanzar excepción al renovar token inválido")
        void shouldThrowExceptionWhenRefreshingInvalidToken() {
            // Given
            String invalidToken = "invalid.token.format";

            // When & Then
            assertThrows(JwtException.class, () -> {
                tokenUtil.refreshToken(invalidToken);
            }, "Debe lanzar JwtException al renovar token inválido");
        }
    }

    @Nested
    @DisplayName("Verificación desde HTTP Request")
    class HttpRequestVerification {

        @Test
        @DisplayName("Debe verificar token desde header Authorization válido")
        void shouldVerifyTokenFromValidAuthorizationHeader() {
            // Given
            String token = tokenUtil.createTokenForUser(TEST_USER);
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer " + token);

            // When
            var authentication = tokenUtil.verifyToken(request);

            // Then
            assertTrue(authentication.isPresent(), "La autenticación debe estar presente");
            assertEquals(TEST_USER, authentication.get().getName(), "El nombre de usuario debe coincidir");
        }

        @Test
        @DisplayName("Debe retornar empty para request sin header Authorization")
        void shouldReturnEmptyForRequestWithoutAuthorizationHeader() {
            // Given
            MockHttpServletRequest request = new MockHttpServletRequest();

            // When
            var authentication = tokenUtil.verifyToken(request);

            // Then
            assertFalse(authentication.isPresent(), "La autenticación debe estar ausente");
        }

        @Test
        @DisplayName("Debe retornar empty para header Authorization sin prefijo Bearer")
        void shouldReturnEmptyForAuthorizationHeaderWithoutBearerPrefix() {
            // Given
            String token = tokenUtil.createTokenForUser(TEST_USER);
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", token); // Sin "Bearer "

            // When
            var authentication = tokenUtil.verifyToken(request);

            // Then
            assertFalse(authentication.isPresent(), "La autenticación debe estar ausente sin prefijo Bearer");
        }

        @Test
        @DisplayName("Debe retornar empty para token inválido en header")
        void shouldReturnEmptyForInvalidTokenInHeader() {
            // Given
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer invalid.token.format");

            // When
            var authentication = tokenUtil.verifyToken(request);

            // Then
            assertFalse(authentication.isPresent(), "La autenticación debe estar ausente para token inválido");
        }

        @Test
        @DisplayName("Debe retornar empty para header Authorization vacío")
        void shouldReturnEmptyForEmptyAuthorizationHeader() {
            // Given
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "");

            // When
            var authentication = tokenUtil.verifyToken(request);

            // Then
            assertFalse(authentication.isPresent(), "La autenticación debe estar ausente para header vacío");
        }

        @Test
        @DisplayName("Debe manejar espacios en blanco en el token")
        void shouldHandleWhitespaceInToken() {
            // Given
            String token = tokenUtil.createTokenForUser(TEST_USER);
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer   " + token + "   ");

            // When
            var authentication = tokenUtil.verifyToken(request);

            // Then
            assertTrue(authentication.isPresent(), "La autenticación debe estar presente (espacios trimmed)");
        }
    }

    @Nested
    @DisplayName("Casos Edge y Manejo de Errores")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Debe manejar usuario con caracteres especiales")
        void shouldHandleUserWithSpecialCharacters() {
            // Given
            String specialUser = "user@example.com";

            // When
            String token = tokenUtil.createTokenForUser(specialUser);
            String subject = tokenUtil.getSubjectFromToken(token);

            // Then
            assertEquals(specialUser, subject, "Debe manejar caracteres especiales en el usuario");
        }

        @Test
        @DisplayName("Debe manejar usuario con espacios")
        void shouldHandleUserWithSpaces() {
            // Given
            String userWithSpaces = "John Doe";

            // When
            String token = tokenUtil.createTokenForUser(userWithSpaces);
            String subject = tokenUtil.getSubjectFromToken(token);

            // Then
            assertEquals(userWithSpaces, subject, "Debe manejar espacios en el usuario");
        }

        @Test
        @DisplayName("Debe manejar usuario con caracteres Unicode")
        void shouldHandleUserWithUnicodeCharacters() {
            // Given
            String unicodeUser = "usuario_español_日本語";

            // When
            String token = tokenUtil.createTokenForUser(unicodeUser);
            String subject = tokenUtil.getSubjectFromToken(token);

            // Then
            assertEquals(unicodeUser, subject, "Debe manejar caracteres Unicode en el usuario");
        }

        @Test
        @DisplayName("Debe generar token con expiración de 0 días")
        void shouldGenerateTokenWithZeroDaysExpiration() {
            // Given
            int zeroDays = 0;

            // When
            String token = tokenUtil.createTokenForUser(TEST_USER, zeroDays);

            // Then
            assertNotNull(token, "Debe generar token incluso con 0 días de expiración");
            // El token expirará inmediatamente pero debe ser generado
        }

        @Test
        @DisplayName("Debe manejar múltiples verificaciones del mismo token")
        void shouldHandleMultipleVerificationsOfSameToken() {
            // Given
            String token = tokenUtil.createTokenForUser(TEST_USER);

            // When
            boolean isValid1 = tokenUtil.validateToken(token);
            boolean isValid2 = tokenUtil.validateToken(token);
            boolean isValid3 = tokenUtil.validateToken(token);

            // Then
            assertTrue(isValid1, "Primera validación debe ser exitosa");
            assertTrue(isValid2, "Segunda validación debe ser exitosa");
            assertTrue(isValid3, "Tercera validación debe ser exitosa");
        }
    }

    @Nested
    @DisplayName("Seguridad")
    class Security {

        @Test
        @DisplayName("Debe usar clave secreta de al menos 256 bits")
        void shouldUseSecretKeyOfAtLeast256Bits() {
            // Given - Clave de 256 bits (32 bytes)
            String validSecret = "12345678901234567890123456789012"; // 32 caracteres

            // When & Then
            assertDoesNotThrow(() -> {
                new TokenUtil(validSecret, TEST_EXPIRATION);
            }, "Debe aceptar clave de 256 bits");
        }

        @Test
        @DisplayName("Tokens generados en diferentes momentos deben ser únicos")
        void shouldGenerateUniqueTokensAtDifferentTimes() throws InterruptedException {
            // Given
            String token1 = tokenUtil.createTokenForUser(TEST_USER);
            Thread.sleep(1); // Asegurar diferente timestamp
            String token2 = tokenUtil.createTokenForUser(TEST_USER);

            // Then
            assertNotEquals(token1, token2, "Los tokens deben ser únicos por timestamp");
        }

        @Test
        @DisplayName("No debe ser posible modificar el token sin invalidarlo")
        void shouldNotBeAbleToModifyTokenWithoutInvalidating() {
            // Given
            String token = tokenUtil.createTokenForUser(TEST_USER);
            String[] parts = token.split("\\.");
            
            // Modificar el payload (parte 2)
            String modifiedToken = parts[0] + ".modified." + parts[2];

            // When
            boolean isValid = tokenUtil.validateToken(modifiedToken);

            // Then
            assertFalse(isValid, "El token modificado debe ser inválido");
        }
    }
}
