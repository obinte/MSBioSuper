package com.acosux.MSBioSuper.infrastructure.rest;

import com.acosux.MSBioSuper.application.usecase.AuthenticateUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para SecurityController
 * 
 * Cobertura:
 * - Generación exitosa de tokens JWT
 * - Manejo de credenciales inválidas
 * - Manejo de errores de validación
 * - Manejo de excepciones inesperadas
 * - Códigos de estado HTTP correctos
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityController - Pruebas de Autenticación JWT")
class SecurityControllerTest {

    @Mock
    private AuthenticateUserUseCase authenticateUserUseCase;

    @InjectMocks
    private SecurityController controller;

    private static final String VALID_USER = "testuser";
    private static final String VALID_PASSWORD = "password123";
    private static final int VALID_EXPIRATION_DAYS = 7;
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciJ9.test";

    @Nested
    @DisplayName("Generación Exitosa de Tokens")
    class TokenGenerationSuccess {

        @Test
        @DisplayName("Debe generar token JWT exitosamente con credenciales válidas")
        void debeGenerarTokenExitosamente() {
            // Given
            when(authenticateUserUseCase.execute(VALID_USER, VALID_PASSWORD, VALID_EXPIRATION_DAYS))
                .thenReturn(VALID_TOKEN);

            // When
            ResponseEntity<String> response = controller.getToken(
                VALID_USER, VALID_PASSWORD, VALID_EXPIRATION_DAYS);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(VALID_TOKEN, response.getBody());
            verify(authenticateUserUseCase, times(1))
                .execute(VALID_USER, VALID_PASSWORD, VALID_EXPIRATION_DAYS);
        }

        @Test
        @DisplayName("Debe generar token con diferentes tiempos de expiración")
        void debeGenerarTokenConDiferentesExpiraciones() {
            // Given
            int[] expirationDays = {1, 7, 30, 90};
            
            for (int days : expirationDays) {
                when(authenticateUserUseCase.execute(VALID_USER, VALID_PASSWORD, days))
                    .thenReturn(VALID_TOKEN);

                // When
                ResponseEntity<String> response = controller.getToken(
                    VALID_USER, VALID_PASSWORD, days);

                // Then
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(VALID_TOKEN, response.getBody());
            }
            
            verify(authenticateUserUseCase, times(expirationDays.length))
                .execute(eq(VALID_USER), eq(VALID_PASSWORD), anyInt());
        }

        @Test
        @DisplayName("Debe invocar caso de uso con parámetros correctos")
        void debeInvocarCasoDeUsoConParametrosCorrectos() {
            // Given
            when(authenticateUserUseCase.execute(VALID_USER, VALID_PASSWORD, VALID_EXPIRATION_DAYS))
                .thenReturn(VALID_TOKEN);

            // When
            controller.getToken(VALID_USER, VALID_PASSWORD, VALID_EXPIRATION_DAYS);

            // Then
            verify(authenticateUserUseCase).execute(
                eq(VALID_USER), 
                eq(VALID_PASSWORD), 
                eq(VALID_EXPIRATION_DAYS)
            );
        }
    }

    @Nested
    @DisplayName("Manejo de Credenciales Inválidas")
    class InvalidCredentials {

        @Test
        @DisplayName("Debe retornar 401 cuando las credenciales son inválidas")
        void debeRetornar401ConCredencialesInvalidas() {
            // Given
            when(authenticateUserUseCase.execute(anyString(), anyString(), anyInt()))
                .thenReturn(null);

            // When
            ResponseEntity<String> response = controller.getToken(
                "invaliduser", "wrongpassword", VALID_EXPIRATION_DAYS);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals("Credenciales inválidas", response.getBody());
        }

        @Test
        @DisplayName("Debe retornar 401 cuando el token es vacío")
        void debeRetornar401CuandoTokenEsVacio() {
            // Given
            when(authenticateUserUseCase.execute(anyString(), anyString(), anyInt()))
                .thenReturn("");

            // When
            ResponseEntity<String> response = controller.getToken(
                VALID_USER, VALID_PASSWORD, VALID_EXPIRATION_DAYS);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals("Credenciales inválidas", response.getBody());
        }

        @Test
        @DisplayName("Debe retornar 401 cuando el usuario no existe")
        void debeRetornar401CuandoUsuarioNoExiste() {
            // Given
            when(authenticateUserUseCase.execute("nonexistent", VALID_PASSWORD, VALID_EXPIRATION_DAYS))
                .thenReturn(null);

            // When
            ResponseEntity<String> response = controller.getToken(
                "nonexistent", VALID_PASSWORD, VALID_EXPIRATION_DAYS);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertTrue(response.getBody().contains("Credenciales inválidas"));
        }
    }

    @Nested
    @DisplayName("Manejo de Errores de Validación")
    class ValidationErrors {

