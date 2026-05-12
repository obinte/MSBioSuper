package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.security.TokenUtil;
import com.acosux.MSBioSuper.util.Encriptacion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para autenticar usuarios y generar tokens JWT.
 * Implementa la lógica de negocio para validar credenciales y generar tokens de acceso.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones de autenticación.
 * 
 * NOTA: Las credenciales están temporalmente hardcodeadas. En una implementación
 * de producción, estas deberían obtenerse de una base de datos o servicio de autenticación.
 */
@Service
public class AuthenticateUserUseCase {

    private final TokenUtil tokenUtil;
    
    // TODO: Externalizar estas credenciales a application.properties o base de datos
    @Value("${app.security.admin.username:adminbio}")
    private String adminUsername;
    
    @Value("${app.security.admin.password-hash:8601e29778f60cb8f547246e2aa3694cb0cc597298645ae26e2725a2fc3dad00}")
    private String adminPasswordHash;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param tokenUtil Utilidad para generación de tokens JWT
     */
    public AuthenticateUserUseCase(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    /**
     * Ejecuta el caso de uso para autenticar un usuario y generar un token JWT.
     * 
     * Valida las credenciales del usuario comparando el hash SHA-256 de la contraseña
     * con el hash almacenado. Si las credenciales son válidas, genera un token JWT
     * con el tiempo de expiración especificado.
     *
     * @param username Nombre de usuario
     * @param password Contraseña en texto plano
     * @param timeExpiredDays Tiempo de expiración del token en días
     * @return Token JWT (sin prefijo "Bearer ") si las credenciales son válidas, cadena vacía si no
     * @throws Exception Si ocurre un error durante el proceso de autenticación
     */
    public String execute(String username, String password, int timeExpiredDays) throws Exception {
        // Validar parámetros de entrada
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        
        if (timeExpiredDays <= 0) {
            throw new IllegalArgumentException("El tiempo de expiración debe ser mayor a 0");
        }

        // Lógica de negocio: Validar credenciales
        Encriptacion encriptacion = new Encriptacion();
        String passwordHash = encriptacion.sha256(password);
        
        // Comparar credenciales con las almacenadas
        if (username.equals(adminUsername) && passwordHash.equals(adminPasswordHash)) {
            // Generar token JWT si las credenciales son válidas
            // NOTA: Devolvemos solo el token sin el prefijo "Bearer " porque Swagger UI
            // y otros clientes HTTP lo agregan automáticamente al header Authorization
            String token = tokenUtil.createTokenForUser(username, timeExpiredDays);
            return token;
        }
        
        // Retornar cadena vacía si las credenciales no son válidas
        // En una implementación de producción, se debería lanzar una excepción específica
        return "";
    }
    
    /**
     * Valida si las credenciales proporcionadas son correctas.
     * 
     * Este método puede ser usado para validación sin generar un token.
     *
     * @param username Nombre de usuario
     * @param password Contraseña en texto plano
     * @return true si las credenciales son válidas, false en caso contrario
     * @throws Exception Si ocurre un error durante la validación
     */
    public boolean validateCredentials(String username, String password) throws Exception {
        if (username == null || password == null) {
            return false;
        }
        
        Encriptacion encriptacion = new Encriptacion();
        String passwordHash = encriptacion.sha256(password);
        
        return username.equals(adminUsername) && passwordHash.equals(adminPasswordHash);
    }
}
