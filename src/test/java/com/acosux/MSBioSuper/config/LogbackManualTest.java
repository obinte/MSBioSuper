package com.acosux.MSBioSuper.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Test manual para verificar la configuración de Logback con JSON encoder
 * 
 * Este test NO es un test unitario automático. Es un programa que se puede
 * ejecutar manualmente para verificar que los logs se generan en formato JSON.
 * 
 * Para ejecutar:
 * mvn test-compile exec:java -Dexec.mainClass="com.acosux.MSBiosuper.config.LogbackManualTest" -Dexec.classpathScope=test
 */
public class LogbackManualTest {
    
    private static final Logger logger = LoggerFactory.getLogger(LogbackManualTest.class);
    
    public static void main(String[] args) {
        System.out.println("=== Iniciando prueba de configuración de Logback ===\n");
        
        // Test 1: Logs básicos en diferentes niveles
        logger.debug("Este es un mensaje DEBUG");
        logger.info("Este es un mensaje INFO");
        logger.warn("Este es un mensaje WARN");
        logger.error("Este es un mensaje ERROR");
        
        // Test 2: Log con MDC (Mapped Diagnostic Context) para correlationId
        MDC.put("correlationId", "test-correlation-123");
        MDC.put("userId", "testuser@example.com");
        logger.info("Log con MDC - correlationId y userId");
        MDC.clear();
        
        // Test 3: Log con excepción
        try {
            throw new RuntimeException("Esta es una excepción de prueba");
        } catch (Exception e) {
            logger.error("Error capturado en el test", e);
        }
        
        // Test 4: Log con parámetros
        String usuario = "Juan Pérez";
        int intentos = 3;
        logger.info("Usuario {} intentó iniciar sesión {} veces", usuario, intentos);
        
        System.out.println("\n=== Prueba completada ===");
        System.out.println("Si ves logs en formato JSON arriba, la configuración está funcionando correctamente.");
    }
}
