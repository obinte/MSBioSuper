# Pruebas de Repositorios JPA

## Situación Actual

Las pruebas unitarias creadas para los repositorios JPA (`*JpaRepositoryTest.java`) están diseñadas para usar `@DataJpaTest` con una base de datos H2 en memoria. Sin embargo, existe una limitación importante:

### Limitación Identificada

Las entidades del dominio (`InvComprasDetalleProductoTO`, `PrdConsumosDiarioMSTO`, etc.) están diseñadas para trabajar con **funciones de PostgreSQL** en lugar de tablas directas. Estas entidades:

1. No tienen anotación `@Table` que especifique el nombre de la tabla
2. Los métodos de consulta personalizados con `@Query` llaman a funciones de PostgreSQL (ej: `inventario.fun_compras_detalle_producto`)
3. La estructura de las columnas está optimizada para el resultado de funciones, no para tablas normalizadas

### Implicaciones

- **Pruebas con H2**: No funcionarán correctamente porque H2 no tiene las funciones de PostgreSQL necesarias
- **Pruebas con @DataJpaTest**: Requieren que las entidades tengan tablas reales en la base de datos

## Soluciones Propuestas

### Opción 1: Pruebas de Integración con Testcontainers (RECOMENDADO)

Usar Testcontainers para levantar un contenedor PostgreSQL real con las funciones necesarias:

```java
@SpringBootTest
@Testcontainers
@ActiveProfiles("integration-test")
class ComprasJpaRepositoryIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("init-db.sql"); // Script con funciones PostgreSQL
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Autowired
    private ComprasJpaRepository comprasRepository;
    
    @Test
    void debeEncontrarComprasPorEmpresa() {
        // Prueba con base de datos PostgreSQL real
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findComprasDetalleProducto(
                        "01", null, null, null, null, 
                        "2024-01-01", "2024-12-31", null, false);
        
        assertThat(resultado).isNotNull();
    }
}
```

**Ventajas**:
- Pruebas realistas con PostgreSQL real
- Funciones de base de datos disponibles
- Comportamiento idéntico a producción

**Desventajas**:
- Requiere Docker
- Pruebas más lentas
- Requiere scripts de inicialización con las funciones

### Opción 2: Pruebas Unitarias de Métodos Derivados Solamente

Mantener las pruebas actuales pero solo para métodos de consulta derivados (Query Methods) que no requieren funciones de PostgreSQL:

```java
@DataJpaTest
@ActiveProfiles("test")
class ComprasJpaRepositoryTest {
    
    @Autowired
    private ComprasJpaRepository comprasRepository;
    
    @Test
    void debeEncontrarComprasPorEmpresa() {
        // Solo probar métodos derivados como findByEmpresa
        // NO probar métodos con @Query que llaman funciones PostgreSQL
    }
}
```

**Ventajas**:
- Pruebas rápidas
- No requiere Docker
- Útil para validar lógica de Spring Data JPA

**Desventajas**:
- No prueba los métodos más importantes (con @Query)
- Cobertura limitada

### Opción 3: Mocks con Mockito

Usar mocks para probar la lógica de negocio que usa los repositorios:

```java
@ExtendWith(MockitoExtension.class)
class ComprasServiceTest {
    
    @Mock
    private ComprasJpaRepository comprasRepository;
    
    @InjectMocks
    private ComprasService comprasService;
    
    @Test
    void debeObtenerComprasPorEmpresa() {
        // Arrange
        List<InvComprasDetalleProductoTO> comprasMock = Arrays.asList(/* ... */);
        when(comprasRepository.findByEmpresa("01")).thenReturn(comprasMock);
        
        // Act
        List<CompraDTO> resultado = comprasService.obtenerComprasPorEmpresa("01");
        
        // Assert
        assertThat(resultado).hasSize(comprasMock.size());
        verify(comprasRepository).findByEmpresa("01");
    }
}
```

**Ventajas**:
- Pruebas muy rápidas
- No requiere base de datos
- Enfoque en lógica de negocio

**Desventajas**:
- No prueba la capa de persistencia real
- Requiere mantener mocks actualizados

## Recomendación

Para este proyecto, se recomienda **Opción 1: Pruebas de Integración con Testcontainers** porque:

1. Las funciones de PostgreSQL son parte crítica de la lógica de negocio
2. Los métodos con `@Query` son los más importantes y deben probarse
3. Proporciona confianza real en que el código funciona con la base de datos de producción

## Pasos para Implementar Testcontainers

1. Agregar dependencia en `pom.xml`:
```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>
```

2. Crear script SQL con las funciones de PostgreSQL en `src/test/resources/init-db.sql`

3. Convertir las pruebas actuales a pruebas de integración

## Estado Actual de las Pruebas

Las pruebas creadas en este directorio son **plantillas** que muestran:
- Estructura correcta de pruebas con JUnit 5
- Uso de AssertJ para aserciones
- Patrón Given-When-Then
- Nombres descriptivos en español

Estas pruebas necesitan ser adaptadas según la opción elegida.

## Requisitos del Spec

Según TR-19 (Cobertura de código > 80%, JUnit 5, Mockito), se recomienda:
- Usar **Opción 1** para pruebas de repositorio (integración)
- Usar **Opción 3** para pruebas de servicios y casos de uso (unitarias con mocks)
- Combinar ambas para lograr cobertura > 80%
