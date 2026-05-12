# Estrategia de Versionado de API - MSBioSuper

## Información General

**Proyecto**: MSBioSuper  
**Versión Actual**: v1  
**Fecha de Implementación**: 2026-04-21  
**Requisito**: TR-15

---

## 1. Estrategia de Versionado

### 1.1 Tipo de Versionado

MSBioSuper utiliza **versionado basado en URL (URL-based versioning)** para gestionar las versiones de su API REST.

**Formato**: `/api/{version}/{recurso}`

**Ejemplo**:
- Versión 1: `https://api.msbiosuper.acosux.com/api/v1/biosuperController/getListaResumenPesca`
- Versión 2 (futura): `https://api.msbiosuper.acosux.com/api/v2/biosuperController/getListaResumenPesca`

### 1.2 Justificación

El versionado basado en URL fue seleccionado por las siguientes razones:

1. **Claridad**: La versión es explícita y visible en la URL
2. **Simplicidad**: Fácil de implementar y entender para los consumidores de la API
3. **Compatibilidad**: Permite mantener múltiples versiones simultáneamente
4. **Caché**: Facilita el cacheo de respuestas por versión
5. **Documentación**: Swagger/OpenAPI puede documentar cada versión por separado
6. **Estándar de Industria**: Ampliamente adoptado por APIs REST modernas

---

## 2. Estructura de Versiones

### 2.1 Versión Actual (v1)

**Base Path**: `/api/v1/`

**Endpoints Disponibles**:

#### Seguridad
- `POST /api/v1/token/{user}/{password}/{timeExpiredDay}` - Generar token JWT

#### Operaciones de Producción
- `POST /api/v1/biosuperController/getListaResumenPesca` - Resumen de pesca
- `POST /api/v1/biosuperController/getListaResumenSiembra` - Resumen de siembra
- `POST /api/v1/biosuperController/getConsumosDiarios` - Consumos diarios
- `POST /api/v1/biosuperController/getListadoLiquidacionDetalleProducto` - Liquidaciones detalle
- `POST /api/v1/biosuperController/listarLiquidacionesDetalle` - Liquidaciones listado
- `POST /api/v1/biosuperController/listarCostosPorPiscina` - Costos por piscina
- `POST /api/v1/biosuperController/obtenerListadoGramajes` - Gramajes
- `POST /api/v1/biosuperController/getListadoComprasDetalleProducto` - Compras detalle
- `POST /api/v1/biosuperController/listarVentasBioSuper` - Ventas
- `POST /api/v1/biosuperController/getListaProductosGeneralTO` - Productos general
- `POST /api/v1/biosuperController/getApiCarListaCuentasPorCobrarDetalladoTO` - Cuentas por cobrar
- `POST /api/v1/biosuperController/getCarListaCuentasPorPagarDetalladoTO` - Cuentas por pagar

### 2.2 Versiones Futuras

Cuando se requiera una nueva versión (v2, v3, etc.), se seguirá el siguiente proceso:

1. **Crear nueva versión**: Implementar controladores con el nuevo prefijo `/api/v2/`
2. **Mantener versión anterior**: La versión v1 continuará funcionando
3. **Período de transición**: Ambas versiones coexistirán durante un período definido
4. **Deprecación**: Marcar la versión antigua como deprecada
5. **Retiro**: Después del período de transición, retirar la versión antigua

---

## 3. Política de Compatibilidad

### 3.1 Cambios que NO Requieren Nueva Versión

Los siguientes cambios son **compatibles hacia atrás** y pueden implementarse en la versión actual:

- ✅ Agregar nuevos endpoints
- ✅ Agregar nuevos campos opcionales a las respuestas
- ✅ Agregar nuevos parámetros opcionales a las solicitudes
- ✅ Agregar nuevos códigos de estado HTTP
- ✅ Mejorar mensajes de error
- ✅ Optimizaciones de rendimiento
- ✅ Correcciones de bugs

### 3.2 Cambios que REQUIEREN Nueva Versión

Los siguientes cambios son **incompatibles hacia atrás** y requieren una nueva versión:

- ❌ Eliminar endpoints existentes
- ❌ Eliminar campos de las respuestas
- ❌ Cambiar el tipo de datos de campos existentes
- ❌ Hacer obligatorios parámetros que eran opcionales
- ❌ Cambiar la estructura de las respuestas
- ❌ Cambiar el comportamiento de endpoints existentes
- ❌ Cambiar códigos de estado HTTP para casos existentes