        @Test
        @DisplayName("Debe retornar 400 cuando hay error de validación")
        void debeRetornar400ConErrorDeValidacion() {
            // Given
            when(authenticateUserUseCase.execute(anyString(), anyString(), anyInt()))
                .thenThrow(new IllegalArgumentException("Tiempo de expiración inválido"));

            // When
            ResponseEntity<String> response = controller.getToken(
                VALID_USER, VALID_PASSWORD, -1);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().contains("Error de validación"));
            assertTrue(response.getBody().contains("Tiempo de expiración inválido"));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el usuario es inválido")
        void debeRetornar400ConUsuarioInvalido() {
            // Given
            when(authenticateUserUseCase.execute(eq(""), anyString(), anyInt()))
                .thenThrow(new IllegalArgumentException("Usuario no puede estar vacío"));

            // When
            ResponseEntity<String> response = controller.getToken(
                "", VALID_PASSWORD, VALID_EXPIRATION_DAYS);

            // Then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().contains("Usuario no puede estar vacío"));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando la contraseña es inválida")
        void debeRetornar400ConPasswordInvalida() {
            // Given
            when(authenticateUserUseCase.execute(anyString(), eq(""), anyInt()))
                .thenThrow(new IllegalArgumentException("Contraseña no puede estar vacía"));

            // When
            ResponseEntity<String> response = controller.getToken(
                VALID_USER, "", VALID_EXPIRATION_DAYS);

            // Then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().contains("Contraseña no puede estar vacía"));
        }
    }

    @Nested
    @DisplayName("Manejo de Excepciones Inesperadas")
    class UnexpectedExceptions {

        @Test
        @DisplayName("Debe retornar 500 cuando ocurre excepción inesperada")
        void debeRetornar500ConExcepcionInesperada() {
            // Given
            when(authenticateUserUseCase.execute(anyString(), anyString(), anyInt()))
                .thenThrow(new RuntimeException("Error de base de datos"));

            // When
            ResponseEntity<String> response = controller.getToken(
                VALID_USER, VALID_PASSWORD, VALID_EXPIRATION_DAYS);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Error interno del servidor", response.getBody());
        }

        @Test
        @DisplayName("Debe retornar 500 cuando hay NullPointerException")
        void debeRetornar500ConNullPointerException() {
            // Given
            when(authenticateUserUseCase.execute(anyString(), anyString(), anyInt()))
                .thenThrow(new NullPointerException("Referencia nula"));

            // When
            ResponseEntity<String> response = controller.getToken(
                VALID_USER, VALID_PASSWORD, VALID_EXPIRATION_DAYS);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Error interno del servidor", response.getBody());
        }

        @Test
        @DisplayName("Debe manejar excepciones sin exponer detalles internos")
        void debeOcultarDetallesInternosEnErrores() {
            // Given
            when(authenticateUserUseCase.execute(anyString(), anyString(), anyInt()))
                .thenThrow(new RuntimeException("Detalles técnicos sensibles"));

            // When
            ResponseEntity<String> response = controller.getToken(
                VALID_USER, VALID_PASSWORD, VALID_EXPIRATION_DAYS);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertFalse(response.getBody().contains("Detalles técnicos sensibles"));
            assertEquals("Error interno del servidor", response.getBody());
        }
    }

    @Nested
    @DisplayName("Casos Edge")
    class EdgeCases {

        @Test
        @DisplayName("Debe manejar usuario con caracteres especiales")
        void debeManejarUsuarioConCaracteresEspeciales() {
            // Given
            String specialUser = "user@example.com";
            when(authenticateUserUseCase.execute(specialUser, VALID_PASSWORD, VALID_EXPIRATION_DAYS))
                .thenReturn(VALID_TOKEN);

            // When
            ResponseEntity<String> response = controller.getToken(
                specialUser, VALID_PASSWORD, VALID_EXPIRATION_DAYS);

            // Then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(VALID_TOKEN, response.getBody());
        }

        @Test
        @DisplayName("Debe manejar tiempo de expiración de cero días")
        void debeManejarExpiracionCeroDias() {
            // Given
            when(authenticateUserUseCase.execute(VALID_USER, VALID_PASSWORD, 0))
                .thenReturn(VALID_TOKEN);

            // When
            ResponseEntity<String> response = controller.getToken(
                VALID_USER, VALID_PASSWORD, 0);

            // Then
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Debe manejar tiempo de expiración muy grande")
        void debeManejarExpiracionMuyGrande() {
            // Given
            int largeExpiration = 365;
            when(authenticateUserUseCase.execute(VALID_USER, VALID_PASSWORD, largeExpiration))
                .thenReturn(VALID_TOKEN);

            // When
            ResponseEntity<String> response = controller.getToken(
                VALID_USER, VALID_PASSWORD, largeExpiration);

            // Then
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Debe manejar contraseña con caracteres especiales")
        void debeManejarPasswordConCaracteresEspeciales() {
            // Given
            String specialPassword = "P@ssw0rd!#$%";
            when(authenticateUserUseCase.execute(VALID_USER, specialPassword, VALID_EXPIRATION_DAYS))
                .thenReturn(VALID_TOKEN);

            // When
            ResponseEntity<String> response = controller.getToken(
                VALID_USER, specialPassword, VALID_EXPIRATION_DAYS);

            // Then
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }
}
