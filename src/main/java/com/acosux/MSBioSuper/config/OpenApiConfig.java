package com.acosux.MSBioSuper.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3.0 para documentación de la API MSBioSuper.
 * 
 * Esta clase configura:
 * - Información general de la API (título, descripción, versión)
 * - Esquemas de seguridad (JWT Bearer Token)
 * - Servidores disponibles (desarrollo, staging, producción)
 * - Contacto y licencia
 * 
 * La documentación estará disponible en:
 * - Swagger UI: /swagger-ui.html
 * - OpenAPI JSON: /v3/api-docs
 * - OpenAPI YAML: /v3/api-docs.yaml
 */
@Configuration
public class OpenApiConfig {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.api.dev-url:http://localhost:8080}")
    private String devUrl;

    @Value("${app.api.staging-url:https://staging.msbiosuper.acosux.com}")
    private String stagingUrl;

    @Value("${app.api.prod-url:https://api.msbiosuper.acosux.com}")
    private String prodUrl;

    /**
     * Configura la especificación OpenAPI 3.0 para la API MSBioSuper.
     * 
     * @return Objeto OpenAPI configurado con información de la API, seguridad y servidores
     */
    @Bean
    public OpenAPI msBioSuperOpenAPI() {
        // Definir el esquema de seguridad JWT Bearer
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                // Información general de la API
                .info(new Info()
                        .title("MSBioSuper API")
                        .description("""
                                API REST para el sistema de gestión MSBioSuper.
                                
                                Esta API proporciona endpoints para:
                                - Gestión de resúmenes de pesca y siembra
                                - Consulta de consumos diarios
                                - Gestión de liquidaciones y costos
                                - Consulta de productos y gramajes
                                - Gestión de cuentas por cobrar y pagar
                                - Autenticación y autorización mediante JWT
                                
                                **Autenticación**: Todos los endpoints (excepto /login) requieren un token JWT válido.
                                Para autenticarse, primero obtenga un token mediante el endpoint de login y luego 
                                inclúyalo en el header Authorization con el formato: Bearer {token}
                                """)
                        .version(appVersion)
                        .contact(new Contact()
                                .name("Equipo de Desarrollo ACOSUX")
                                .email("desarrollo@acosux.com")
                                .url("https://www.acosux.com"))
                        .license(new License()
                                .name("Propietario")
                                .url("https://www.acosux.com/licencia")))
                
                // Configurar servidores disponibles
                // IMPORTANTE: No agregar /api/v1 aquí porque los endpoints ya lo incluyen en su @RequestMapping
                .servers(List.of(
                        new Server()
                                .url(devUrl)
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url(stagingUrl)
                                .description("Servidor de Staging"),
                        new Server()
                                .url(prodUrl)
                                .description("Servidor de Producción")
                ))
                
                // Configurar esquema de seguridad JWT Bearer
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("""
                                                Token JWT para autenticación.
                                                
                                                Para obtener un token:
                                                1. Realice una petición POST a /login con credenciales válidas
                                                2. Copie el token de la respuesta
                                                3. Haga clic en el botón 'Authorize' arriba
                                                4. Ingrese el token en el formato: Bearer {token}
                                                
                                                El token expira después de un período configurado (por defecto 24 horas).
                                                """)))
                
                // Aplicar seguridad globalmente a todos los endpoints
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}