---

## 4. Ciclo de Vida de Versiones

### 4.1 Estados de Versión

Cada versión de la API puede estar en uno de los siguientes estados:

| Estado | Descripción | Soporte |
|--------|-------------|---------|
| **Actual** | Versión más reciente y recomendada | Soporte completo, nuevas características |
| **Soportada** | Versión anterior aún en uso | Soporte completo, solo correcciones críticas |
| **Deprecada** | Versión marcada para retiro | Soporte limitado, solo correcciones de seguridad |
| **Retirada** | Versión ya no disponible | Sin soporte |

### 4.2 Cronograma de Deprecación

Cuando se lance una nueva versión:

1. **Día 0**: Nueva versión (v2) lanzada como "Actual"
2. **Día 0**: Versión anterior (v1) pasa a "Soportada"
3. **Mes 6**: Versión anterior (v1) pasa a "Deprecada"
4. **Mes 12**: Versión anterior (v1) es "Retirada"

**Nota**: Los plazos pueden ajustarse según las necesidades del negocio y los acuerdos con clientes.

---

## 5. Avisos de Deprecación

### 5.1 Comunicación

Cuando una versión sea marcada como deprecada:

1. **Documentación**: Actualizar la documentación OpenAPI con avisos de deprecación
2. **Headers HTTP**: Incluir header `Deprecation: true` en las respuestas
3. **Header Sunset**: Incluir header `Sunset: <fecha>` indicando la fecha de retiro
4. **Notificación**: Enviar comunicación a todos los consumidores conocidos de la API
5. **Changelog**: Documentar en el changelog del proyecto

### 5.2 Ejemplo de Headers de Deprecación

```http
HTTP/1.1 200 OK
Content-Type: application/json
Deprecation: true
Sunset: Sat, 31 Dec 2026 23:59:59 GMT
Link: </api/v2/biosuperController/getListaResumenPesca>; rel="successor-version"
Warning: 299 - "Esta versión de la API será retirada el 31 de diciembre de 2026. Por favor, migre a /api/v2/"
```

### 5.3 Documentación en OpenAPI

```yaml
paths:
  /api/v1/biosuperController/getListaResumenPesca:
    post:
      deprecated: true
      description: |
        ⚠️ **DEPRECADO**: Este endpoint será retirado el 31 de diciembre de 2026.
        Por favor, use la versión 2: `/api/v2/biosuperController/getListaResumenPesca`
```

---

## 6. Guía de Migración entre Versiones

### 6.1 Proceso de Migración

Cuando se lance una nueva versión, se proporcionará una guía de migración que incluya:

1. **Resumen de cambios**: Lista de cambios incompatibles hacia atrás
2. **Mapeo de endpoints**: Tabla de correspondencia entre versiones
3. **Cambios en DTOs**: Diferencias en estructuras de solicitud/respuesta
4. **Ejemplos de código**: Ejemplos antes/después de la migración
5. **Herramientas de migración**: Scripts o herramientas para facilitar la transición
6. **Cronograma**: Fechas importantes (lanzamiento, deprecación, retiro)

### 6.2 Ejemplo de Guía de Migración (v1 → v2)

**Nota**: Este es un ejemplo hipotético para futuras versiones.

#### Cambios en Endpoints

| v1 | v2 | Cambio |
|----|----|----|
| `POST /api/v1/biosuperController/getListaResumenPesca` | `GET /api/v2/pesca/resumen` | Cambio de método y estructura RESTful |
| `POST /api/v1/token/{user}/{password}/{timeExpiredDay}` | `POST /api/v2/auth/login` | Credenciales en body, no en URL |

#### Cambios en DTOs

**v1 - ResumenPescaRequest**:
```json
{
  "empCodigo": "001",
  "prdFechaDesde": "2024-01-01",
  "prdFechaHasta": "2024-12-31"
}
```

**v2 - ResumenPescaRequest**:
```json
{
  "empresaId": "001",
  "fechaInicio": "2024-01-01T00:00:00Z",
  "fechaFin": "2024-12-31T23:59:59Z",
  "formato": "ISO8601"
}
```

---

## 7. Implementación Técnica

### 7.1 Configuración de Controladores

Los controladores utilizan la anotación `@RequestMapping` para definir el prefijo de versión:

