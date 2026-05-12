package com.acosux.MSBioSuper.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtro de ID de correlación para rastreo de solicitudes.
 * 
 * Este filtro genera o extrae un ID de correlación único para cada solicitud HTTP
 * y lo agrega al MDC (Mapped Diagnostic Context) de Logback. Esto permite rastrear
 * todas las operaciones relacionadas con una solicitud específica a través de los logs.
 * 
 * Funcionalidad:
 * - Genera un UUID único si no existe un ID de correlación en los headers
 * - Extrae el ID de correlación del header X-Correlation-ID si está presente
 * - Agrega el ID al MDC para que aparezca en todos los logs
 * - Incluye el ID en la respuesta HTTP para facilitar el rastreo
 * - Limpia el MDC después de procesar la solicitud
 * 
 * El filtro se ejecuta con la prioridad más alta (Ordered.HIGHEST_PRECEDENCE)
 * para asegurar que el ID de correlación esté disponible para todos los demás filtros.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-23
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CorrelationIdFilter.class);

    /**
     * Nombre del header HTTP para el ID de correlación.
     * Los clientes pueden enviar este header para proporcionar su propio ID de correlación.
     */
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    /**
     * Clave del MDC para el ID de correlación.
     * Esta clave se usa para almacenar y recuperar el ID del MDC de Logback.
     */
    public static final String CORRELATION_ID_MDC_KEY = "correlationId";

    /**
     * Filtra cada petición HTTP para agregar el ID de correlación al MDC.
     * 
     * Flujo de ejecución:
     * 1. Intenta extraer el ID de correlación del header X-Correlation-ID
     * 2. Si no existe, genera un nuevo UUID
     * 3. Agrega el ID al MDC de Logback
     * 4. Agrega el ID a la respuesta HTTP
     * 5. Procesa la solicitud
     * 6. Limpia el MDC después de procesar
     * 
     * @param request la petición HTTP
     * @param response la respuesta HTTP
     * @param filterChain la cadena de filtros
     * @throws ServletException si hay error en el procesamiento del filtro
     * @throws IOException si hay error de I/O
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String correlationId = null;

        try {
            // Intentar extraer el ID de correlación del header de la solicitud
            correlationId = request.getHeader(CORRELATION_ID_HEADER);

            // Si no existe, generar un nuevo UUID
            if (correlationId == null || correlationId.trim().isEmpty()) {
                correlationId = generateCorrelationId();
                logger.debug("ID de correlación generado: {}", correlationId);
            } else {
                logger.debug("ID de correlación extraído del header: {}", correlationId);
            }

            // Agregar el ID de correlación al MDC
            // Esto hace que el ID esté disponible en todos los logs de esta solicitud
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

            // Agregar el ID de correlación a la respuesta HTTP
            // Esto permite a los clientes rastrear sus solicitudes
            response.setHeader(CORRELATION_ID_HEADER, correlationId);

            // Continuar con la cadena de filtros
            filterChain.doFilter(request, response);

        } finally {
            // IMPORTANTE: Limpiar el MDC después de procesar la solicitud
            // Esto previene fugas de información entre solicitudes en entornos multi-hilo
            MDC.remove(CORRELATION_ID_MDC_KEY);
            
            if (correlationId != null) {
                logger.debug("ID de correlación limpiado del MDC: {}", correlationId);
            }
        }
    }

    /**
     * Genera un nuevo ID de correlación único usando UUID.
     * 
     * El formato del UUID es: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
     * Ejemplo: 550e8400-e29b-41d4-a716-446655440000
     * 
     * @return un ID de correlación único
     */
    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Obtiene el ID de correlación actual del MDC.
     * 
     * Este método estático puede ser usado por otras clases para obtener
     * el ID de correlación de la solicitud actual.
     * 
     * @return el ID de correlación actual, o null si no existe
     */
    public static String getCurrentCorrelationId() {
        return MDC.get(CORRELATION_ID_MDC_KEY);
    }
}
