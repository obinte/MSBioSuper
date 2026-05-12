package com.acosux.MSBioSuper.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para BusinessMetricsService.
 * 
 * Verifica que las métricas de negocio se registran correctamente.
 */
class BusinessMetricsServiceTest {

    private MeterRegistry meterRegistry;
    private BusinessMetricsService metricsService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metricsService = new BusinessMetricsService(meterRegistry);
    }

    @Test
    void testIncrementResumenPescaCount() {
        // Given
        Counter counter = meterRegistry.find("msbiosuper.business.resumen_pesca.count").counter();
        assertNotNull(counter, "El contador debe estar registrado");
        double initialCount = counter.count();

        // When
        metricsService.incrementResumenPescaCount();

        // Then
        assertEquals(initialCount + 1, counter.count(), "El contador debe incrementarse en 1");
    }

    @Test
    void testIncrementResumenSiembraCount() {
        // Given
        Counter counter = meterRegistry.find("msbiosuper.business.resumen_siembra.count").counter();
        assertNotNull(counter, "El contador debe estar registrado");
        double initialCount = counter.count();

        // When
        metricsService.incrementResumenSiembraCount();

        // Then
        assertEquals(initialCount + 1, counter.count(), "El contador debe incrementarse en 1");
    }

    @Test
    void testIncrementConsumosDiariosCount() {
        // Given
        Counter counter = meterRegistry.find("msbiosuper.business.consumos_diarios.count").counter();
        assertNotNull(counter, "El contador debe estar registrado");
        double initialCount = counter.count();

        // When
        metricsService.incrementConsumosDiariosCount();

        // Then
        assertEquals(initialCount + 1, counter.count(), "El contador debe incrementarse en 1");
    }

    @Test
    void testRecordResumenPescaDuration() {
        // Given
        Timer timer = meterRegistry.find("msbiosuper.business.resumen_pesca.duration").timer();
        assertNotNull(timer, "El timer debe estar registrado");
        long initialCount = timer.count();

        // When
        metricsService.recordResumenPescaDuration(150);

        // Then
        assertEquals(initialCount + 1, timer.count(), "El timer debe registrar una medición");
        assertTrue(timer.totalTime(java.util.concurrent.TimeUnit.MILLISECONDS) >= 150, 
                   "El tiempo total debe incluir la duración registrada");
    }

    @Test
    void testRecordResumenSiembraDuration() {
        // Given
        Timer timer = meterRegistry.find("msbiosuper.business.resumen_siembra.duration").timer();
        assertNotNull(timer, "El timer debe estar registrado");
        long initialCount = timer.count();

        // When
        metricsService.recordResumenSiembraDuration(200);

        // Then
        assertEquals(initialCount + 1, timer.count(), "El timer debe registrar una medición");
        assertTrue(timer.totalTime(java.util.concurrent.TimeUnit.MILLISECONDS) >= 200, 
                   "El tiempo total debe incluir la duración registrada");
    }

    @Test
    void testRecordResultCount() {
        // Given
        String operation = "test_operation";
        int resultCount = 42;

        // When
        metricsService.recordResultCount(operation, resultCount);

        // Then
        Counter counter = meterRegistry.find("msbiosuper.business.result_count")
                .tag("operation", operation)
                .counter();
        assertNotNull(counter, "El contador de resultados debe estar registrado");
        assertEquals(resultCount, counter.count(), "El contador debe reflejar el número de resultados");
    }

    @Test
    void testRecordBusinessError() {
        // Given
        String operation = "test_operation";
        String errorType = "NullPointerException";

        // When
        metricsService.recordBusinessError(operation, errorType);

        // Then
        Counter counter = meterRegistry.find("msbiosuper.business.errors")
                .tag("operation", operation)
                .tag("error_type", errorType)
                .counter();
        assertNotNull(counter, "El contador de errores debe estar registrado");
        assertEquals(1.0, counter.count(), "El contador debe incrementarse en 1");
    }

    @Test
    void testRecordCustomMetric() {
        // Given
        String metricName = "custom.metric.test";
        double value = 123.45;
        String tagKey = "custom_tag";
        String tagValue = "custom_value";

        // When
        metricsService.recordCustomMetric(metricName, value, tagKey, tagValue);

        // Then
        Counter counter = meterRegistry.find(metricName)
                .tag(tagKey, tagValue)
                .counter();
        assertNotNull(counter, "La métrica personalizada debe estar registrada");
        assertEquals(value, counter.count(), "El contador debe reflejar el valor registrado");
    }

    @Test
    void testMultipleIncrements() {
        // Given
        Counter counter = meterRegistry.find("msbiosuper.business.resumen_pesca.count").counter();
        double initialCount = counter.count();

        // When
        metricsService.incrementResumenPescaCount();
        metricsService.incrementResumenPescaCount();
        metricsService.incrementResumenPescaCount();

        // Then
        assertEquals(initialCount + 3, counter.count(), "El contador debe incrementarse 3 veces");
    }

    @Test
    void testMultipleDurationRecordings() {
        // Given
        Timer timer = meterRegistry.find("msbiosuper.business.resumen_pesca.duration").timer();
        long initialCount = timer.count();

        // When
        metricsService.recordResumenPescaDuration(100);
        metricsService.recordResumenPescaDuration(200);
        metricsService.recordResumenPescaDuration(300);

        // Then
        assertEquals(initialCount + 3, timer.count(), "El timer debe registrar 3 mediciones");
        assertTrue(timer.totalTime(java.util.concurrent.TimeUnit.MILLISECONDS) >= 600, 
                   "El tiempo total debe ser al menos 600ms");
    }

    @Test
    void testAllCountersAreRegistered() {
        // Verificar que todos los contadores están registrados al inicializar el servicio
        assertNotNull(meterRegistry.find("msbiosuper.business.resumen_pesca.count").counter());
        assertNotNull(meterRegistry.find("msbiosuper.business.resumen_siembra.count").counter());
        assertNotNull(meterRegistry.find("msbiosuper.business.consumos_diarios.count").counter());
        assertNotNull(meterRegistry.find("msbiosuper.business.liquidaciones.count").counter());
        assertNotNull(meterRegistry.find("msbiosuper.business.ventas.count").counter());
        assertNotNull(meterRegistry.find("msbiosuper.business.cuentas_por_cobrar.count").counter());
        assertNotNull(meterRegistry.find("msbiosuper.business.cuentas_por_pagar.count").counter());
        assertNotNull(meterRegistry.find("msbiosuper.business.productos.count").counter());
    }

    @Test
    void testAllTimersAreRegistered() {
        // Verificar que todos los timers están registrados al inicializar el servicio
        assertNotNull(meterRegistry.find("msbiosuper.business.resumen_pesca.duration").timer());
        assertNotNull(meterRegistry.find("msbiosuper.business.resumen_siembra.duration").timer());
        assertNotNull(meterRegistry.find("msbiosuper.business.consumos_diarios.duration").timer());
        assertNotNull(meterRegistry.find("msbiosuper.business.liquidaciones.duration").timer());
        assertNotNull(meterRegistry.find("msbiosuper.business.ventas.duration").timer());
        assertNotNull(meterRegistry.find("msbiosuper.business.cuentas_por_cobrar.duration").timer());
        assertNotNull(meterRegistry.find("msbiosuper.business.cuentas_por_pagar.duration").timer());
    }

    @Test
    void testMetricsHaveCorrectTags() {
        // Verificar que las métricas tienen los tags correctos
        Counter counter = meterRegistry.find("msbiosuper.business.resumen_pesca.count")
                .tag("operation", "resumen_pesca")
                .counter();
        assertNotNull(counter, "El contador debe tener el tag 'operation'");

        Timer timer = meterRegistry.find("msbiosuper.business.resumen_pesca.duration")
                .tag("operation", "resumen_pesca")
                .timer();
        assertNotNull(timer, "El timer debe tener el tag 'operation'");
    }
}
