/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.acosux.MSBiosuper.dao;

import com.acosux.MSBiosuper.util.dao.GenericSQLDao;
import com.acosux.MSBiosuper.util.entity.CuentasPorCobrarDetalladoApiTO;
import com.acosux.MSBiosuper.util.entity.CuentasPorPagarDetalladoTO;
import com.acosux.MSBiosuper.util.entity.InvComprasDetalleProductoTO;
import com.acosux.MSBiosuper.util.entity.InvListaProductosGeneralTO;
import com.acosux.MSBiosuper.util.entity.InvVentasBioSuperTO;
import com.acosux.MSBiosuper.util.entity.InvVentasDetalleBioSuperTO;
import com.acosux.MSBiosuper.util.entity.InvVentasLiquidacionBioSuperTO;
import com.acosux.MSBiosuper.util.entity.PrdConsumosDiarioMSTO;
import com.acosux.MSBiosuper.util.entity.PrdLiquidacionDetalleProductoTO;
import com.acosux.MSBiosuper.util.entity.PrdLiquidacionesDetalleTO;
import com.acosux.MSBiosuper.util.entity.PrdListaCostosDetalleCorridaTO;
import com.acosux.MSBiosuper.util.entity.PrdListadoGrameajeTO;
import com.acosux.MSBiosuper.util.entity.PrdResumenCorridaTO;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author BC
 */
@Repository
public class MSBioSuperDaoImpl implements MSBioSuperDao {

    /*
     * Todas las funciones se llaman en el esquema integraciones (US-240) con parámetros enlazados
     * (US-218). Para no cambiar lo que recibe la función:
     * - Los parámetros que antes iban siempre entre comillas ('" + x + "'), aunque x fuera null, se
     *   enlazan con String.valueOf(x): un null sigue llegando como el texto 'null', no como NULL.
     * - Cada parámetro lleva CAST al tipo de la firma: un NULL enlazado no tiene tipo y las fechas
     *   llegan como texto.
     */
    @Autowired
    private GenericSQLDao genericSQLDao;

    @Override
    public List<PrdResumenCorridaTO> getListaResumenCorridaTO(String empresa, String sector, String desde, String hasta, String tipoResumen, boolean resumenHengXin) throws Exception {
        String sqlOrder = resumenHengXin ? " ORDER BY rc_corrida" : "";
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("empresa", String.valueOf(empresa));
        parametros.put("sector", sector);
        // Un tipoResumen null sigue fallando aquí (NullPointerException), como antes.
        parametros.put("tipoResumen", tipoResumen.equals("SIEMBRA") ? "SIEMBRA" : tipoResumen);
        parametros.put("desde", desde);
        parametros.put("hasta", hasta);
        String sql = "SELECT * FROM integraciones.fun_resumen_corrida_mejorado(CAST(:empresa AS text), CAST(:sector AS text), "
                + "CAST(:tipoResumen AS text), CAST(:desde AS date), CAST(:hasta AS date))" + sqlOrder;
        return genericSQLDao.obtenerPorSql(sql, PrdResumenCorridaTO.class, parametros);
    }

    @Override
    public List<PrdConsumosDiarioMSTO> getConsumosDiariosMicroservice(String empresa, String codigoSector, String codigoBodega, String fechaInicio, String fechaFin, String piscina, String codigoProducto) throws Exception {
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("empresa", String.valueOf(empresa));
        parametros.put("codigoSector", codigoSector);
        parametros.put("piscina", piscina);
        parametros.put("codigoBodega", codigoBodega);
        parametros.put("codigoProducto", codigoProducto);
        parametros.put("fechaInicio", fechaInicio);
        parametros.put("fechaFin", fechaFin);
        String sql = "SELECT * FROM integraciones.fun_consumos_diarios_microservice(CAST(:empresa AS text), CAST(:codigoSector AS text), "
                + "CAST(:piscina AS text), CAST(:codigoBodega AS text), CAST(:codigoProducto AS text), CAST(:fechaInicio AS date), "
                + "CAST(:fechaFin AS date))";
        return genericSQLDao.obtenerPorSql(sql, PrdConsumosDiarioMSTO.class, parametros);
    }

