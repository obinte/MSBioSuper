# Configuración de Pruebas de Integración con Testcontainers

## Descripción General

Este directorio contiene la configuración base para pruebas de integración utilizando **Testcontainers**, una biblioteca Java que permite ejecutar contenedores Docker durante las pruebas, proporcionando entornos de base de datos reales y aislados.

## ¿Qué es Testcontainers?

Testcontainers es una biblioteca Java que soporta pruebas JUnit, proporcionando instancias ligeras y desechables de bases de datos, navegadores web, o cualquier cosa que pueda ejecutarse en un contenedor Docker.

### Ventajas de Testcontainers

1. **Base de datos real**: Pruebas contra PostgreSQL real, no H2 en memoria
2. **Aislamiento**: Cada ejecución de pruebas usa un contenedor limpio
3. **Consistencia**: Mismo comportamiento en desarrollo, CI/CD y producción
4. **Facilidad**: No requiere instalación manual de PostgreSQL
5. **Portabilidad**: Funciona en cualquier máquina con Docker

## Prerequisitos

### 1. Docker Instalado

Testcontainers requiere Docker para ejecutar los contenedores. Verifica que Docker esté instalado y ejecutándose:

```bash
docker --version
docker ps
```

### 2. Dependencias Maven

Las siguientes dependencias ya están configuradas en `pom.xml`:

```xml
<!-- Testcontainers Core -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers</artifactId>
    <version>1.20.4</version>
    <scope>test</scope>
</dependency>

<!-- Testcontainers PostgreSQL Module -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>1.20.4</version>
    <scope>test</scope>
</dependency>

<!-- Testcontainers JUnit 5 Integration -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>1.20.4</version>
    <scope>test</scope>
</dependency>
```

## Uso de BaseIntegrationTest

### Clase Base

`BaseIntegrationTest` es la clase abstracta que todas las pruebas de integración deben extender:

```java
@SpringBootTest
@Testcontainers
public abstract class BaseIntegrationTest {
    
    @Container
    protected static final PostgreSQLContainer<?> postgresContainer = 
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withReuse(true);
    
    // Configuración dinámica de propiedades...
}
```

### Características Principales

1. **Contenedor Compartido**: Un solo contenedor PostgreSQL para todas las pruebas (mejor rendimiento)
2. **Configuración Automática**: Las propiedades de Spring Boot se configuran automáticamente
3. **Rollback Automático**: Las transacciones se revierten después de cada prueba
4. **Aislamiento de Datos**: Cada prueba comienza con una base de datos limpia

## Crear Pruebas de Integración

### Ejemplo Básico

```java
@Transactional
@DisplayName("Pruebas de Integración - MiRepositorio")
class MiRepositorioIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MiRepository repository;

    @Test
    @DisplayName("Debe guardar y recuperar una entidad")
    void shouldSaveAndRetrieveEntity() {
        // Given
        MiEntidad entidad = new MiEntidad();
        entidad.setNombre("Test");
        
        // When
        MiEntidad saved = repository.save(entidad);
        
        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNombre()).isEqualTo("Test");
    }
}
```

### Patrón Given-When-Then

Todas las pruebas deben seguir el patrón **Given-When-Then** (Dado-Cuando-Entonces):

```java
@Test
void testExample() {
    // Given - Preparar el estado inicial
    MiEntidad entidad = crearEntidadDePrueba();
    repository.save(entidad);
    
    // When - Ejecutar la acción a probar
    List<MiEntidad> resultado = repository.findByNombre("Test");
    
    // Then - Verificar el resultado
    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getNombre()).isEqualTo("Test");
}
```

## Configuración del Contenedor PostgreSQL

### Imagen Docker

- **Imagen**: `postgres:16-alpine`
- **Versión**: PostgreSQL 16 (última versión estable)
- **Variante**: Alpine Linux (imagen ligera, ~80MB)

### Credenciales de Prueba

- **Base de datos**: `testdb`
- **Usuario**: `testuser`
- **Contraseña**: `testpass`

### Propiedades Configuradas Automáticamente

```properties
spring.datasource.url=jdbc:postgresql://localhost:<puerto-aleatorio>/testdb
spring.datasource.username=testuser
spring.datasource.password=testpass
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.datasource.hikari.maximum-pool-size=5
```

