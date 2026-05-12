# Arquitectura Hexagonal - MSBioSuper

## Visión General

Este proyecto sigue el patrón de **Arquitectura Hexagonal** (también conocido como Puertos y Adaptadores), que proporciona una clara separación entre la lógica de negocio y las preocupaciones técnicas de infraestructura.

## Estructura de Paquetes

```
com.acosux.msbiosuper/
├── application/              # Capa de Aplicación
│   ├── dto/                 # Data Transfer Objects
│   ├── mapper/              # Conversores DTO ↔ Entidad
│   └── usecase/             # Casos de Uso
│
├── domain/                   # Capa de Dominio (Núcleo)
│   ├── exception/           # Excepciones de Dominio
│   ├── model/               # Entidades de Dominio
│   ├── repository/          # Interfaces de Repositorio (Puertos)
│   └── service/             # Servicios de Dominio
│
├── infrastructure/           # Capa de Infraestructura
│   ├── config/              # Configuración de Spring Boot
│   ├── exception/           # Manejadores Globales de Excepciones
│   ├── persistence/         # Implementaciones de Repositorios JPA
│   ├── rest/                # Controladores REST
│   └── security/            # Configuración de Seguridad
│
└── shared/                   # Componentes Compartidos
    ├── constant/            # Constantes
    ├── exception/           # Excepciones Base
    └── util/                # Utilidades Comunes
```

## Capas y Responsabilidades

### 1. Capa de Dominio (Domain Layer)

**Propósito**: Contiene la lógica de negocio pura, independiente de frameworks e infraestructura.

**Componentes**:
- **Entidades de Dominio** (`domain/model/`): Objetos de negocio con comportamiento
- **Interfaces de Repositorio** (`domain/repository/`): Contratos para acceso a datos (Puertos)
- **Servicios de Dominio** (`domain/service/`): Lógica de negocio compleja
- **Excepciones de Dominio** (`domain/exception/`): Violaciones de reglas de negocio

**Reglas**:
- ✅ Sin dependencias de Spring, Hibernate o cualquier framework
- ✅ Solo objetos Java puros con lógica de negocio
- ✅ Depende únicamente de la biblioteca estándar de Java
- ❌ No debe conocer detalles de infraestructura (base de datos, HTTP, etc.)

**Ejemplo**:
```java
// domain/model/ResumenPesca.java
public class ResumenPesca {
    private String empresa;
    private LocalDate fecha;
    private BigDecimal cantidad;
    
    // Lógica de negocio pura
    public boolean esValido() {
        return cantidad != null && cantidad.compareTo(BigDecimal.ZERO) > 0;
    }
}

// domain/repository/ResumenPescaRepository.java (Puerto)
public interface ResumenPescaRepository {
    List<ResumenPesca> findByDateRange(LocalDate start, LocalDate end);
}
```

### 2. Capa de Aplicación (Application Layer)

**Propósito**: Orquesta objetos de dominio para cumplir casos de uso específicos.

**Componentes**:
- **Casos de Uso** (`application/usecase/`): Reglas de negocio específicas de la aplicación
- **DTOs** (`application/dto/`): Objetos de transferencia de datos
- **Mappers** (`application/mapper/`): Conversión entre DTOs y entidades de dominio

**Reglas**:
- ✅ Depende de la capa de dominio
- ✅ Orquesta objetos de dominio
- ✅ Maneja límites transaccionales
- ❌ Sin dependencia directa de infraestructura

**Ejemplo**:
```java
// application/usecase/GetResumenPescaUseCase.java
@Service
@Transactional(readOnly = true)
public class GetResumenPescaUseCase {
    private final ResumenPescaRepository repository;
    private final ResumenPescaMapper mapper;
    
    public List<ResumenPescaResponse> execute(ResumenPescaRequest request) {
        // 1. Convertir DTO a parámetros de dominio
        LocalDate start = request.fechaInicio();
        LocalDate end = request.fechaFin();
        
        // 2. Ejecutar lógica de dominio
        List<ResumenPesca> resultados = repository.findByDateRange(start, end);
        
        // 3. Convertir entidades a DTOs de respuesta
        return resultados.stream()
            .map(mapper::toResponse)
            .toList();
    }
}
```

### 3. Capa de Infraestructura (Infrastructure Layer)

**Propósito**: Implementa preocupaciones técnicas y adaptadores a sistemas externos.

**Componentes**:
- **Controladores REST** (`infrastructure/rest/`): Endpoints HTTP (Adaptadores)
- **Repositorios JPA** (`infrastructure/persistence/`): Implementaciones de acceso a datos (Adaptadores)
- **Configuración de Seguridad** (`infrastructure/security/`): JWT, Spring Security
- **Configuración** (`infrastructure/config/`): Beans de Spring, configuración de BD
- **Manejadores de Excepciones** (`infrastructure/exception/`): Manejo global de errores

**Reglas**:
- ✅ Depende de las capas de aplicación y dominio
- ✅ Implementa interfaces de repositorio del dominio
- ✅ Contiene todo el código específico de frameworks
- ✅ Maneja preocupaciones técnicas (logging, monitoreo, etc.)