    @Override
    public List<PrdLiquidacionDetalleProductoTO> getListadoLiquidacionDetalleProductoTO(String empresa, String sector, String piscina, String desde, String hasta, String cliente, String talla, String tipo) throws Exception {
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("empresa", empresa);
        parametros.put("sector", sector);
        parametros.put("piscina", piscina);
        parametros.put("desde", desde);
        parametros.put("hasta", hasta);
        parametros.put("cliente", cliente);
        parametros.put("talla", talla);
        parametros.put("tipo", tipo);
        String sql = "SELECT * FROM integraciones.fun_liquidacion_detalle_producto(CAST(:empresa AS text), CAST(:sector AS text), "
                + "CAST(:piscina AS text), CAST(:desde AS date), CAST(:hasta AS date), CAST(:cliente AS text), CAST(:talla AS text), "
                + "CAST(:tipo AS text))";
        return genericSQLDao.obtenerPorSql(sql, PrdLiquidacionDetalleProductoTO.class, parametros);
    }

    @Override
    public List<PrdLiquidacionesDetalleTO> listarLiquidacionesDetalle(String empresa, String desde, String hasta) throws Exception {
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("empresa", empresa);
        parametros.put("desde", desde);
        parametros.put("hasta", hasta);
        String sql = "SELECT * FROM integraciones.fun_liquidaciones_detalle(CAST(:empresa AS text), CAST(:desde AS date), "
                + "CAST(:hasta AS date))";
        return genericSQLDao.obtenerPorSql(sql, PrdLiquidacionesDetalleTO.class, parametros);
    }

    @Override
    public List<PrdListaCostosDetalleCorridaTO> getPrdListaCostosDetalleCorridaTO(String empresa, String sector,
            String piscina, String corrida, String desde, String hasta, String agrupadoPor) throws Exception {
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("empresa", String.valueOf(empresa));
        parametros.put("sector", String.valueOf(sector));
        parametros.put("piscina", String.valueOf(piscina));
        parametros.put("corrida", String.valueOf(corrida));
        // desde y hasta iban sin comillas: el cliente manda null o la fecha ya entre comillas.
        parametros.put("desde", valorDeLiteral(desde));
        parametros.put("hasta", valorDeLiteral(hasta));
        parametros.put("agrupadoPor", agrupadoPor);
        String sql = "SELECT * FROM integraciones.fun_costos_detalle_corrida(CAST(:empresa AS text), CAST(:sector AS text), "
                + "CAST(:piscina AS text), CAST(:corrida AS text), CAST(:desde AS date), CAST(:hasta AS date), "
                + "CAST(:agrupadoPor AS text))";
        return genericSQLDao.obtenerPorSql(sql, PrdListaCostosDetalleCorridaTO.class, parametros);
    }

    @Override
    public List<PrdListadoGrameajeTO> getPrdListadoGrameajeTO(String empresa, String sector, String piscina,
            String desde, String hasta) throws Exception {
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("empresa", String.valueOf(empresa));
        parametros.put("sector", String.valueOf(sector));
        parametros.put("piscina", piscina);
        parametros.put("desde", desde);
        parametros.put("hasta", hasta);
        String sql = "SELECT * FROM integraciones.fun_listado_grameaje(CAST(:empresa AS text), CAST(:sector AS text), "
                + "CAST(:piscina AS text), CAST(:desde AS date), CAST(:hasta AS date))";
        return genericSQLDao.obtenerPorSql(sql, PrdListadoGrameajeTO.class, parametros);
    }

