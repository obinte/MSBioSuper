package com.acosux.MSBioSuper.integration.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Pruebas de integración para SecurityController.
 * 
 * Prueba el endpoint de autenticación JWT:
 * - GET /api/v1/token/{user}/{password}/{timeExpiredDay}
 * 
 * Escenarios probados:
 * - ✅ Generación exitosa de token con credenciales válidas
 * - ❌ Rechazo con credenciales inválidas (usuario incorrecto)
 * - ❌ Rechazo con credenciales inválidas (contraseña incorrecta)
 * - ❌ Validación de parámetros (tiempo de expiración inválido)
 * - ✅ Token generado es válido y tiene formato correcto
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-24
 */
@DisplayName("Pruebas de Integración - SecurityController")
class SecurityControllerIntegrationTest extends BaseApiIntegrationTest {

    @Test
    @DisplayName("Debe generar token JWT con credenciales válidas")
    void shouldGenerateTokenWithValidCredentials() throws Exception {
        // Given: Credenciales válidas
        String username = VALID_USERNAME;
        String password = VALID_PASSWORD;
        int expirationDays = 7;

        // When: Se solicita un token
        mockMvc.perform(MockMvcRequestBuilders
                .get(SECURITY_BASE_URL + "/token/{user}/{password}/{timeExpiredDay}", 
                     username, password, expirationDays))
                
                // Then: Se retorna 200 OK con un token válido
                .andExpect(status().isOk())
                .andExpect(content().string(startsWith("Bearer ")))
                .andExpect(content().string(not(emptyString())));
    }

    @Test
    @DisplayName("Debe rechazar con usuario inválido")
    void shouldRejectWithInvalidUsername() throws Exception {
        // Given: Usuario inválido
        String invalidUsername = "usuarioInvalido";
        String password = VALID_PASSWORD;
        int expirationDays = 7;

        // When: Se solicita un token con usuario inválido
        mockMvc.perform(MockMvcRequestBuilders
                .get(SECURITY_BASE_URL + "/token/{user}/{password}/{timeExpiredDay}", 
                     invalidUsername, password, expirationDays))
                
                // Then: Se retorna 401 Unauthorized
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Credenciales inválidas")));
    }

    @Test
    @DisplayName("Debe rechazar con contraseña inválida")
    void shouldRejectWithInvalidPassword() throws Exception {
        // Given: Contraseña inválida
        String username = VALID_USERNAME;
        String invalidPassword = "contraseñaIncorrecta";
        int expirationDays = 7;

        // When: Se solicita un token con contraseña inválida
        mockMvc.perform(MockMvcRequestBuilders
                .get(SECURITY_BASE_URL + "/token/{user}/{password}/{timeExpiredDay}", 
                     username, invalidPassword, expirationDays))
                
                // Then: Se retorna 401 Unauthorized
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Credenciales inválidas")));
    }

    @Test
    @DisplayName("Debe rechazar con tiempo de expiración inválido (cero)")
    void shouldRejectWithZeroExpirationTime() throws Exception {
        // Given: Tiempo de expiración inválido (0)
        String username = VALID_USERNAME;
        String password = VALID_PASSWORD;
        int invalidExpirationDays = 0;

        // When: Se solicita un token con tiempo de expiración inválido
        mockMvc.perform(MockMvcRequestBuilders
                .get(SECURITY_BASE_URL + "/token/{user}/{password}/{timeExpiredDay}", 
                     username, password, invalidExpirationDays))
                
                // Then: Se retorna 400 Bad Request
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error de validación")));
    }

    @Test
    @DisplayName("Debe rechazar con tiempo de expiración negativo")
    void shouldRejectWithNegativeExpirationTime() throws Exception {
        // Given: Tiempo de expiración negativo
        String username = VALID_USERNAME;
        String password = VALID_PASSWORD;
        int invalidExpirationDays = -5;

        // When: Se solicita un token con tiempo de expiración negativo
        mockMvc.perform(MockMvcRequestBuilders
                .get(SECURITY_BASE_URL + "/token/{user}/{password}/{timeExpiredDay}", 
                     username, password, invalidExpirationDays))
                
                // Then: Se retorna 400 Bad Request
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Error de validación")));
    }

    @Test
    @DisplayName("Debe generar token con diferentes tiempos de expiración")
    void shouldGenerateTokenWithDifferentExpirationTimes() throws Exception {
        // Given: Diferentes tiempos de expiración válidos
        int[] expirationDays = {1, 7, 30, 90};

        for (int days : expirationDays) {
            // When: Se solicita un token con cada tiempo de expiración
            mockMvc.perform(MockMvcRequestBuilders
                    .get(SECURITY_BASE_URL + "/token/{user}/{password}/{timeExpiredDay}", 
                         VALID_USERNAME, VALID_PASSWORD, days))
                    
                    // Then: Se retorna 200 OK con un token válido
                    .andExpect(status().isOk())
                    .andExpect(content().string(startsWith("Bearer ")))
                    .andExpect(content().string(not(emptyString())));
        }
    }

    @Test
    @DisplayName("Debe rechazar con usuario vacío")
    void shouldRejectWithEmptyUsername() throws Exception {
        // Given: Usuario vacío
        String emptyUsername = "";
        String password = VALID_PASSWORD;
        int expirationDays = 7;

        // When: Se solicita un token con usuario vacío
        mockMvc.perform(MockMvcRequestBuilders
                .get(SECURITY_BASE_URL + "/token/{user}/{password}/{timeExpiredDay}", 
                     emptyUsername, password, expirationDays))
                
                // Then: Se retorna 400 Bad Request o 401 Unauthorized
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Debe rechazar con contraseña vacía")
    void shouldRejectWithEmptyPassword() throws Exception {
        // Given: Contraseña vacía
        String username = VALID_USERNAME;
        String emptyPassword = "";
        int expirationDays = 7;

        // When: Se solicita un token con contraseña vacía
        mockMvc.perform(MockMvcRequestBuilders
                .get(SECURITY_BASE_URL + "/token/{user}/{password}/{timeExpiredDay}", 
                     username, emptyPassword, expirationDays))
                
                // Then: Se retorna 400 Bad Request o 401 Unauthorized
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Debe generar tokens diferentes en solicitudes consecutivas")
    void shouldGenerateDifferentTokensInConsecutiveRequests() throws Exception {
        // Given: Mismas credenciales
        String username = VALID_USERNAME;
        String password = VALID_PASSWORD;
        int expirationDays = 7;

        // When: Se solicitan dos tokens consecutivamente
        String token1 = mockMvc.perform(MockMvcRequestBuilders
                .get(SECURITY_BASE_URL + "/token/{user}/{password}/{timeExpiredDay}", 
                     username, password, expirationDays))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Pequeña pausa para asegurar diferente timestamp
        Thread.sleep(1000);

        String token2 = mockMvc.perform(MockMvcRequestBuilders
                .get(SECURITY_BASE_URL + "/token/{user}/{password}/{timeExpiredDay}", 
                     username, password, expirationDays))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Then: Los tokens deben ser diferentes (diferente timestamp de emisión)
        assert !token1.equals(token2) : "Los tokens consecutivos deben ser diferentes";
    }
}
