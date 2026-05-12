# Plan de Implementación: Migración MSBioSuper a Java 21

## Resumen General

Este plan de implementación divide la migración completa de MSBioSuper de Java 8 a Java 21 en tareas manejables y secuenciales. La migración sigue un enfoque por fases para minimizar riesgos y asegurar la validación continua de la funcionalidad.

**Alcance de la Migración**:
- Actualización Java 8 → Java 21 LTS
- Actualización Spring Boot 2.5.2 → 3.3.x
- Actualización Hibernate 4.x → 6.5.x
- Refactorización arquitectónica: Monolítico → Hexagonal
- Modernización de seguridad: JWT, Spring Security 6
- Infraestructura: WAR → JAR, contenedorización Docker

**Estrategia de Implementación**:
- Migración incremental con validación en cada paso
- Mantener compatibilidad hacia atrás durante la transición
- Enfoque dirigido por pruebas con cobertura completa
- Tareas de verificación para validación y retroalimentación del usuario

---

## Tareas

### Fase 1: Configuración Base (Java 21 y Spring Boot 3)

- [x] 1. Configurar entorno de desarrollo Java 21
  - Actualizar configuración Maven a Java 21 (`<java.version>21</java.version>`)
  - Actualizar `pom.xml` para usar Spring Boot 3.3.x parent
  - Agregar Maven Wrapper para reproducibilidad del build
  - Configurar plugin del compilador Maven para Java 21
  - _Requisitos: TR-1, TR-2, TR-23_

- [ ] 2. Migrar imports javax.* a jakarta.*
  - [x] 2.1 Reemplazar todos los imports `javax.servlet.*` con `jakarta.servlet.*`
    - Actualizar clases de filtros (CorsFilter, VerifyTokenFilter)
    - Actualizar clases de controladores
    - _Requisitos: TR-2_
  
  - [x] 2.2 Reemplazar todos los imports `javax.persistence.*` con `jakarta.persistence.*`
    - Actualizar clases de entidades en el paquete util/entity
    - Actualizar interfaces e implementaciones DAO
    - _Requisitos: TR-2, TR-3_
  
  - [x] 2.3 Reemplazar todos los imports `javax.validation.*` con `jakarta.validation.*`
    - Actualizar clases DTO con anotaciones de validación
    - _Requisitos: TR-2, TR-12_

- [ ] 3. Actualizar dependencias principales en pom.xml
  - [x] 3.1 Actualizar Spring Boot a 3.3.x
    - Eliminar parent Spring Boot 2.5.2
    - Agregar parent Spring Boot 3.3.x
    - Actualizar spring-boot-starter-web
    - Actualizar spring-boot-starter-test
    - _Requisitos: TR-2_
  
  - [x] 3.2 Actualizar driver PostgreSQL a 42.7.x
    - Reemplazar postgresql 42.2.18 con 42.7.x
    - _Requisitos: TR-5_
  
  - [x] 3.3 Eliminar dependencias obsoletas
    - Eliminar javaee-web-api 7.0
    - Eliminar spring-orm 4.2.3 (incluido en Spring Boot 3)
    - Eliminar commons-dbcp2 (se usará HikariCP)
    - _Requisitos: TR-2, TR-3_

- [ ] 4. Configurar Hibernate 6.5.x y JPA
  - [x] 4.1 Agregar dependencias de Hibernate 6.5.x
    - Agregar hibernate-core 6.5.x
    - Agregar HikariCP para pool de conexiones
    - Eliminar hibernate-entitymanager (fusionado en hibernate-core)
    - _Requisitos: TR-3_
  
  - [x] 4.2 Reemplazar SessionFactory con configuración EntityManager
    - Crear nueva clase JpaConfig
    - Configurar LocalContainerEntityManagerFactoryBean
    - Configurar JpaTransactionManager
    - Eliminar PersistenceConfig.java (configuración antigua de Hibernate 4)
    - _Requisitos: TR-3_
  
  - [x] 4.3 Configurar pool de conexiones HikariCP
    - Establecer tamaño máximo del pool (20 conexiones)
    - Establecer timeout de conexión (30 segundos)
    - Establecer timeout de inactividad (10 minutos)
    - Habilitar detección de fugas de conexión
    - _Requisitos: TR-3, TR-5_

