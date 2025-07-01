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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author BC
 */
@Repository
public class MSBioSuperDaoImpl implements MSBioSuperDao {

    @Autowired
    private GenericSQLDao genericSQLDao;

    @Override
    public List<PrdResumenCorridaTO> getListaResumenCorridaTO(String empresa, String sector, String desde, String hasta, String tipoResumen, boolean resumenHengXin) throws Exception {
        String sql;
        desde = desde == null ? null : "'" + desde + "'";
        hasta = hasta == null ? null : "'" + hasta + "'";
        sector = sector == null ? null : "'" + sector + "'";
        String sqlOrder = resumenHengXin ? " ORDER BY rc_corrida" : "";
        if (tipoResumen.equals("SIEMBRA")) {
            sql = "SELECT * FROM produccion.fun_resumen_corrida_mejorado('" + empresa + "', " + sector + ", 'SIEMBRA', " + desde
                    + ", " + hasta + ")" + sqlOrder;
        } else {
            sql = "SELECT * FROM produccion.fun_resumen_corrida_mejorado('" + empresa + "', " + sector + ", '" + tipoResumen + "', " + desde
                    + ", " + hasta + ")" + sqlOrder;
        }
        return genericSQLDao.obtenerPorSql(sql, PrdResumenCorridaTO.class);
    }

    @Override
    public List<PrdConsumosDiarioMSTO> getConsumosDiariosMicroservice(String empresa, String codigoSector, String codigoBodega, String fechaInicio, String fechaFin, String piscina, String codigoProducto) throws Exception {
        fechaInicio = fechaInicio == null ? null : "'" + fechaInicio + "'";
        fechaFin = fechaFin == null ? null : "'" + fechaFin + "'";
        codigoSector = codigoSector == null ? null : "'" + codigoSector + "'";
        codigoBodega = codigoBodega == null ? null : "'" + codigoBodega + "'";
        piscina = piscina == null ? null : "'" + piscina + "'";
        codigoProducto = codigoProducto == null ? null : "'" + codigoProducto + "'";
        String sql = "SELECT * FROM produccion.fun_consumos_diarios_microservice('" + empresa + "', " + codigoSector + ", " + piscina + ", " + codigoBodega + ", " + codigoProducto + ", " + fechaInicio + ", " + fechaFin + ")";
        return genericSQLDao.obtenerPorSql(sql, PrdConsumosDiarioMSTO.class);
    }

    @Override
    public List<PrdLiquidacionDetalleProductoTO> getListadoLiquidacionDetalleProductoTO(String empresa, String sector, String piscina, String desde, String hasta, String cliente, String talla, String tipo) throws Exception {
        empresa = empresa == null ? empresa : "'" + empresa + "'";
        sector = sector == null ? sector : "'" + sector + "'";
        piscina = piscina == null ? piscina : "'" + piscina + "'";
        desde = desde == null ? desde : "'" + desde + "'";
        hasta = hasta == null ? hasta : "'" + hasta + "'";
        cliente = cliente == null ? cliente : "'" + cliente + "'";
        talla = talla == null ? talla : "'" + talla + "'";
        tipo = tipo == null ? tipo : "'" + tipo + "'";
        String sql = "SELECT * FROM produccion.fun_liquidacion_detalle_producto(" + empresa + "," + sector + ","
                + piscina + "," + desde + ", " + hasta + "," + cliente + "," + talla + "," + tipo + ")";
        return genericSQLDao.obtenerPorSql(sql, PrdLiquidacionDetalleProductoTO.class);
    }

    @Override
    public List<PrdLiquidacionesDetalleTO> listarLiquidacionesDetalle(String empresa, String desde, String hasta) throws Exception {
        empresa = empresa == null ? empresa : "'" + empresa + "'";
        desde = desde == null ? desde : "'" + desde + "'";
        hasta = hasta == null ? hasta : "'" + hasta + "'";
        String sql = "SELECT * FROM produccion.fun_liquidaciones_detalle(" + empresa + "," + desde + ", " + hasta + ")";
        return genericSQLDao.obtenerPorSql(sql, PrdLiquidacionesDetalleTO.class);
    }

    @Override
    public List<PrdListaCostosDetalleCorridaTO> getPrdListaCostosDetalleCorridaTO(String empresa, String sector,
            String piscina, String corrida, String desde, String hasta, String agrupadoPor) throws Exception {
        agrupadoPor = agrupadoPor == null ? agrupadoPor : "'" + agrupadoPor + "'";
        hasta = hasta == null ? null : hasta;
        String sql = "SELECT * FROM produccion.fun_costos_detalle_corrida('" + empresa + "', '" + sector + "', "
                + "'" + piscina + "', " + "'" + corrida + "', " + desde + ", " + hasta + ", " + agrupadoPor + ");";
        return genericSQLDao.obtenerPorSql(sql, PrdListaCostosDetalleCorridaTO.class);
    }

    @Override
    public List<PrdListadoGrameajeTO> getPrdListadoGrameajeTO(String empresa, String sector, String piscina,
            String desde, String hasta) throws Exception {
        piscina = piscina == null ? null : "'" + piscina + "'";
        desde = desde == null ? null : "'" + desde + "'";
        hasta = hasta == null ? null : "'" + hasta + "'";
        String sql = "SELECT * FROM produccion.fun_listado_grameaje('" + empresa + "', '" + sector + "', "
                + piscina + ", " + desde + ", " + hasta + ")";
        return genericSQLDao.obtenerPorSql(sql, PrdListadoGrameajeTO.class);
    }

