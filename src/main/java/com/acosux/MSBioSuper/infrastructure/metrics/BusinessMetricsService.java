package com.acosux.MSBioSuper.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Servicio para registrar métricas de negocio personalizadas.
 * 
 * Este servicio proporciona métodos para registrar métricas relacionadas con:
 * - Operaciones de negocio (consultas, transacciones)
 * - Latencia de endpoints API
 * - Contadores de eventos de negocio
 * - Métricas de rendimiento
 * 
 * Las métricas se exponen a través de Prometheus en /actuator/prometheus
 * 
 * @author MSBioSuper Team
 * @version 1.0
 * @since 2024
 */
@Service
public class BusinessMetricsService {

    private final MeterRegistry meterRegistry;

    // Contadores para operaciones de negocio
    private final Counter resumenPescaCounter;
    private final Counter resumenSiembraCounter;
    private final Counter consumosDiariosCounter;
    private final Counter liquidacionesCounter;
    private final Counter ventasCounter;
    private final Counter cuentasPorCobrarCounter;
    private final Counter cuentasPorPagarCounter;
    private final Counter productosCounter;

    // Timers para medir latencia de operaciones
    private final Timer resumenPescaTimer;
    private final Timer resumenSiembraTimer;
    private final Timer consumosDiariosTimer;
    private final Timer liquidacionesTimer;
    private final Timer ventasTimer;
    private final Timer cuentasPorCobrarTimer;
    private final Timer cuentasPorPagarTimer;

    /**
     * Constructor que inicializa todas las métricas personalizadas.
     * 
     * @param meterRegistry Registro de métricas de Micrometer
     */
    public BusinessMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Inicializar contadores de operaciones
        this.resumenPescaCounter = Counter.builder("msbiosuper.business.resumen_pesca.count")
                .description("Número total de consultas de resumen de pesca")
                .tag("operation", "resumen_pesca")
                .register(meterRegistry);

        this.resumenSiembraCounter = Counter.builder("msbiosuper.business.resumen_siembra.count")
                .description("Número total de consultas de resumen de siembra")
                .tag("operation", "resumen_siembra")
                .register(meterRegistry);

        this.consumosDiariosCounter = Counter.builder("msbiosuper.business.consumos_diarios.count")
                .description("Número total de consultas de consumos diarios")
                .tag("operation", "consumos_diarios")
                .register(meterRegistry);

        this.liquidacionesCounter = Counter.builder("msbiosuper.business.liquidaciones.count")
                .description("Número total de consultas de liquidaciones")
                .tag("operation", "liquidaciones")
                .register(meterRegistry);

        this.ventasCounter = Counter.builder("msbiosuper.business.ventas.count")
                .description("Número total de consultas de ventas")
                .tag("operation", "ventas")
                .register(meterRegistry);

        this.cuentasPorCobrarCounter = Counter.builder("msbiosuper.business.cuentas_por_cobrar.count")
                .description("Número total de consultas de cuentas por cobrar")
                .tag("operation", "cuentas_por_cobrar")
                .register(meterRegistry);

        this.cuentasPorPagarCounter = Counter.builder("msbiosuper.business.cuentas_por_pagar.count")
                .description("Número total de consultas de cuentas por pagar")
                .tag("operation", "cuentas_por_pagar")
                .register(meterRegistry);

        this.productosCounter = Counter.builder("msbiosuper.business.productos.count")
                .description("Número total de consultas de productos")
                .tag("operation", "productos")
                .register(meterRegistry);

        // Inicializar timers para latencia
        this.resumenPescaTimer = Timer.builder("msbiosuper.business.resumen_pesca.duration")
                .description("Tiempo de ejecución de consultas de resumen de pesca")
                .tag("operation", "resumen_pesca")
                .register(meterRegistry);

        this.resumenSiembraTimer = Timer.builder("msbiosuper.business.resumen_siembra.duration")
                .description("Tiempo de ejecución de consultas de resumen de siembra")
                .tag("operation", "resumen_siembra")
                .register(meterRegistry);

        this.consumosDiariosTimer = Timer.builder("msbiosuper.business.consumos_diarios.duration")
                .description("Tiempo de ejecución de consultas de consumos diarios")
                .tag("operation", "consumos_diarios")
                .register(meterRegistry);

        this.liquidacionesTimer = Timer.builder("msbiosuper.business.liquidaciones.duration")
                .description("Tiempo de ejecución de consultas de liquidaciones")
                .tag("operation", "liquidaciones")
                .register(meterRegistry);

        this.ventasTimer = Timer.builder("msbiosuper.business.ventas.duration")
                .description("Tiempo de ejecución de consultas de ventas")
                .tag("operation", "ventas")
                .register(meterRegistry);

