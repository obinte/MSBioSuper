/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.service;

import com.acosux.MSBiosuper.util.RespuestaWebTO;
import com.acosux.MSBiosuper.util.UtilsJSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author mario
 */
@Service
public class MSBiosSuperServiceImpl implements MSBioSuperService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${acosux.endpoint}")
    private String ENDPOINT;
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
    public RespuestaWebTO getListaResumenCorridaTO(Map<String, Object> map) throws Exception {
        return restTemplate.postForObject(ENDPOINT + "/todocompuWS/produccionWebController/getListaResumenCorridaTOMS", createTokenForUser(map), RespuestaWebTO.class);
    }

    @Override
    public RespuestaWebTO getListadoLiquidacionDetalleProductoTO(Map<String, Object> parametros) throws Exception {
        return restTemplate.postForObject(ENDPOINT + "/todocompuWS/produccionWebController/getListadoLiquidacionDetalleProducto", createTokenForUser(parametros), RespuestaWebTO.class);
    }

    @Override
    public RespuestaWebTO listarLiquidacionesDetalle(Map<String, Object> parametros) throws Exception {
        return restTemplate.postForObject(ENDPOINT + "/todocompuWS/produccionWebController/listarLiquidacionesDetalle", createTokenForUser(parametros), RespuestaWebTO.class);
    }

    @Override
    public RespuestaWebTO getConsumosDiarios(Map<String, Object> parametros) throws Exception {
        return restTemplate.postForObject(ENDPOINT + "/todocompuWS/produccionWebController/getConsumosDiariosMicroservice", createTokenForUser(parametros), RespuestaWebTO.class);
    }

    @Override
    public RespuestaWebTO listarCostosPorPiscina(Map<String, Object> parametros) throws Exception {
        return restTemplate.postForObject(ENDPOINT + "/todocompuWS/produccionWebController/getPrdListaCostosDetalleCorridaTO", createTokenForUser(parametros), RespuestaWebTO.class);
    }

    @Override
    public RespuestaWebTO obtenerListadoGramajes(Map<String, Object> parametros) throws Exception {
        return restTemplate.postForObject(ENDPOINT + "/todocompuWS/produccionWebController/obtenerListadoGramajes", createTokenForUser(parametros), RespuestaWebTO.class);
    }

    @Override
    public RespuestaWebTO getListaProductosGeneralTO(Map<String, Object> parametros) throws Exception {
        return restTemplate.postForObject(ENDPOINT + "/todocompuWS/inventarioWebController/getListaProductosGeneralTO", createTokenForUser(parametros), RespuestaWebTO.class);
    }
    
    @Override
    public RespuestaWebTO listarVentasBioSuper(Map<String, Object> parametros) throws Exception {
        return restTemplate.postForObject(ENDPOINT + "/todocompuWS/inventarioWebController/listarVentasBioSuper", createTokenForUser(parametros), RespuestaWebTO.class);
    }
    
    @Override
    public RespuestaWebTO getApiCarListaCuentasPorCobrarDetalladoTO(Map<String, Object> parametros) throws Exception {
        return restTemplate.postForObject(ENDPOINT + "/todocompuWS/carteraWebController/getApiCarListaCuentasPorCobrarDetalladoTO", createTokenForUser(parametros), RespuestaWebTO.class);
    }

}
