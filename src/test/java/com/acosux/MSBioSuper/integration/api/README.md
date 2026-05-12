# Pruebas de Integración de API - MSBioSuper

## Descripción

Este paquete contiene las pruebas de integración de API para el proyecto MSBioSuper. Las pruebas verifican el comportamiento end-to-end de los endpoints REST, incluyendo autenticación JWT, validación de entrada y manejo de errores.

## Estructura de Pruebas

### BaseApiIntegrationTest.java

Clase base abstracta que proporciona configuración común para todas las pruebas de API:

- **Configuración de MockMvc**: Para realizar peticiones HTTP simuladas
- **Testcontainers PostgreSQL**: Base de datos real para pruebas de integración (heredado de BaseIntegrationTest)
- **Generación de tokens JWT**: Métodos helper para autenticación
- **Métodos helper**: Para crear JSON de solicitudes de prueba

**Métodos helper disponibles**:
- `generateToken(username, password, expirationDays)`: Genera un token JWT personalizado
- `createBasicRequestJson(empresa, sector, fechaDesde, fechaHasta)`: JSON básico para endpoints de resumen
- `createFullRequestJson(...)`: JSON completo con todos los campos
- `createProductoRequestJson(empresa, bodega)`: JSON para endpoint de productos
- `createCuentasRequestJson(empresa, fechaDesde, fechaHasta)`: JSON para cuentas por cobrar/pagar
- `createLiquidacionRequestJson(...)`: JSON para liquidaciones
- `createConsumosDiariosRequestJson(...)`: JSON para consumos diarios

### SecurityControllerIntegrationTest.java

Pruebas para el endpoint de autenticación JWT:

**Endpoint probado**:
- `GET /api/v1/token/{user}/{password}/{timeExpiredDay}`

**Escenarios de prueba**:
1. ✅ Generación exitosa de token con credenciales válidas
2. ❌ Rechazo con usuario inválido (401 Unauthorized)
3. ❌ Rechazo con contraseña inválida (401 Unauthorized)
4. ❌ Validación de tiempo de expiración inválido (400 Bad Request)
5. ✅ Generación de tokens con diferentes tiempos de expiración
6. ❌ Rechazo con usuario vacío
7. ❌ Rechazo con contraseña vacía
8. ✅ Tokens diferentes en solicitudes consecutivas

### MSBioSuperControllerIntegrationTest.java

Pruebas para los 12 endpoints REST del controlador principal:

**Endpoints probados**:
1. `POST /api/v1/biosuperController/getListaResumenPesca` - Resumen de pesca
2. `POST /api/v1/biosuperController/getListaResumenSiembra` - Resumen de siembra
3. `POST /api/v1/biosuperController/getConsumosDiarios` - Consumos diarios
4. `POST /api/v1/biosuperController/getListadoLiquidacionDetalleProducto` - Liquidaciones detalle
5. `POST /api/v1/biosuperController/listarLiquidacionesDetalle` - Liquidaciones listado
6. `POST /api/v1/biosuperController/listarCostosPorPiscina` - Costos por piscina
7. `POST /api/v1/biosuperController/obtenerListadoGramajes` - Gramajes
8. `POST /api/v1/biosuperController/getListadoComprasDetalleProducto` - Compras detalle
9. `POST /api/v1/biosuperController/listarVentasBioSuper` - Ventas
10. `POST /api/v1/biosuperController/getListaProductosGeneralTO` - Productos general
11. `POST /api/v1/biosuperController/getApiCarListaCuentasPorCobrarDetalladoTO` - Cuentas por cobrar
12. `POST /api/v1/biosuperController/getCarListaCuentasPorPagarDetalladoTO` - Cuentas por pagar

**Escenarios de prueba por endpoint**:
- ✅ **Happy path**: Request válido con token → 200 OK con estructura RespuestaWebTO
- ❌ **Sin autenticación**: Request sin token → 401 Unauthorized
- ❌ **Token inválido**: Request con token malformado → 401 Unauthorized
- ❌ **Validación**: Request con datos inválidos → 400 Bad Request
- ⚠️ **Sin resultados**: Request válido pero sin datos → 200 OK con advertencia

**Organización**:
- Las pruebas están organizadas en clases anidadas (`@Nested`) por endpoint
- Cada clase anidada contiene las pruebas específicas para ese endpoint
- Uso de `@DisplayName` para descripciones claras de cada prueba

## Tecnologías Utilizadas