- [x] 5. Actualizar application.properties para Spring Boot 3
  - Migrar configuración de base de datos a propiedades spring.datasource.*
  - Agregar propiedades JPA/Hibernate (spring.jpa.*)
  - Configurar propiedades de HikariCP
  - Agregar configuración de logging
  - _Requisitos: TR-2, TR-3, TR-5_

- [x] 6. Cambiar empaquetado de WAR a JAR
  - Actualizar pom.xml packaging a `<packaging>jar</packaging>`
  - Eliminar ServletInitializer.java
  - Eliminar scope provided de spring-boot-starter-tomcat
  - Eliminar directorio webapp (WEB-INF, META-INF, index.jsp)
  - _Requisitos: TR-24_

- [x] 7. Punto de verificación - Verificar que la aplicación inicia con Spring Boot 3
  - Ejecutar aplicación y verificar que inicia sin errores
  - Verificar que Tomcat embebido inicia exitosamente
  - Verificar que la conexión a base de datos se establece
  - Asegurar que todas las pruebas pasan, preguntar al usuario si surgen dudas.

---

### Fase 2: Migración Principal (Capa de Acceso a Datos)

- [x] 8. Crear estructura de paquetes de arquitectura hexagonal
  - Crear paquete `com.acosux.msbiosuper.domain`
  - Crear paquete `com.acosux.msbiosuper.application`
  - Crear paquete `com.acosux.msbiosuper.infrastructure`
  - Crear paquete `com.acosux.msbiosuper.shared`
  - _Requisitos: TR-6_

- [ ] 9. Migrar entidades de dominio a JPA 3.1
  - [x] 9.1 Actualizar anotaciones de entidades a jakarta.persistence
    - Actualizar todas las clases de entidades en el paquete util/entity
    - Reemplazar anotaciones @Entity, @Table, @Column
    - Actualizar anotaciones @Id, @GeneratedValue
    - _Requisitos: TR-3, TR-6_
  
  - [x] 9.2 Mover entidades a la capa de dominio
    - Mover clases de entidades de util/entity a domain/model
    - Asegurar que las entidades no tengan dependencias de frameworks (excepto anotaciones JPA)
    - _Requisitos: TR-6_

- [ ] 10. Crear interfaces de repositorio (puertos) en la capa de dominio
  - [x] 10.1 Crear interfaces base de repositorio
    - Crear interfaz ResumenPescaRepository
    - Crear interfaz ResumenSiembraRepository
    - Crear interfaz ConsumosDiariosRepository
    - Crear interfaz LiquidacionRepository
    - Crear interfaz ProductoRepository
    - Crear interfaz CuentasPorCobrarRepository
    - Crear interfaz CuentasPorPagarRepository
    - _Requisitos: TR-4, TR-6_
  
  - [x] 10.2 Definir firmas de métodos de repositorio
    - Definir métodos de consulta siguiendo convenciones de nomenclatura de Spring Data JPA
    - Agregar métodos de consulta personalizados con anotaciones @Query
    - Definir métodos de paginación y ordenamiento
    - _Requisitos: TR-4_

- [x] 11. Implementar repositorios Spring Data JPA (adaptadores)
  - [x] 11.1 Crear implementaciones de repositorio JPA en la capa de infraestructura
    - Implementar ResumenPescaJpaRepository extendiendo JpaRepository
    - Implementar ResumenSiembraJpaRepository extendiendo JpaRepository
    - Implementar ConsumosDiariosJpaRepository extendiendo JpaRepository
    - Implementar LiquidacionJpaRepository extendiendo JpaRepository
    - Implementar ProductoJpaRepository extendiendo JpaRepository
    - Implementar CuentasPorCobrarJpaRepository extendiendo JpaRepository
    - Implementar CuentasPorPagarJpaRepository extendiendo JpaRepository
    - _Requisitos: TR-4, TR-6_
  
  - [x] 11.2 Migrar consultas SQL de implementaciones DAO
    - Convertir consultas SQL nativas a JPQL o Criteria API
    - Agregar anotaciones @Query para consultas personalizadas
    - Implementar consultas parametrizadas para prevenir inyección SQL
    - _Requisitos: TR-4, NFR-10_
  
  - [x] 11.3 Escribir pruebas unitarias para la capa de repositorio
    - Probar métodos de consulta de repositorio
    - Probar paginación y ordenamiento
    - Probar consultas personalizadas
    - _Requisitos: TR-19_