```java
@RestController
@RequestMapping("/api/v1/biosuperController/")
public class MSBioSuperController {
    // Endpoints...
}
```

### 7.2 Configuración de OpenAPI

La configuración de OpenAPI incluye las URLs base con el prefijo de versión:

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI msBioSuperOpenAPI() {
        return new OpenAPI()
            .servers(List.of(
                new Server()
                    .url(devUrl + "/api/v1")
                    .description("Servidor de Desarrollo - v1")
            ));
    }
}
```

### 7.3 Soporte Multi-Versión

Para soportar múltiples versiones simultáneamente:

1. **Controladores separados**: Crear controladores distintos para cada versión
2. **Paquetes por versión**: Organizar código en paquetes `v1`, `v2`, etc.
3. **Configuración por versión**: Configuraciones OpenAPI separadas para cada versión
4. **Pruebas por versión**: Suite de pruebas independiente para cada versión

**Estructura de paquetes**:
```
com.acosux.MSBiosuper.infrastructure.rest
├── v1
│   ├── MSBioSuperController.java
│   └── SecurityController.java
└── v2
    ├── PescaController.java
    ├── SiembraController.java
    └── AuthController.java
```

---

## 8. Monitoreo y Métricas

### 8.1 Métricas por Versión

Se deben recopilar las siguientes métricas para cada versión:

- **Uso por versión**: Número de solicitudes por versión
- **Clientes por versión**: Número de clientes únicos usando cada versión
- **Tasa de adopción**: Velocidad de migración a nuevas versiones
- **Errores por versión**: Tasa de errores en cada versión
- **Latencia por versión**: Tiempo de respuesta promedio por versión

### 8.2 Alertas

Configurar alertas para:

- ⚠️ Uso de versiones deprecadas después de la fecha de retiro
- ⚠️ Aumento inusual de errores en una versión específica
- ⚠️ Clientes que no han migrado cerca de la fecha de retiro

---

## 9. Documentación y Recursos

### 9.1 Documentación de API

- **Swagger UI**: `https://api.msbiosuper.acosux.com/swagger-ui.html`
- **OpenAPI JSON**: `https://api.msbiosuper.acosux.com/v3/api-docs`
- **OpenAPI YAML**: `https://api.msbiosuper.acosux.com/v3/api-docs.yaml`

### 9.2 Recursos Adicionales

- **Changelog**: Registro de cambios entre versiones
- **Guías de migración**: Documentación específica para cada transición de versión
- **Ejemplos de código**: Repositorio con ejemplos de uso de cada versión
- **Soporte**: Canal de comunicación para consultas sobre versionado

---

## 10. Preguntas Frecuentes (FAQ)

### ¿Por qué versionado en URL y no en headers?

El versionado en URL es más explícito, fácil de probar y compatible con herramientas de documentación como Swagger. También facilita el cacheo y es más intuitivo para los desarrolladores.

### ¿Cuánto tiempo se soportará una versión antigua?

Por defecto, una versión será soportada durante 12 meses después del lanzamiento de su sucesora. Este período puede extenderse según acuerdos con clientes.

### ¿Qué pasa si necesito usar una versión retirada?

Las versiones retiradas no estarán disponibles. Se recomienda planificar la migración con anticipación durante el período de deprecación.

### ¿Puedo usar diferentes versiones para diferentes endpoints?

Sí, técnicamente es posible, pero no se recomienda. Es mejor migrar toda la integración a la nueva versión para mantener consistencia.

### ¿Cómo sé qué versión estoy usando?

La versión está explícita en la URL del endpoint. También puedes verificar la documentación OpenAPI para ver las versiones disponibles.

---

## 11. Historial de Versiones

| Versión | Fecha de Lanzamiento | Estado | Fecha de Retiro | Notas |
|---------|---------------------|--------|-----------------|-------|
| v1 | 2026-04-21 | Actual | N/A | Versión inicial con migración a Java 21 |

---

## 12. Contacto y Soporte

Para consultas sobre versionado de API:

- **Email**: desarrollo@acosux.com
- **Equipo**: Equipo de Desarrollo ACOSUX
- **Documentación**: https://www.acosux.com/msbiosuper/api-docs

---

**Última actualización**: 2026-04-21  
**Versión del documento**: 1.0  
**Autor**: Equipo de Desarrollo ACOSUX
