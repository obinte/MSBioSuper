# Configuración de OpenAPI 3.0 - MSBioSuper

## Resumen

Este documento describe la configuración de OpenAPI 3.0 implementada en MSBioSuper utilizando SpringDoc OpenAPI.

## Componentes Implementados

### 1. Clase de Configuración: `OpenApiConfig.java`

**Ubicación**: `src/main/java/com/acosux/MSBiosuper/config/OpenApiConfig.java`

**Características**:
- Configuración completa de la especificación OpenAPI 3.0
- Información de la API (título, descripción, versión, contacto, licencia)
- Esquema de seguridad JWT Bearer Token
- Configuración de múltiples servidores (desarrollo, staging, producción)
- Aplicación global del esquema de seguridad a todos los endpoints

### 2. Propiedades de Configuración

#### application.properties (Base)
```properties
# Versión de la aplicación
app.version=1.0.0

# URLs de servidores por entorno
app.api.dev-url=${API_DEV_URL:http://localhost:8080}
app.api.staging-url=${API_STAGING_URL:https://staging.msbiosuper.acosux.com}
app.api.prod-url=${API_PROD_URL:https://api.msbiosuper.acosux.com}

# Configuración de SpringDoc
springdoc.api-docs.enabled=true
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha
springdoc.swagger-ui.filter=true
springdoc.swagger-ui.display-request-duration=true
```

#### application-dev.properties (Desarrollo)
```properties
# Swagger UI completamente habilitado en desarrollo
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.display-request-duration=true
springdoc.swagger-ui.operations-sorter=alpha
springdoc.swagger-ui.tags-sorter=alpha

# URL del servidor de desarrollo
app.api.dev-url=http://localhost:8080
```

#### application-prod.properties (Producción)
```properties
# Swagger UI deshabilitado en producción por seguridad
springdoc.swagger-ui.enabled=false

# OpenAPI docs disponible para herramientas internas
springdoc.api-docs.enabled=true
springdoc.api-docs.path=/v3/api-docs

# URL del servidor de producción
app.api.prod-url=${API_PROD_URL:https://api.msbiosuper.acosux.com}
```

## Endpoints de Documentación

### Desarrollo y Staging

1. **Swagger UI (Interfaz Interactiva)**
   - URL: `http://localhost:8080/swagger-ui.html`
   - Descripción: Interfaz web interactiva para explorar y probar la API
   - Características:
     - Visualización de todos los endpoints
     - Prueba de endpoints directamente desde el navegador
     - Autenticación JWT integrada
     - Ejemplos de request/response

2. **OpenAPI JSON**
   - URL: `http://localhost:8080/v3/api-docs`
   - Descripción: Especificación OpenAPI 3.0 en formato JSON
   - Uso: Importar en herramientas como Postman, Insomnia, etc.

3. **OpenAPI YAML**
   - URL: `http://localhost:8080/v3/api-docs.yaml`
   - Descripción: Especificación OpenAPI 3.0 en formato YAML
   - Uso: Generación de código cliente, documentación

### Producción

1. **OpenAPI JSON** (Solo para herramientas internas)
   - URL: `https://api.msbiosuper.acosux.com/v3/api-docs`
   - Nota: Swagger UI está deshabilitado por seguridad

## Esquema de Seguridad JWT

### Configuración

```java
new SecurityScheme()
    .name("bearerAuth")
    .type(SecurityScheme.Type.HTTP)
    .scheme("bearer")
    .bearerFormat("JWT")
    .description("Token JWT para autenticación...")
```

### Uso en Swagger UI

1. Obtener un token JWT:
   - Realizar petición POST a `/login` con credenciales válidas
   - Copiar el token de la respuesta

2. Autenticarse en Swagger UI:
   - Hacer clic en el botón "Authorize" (candado verde)
   - Ingresar el token en el formato: `Bearer {token}`
   - Hacer clic en "Authorize"

3. Probar endpoints:
   - Todos los endpoints ahora incluirán automáticamente el header `Authorization`

## Información de la API

### Metadatos

- **Título**: MSBioSuper API
- **Versión**: 1.0.0 (configurable mediante `app.version`)
- **Descripción**: API REST para el sistema de gestión MSBioSuper
- **Contacto**: 
  - Nombre: Equipo de Desarrollo ACOSUX
  - Email: desarrollo@acosux.com
  - URL: https://www.acosux.com
- **Licencia**: Propietario

### Servidores Configurados

1. **Desarrollo**
   - URL: `http://localhost:8080`
   - Descripción: Servidor de Desarrollo

2. **Staging**
   - URL: `https://staging.msbiosuper.acosux.com`
   - Descripción: Servidor de Staging

3. **Producción**
   - URL: `https://api.msbiosuper.acosux.com`
   - Descripción: Servidor de Producción

## Funcionalidades de la API Documentadas

La documentación OpenAPI incluye información sobre:

1. **Gestión de Pesca y Siembra**
   - Resúmenes de pesca
   - Resúmenes de siembra

2. **Consumos y Costos**
   - Consumos diarios
   - Costos por piscina

3. **Liquidaciones**
   - Listado de liquidaciones
   - Detalle de liquidaciones

4. **Productos**
   - Productos generales
   - Gramajes

5. **Compras y Ventas**
   - Detalle de compras
   - Listado de ventas

6. **Cuentas**
   - Cuentas por cobrar
   - Cuentas por pagar