## Ejecución de Pruebas

### Desde Maven

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar solo pruebas de integración
mvn test -Dtest="*IntegrationTest"

# Ejecutar una prueba específica
mvn test -Dtest=ResumenPescaJpaRepositoryIntegrationTest
```

### Desde IDE

1. Asegúrate de que Docker esté ejecutándose
2. Ejecuta la prueba normalmente desde tu IDE (IntelliJ IDEA, Eclipse, VS Code)
3. Testcontainers descargará la imagen de PostgreSQL automáticamente (solo la primera vez)
4. El contenedor se iniciará, ejecutará las pruebas, y se detendrá automáticamente

## Rendimiento y Optimización

### Reutilización de Contenedores

El contenedor está configurado con `.withReuse(true)`, lo que significa que:

- El contenedor se mantiene ejecutándose entre ejecuciones de pruebas
- Mejora significativamente el tiempo de ejecución
- Requiere configuración adicional en `~/.testcontainers.properties`:

```properties
testcontainers.reuse.enable=true
```

### Tiempo de Ejecución

- **Primera ejecución**: ~30-60 segundos (descarga de imagen + inicio)
- **Ejecuciones subsecuentes**: ~5-10 segundos (contenedor reutilizado)

## Troubleshooting

### Docker no está ejecutándose

**Error**: `Could not find a valid Docker environment`

**Solución**: Inicia Docker Desktop o el daemon de Docker:
```bash
# Linux
sudo systemctl start docker

# macOS
open -a Docker

# Windows
# Inicia Docker Desktop desde el menú de inicio
```

### Puerto ya en uso

**Error**: `Port 5432 is already allocated`

**Solución**: Testcontainers usa puertos aleatorios, pero si tienes PostgreSQL local ejecutándose, puede causar conflictos. Detén PostgreSQL local:
```bash
# Linux
sudo systemctl stop postgresql

# macOS
brew services stop postgresql
```

### Imagen no se descarga

**Error**: `Unable to pull image`

**Solución**: Verifica tu conexión a internet y permisos de Docker:
```bash
docker pull postgres:16-alpine
```

### Pruebas lentas

**Solución**: Habilita la reutilización de contenedores:

1. Crea `~/.testcontainers.properties`:
```properties
testcontainers.reuse.enable=true
```

2. Verifica que el contenedor tenga `.withReuse(true)` (ya configurado)

## Mejores Prácticas

### 1. Usar @Transactional

Siempre anota las clases de prueba con `@Transactional` para rollback automático:

```java
@Transactional
class MiRepositorioIntegrationTest extends BaseIntegrationTest {
    // Las transacciones se revierten después de cada prueba
}
```

### 2. Datos de Prueba Aislados

Crea datos de prueba dentro de cada método de prueba:

```java
@Test
void testExample() {
    // Crear datos específicos para esta prueba
    MiEntidad entidad = crearEntidadDePrueba();
    repository.save(entidad);
    
    // Ejecutar prueba...
}
```

### 3. Métodos Auxiliares

Crea métodos auxiliares para construcción de entidades:

```java
private MiEntidad crearEntidadDePrueba(String nombre) {
    MiEntidad entidad = new MiEntidad();
    entidad.setNombre(nombre);
    entidad.setFecha(LocalDate.now());
    return entidad;
}
```

### 4. Assertions Descriptivas

Usa AssertJ para assertions más legibles:

```java
assertThat(resultado)
    .isNotNull()
    .hasSize(3)
    .extracting(MiEntidad::getNombre)
    .containsExactlyInAnyOrder("A", "B", "C");
```

## Recursos Adicionales

- [Documentación oficial de Testcontainers](https://www.testcontainers.org/)
- [Testcontainers con Spring Boot](https://www.testcontainers.org/modules/databases/postgres/)
- [Guía de JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [AssertJ Documentation](https://assertj.github.io/doc/)

## Soporte

Para problemas o preguntas sobre las pruebas de integración, contacta al equipo de desarrollo.

---

**Última actualización**: 2026-04-24  
**Autor**: Josias Rubén Aldaz Arcalles
