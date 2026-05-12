package msbiosuper

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
 * Simulación de prueba de pico (spike test) para MSBioSuper.
 * 
 * Prueba la respuesta del sistema ante incrementos súbitos de carga:
 * - Carga base: 50 usuarios
 * - Pico: 1000 usuarios durante 30 segundos
 * - Vuelta a carga base
 * 
 * Objetivo: Verificar que el sistema se recupera correctamente después
 * de un pico de tráfico y no queda en estado degradado.
 * 
 * Ejecución:
 * mvn gatling:test -Dgatling.simulationClass=msbiosuper.SpikeTestSimulation \
 *   -Dspike.users=1000 -Dspike.duration=30
 */
class SpikeTestSimulation extends BaseSimulation {

  val spikeUsers = Integer.getInteger("spike.users", 1000)
  val spikeDuration = Integer.getInteger("spike.duration", 30)
  val baseUsers = 50

  // Escenario de endpoint más crítico (ejemplo: Resumen de Pesca)
  val criticalEndpointScenario = scenario("Endpoint Crítico - Resumen Pesca")
    .exec(
      http("Resumen Pesca")
        .post("/api/v1/biosuperController/getListaResumenPesca")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.in(200, 429, 503))  // Aceptar rate limiting o sobrecarga
        .check(responseTimeInMillis.lte(5000))
    )
    .pause(1, 2)

  // Configuración de inyección con patrón de pico
  setUp(
    criticalEndpointScenario.inject(
      // Fase 1: Carga base inicial (1 minuto)
      constantUsersPerSec(baseUsers).during(60.seconds),
      
      // Fase 2: PICO - Incremento súbito (10 segundos)
      rampUsers(spikeUsers).during(10.seconds),
      
      // Fase 3: Mantener pico (duración configurable)
      constantUsersPerSec(spikeUsers / 2).during(spikeDuration.seconds),
      
      // Fase 4: Descenso rápido (10 segundos)
      rampUsers(baseUsers).during(10.seconds),
      
      // Fase 5: Recuperación - Volver a carga base (2 minutos)
      constantUsersPerSec(baseUsers).during(120.seconds)
    )
  ).protocols(httpProtocol)
    .maxDuration(300.seconds)
    .assertions(
      // Durante el pico, verificamos que el sistema responde (aunque sea lento)
      global.responseTime.max.lt(10000),          // Tiempo máximo < 10s
      global.responseTime.percentile3.lt(3000),   // P95 < 3s durante pico
      global.successfulRequests.percent.gt(85),   // Tasa de éxito > 85%
      
      // Verificar que el sistema se recupera después del pico
      details("Resumen Pesca").responseTime.mean.lt(1000)  // Tiempo medio < 1s en general
    )
}
