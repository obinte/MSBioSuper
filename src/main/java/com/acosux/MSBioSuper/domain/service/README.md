# Servicios de Dominio

## Propósito

Los servicios de dominio encapsulan lógica de negocio compleja que:
- Involucra múltiples entidades
- No pertenece naturalmente a una sola entidad
- Representa operaciones o reglas de negocio del dominio

## Principios de Diseño

### 1. Sin Dependencias de Infraestructura
Los servicios de dominio NO deben depender de:
- Spring Framework (`@Service`, `@Autowired`, etc.)
- JPA/Hibernate (`@Entity`, `EntityManager`, etc.)
- Bibliotecas de infraestructura (Jackson, Logback, etc.)

### 2. Lógica de Negocio Pura
Los servicios de dominio contienen:
- Reglas de negocio complejas
- Validaciones de dominio
- Cálculos de negocio
- Operaciones multi-entidad

### 3. Stateless (Sin Estado)
Los servicios de dominio:
- No mantienen estado entre llamadas
- Son thread-safe por diseño
- Pueden ser instanciados múltiples veces sin efectos secundarios

### 4. Testabilidad
Los servicios de dominio:
- Son fáciles de probar unitariamente
- No requieren mocks de infraestructura
- Tienen dependencias mínimas

## Servicios Disponibles

### VentasService
**Propósito**: Gestiona operaciones de negocio relacionadas con ventas.

**Responsabilidades**:
- Procesar relaciones bidireccionales entre ventas, detalles y liquidaciones
- Validar estructura de ventas
- Filtrar ventas según reglas de negocio

**Casos de uso**:
```java
VentasService ventasService = new VentasService();

// Procesar relaciones bidireccionales
List<InvVentasBioSuperTO> ventas = ventasService.procesarRelacionesBidireccionales(ventasRaw);

// Validar venta
boolean esValida = ventasService.esVentaValida(venta);

// Filtrar ventas válidas
List<InvVentasBioSuperTO> ventasValidas = ventasService.filtrarVentasValidas(ventas);
```

### ProductoService
**Propósito**: Gestiona operaciones de negocio relacionadas con productos.

**Responsabilidades**:
- Aplicar filtros complejos de negocio
- Validar productos
- Limitar resultados según reglas de negocio

**Casos de uso**:
```java
ProductoService productoService = new ProductoService();

// Aplicar filtros de negocio
List<InvListaProductosGeneralTO> productosFiltrados = productoService.aplicarFiltrosNegocio(
    productos, 
    "PROD001", 
    true,  // buscar por código
    "ALIMENTO", 
    true   // aplicar límite
);

// Validar producto
boolean esValido = productoService.esProductoValido(producto);

// Filtrar productos activos
List<InvListaProductosGeneralTO> productosActivos = productoService.filtrarProductosActivos(productos);
```

### LiquidacionService
**Propósito**: Gestiona operaciones de negocio relacionadas con liquidaciones.

**Responsabilidades**:
- Filtrar liquidaciones por criterios de negocio
- Calcular totales y promedios
- Agrupar liquidaciones
- Validar liquidaciones

**Casos de uso**:
```java
LiquidacionService liquidacionService = new LiquidacionService();

// Filtrar por corrida
List<PrdLiquidacionDetalleProductoTO> liquidacionesCorrida = 
    liquidacionService.filtrarPorCorrida(liquidaciones, "C001");

// Calcular totales
BigDecimal totalLibras = liquidacionService.calcularTotalLibras(liquidaciones);
BigDecimal totalValor = liquidacionService.calcularTotalValor(liquidaciones);

// Calcular precio promedio
BigDecimal precioPromedio = liquidacionService.calcularPrecioPromedioLibra(liquidaciones);

// Agrupar por cliente
Map<String, List<PrdLiquidacionDetalleProductoTO>> porCliente = 
    liquidacionService.agruparPorCliente(liquidaciones);
```

### ResumenCorridaService
**Propósito**: Gestiona operaciones de negocio relacionadas con resúmenes de corrida.

**Responsabilidades**:
- Filtrar resúmenes por criterios de negocio
- Calcular totales y promedios
- Agrupar resúmenes
- Validar resúmenes

