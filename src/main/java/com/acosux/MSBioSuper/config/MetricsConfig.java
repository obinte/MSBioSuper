package com.acosux.MSBioSuper.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de métricas personalizadas para MSBioSuper.
 * 
 * Esta clase configura:
 * - Tags comunes para todas las métricas
 * - Métricas personalizadas de negocio
 * - Configuración de Micrometer
 * 
 * @author MSBioSuper Team
 * @version 1.0
 * @since 2024
 */
@Configuration
public class MetricsConfig {

    /**
     * Personaliza el registro de métricas con tags comunes.
     * 
     * Los tags permiten filtrar y agrupar métricas en Prometheus/Grafana.
     * 
     * @return Customizer para el MeterRegistry
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
                .commonTags(
                        "application", "msbiosuper",
                        "version", "1.0",
                        "team", "acosux"
                );
    }

    /**
     * Configura métricas personalizadas de negocio.
     * 
     * Estas métricas pueden ser utilizadas para monitorear:
     * - Operaciones de negocio específicas
     * - Tiempos de procesamiento
     * - Contadores de eventos
     * 
     * Las métricas específicas de endpoints se registran automáticamente
     * a través de BusinessMetricsService y ApiMetricsAspect.
     * 
     * @param registry Registro de métricas de Micrometer
     * @return MeterBinder con métricas personalizadas
     */
    @Bean
    public MeterBinder customMetrics(MeterRegistry registry) {
        return (reg) -> {
            // Las métricas de negocio se registran automáticamente a través de:
            // - BusinessMetricsService: Contadores y timers para operaciones
            // - ApiMetricsAspect: Interceptor AOP para capturar métricas de endpoints
            
            // Métricas disponibles:
            // - msbiosuper.business.resumen_pesca.count
            // - msbiosuper.business.resumen_pesca.duration
            // - msbiosuper.business.resumen_siembra.count
            // - msbiosuper.business.resumen_siembra.duration
            // - msbiosuper.business.consumos_diarios.count
            // - msbiosuper.business.consumos_diarios.duration
            // - msbiosuper.business.liquidaciones.count
            // - msbiosuper.business.liquidaciones.duration
            // - msbiosuper.business.ventas.count
            // - msbiosuper.business.ventas.duration
            // - msbiosuper.business.cuentas_por_cobrar.count
            // - msbiosuper.business.cuentas_por_cobrar.duration
            // - msbiosuper.business.cuentas_por_pagar.count
            // - msbiosuper.business.cuentas_por_pagar.duration
            // - msbiosuper.business.productos.count
            // - msbiosuper.business.result_count
            // - msbiosuper.business.errors
        };
    }
}
