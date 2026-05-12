package msbiosuper

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
 * Simulación de Benchmark Base para MSBioSuper.
 * 
 * Esta simulación establece líneas base de rendimiento midiendo:
 * 1. Tiempos de respuesta de API por endpoint
 * 2. Throughput (requests/segundo)
 * 3. Rendimiento de consultas de base de datos (indirectamente)
 * 
 * Objetivos de rendimiento (TR-22, NFR-1, NFR-2):
 * - Tiempo de respuesta API: p50 < 200ms, p95 < 500ms, p99 < 1000ms
 * - Throughput: > 1000 req/sec (bajo carga máxima)
 * - Tiempo de consulta DB: p95 < 100ms (medido indirectamente)
 * - Tasa de éxito: > 95%
 * 
 * Ejecución:
 * mvn gatling:test -Dgatling.simulationClass=msbiosuper.BenchmarkSimulation
 * 
 * Con parámetros personalizados:
 * mvn gatling:test -Dgatling.simulationClass=msbiosuper.BenchmarkSimulation \
 *   -Dbenchmark.users=50 \
 *   -Dbenchmark.duration=180 \
 *   -Dtest.jwt.token="<token>"
 */
class BenchmarkSimulation extends BaseSimulation {

  // Configuración específica de benchmark
  val benchmarkUsers = Integer.getInteger("benchmark.users", 50)
  val benchmarkDuration = Integer.getInteger("benchmark.duration", 300) // 5 minutos por defecto
  val thinkTime = Integer.getInteger("benchmark.thinkTime", 2) // Pausa entre requests

  /**
   * Escenario de benchmark para cada endpoint individual.
   * Permite medir el rendimiento específico de cada operación.
   */
  