- **JUnit 5**: Framework de pruebas
- **MockMvc**: Para simular peticiones HTTP
- **Testcontainers**: PostgreSQL real para pruebas de integración
- **Spring Boot Test**: Configuración de contexto de Spring
- **Hamcrest**: Matchers para assertions más expresivas
- **AssertJ**: Assertions fluidas (disponible si se necesita)

## Ejecución de Pruebas

### Ejecutar todas las pruebas de integración de API

```bash
mvn test -Dtest="*IntegrationTest"
```

### Ejecutar solo pruebas de SecurityController

```bash
mvn test -Dtest=SecurityControllerIntegrationTest
```

### Ejecutar solo pruebas de MSBioSuperController

```bash
mvn test -Dtest=MSBioSuperControllerIntegrationTest
```

### Ejecutar con perfil de pruebas

```bash
mvn test -Dspring.profiles.active=test
```

## Configuración

### Variables de Entorno Requeridas

Las pruebas utilizan las siguientes variables de entorno (configuradas automáticamente por Testcontainers):

- `JDBC_SERVER`: Configurado por Testcontainers
- `JDBC_PORT`: Configurado por Testcontainers
- `JDBC_DATABASE`: Configurado por Testcontainers
- `JDBC_USER`: Configurado por Testcontainers
- `JDBC_PASSWORD`: Configurado por Testcontainers
- `JWT_SECRET`: Debe estar configurado en application-test.properties o .env

### Credenciales de Prueba

Las pruebas utilizan las siguientes credenciales predefinidas:

- **Usuario**: `adminbio`
- **Contraseña**: `admin123`
- **Tiempo de expiración por defecto**: 7 días

Estas credenciales están configuradas en `AuthenticateUserUseCase` y pueden ser modificadas mediante propiedades de configuración.

## Estructura de Respuesta

Todos los endpoints retornan una estructura `RespuestaWebTO` con los siguientes campos:

```json
{
  "estadoOperacion": "EXITO|ERROR|ADVERTENCIA|SIN_ACCESO",
  "operacionMensaje": "Mensaje descriptivo",
  "extraInfo": { /* Datos específicos del endpoint */ }
}
```

**Estados de operación**:
- `EXITO`: Operación exitosa con datos
- `ADVERTENCIA`: Operación exitosa pero sin datos encontrados
- `ERROR`: Error en la operación
- `SIN_ACCESO`: Sin permisos para la operación

## Datos de Prueba

Las pruebas utilizan datos de prueba mínimos:

- **Empresa**: `"01"`
- **Sector**: `"SECTOR-A"`
- **Fechas**: `"2024-01-01"` a `"2024-12-31"`
- **Bodega**: `"BODEGA-01"`

Los datos reales son generados por Testcontainers con el esquema de base de datos configurado en `spring.jpa.hibernate.ddl-auto=create-drop`.

## Mejores Prácticas

1. **Aislamiento**: Cada prueba es independiente y no depende del estado de otras pruebas
2. **Rollback automático**: Las transacciones se revierten automáticamente después de cada prueba
3. **Tokens JWT**: Se genera un token válido antes de cada prueba en `@BeforeEach`
4. **Nombres descriptivos**: Uso de `@DisplayName` para descripciones claras
5. **Organización**: Clases anidadas para agrupar pruebas relacionadas
6. **Assertions expresivas**: Uso de Hamcrest matchers para assertions legibles

## Cobertura de Pruebas

Las pruebas cubren los siguientes aspectos:

- ✅ Autenticación JWT (generación, validación, expiración)
- ✅ Autorización (acceso con/sin token)
- ✅ Validación de entrada (DTOs con @Valid)
- ✅ Manejo de errores (400, 401, 404, 500)
- ✅ Respuestas exitosas (200 OK)
- ✅ Respuestas sin resultados (advertencias)
- ✅ Estructura de respuesta RespuestaWebTO
- ✅ Todos los 12 endpoints REST

## Requisitos Cumplidos

Esta implementación cumple con los siguientes requisitos técnicos:

- **TR-20**: Pruebas de integración para capa de base de datos y API
- **TR-21**: Pruebas de API end-to-end
- **FR-1**: Mantener compatibilidad de API
- **FR-3**: Autenticación y autorización JWT
- **TR-12**: Validación de entrada

## Notas Adicionales

- Las pruebas están diseñadas para ejecutarse en cualquier entorno sin configuración adicional
- Testcontainers maneja automáticamente el ciclo de vida del contenedor PostgreSQL
- Los tokens JWT se generan dinámicamente para cada ejecución de pruebas
- Las pruebas no requieren una base de datos externa configurada

## Autor

Josias Rubén Aldaz Arcalles

## Versión

1.0 - 2026-04-24
