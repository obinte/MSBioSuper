package msbiosuper

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
 * Clase base para todas las simulaciones de Gatling de MSBioSuper.
 * Proporciona configuración común y utilidades compartidas.
 */
abstract class BaseSimulation extends Simulation {

  // Configuración HTTP base
  val baseUrl = System.getProperty("baseUrl", "http://localhost:8080")
  
  // Token JWT para autenticación (debe ser generado previamente o durante la prueba)
  val jwtToken = System.getProperty("test.jwt.token", "")
  
  // Configuración HTTP común
  val httpProtocol = http
    .baseUrl(baseUrl)
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .userAgentHeader("Gatling Performance Test")
    .authorizationHeader(s"Bearer $jwtToken")
  
  // Headers comunes para todas las peticiones
  val commonHeaders = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Authorization" -> s"Bearer $jwtToken"
  )
  
  // Parámetros de prueba comunes
  val fechaInicio = System.getProperty("test.fechaInicio", "2024-01-01")
  val fechaFin = System.getProperty("test.fechaFin", "2024-12-31")
  val empresaCodigo = System.getProperty("test.empresaCodigo", "001")
  val piscinaCodigo = System.getProperty("test.piscinaCodigo", "P001")
  
  // Configuración de usuarios y duración
  val loadUsers = Integer.getInteger("load.users", 100)
  val rampDuration = Integer.getInteger("load.rampDuration", 60)
  val testDuration = Integer.getInteger("load.testDuration", 300)
  
  /**
   * Crea un cuerpo de petición JSON genérico con fechas y código de empresa
   */
  def createRequestBody(additionalFields: Map[String, Any] = Map()): String = {
    val baseFields = Map(
      "fechaInicio" -> fechaInicio,
      "fechaFin" -> fechaFin,
      "empresaCodigo" -> empresaCodigo
    )
    val allFields = baseFields ++ additionalFields
    
    val jsonFields = allFields.map {
      case (key, value: String) => s""""$key": "$value""""
      case (key, value: Int) => s""""$key": $value"""
      case (key, value: Boolean) => s""""$key": $value"""
      case (key, value) => s""""$key": "$value""""
    }.mkString(", ")
    
    s"{$jsonFields}"
  }
  
  /**
   * Configuración de aserciones comunes para todas las simulaciones
   */
  def setupCommonAssertions(): Unit = {
    setUp(
      // Las subclases deben definir sus escenarios
    ).protocols(httpProtocol)
      .assertions(
        global.responseTime.max.lt(5000),           // Tiempo máximo de respuesta < 5s
        global.responseTime.mean.lt(1000),          // Tiempo medio de respuesta < 1s
        global.responseTime.percentile3.lt(500),    // P95 < 500ms (objetivo TR-22)
        global.successfulRequests.percent.gt(95)    // Tasa de éxito > 95%
      )
  }
}