  // 1. Resumen de Pesca - Operación de lectura compleja con agregaciones
  val benchmarkResumenPesca = scenario("Benchmark: Resumen Pesca")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Resumen Pesca")
          .post("/api/v1/biosuperController/getListaResumenPesca")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Resumen Pesca] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 2. Resumen de Siembra - Operación de lectura con joins
  val benchmarkResumenSiembra = scenario("Benchmark: Resumen Siembra")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Resumen Siembra")
          .post("/api/v1/biosuperController/getListaResumenSiembra")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Resumen Siembra] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 3. Consumos Diarios - Operación de lectura con filtros de fecha
  val benchmarkConsumosDiarios = scenario("Benchmark: Consumos Diarios")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Consumos Diarios")
          .post("/api/v1/biosuperController/getConsumosDiarios")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Consumos Diarios] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 4. Liquidación Detalle - Operación compleja con múltiples joins
  val benchmarkLiquidacionDetalle = scenario("Benchmark: Liquidación Detalle")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Liquidación Detalle")
          .post("/api/v1/biosuperController/getListadoLiquidacionDetalleProducto")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Liquidación Detalle] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 5. Listar Liquidaciones - Operación de listado con paginación
  val benchmarkListarLiquidaciones = scenario("Benchmark: Listar Liquidaciones")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Listar Liquidaciones")
          .post("/api/v1/biosuperController/listarLiquidacionesDetalle")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Listar Liquidaciones] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 6. Costos por Piscina - Operación con filtro específico
  val benchmarkCostosPorPiscina = scenario("Benchmark: Costos por Piscina")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Costos por Piscina")
          .post("/api/v1/biosuperController/listarCostosPorPiscina")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody(Map("piscinaCodigo" -> piscinaCodigo))))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Costos por Piscina] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 7. Gramajes - Operación de lectura simple
  val benchmarkGramajes = scenario("Benchmark: Gramajes")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Gramajes")
          .post("/api/v1/biosuperController/obtenerListadoGramajes")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Gramajes] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 8. Compras Detalle - Operación de lectura con agregaciones
  val benchmarkComprasDetalle = scenario("Benchmark: Compras Detalle")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Compras Detalle")
          .post("/api/v1/biosuperController/getListadoComprasDetalleProducto")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Compras Detalle] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 9. Ventas - Operación de lectura con cálculos
  val benchmarkVentas = scenario("Benchmark: Ventas")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Ventas")
          .post("/api/v1/biosuperController/listarVentasBioSuper")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Ventas] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 10. Productos General - Operación de catálogo
  val benchmarkProductosGeneral = scenario("Benchmark: Productos General")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Productos General")
          .post("/api/v1/biosuperController/getListaProductosGeneralTO")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Productos General] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 11. Cuentas por Cobrar - Operación financiera compleja
  val benchmarkCuentasPorCobrar = scenario("Benchmark: Cuentas por Cobrar")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Cuentas por Cobrar")
          .post("/api/v1/biosuperController/getApiCarListaCuentasPorCobrarDetalladoTO")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Cuentas por Cobrar] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  // 12. Cuentas por Pagar - Operación financiera compleja
  val benchmarkCuentasPorPagar = scenario("Benchmark: Cuentas por Pagar")
    .during(benchmarkDuration.seconds) {
      exec(
        http("Cuentas por Pagar")
          .post("/api/v1/biosuperController/getCarListaCuentasPorPagarDetalladoTO")
          .headers(commonHeaders)
          .body(StringBody(createRequestBody()))
          .check(status.is(200))
          .check(jsonPath("$").exists)
          .check(responseTimeInMillis.saveAs("responseTime"))
      )
      .exec { session =>
        val responseTime = session("responseTime").as[Int]
        println(s"[Cuentas por Pagar] Response time: ${responseTime}ms")
        session
      }
      .pause(thinkTime.seconds)
    }

  /**
   * Configuración de la simulación de benchmark.
   * 
   * Estrategia:
   * - Ejecuta todos los endpoints en paralelo con carga constante
   * - Cada endpoint tiene usuarios dedicados para medición aislada
   * - Duración configurable para establecer líneas base confiables
   */
  setUp(
    benchmarkResumenPesca.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkResumenSiembra.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkConsumosDiarios.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkLiquidacionDetalle.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkListarLiquidaciones.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkCostosPorPiscina.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkGramajes.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkComprasDetalle.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkVentas.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkProductosGeneral.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkCuentasPorCobrar.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds)),
    benchmarkCuentasPorPagar.inject(constantUsersPerSec(benchmarkUsers / 12.0).during(benchmarkDuration.seconds))
  ).protocols(httpProtocol)
    .assertions(
      // Aserciones de tiempo de respuesta (NFR-1)
      global.responseTime.percentile1.lt(200),    // P50 < 200ms
      global.responseTime.percentile3.lt(500),    // P95 < 500ms (objetivo TR-22)
      global.responseTime.percentile4.lt(1000),   // P99 < 1000ms
      global.responseTime.max.lt(5000),           // Máximo < 5s
      
      // Aserciones de throughput (NFR-2)
      global.requestsPerSec.gte(benchmarkUsers / 2.0), // Throughput mínimo
      
      // Aserciones de tasa de éxito
      global.successfulRequests.percent.gt(95),   // Tasa de éxito > 95%
      
      // Aserciones por endpoint individual para identificar cuellos de botella
      details("Resumen Pesca").responseTime.percentile3.lt(500),
      details("Resumen Siembra").responseTime.percentile3.lt(500),
      details("Consumos Diarios").responseTime.percentile3.lt(500),
      details("Liquidación Detalle").responseTime.percentile3.lt(500),
      details("Listar Liquidaciones").responseTime.percentile3.lt(500),
      details("Costos por Piscina").responseTime.percentile3.lt(500),
      details("Gramajes").responseTime.percentile3.lt(500),
      details("Compras Detalle").responseTime.percentile3.lt(500),
      details("Ventas").responseTime.percentile3.lt(500),
      details("Productos General").responseTime.percentile3.lt(500),
      details("Cuentas por Cobrar").responseTime.percentile3.lt(500),
      details("Cuentas por Pagar").responseTime.percentile3.lt(500)
    )
}
