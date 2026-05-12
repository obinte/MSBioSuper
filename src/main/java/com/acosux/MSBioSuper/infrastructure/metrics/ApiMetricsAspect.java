package com.acosux.MSBioSuper.infrastructure.metrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspecto para capturar métricas de endpoints API automáticamente.
 * 
 * Este aspecto intercepta todas las llamadas a los controladores REST
 * y registra métricas de:
 * - Latencia de endpoints
 * - Contadores de invocaciones
 * - Número de resultados
 * - Errores
 * 
 * Utiliza AspectJ para interceptar métodos sin modificar el código de los controladores.
 * 
 * @author MSBioSuper Team
 * @version 1.0
 * @since 2024
 */
@Aspect
@Component
public class ApiMetricsAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApiMetricsAspect.class);

    private final BusinessMetricsService metricsService;

    public ApiMetricsAspect(BusinessMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    /**
     * Pointcut que captura todos los métodos públicos en los controladores REST.
     */
    @Pointcut("execution(* com.acosux.MSBiosuper.infrastructure.rest.*Controller.*(..))")
    public void controllerMethods() {
    }

    /**
     * Advice que se ejecuta alrededor de cada método del controlador.
     * Registra la latencia y el contador de invocaciones.
     * 
     * @param joinPoint Punto de unión de AspectJ
     * @return Resultado del método interceptado
     * @throws Throwable Si ocurre un error durante la ejecución
     */
    @Around("controllerMethods()")
    public Object measureApiLatency(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();

        try {
            // Ejecutar el método original
            Object result = joinPoint.proceed();
            
            // Calcular duración
            long duration = System.currentTimeMillis() - startTime;
            
            // Registrar métricas según el método
            recordMetrics(methodName, duration, result);
            
            logger.debug("Endpoint {} ejecutado en {} ms", methodName, duration);
            
            return result;
            
        } catch (Exception e) {
            // Registrar error
            long duration = System.currentTimeMillis() - startTime;
            metricsService.recordBusinessError(methodName, e.getClass().getSimpleName());
            logger.error("Error en endpoint {} después de {} ms: {}", methodName, duration, e.getMessage());
            throw e;
        }
    }

    /**
     * Registra las métricas específicas según el método invocado.
     * 
     * @param methodName Nombre del método
     * @param duration Duración en milisegundos
     * @param result Resultado del método
     */
    private void recordMetrics(String methodName, long duration, Object result) {
        switch (methodName) {
            case "getListaResumenPesca":
                metricsService.incrementResumenPescaCount();
                metricsService.recordResumenPescaDuration(duration);
                recordResultCountFromResponse(result, "resumen_pesca");
                break;
                
            case "getListaResumenSiembra":
                metricsService.incrementResumenSiembraCount();
                metricsService.recordResumenSiembraDuration(duration);
                recordResultCountFromResponse(result, "resumen_siembra");
                break;
                
            case "getConsumosDiarios":
                metricsService.incrementConsumosDiariosCount();
                metricsService.recordConsumosDiariosDuration(duration);
                recordResultCountFromResponse(result, "consumos_diarios");
                break;
                
            case "getListadoLiquidacionDetalleProducto":
            case "listarLiquidacionesDetalle":
                metricsService.incrementLiquidacionesCount();
                metricsService.recordLiquidacionesDuration(duration);
                recordResultCountFromResponse(result, "liquidaciones");
                break;
                
            case "listarVentasBioSuper":
                metricsService.incrementVentasCount();
                metricsService.recordVentasDuration(duration);
                recordResultCountFromResponse(result, "ventas");
                break;
                
            case "getApiCarListaCuentasPorCobrarDetalladoTO":
                metricsService.incrementCuentasPorCobrarCount();
                metricsService.recordCuentasPorCobrarDuration(duration);
                recordResultCountFromResponse(result, "cuentas_por_cobrar");
                break;
                
            case "getCarListaCuentasPorPagarDetalladoTO":
                metricsService.incrementCuentasPorPagarCount();
                metricsService.recordCuentasPorPagarDuration(duration);
                recordResultCountFromResponse(result, "cuentas_por_pagar");
                break;
                
            case "getListaProductosGeneralTO":
                metricsService.incrementProductosCount();
                recordResultCountFromResponse(result, "productos");
                break;
                
            default:
                // Registrar métrica genérica para otros endpoints
                metricsService.recordCustomMetric(
                    "msbiosuper.api.endpoint.duration",
                    duration,
                    "endpoint", methodName
                );
                break;
        }
    }

    /**
     * Extrae el número de resultados de la respuesta y lo registra como métrica.
     * 
     * @param result Resultado del método
     * @param operation Nombre de la operación
     */
    private void recordResultCountFromResponse(Object result, String operation) {
        try {
            if (result != null) {
                // Intentar extraer el tamaño de la lista de resultados
                // Esto asume que el resultado es un RespuestaWebTO con extraInfo
                var respuesta = (com.acosux.MSBioSuper.util.RespuestaWebTO) result;
                if (respuesta.getExtraInfo() instanceof java.util.List) {
                    int count = ((java.util.List<?>) respuesta.getExtraInfo()).size();
                    metricsService.recordResultCount(operation, count);
                }
            }
        } catch (Exception e) {
            // Si no se puede extraer el conteo, simplemente ignorar
            logger.trace("No se pudo extraer el conteo de resultados para {}: {}", operation, e.getMessage());
        }
    }
}
