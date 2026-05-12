package msbiosuper

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
 * Simulación de prueba de resistencia (soak test) para MSBioSuper.
 * 
 * Prueba el sistema bajo carga sostenida durante un período prolongado:
 * - 100 usuarios concurrentes constantes
 * - Duración: 1 hora (configurable)
 * 
 * Objetivo: Detectar problemas de:
 * - Fugas de memoria
 * - Degradación de rendimiento con el tiempo
 * - Agotamiento de recursos (conexiones DB, file descriptors, etc.)
 * - Problemas de garbage collection
 * 
 * Ejecución:
 * mvn gatling:test -Dgatling.simulationClass=msbiosuper.SoakTestSimulation \
 *   -Dload.users=100 -Dload.testDuration=3600
 * 
 * NOTA: Esta prueba debe ejecutarse en un entorno aislado y monitorearse
 * con herramientas como Prometheus/Grafana para observar métricas de sistema.
 */
class SoakTestSimulation extends BaseSimulation {

  val soakUsers = Integer.getInteger("load.users", 100)
  val soakDuration = Integer.getInteger("load.testDuration", 3600)  // 1 hora por defecto

  // Escenario realista que simula flujo de trabajo completo
  val realisticWorkflowScenario = scenario("Flujo de Trabajo Realista")
    .exec(
      // 1. Consultar resumen de pesca
      http("Consultar Resumen Pesca")
        .post("/api/v1/biosuperController/getListaResumenPesca")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(2, 5)
    .exec(
      // 2. Consultar consumos diarios
      http("Consultar Consumos Diarios")
        .post("/api/v1/biosuperController/getConsumosDiarios")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(3, 7)
    .exec(
      // 3. Consultar liquidaciones
      http("Consultar Liquidaciones")
        .post("/api/v1/biosuperController/listarLiquidacionesDetalle")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(2, 5)
    .exec(
      // 4. Consultar productos
      http("Consultar Productos")
        .post("/api/v1/biosuperController/getListaProductosGeneralTO")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(5, 10)
    .exec(
      // 5. Consultar cuentas por cobrar
      http("Consultar Cuentas por Cobrar")
        .post("/api/v1/biosuperController/getApiCarListaCuentasPorCobrarDetalladoTO")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(10, 20)  // Pausa más larga simulando tiempo de análisis del usuario

  // Configuración de inyección con carga constante
  setUp(
    realisticWorkflowScenario.inject(
      // Rampa inicial suave (5 minutos)
      rampUsers(soakUsers).during(300.seconds),
      
      // Mantener carga constante durante toda la duración
      constantUsersPerSec(soakUsers / 10).during((soakDuration - 300).seconds)
    )
  ).protocols(httpProtocol)
    .maxDuration(soakDuration.seconds)
    .assertions(
      // Verificar que el rendimiento se mantiene estable durante toda la prueba
      global.responseTime.max.lt(5000),           // Tiempo máximo < 5s
      global.responseTime.mean.lt(1000),          // Tiempo medio < 1s
      global.responseTime.percentile3.lt(500),    // P95 < 500ms (objetivo TR-22)
      global.successfulRequests.percent.gt(99),   // Tasa de éxito > 99% (alta estabilidad)
      
      // Verificar que no hay degradación significativa con el tiempo
      // (esto se debe monitorear visualmente en los reportes de Gatling)
      forAll.responseTime.percentile3.lt(1000)    // P95 < 1s para todos los endpoints
    )
}
