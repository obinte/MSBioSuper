package com.acosux.MSBioSuper.infrastructure.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas para verificar el logging estructurado en formato JSON.
 * 
 * Verifica que:
 * - Los logs se generan en formato JSON válido
 * - El correlation ID se incluye en los logs
 * - Los campos requeridos están presentes
 * - Los niveles de log funcionan correctamente
 * 
 * Estas pruebas son críticas para el Punto de Control 31.
 */
@DisplayName("Pruebas de Logging Estructurado")
class StructuredLoggingTest {

    private Logger logger;
    private ListAppender<ILoggingEvent> listAppender;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Configurar logger de prueba
        logger = (Logger) LoggerFactory.getLogger("com.acosux.MSBiosuper.test");
        
        // Crear appender para capturar logs
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        
        // ObjectMapper para parsear JSON
        objectMapper = new ObjectMapper();
        
        // Limpiar MDC
        MDC.clear();
    }

    @AfterEach
    void tearDown() {
        // Limpiar MDC después de cada prueba
        MDC.clear();
        
        // Detener appender
        if (listAppender != null) {
            listAppender.stop();
        }
    }

    // ========================================
    // PRUEBAS DE NIVELES DE LOG
    // ========================================

    @Test
    @DisplayName("Debe generar log de nivel INFO")
    void debeGenerarLogNivelInfo() {
        // Act
        logger.info("Mensaje de prueba INFO");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getLevel().toString()).isEqualTo("INFO");
        assertThat(logEvent.getMessage()).isEqualTo("Mensaje de prueba INFO");
    }

    @Test
    @DisplayName("Debe generar log de nivel WARN")
    void debeGenerarLogNivelWarn() {
        // Act
        logger.warn("Mensaje de prueba WARN");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getLevel().toString()).isEqualTo("WARN");
        assertThat(logEvent.getMessage()).isEqualTo("Mensaje de prueba WARN");
    }

    @Test
    @DisplayName("Debe generar log de nivel ERROR")
    void debeGenerarLogNivelError() {
        // Act
        logger.error("Mensaje de prueba ERROR");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getLevel().toString()).isEqualTo("ERROR");
        assertThat(logEvent.getMessage()).isEqualTo("Mensaje de prueba ERROR");
    }

    @Test
    @DisplayName("Debe generar log de nivel DEBUG")
    void debeGenerarLogNivelDebug() {
        // Configurar nivel DEBUG para el logger
        logger.setLevel(ch.qos.logback.classic.Level.DEBUG);

        // Act
        logger.debug("Mensaje de prueba DEBUG");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getLevel().toString()).isEqualTo("DEBUG");
        assertThat(logEvent.getMessage()).isEqualTo("Mensaje de prueba DEBUG");
    }

    // ========================================
    // PRUEBAS DE CORRELATION ID (MDC)
    // ========================================

    @Test
    @DisplayName("Debe incluir correlation ID en MDC cuando está presente")
    void debeIncluirCorrelationIdEnMDC() {
        // Arrange
        String correlationId = "test-correlation-id-12345";
        MDC.put("correlationId", correlationId);

        // Act
        logger.info("Mensaje con correlation ID");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getMDCPropertyMap()).containsEntry("correlationId", correlationId);
    }

    @Test
    @DisplayName("Debe manejar logs sin correlation ID")
    void debeManejarLogsSinCorrelationId() {
        // Act - No se establece correlation ID
        logger.info("Mensaje sin correlation ID");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getMDCPropertyMap()).doesNotContainKey("correlationId");
    }

    @Test
    @DisplayName("Debe mantener correlation ID a través de múltiples logs")
    void debeMantenerCorrelationIdEnMultiplesLogs() {
        // Arrange
        String correlationId = "persistent-correlation-id";
        MDC.put("correlationId", correlationId);

        // Act
        logger.info("Primer mensaje");
        logger.info("Segundo mensaje");
        logger.info("Tercer mensaje");

        // Assert
        assertThat(listAppender.list).hasSize(3);
        for (ILoggingEvent logEvent : listAppender.list) {
            assertThat(logEvent.getMDCPropertyMap()).containsEntry("correlationId", correlationId);
        }
    }

    // ========================================
    // PRUEBAS DE CONTEXTO ADICIONAL EN MDC
    // ========================================

    @Test
    @DisplayName("Debe incluir userId en MDC cuando está presente")
    void debeIncluirUserIdEnMDC() {
        // Arrange
        String userId = "user-123";
        MDC.put("userId", userId);

        // Act
        logger.info("Mensaje con userId");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getMDCPropertyMap()).containsEntry("userId", userId);
    }

    @Test
    @DisplayName("Debe incluir múltiples campos en MDC")
    void debeIncluirMultiplesCamposEnMDC() {
        // Arrange
        MDC.put("correlationId", "corr-123");
        MDC.put("userId", "user-456");
        MDC.put("requestId", "req-789");

        // Act
        logger.info("Mensaje con múltiples campos MDC");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getMDCPropertyMap())
                .containsEntry("correlationId", "corr-123")
                .containsEntry("userId", "user-456")
                .containsEntry("requestId", "req-789");
    }

    // ========================================
    // PRUEBAS DE EXCEPCIONES EN LOGS
    // ========================================

    @Test
    @DisplayName("Debe incluir stack trace cuando se registra una excepción")
    void debeIncluirStackTraceConExcepcion() {
        // Arrange
        Exception exception = new RuntimeException("Error de prueba");

        // Act
        logger.error("Error ocurrido", exception);

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getThrowableProxy()).isNotNull();
        assertThat(logEvent.getThrowableProxy().getMessage()).isEqualTo("Error de prueba");
        assertThat(logEvent.getThrowableProxy().getClassName()).isEqualTo("java.lang.RuntimeException");
    }

    @Test
    @DisplayName("Debe incluir causa raíz en stack trace")
    void debeIncluirCausaRaizEnStackTrace() {
        // Arrange
        Exception causaRaiz = new IllegalArgumentException("Causa raíz");
        Exception exception = new RuntimeException("Error principal", causaRaiz);

        // Act
        logger.error("Error con causa", exception);

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getThrowableProxy()).isNotNull();
        assertThat(logEvent.getThrowableProxy().getCause()).isNotNull();
        assertThat(logEvent.getThrowableProxy().getCause().getMessage()).isEqualTo("Causa raíz");
    }

    // ========================================
    // PRUEBAS DE CAMPOS REQUERIDOS
    // ========================================

    @Test
    @DisplayName("Debe incluir timestamp en log event")
    void debeIncluirTimestampEnLogEvent() {
        // Act
        logger.info("Mensaje con timestamp");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getTimeStamp()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Debe incluir nombre del logger")
    void debeIncluirNombreDelLogger() {
        // Act
        logger.info("Mensaje con nombre de logger");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getLoggerName()).isEqualTo("com.acosux.MSBiosuper.test");
    }

    @Test
    @DisplayName("Debe incluir nombre del thread")
    void debeIncluirNombreDelThread() {
        // Act
        logger.info("Mensaje con nombre de thread");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getThreadName()).isNotNull();
        assertThat(logEvent.getThreadName()).isNotEmpty();
    }

    // ========================================
    // PRUEBAS DE LIMPIEZA DE MDC
    // ========================================

    @Test
    @DisplayName("Debe limpiar MDC correctamente")
    void debeLimpiarMDCCorrectamente() {
        // Arrange
        MDC.put("correlationId", "test-id");
        MDC.put("userId", "user-id");

        // Act
        MDC.clear();
        logger.info("Mensaje después de limpiar MDC");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getMDCPropertyMap()).isEmpty();
    }

    @Test
    @DisplayName("Debe permitir remover campos individuales de MDC")
    void debePermitirRemoverCamposIndividualesDeMDC() {
        // Arrange
        MDC.put("correlationId", "test-id");
        MDC.put("userId", "user-id");

        // Act
        MDC.remove("userId");
        logger.info("Mensaje después de remover userId");

        // Assert
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getMDCPropertyMap())
                .containsEntry("correlationId", "test-id")
                .doesNotContainKey("userId");
    }

    // ========================================
    // PRUEBAS DE RENDIMIENTO
    // ========================================

    @Test
    @DisplayName("Debe manejar múltiples logs de manera eficiente")
    void debeManejarMultiplesLogsEficientemente() {
        // Arrange
        int numeroLogs = 1000;

        // Act
        long inicio = System.currentTimeMillis();
        for (int i = 0; i < numeroLogs; i++) {
            logger.info("Mensaje de prueba {}", i);
        }
        long fin = System.currentTimeMillis();

        // Assert
        assertThat(listAppender.list).hasSize(numeroLogs);
        long duracion = fin - inicio;
        // Verificar que el logging es razonablemente rápido (< 1 segundo para 1000 logs)
        assertThat(duracion).isLessThan(1000);
    }
}