**Ejemplo**:
```java
// infrastructure/rest/ResumenPescaController.java
@RestController
@RequestMapping("/api/v1/resumen-pesca")
public class ResumenPescaController {
    private final GetResumenPescaUseCase useCase;
    
    @PostMapping
    public ResponseEntity<List<ResumenPescaResponse>> getResumenPesca(
            @Valid @RequestBody ResumenPescaRequest request) {
        List<ResumenPescaResponse> response = useCase.execute(request);
        return ResponseEntity.ok(response);
    }
}

// infrastructure/persistence/ResumenPescaJpaRepository.java (Adaptador)
@Repository
public interface ResumenPescaJpaRepository 
        extends JpaRepository<ResumenPesca, Long>, ResumenPescaRepository {
    
    @Query("SELECT r FROM ResumenPesca r WHERE r.fecha BETWEEN :start AND :end")
    List<ResumenPesca> findByDateRange(
        @Param("start") LocalDate start, 
        @Param("end") LocalDate end
    );
}
```

### 4. Capa Compartida (Shared Layer)

**Propósito**: Contiene utilidades y componentes compartidos entre todas las capas.

**Componentes**:
- **Utilidades Comunes** (`shared/util/`): Funciones auxiliares reutilizables
- **Excepciones Base** (`shared/exception/`): Jerarquía de excepciones del sistema
- **Constantes** (`shared/constant/`): Valores constantes compartidos

**Reglas**:
- ✅ Sin dependencias de otras capas de la aplicación
- ✅ Solo utilidades genéricas y reutilizables
- ✅ Puede ser usado por cualquier capa

## Flujo de Interacción

```
Cliente HTTP
    ↓
[REST Controller] (Infrastructure)
    ↓
[Use Case] (Application)
    ↓
[Domain Service] (Domain)
    ↓
[Repository Interface] (Domain - Puerto)
    ↓
[JPA Repository] (Infrastructure - Adaptador)
    ↓
Base de Datos PostgreSQL
```

## Principios de Diseño

### 1. Dependency Rule (Regla de Dependencia)

Las dependencias solo pueden apuntar hacia adentro:

```
Infrastructure → Application → Domain
     ↓              ↓
   Shared ←────────┘
```

- **Domain**: No depende de nadie (excepto Java estándar)
- **Application**: Depende solo de Domain
- **Infrastructure**: Depende de Application y Domain
- **Shared**: Independiente, usado por todos

### 2. Ports & Adapters (Puertos y Adaptadores)

- **Puertos**: Interfaces definidas en el dominio (ej: `ResumenPescaRepository`)
- **Adaptadores**: Implementaciones en infraestructura (ej: `ResumenPescaJpaRepository`)

### 3. Separation of Concerns (Separación de Preocupaciones)

- **Domain**: ¿Qué hace el negocio?
- **Application**: ¿Cómo se orquestan las operaciones?
- **Infrastructure**: ¿Cómo se implementan técnicamente?

## Beneficios de esta Arquitectura

1. **Testabilidad**: La lógica de negocio puede probarse sin frameworks
2. **Mantenibilidad**: Cambios en infraestructura no afectan el dominio
3. **Flexibilidad**: Fácil cambiar de base de datos, framework web, etc.
4. **Claridad**: Separación clara de responsabilidades
5. **Independencia de Frameworks**: El dominio no está acoplado a Spring, Hibernate, etc.

## Convenciones de Nomenclatura

### Casos de Uso
- Patrón: `{Verbo}{Entidad}UseCase`
- Ejemplos: `GetResumenPescaUseCase`, `CreateLiquidacionUseCase`

### DTOs
- Request: `{Entidad}Request`
- Response: `{Entidad}Response`
- Ejemplos: `ResumenPescaRequest`, `ResumenPescaResponse`

### Repositorios
- Interfaz (Puerto): `{Entidad}Repository`
- Implementación (Adaptador): `{Entidad}JpaRepository`
- Ejemplos: `ResumenPescaRepository`, `ResumenPescaJpaRepository`

### Controladores
- Patrón: `{Entidad}Controller`
- Ejemplos: `ResumenPescaController`, `LiquidacionController`

## Migración desde Arquitectura Monolítica

Durante la migración de la arquitectura monolítica anterior:

1. **Fase 1**: Crear estructura de paquetes (esta tarea)
2. **Fase 2**: Mover entidades a `domain/model/`
3. **Fase 3**: Crear interfaces de repositorio en `domain/repository/`
4. **Fase 4**: Implementar repositorios JPA en `infrastructure/persistence/`
5. **Fase 5**: Crear casos de uso en `application/usecase/`
6. **Fase 6**: Refactorizar controladores en `infrastructure/rest/`
7. **Fase 7**: Eliminar código legacy (DAOs, servicios antiguos)

## Referencias

- [Hexagonal Architecture (Alistair Cockburn)](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture (Robert C. Martin)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design (Eric Evans)](https://www.domainlanguage.com/ddd/)

---

**Versión**: 1.0  
**Fecha**: 2026-04-22  
**Autor**: Josias Rubén Aldaz Arcalles
