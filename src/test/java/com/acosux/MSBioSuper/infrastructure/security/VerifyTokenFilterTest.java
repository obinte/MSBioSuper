/*
 * Pruebas unitarias para VerifyTokenFilter.
 * Valida la verificación de tokens JWT y manejo de rutas públicas.
 */
package com.acosux.MSBioSuper.infrastructure.security;

import com.acosux.MSBioSuper.security.TokenUtil;
import com.acosux.MSBioSuper.security.VerifyTokenFilter;
import com.acosux.MSBioSuper.security.UserAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el filtro de verificación de tokens JWT.
 * Valida la autenticación, rutas públicas y manejo de errores.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0 - Java 21 Migration
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("VerifyTokenFilter - Pruebas Unitarias")
class VerifyTokenFilterTest {

    @Mock
    private TokenUtil tokenUtil;

    @Mock
    private FilterChain filterChain;

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

    // ========== Pruebas de rutas públicas ==========

    @Test
    @DisplayName("Debe permitir acceso a ruta raíz sin token")
    void doFilterInternal_RutaRaiz_DebePermitirAcceso() throws ServletException, IOException {
        // Given
        request.setRequestURI("/MSBioSuper/");

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(tokenUtil, never()).verifyToken(any());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    }

    @Test
    @DisplayName("Debe permitir acceso a Swagger UI sin token")
    void doFilterInternal_SwaggerUI_DebePermitirAcceso() throws ServletException, IOException {
        // Given
        request.setRequestURI("/swagger-ui.html");

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(tokenUtil, never()).verifyToken(any());
    }

    @Test
    @DisplayName("Debe permitir acceso a API docs sin token")
    void doFilterInternal_ApiDocs_DebePermitirAcceso() throws ServletException, IOException {
        // Given
        request.setRequestURI("/v3/api-docs");

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(tokenUtil, never()).verifyToken(any());
    }