7. **Autenticación**
   - Login con JWT

## Variables de Entorno

Para personalizar las URLs de los servidores, configurar las siguientes variables de entorno:

```bash
# URL del servidor de desarrollo (opcional)
export API_DEV_URL=http://localhost:8080

# URL del servidor de staging (opcional)
export API_STAGING_URL=https://staging.msbiosuper.acosux.com

# URL del servidor de producción (requerido en producción)
export API_PROD_URL=https://api.msbiosuper.acosux.com
```

## Verificación de la Configuración

### 1. Iniciar la Aplicación

```bash
# Con perfil de desarrollo
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# O con JAR compilado
java -jar target/MSBioSuper-1.0.jar --spring.profiles.active=dev
```

### 2. Verificar Swagger UI

Abrir en el navegador: `http://localhost:8080/swagger-ui.html`

**Verificaciones**:
- ✅ La página de Swagger UI se carga correctamente
- ✅ Se muestra el título "MSBioSuper API"
- ✅ Se muestra la versión "1.0.0"
- ✅ Se muestran los 3 servidores configurados
- ✅ Se muestra el botón "Authorize" para JWT
- ✅ Se listan todos los endpoints de la API

### 3. Verificar OpenAPI JSON

Abrir en el navegador: `http://localhost:8080/v3/api-docs`

**Verificaciones**:
- ✅ Se devuelve un JSON válido
- ✅ Contiene la sección `info` con título, descripción, versión
- ✅ Contiene la sección `servers` con los 3 servidores
- ✅ Contiene la sección `components.securitySchemes` con bearerAuth
- ✅ Contiene la sección `paths` con todos los endpoints
- ✅ Todos los endpoints tienen `security: [bearerAuth]`

### 4. Probar Autenticación JWT

1. En Swagger UI, expandir el endpoint `/login`
2. Hacer clic en "Try it out"
3. Ingresar credenciales válidas
4. Ejecutar la petición
5. Copiar el token de la respuesta
6. Hacer clic en "Authorize"
7. Ingresar: `Bearer {token}`
8. Probar cualquier otro endpoint

**Verificaciones**:
- ✅ El login devuelve un token JWT válido
- ✅ El botón "Authorize" funciona correctamente
- ✅ Los endpoints protegidos funcionan con el token
- ✅ Los endpoints protegidos fallan sin el token (401 Unauthorized)

## Integración con Herramientas Externas

### Postman

1. Importar la especificación OpenAPI:
   - En Postman, ir a "Import"
   - Seleccionar "Link"
   - Ingresar: `http://localhost:8080/v3/api-docs`
   - Hacer clic en "Continue" e "Import"

2. Configurar autenticación:
   - Seleccionar la colección importada
   - Ir a "Authorization"
   - Tipo: "Bearer Token"
   - Token: Pegar el token JWT obtenido del login

### Insomnia

1. Importar la especificación OpenAPI:
   - En Insomnia, ir a "Application" > "Preferences" > "Data"
   - Hacer clic en "Import Data" > "From URL"
   - Ingresar: `http://localhost:8080/v3/api-docs`

2. Configurar autenticación:
   - Seleccionar cualquier request
   - Ir a "Auth" tab
   - Tipo: "Bearer Token"
   - Token: Pegar el token JWT

### Generación de Código Cliente

Usando OpenAPI Generator:

```bash
# Instalar OpenAPI Generator
npm install -g @openapitools/openapi-generator-cli

# Generar cliente Java
openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g java \
  -o ./generated-client-java

# Generar cliente TypeScript/Angular
openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g typescript-angular \
  -o ./generated-client-angular

# Generar cliente Python
openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g python \
  -o ./generated-client-python
```

## Próximos Pasos (Tarea 23.4)

La siguiente tarea será agregar anotaciones OpenAPI a los controladores para mejorar la documentación:

- `@Tag`: Agrupar endpoints por funcionalidad
- `@Operation`: Documentar cada endpoint
- `@ApiResponse`: Documentar respuestas posibles
- `@Parameter`: Documentar parámetros de entrada
- `@Schema`: Documentar estructura de DTOs

## Beneficios de la Configuración Actual

1. **Documentación Automática**: La especificación OpenAPI se genera automáticamente
2. **Interfaz Interactiva**: Swagger UI permite probar la API sin herramientas adicionales
3. **Seguridad Integrada**: JWT Bearer Token configurado globalmente
4. **Multi-Entorno**: Soporte para desarrollo, staging y producción
5. **Estándar de Industria**: OpenAPI 3.0 es el estándar para documentación de APIs REST
6. **Generación de Clientes**: Permite generar código cliente en múltiples lenguajes
7. **Integración con Herramientas**: Compatible con Postman, Insomnia, etc.

## Seguridad en Producción

**IMPORTANTE**: En producción, Swagger UI está deshabilitado por razones de seguridad:

- ✅ Swagger UI deshabilitado (`springdoc.swagger-ui.enabled=false`)
- ✅ OpenAPI docs disponible solo para herramientas internas
- ✅ Considerar autenticación adicional para `/v3/api-docs` si es necesario
- ✅ Usar API Gateway para control de acceso adicional

## Referencias

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI Specification 3.0](https://swagger.io/specification/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [OpenAPI Generator](https://openapi-generator.tech/)

---

**Fecha de Creación**: 2026-04-23  
**Autor**: Josias Rubén Aldaz Arcalles  
**Estado**: Completado
