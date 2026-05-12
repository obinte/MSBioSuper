package msbiosuper

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
 * Simulación de prueba de carga para MSBioSuper.
 * 
 * Prueba los 12 endpoints principales con carga normal:
 * - 100 usuarios concurrentes (configurable)
 * - Rampa de 60 segundos
 * - Duración total de 5 minutos
 * 
 * Objetivos de rendimiento (TR-22):
 * - Tiempo de respuesta API: p95 < 500ms
 * - Throughput: > 1000 req/sec
 * - Tasa de éxito: > 95%
 * 
 * Ejecución:
 * mvn gatling:test -Dgatling.simulationClass=msbiosuper.LoadTestSimulation
 */
class LoadTestSimulation extends BaseSimulation {

  // Escenario 1: Resumen de Pesca
  val resumenPescaScenario = scenario("Resumen de Pesca")
    .exec(
      http("GET Lista Resumen Pesca")
        .post("/api/v1/biosuperController/getListaResumenPesca")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 2: Resumen de Siembra
  val resumenSiembraScenario = scenario("Resumen de Siembra")
    .exec(
      http("GET Lista Resumen Siembra")
        .post("/api/v1/biosuperController/getListaResumenSiembra")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 3: Consumos Diarios
  val consumosDiariosScenario = scenario("Consumos Diarios")
    .exec(
      http("GET Consumos Diarios")
        .post("/api/v1/biosuperController/getConsumosDiarios")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 4: Liquidaciones Detalle Producto
  val liquidacionDetalleScenario = scenario("Liquidación Detalle Producto")
    .exec(
      http("GET Liquidación Detalle Producto")
        .post("/api/v1/biosuperController/getListadoLiquidacionDetalleProducto")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 5: Listar Liquidaciones Detalle
  val listarLiquidacionesScenario = scenario("Listar Liquidaciones Detalle")
    .exec(
      http("GET Listar Liquidaciones Detalle")
        .post("/api/v1/biosuperController/listarLiquidacionesDetalle")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 6: Costos por Piscina
  val costosPorPiscinaScenario = scenario("Costos por Piscina")
    .exec(
      http("GET Costos por Piscina")
        .post("/api/v1/biosuperController/listarCostosPorPiscina")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody(Map("piscinaCodigo" -> piscinaCodigo))))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 7: Gramajes
  val gramajesScenario = scenario("Obtener Gramajes")
    .exec(
      http("GET Listado Gramajes")
        .post("/api/v1/biosuperController/obtenerListadoGramajes")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 8: Compras Detalle Producto
  val comprasDetalleScenario = scenario("Compras Detalle Producto")
    .exec(
      http("GET Compras Detalle Producto")
        .post("/api/v1/biosuperController/getListadoComprasDetalleProducto")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 9: Ventas BioSuper
  val ventasScenario = scenario("Listar Ventas BioSuper")
    .exec(
      http("GET Ventas BioSuper")
        .post("/api/v1/biosuperController/listarVentasBioSuper")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 10: Productos General
  val productosGeneralScenario = scenario("Productos General")
    .exec(
      http("GET Productos General")
        .post("/api/v1/biosuperController/getListaProductosGeneralTO")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 11: Cuentas por Cobrar
  val cuentasPorCobrarScenario = scenario("Cuentas por Cobrar")
    .exec(
      http("GET Cuentas por Cobrar")
        .post("/api/v1/biosuperController/getApiCarListaCuentasPorCobrarDetalladoTO")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Escenario 12: Cuentas por Pagar
  val cuentasPorPagarScenario = scenario("Cuentas por Pagar")
    .exec(
      http("GET Cuentas por Pagar")
        .post("/api/v1/biosuperController/getCarListaCuentasPorPagarDetalladoTO")
        .headers(commonHeaders)
        .body(StringBody(createRequestBody()))
        .check(status.is(200))
        .check(responseTimeInMillis.lte(500))
    )
    .pause(1, 3)

  // Configuración de la simulación con todos los escenarios
  setUp(
    resumenPescaScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    resumenSiembraScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    consumosDiariosScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    liquidacionDetalleScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    listarLiquidacionesScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    costosPorPiscinaScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    gramajesScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    comprasDetalleScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    ventasScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    productosGeneralScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    cuentasPorCobrarScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds)),
    cuentasPorPagarScenario.inject(rampUsers(loadUsers / 12).during(rampDuration.seconds))
  ).protocols(httpProtocol)
    .maxDuration(testDuration.seconds)
    .assertions(
      global.responseTime.max.lt(5000),           // Tiempo máximo < 5s
      global.responseTime.mean.lt(1000),          // Tiempo medio < 1s
      global.responseTime.percentile3.lt(500),    // P95 < 500ms (objetivo TR-22)
      global.responseTime.percentile4.lt(1000),   // P99 < 1s
      global.successfulRequests.percent.gt(95),   // Tasa de éxito > 95%
      global.requestsPerSec.gte(50)               // Throughput mínimo 50 req/s
    )
}
