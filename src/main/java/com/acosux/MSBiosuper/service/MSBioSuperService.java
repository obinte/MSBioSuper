/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.service;

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
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mario
 */
@Transactional
public interface MSBioSuperService {

    public List<PrdResumenCorridaTO> getListaResumenCorridaTO(Map<String, Object> map) throws Exception;

    public List<PrdConsumosDiarioMSTO> getConsumosDiarios(Map<String, Object> map) throws Exception;

    public List<PrdLiquidacionDetalleProductoTO> getListadoLiquidacionDetalleProductoTO(Map<String, Object> parametros) throws Exception;

    public List<PrdLiquidacionesDetalleTO> listarLiquidacionesDetalle(Map<String, Object> parametros) throws Exception;

    public List<PrdListaCostosDetalleCorridaTO> listarCostosPorPiscina(Map<String, Object> parametros) throws Exception;

    public List<PrdListadoGrameajeTO> obtenerListadoGramajes(Map<String, Object> parametros) throws Exception;

    public List<InvListaProductosGeneralTO> getListaProductosGeneralTO(Map<String, Object> parametros) throws Exception;

    public List<InvComprasDetalleProductoTO> getListadoComprasDetalleProducto(Map<String, Object> parametros) throws Exception;

    public List<InvVentasBioSuperTO> listarVentasBioSuper(Map<String, Object> parametros) throws Exception;

    public List<CuentasPorCobrarDetalladoApiTO> getApiCarListaCuentasPorCobrarDetalladoTO(Map<String, Object> parametros) throws Exception;

    public List<CuentasPorPagarDetalladoTO> getCarListaCuentasPorPagarDetalladoTO(Map<String, Object> parametros) throws Exception;

}