- [x] 12. Eliminar capa DAO heredada
  - [x] 12.1 Eliminar interfaces e implementaciones DAO antiguas
    - Eliminar MSBioSuperDao y MSBioSuperDaoImpl
    - Eliminar GenericDao y GenericDaoImpl
    - Eliminar GenericSQLDao y GenericSQLDaoImpl
    - _Requisitos: TR-4, TR-6_
  
  - [x] 12.2 Actualizar capa de servicio para usar nuevos repositorios
    - Reemplazar inyecciones DAO con inyecciones de repositorio
    - Actualizar llamadas a métodos para usar métodos de repositorio
    - _Requisitos: TR-4, TR-6_

- [x] 13. Punto de verificación - Verificar que la capa de acceso a datos funciona
  - Ejecutar aplicación y probar consultas a base de datos
  - Verificar que todos los métodos de repositorio retornan datos correctos
  - Verificar que las transacciones funcionan correctamente
  - Asegurar que todas las pruebas pasan, preguntar al usuario si surgen dudas.

---

### Fase 3: Refactorización Arquitectónica (Arquitectura Hexagonal)

- [x] 14. Crear DTOs y mapeadores en la capa de aplicación
  - [x] 14.1 Crear DTOs de solicitud usando Records de Java 21
    - Crear record ResumenPescaRequest
    - Crear record ResumenSiembraRequest
    - Crear record ConsumosDiariosRequest
    - Crear record LiquidacionRequest
    - Crear record ProductoRequest
    - Crear record CuentasPorCobrarRequest
    - Crear record CuentasPorPagarRequest
    - _Requisitos: TR-1, TR-6_
  
  - [x] 14.2 Crear DTOs de respuesta usando Records de Java 21
    - Crear record ResumenPescaResponse
    - Crear record ResumenSiembraResponse
    - Crear record ConsumosDiariosResponse
    - Crear record LiquidacionResponse
    - Crear record ProductoResponse
    - Crear record CuentasPorCobrarResponse
    - Crear record CuentasPorPagarResponse
    - _Requisitos: TR-1, TR-6_
  
  - [x] 14.3 Crear clases mapeadoras para conversión DTO ↔ Entidad
    - Crear ResumenPescaMapper
    - Crear ResumenSiembraMapper
    - Crear ConsumosDiariosMapper
    - Crear LiquidacionMapper
    - Crear ProductoMapper
    - Crear CuentasPorCobrarMapper
    - Crear CuentasPorPagarMapper
    - Usar MapStruct o mapeo manual
    - _Requisitos: TR-6_
  
  - [ ] 14.4 Escribir pruebas unitarias para mapeadores
    - Probar mapeo de DTO a Entidad
    - Probar mapeo de Entidad a DTO
    - Probar manejo de valores nulos
    - _Requisitos: TR-19_

- [ ] 15. Crear casos de uso en la capa de aplicación
  - [x] 15.1 Crear clases de casos de uso para operaciones de negocio
    - Crear GetResumenPescaUseCase
    - Crear GetResumenSiembraUseCase
    - Crear GetConsumosDiariosUseCase
    - Crear GetLiquidacionDetalleUseCase
    - Crear ListarLiquidacionesUseCase
    - Crear ListarCostosPorPiscinaUseCase
    - Crear ObtenerGramajesUseCase
    - Crear GetComprasDetalleUseCase
    - Crear ListarVentasUseCase
    - Crear GetProductosGeneralUseCase
    - Crear GetCuentasPorCobrarUseCase
    - Crear GetCuentasPorPagarUseCase
    - _Requisitos: TR-6, FR-1_
  
  - [x] 15.2 Implementar lógica de casos de uso
    - Inyectar interfaces de repositorio (puertos)
    - Implementar orquestación de lógica de negocio
    - Manejar mapeo DTO ↔ Entidad
    - Agregar límites transaccionales con @Transactional
    - _Requisitos: TR-6, FR-2_
  
  - [ ]* 15.3 Escribir pruebas unitarias para casos de uso
    - Simular dependencias de repositorio (mock)
    - Probar lógica de negocio
    - Probar manejo de errores
    - Probar límites transaccionales
    - _Requisitos: TR-19_

