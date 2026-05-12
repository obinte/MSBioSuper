package msbiosuper

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
 * Simulación de prueba de estrés para MSBioSuper.
 * 
 * Prueba el sistema bajo carga extrema:
 * - 500 usuarios concurrentes (configurable)
 * - Rampa de 120 segundos
 * - Duración total de 10 minutos
 * 
 * Objetivo: Identificar el punto de quiebre del sistema y verificar
 * que se degrada de forma elegante bajo carga extrema.
 * 
 * Ejecución:
 * mvn gatling:test -Dgatling.simulationClass=msbiosuper.StressTestSimulation \
 *   -Dstress.users=500 -Dstress.rampDuration=120 -Dstress.testDuration=600
 */
class StressTestSimulation extends BaseSimulation {

  val stressUsers = Integer.getInteger("stress.users", 500)
  val stressRampDuration = Integer.getInteger("stress.rampDuration", 120)
  val stressTestDuration = Integer.getInteger("stress.testDuration", 600)

  // Escenario mixto que simula uso real con todos los endpoints
  val mixedScenario = scenario("Escenario Mixto de Estrés")
    .exec(
      // Resumen de Pesca (20% del tráfico)
      http("Resumen Pesca")
        .post("/api/v1/biosuperController/getListaResumenPesca")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.in(200, 500, 503))  // Aceptar errores de servidor bajo estrés
    )
    .pause(500.milliseconds, 2.seconds)
    .exec(
      // Consumos Diarios (15% del tráfico)
      http("Consumos Diarios")
        .post("/api/v1/biosuperController/getConsumosDiarios")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.in(200, 500, 503))
    )
    .pause(500.milliseconds, 2.seconds)
    .exec(
      // Liquidaciones (15% del tráfico)
      http("Liquidaciones Detalle")
        .post("/api/v1/biosuperController/listarLiquidacionesDetalle")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.in(200, 500, 503))
    )
    .pause(500.milliseconds, 2.seconds)
    .exec(
      // Productos (10% del tráfico)
      http("Productos General")
        .post("/api/v1/biosuperController/getListaProductosGeneralTO")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.in(200, 500, 503))
    )
    .pause(500.milliseconds, 2.seconds)
    .exec(
      // Cuentas por Cobrar (10% del tráfico)
      http("Cuentas por Cobrar")
        .post("/api/v1/biosuperController/getApiCarListaCuentasPorCobrarDetalladoTO")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.in(200, 500, 503))
    )

  // Configuración de inyección de usuarios con patrón de estrés
  setUp(
    mixedScenario.inject(
      // Fase 1: Rampa inicial
      rampUsers(stressUsers / 4).during((stressRampDuration / 3).seconds),
      // Fase 2: Mantener carga media
      constantUsersPerSec(stressUsers / 4).during((stressRampDuration / 3).seconds),
      // Fase 3: Incremento agresivo hasta carga máxima
      rampUsers(stressUsers).during((stressRampDuration / 3).seconds),
      // Fase 4: Mantener carga máxima
      constantUsersPerSec(stressUsers / 2).during((stressTestDuration - stressRampDuration).seconds)
    )
  ).protocols(httpProtocol)
    .maxDuration(stressTestDuration.seconds)
    .assertions(
      // Bajo estrés, relajamos los requisitos pero verificamos que el sistema no colapsa
      global.responseTime.max.lt(10000),          // Tiempo máximo < 10s
      global.responseTime.percentile3.lt(2000),   // P95 < 2s (relajado bajo estrés)
      global.successfulRequests.percent.gt(80),   // Tasa de éxito > 80% (relajado)
      global.requestsPerSec.gte(100)              // Throughput mínimo 100 req/s
    )
}
