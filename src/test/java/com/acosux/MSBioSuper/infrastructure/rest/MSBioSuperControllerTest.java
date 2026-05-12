package com.acosux.MSBioSuper.infrastructure.rest;

import com.acosux.MSBioSuper.application.dto.*;
import com.acosux.MSBioSuper.application.usecase.*;
import com.acosux.MSBioSuper.domain.model.*;
import com.acosux.MSBioSuper.util.RespuestaWebTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para MSBioSuperController
 * 
 * Cobertura:
 * - Endpoints de resumen de pesca y siembra
 * - Endpoints de consumos diarios
 * - Endpoints de liquidaciones
 * - Endpoints de costos y gramajes
 * - Endpoints de compras y ventas
 * - Endpoints de productos y cuentas
 * - Manejo de respuestas exitosas y advertencias
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MSBioSuperController - Pruebas de Endpoints REST")
class MSBioSuperControllerTest {

    @Mock
    private GetResumenPescaUseCase getResumenPescaUseCase;
    
    @Mock
    private GetResumenSiembraUseCase getResumenSiembraUseCase;
    
    @Mock
    private GetConsumosDiariosUseCase getConsumosDiariosUseCase;
    
    @Mock
    private GetLiquidacionDetalleUseCase getLiquidacionDetalleUseCase;
    
    @Mock
    private ListarLiquidacionesUseCase listarLiquidacionesUseCase;
    
    @Mock
    private ListarCostosPorPiscinaUseCase listarCostosPorPiscinaUseCase;
    
    @Mock
    private ObtenerGramajesUseCase obtenerGramajesUseCase;
    
    @Mock
    private GetComprasDetalleUseCase getComprasDetalleUseCase;
    
    @Mock
    private ListarVentasUseCase listarVentasUseCase;
    
    @Mock
    private GetProductosGeneralUseCase getProductosGeneralUseCase;
    
    @Mock
    private GetCuentasPorCobrarUseCase getCuentasPorCobrarUseCase;
    
    @Mock
    private GetCuentasPorPagarUseCase getCuentasPorPagarUseCase;

