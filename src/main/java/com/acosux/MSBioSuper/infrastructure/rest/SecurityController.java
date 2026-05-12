package com.acosux.MSBioSuper.infrastructure.rest;

import com.acosux.MSBioSuper.application.usecase.AuthenticateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operaciones de seguridad y autenticación.
 * 
 * Este controlador maneja la generación de tokens JWT para autenticación de usuarios.
 * Forma parte de la capa de infraestructura en la arquitectura hexagonal.
 * 
 * @author mario
 */
@Tag(name = "Seguridad", description = "API para autenticación y generación de tokens JWT")
@RestController
@RequestMapping("/api/v1/")
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    private final AuthenticateUserUseCase authenticateUserUseCase;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param authenticateUserUseCase Caso de uso para autenticación de usuarios
     */
    public SecurityController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     * 
     * Este endpoint valida las credenciales del usuario y genera un token JWT
     * con el tiempo de expiración especificado.
     * 
     * @param user Nombre de usuario
     * @param password Contraseña del usuario
     * @param timeExpiredDay Tiempo de expiración del token en días
     * @return ResponseEntity con el token JWT si las credenciales son válidas,
     *         o un error 401 UNAUTHORIZED si las credenciales son inválidas
     */
    @SecurityRequirements() // Este endpoint NO requiere autenticación JWT (es el que genera el token)
    @Operation(
        summary = "Generar token JWT",
        description = """
                Autentica un usuario con sus credenciales y genera un token JWT válido por el número 
                de días especificado.
                
                **Cómo usar el token en Swagger UI:**
                1. Copie el token devuelto por este endpoint (solo el token, sin prefijos)
                2. Haga clic en el botón 'Authorize' en la parte superior
                3. Pegue el token en el campo de valor (Swagger UI agregará automáticamente 'Bearer ')
                4. Haga clic en 'Authorize' y luego en 'Close'
                
                **Cómo usar el token con curl o Postman:**
                Incluya el token en el header Authorization con el formato: Bearer {token}
                Ejemplo: Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
                """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token JWT generado exitosamente",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(implementation = String.class, example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas",
            content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(mediaType = "text/plain")
        )
    })
    @RequestMapping(value = "/token/{user}/{password}/{timeExpiredDay}", method = {RequestMethod.GET})
    public ResponseEntity<String> getToken(
            @Parameter(description = "Nombre de usuario", required = true, example = "admin")
            @PathVariable("user") String user,
            @Parameter(description = "Contraseña del usuario", required = true, example = "password123")
            @PathVariable("password") String password,
            @Parameter(description = "Tiempo de expiración del token en días", required = true, example = "7")
            @PathVariable("timeExpiredDay") int timeExpiredDay) {
        
        logger.debug("Solicitud de token para usuario: {}", user);
        
        try {
            // Ejecutar caso de uso de autenticación
            String token = authenticateUserUseCase.execute(user, password, timeExpiredDay);
            
            // Verificar si el token fue generado exitosamente
            if (token != null && !token.isEmpty()) {
                logger.info("Token generado exitosamente para usuario: {}", user);
                return ResponseEntity.ok(token);
            }
            
            // Credenciales inválidas
            logger.warn("Intento de autenticación fallido para usuario: {}", user);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
            
        } catch (IllegalArgumentException e) {
            // Error de validación de parámetros
            logger.error("Error de validación en autenticación: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body("Error de validación: " + e.getMessage());
            
        } catch (Exception e) {
            // Error inesperado durante la autenticación
            logger.error("Error inesperado durante la autenticación para usuario: {}", user, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

}