    @Override
    public List<InvListaProductosGeneralTO> getListaProductosTOWeb(String empresa, String busqueda, String bodega, String categoria, String fecha,
            Integer precio, boolean incluirInactivos, boolean limite, boolean codigo, String buscarPorcodigo, String tipo) throws Exception {
        Map<String, Object> parametros = new LinkedHashMap<>();
        String porcionSql = " where true ";
        if (codigo) {
            porcionSql = porcionSql + " AND lpsp_codigo_principal = CAST(:codigoPrincipal AS text) ";
            // Antes de reemplazar null por "": un null se comparaba con el texto 'null'.
            parametros.put("codigoPrincipal", String.valueOf(busqueda));
        }
        if (categoria == null) {
            categoria = "";
        }
        if (buscarPorcodigo == null || buscarPorcodigo.equals("")) {
            buscarPorcodigo = null;
        }
        if (busqueda == null) {
            busqueda = "";
        }
        if (bodega == null) {
            bodega = "";
        }
        if (precio == null) {
            precio = 1;
        }

        if (tipo != null) {
            porcionSql = porcionSql + " AND lpsp_tipo LIKE '%' || CAST(:tipo AS text) || '%' ";
            parametros.put("tipo", tipo);
        }

        if (limite) {
            porcionSql = porcionSql + "LIMIT 1000 ";
        }
        // Una fecha que ya venía entre comillas se usaba tal cual; si no, se ponía entre comillas.
        fecha = fecha == null ? null
                : fecha.isEmpty() ? null : fecha.substring(0, 1).compareTo("'") == 0 ? valorDeLiteral(fecha) : fecha;

        parametros.put("empresa", String.valueOf(empresa));
        parametros.put("bodega", bodega);
        parametros.put("buscarPorcodigo", buscarPorcodigo);
        parametros.put("categoria", categoria);
        parametros.put("busqueda", busqueda);
        parametros.put("fecha", fecha);
        parametros.put("precio", precio);
        parametros.put("incluirInactivos", incluirInactivos);
        String sql = "SELECT lpsp_codigo_principal, lpsp_nombre, "
                + "lpsp_categoria, lpsp_empaque, lpsp_saldo, lpsp_ultimo_costo, lpsp_costo_promedio, lpsp_factor_conversion, lpsp_medida, "
                + "lpsp_cantidad, lpsp_base_imponible, lpsp_precio, lpsp_descuento, lpsp_margen_utilidad, lpsp_precio2, lpsp_precio3,"
                + "lpsp_iva , lpsp_inactivo, lpsp_tipo, lpsp_precio_fijo, lpsp_exigir_serie,lpsp_costo_referencial,lpsp_imagenes, "
                + "lpsp_credito_tributario, lpsp_ecommerce,lpsp_tipo_tipo_producto, "
                + "lpsp_porcentaje_ice, lpsp_tarifa_fija_ice, lpsp_ice_codigo, lpsp_exigir_lote, lpsp_subcategoria, lpsp_cuenta_inventario, "
                + "lpsp_nombre_inventario, lpsp_cuenta_costo_consumo, lpsp_nombre_costo_consumo, lpsp_pi_codigo, lpsp_pi_porcentaje, id "
                + "FROM integraciones.fun_lista_productos_saldos_precios_web(CAST(:empresa AS text), CAST(:bodega AS text), "
                + "CAST(:buscarPorcodigo AS text), CAST(:categoria AS text), CAST(:busqueda AS text), CAST(:fecha AS date), "
                + "CAST(:precio AS integer), CAST(:incluirInactivos AS boolean))" + porcionSql + ";";
        return genericSQLDao.obtenerPorSql(sql, InvListaProductosGeneralTO.class, parametros);
    }

    @Override
    public List<InvComprasDetalleProductoTO> obtenerListadoComprasDetalleProducto(String empresa, String fechaDesde, String fechaHasta, String sector, String piscina, String bodega, Boolean incluirIMB) throws Exception {
        if (sector == null || sector.equals("")) {
            sector = null;
        }
        if (piscina == null || piscina.equals("")) {
            piscina = null;
        }
        if (bodega == null || bodega.equals("")) {
            bodega = null;
        }
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("empresa", String.valueOf(empresa));
        parametros.put("sector", sector);
        parametros.put("piscina", piscina);
        parametros.put("fechaDesde", String.valueOf(fechaDesde));
        parametros.put("fechaHasta", String.valueOf(fechaHasta));
        parametros.put("bodega", bodega);
        parametros.put("incluirIMB", incluirIMB);
        String sql = "SELECT * FROM integraciones.fun_compras_detalle_producto(CAST(:empresa AS text), CAST(:sector AS text), "
                + "CAST(:piscina AS text), CAST(NULL AS text), CAST(NULL AS text), CAST(:fechaDesde AS date), CAST(:fechaHasta AS date), "
                + "CAST(:bodega AS text), CAST(:incluirIMB AS boolean))";

        return genericSQLDao.obtenerPorSql(sql, InvComprasDetalleProductoTO.class, parametros);
    }