**Casos de uso**:
```java
ResumenCorridaService resumenService = new ResumenCorridaService();

// Filtrar por piscina
List<PrdResumenCorridaTO> resumenesPiscina = 
    resumenService.filtrarPorPiscina(resumenes, "P001");

// Calcular totales
BigDecimal totalLibras = resumenService.calcularTotalLibras(resumenes);
Integer totalAnimales = resumenService.calcularTotalAnimales(resumenes);

// Calcular peso promedio
BigDecimal pesoPromedio = resumenService.calcularPesoPromedioAnimal(resumenes);

// Filtrar corridas activas
List<PrdResumenCorridaTO> corridasActivas = resumenService.filtrarCorridasActivas(resumenes);
```

### CuentasService
**Propósito**: Gestiona operaciones de negocio relacionadas con cuentas por cobrar y pagar.

**Responsabilidades**:
- Calcular totales de saldos
- Filtrar cuentas vencidas
- Agrupar cuentas por cliente/proveedor
- Validar cuentas

**Casos de uso**:
```java
CuentasService cuentasService = new CuentasService();

// Calcular totales
BigDecimal totalCobrar = cuentasService.calcularTotalSaldoCobrar(cuentasCobrar);
BigDecimal totalPagar = cuentasService.calcularTotalSaldoPagar(cuentasPagar);

// Filtrar cuentas vencidas
List<CuentasPorCobrarDetalladoApiTO> cuentasVencidas = 
    cuentasService.filtrarCuentasCobrarVencidas(cuentasCobrar, new Date());

// Agrupar por cliente
Map<String, List<CuentasPorCobrarDetalladoApiTO>> porCliente = 
    cuentasService.agruparCuentasCobrarPorCliente(cuentasCobrar);
```

## Integración con Casos de Uso

Los servicios de dominio son utilizados por los casos de uso en la capa de aplicación:

```java
@Service
@Transactional(readOnly = true)
public class ListarVentasUseCase {
    
    private final VentasRepository ventasRepository;
    private final VentasService ventasService; // Servicio de dominio
    
    public ListarVentasUseCase(
            VentasRepository ventasRepository,
            VentasService ventasService) {
        this.ventasRepository = ventasRepository;
        this.ventasService = ventasService;
    }
    
    public List<VentaResponse> execute(VentaRequest request) {
        // 1. Obtener datos del repositorio
        List<InvVentasBioSuperTO> ventas = ventasRepository.findVentas(...);
        
        // 2. Aplicar lógica de negocio del dominio
        ventas = ventasService.procesarRelacionesBidireccionales(ventas);
        ventas = ventasService.filtrarVentasValidas(ventas);
        
        // 3. Mapear a DTOs de respuesta
        return mapper.toResponseList(ventas);
    }
}
```

## Cuándo Crear un Servicio de Dominio

Crea un servicio de dominio cuando:
- La lógica involucra múltiples entidades
- La lógica no pertenece naturalmente a una entidad
- La lógica es compleja y merece ser aislada
- La lógica representa una operación o regla de negocio importante

NO crees un servicio de dominio para:
- Operaciones CRUD simples (usa repositorios)
- Lógica de presentación (usa DTOs y mappers)
- Lógica de infraestructura (usa adaptadores)

## Testing

Los servicios de dominio son fáciles de probar:

```java
class VentasServiceTest {
    
    private VentasService ventasService;
    
    @BeforeEach
    void setUp() {
        ventasService = new VentasService();
    }
    
    @Test
    void deberiaValidarVentaConDetalles() {
        // Given
        InvVentasBioSuperTO venta = crearVentaConDetalles();
        
        // When
        boolean esValida = ventasService.esVentaValida(venta);
        
        // Then
        assertTrue(esValida);
    }
    
    @Test
    void deberiaRechazarVentaSinDetalles() {
        // Given
        InvVentasBioSuperTO venta = crearVentaSinDetalles();
        
        // When
        boolean esValida = ventasService.esVentaValida(venta);
        
        // Then
        assertFalse(esValida);
    }
}
```

## Mejores Prácticas

1. **Mantén los servicios pequeños y enfocados**: Cada servicio debe tener una responsabilidad clara
2. **Usa nombres descriptivos**: Los métodos deben expresar claramente su intención de negocio
3. **Documenta las reglas de negocio**: Explica el "por qué" de cada operación
4. **Valida entradas**: Siempre valida parámetros nulos o inválidos
5. **Retorna valores inmutables**: Usa `List.of()` en lugar de listas mutables cuando sea posible
6. **Evita efectos secundarios**: Los métodos no deben modificar el estado global

## Referencias

- [Domain-Driven Design (DDD)](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
