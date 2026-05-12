package com.acosux.MSBioSuper.config;

import com.acosux.MSBioSuper.security.VerifyTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración moderna de seguridad para Spring Security 6.
 * 
 * Implementa:
 * - SecurityFilterChain con DSL lambda (sin WebSecurityConfigurerAdapter)
 * - Configuración CORS externalizada mediante CorsConfigurationSource
 * - Protección CSRF deshabilitada (API REST stateless con JWT)
 * - Gestión de sesiones stateless (sin sesiones HTTP)
 * - Autenticación JWT mediante filtro personalizado
 * - Endpoints públicos para documentación API y autenticación
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-23
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final VerifyTokenFilter verifyTokenFilter;

    /**
     * Constructor para inyección de dependencias.
     * 
     * @param verifyTokenFilter el filtro de verificación de tokens JWT
     */
    public SecurityConfig(VerifyTokenFilter verifyTokenFilter) {
        this.verifyTokenFilter = verifyTokenFilter;
    }

    /**
     * Orígenes permitidos para CORS, configurables mediante properties.
     * Ejemplo: http://localhost:3000,http://localhost:4200
     */
    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:4200}")
    private String allowedOrigins;

    /**
     * Métodos HTTP permitidos para CORS.
     */
    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    /**
     * Headers permitidos para CORS.
     */
    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    /**
     * Permitir credenciales en peticiones CORS.
     */
    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    /**
     * Endpoints públicos que no requieren autenticación.
     */
    private static final String[] PUBLIC_ENDPOINTS = {
        // Documentación API - Swagger UI (antiguo)
        "/v2/api-docs",
        "/v2/api-docs/**",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/webjars/**",
        
        // Documentación API - OpenAPI 3 (nuevo)
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/v3/api-docs.yaml",
        
        // Endpoints de autenticación
        "/token/**",
        "/auth/**",
        "/api/v1/security/**",
        "/api/v1/token/**",
        
        // Endpoint de error de Spring Boot
        "/error",
        
        // Actuator endpoints (público para monitoreo y pruebas)
        "/actuator",
        "/actuator/health",
        "/actuator/health/**",
        "/actuator/info",
        "/actuator/metrics",
        "/actuator/metrics/**",
        "/actuator/prometheus"
    };

    /**
     * Configura el filtro de seguridad principal de la aplicación.
     * 
     * Características:
     * - CORS habilitado con configuración externalizada
     * - CSRF deshabilitado (API REST stateless)
     * - Sesiones stateless (sin estado HTTP)
     * - JWT authentication filter
     * - Autorización basada en endpoints
     * 
     * @param http el objeto HttpSecurity para configurar
     * @return el SecurityFilterChain configurado
     * @throws Exception si hay error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuración CORS - usar bean CorsConfigurationSource
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Deshabilitar CSRF - no necesario para API REST stateless con JWT
            // Las peticiones no usan cookies de sesión, sino tokens JWT en headers
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configuración de autorización de peticiones
            .authorizeHttpRequests(authorize -> authorize
                // Permitir acceso público a endpoints de documentación y autenticación
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                
                // Permitir acceso público a métodos GET específicos
                .requestMatchers(HttpMethod.GET, 
                    "/",
                    "/v2/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                ).permitAll()
                
                // Permitir OPTIONS para preflight CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Todas las demás peticiones requieren autenticación
                .anyRequest().authenticated()
            )
            
            // Configuración de gestión de sesiones - STATELESS para JWT
            // No se crean ni se usan sesiones HTTP
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Agregar filtro JWT antes del filtro de autenticación estándar
            // Se inyecta como bean para aprovechar la gestión de dependencias de Spring
            .addFilterBefore(
                verifyTokenFilter, 
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    /**
     * Configura CORS (Cross-Origin Resource Sharing) de forma externalizada.
     * 
     * Permite que aplicaciones frontend en diferentes dominios puedan
     * consumir la API REST. La configuración se lee desde application.properties
     * para facilitar cambios por entorno (dev, test, prod).
     * 
     * Configuración externalizada mediante properties:
     * - cors.allowed-origins: Orígenes permitidos (ej: http://localhost:3000)
     * - cors.allowed-methods: Métodos HTTP permitidos (GET, POST, etc.)
     * - cors.allowed-headers: Headers permitidos en peticiones
     * - cors.allow-credentials: Permitir envío de credenciales (cookies, auth headers)
     * 
     * @return CorsConfigurationSource configurado con los valores externalizados
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Log para debug - ver qué orígenes se están configurando
        System.out.println("=== CORS Configuration Debug ===");
        System.out.println("allowedOrigins from properties: " + allowedOrigins);
        
        // IMPORTANTE: Limpiar espacios en blanco y configurar orígenes
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .toList();
        
        System.out.println("Cleaned origins: " + origins);
        
        // Usar allowedOriginPatterns para compatibilidad con allowCredentials
        configuration.setAllowedOriginPatterns(origins);
        
        // Configurar métodos HTTP permitidos
        List<String> methods = Arrays.stream(allowedMethods.split(","))
                .map(String::trim)
                .toList();
        configuration.setAllowedMethods(methods);
        System.out.println("Configured methods: " + methods);
        
        // Configurar headers permitidos
        if ("*".equals(allowedHeaders.trim())) {
            configuration.addAllowedHeader("*");
            System.out.println("Configured headers: *");
        } else {
            List<String> headers = Arrays.stream(allowedHeaders.split(","))
                    .map(String::trim)
                    .toList();
            configuration.setAllowedHeaders(headers);
            System.out.println("Configured headers: " + headers);
        }
        
        // Permitir credenciales (necesario para JWT en Authorization header)
        configuration.setAllowCredentials(allowCredentials);
        System.out.println("Allow credentials: " + allowCredentials);
        
        // Headers expuestos al cliente
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Total-Count"
        ));
        
        // Tiempo de caché para preflight requests (1 hora)
        configuration.setMaxAge(3600L);
        
        // Aplicar configuración a todos los endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        System.out.println("CORS configuration registered for pattern: /**");
        System.out.println("=== End CORS Configuration ===");
        
        return source;
    }
}