- [ ] 16. Refactorizar capa de servicio
  - [x] 16.1 Mover lógica de negocio de servicios a casos de uso
    - Extraer lógica de negocio de MSBiosSuperServiceImpl
    - Extraer lógica de negocio de SecurityServiceImpl
    - Mover lógica a casos de uso apropiados
    - _Requisitos: TR-6, TR-7_
  
  - [x] 16.2 Crear servicios de dominio para reglas de negocio complejas
    - Crear servicios de dominio para operaciones multi-entidad
    - Asegurar que servicios de dominio no tengan dependencias de infraestructura
    - _Requisitos: TR-6, TR-7_
  
  - [x] 16.3 Eliminar implementaciones de servicio antiguas
    - Eliminar MSBiosSuperServiceImpl
    - Eliminar SecurityServiceImpl
    - Mantener interfaces de servicio si se necesitan para compatibilidad hacia atrás
    - _Requisitos: TR-6_

- [ ] 17. Refactorizar controladores para usar casos de uso 
  - [x] 17.1 Actualizar MSBioSuperController
    - Reemplazar inyecciones de servicio con inyecciones de casos de uso
    - Actualizar métodos de endpoints para usar DTOs de solicitud/respuesta
    - Agregar anotaciones de validación de entrada
    - _Requisitos: TR-6, TR-12, FR-1_
  
  - [x] 17.2 Actualizar SecurityController
    - Reemplazar inyecciones de servicio con inyecciones de casos de uso
    - Actualizar lógica de autenticación
    - _Requisitos: TR-6, FR-3_
  
  - [x] 17.3 Mover controladores a la capa de infraestructura
    - Mover controladores del paquete controller a infrastructure/rest
    - _Requisitos: TR-6_

- [x] 18. Punto de verificación - Verificar arquitectura hexagonal
  - Verificar que la capa de dominio no tiene dependencias de frameworks
  - Verificar que la capa de aplicación orquesta correctamente
  - Verificar que la capa de infraestructura implementa adaptadores
  - Probar que todos los endpoints siguen funcionando
  - Asegurar que todas las pruebas pasan, preguntar al usuario si surgen dudas.

---

### Fase 4: Seguridad y Documentación de API

- [ ] 19. Actualizar Spring Security a versión 6
  - [x] 19.1 Eliminar WebSecurityConfigurerAdapter obsoleto
    - Eliminar clase SecurityConfig que extiende WebSecurityConfigurerAdapter
    - _Requisitos: TR-9_
  
  - [x] 19.2 Crear configuración moderna de SecurityFilterChain
    - Crear nuevo SecurityConfig con @Bean SecurityFilterChain
    - Configurar seguridad HTTP usando DSL lambda
    - Configurar CORS usando bean CorsConfigurationSource
    - Configurar protección CSRF
    - Configurar gestión de sesiones
    - _Requisitos: TR-9, TR-13_
  
  - [x] 19.3 Actualizar filtros de seguridad para Spring Security 6
    - Actualizar VerifyTokenFilter para extender OncePerRequestFilter
    - Actualizar registro de filtros
    - _Requisitos: TR-9_

- [ ] 20. Implementar autenticación JWT moderna con JJWT 0.12.x
  - [x] 20.1 Actualizar dependencia JJWT a 0.12.x
    - Eliminar jjwt 0.7.0
    - Agregar jjwt-api 0.12.x
    - Agregar jjwt-impl 0.12.x
    - Agregar jjwt-jackson 0.12.x
    - _Requisitos: TR-10_
  
  - [x] 20.2 Refactorizar TokenUtil para API JJWT 0.12.x
    - Actualizar generación de tokens usando nueva API Jwts.builder()
    - Actualizar validación de tokens usando nueva API JwtParser
    - Implementar mecanismo de renovación de tokens
    - Usar SecretKey en lugar de String para firma
    - _Requisitos: TR-10, FR-3_
  
  - [x] 20.3 Externalizar clave secreta JWT
    - Mover secreto JWT a application.properties
    - Usar anotación @Value para inyectar secreto
    - Agregar soporte para variables de entorno
    - Documentar generación de clave secreta
    - _Requisitos: TR-10, TR-11_
  
  - [x] 20.4 Escribir pruebas unitarias para funcionalidad JWT
    - Probar generación de tokens
    - Probar validación de tokens
    - Probar expiración de tokens
    - Probar renovación de tokens
    - _Requisitos: TR-19, FR-3_

