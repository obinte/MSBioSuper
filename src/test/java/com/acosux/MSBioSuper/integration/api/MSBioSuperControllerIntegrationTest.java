package com.acosux.MSBioSuper.integration.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Pruebas de integración para MSBioSuperController.
 * 
 * Prueba los 12 endpoints REST del controlador principal:
 * 1. POST /getListaResumenPesca - Resumen de pesca
 * 2. POST /getListaResumenSiembra - Resumen de siembra
 * 3. POST /getConsumosDiarios - Consumos diarios
 * 4. POST /getListadoLiquidacionDetalleProducto - Liquidaciones detalle
 * 5. POST /listarLiquidacionesDetalle - Liquidaciones listado
 * 6. POST /listarCostosPorPiscina - Costos por piscina
 * 7. POST /obtenerListadoGramajes - Gramajes
 * 8. POST /getListadoComprasDetalleProducto - Compras detalle
 * 9. POST /listarVentasBioSuper - Ventas
 * 10. POST /getListaProductosGeneralTO - Productos general
 * 11. POST /getApiCarListaCuentasPorCobrarDetalladoTO - Cuentas por cobrar
 * 12. POST /getCarListaCuentasPorPagarDetalladoTO - Cuentas por pagar
 * 
 * Escenarios probados por endpoint:
 * - ✅ Happy path: Request válido con token → 200 OK
 * - ❌ Sin autenticación: Request sin token → 401 Unauthorized
 * - ❌ Token inválido: Request con token malformado → 401 Unauthorized
 * - ❌ Validación: Request con datos inválidos → 400 Bad Request
 * - ⚠️ Sin resultados: Request válido pero sin datos → 200 OK con advertencia
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-24
 */
@DisplayName("Pruebas de Integración - MSBioSuperController")
class MSBioSuperControllerIntegrationTest extends BaseApiIntegrationTest {

    // ========================================
    // 1. Resumen de Pesca
    // ========================================
    
    @Nested
    @DisplayName("1. Endpoint: getListaResumenPesca")
    class ResumenPescaTests {

