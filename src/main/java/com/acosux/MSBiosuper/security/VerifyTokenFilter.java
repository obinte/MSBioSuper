package com.acosux.MSBiosuper.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.FilterChain;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;

@Component
public class VerifyTokenFilter implements javax.servlet.Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String a = request.getRequestURI();
        if (a != null && (a.equals("/MSBioSuper/") || a.contains("api-docs") || a.contains("swagger") || a.contains("token"))) {
            SecurityContextHolder.getContext().setAuthentication(Optional.of(new UserAuthentication(new TokenUser("admin", a))).get());
            filterChain.doFilter(req, res);
            return;
        } else {
            HttpServletResponse response = (HttpServletResponse) res;
            try {
                TokenUtil tokenUtil = new TokenUtil();
                Optional<Authentication> authentication = tokenUtil.verifyToken(request);
                if (authentication.isPresent()) {
                    SecurityContextHolder.getContext().setAuthentication(authentication.get());
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    throw new Exception();
                }
                filterChain.doFilter(req, res);
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } finally {
                SecurityContextHolder.getContext().setAuthentication(null);
                return;
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
