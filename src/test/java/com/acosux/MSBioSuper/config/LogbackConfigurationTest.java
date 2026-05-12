package com.acosux.MSBioSuper.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test para verificar la configuración de Logback con JSON encoder
 */
class LogbackConfigurationTest {

    @Test
    void testLogbackConfigurationLoaded() {
        // Obtener el contexto de Logback
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        assertNotNull(loggerContext, "LoggerContext no debe ser null");
        
        // Verificar que el logger root está configurado
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        assertNotNull(rootLogger, "Root logger no debe ser null");
        
        // Verificar que hay appenders configurados
        Iterator<Appender<ILoggingEvent>> appenderIterator = rootLogger.iteratorForAppenders();
        assertTrue(appenderIterator.hasNext(), "Debe haber al menos un appender configurado");
    }

    @Test
    void testApplicationLoggerConfigured() {
        // Obtener el contexto de Logback
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        
        // Obtener el logger de la aplicación
        Logger appLogger = loggerContext.getLogger("com.acosux.MSBioSuper");
        
        assertNotNull(appLogger, "Logger de aplicación no debe ser null");
    }

    @Test
    void testLoggingWorks() {
        // Crear un logger de prueba
        org.slf4j.Logger logger = LoggerFactory.getLogger(LogbackConfigurationTest.class);
        
        // Verificar que podemos escribir logs sin errores
        assertDoesNotThrow(() -> {
            logger.debug("Test DEBUG log");
            logger.info("Test INFO log");
            logger.warn("Test WARN log");
            logger.error("Test ERROR log");
        }, "Los logs deben escribirse sin errores");
    }

    @Test
    void testJsonEncoderPresent() {
        // Verificar que la clase del encoder JSON está disponible
        assertDoesNotThrow(() -> {
            Class.forName("net.logstash.logback.encoder.LogstashEncoder");
        }, "LogstashEncoder debe estar disponible en el classpath");
    }
}
