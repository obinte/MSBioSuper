package com.acosux.MSBiosuper.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Con Swagger apagado (swagger.enabled distinto de true, el valor por defecto),
 * responde 404 a sus rutas. Hace falta porque Spring Boot sirve swagger-ui.html
 * y sus webjars como recursos estáticos del jar springfox-swagger-ui aunque
 * SwaggerConfig no se cargue.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "false", matchIfMissing = true)
public class SwaggerBloqueoFilter implements Filter {

    private static final String[] RUTAS_SWAGGER = {
        "/swagger-ui.html",
        "/swagger-resources",
        "/v2/api-docs",
        "/webjars/springfox-swagger-ui"
    };

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        // ruta ya decodificada y sin parámetros ;x, para que variantes codificadas no la salten
        String ruta = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        for (String rutaSwagger : RUTAS_SWAGGER) {
            if (ruta.startsWith(rutaSwagger)) {
                ((HttpServletResponse) res).sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        filterChain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
