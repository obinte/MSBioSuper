/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.service;

import com.acosux.MSBiosuper.dao.MSBioSuperDao;
import com.acosux.MSBiosuper.util.UtilsDate;
import com.acosux.MSBiosuper.util.UtilsJSON;
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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

/**
 *
 * @author mario
 */
@Service
public class MSBiosSuperServiceImpl implements MSBioSuperService {

    @Autowired
    private MSBioSuperDao bioSuperDao;
    private static final long VALIDITY_TIME_MS = 10 * 24 * 60 * 60 * 1000;// 10 days Validity
    private final String secret = "mrin";

    public HttpEntity<String> createTokenForUser(Map<String, Object> map) throws Exception {
        String token = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
                .setSubject("admin")
                .claim("userId", "admin")
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + token);
        HttpEntity<String> entity = new HttpEntity<>(UtilsJSON.objetoToJson(map), headers);
        return entity;
    }

    @Override
    public List<PrdResumenCorridaTO> getListaResumenCorridaTO(Map<String, Object> parametros) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, parametros.get("empresa"));
        String codigoSector = UtilsJSON.jsonToObjeto(String.class, parametros.get("codigoSector"));
        String fechaInicio = UtilsJSON.jsonToObjeto(String.class, parametros.get("fechaInicio"));
        String fechaFin = UtilsJSON.jsonToObjeto(String.class, parametros.get("fechaFin"));
        String tipoResumen = UtilsJSON.jsonToObjeto(String.class, parametros.get("tipoResumen"));
        return bioSuperDao.getListaResumenCorridaTO(empresa, codigoSector, fechaInicio, fechaFin, tipoResumen, true);
    }

    @Override
    public List<PrdConsumosDiarioMSTO> getConsumosDiarios(Map<String, Object> map) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, map.get("empresa"));
        String codigoSector = UtilsJSON.jsonToObjeto(String.class, map.get("codigoSector"));
        String codigoBodega = UtilsJSON.jsonToObjeto(String.class, map.get("codigoBodega"));
        String fechaInicio = UtilsJSON.jsonToObjeto(String.class, map.get("fechaInicio"));
        String fechaFin = UtilsJSON.jsonToObjeto(String.class, map.get("fechaFin"));
        String piscinaNumero = UtilsJSON.jsonToObjeto(String.class, map.get("piscinaNumero"));
        String codigoProducto = UtilsJSON.jsonToObjeto(String.class, map.get("codigoProducto"));
        return bioSuperDao.getConsumosDiariosMicroservice(empresa, codigoSector, codigoBodega, fechaInicio, fechaFin, piscinaNumero, codigoProducto);
    }

    @Override
    public List<PrdLiquidacionDetalleProductoTO> getListadoLiquidacionDetalleProductoTO(Map<String, Object> map) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, map.get("empresa"));
        String fechaDesde = UtilsJSON.jsonToObjeto(String.class, map.get("fechaDesde"));
        String fechaHasta = UtilsJSON.jsonToObjeto(String.class, map.get("fechaHasta"));
        String sector = UtilsJSON.jsonToObjeto(String.class, map.get("sector"));
        String piscina = UtilsJSON.jsonToObjeto(String.class, map.get("piscina"));
        String cliente = UtilsJSON.jsonToObjeto(String.class, map.get("cliente"));
        String talla = UtilsJSON.jsonToObjeto(String.class, map.get("talla"));
        String tipo = UtilsJSON.jsonToObjeto(String.class, map.get("tipo"));
        return bioSuperDao.getListadoLiquidacionDetalleProductoTO(empresa, sector, piscina, fechaDesde, fechaHasta, cliente, talla, tipo);
    }

    @Override
    public List<PrdLiquidacionesDetalleTO> listarLiquidacionesDetalle(Map<String, Object> map) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, map.get("empresa"));
        String fechaDesde = UtilsJSON.jsonToObjeto(String.class, map.get("fechaDesde"));
        String fechaHasta = UtilsJSON.jsonToObjeto(String.class, map.get("fechaHasta"));
        return bioSuperDao.listarLiquidacionesDetalle(empresa, fechaDesde, fechaHasta);
    }

    @Override
    public List<PrdListaCostosDetalleCorridaTO> listarCostosPorPiscina(Map<String, Object> map) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, map.get("empresa"));
        String sector = UtilsJSON.jsonToObjeto(String.class, map.get("sector"));
        String piscina = UtilsJSON.jsonToObjeto(String.class, map.get("piscina"));
        String corrida = UtilsJSON.jsonToObjeto(String.class, map.get("corrida"));
        String desde = UtilsJSON.jsonToObjeto(String.class, map.get("desde"));
        String hasta = UtilsJSON.jsonToObjeto(String.class, map.get("hasta"));
        String agrupadoPor = UtilsJSON.jsonToObjeto(String.class, map.get("agrupadoPor"));
        return bioSuperDao.getPrdListaCostosDetalleCorridaTO(empresa, sector, piscina, corrida, desde, hasta, agrupadoPor);
    }

    @Override
    public List<PrdListadoGrameajeTO> obtenerListadoGramajes(Map<String, Object> map) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, map.get("empresa"));
        String sector = UtilsJSON.jsonToObjeto(String.class, map.get("sector"));
        String piscina = UtilsJSON.jsonToObjeto(String.class, map.get("piscina"));
        String desde = UtilsJSON.jsonToObjeto(String.class, map.get("desde"));
        String hasta = UtilsJSON.jsonToObjeto(String.class, map.get("hasta"));
        return bioSuperDao.getPrdListadoGrameajeTO(empresa, sector, piscina, desde, hasta);
    }

    @Override
    public List<InvListaProductosGeneralTO> getListaProductosGeneralTO(Map<String, Object> map) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, map.get("empresa"));
        String busqueda = UtilsJSON.jsonToObjeto(String.class, map.get("busqueda"));
        String bodega = UtilsJSON.jsonToObjeto(String.class, map.get("bodega"));
        String categoria = UtilsJSON.jsonToObjeto(String.class, map.get("categoria"));
        String fecha = UtilsJSON.jsonToObjeto(String.class, map.get("fecha"));
        boolean incluirInactivos = UtilsJSON.jsonToObjeto(boolean.class, map.get("incluirInactivos"));
        boolean limite = UtilsJSON.jsonToObjeto(boolean.class, map.get("limite"));
        Integer precio = UtilsJSON.jsonToObjeto(Integer.class, map.get("precio"));
        boolean codigo = UtilsJSON.jsonToObjeto(boolean.class, map.get("codigo"));
        String buscarPorcodigo = UtilsJSON.jsonToObjeto(String.class, map.get("buscarPorcodigo"));
        String tipo = UtilsJSON.jsonToObjeto(String.class, map.get("tipo"));
        return bioSuperDao.getListaProductosTOWeb(empresa, busqueda, bodega, categoria, fecha, precio, incluirInactivos, limite, codigo, buscarPorcodigo, tipo);
    }

    @Override
    public List<InvComprasDetalleProductoTO> getListadoComprasDetalleProducto(Map<String, Object> map) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, map.get("empresa"));
        String fechaDesde = UtilsJSON.jsonToObjeto(String.class, map.get("fechaDesde"));
        String fechaHasta = UtilsJSON.jsonToObjeto(String.class, map.get("fechaHasta"));
        String sector = UtilsJSON.jsonToObjeto(String.class, map.get("sector"));
        String piscina = UtilsJSON.jsonToObjeto(String.class, map.get("piscina"));
        String bodega = UtilsJSON.jsonToObjeto(String.class, map.get("bodega"));
        Boolean incluirIBM = UtilsJSON.jsonToObjeto(Boolean.class, map.get("incluirIBM"));
        return bioSuperDao.obtenerListadoComprasDetalleProducto(empresa, fechaDesde, fechaHasta, sector, piscina, bodega, incluirIBM);
    }

    @Override
    public List<InvVentasBioSuperTO> listarVentasBioSuper(Map<String, Object> map) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, map.get("empresa"));
        String fechaDesde = UtilsJSON.jsonToObjeto(String.class, map.get("fechaDesde"));
        String fechaHasta = UtilsJSON.jsonToObjeto(String.class, map.get("fechaHasta"));
        String tipoDocumento = UtilsJSON.jsonToObjeto(String.class, map.get("tipoDocumento"));
        return bioSuperDao.getListaAnxVentaElectronicaTO(empresa, fechaDesde, fechaHasta, tipoDocumento);
    }

    @Override
    public List<CuentasPorCobrarDetalladoApiTO> getApiCarListaCuentasPorCobrarDetalladoTO(Map<String, Object> map) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, map.get("empresa"));
        String hasta = UtilsDate.DeDateAString(new Date());
        return bioSuperDao.getApiCarListaCuentasPorCobrarDetalladoTO(empresa, hasta);
    }

    @Override
    public List<CuentasPorPagarDetalladoTO> getCarListaCuentasPorPagarDetalladoTO(Map<String, Object> map) throws Exception {
        String empresa = UtilsJSON.jsonToObjeto(String.class, map.get("empresa"));
        String sector = UtilsJSON.jsonToObjeto(String.class, map.get("sector"));
        String proveedor = UtilsJSON.jsonToObjeto(String.class, map.get("proveedor"));
        String desde = UtilsJSON.jsonToObjeto(String.class, map.get("desde"));
        String hasta = UtilsJSON.jsonToObjeto(String.class, map.get("hasta"));
        boolean excluirAprobadas = UtilsJSON.jsonToObjeto(boolean.class, map.get("excluirAprobadas"));
        boolean incluirCheques = UtilsJSON.jsonToObjeto(boolean.class, map.get("incluirCheques"));
        String formatoMensual = UtilsJSON.jsonToObjeto(String.class, map.get("formatoMensual"));
        return bioSuperDao.getCarListaCuentasPorPagarDetalladoTO(empresa, sector, proveedor, desde, hasta, excluirAprobadas, incluirCheques, formatoMensual);
    }

}
