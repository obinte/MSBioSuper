/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.controller;

import com.acosux.MSBiosuper.service.MSBioSuperService;
import com.acosux.MSBiosuper.util.RespuestaWebTO;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mario
 */
@RestController
@RequestMapping("/biosuperController/")
public class MSBioSuperController {

    @Autowired
    MSBioSuperService mSBioSuperService;

    @RequestMapping("/getListaResumenPesca")
    public RespuestaWebTO getListaResumenPesca(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws Exception {
        RespuestaWebTO resp = new RespuestaWebTO();
        parametros.put("tipoResumen", "PESCA*");
        try {
            resp = mSBioSuperService.getListaResumenCorridaTO(parametros);
            return resp;
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/getListaResumenSiembra")
    public RespuestaWebTO getListaResumenSiembra(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws Exception {
        RespuestaWebTO resp = new RespuestaWebTO();
        parametros.put("tipoResumen", "SIEMBRA");
        try {
            resp = mSBioSuperService.getListaResumenCorridaTO(parametros);
            return resp;
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/getConsumosDiarios")
    public RespuestaWebTO getConsumosDiarios(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws Exception {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            resp = mSBioSuperService.getConsumosDiarios(parametros);
            return resp;
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/getListadoLiquidacionDetalleProducto")
    public RespuestaWebTO getListadoLiquidacionDetalleProducto(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            return mSBioSuperService.getListadoLiquidacionDetalleProductoTO(parametros);
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/listarLiquidacionesDetalle")
    public RespuestaWebTO listarLiquidacionesDetalle(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            return mSBioSuperService.listarLiquidacionesDetalle(parametros);
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }
    
    @RequestMapping("/listarCostosPorPiscina")
    public RespuestaWebTO listarCostosPorPiscina(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            return mSBioSuperService.listarCostosPorPiscina(parametros);
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }
    
    @RequestMapping("/obtenerListadoGramajes")
    public RespuestaWebTO obtenerListadoGramajes(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            return mSBioSuperService.obtenerListadoGramajes(parametros);
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }
    
    @RequestMapping("/listarVentasBioSuper")
    public RespuestaWebTO listarVentasBioSuper(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            return mSBioSuperService.listarVentasBioSuper(parametros);
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }
    
    @RequestMapping("/getListaProductosGeneralTO")
    public RespuestaWebTO getListaProductosGeneralTO(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            return mSBioSuperService.getListaProductosGeneralTO(parametros);
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }
    
    @RequestMapping("/getApiCarListaCuentasPorCobrarDetalladoTO")
    public RespuestaWebTO getApiCarListaCuentasPorCobrarDetalladoTO(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            return mSBioSuperService.getApiCarListaCuentasPorCobrarDetalladoTO(parametros);
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }
    
    @RequestMapping("/getListadoComprasDetalleProducto")
    public RespuestaWebTO getListadoComprasDetalleProducto(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            return mSBioSuperService.getListadoComprasDetalleProducto(parametros);
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }
    
    @RequestMapping("/getCarListaCuentasPorPagarDetalladoTO")
    public RespuestaWebTO getCarListaCuentasPorPagarDetalladoTO(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            return mSBioSuperService.getCarListaCuentasPorPagarDetalladoTO(parametros);
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }
   
}
