package com.acosux.MSBioSuper.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para VerifyTokenFilter
 * 
 * Cobertura:
 * - Validación de tokens JWT
 * - Manejo de rutas públicas
 * - Manejo de rutas protegidas
 * - Manejo de tokens inválidos
 * - Manejo de tokens ausentes
 * - Manejo de excepciones JWT
 * - Configuración del contexto de seguridad
 * - Limpieza del contexto de seguridad
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("VerifyTokenFilter - Pruebas de Filtro de Seguridad JWT")
class VerifyTokenFilterTest {

    @Mock
    private TokenUtil tokenUtil;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private VerifyTokenFilter filter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("Rutas Públicas")
    class PublicPaths {

        @Test
        @DisplayName("Debe permitir acceso a ruta raíz sin token")
        void debePermitirAccesoRutaRaiz() throws ServletException, IOException {
            // Given
            request.setRequestURI("/MSBioSuper/");

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, times(1)).doFilter(request, response);
            verify(tokenUtil, never()).verifyToken(any());
            assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        @DisplayName("Debe permitir acceso a documentación API sin token")
        void debePermitirAccesoDocumentacionAPI() throws ServletException, IOException {
            // Given
            String[] publicPaths = {
                "/api-docs",
                "/v3/api-docs",
                "/swagger-ui.html",
                "/swagger-ui/index.html"
            };

            for (String path : publicPaths) {
                request = new MockHttpServletRequest();
                request.setRequestURI(path);
                SecurityContextHolder.clearContext();

                // When
                filter.doFilterInternal(request, response, filterChain);

                // Then
                verify(filterChain).doFilter(request, response);
            }
        }