        private static final String ENDPOINT = "/getListaResumenPesca";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            // Given: Request válido
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            // When: Se realiza la petición con token válido
            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    
                    // Then: Se retorna 200 OK con estructura RespuestaWebTO
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists())
                    .andExpect(jsonPath("$.estadoOperacion").value(anyOf(
                        equalTo("EXITO"), 
                        equalTo("ADVERTENCIA")
                    )));
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            // Given: Request válido pero sin token
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            // When: Se realiza la petición sin token
            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    
                    // Then: Se retorna 401 Unauthorized
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 401 con token inválido")
        void shouldReturn401WithInvalidToken() throws Exception {
            // Given: Request válido pero con token inválido
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            // When: Se realiza la petición con token inválido
            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", INVALID_TOKEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    
                    // Then: Se retorna 401 Unauthorized
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 400 con empresa vacía")
        void shouldReturn400WithEmptyEmpresa() throws Exception {
            // Given: Request con empresa vacía
            String requestJson = createBasicRequestJson("", "SECTOR-A", "2024-01-01", "2024-12-31");

            // When: Se realiza la petición
            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    
                    // Then: Se retorna 400 Bad Request
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 con formato de fecha inválido")
        void shouldReturn400WithInvalidDateFormat() throws Exception {
            // Given: Request con formato de fecha inválido
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024/01/01", "2024/12/31");

            // When: Se realiza la petición
            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    
                    // Then: Se retorna 400 Bad Request
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 con rango de fechas inválido (desde > hasta)")
        void shouldReturn400WithInvalidDateRange() throws Exception {
            // Given: Request con rango de fechas inválido
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-12-31", "2024-01-01");

            // When: Se realiza la petición
            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    
                    // Then: Se retorna 400 Bad Request
                    .andExpect(status().isBadRequest());
        }
    }

    // ========================================
    // 2. Resumen de Siembra
    // ========================================
    
    @Nested
    @DisplayName("2. Endpoint: getListaResumenSiembra")
    class ResumenSiembraTests {

        private static final String ENDPOINT = "/getListaResumenSiembra";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 400 con datos inválidos")
        void shouldReturn400WithInvalidData() throws Exception {
            String requestJson = createBasicRequestJson("", "", "", "");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    // ========================================
    // 3. Consumos Diarios
    // ========================================
    
    @Nested
    @DisplayName("3. Endpoint: getConsumosDiarios")
    class ConsumosDiariosTests {

        private static final String ENDPOINT = "/getConsumosDiarios";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createConsumosDiariosRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createConsumosDiariosRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 400 con datos inválidos")
        void shouldReturn400WithInvalidData() throws Exception {
            String requestJson = createConsumosDiariosRequestJson("", "", "fecha-invalida", "fecha-invalida");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    // ========================================
    // 4. Liquidación Detalle Producto
    // ========================================
    
    @Nested
    @DisplayName("4. Endpoint: getListadoLiquidacionDetalleProducto")
    class LiquidacionDetalleProductoTests {

        private static final String ENDPOINT = "/getListadoLiquidacionDetalleProducto";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createLiquidacionRequestJson("01", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createLiquidacionRequestJson("01", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 400 con datos inválidos")
        void shouldReturn400WithInvalidData() throws Exception {
            String requestJson = createLiquidacionRequestJson("", "", "");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    // ========================================
    // 5. Listar Liquidaciones Detalle
    // ========================================
    
    @Nested
    @DisplayName("5. Endpoint: listarLiquidacionesDetalle")
    class ListarLiquidacionesDetalleTests {

        private static final String ENDPOINT = "/listarLiquidacionesDetalle";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createLiquidacionRequestJson("01", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createLiquidacionRequestJson("01", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ========================================
    // 6. Costos por Piscina
    // ========================================
    
    @Nested
    @DisplayName("6. Endpoint: listarCostosPorPiscina")
    class CostosPorPiscinaTests {

        private static final String ENDPOINT = "/listarCostosPorPiscina";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ========================================
    // 7. Gramajes
    // ========================================
    
    @Nested
    @DisplayName("7. Endpoint: obtenerListadoGramajes")
    class GramajesTests {

        private static final String ENDPOINT = "/obtenerListadoGramajes";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ========================================
    // 8. Compras Detalle Producto
    // ========================================
    
    @Nested
    @DisplayName("8. Endpoint: getListadoComprasDetalleProducto")
    class ComprasDetalleProductoTests {

        private static final String ENDPOINT = "/getListadoComprasDetalleProducto";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ========================================
    // 9. Ventas BioSuper
    // ========================================
    
    @Nested
    @DisplayName("9. Endpoint: listarVentasBioSuper")
    class VentasBioSuperTests {

        private static final String ENDPOINT = "/listarVentasBioSuper";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createBasicRequestJson("01", "SECTOR-A", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ========================================
    // 10. Productos General
    // ========================================
    
    @Nested
    @DisplayName("10. Endpoint: getListaProductosGeneralTO")
    class ProductosGeneralTests {

        private static final String ENDPOINT = "/getListaProductosGeneralTO";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createProductoRequestJson("01", "BODEGA-01");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createProductoRequestJson("01", "BODEGA-01");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 400 con empresa vacía")
        void shouldReturn400WithEmptyEmpresa() throws Exception {
            String requestJson = createProductoRequestJson("", "BODEGA-01");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    // ========================================
    // 11. Cuentas por Cobrar
    // ========================================
    
    @Nested
    @DisplayName("11. Endpoint: getApiCarListaCuentasPorCobrarDetalladoTO")
    class CuentasPorCobrarTests {

        private static final String ENDPOINT = "/getApiCarListaCuentasPorCobrarDetalladoTO";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createCuentasRequestJson("01", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createCuentasRequestJson("01", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 400 con datos inválidos")
        void shouldReturn400WithInvalidData() throws Exception {
            String requestJson = createCuentasRequestJson("", "", "");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    // ========================================
    // 12. Cuentas por Pagar
    // ========================================
    
    @Nested
    @DisplayName("12. Endpoint: getCarListaCuentasPorPagarDetalladoTO")
    class CuentasPorPagarTests {

        private static final String ENDPOINT = "/getCarListaCuentasPorPagarDetalladoTO";

        @Test
        @DisplayName("Debe retornar 200 OK con token válido y datos válidos")
        void shouldReturn200WithValidTokenAndData() throws Exception {
            String requestJson = createCuentasRequestJson("01", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estadoOperacion").exists());
        }

        @Test
        @DisplayName("Debe retornar 401 sin token de autenticación")
        void shouldReturn401WithoutToken() throws Exception {
            String requestJson = createCuentasRequestJson("01", "2024-01-01", "2024-12-31");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 400 con datos inválidos")
        void shouldReturn400WithInvalidData() throws Exception {
            String requestJson = createCuentasRequestJson("", "", "");

            mockMvc.perform(MockMvcRequestBuilders
                    .post(BIOSUPER_BASE_URL + ENDPOINT)
                    .header("Authorization", validToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }
}