- [ ] 21. Implementar validación de entrada
  - [x] 21.1 Agregar dependencia Jakarta Validation
    - Agregar spring-boot-starter-validation
    - _Requisitos: TR-12_
  
  - [x] 21.2 Agregar anotaciones de validación a DTOs de solicitud
    - Agregar anotaciones @NotNull, @NotBlank, @Size
    - Agregar @Pattern para validación de formato
    - Agregar @Past, @Future para validación de fechas
    - Agregar validadores personalizados para reglas de negocio
    - _Requisitos: TR-12_
  
  - [x] 21.3 Crear manejador global de excepciones
    - Crear clase @RestControllerAdvice
    - Manejar MethodArgumentNotValidException
    - Manejar ConstraintViolationException
    - Manejar excepciones de negocio
    - Retornar respuestas de error estandarizadas
    - _Requisitos: TR-12, FR-5_
  
  - [ ]* 21.4 Escribir pruebas unitarias para validación
    - Probar anotaciones de validación
    - Probar validadores personalizados
    - Probar manejador de excepciones
    - _Requisitos: TR-19_

- [ ] 22. Externalizar configuración y secretos
  - [x] 22.1 Mover credenciales de base de datos a variables de entorno
    - Actualizar application.properties para usar sintaxis ${ENV_VAR}
    - Documentar variables de entorno requeridas
    - _Requisitos: TR-11_
  
  - [x] 22.2 Crear archivos de configuración específicos por entorno
    - Crear application-dev.properties
    - Crear application-test.properties
    - Crear application-prod.properties
    - _Requisitos: TR-11, TR-23_
  
  - [x] 22.3 Documentar gestión de configuración
    - Crear CONFIGURATION.md con todas las configuraciones
    - Documentar requisitos de variables de entorno
    - Documentar estrategia de gestión de secretos
    - _Requisitos: TR-11, NFR-7_

- [ ] 23. Migrar documentación de API de Swagger 2 a OpenAPI 3
  - [x] 23.1 Eliminar dependencias de Swagger 2
    - Eliminar springfox-swagger2
    - Eliminar springfox-swagger-uix
    - Eliminar SwaggerConfig.java
    - _Requisitos: TR-14_
  
  - [x] 23.2 Agregar dependencias SpringDoc OpenAPI
    - Agregar springdoc-openapi-starter-webmvc-ui 2.6.x
    - _Requisitos: TR-14_
  
  - [x] 23.3 Configurar documentación OpenAPI
    - Crear clase OpenApiConfig
    - Configurar información de API (título, descripción, versión)
    - Configurar esquemas de seguridad (JWT Bearer)
    - Configurar servidores (dev, staging, prod)
    - _Requisitos: TR-14_
  
  - [x] 23.4 Agregar anotaciones OpenAPI a controladores
    - Agregar @Tag para agrupación de controladores
    - Agregar @Operation para documentación de endpoints
    - Agregar @ApiResponse para documentación de respuestas
    - Agregar @Parameter para documentación de parámetros
    - Agregar @Schema para documentación de DTOs
    - _Requisitos: TR-14_
  
  - [x] 23.5 Implementar versionado de API
    - Agregar prefijo /api/v1 a todos los endpoints
    - Actualizar @RequestMapping de controladores
    - Documentar estrategia de versionado
    - _Requisitos: TR-15_

- [x] 24. Punto de verificación - Verificar seguridad y documentación
  - Probar flujo de autenticación JWT
  - Probar validación de entrada
  - Verificar especificación OpenAPI en /v3/api-docs
  - Verificar Swagger UI en /swagger-ui.html
  - Probar todos los endpoints con autenticación
  - Asegurar que todas las pruebas pasan, preguntar al usuario si surgen dudas.

---

### Fase 5: Observabilidad y Pruebas

