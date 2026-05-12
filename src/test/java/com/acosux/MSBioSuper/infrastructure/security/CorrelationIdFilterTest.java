package com.acosux.MSBioSuper.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CorrelationIdFilter.
 * 
 * Verifica que el filtro:
 * - Genera un ID de correlación cuando no existe
 * - Extrae el ID de correlación del header cuando está presente
 * - Agrega el ID al MDC correctamente
 * - Incluye el ID en la respuesta HTTP
 * - Limpia el MDC después de procesar la solicitud
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-23
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas del Filtro de ID de Correlación")
class CorrelationIdFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private CorrelationIdFilter filter;

    @BeforeEach
    void setUp() {
        filter = new CorrelationIdFilter();
        // Limpiar el MDC antes de cada prueba
        MDC.clear();
    }

    @AfterEach
    void tearDown() {
        // Limpiar el MDC después de cada prueba
        MDC.clear();
    }

    @Test
    @DisplayName("Debe generar un ID de correlación cuando no existe en el header")
    void shouldGenerateCorrelationIdWhenNotPresent() throws ServletException, IOException {
        // Given: No hay ID de correlación en el header
        when(request.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER)).thenReturn(null);

        // When: Se procesa la solicitud
        filter.doFilterInternal(request, response, filterChain);

        // Then: Se debe generar un ID y agregarlo a la respuesta
        verify(response).setHeader(eq(CorrelationIdFilter.CORRELATION_ID_HEADER), anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Debe usar el ID de correlación del header cuando está presente")
    void shouldUseCorrelationIdFromHeader() throws ServletException, IOException {
        // Given: Hay un ID de correlación en el header
        String existingCorrelationId = "test-correlation-id-12345";
        when(request.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER)).thenReturn(existingCorrelationId);

        // When: Se procesa la solicitud
        filter.doFilterInternal(request, response, filterChain);

        // Then: Se debe usar el ID existente
        verify(response).setHeader(CorrelationIdFilter.CORRELATION_ID_HEADER, existingCorrelationId);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Debe agregar el ID de correlación al MDC durante el procesamiento")
    void shouldAddCorrelationIdToMDC() throws ServletException, IOException {
        // Given: No hay ID de correlación en el header
        when(request.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER)).thenReturn(null);

        // When: Se procesa la solicitud
        doAnswer(invocation -> {
            // Then: Durante el procesamiento, el MDC debe contener el ID de correlación
            String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
            assertThat(correlationId).isNotNull();
            assertThat(correlationId).isNotEmpty();
            return null;
        }).when(filterChain).doFilter(request, response);

        filter.doFilterInternal(request, response, filterChain);
    }

    @Test
    @DisplayName("Debe limpiar el MDC después de procesar la solicitud")
    void shouldClearMDCAfterProcessing() throws ServletException, IOException {
        // Given: No hay ID de correlación en el header
        when(request.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER)).thenReturn(null);

        // When: Se procesa la solicitud
        filter.doFilterInternal(request, response, filterChain);

        // Then: El MDC debe estar limpio después del procesamiento
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        assertThat(correlationId).isNull();
    }

    @Test
    @DisplayName("Debe limpiar el MDC incluso si ocurre una excepción")
    void shouldClearMDCEvenOnException() throws ServletException, IOException {
        // Given: No hay ID de correlación en el header
        when(request.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER)).thenReturn(null);
        
        // And: La cadena de filtros lanza una excepción
        doThrow(new ServletException("Error de prueba")).when(filterChain).doFilter(request, response);

        // When: Se procesa la solicitud y ocurre una excepción
        try {
            filter.doFilterInternal(request, response, filterChain);
        } catch (ServletException e) {
            // Expected exception
        }

        // Then: El MDC debe estar limpio incluso después de la excepción
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        assertThat(correlationId).isNull();
    }

    @Test
    @DisplayName("Debe generar un ID válido en formato UUID")
    void shouldGenerateValidUUIDFormat() throws ServletException, IOException {
        // Given: No hay ID de correlación en el header
        when(request.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER)).thenReturn(null);

        // When: Se procesa la solicitud
        filter.doFilterInternal(request, response, filterChain);

        // Then: El ID generado debe tener formato UUID válido
        verify(response).setHeader(eq(CorrelationIdFilter.CORRELATION_ID_HEADER), argThat(id -> 
            id != null && id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
        ));
    }

    @Test
    @DisplayName("Debe manejar ID de correlación vacío del header")
    void shouldHandleEmptyCorrelationIdFromHeader() throws ServletException, IOException {
        // Given: Hay un ID de correlación vacío en el header
        when(request.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER)).thenReturn("   ");

        // When: Se procesa la solicitud
        filter.doFilterInternal(request, response, filterChain);

        // Then: Se debe generar un nuevo ID (no usar el vacío)
        verify(response).setHeader(eq(CorrelationIdFilter.CORRELATION_ID_HEADER), argThat(id -> 
            id != null && !id.trim().isEmpty()
        ));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("getCurrentCorrelationId debe retornar el ID del MDC")
    void getCurrentCorrelationIdShouldReturnMDCValue() {
        // Given: Se agrega un ID al MDC
        String testId = "test-id-123";
        MDC.put(CorrelationIdFilter.CORRELATION_ID_MDC_KEY, testId);

        // When: Se obtiene el ID actual
        String currentId = CorrelationIdFilter.getCurrentCorrelationId();

        // Then: Debe retornar el ID del MDC
        assertThat(currentId).isEqualTo(testId);
    }

    @Test
    @DisplayName("getCurrentCorrelationId debe retornar null cuando no hay ID en el MDC")
    void getCurrentCorrelationIdShouldReturnNullWhenNoId() {
        // Given: El MDC está limpio
        MDC.clear();

        // When: Se obtiene el ID actual
        String currentId = CorrelationIdFilter.getCurrentCorrelationId();

        // Then: Debe retornar null
        assertThat(currentId).isNull();
    }
}