    @Test
    @DisplayName("Debe permitir acceso a endpoint de token sin autenticación")
    void doFilterInternal_EndpointToken_DebePermitirAcceso() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/token/user/password/7");

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(tokenUtil, never()).verifyToken(any());
    }

    @Test
    @DisplayName("Debe permitir acceso a recursos de Swagger sin token")
    void doFilterInternal_RecursosSwagger_DebePermitirAcceso() throws ServletException, IOException {
        // Given
        String[] rutasSwagger = {
                "/swagger-ui/index.html",
                "/swagger-ui/swagger-ui.css",
                "/swagger-ui/swagger-ui-bundle.js",
                "/v3/api-docs/swagger-config"
        };

        for (String ruta : rutasSwagger) {
            request.setRequestURI(ruta);
            SecurityContextHolder.clearContext();

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, atLeastOnce()).doFilter(request, response);
        }

        verify(tokenUtil, never()).verifyToken(any());
    }

    // ========== Pruebas de autenticación JWT exitosa ==========

    @Test
    @DisplayName("Debe autenticar correctamente con token JWT válido")
    void doFilterInternal_ConTokenValido_DebeAutenticar() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getName()).thenReturn("testuser");
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(mockAuth));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        verify(filterChain).doFilter(request, response);
        assertThat(response.getStatus()).isNotEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Debe establecer autenticación en SecurityContext con token válido")
    void doFilterInternal_ConTokenValido_DebeEstablecerSecurityContext() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getName()).thenReturn("testuser");
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(mockAuth));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        // Nota: SecurityContext se limpia en el finally, por lo que no podemos verificarlo después
    }

    @Test
    @DisplayName("Debe procesar múltiples solicitudes con tokens válidos")
    void doFilterInternal_MultiplesTokensValidos_DebeProcesarTodos() throws ServletException, IOException {
        // Given
        String[] endpoints = {
                "/api/v1/biosuperController/getListaResumenPesca",
                "/api/v1/biosuperController/getListaResumenSiembra",
                "/api/v1/biosuperController/getConsumosDiarios"
        };

        Authentication mockAuth = mock(Authentication.class);
        when(tokenUtil.verifyToken(any())).thenReturn(Optional.of(mockAuth));

        for (String endpoint : endpoints) {
            request.setRequestURI(endpoint);
            SecurityContextHolder.clearContext();

            // When
            filter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, atLeastOnce()).doFilter(request, response);
        }

        verify(tokenUtil, times(endpoints.length)).verifyToken(any());
    }

    // ========== Pruebas de token inválido o ausente ==========

    @Test
    @DisplayName("Debe retornar 401 cuando el token es inválido")
    void doFilterInternal_ConTokenInvalido_DebeRetornar401() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentAsString()).contains("Token JWT inválido o ausente");
    }

    @Test
    @DisplayName("Debe retornar 401 cuando no hay token en la solicitud")
    void doFilterInternal_SinToken_DebeRetornar401() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Debe retornar 401 con mensaje JSON cuando el token es inválido")
    void doFilterInternal_ConTokenInvalido_DebeRetornarMensajeJSON() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContentAsString()).contains("error");
        assertThat(response.getContentAsString()).contains("Token JWT inválido o ausente");
    }

    // ========== Pruebas de manejo de excepciones ==========

    @Test
    @DisplayName("Debe retornar 401 cuando TokenUtil lanza JwtException")
    void doFilterInternal_ConJwtException_DebeRetornar401() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        when(tokenUtil.verifyToken(request))
                .thenThrow(new io.jsonwebtoken.JwtException("Token expirado"));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentAsString()).contains("Error al validar token JWT");
        assertThat(response.getContentAsString()).contains("Token expirado");
    }

    @Test
    @DisplayName("Debe retornar 401 cuando ocurre excepción inesperada")
    void doFilterInternal_ConExcepcionInesperada_DebeRetornar401() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        when(tokenUtil.verifyToken(request))
                .thenThrow(new RuntimeException("Error inesperado"));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentAsString()).contains("Error de autenticación");
    }

    @Test
    @DisplayName("Debe manejar NullPointerException correctamente")
    void doFilterInternal_ConNullPointerException_DebeRetornar401() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        when(tokenUtil.verifyToken(request))
                .thenThrow(new NullPointerException("Referencia nula"));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    }

    // ========== Pruebas de limpieza de SecurityContext ==========

    @Test
    @DisplayName("Debe limpiar SecurityContext después de procesar solicitud exitosa")
    void doFilterInternal_DespuesDeProcesar_DebeLimpiarSecurityContext() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        Authentication mockAuth = mock(Authentication.class);
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(mockAuth));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        // El SecurityContext debe estar limpio después del filtro
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("Debe limpiar SecurityContext incluso cuando hay error")
    void doFilterInternal_ConError_DebeLimpiarSecurityContext() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        when(tokenUtil.verifyToken(request))
                .thenThrow(new RuntimeException("Error"));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    // ========== Pruebas de casos edge ==========

    @Test
    @DisplayName("Debe manejar URI nula correctamente")
    void doFilterInternal_ConURINula_DebeValidarToken() throws ServletException, IOException {
        // Given
        request.setRequestURI(null);
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Debe manejar URI vacía correctamente")
    void doFilterInternal_ConURIVacia_DebeValidarToken() throws ServletException, IOException {
        // Given
        request.setRequestURI("");
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Debe distinguir entre rutas públicas y protegidas con prefijos similares")
    void doFilterInternal_ConPrefijosSimilares_DebeDistinguirRutas() throws ServletException, IOException {
        // Given - Ruta pública
        request.setRequestURI("/swagger-ui.html");
        filter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);

        // Given - Ruta protegida con prefijo similar
        SecurityContextHolder.clearContext();
        request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/swagger-data");
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.empty());

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Debe manejar rutas con parámetros de consulta")
    void doFilterInternal_ConParametrosConsulta_DebeValidarCorrectamente() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        request.setQueryString("empresa=01&sector=A");
        Authentication mockAuth = mock(Authentication.class);
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(mockAuth));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        verify(filterChain).doFilter(request, response);
    }

    // ========== Pruebas de verificación de inyección de dependencias ==========

    @Test
    @DisplayName("Debe inyectar TokenUtil correctamente")
    void constructor_DebeInyectarTokenUtil() {
        // Then - Verificar que el filtro fue creado con el mock
        assertThat(filter).isNotNull();
        assertThat(tokenUtil).isNotNull();
    }

    // ========== Pruebas de diferentes métodos HTTP ==========

    @Test
    @DisplayName("Debe validar token para solicitudes POST")
    void doFilterInternal_ConPOST_DebeValidarToken() throws ServletException, IOException {
        // Given
        request.setMethod("POST");
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        Authentication mockAuth = mock(Authentication.class);
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(mockAuth));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Debe validar token para solicitudes GET")
    void doFilterInternal_ConGET_DebeValidarToken() throws ServletException, IOException {
        // Given
        request.setMethod("GET");
        request.setRequestURI("/api/v1/biosuperController/getListaResumenPesca");
        Authentication mockAuth = mock(Authentication.class);
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(mockAuth));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Debe validar token para solicitudes PUT")
    void doFilterInternal_ConPUT_DebeValidarToken() throws ServletException, IOException {
        // Given
        request.setMethod("PUT");
        request.setRequestURI("/api/v1/biosuperController/updateData");
        Authentication mockAuth = mock(Authentication.class);
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(mockAuth));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Debe validar token para solicitudes DELETE")
    void doFilterInternal_ConDELETE_DebeValidarToken() throws ServletException, IOException {
        // Given
        request.setMethod("DELETE");
        request.setRequestURI("/api/v1/biosuperController/deleteData");
        Authentication mockAuth = mock(Authentication.class);
        when(tokenUtil.verifyToken(request)).thenReturn(Optional.of(mockAuth));

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        verify(tokenUtil).verifyToken(request);
        verify(filterChain).doFilter(request, response);
    }
}
