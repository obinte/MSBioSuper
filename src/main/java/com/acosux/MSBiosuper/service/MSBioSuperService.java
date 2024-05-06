/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.service;

import com.acosux.MSBiosuper.util.RespuestaWebTO;
import java.util.Map;

/**
 *
 * @author mario
 */
public interface MSBioSuperService {

    public RespuestaWebTO getListaResumenCorridaTO(Map<String, Object> map) throws Exception;

    public RespuestaWebTO getConsumosDiarios(Map<String, Object> map) throws Exception;

    public RespuestaWebTO getListadoLiquidacionDetalleProductoTO(Map<String, Object> parametros) throws Exception;

    public RespuestaWebTO listarLiquidacionesDetalle(Map<String, Object> parametros) throws Exception;

    public RespuestaWebTO listarCostosPorPiscina(Map<String, Object> parametros) throws Exception;

    public RespuestaWebTO obtenerListadoGramajes(Map<String, Object> parametros) throws Exception;

    public RespuestaWebTO getListaProductosGeneralTO(Map<String, Object> parametros) throws Exception;

    public RespuestaWebTO listarVentasBioSuper(Map<String, Object> parametros) throws Exception;
    
    public RespuestaWebTO getApiCarListaCuentasPorCobrarDetalladoTO(Map<String, Object> parametros) throws Exception;

}
