package com.acosux.MSBiosuper.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.FilterChain;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;

@Component
public class VerifyTokenFilter implements javax.servlet.Filter {

    private static final Logger LOG = Logger.getLogger(VerifyTokenFilter.class.getName());

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String a = request.getRequestURI();
        if (a != null && (a.equals(request.getContextPath() + "/") || a.contains("api-docs") || a.contains("swagger") || a.contains("token"))) {
            SecurityContextHolder.getContext().setAuthentication(Optional.of(new UserAuthentication(new TokenUser("admin", a))).get());
            filterChain.doFilter(req, res);
            return;
        } else {
            HttpServletResponse response = (HttpServletResponse) res;
            Optional<Authentication> authentication;
            try {
                authentication = new TokenUtil().verifyToken(request);
            } catch (JwtException | IllegalArgumentException e) {
                // Token vencido, mal firmado o mal formado: se registra el motivo para poder diagnosticar el 401.
                LOG.log(Level.WARNING, "[VerifyTokenFilter] 401 {0} desde {1}: token inválido ({2}: {3})",
                        new Object[]{a, request.getRemoteAddr(), e.getClass().getSimpleName(), e.getMessage()});
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            if (!authentication.isPresent()) {
                LOG.log(Level.WARNING, "[VerifyTokenFilter] 401 {0} desde {1}: sin header Authorization",
                        new Object[]{a, request.getRemoteAddr()});
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            // Las excepciones del resto de la cadena ya no se descartan: llegan a Tomcat, que las registra y responde 500.
            try {
                SecurityContextHolder.getContext().setAuthentication(authentication.get());
                filterChain.doFilter(req, res);
            } finally {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
