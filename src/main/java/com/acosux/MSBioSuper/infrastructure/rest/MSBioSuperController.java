/*
 * Controlador REST para operaciones de MSBioSuper.
 * Migrado a arquitectura hexagonal con casos de uso.
 */
package com.acosux.MSBioSuper.infrastructure.rest;

import com.acosux.MSBioSuper.application.dto.*;
import com.acosux.MSBioSuper.application.usecase.*;
import com.acosux.MSBioSuper.domain.model.*;
import com.acosux.MSBioSuper.util.RespuestaWebTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones de MSBioSuper.
 * Proporciona endpoints para consultas de producción, liquidaciones, ventas y cuentas.
 * 
 * Migrado a arquitectura hexagonal:
 * - Usa casos de uso en lugar de servicios (TR-6)
 * - Usa DTOs de Request/Response en lugar de Map<String, Object> (TR-6)
 * - Incluye validación de entrada con @Valid (TR-12)
 * - Mantiene compatibilidad hacia atrás con RespuestaWebTO (FR-1)
 * - Manejo de excepciones centralizado con GlobalExceptionHandler (TR-12)
 * 
 * @author mario
 * @version 2.0 - Migrado a Java 21 y arquitectura hexagonal
 */
@Tag(name = "MSBioSuper", description = "API para gestión de operaciones de producción acuícola")
@RestController
@RequestMapping("/api/v1/biosuperController/")
@SecurityRequirement(name = "bearerAuth")
public class MSBioSuperController {

    // Inyección de casos de uso (arquitectura hexagonal - TR-6)
    private final GetResumenPescaUseCase getResumenPescaUseCase;
    private final GetResumenSiembraUseCase getResumenSiembraUseCase;
    private final GetConsumosDiariosUseCase getConsumosDiariosUseCase;
    private final GetLiquidacionDetalleUseCase getLiquidacionDetalleUseCase;
    private final ListarLiquidacionesUseCase listarLiquidacionesUseCase;
    private final ListarCostosPorPiscinaUseCase listarCostosPorPiscinaUseCase;
    private final ObtenerGramajesUseCase obtenerGramajesUseCase;
    private final GetComprasDetalleUseCase getComprasDetalleUseCase;
    private final ListarVentasUseCase listarVentasUseCase;
    private final GetProductosGeneralUseCase getProductosGeneralUseCase;
    private final GetCuentasPorCobrarUseCase getCuentasPorCobrarUseCase;
    private final GetCuentasPorPagarUseCase getCuentasPorPagarUseCase;

    /**
     * Constructor con inyección de dependencias.
     * Spring inyecta automáticamente todos los casos de uso.
     */
    public MSBioSuperController(
            GetResumenPescaUseCase getResumenPescaUseCase,
            GetResumenSiembraUseCase getResumenSiembraUseCase,
            GetConsumosDiariosUseCase getConsumosDiariosUseCase,
            GetLiquidacionDetalleUseCase getLiquidacionDetalleUseCase,
            ListarLiquidacionesUseCase listarLiquidacionesUseCase,
            ListarCostosPorPiscinaUseCase listarCostosPorPiscinaUseCase,
            ObtenerGramajesUseCase obtenerGramajesUseCase,
            GetComprasDetalleUseCase getComprasDetalleUseCase,
            ListarVentasUseCase listarVentasUseCase,
            GetProductosGeneralUseCase getProductosGeneralUseCase,
            GetCuentasPorCobrarUseCase getCuentasPorCobrarUseCase,
            GetCuentasPorPagarUseCase getCuentasPorPagarUseCase) {
        this.getResumenPescaUseCase = getResumenPescaUseCase;
        this.getResumenSiembraUseCase = getResumenSiembraUseCase;
        this.getConsumosDiariosUseCase = getConsumosDiariosUseCase;
        this.getLiquidacionDetalleUseCase = getLiquidacionDetalleUseCase;
        this.listarLiquidacionesUseCase = listarLiquidacionesUseCase;
        this.listarCostosPorPiscinaUseCase = listarCostosPorPiscinaUseCase;
        this.obtenerGramajesUseCase = obtenerGramajesUseCase;
        this.getComprasDetalleUseCase = getComprasDetalleUseCase;
        this.listarVentasUseCase = listarVentasUseCase;
        this.getProductosGeneralUseCase = getProductosGeneralUseCase;
        this.getCuentasPorCobrarUseCase = getCuentasPorCobrarUseCase;
        this.getCuentasPorPagarUseCase = getCuentasPorPagarUseCase;
    }