    @Override
    public List<InvVentasBioSuperTO> getListaAnxVentaElectronicaTO(String empresa, String fechaDesde, String fechaHasta,
            String tipoDocumento) throws Exception {
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("empresa", empresa);
        parametros.put("fechaDesde", fechaDesde);
        parametros.put("fechaHasta", fechaHasta);
        String sql = "SELECT * FROM integraciones.fun_ventas_electronicas_autorizadas_listado_api(CAST(:empresa AS text), "
                + "CAST(:fechaDesde AS date), CAST(:fechaHasta AS date), '18')";
        List<InvVentasBioSuperTO> list = genericSQLDao.obtenerPorSql(sql, InvVentasBioSuperTO.class, parametros);
        if (list != null && !list.isEmpty()) {
            for (InvVentasBioSuperTO venta : list) {
                if (venta.getInvVentasDetalleBioSuperTO() != null && !venta.getInvVentasDetalleBioSuperTO().isEmpty()) {
                    for (InvVentasDetalleBioSuperTO detalle : venta.getInvVentasDetalleBioSuperTO()) {
                        detalle.setInvVentas(new InvVentasBioSuperTO(venta.getInvVentasPK()));
                    }
                }
                if (venta.getInvVentasLiquidacionBioSuperTO() != null && !venta.getInvVentasLiquidacionBioSuperTO().isEmpty()) {
                    for (InvVentasLiquidacionBioSuperTO liquidacion : venta.getInvVentasLiquidacionBioSuperTO()) {
                        liquidacion.setInvVentas(new InvVentasBioSuperTO(venta.getInvVentasPK()));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<CuentasPorCobrarDetalladoApiTO> getApiCarListaCuentasPorCobrarDetalladoTO(String empresa, String hasta) throws Exception {
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("empresa", String.valueOf(empresa));
        parametros.put("hasta", hasta);
        String sql = "SELECT * FROM integraciones.fun_cuentas_por_cobrar_detallado_api(CAST(:empresa AS text), CAST(:hasta AS date))";
        return genericSQLDao.obtenerPorSql(sql, CuentasPorCobrarDetalladoApiTO.class, parametros);
    }

    @Override
    public List<CuentasPorPagarDetalladoTO> getCarListaCuentasPorPagarDetalladoTO(String empresa, String sector,
            String proveedor, String desde, String hasta, boolean excluirAprobadas, boolean incluirCheques, String formatoMensual) throws Exception {
        Map<String, Object> parametros = new LinkedHashMap<>();
        parametros.put("empresa", String.valueOf(empresa));
        parametros.put("sector", sector);
        parametros.put("proveedor", proveedor);
        parametros.put("desde", desde);
        parametros.put("hasta", hasta);
        parametros.put("excluirAprobadas", excluirAprobadas);
        parametros.put("incluirCheques", incluirCheques);
        parametros.put("formatoMensual", formatoMensual);
        String sql = "SELECT * FROM integraciones.fun_cuentas_por_pagar_detallado(CAST(:empresa AS text), CAST(:sector AS text), "
                + "CAST(:proveedor AS text), CAST(:desde AS date), CAST(:hasta AS date), CAST(:excluirAprobadas AS boolean), "
                + "CAST(:incluirCheques AS boolean), CAST(:formatoMensual AS text))";
        return genericSQLDao.obtenerPorSql(sql, CuentasPorPagarDetalladoTO.class, parametros);
    }

    /**
     * Valor de un parámetro que antes se pegaba al SQL tal cual, sin comillas: null o "null" eran
     * NULL y 'texto' era el literal texto.
     */
    private static String valorDeLiteral(String literal) {
        if (literal == null || literal.equalsIgnoreCase("null")) {
            return null;
        }
        if (literal.length() >= 2 && literal.startsWith("'") && literal.endsWith("'")) {
            return literal.substring(1, literal.length() - 1);
        }
        return literal;
    }

}
