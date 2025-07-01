/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.acosux.MSBiosuper.dao;

import com.acosux.MSBiosuper.util.entity.CuentasPorCobrarDetalladoApiTO;
import com.acosux.MSBiosuper.util.entity.CuentasPorPagarDetalladoTO;
import com.acosux.MSBiosuper.util.entity.InvComprasDetalleProductoTO;
import com.acosux.MSBiosuper.util.entity.InvListaProductosGeneralTO;
import com.acosux.MSBiosuper.util.entity.InvVentasBioSuperTO;
import com.acosux.MSBiosuper.util.entity.PrdConsumosDiarioMSTO;
import com.acosux.MSBiosuper.util.entity.PrdLiquidacionDetalleProductoTO;
import com.acosux.MSBiosuper.util.entity.PrdLiquidacionesDetalleTO;
import com.acosux.MSBiosuper.util.entity.PrdListaCostosDetalleCorridaTO;
import com.acosux.MSBiosuper.util.entity.PrdListadoGrameajeTO;
import com.acosux.MSBiosuper.util.entity.PrdResumenCorridaTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author BC
 */
public interface MSBioSuperDao {

    @Transactional
    public List<PrdResumenCorridaTO> getListaResumenCorridaTO(String empresa, String sector, String desde, String hasta, String tipoResumen, boolean resumenHengXin) throws Exception;

    @Transactional
    public List<PrdConsumosDiarioMSTO> getConsumosDiariosMicroservice(String empresa, String codigoSector, String codigoBodega, String fechaInicio, String fechaFin, String piscina, String codigoProducto) throws Exception;

    @Transactional
    public List<PrdLiquidacionDetalleProductoTO> getListadoLiquidacionDetalleProductoTO(String empresa, String sector, String piscina, String desde, String hasta, String cliente, String talla, String tipo) throws Exception;

    @Transactional
    public List<PrdLiquidacionesDetalleTO> listarLiquidacionesDetalle(String empresa, String desde, String hasta) throws Exception;

    @Transactional
    public List<PrdListaCostosDetalleCorridaTO> getPrdListaCostosDetalleCorridaTO(String empresa, String sector,
            String piscina, String corrida, String desde, String hasta, String agrupadoPor) throws Exception;

    @Transactional
    public List<PrdListadoGrameajeTO> getPrdListadoGrameajeTO(String empresa, String sector, String piscina,
            String desde, String hasta) throws Exception;

    @Transactional
    public List<InvListaProductosGeneralTO> getListaProductosTOWeb(String empresa, String busqueda, String bodega, String categoria, String fecha,
            Integer precio, boolean incluirInactivos, boolean limite, boolean codigo, String buscarPorcodigo, String tipo) throws Exception;

    @Transactional
    public List<InvComprasDetalleProductoTO> obtenerListadoComprasDetalleProducto(String empresa, String fechaDesde, String fechaHasta, String sector, String piscina, String bodega, Boolean incluirIMB) throws Exception;

    @Transactional
    public List<InvVentasBioSuperTO> getListaAnxVentaElectronicaTO(String empresa, String fechaDesde, String fechaHasta,
            String tipoDocumento) throws Exception;

    @Transactional
    public List<CuentasPorCobrarDetalladoApiTO> getApiCarListaCuentasPorCobrarDetalladoTO(String empresa, String hasta) throws Exception;

    @Transactional
    public List<CuentasPorPagarDetalladoTO> getCarListaCuentasPorPagarDetalladoTO(String empresa, String sector,
            String proveedor, String desde, String hasta, boolean excluirAprobadas, boolean incluirCheques, String formatoMensual) throws Exception;

}
