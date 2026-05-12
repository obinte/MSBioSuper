/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBioSuper.service;

import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;
import com.acosux.MSBioSuper.domain.model.InvComprasDetalleProductoTO;
import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;
import com.acosux.MSBioSuper.domain.model.InvVentasBioSuperTO;
import com.acosux.MSBioSuper.domain.model.PrdConsumosDiarioMSTO;
import com.acosux.MSBioSuper.domain.model.PrdLiquidacionDetalleProductoTO;
import com.acosux.MSBioSuper.domain.model.PrdLiquidacionesDetalleTO;
import com.acosux.MSBioSuper.domain.model.PrdListaCostosDetalleCorridaTO;
import com.acosux.MSBioSuper.domain.model.PrdListadoGrameajeTO;
import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
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
