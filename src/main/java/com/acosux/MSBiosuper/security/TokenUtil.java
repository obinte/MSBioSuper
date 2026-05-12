package com.acosux.MSBioSuper.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.*;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Utilidad para generación y validación de tokens JWT usando JJWT 0.12.x
 * 
 * Características:
 * - Usa SecretKey en lugar de String para mayor seguridad
 * - Implementa mecanismo de renovación de tokens
 * - Configuración externalizada mediante properties y variables de entorno
 * - Validación robusta con manejo de excepciones
 * 
 * Configuración:
 * - jwt.secret: Clave secreta (mínimo 256 bits). Variable de entorno: JWT_SECRET
 * - jwt.expiration: Tiempo de expiración en ms. Variable de entorno: JWT_EXPIRATION
 * 
 * @see JWT-CONFIGURATION.md para guía completa de configuración
 */
@Service
public class TokenUtil {

    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_ID_CLAIM = "userId";
    
    // Tiempo de renovación: 1 hora antes de expiración
    private static final long REFRESH_THRESHOLD_MS = 60 * 60 * 1000;

    private final SecretKey secretKey;
    private final long expirationMs;
    private final JwtParser jwtParser;

    /**
     * Constructor que inicializa la clave secreta y el parser JWT
     * 
     * @param secret Clave secreta desde application.properties
     * @param expirationMs Tiempo de expiración en milisegundos
     */
    public TokenUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs) {
        
        // Generar SecretKey desde el string de configuración
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        
        // Crear parser JWT reutilizable (thread-safe)
        this.jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
    }

    /**
     * Verifica el token JWT desde el header de la petición HTTP
     * 
     * @param request Petición HTTP
     * @return Optional con Authentication si el token es válido
     */
    public Optional<Authentication> verifyToken(HttpServletRequest request) {
        final String token = extractTokenFromHeader(request);
        
        if (token != null && !token.isEmpty()) {
            try {
                final TokenUser user = parseUserFromToken(token);
                if (user != null) {
                    return Optional.of(new UserAuthentication(user));
                }
            } catch (JwtException | IllegalArgumentException e) {
                // Token inválido, expirado o malformado
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    /**
     * Extrae el token JWT del header Authorization
     * 
     * @param request Petición HTTP
     * @return Token JWT sin el prefijo "Bearer ", o null si no existe
     */
    private String extractTokenFromHeader(HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTH_HEADER_NAME);
        
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length()).trim();
        }
        
        return null;
    }

    /**
     * Parsea el token JWT y extrae la información del usuario
     * 
     * @param token Token JWT
     * @return TokenUser con la información del usuario
     * @throws JwtException si el token es inválido o expirado
     */
    public TokenUser parseUserFromToken(String token) {
        Claims claims = jwtParser
                .parseSignedClaims(token)
                .getPayload();
        
        String userId = claims.get(USER_ID_CLAIM, String.class);
        String subject = claims.getSubject();
        
        return new TokenUser(userId, subject);
    }

    /**
     * Crea un token JWT para el usuario especificado
     * 
     * @param user Identificador del usuario
     * @param timeExpiredDay Días hasta la expiración
     * @return Token JWT firmado
     */
    public String createTokenForUser(String user, int timeExpiredDay) {
        Instant now = Instant.now();
        Instant expiration = now.plus(timeExpiredDay, ChronoUnit.DAYS);
        
        return Jwts.builder()
                .subject(user)
                .claim(USER_ID_CLAIM, user)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Crea un token JWT con el tiempo de expiración por defecto
     * 
     * @param user Identificador del usuario
     * @return Token JWT firmado
     */
    public String createTokenForUser(String user) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(expirationMs);
        
        return Jwts.builder()
                .subject(user)
                .claim(USER_ID_CLAIM, user)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Verifica si un token necesita ser renovado
     * 
     * @param token Token JWT
     * @return true si el token está próximo a expirar
     */
    public boolean shouldRefreshToken(String token) {
        try {
            Claims claims = jwtParser
                    .parseSignedClaims(token)
                    .getPayload();
            
            Date expiration = claims.getExpiration();
            long timeUntilExpiration = expiration.getTime() - System.currentTimeMillis();
            
            // Renovar si queda menos del umbral de tiempo
            return timeUntilExpiration < REFRESH_THRESHOLD_MS;
        } catch (JwtException e) {
            // Token inválido, no se puede renovar
            return false;
        }
    }

    /**
     * Renueva un token JWT existente manteniendo los mismos claims
     * 
     * @param token Token JWT actual
     * @return Nuevo token JWT con tiempo de expiración extendido
     * @throws JwtException si el token es inválido
     */
    public String refreshToken(String token) {
        Claims claims = jwtParser
                .parseSignedClaims(token)
                .getPayload();
        
        String userId = claims.get(USER_ID_CLAIM, String.class);
        String subject = claims.getSubject();
        
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(expirationMs);
        
        return Jwts.builder()
                .subject(subject)
                .claim(USER_ID_CLAIM, userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Valida si un token JWT es válido (no expirado, firma correcta)
     * 
     * @param token Token JWT
     * @return true si el token es válido
     */
    public boolean validateToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extrae el subject (usuario) de un token JWT
     * 
     * @param token Token JWT
     * @return Subject del token
     * @throws JwtException si el token es inválido
     */
    public String getSubjectFromToken(String token) {
        Claims claims = jwtParser
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.getSubject();
    }

    /**
     * Extrae la fecha de expiración de un token JWT
     * 
     * @param token Token JWT
     * @return Fecha de expiración
     * @throws JwtException si el token es inválido
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = jwtParser
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.getExpiration();
    }
}
