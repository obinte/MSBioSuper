package com.acosux.MSBiosuper.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health del contexto: publico (excluido en VerifyTokenFilter) y valida la conexion a la base.
 */
@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @RequestMapping(value = "/health", method = {RequestMethod.GET})
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        try (Connection conexion = dataSource.getConnection();
                Statement sentencia = conexion.createStatement();
                ResultSet rs = sentencia.executeQuery("SELECT current_database()")) {
            rs.next();
            respuesta.put("status", "UP");
            respuesta.put("database", rs.getString(1));
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (Exception e) {
            respuesta.put("status", "DOWN");
            respuesta.put("error", e.getClass().getSimpleName());
            return new ResponseEntity<>(respuesta, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}