- [ ] 25. Configurar Spring Boot Actuator
  - [x] 25.1 Agregar dependencia Actuator
    - Agregar spring-boot-starter-actuator
    - _Requisitos: TR-16_
  
  - [x] 25.2 Configurar verificaciones de salud
    - Habilitar endpoint /actuator/health
    - Configurar indicador de salud de base de datos
    - Configurar indicador de salud de espacio en disco
    - Crear indicadores de salud personalizados para lógica de negocio
    - _Requisitos: TR-16_
  
  - [x] 25.3 Configurar sondas de liveness y readiness
    - Configurar /actuator/health/liveness
    - Configurar /actuator/health/readiness
    - _Requisitos: TR-16_

- [ ] 26. Configurar métricas y monitoreo
  - [x] 26.1 Agregar dependencias Micrometer
    - Agregar micrometer-registry-prometheus
    - _Requisitos: TR-17_
  
  - [x] 26.2 Configurar endpoint de métricas Prometheus
    - Habilitar /actuator/prometheus
    - Configurar métricas JVM
    - Configurar métricas de solicitudes HTTP
    - Configurar métricas de base de datos
    - _Requisitos: TR-17_
  
  - [x] 26.3 Agregar métricas de negocio personalizadas
    - Crear métricas personalizadas para latencia de endpoints API
    - Crear métricas personalizadas para operaciones de negocio
    - _Requisitos: TR-17_

- [ ] 27. Implementar logging estructurado
  - [x] 27.1 Configurar Logback con codificador JSON
    - Agregar dependencia logstash-logback-encoder
    - Crear configuración logback-spring.xml
    - Configurar formato JSON para logs
    - _Requisitos: TR-18_
  
  - [x] 27.2 Agregar ID de correlación para rastreo de solicitudes
    - Crear filtro de ID de correlación
    - Agregar ID de correlación a MDC
    - Incluir ID de correlación en todos los logs
    - _Requisitos: TR-18_
  
  - [x] 27.3 Configurar niveles de log por paquete
    - Establecer niveles de log apropiados para paquetes de aplicación
    - Establecer niveles de log para framework Spring
    - Establecer niveles de log para Hibernate
    - _Requisitos: TR-18_

- [ ] 28. Escribir pruebas unitarias completas
  - [x] 28.1 Configurar JUnit 5 y Mockito
    - Verificar que JUnit 5 está configurado (incluido en Spring Boot 3)
    - Verificar que Mockito está configurado
    - _Requisitos: TR-19_
  
  - [x] 28.2 Escribir pruebas unitarias para capa de dominio
    - Probar entidades de dominio
    - Probar servicios de dominio
    - Probar lógica de negocio
    - _Requisitos: TR-19_
    
  - [x] 28.3 Escribir pruebas unitarias para capa de aplicación
    - Probar casos de uso con repositorios simulados
    - Probar mapeadores
    - Probar DTOs
    - _Requisitos: TR-19_
  
  - [x] 28.4 Escribir pruebas unitarias para capa de infraestructura
    - Probar controladores con casos de uso simulados
    - Probar manejadores de excepciones
    - Probar filtros de seguridad
    - _Requisitos: TR-19_
  
  - [x] 28.5 Verificar cobertura de código
    - Ejecutar análisis de cobertura de código
    - Asegurar cobertura mínima del 80%
    - _Requisitos: TR-19_

- [ ] 29. Escribir pruebas de integración
  - [x] 29.1 Configurar Testcontainers para PostgreSQL
    - Agregar dependencia testcontainers
    - Agregar módulo testcontainers de postgresql
    - Crear clase base de prueba de integración
    - _Requisitos: TR-20_
  
  - [x] 29.2 Escribir pruebas de integración de repositorio
    - Probar consultas de repositorio contra base de datos real
    - Probar paginación y ordenamiento
    - Probar consultas personalizadas
    - Probar rollback de transacciones
    - _Requisitos: TR-20_
  
  - [x] 29.3 Escribir pruebas de integración de API
    - Probar todos los endpoints con RestAssured o MockMvc
    - Probar flujo de autenticación
    - Probar validación de entrada
    - Probar escenarios de error
    - _Requisitos: TR-20, TR-21_