    /**
     * Obtiene el resumen de pesca según los criterios especificados.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, sector, fechas, etc.)
     * @return Respuesta con lista de resúmenes de pesca
     */
    @Operation(
        summary = "Obtener resumen de pesca",
        description = "Consulta el resumen de pesca filtrado por empresa, sector, rango de fechas, piscina y corrida. " +
                      "Retorna información detallada sobre biomasa, costos, ventas y rendimiento."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Resumen de pesca obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/getListaResumenPesca")
    public RespuestaWebTO getListaResumenPesca(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para el resumen de pesca", required = true)
            @Valid @RequestBody ResumenPescaRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<ResumenPescaResponse> respues = getResumenPescaUseCase.execute(requestDto);
        if (respues != null && !respues.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setOperacionMensaje("");
            resp.setExtraInfo(respues);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados");
        }
        return resp;
    }

    /**
     * Obtiene el resumen de siembra según los criterios especificados.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, sector, fechas, etc.)
     * @return Respuesta con lista de resúmenes de siembra
     */
    @Operation(
        summary = "Obtener resumen de siembra",
        description = "Consulta el resumen de siembra filtrado por empresa, sector, rango de fechas, piscina y corrida. " +
                      "Retorna información sobre larvas, densidad, laboratorio y costos de siembra."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Resumen de siembra obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/getListaResumenSiembra")
    public RespuestaWebTO getListaResumenSiembra(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para el resumen de siembra", required = true)
            @Valid @RequestBody ResumenSiembraRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<ResumenSiembraResponse> respues = getResumenSiembraUseCase.execute(requestDto);
        if (respues != null && !respues.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setOperacionMensaje("");
            resp.setExtraInfo(respues);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados");
        }
        return resp;
    }

    /**
     * Obtiene los consumos diarios según los criterios especificados.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, sector, fechas, etc.)
     * @return Respuesta con lista de consumos diarios
     */
    @Operation(
        summary = "Obtener consumos diarios",
        description = "Consulta los consumos diarios de alimento balanceado filtrado por empresa, sector y rango de fechas. " +
                      "Retorna información sobre consumo de balanceado, conversión alimenticia y costos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Consumos diarios obtenidos exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/getConsumosDiarios")
    public RespuestaWebTO getConsumosDiarios(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para consumos diarios", required = true)
            @Valid @RequestBody ConsumosDiariosRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<ConsumosDiariosResponse> respues = getConsumosDiariosUseCase.execute(requestDto);
        if (respues != null && !respues.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(respues);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados.");
            resp.setExtraInfo(false);
        }
        return resp;
    }

    /**
     * Obtiene el listado de liquidaciones con detalle por producto.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, fechas, etc.)
     * @return Respuesta con lista de liquidaciones detalladas
     */
    @Operation(
        summary = "Obtener liquidaciones con detalle por producto",
        description = "Consulta las liquidaciones detalladas por producto filtrado por empresa, rango de fechas, " +
                      "sector, piscina, corrida, cliente y tipo. Retorna información sobre productos liquidados, " +
                      "cantidades, precios y totales."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Liquidaciones obtenidas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/getListadoLiquidacionDetalleProducto")
    public RespuestaWebTO getListadoLiquidacionDetalleProducto(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para liquidaciones", required = true)
            @Valid @RequestBody LiquidacionRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<LiquidacionResponse> respues = getLiquidacionDetalleUseCase.execute(requestDto);
        if (respues != null && !respues.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(respues);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados.");
        }
        return resp;
    }

    /**
     * Lista las liquidaciones con detalle.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, fechas, etc.)
     * @return Respuesta con lista de liquidaciones
     */
    @Operation(
        summary = "Listar liquidaciones con detalle",
        description = "Lista todas las liquidaciones con información detallada filtrado por empresa, rango de fechas, " +
                      "sector, piscina, corrida, cliente y tipo."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Liquidaciones listadas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/listarLiquidacionesDetalle")
    public RespuestaWebTO listarLiquidacionesDetalle(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para liquidaciones", required = true)
            @Valid @RequestBody LiquidacionRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<LiquidacionResponse> respues = listarLiquidacionesUseCase.execute(requestDto);
        if (respues != null && !respues.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(respues);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados.");
        }
        return resp;
    }

    /**
     * Lista los costos detallados por piscina.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, sector, etc.)
     * @return Respuesta con lista de costos por piscina
     */
    @Operation(
        summary = "Listar costos por piscina",
        description = "Consulta los costos detallados por piscina y corrida filtrado por empresa, sector, " +
                      "rango de fechas, piscina y corrida. Retorna información sobre costos directos, " +
                      "indirectos y totales por piscina."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Costos por piscina obtenidos exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/listarCostosPorPiscina")
    public RespuestaWebTO listarCostosPorPiscina(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para costos por piscina", required = true)
            @Valid @RequestBody ResumenPescaRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<PrdListaCostosDetalleCorridaTO> respues = listarCostosPorPiscinaUseCase.execute(requestDto);
        if (respues != null && !respues.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(respues);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados.");
        }
        return resp;
    }

    /**
     * Obtiene el listado de gramajes.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, sector, etc.)
     * @return Respuesta con lista de gramajes
     */
    @Operation(
        summary = "Obtener listado de gramajes",
        description = "Consulta el listado de gramajes (pesos promedio) de camarones filtrado por empresa, " +
                      "sector, rango de fechas, piscina y corrida. Retorna información sobre distribución " +
                      "de tamaños y pesos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Gramajes obtenidos exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/obtenerListadoGramajes")
    public RespuestaWebTO obtenerListadoGramajes(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para gramajes", required = true)
            @Valid @RequestBody ResumenPescaRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<PrdListadoGrameajeTO> respues = obtenerGramajesUseCase.execute(requestDto);
        if (respues != null && !respues.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(respues);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se han encontrado resultados.");
        }
        return resp;
    }

    /**
     * Obtiene el listado de compras con detalle por producto.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, fechas, etc.)
     * @return Respuesta con lista de compras detalladas
     */
    @Operation(
        summary = "Obtener compras con detalle por producto",
        description = "Consulta las compras detalladas por producto filtrado por empresa, sector, rango de fechas, " +
                      "piscina y corrida. Retorna información sobre productos comprados, cantidades, " +
                      "precios unitarios y totales."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Compras obtenidas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/getListadoComprasDetalleProducto")
    public RespuestaWebTO getListadoComprasDetalleProducto(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para compras", required = true)
            @Valid @RequestBody ComprasDetalleRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<InvComprasDetalleProductoTO> respues = getComprasDetalleUseCase.execute(requestDto);
        if (respues != null && !respues.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(respues);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados.");
        }
        return resp;
    }

    /**
     * Lista las ventas de BioSuper.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, fechas)
     * @return Respuesta con lista de ventas
     */
    @Operation(
        summary = "Listar ventas de BioSuper",
        description = "Consulta las ventas realizadas filtrado por empresa, sector, rango de fechas, " +
                      "piscina y corrida. Retorna información sobre productos vendidos, clientes, " +
                      "cantidades, precios y totales."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ventas listadas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/listarVentasBioSuper")
    public RespuestaWebTO listarVentasBioSuper(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para ventas", required = true)
            @Valid @RequestBody ResumenPescaRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<InvVentasBioSuperTO> lista = listarVentasUseCase.execute(requestDto);
        if (lista != null && !lista.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(lista);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados.");
        }
        return resp;
    }

    /**
     * Obtiene la lista general de productos.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, bodega, etc.)
     * @return Respuesta con lista de productos
     */
    @Operation(
        summary = "Obtener lista general de productos",
        description = "Consulta el catálogo general de productos filtrado por empresa, bodega y otros criterios. " +
                      "Retorna información sobre código, nombre, categoría, unidad de medida y precios."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Productos obtenidos exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/getListaProductosGeneralTO")
    public RespuestaWebTO getListaProductosGeneralTO(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para productos", required = true)
            @Valid @RequestBody ProductoRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<ProductoResponse> respues = getProductosGeneralUseCase.execute(requestDto);
        if (respues != null && !respues.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(respues);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados.");
        }
        return resp;
    }

    /**
     * Obtiene la lista de cuentas por cobrar detalladas.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, fechas, etc.)
     * @return Respuesta con lista de cuentas por cobrar
     */
    @Operation(
        summary = "Obtener cuentas por cobrar detalladas",
        description = "Consulta las cuentas por cobrar con información detallada filtrado por empresa, " +
                      "rango de fechas y otros criterios. Retorna información sobre clientes, facturas, " +
                      "montos, saldos y fechas de vencimiento."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cuentas por cobrar obtenidas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/getApiCarListaCuentasPorCobrarDetalladoTO")
    public RespuestaWebTO getApiCarListaCuentasPorCobrarDetalladoTO(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para cuentas por cobrar", required = true)
            @Valid @RequestBody CuentasPorCobrarRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<CuentasPorCobrarResponse> respuesta = getCuentasPorCobrarUseCase.execute(requestDto);
        if (respuesta != null && !respuesta.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(respuesta);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados.");
        }
        return resp;
    }

    /**
     * Obtiene la lista de cuentas por pagar detalladas.
     * 
     * @param request Contexto HTTP de la solicitud
     * @param requestDto DTO con los criterios de búsqueda validados (empresa, fechas, etc.)
     * @return Respuesta con lista de cuentas por pagar
     */
    @Operation(
        summary = "Obtener cuentas por pagar detalladas",
        description = "Consulta las cuentas por pagar con información detallada filtrado por empresa, " +
                      "rango de fechas y otros criterios. Retorna información sobre proveedores, facturas, " +
                      "montos, saldos y fechas de vencimiento."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cuentas por pagar obtenidas exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RespuestaWebTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros de solicitud inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado - Token JWT inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/getCarListaCuentasPorPagarDetalladoTO")
    public RespuestaWebTO getCarListaCuentasPorPagarDetalladoTO(
            HttpServletRequest request,
            @Parameter(description = "Criterios de búsqueda para cuentas por pagar", required = true)
            @Valid @RequestBody CuentasPorPagarRequest requestDto) {
        RespuestaWebTO resp = new RespuestaWebTO();
        List<CuentasPorPagarResponse> respuesta = getCuentasPorPagarUseCase.execute(requestDto);
        if (respuesta != null && !respuesta.isEmpty()) {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
            resp.setExtraInfo(respuesta);
        } else {
            resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
            resp.setOperacionMensaje("No se encontraron resultados.");
        }
        return resp;
    }
}