    @InjectMocks
    private MSBioSuperController controller;

    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockRequest = new MockHttpServletRequest();
    }

    @Nested
    @DisplayName("Endpoints de Resumen de Pesca")
    class ResumenPescaTests {

        @Test
        @DisplayName("Debe retornar resumen de pesca exitosamente cuando hay datos")
        void debeRetornarResumenPescaExitosamente() {
            // Given
            ResumenPescaRequest request = new ResumenPescaRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            List<ResumenPescaResponse> mockResponse = List.of(
                new ResumenPescaResponse("PISCINA1", "CORRIDA1", 1000.0, 500.0, 2.0)
            );
            
            when(getResumenPescaUseCase.execute(any(ResumenPescaRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.getListaResumenPesca(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
            assertNotNull(response.getExtraInfo());
            verify(getResumenPescaUseCase, times(1)).execute(request);
        }

        @Test
        @DisplayName("Debe retornar advertencia cuando no hay datos de pesca")
        void debeRetornarAdvertenciaCuandoNoHayDatos() {
            // Given
            ResumenPescaRequest request = new ResumenPescaRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            when(getResumenPescaUseCase.execute(any(ResumenPescaRequest.class)))
                .thenReturn(Collections.emptyList());

            // When
            RespuestaWebTO response = controller.getListaResumenPesca(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor(), 
                response.getEstadoOperacion());
            assertEquals("No se encontraron resultados", response.getOperacionMensaje());
        }

        @Test
        @DisplayName("Debe retornar advertencia cuando la lista es nula")
        void debeRetornarAdvertenciaCuandoListaEsNula() {
            // Given
            ResumenPescaRequest request = new ResumenPescaRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            when(getResumenPescaUseCase.execute(any(ResumenPescaRequest.class)))
                .thenReturn(null);

            // When
            RespuestaWebTO response = controller.getListaResumenPesca(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor(), 
                response.getEstadoOperacion());
        }
    }

    @Nested
    @DisplayName("Endpoints de Resumen de Siembra")
    class ResumenSiembraTests {

        @Test
        @DisplayName("Debe retornar resumen de siembra exitosamente cuando hay datos")
        void debeRetornarResumenSiembraExitosamente() {
            // Given
            ResumenSiembraRequest request = new ResumenSiembraRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            List<ResumenSiembraResponse> mockResponse = List.of(
                new ResumenSiembraResponse("PISCINA1", "CORRIDA1", 50000, 100.0, "LAB1")
            );
            
            when(getResumenSiembraUseCase.execute(any(ResumenSiembraRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.getListaResumenSiembra(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
            assertNotNull(response.getExtraInfo());
            verify(getResumenSiembraUseCase, times(1)).execute(request);
        }

        @Test
        @DisplayName("Debe retornar advertencia cuando no hay datos de siembra")
        void debeRetornarAdvertenciaCuandoNoHayDatos() {
            // Given
            ResumenSiembraRequest request = new ResumenSiembraRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            when(getResumenSiembraUseCase.execute(any(ResumenSiembraRequest.class)))
                .thenReturn(new ArrayList<>());

            // When
            RespuestaWebTO response = controller.getListaResumenSiembra(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor(), 
                response.getEstadoOperacion());
        }
    }

    @Nested
    @DisplayName("Endpoints de Consumos Diarios")
    class ConsumosDiariosTests {

        @Test
        @DisplayName("Debe retornar consumos diarios exitosamente")
        void debeRetornarConsumosDiariosExitosamente() {
            // Given
            ConsumosDiariosRequest request = new ConsumosDiariosRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31"
            );
            
            List<ConsumosDiariosResponse> mockResponse = List.of(
                new ConsumosDiariosResponse("2024-01-15", 100.0, 1.5, 150.0)
            );
            
            when(getConsumosDiariosUseCase.execute(any(ConsumosDiariosRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.getConsumosDiarios(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
            assertNotNull(response.getExtraInfo());
        }

        @Test
        @DisplayName("Debe retornar advertencia cuando no hay consumos")
        void debeRetornarAdvertenciaCuandoNoHayConsumos() {
            // Given
            ConsumosDiariosRequest request = new ConsumosDiariosRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31"
            );
            
            when(getConsumosDiariosUseCase.execute(any(ConsumosDiariosRequest.class)))
                .thenReturn(Collections.emptyList());

            // When
            RespuestaWebTO response = controller.getConsumosDiarios(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor(), 
                response.getEstadoOperacion());
            assertEquals("No se encontraron resultados.", response.getOperacionMensaje());
            assertEquals(false, response.getExtraInfo());
        }
    }

    @Nested
    @DisplayName("Endpoints de Liquidaciones")
    class LiquidacionesTests {

        @Test
        @DisplayName("Debe retornar liquidaciones con detalle exitosamente")
        void debeRetornarLiquidacionesDetalleExitosamente() {
            // Given
            LiquidacionRequest request = new LiquidacionRequest(
                "01", "2024-01-01", "2024-01-31", "SECTOR1", "PISCINA1", 
                "CORRIDA1", "CLIENTE1", "TIPO1"
            );
            
            List<LiquidacionResponse> mockResponse = List.of(
                new LiquidacionResponse("LIQ001", "PROD1", 100.0, 10.0, 1000.0)
            );
            
            when(getLiquidacionDetalleUseCase.execute(any(LiquidacionRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.getListadoLiquidacionDetalleProducto(
                mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
            verify(getLiquidacionDetalleUseCase, times(1)).execute(request);
        }

        @Test
        @DisplayName("Debe listar liquidaciones exitosamente")
        void debeListarLiquidacionesExitosamente() {
            // Given
            LiquidacionRequest request = new LiquidacionRequest(
                "01", "2024-01-01", "2024-01-31", "SECTOR1", "PISCINA1", 
                "CORRIDA1", "CLIENTE1", "TIPO1"
            );
            
            List<LiquidacionResponse> mockResponse = List.of(
                new LiquidacionResponse("LIQ001", "PROD1", 100.0, 10.0, 1000.0)
            );
            
            when(listarLiquidacionesUseCase.execute(any(LiquidacionRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.listarLiquidacionesDetalle(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
        }

        @Test
        @DisplayName("Debe retornar advertencia cuando no hay liquidaciones")
        void debeRetornarAdvertenciaCuandoNoHayLiquidaciones() {
            // Given
            LiquidacionRequest request = new LiquidacionRequest(
                "01", "2024-01-01", "2024-01-31", "SECTOR1", "PISCINA1", 
                "CORRIDA1", "CLIENTE1", "TIPO1"
            );
            
            when(getLiquidacionDetalleUseCase.execute(any(LiquidacionRequest.class)))
                .thenReturn(null);

            // When
            RespuestaWebTO response = controller.getListadoLiquidacionDetalleProducto(
                mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor(), 
                response.getEstadoOperacion());
        }
    }

    @Nested
    @DisplayName("Endpoints de Costos y Gramajes")
    class CostosGramajesTests {

        @Test
        @DisplayName("Debe listar costos por piscina exitosamente")
        void debeListarCostosPorPiscinaExitosamente() {
            // Given
            ResumenPescaRequest request = new ResumenPescaRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            List<PrdListaCostosDetalleCorridaTO> mockResponse = List.of(
                new PrdListaCostosDetalleCorridaTO()
            );
            
            when(listarCostosPorPiscinaUseCase.execute(any(ResumenPescaRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.listarCostosPorPiscina(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
        }

        @Test
        @DisplayName("Debe obtener gramajes exitosamente")
        void debeObtenerGramajesExitosamente() {
            // Given
            ResumenPescaRequest request = new ResumenPescaRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            List<PrdListadoGrameajeTO> mockResponse = List.of(
                new PrdListadoGrameajeTO()
            );
            
            when(obtenerGramajesUseCase.execute(any(ResumenPescaRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.obtenerListadoGramajes(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
        }

        @Test
        @DisplayName("Debe retornar advertencia cuando no hay gramajes")
        void debeRetornarAdvertenciaCuandoNoHayGramajes() {
            // Given
            ResumenPescaRequest request = new ResumenPescaRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            when(obtenerGramajesUseCase.execute(any(ResumenPescaRequest.class)))
                .thenReturn(Collections.emptyList());

            // When
            RespuestaWebTO response = controller.obtenerListadoGramajes(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor(), 
                response.getEstadoOperacion());
            assertEquals("No se han encontrado resultados.", response.getOperacionMensaje());
        }
    }

    @Nested
    @DisplayName("Endpoints de Compras y Ventas")
    class ComprasVentasTests {

        @Test
        @DisplayName("Debe obtener compras con detalle exitosamente")
        void debeObtenerComprasDetalleExitosamente() {
            // Given
            ResumenPescaRequest request = new ResumenPescaRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            List<InvComprasDetalleProductoTO> mockResponse = List.of(
                new InvComprasDetalleProductoTO()
            );
            
            when(getComprasDetalleUseCase.execute(any(ResumenPescaRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.getListadoComprasDetalleProducto(
                mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
        }

        @Test
        @DisplayName("Debe listar ventas exitosamente")
        void debeListarVentasExitosamente() {
            // Given
            ResumenPescaRequest request = new ResumenPescaRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            List<InvVentasBioSuperTO> mockResponse = List.of(
                new InvVentasBioSuperTO()
            );
            
            when(listarVentasUseCase.execute(any(ResumenPescaRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.listarVentasBioSuper(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
        }

        @Test
        @DisplayName("Debe retornar advertencia cuando no hay ventas")
        void debeRetornarAdvertenciaCuandoNoHayVentas() {
            // Given
            ResumenPescaRequest request = new ResumenPescaRequest(
                "01", "SECTOR1", "2024-01-01", "2024-01-31", "PISCINA1", "CORRIDA1"
            );
            
            when(listarVentasUseCase.execute(any(ResumenPescaRequest.class)))
                .thenReturn(null);

            // When
            RespuestaWebTO response = controller.listarVentasBioSuper(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor(), 
                response.getEstadoOperacion());
        }
    }

    @Nested
    @DisplayName("Endpoints de Productos y Cuentas")
    class ProductosCuentasTests {

        @Test
        @DisplayName("Debe obtener productos generales exitosamente")
        void debeObtenerProductosGeneralesExitosamente() {
            // Given
            ProductoRequest request = new ProductoRequest("01", "BODEGA1", "CATEGORIA1");
            
            List<ProductoResponse> mockResponse = List.of(
                new ProductoResponse("PROD001", "Producto Test", "CATEGORIA1", "UND", 10.0)
            );
            
            when(getProductosGeneralUseCase.execute(any(ProductoRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.getListaProductosGeneralTO(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
        }

        @Test
        @DisplayName("Debe obtener cuentas por cobrar exitosamente")
        void debeObtenerCuentasPorCobrarExitosamente() {
            // Given
            CuentasPorCobrarRequest request = new CuentasPorCobrarRequest(
                "01", "2024-01-01", "2024-01-31", "CLIENTE1"
            );
            
            List<CuentasPorCobrarResponse> mockResponse = List.of(
                new CuentasPorCobrarResponse("FACT001", "CLIENTE1", 1000.0, 500.0, "2024-02-15")
            );
            
            when(getCuentasPorCobrarUseCase.execute(any(CuentasPorCobrarRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.getApiCarListaCuentasPorCobrarDetalladoTO(
                mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
        }

        @Test
        @DisplayName("Debe obtener cuentas por pagar exitosamente")
        void debeObtenerCuentasPorPagarExitosamente() {
            // Given
            CuentasPorPagarRequest request = new CuentasPorPagarRequest(
                "01", "2024-01-01", "2024-01-31", "PROVEEDOR1"
            );
            
            List<CuentasPorPagarResponse> mockResponse = List.of(
                new CuentasPorPagarResponse("FACT001", "PROVEEDOR1", 2000.0, 1000.0, "2024-02-15")
            );
            
            when(getCuentasPorPagarUseCase.execute(any(CuentasPorPagarRequest.class)))
                .thenReturn(mockResponse);

            // When
            RespuestaWebTO response = controller.getCarListaCuentasPorPagarDetalladoTO(
                mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor(), 
                response.getEstadoOperacion());
        }

        @Test
        @DisplayName("Debe retornar advertencia cuando no hay productos")
        void debeRetornarAdvertenciaCuandoNoHayProductos() {
            // Given
            ProductoRequest request = new ProductoRequest("01", "BODEGA1", "CATEGORIA1");
            
            when(getProductosGeneralUseCase.execute(any(ProductoRequest.class)))
                .thenReturn(Collections.emptyList());

            // When
            RespuestaWebTO response = controller.getListaProductosGeneralTO(mockRequest, request);

            // Then
            assertNotNull(response);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor(), 
                response.getEstadoOperacion());
        }
    }
}