- [ ] 30. Escribir pruebas de rendimiento
  - [x] 30.1 Configurar JMeter o Gatling
    - Instalar herramienta de pruebas de rendimiento
    - Crear escenarios de prueba
    - _Requisitos: TR-22_
  
  - [x] 30.2 Crear benchmarks de rendimiento base
    - Medir tiempos de respuesta de API
    - Medir throughput
    - Medir rendimiento de consultas de base de datos
    - _Requisitos: TR-22, NFR-1, NFR-2_
  
  - [x] 30.3 Ejecutar pruebas de carga y estrés
    - Probar con 100 usuarios concurrentes
    - Probar con 1000 solicitudes/segundo
    - Identificar cuellos de botella de rendimiento
    - _Requisitos: TR-22, NFR-2_

- [x] 31. Punto de verificación - Verificar observabilidad y pruebas
  - Verificar que las verificaciones de salud funcionan
  - Verificar que las métricas están expuestas
  - Verificar que los logs están estructurados
  - Verificar que todas las pruebas pasan
  - Verificar que la cobertura de código cumple el objetivo
  - Asegurar que todas las pruebas pasan, preguntar al usuario si surgen dudas.

---

### Fase 6: Contenedorización y Despliegue

- [ ] 32. Crear Dockerfile para contenedorización
  - [x] 32.1 Crear Dockerfile multi-etapa
    - Etapa 1: Build con Maven
    - Etapa 2: Runtime con OpenJDK 21
    - Usar imagen base oficial eclipse-temurin:21-jre-alpine
    - _Requisitos: TR-25_
  
  - [x] 32.2 Optimizar imagen Docker
    - Usar usuario no-root para seguridad
    - Minimizar capas de imagen
    - Optimizar caché de capas
    - Objetivo de tamaño de imagen < 200MB
    - _Requisitos: TR-25_
  
  - [x] 32.3 Agregar health check al Dockerfile
    - Configurar instrucción HEALTHCHECK
    - Usar endpoint /actuator/health/liveness
    - _Requisitos: TR-25_

- [ ] 33. Crear Docker Compose para desarrollo local
  - [x] 33.1 Crear docker-compose.yml
    - Definir servicio msbiosuper
    - Definir servicio postgres
    - Configurar networking
    - Configurar volúmenes para persistencia de datos
    - _Requisitos: TR-25_
   
  - [x] 33.2 Crear docker-compose.override.yml para desarrollo
    - Configurar hot reload
    - Configurar puerto de debug
    - Configurar variables de entorno de desarrollo
    - _Requisitos: TR-25_

- [x] 34. Configurar Maven para builds Docker
  - [x] 34.1 Agregar plugin Jib Maven (alternativa opcional a Dockerfile)
    - Configurar jib-maven-plugin
    - Configurar imagen base
    - Configurar registro destino
    - _Requisitos: TR-25_
  
  - [x] 34.2 Crear perfiles Maven para diferentes entornos
    - Crear perfil dev
    - Crear perfil test
    - Crear perfil prod
    - _Requisitos: TR-23_

- [ ] 35. Crear documentación de despliegue
  - [x] 35.1 Crear README.md
    - Documentar resumen del proyecto
    - Documentar prerequisitos
    - Documentar configuración de desarrollo local
    - Documentar ejecución con Docker
    - _Requisitos: NFR-7_
  
  - [x] 35.2 Crear DEPLOYMENT.md
    - Documentar proceso de despliegue
    - Documentar variables de entorno
    - Documentar gestión de configuración
    - Documentar procedimiento de rollback
    - _Requisitos: NFR-7, MR-3_
  
  - [x] 35.3 Crear ARCHITECTURE.md
    - Documentar arquitectura hexagonal
    - Documentar estructura de paquetes
    - Documentar decisiones de diseño
    - Incluir diagramas de arquitectura
    - _Requisitos: NFR-7_

