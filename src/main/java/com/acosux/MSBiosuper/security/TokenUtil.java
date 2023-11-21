package com.acosux.MSBiosuper.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import javax.servlet.http.*;
import java.util.*;

@Service
public class TokenUtil {

    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final long VALIDITY_TIME_MS = 1 * 24 * 60 * 60 * 1000;

    private final String secret = "mrin";

    public Optional<Authentication> verifyToken(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null && !token.isEmpty()) {
            final TokenUser user = parseUserFromToken(token.replace("Bearer", "").trim());
            if (user != null) {
                return Optional.of(new UserAuthentication(user));
            }
        }
        return Optional.empty();
    }

    public TokenUser parseUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return new TokenUser((String) claims.get("userId"), (String) claims.get("sub"));
    }

    public String createTokenForUser(String user, int timeExpiredDay) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS * timeExpiredDay))
                .setSubject(user)
                .claim("userId", user)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

}
