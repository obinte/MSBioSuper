package com.acosux.MSBioSuper.integration.api;

import com.acosux.MSBioSuper.application.usecase.AuthenticateUserUseCase;
import com.acosux.MSBioSuper.config.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Clase base abstracta para pruebas de integración de API.
 * 
 * Proporciona configuración común para todas las pruebas de API:
 * - MockMvc configurado para pruebas HTTP
 * - Testcontainers PostgreSQL (heredado de BaseIntegrationTest)
 * - Métodos helper para generar tokens JWT
 * - Métodos helper para crear datos de prueba
 * 
 * Las clases de prueba de API deben extender esta clase base para
 * reutilizar la configuración y los métodos helper.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-24
 */
@AutoConfigureMockMvc
public abstract class BaseApiIntegrationTest extends BaseIntegrationTest {

    /**
     * MockMvc para realizar peticiones HTTP simuladas.
     * Permite probar controladores sin iniciar un servidor HTTP real.
     */
    @Autowired
    protected MockMvc mockMvc;

    /**
     * Caso de uso de autenticación para generar tokens JWT.
     */
    @Autowired
    protected AuthenticateUserUseCase authenticateUserUseCase;

    /**
     * Token JWT válido para usar en las pruebas.
     * Se genera antes de cada prueba con credenciales válidas.
     */
    protected String validToken;

    /**
     * Token JWT inválido para probar escenarios de error.
     */
    protected static final String INVALID_TOKEN = "Bearer invalid.token.here";

    /**
     * Credenciales de usuario válidas para pruebas.
     */
    protected static final String VALID_USERNAME = "adminbio";
    protected static final String VALID_PASSWORD = "admin123";
    protected static final int DEFAULT_TOKEN_EXPIRATION_DAYS = 7;

    /**
     * URLs base de los controladores.
     */
    protected static final String SECURITY_BASE_URL = "/api/v1";
    protected static final String BIOSUPER_BASE_URL = "/api/v1/biosuperController";

    /**
     * Configuración ejecutada antes de cada prueba.
     * Genera un token JWT válido para usar en las pruebas.
     */
    @BeforeEach
    void setUpApiTest() throws Exception {
        // Generar token JWT válido para las pruebas
        validToken = authenticateUserUseCase.execute(
            VALID_USERNAME, 
            VALID_PASSWORD, 
            DEFAULT_TOKEN_EXPIRATION_DAYS
        );
    }

    /**
     * Genera un token JWT con credenciales personalizadas.
     * 
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param expirationDays Días de expiración del token
     * @return Token JWT con prefijo "Bearer "
     * @throws Exception Si ocurre un error al generar el token
     */
    protected String generateToken(String username, String password, int expirationDays) throws Exception {
        return authenticateUserUseCase.execute(username, password, expirationDays);
    }

    /**
     * Crea un JSON de solicitud genérico para pruebas.
     * 
     * @param empresa Código de empresa
     * @param sector Código de sector
     * @param fechaDesde Fecha desde (formato yyyy-MM-dd)
     * @param fechaHasta Fecha hasta (formato yyyy-MM-dd)
     * @return JSON de solicitud
     */
    protected String createBasicRequestJson(String empresa, String sector, String fechaDesde, String fechaHasta) {
        return String.format("""
            {
                "empresa": "%s",
                "sector": "%s",
                "fechaDesde": "%s",
                "fechaHasta": "%s",
                "piscina": "",
                "corrida": ""
            }
            """, empresa, sector, fechaDesde, fechaHasta);
    }

    /**
     * Crea un JSON de solicitud con todos los campos.
     * 
     * @param empresa Código de empresa
     * @param sector Código de sector
     * @param fechaDesde Fecha desde (formato yyyy-MM-dd)
     * @param fechaHasta Fecha hasta (formato yyyy-MM-dd)
     * @param piscina Número de piscina
     * @param corrida Número de corrida
     * @return JSON de solicitud
     */
    protected String createFullRequestJson(String empresa, String sector, String fechaDesde, 
                                          String fechaHasta, String piscina, String corrida) {
        return String.format("""
            {
                "empresa": "%s",
                "sector": "%s",
                "fechaDesde": "%s",
                "fechaHasta": "%s",
                "piscina": "%s",
                "corrida": "%s"
            }
            """, empresa, sector, fechaDesde, fechaHasta, piscina, corrida);
    }

    /**
     * Crea un JSON de solicitud de producto.
     * 
     * @param empresa Código de empresa
     * @param bodega Código de bodega
     * @return JSON de solicitud
     */
    protected String createProductoRequestJson(String empresa, String bodega) {
        return String.format("""
            {
                "empresa": "%s",
                "bodega": "%s"
            }
            """, empresa, bodega);
    }

    /**
     * Crea un JSON de solicitud de cuentas por cobrar/pagar.
     * 
     * @param empresa Código de empresa
     * @param fechaDesde Fecha desde (formato yyyy-MM-dd)
     * @param fechaHasta Fecha hasta (formato yyyy-MM-dd)
     * @return JSON de solicitud
     */
    protected String createCuentasRequestJson(String empresa, String fechaDesde, String fechaHasta) {
        return String.format("""
            {
                "empresa": "%s",
                "fechaDesde": "%s",
                "fechaHasta": "%s"
            }
            """, empresa, fechaDesde, fechaHasta);
    }

    /**
     * Crea un JSON de solicitud de liquidación.
     * 
     * @param empresa Código de empresa
     * @param fechaDesde Fecha desde (formato yyyy-MM-dd)
     * @param fechaHasta Fecha hasta (formato yyyy-MM-dd)
     * @return JSON de solicitud
     */
    protected String createLiquidacionRequestJson(String empresa, String fechaDesde, String fechaHasta) {
        return String.format("""
            {
                "empresa": "%s",
                "fechaDesde": "%s",
                "fechaHasta": "%s",
                "sector": "",
                "piscina": "",
                "corrida": "",
                "cliente": "",
                "tipo": ""
            }
            """, empresa, fechaDesde, fechaHasta);
    }

    /**
     * Crea un JSON de solicitud de consumos diarios.
     * 
     * @param empresa Código de empresa
     * @param sector Código de sector
     * @param fechaDesde Fecha desde (formato yyyy-MM-dd)
     * @param fechaHasta Fecha hasta (formato yyyy-MM-dd)
     * @return JSON de solicitud
     */
    protected String createConsumosDiariosRequestJson(String empresa, String sector, 
                                                      String fechaDesde, String fechaHasta) {
        return String.format("""
            {
                "empresa": "%s",
                "sector": "%s",
                "fechaDesde": "%s",
                "fechaHasta": "%s"
            }
            """, empresa, sector, fechaDesde, fechaHasta);
    }
}