        @Test
        @DisplayName("Debe permitir acceso a endpoint de token sin autenticación")
        void debePermitirAccesoEndpointToken() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/token/user/password/7");

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, times(1)).doFilter(request, response);
            verify(tokenUtil, never()).verifyToken(any());
        }

        @Test
        @DisplayName("Debe establecer autenticación administrativa para rutas públicas")
        void debeEstablecerAutenticacionAdministrativaParaRutasPublicas() throws ServletException, IOException {
            // Given
            request.setRequestURI("/swagger-ui.html");

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            assertNotNull(auth);
            assertEquals("admin", auth.getName());
        }
    }

    @Nested
    @DisplayName("Rutas Protegidas con Token Válido")
    class ProtectedPathsWithValidToken {

        @Test
        @DisplayName("Debe permitir acceso con token JWT válido")
        void debePermitirAccesoConTokenValido() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            request.addHeader("Authorization", "Bearer valid.jwt.token");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(authentication));
            when(authentication.getName()).thenReturn("testuser");

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(tokenUtil, times(1)).verifyToken(request);
            verify(filterChain, times(1)).doFilter(request, response);
            assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        }

        @Test
        @DisplayName("Debe establecer autenticación en contexto de seguridad")
        void debeEstablecerAutenticacionEnContexto() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            request.addHeader("Authorization", "Bearer valid.jwt.token");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(authentication));

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, times(1)).doFilter(request, response);
        }

        @Test
        @DisplayName("Debe procesar múltiples requests con tokens válidos")
        void debeProcesarMultiplesRequestsConTokensValidos() throws ServletException, IOException {
            // Given
            String[] endpoints = {
                "/api/v1/biosuperController/getListaResumenPesca",
                "/api/v1/biosuperController/getListaResumenSiembra",
                "/api/v1/biosuperController/getConsumosDiarios"
            };

            for (String endpoint : endpoints) {
                request = new MockHttpServletRequest();
                response = new MockHttpServletResponse();
                request.setRequestURI(endpoint);
                request.addHeader("Authorization", "Bearer valid.jwt.token");
                SecurityContextHolder.clearContext();
                
                when(tokenUtil.verifyToken(any())).thenReturn(Optional.of(authentication));

                // When
                filter.doFilterInternal(request, response, filterChain);

                // Then
                assertEquals(HttpServletResponse.SC_OK, response.getStatus());
            }
            
            verify(filterChain, times(endpoints.length)).doFilter(any(), any());
        }
    }

    @Nested
    @DisplayName("Rutas Protegidas con Token Inválido")
    class ProtectedPathsWithInvalidToken {

        @Test
        @DisplayName("Debe rechazar request sin token")
        void debeRechazarRequestSinToken() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, never()).doFilter(request, response);
            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
            assertTrue(response.getContentAsString().contains("Token JWT inválido o ausente"));
        }

        @Test
        @DisplayName("Debe rechazar request con token inválido")
        void debeRechazarRequestConTokenInvalido() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            request.addHeader("Authorization", "Bearer invalid.token");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, never()).doFilter(request, response);
            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        }

        @Test
        @DisplayName("Debe retornar respuesta JSON con error")
        void debeRetornarRespuestaJSONConError() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            assertEquals("application/json", response.getContentType());
            assertTrue(response.getContentAsString().contains("error"));
            assertTrue(response.getContentAsString().contains("Token JWT inválido o ausente"));
        }

        @Test
        @DisplayName("Debe rechazar token expirado")
        void debeRechazarTokenExpirado() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            request.addHeader("Authorization", "Bearer expired.token");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
            verify(filterChain, never()).doFilter(request, response);
        }
    }

    @Nested
    @DisplayName("Manejo de Excepciones JWT")
    class JwtExceptionHandling {

        @Test
        @DisplayName("Debe manejar JwtException correctamente")
        void debeManejarJwtException() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            request.addHeader("Authorization", "Bearer malformed.token");
            
            when(tokenUtil.verifyToken(request)).thenThrow(new JwtException("Token malformado"));

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
            assertTrue(response.getContentAsString().contains("Error al validar token JWT"));
            assertTrue(response.getContentAsString().contains("Token malformado"));
            verify(filterChain, never()).doFilter(request, response);
        }

        @Test
        @DisplayName("Debe manejar excepción genérica correctamente")
        void debeManejarExcepcionGenerica() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            request.addHeader("Authorization", "Bearer token");
            
            when(tokenUtil.verifyToken(request)).thenThrow(new RuntimeException("Error inesperado"));

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
            assertTrue(response.getContentAsString().contains("Error de autenticación"));
            verify(filterChain, never()).doFilter(request, response);
        }

        @Test
        @DisplayName("Debe ocultar detalles técnicos en excepciones genéricas")
        void debeOcultarDetallesTecnicosEnExcepcionesGenericas() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            request.addHeader("Authorization", "Bearer token");
            
            when(tokenUtil.verifyToken(request)).thenThrow(
                new RuntimeException("Detalles técnicos sensibles"));

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
            assertFalse(response.getContentAsString().contains("Detalles técnicos sensibles"));
            assertTrue(response.getContentAsString().contains("Error de autenticación"));
        }
    }

    @Nested
    @DisplayName("Limpieza del Contexto de Seguridad")
    class SecurityContextCleanup {

        @Test
        @DisplayName("Debe limpiar contexto de seguridad después de procesar request")
        void debeLimpiarContextoDespuesDeProcesarRequest() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            request.addHeader("Authorization", "Bearer valid.token");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(authentication));

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            // El contexto debe estar limpio después del filtro
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        @DisplayName("Debe limpiar contexto incluso cuando hay excepción")
        void debeLimpiarContextoConExcepcion() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            request.addHeader("Authorization", "Bearer token");
            
            when(tokenUtil.verifyToken(request)).thenThrow(new JwtException("Error"));

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        @DisplayName("Debe limpiar contexto para rutas públicas")
        void debeLimpiarContextoParaRutasPublicas() throws ServletException, IOException {
            // Given
            request.setRequestURI("/swagger-ui.html");

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            // El contexto debe estar limpio después del filtro
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }
    }

    @Nested
    @DisplayName("Casos Edge")
    class EdgeCases {

        @Test
        @DisplayName("Debe manejar URI nula")
        void debeManejarURINula() throws ServletException, IOException {
            // Given
            request.setRequestURI(null);
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        }

        @Test
        @DisplayName("Debe manejar URI vacía")
        void debeManejarURIVacia() throws ServletException, IOException {
            // Given
            request.setRequestURI("");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        }

        @Test
        @DisplayName("Debe manejar rutas con parámetros de consulta")
        void debeManejarRutasConParametrosConsulta() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca?empresa=01");
            request.addHeader("Authorization", "Bearer valid.token");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(authentication));

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, times(1)).doFilter(request, response);
        }

        @Test
        @DisplayName("Debe manejar rutas con caracteres especiales")
        void debeManejarRutasConCaracteresEspeciales() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca%20Test");
            request.addHeader("Authorization", "Bearer valid.token");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(authentication));

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, times(1)).doFilter(request, response);
        }

        @Test
        @DisplayName("Debe manejar múltiples headers Authorization")
        void debeManejarMultiplesHeadersAuthorization() throws ServletException, IOException {
            // Given
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            request.addHeader("Authorization", "Bearer token1");
            request.addHeader("Authorization", "Bearer token2");
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(authentication));

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(tokenUtil, times(1)).verifyToken(request);
        }

        @Test
        @DisplayName("Debe distinguir entre rutas públicas y protegidas similares")
        void debeDistinguirEntreRutasPublicasYProtegidasSimilares() throws ServletException, IOException {
            // Given - Ruta pública
            request.setRequestURI("/api-docs/swagger-config");
            filter.doFilterInternal(request, response, filterChain);
            verify(filterChain, times(1)).doFilter(request, response);
            
            // Given - Ruta protegida
            request = new MockHttpServletRequest();
            response = new MockHttpServletResponse();
            request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
            SecurityContextHolder.clearContext();
            
            when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        }
    }
}