        this.cuentasPorCobrarTimer = Timer.builder("msbiosuper.business.cuentas_por_cobrar.duration")
                .description("Tiempo de ejecución de consultas de cuentas por cobrar")
                .tag("operation", "cuentas_por_cobrar")
                .register(meterRegistry);

        this.cuentasPorPagarTimer = Timer.builder("msbiosuper.business.cuentas_por_pagar.duration")
                .description("Tiempo de ejecución de consultas de cuentas por pagar")
                .tag("operation", "cuentas_por_pagar")
                .register(meterRegistry);
    }

    // Métodos para incrementar contadores

    /**
     * Incrementa el contador de consultas de resumen de pesca.
     */
    public void incrementResumenPescaCount() {
        resumenPescaCounter.increment();
    }

    /**
     * Incrementa el contador de consultas de resumen de siembra.
     */
    public void incrementResumenSiembraCount() {
        resumenSiembraCounter.increment();
    }

    /**
     * Incrementa el contador de consultas de consumos diarios.
     */
    public void incrementConsumosDiariosCount() {
        consumosDiariosCounter.increment();
    }

    /**
     * Incrementa el contador de consultas de liquidaciones.
     */
    public void incrementLiquidacionesCount() {
        liquidacionesCounter.increment();
    }

    /**
     * Incrementa el contador de consultas de ventas.
     */
    public void incrementVentasCount() {
        ventasCounter.increment();
    }

    /**
     * Incrementa el contador de consultas de cuentas por cobrar.
     */
    public void incrementCuentasPorCobrarCount() {
        cuentasPorCobrarCounter.increment();
    }

    /**
     * Incrementa el contador de consultas de cuentas por pagar.
     */
    public void incrementCuentasPorPagarCount() {
        cuentasPorPagarCounter.increment();
    }

    /**
     * Incrementa el contador de consultas de productos.
     */
    public void incrementProductosCount() {
        productosCounter.increment();
    }

    // Métodos para registrar latencia

    /**
     * Registra el tiempo de ejecución de una consulta de resumen de pesca.
     * 
     * @param durationMs Duración en milisegundos
     */
    public void recordResumenPescaDuration(long durationMs) {
        resumenPescaTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Registra el tiempo de ejecución de una consulta de resumen de siembra.
     * 
     * @param durationMs Duración en milisegundos
     */
    public void recordResumenSiembraDuration(long durationMs) {
        resumenSiembraTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Registra el tiempo de ejecución de una consulta de consumos diarios.
     * 
     * @param durationMs Duración en milisegundos
     */
    public void recordConsumosDiariosDuration(long durationMs) {
        consumosDiariosTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Registra el tiempo de ejecución de una consulta de liquidaciones.
     * 
     * @param durationMs Duración en milisegundos
     */
    public void recordLiquidacionesDuration(long durationMs) {
        liquidacionesTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Registra el tiempo de ejecución de una consulta de ventas.
     * 
     * @param durationMs Duración en milisegundos
     */
    public void recordVentasDuration(long durationMs) {
        ventasTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Registra el tiempo de ejecución de una consulta de cuentas por cobrar.
     * 
     * @param durationMs Duración en milisegundos
     */
    public void recordCuentasPorCobrarDuration(long durationMs) {
        cuentasPorCobrarTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Registra el tiempo de ejecución de una consulta de cuentas por pagar.
     * 
     * @param durationMs Duración en milisegundos
     */
    public void recordCuentasPorPagarDuration(long durationMs) {
        cuentasPorPagarTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Registra una métrica personalizada con un valor específico.
     * 
     * @param metricName Nombre de la métrica
     * @param value Valor a registrar
     * @param tags Tags adicionales para la métrica
     */
    public void recordCustomMetric(String metricName, double value, String... tags) {
        Counter.builder(metricName)
                .tags(tags)
                .register(meterRegistry)
                .increment(value);
    }

    /**
     * Registra el número de resultados devueltos por una operación.
     * 
     * @param operation Nombre de la operación
     * @param resultCount Número de resultados
     */
    public void recordResultCount(String operation, int resultCount) {
        Counter.builder("msbiosuper.business.result_count")
                .description("Número de resultados devueltos por operación")
                .tag("operation", operation)
                .register(meterRegistry)
                .increment(resultCount);
    }

    /**
     * Registra un error en una operación de negocio.
     * 
     * @param operation Nombre de la operación
     * @param errorType Tipo de error
     */
    public void recordBusinessError(String operation, String errorType) {
        Counter.builder("msbiosuper.business.errors")
                .description("Errores en operaciones de negocio")
                .tag("operation", operation)
                .tag("error_type", errorType)
                .register(meterRegistry)
                .increment();
    }
}