    @Override
    public List<InvListaProductosGeneralTO> getListaProductosTOWeb(String empresa, String busqueda, String bodega, String categoria, String fecha,
            Integer precio, boolean incluirInactivos, boolean limite, boolean codigo, String buscarPorcodigo, String tipo) throws Exception {
        String porcionSql = " where true ";
        porcionSql = codigo ? porcionSql + " AND lpsp_codigo_principal='" + busqueda + "' " : porcionSql;
        if (categoria == null) {
            categoria = "";
        }
        if (buscarPorcodigo == null || buscarPorcodigo.equals("")) {
            buscarPorcodigo = null;
        } else {
            buscarPorcodigo = "'" + buscarPorcodigo + "'";
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

        porcionSql = tipo != null ? porcionSql + " AND lpsp_tipo LIKE '%" + tipo + "%' " : porcionSql;

        if (limite) {
            porcionSql = porcionSql + "LIMIT 1000 ";
        }
        fecha = fecha == null ? null
                : fecha.isEmpty() ? null : fecha.substring(0, 1).compareTo("'") == 0 ? fecha : "'" + fecha + "'";

        String sql = "SELECT lpsp_codigo_principal, lpsp_nombre, "
                + "lpsp_categoria, lpsp_empaque, lpsp_saldo, lpsp_ultimo_costo, lpsp_costo_promedio, lpsp_factor_conversion, lpsp_medida, "
                + "lpsp_cantidad, lpsp_base_imponible, lpsp_precio, lpsp_descuento, lpsp_margen_utilidad, lpsp_precio2, lpsp_precio3,"
                + "lpsp_iva , lpsp_inactivo, lpsp_tipo, lpsp_precio_fijo, lpsp_exigir_serie,lpsp_costo_referencial,lpsp_imagenes, "
                + "lpsp_credito_tributario, lpsp_ecommerce,lpsp_tipo_tipo_producto, "
                + "lpsp_porcentaje_ice, lpsp_tarifa_fija_ice, lpsp_ice_codigo, lpsp_exigir_lote, lpsp_subcategoria, lpsp_cuenta_inventario, "
                + "lpsp_nombre_inventario, lpsp_cuenta_costo_consumo, lpsp_nombre_costo_consumo, lpsp_pi_codigo, lpsp_pi_porcentaje, id "
                + "FROM inventario.fun_lista_productos_saldos_precios_web('" + empresa + "', '" + bodega + "'," + buscarPorcodigo + ", '"
                + categoria + "', '" + busqueda + "', " + fecha + ", " + precio + ", " + incluirInactivos + ")" + porcionSql + ";";
        return genericSQLDao.obtenerPorSql(sql, InvListaProductosGeneralTO.class);
    }

    @Override
    public List<InvComprasDetalleProductoTO> obtenerListadoComprasDetalleProducto(String empresa, String fechaDesde, String fechaHasta, String sector, String piscina, String bodega, Boolean incluirIMB) throws Exception {
        if (sector != null && !sector.equals("")) {
            sector = "'" + sector + "'";
        } else {
            sector = null;
        }
        if (piscina != null && !piscina.equals("")) {
            piscina = "'" + piscina + "'";
        } else {
            piscina = null;
        }
        if (bodega != null && !bodega.equals("")) {
            bodega = "'" + bodega + "'";
        } else {
            bodega = null;
        }
        String sql = "SELECT * FROM inventario.fun_compras_detalle_producto('" + empresa + "'," + sector + "," + piscina + "," + null + ",'" + fechaDesde + "', '" + fechaHasta + "'," + bodega + "," + incluirIMB + ")";

        return genericSQLDao.obtenerPorSql(sql, InvComprasDetalleProductoTO.class
        );
    }

    @Override
    public List<InvVentasBioSuperTO> getListaAnxVentaElectronicaTO(String empresa, String fechaDesde, String fechaHasta,
            String tipoDocumento) throws Exception {
        empresa = empresa == null ? empresa : "'" + empresa + "'";
        fechaDesde = fechaDesde == null ? fechaDesde : "'" + fechaDesde + "'";
        fechaHasta = fechaHasta == null ? fechaHasta : "'" + fechaHasta + "'";
        String sql = "SELECT * FROM  anexo.fun_ventas_electronicas_autorizadas_listado_biosuper(" + empresa + ", " + fechaDesde + ", " + fechaHasta + ", '18')";
        List<InvVentasBioSuperTO> list = genericSQLDao.obtenerPorSql(sql, InvVentasBioSuperTO.class);
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
        hasta = hasta == null ? null : "'" + hasta + "'";
        String sql = "SELECT * FROM cartera.fun_cuentas_por_cobrar_detallado_api(" + "'" + empresa + "', " + hasta + ")";
        return genericSQLDao.obtenerPorSql(sql, CuentasPorCobrarDetalladoApiTO.class);
    }

    @Override
    public List<CuentasPorPagarDetalladoTO> getCarListaCuentasPorPagarDetalladoTO(String empresa, String sector,
            String proveedor, String desde, String hasta, boolean excluirAprobadas, boolean incluirCheques, String formatoMensual) throws Exception {
        desde = desde == null ? null : "'" + desde + "'";
        hasta = hasta == null ? null : "'" + hasta + "'";
        sector = sector == null ? sector : "'" + sector + "'";
        proveedor = proveedor == null ? proveedor : "'" + proveedor + "'";
        formatoMensual = formatoMensual == null ? formatoMensual : "'" + formatoMensual + "'";
        String sql = "SELECT * FROM cartera.fun_cuentas_por_pagar_detallado('" + empresa + "', " + sector + ", "
                + proveedor + ", " + desde + ", " + hasta + ", " + excluirAprobadas + ", " + incluirCheques + "," + formatoMensual + ")";
        return genericSQLDao.obtenerPorSql(sql, CuentasPorPagarDetalladoTO.class);
    }

}
