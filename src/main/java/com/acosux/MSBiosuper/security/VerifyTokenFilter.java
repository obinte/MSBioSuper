package com.acosux.MSBioSuper.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;

/**
 * Filtro de verificación de tokens JWT para Spring Security 6.
 * 
 * Extiende OncePerRequestFilter para garantizar que el filtro se ejecute
 * una sola vez por petición, incluso en caso de forwards o includes.
 * 
 * Funcionalidad:
 * - Permite acceso público a endpoints de documentación y autenticación
 * - Valida tokens JWT para endpoints protegidos
 * - Establece el contexto de seguridad con la autenticación del usuario
 * - Maneja errores de autenticación con respuestas HTTP 401
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 2.0 - Actualizado para Spring Security 6
 * @since 2026-04-23
 */
@Component
public class VerifyTokenFilter extends OncePerRequestFilter {

    /**
     * Endpoints públicos que no requieren validación de token JWT.
     */
    private static final String[] PUBLIC_PATHS = {
        "/MSBioSuper/",
        "api-docs",
        "swagger",
        "token",
        "/actuator/health",
        "/actuator/info",
        "/actuator/metrics",
        "/actuator/prometheus"
    };

    /**
     * Utilidad para validación de tokens JWT.
     * Inyectada por Spring mediante constructor.
     */
    private final TokenUtil tokenUtil;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param tokenUtil utilidad para validación de tokens JWT
     */
    public VerifyTokenFilter(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    /**
     * Filtra cada petición HTTP para validar el token JWT.
     * 
     * Flujo de ejecución:
     * 1. Verifica si la URI es pública (documentación, autenticación)
     * 2. Si es pública, permite el acceso sin validación
     * 3. Si es protegida, valida el token JWT
     * 4. Si el token es válido, establece la autenticación en el contexto
     * 5. Si el token es inválido, retorna HTTP 401 Unauthorized
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
        
        String requestUri = request.getRequestURI();
        
        // Verificar si la URI es pública (no requiere autenticación)
        if (isPublicPath(requestUri)) {
            // Establecer autenticación administrativa para endpoints públicos
            // Esto permite que los endpoints de documentación funcionen correctamente
            Authentication publicAuth = new UserAuthentication(new TokenUser("admin", requestUri));
            SecurityContextHolder.getContext().setAuthentication(publicAuth);
            filterChain.doFilter(request, response);
            return;
        }
        
        // Procesar autenticación JWT para endpoints protegidos
        try {
            Optional<Authentication> authentication = tokenUtil.verifyToken(request);
            
            if (authentication.isPresent()) {
                // Token válido - establecer autenticación en el contexto
                SecurityContextHolder.getContext().setAuthentication(authentication.get());
                filterChain.doFilter(request, response);
            } else {
                // Token inválido o ausente - retornar 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Token JWT inválido o ausente\"}");
                response.setContentType("application/json");
            }
        } catch (JwtException e) {
            // Error al procesar el token JWT - retornar 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Error al validar token JWT: " + e.getMessage() + "\"}");
            response.setContentType("application/json");
        } catch (Exception e) {
            // Error inesperado - retornar 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Error de autenticación\"}");
            response.setContentType("application/json");
        }
    }

    /**
     * Verifica si una URI corresponde a un endpoint público.
     * 
     * Los endpoints públicos incluyen:
     * - Raíz de la aplicación (/MSBioSuper/)
     * - Documentación API (api-docs, swagger)
     * - Endpoints de autenticación (token)
     * 
     * @param uri la URI de la petición
     * @return true si la URI es pública, false en caso contrario
     */
    private boolean isPublicPath(String uri) {
        if (uri == null) {
            return false;
        }
        
        for (String publicPath : PUBLIC_PATHS) {
            if (uri.equals(publicPath) || uri.contains(publicPath)) {
                return true;
            }
        }
        
        return false;
    }
}
