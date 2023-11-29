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
        parametros.put("tipoResumen", "PESCA");
        try {
            resp = mSBioSuperService.getListaResumenCorridaTO(parametros);
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

}
