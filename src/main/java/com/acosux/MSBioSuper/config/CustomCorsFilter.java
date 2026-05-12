package com.acosux.MSBioSuper.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filtro CORS personalizado que se ejecuta antes de Spring Security.
 * 
 * Este filtro agrega los headers CORS necesarios a todas las respuestas HTTP,
 * permitiendo que aplicaciones frontend en diferentes dominios puedan consumir la API.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-05-11
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomCorsFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CustomCorsFilter.class);

    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:4200,http://localhost:8082}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS,PATCH}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        
        logger.debug("CORS Filter - Request from origin: {}", origin);
        logger.debug("CORS Filter - Request method: {}", request.getMethod());
        logger.debug("CORS Filter - Request URI: {}", request.getRequestURI());

        // Verificar si el origen está permitido
        if (origin != null && !origin.isEmpty()) {
            List<String> allowedOriginsList = Arrays.stream(allowedOrigins.split(","))
                    .map(String::trim)
                    .toList();

            if (allowedOriginsList.contains(origin)) {
                // Agregar headers CORS
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Credentials", String.valueOf(allowCredentials));
                response.setHeader("Access-Control-Allow-Methods", allowedMethods);
                response.setHeader("Access-Control-Allow-Headers", allowedHeaders);
                response.setHeader("Access-Control-Expose-Headers", "Authorization, Content-Type, X-Total-Count");
                response.setHeader("Access-Control-Max-Age", "3600");

                logger.debug("CORS Filter - Added CORS headers for origin: {}", origin);
            } else {
                logger.warn("CORS Filter - Origin not allowed: {}", origin);
            }
        }

        // Para peticiones OPTIONS (preflight), responder inmediatamente
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            logger.debug("CORS Filter - Handling OPTIONS preflight request");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Continuar con la cadena de filtros
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("CustomCorsFilter initialized with allowed origins: {}", allowedOrigins);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
