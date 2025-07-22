/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.controller;

import com.acosux.MSBiosuper.service.MSBioSuperService;
import com.acosux.MSBiosuper.util.RespuestaWebTO;
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
            List<PrdResumenCorridaTO> respues = mSBioSuperService.getListaResumenCorridaTO(parametros);
            if (respues.size() > 1) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje("");
                resp.setExtraInfo(respues);
            } else {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
                resp.setOperacionMensaje("No se encontraron resultados");
            }
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
            List<PrdResumenCorridaTO> respues = mSBioSuperService.getListaResumenCorridaTO(parametros);
            if (respues.size() > 1) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje("");
                resp.setExtraInfo(respues);
            } else {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ADVERTENCIA.getValor());
                resp.setOperacionMensaje("No se encontraron resultados");
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/getConsumosDiarios")
    public RespuestaWebTO getConsumosDiarios(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws Exception {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            List<PrdConsumosDiarioMSTO> respues = mSBioSuperService.getConsumosDiarios(parametros);
            if (respues != null) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respues);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
                resp.setExtraInfo(false);
            }
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
            List<PrdLiquidacionDetalleProductoTO> respues = mSBioSuperService.getListadoLiquidacionDetalleProductoTO(parametros);
            if (respues != null && !respues.isEmpty()) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respues);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/listarLiquidacionesDetalle")
    public RespuestaWebTO listarLiquidacionesDetalle(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            List<PrdLiquidacionesDetalleTO> respues = mSBioSuperService.listarLiquidacionesDetalle(parametros);
            if (respues != null && !respues.isEmpty()) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respues);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/listarCostosPorPiscina")
    public RespuestaWebTO listarCostosPorPiscina(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            List<PrdListaCostosDetalleCorridaTO> respues = mSBioSuperService.listarCostosPorPiscina(parametros);
            if (respues != null && !respues.isEmpty()) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respues);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/obtenerListadoGramajes")
    public RespuestaWebTO obtenerListadoGramajes(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            List<PrdListadoGrameajeTO> respues = mSBioSuperService.obtenerListadoGramajes(parametros);
            if (respues != null) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respues);
            } else {
                resp.setOperacionMensaje("No se han encontrado resultados.");
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/getListadoComprasDetalleProducto")
    public RespuestaWebTO getListadoComprasDetalleProducto(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            List<InvComprasDetalleProductoTO> respues = mSBioSuperService.getListadoComprasDetalleProducto(parametros);
            if (respues != null && !respues.isEmpty()) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respues);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/listarVentasBioSuper")
    public RespuestaWebTO listarVentasBioSuper(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            List<InvVentasBioSuperTO> lista = mSBioSuperService.listarVentasBioSuper(parametros);
            if (lista != null && !lista.isEmpty()) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(lista);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/getListaProductosGeneralTO")
    public RespuestaWebTO getListaProductosGeneralTO(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            List<InvListaProductosGeneralTO> respues = mSBioSuperService.getListaProductosGeneralTO(parametros);
            if (respues != null && !respues.isEmpty()) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respues);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/getApiCarListaCuentasPorCobrarDetalladoTO")
    public RespuestaWebTO getApiCarListaCuentasPorCobrarDetalladoTO(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            List<CuentasPorCobrarDetalladoApiTO> respuesta = mSBioSuperService.getApiCarListaCuentasPorCobrarDetalladoTO(parametros);
            if (respuesta != null && !respuesta.isEmpty()) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respuesta);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

    @RequestMapping("/getCarListaCuentasPorPagarDetalladoTO")
    public RespuestaWebTO getCarListaCuentasPorPagarDetalladoTO(HttpServletRequest request, @RequestBody Map<String, Object> parametros) {
        RespuestaWebTO resp = new RespuestaWebTO();
        try {
            List<CuentasPorPagarDetalladoTO> respuesta = mSBioSuperService.getCarListaCuentasPorPagarDetalladoTO(parametros);
            if (respuesta != null && !respuesta.isEmpty()) {
                resp.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.EXITO.getValor());
                resp.setExtraInfo(respuesta);
            } else {
                resp.setOperacionMensaje("No se encontraron resultados.");
            }
        } catch (Exception e) {
            resp.setOperacionMensaje(e.getMessage());
        }
        return resp;
    }

}