- [ ] 36. Configurar pipeline CI/CD (opcional)
  - [ ] 36.1 Crear workflow GitHub Actions (o GitLab CI)
    - Configurar job de build
    - Configurar job de pruebas
    - Configurar job de calidad de código
    - Configurar job de escaneo de seguridad
    - Configurar job de build de imagen Docker
    - _Requisitos: TR-26_
  
  - [ ] 36.2 Configurar pruebas automatizadas
    - Ejecutar pruebas unitarias en cada commit
    - Ejecutar pruebas de integración en pull requests
    - Generar reportes de cobertura de código
    - _Requisitos: TR-26_
  
  - [ ] 36.3 Configurar despliegue automatizado a staging
    - Desplegar a staging en merge a main
    - Ejecutar smoke tests después del despliegue
    - _Requisitos: TR-26_

- [ ] 37. Preparar para despliegue en producción
  - [x] 37.1 Crear configuración de producción
    - Crear application-prod.properties
    - Configurar conexión de base de datos de producción
    - Configurar logging de producción
    - Configurar configuraciones de seguridad de producción
    - _Requisitos: TR-11, TR-23_
  
  - [x] 37.2 Crear checklist de despliegue
    - Procedimiento de respaldo de base de datos
    - Pasos de despliegue
    - Pasos de verificación
    - Procedimiento de rollback
    - _Requisitos: MR-2, MR-3_
  
  - [x] 37.3 Documentar plan de rollback
    - Documentar disparadores de rollback
    - Documentar pasos de rollback
    - Documentar verificación de rollback
    - Probar rollback en staging
    - _Requisitos: MR-3_

- [x] 38. Punto de verificación final - Preparación para producción
  - Verificar que todos los endpoints funcionan correctamente
  - Verificar autenticación y autorización
  - Verificar operaciones de base de datos
  - Verificar health checks y métricas
  - Verificar que la imagen Docker se construye exitosamente
  - Verificar que todas las pruebas pasan (unitarias, integración, rendimiento)
  - Verificar que la documentación está completa
  - Verificar que el plan de rollback está probado
  - Asegurar que todas las pruebas pasan, preguntar al usuario si surgen dudas.

---

## Notas

- **Enfoque Incremental**: Cada fase se construye sobre la anterior, permitiendo validación continua
- **Compatibilidad Hacia Atrás**: Los contratos de API se mantienen durante toda la migración
- **Estrategia de Pruebas**: Pruebas completas en cada capa (unitarias, integración, rendimiento)
- **Tareas de Verificación**: Puntos de validación regulares para detectar problemas temprano
- **Tareas Opcionales**: Las tareas marcadas con `*` son opcionales y pueden omitirse para un MVP más rápido
- **Trazabilidad de Requisitos**: Cada tarea referencia requisitos específicos para responsabilidad
- **Seguridad de Rollback**: Plan de rollback documentado y probado antes del despliegue en producción

## Criterios de Éxito

La migración está completa cuando:
- [x] Los 12 endpoints de API son funcionales con Java 21
- [x] Todas las pruebas pasan (unitarias, integración, API)
- [-] Cobertura de código > 80%
- [ ] Objetivos de rendimiento cumplidos (p95 < 500ms)
- [x] Requisitos de seguridad satisfechos (JWT, validación de entrada)
- [x] Documentación completa (README, DEPLOYMENT, ARCHITECTURE)
- [x] Imagen Docker se construye exitosamente
- [x] Aplicación se ejecuta en entorno contenedorizado
- [x] Health checks y métricas funcionando
- [x] Plan de rollback probado y documentado

## Cronograma Estimado

- **Fase 1**: 1-2 semanas (Fundación)
- **Fase 2**: 2-3 semanas (Migración Principal)
- **Fase 3**: 2-3 semanas (Refactorización Arquitectónica)
- **Fase 4**: 2 semanas (Seguridad y Documentación)
- **Fase 5**: 2 semanas (Observabilidad y Pruebas)
- **Fase 6**: 1-2 semanas (Contenedorización y Despliegue)

**Total**: 10-15 semanas (2.5-3.5 meses)

## Mitigación de Riesgos

- **Cambios Disruptivos**: Pruebas exhaustivas en cada fase
- **Problemas de Rendimiento**: Pruebas de rendimiento en Fase 5
- **Vulnerabilidades de Seguridad**: Pruebas de seguridad y escaneo de código
- **Pérdida de Datos**: Respaldo de base de datos antes del despliegue en producción
- **Fallos de Despliegue**: Plan de rollback probado y despliegue blue-green
