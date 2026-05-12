# Tarea 30.3 - Pruebas de Carga y Estrés: BLOQUEADA

## Fecha
2026-04-24

## Estado
**BLOQUEADA** - No se puede ejecutar debido a errores en el código que impiden el inicio de la aplicación

## Resumen

La tarea 30.3 del spec de migración a Java 21 requiere ejecutar pruebas de carga y estrés con Gatling. Sin embargo, la aplicación MSBioSuper no puede iniciarse debido a múltiples errores en los repositorios JPA.

## Problemas Identificados

### Errores en Repositorios JPA

Los repositorios JPA tienen métodos que no coinciden con los nombres de las propiedades de las entidades. Spring Data JPA genera automáticamente las consultas basándose en los nombres de los métodos, pero estos nombres no corresponden a las propiedades reales de las entidades.

#### Repositorios con Errores

1. ✅ **GrameajeJpaRepository** - CORREGIDO
   - Métodos corregidos para usar nombres de propiedades correctos (graEmpresa, graPiscina, etc.)

2. ✅ **LiquidacionJpaRepository** - CORREGIDO
   - Agregadas anotaciones @Query para bypass de nombres incorrectos
   - Métodos ahora usan JPQL explícito

3. ❌ **ProductoJpaRepository** - PENDIENTE
   - Error: `No property 'categoria' found for type 'InvListaProductosGeneralTO'`
   - Método problemático: `findByCategoriaAndActivo`
   - Propiedad real en entidad: Necesita investigación

4. ❌ **ConsumosDiariosJpaRepository** - PENDIENTE
   - Métodos como `findByEmpresa`, `findByPiscina` no coinciden con propiedades de entidad
   - Propiedades reales: `empCodigo`, `pisNumero`, etc.

5. ❌ **VentasJpaRepository** - PENDIENTE
   - Métodos como `findByEmpresa`, `findByCliente` no coinciden
   - Requiere corrección similar a los anteriores

6. ❌ **ResumenPescaJpaRepository** - PENDIENTE
   - Métodos como `findByEmpresa`, `findByPiscina` no coinciden
   - Requiere corrección

7. ❌ **ResumenSiembraJpaRepository** - PENDIENTE
   - Métodos como `findByEmpresa`, `findByPiscina` no coinciden
   - Requiere corrección

8. ❌ **ComprasJpaRepository** - PENDIENTE
   - Métodos comentados pero potencialmente problemáticos
   - Requiere revisión

## Trabajo Realizado

### 1. Corrección de GrameajeJpaRepository
- Actualizado `GrameajeJpaRepository.java` para usar nombres correctos de propiedades
- Actualizado `GrameajeRepository.java` (interfaz del dominio)
- Cambios: `findByEmpresa` → `findByGraEmpresa`, etc.

### 2. Corrección de LiquidacionJpaRepository
- Agregadas anotaciones `@Query` con JPQL explícito
- Esto permite mantener los nombres de métodos originales
- Ejemplo:
  ```java
  @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqIdCliente = :clienteId")
  List<PrdLiquidacionesDetalleTO> findByCliente(@Param("clienteId") String clienteId);
  ```

### 3. Compilación y Empaquetado
- Aplicación compila correctamente con los cambios realizados
- JAR generado exitosamente
- Sin embargo, falla al iniciar debido a errores en otros repositorios

## Opciones para Continuar

### Opción 1: Corregir Todos los Repositorios (Recomendado)
**Tiempo estimado**: 30-45 minutos adicionales

**Pasos**:
1. Identificar propiedades correctas en cada entidad
2. Agregar anotaciones `@Query` a todos los métodos problemáticos
3. Recompilar y probar inicio de aplicación
4. Ejecutar pruebas de rendimiento

**Ventajas**:
- Solución completa y permanente
- Aplicación funcionará correctamente
- Pruebas de rendimiento se podrán ejecutar

**Desventajas**:
- Requiere tiempo adicional significativo
- Múltiples archivos a modificar

### Opción 2: Comentar Repositorios Problemáticos Temporalmente
**Tiempo estimado**: 10-15 minutos

**Pasos**:
1. Comentar temporalmente los repositorios problemáticos
2. Comentar los casos de uso que los utilizan
3. Iniciar aplicación con funcionalidad reducida
4. Ejecutar pruebas de rendimiento en endpoints disponibles

**Ventajas**:
- Rápido de implementar
- Permite ejecutar pruebas parciales

**Desventajas**:
- Funcionalidad reducida
- No es una solución permanente
- Pruebas no serán completas

### Opción 3: Posponer Tarea
**Tiempo estimado**: Inmediato

**Pasos**:
1. Documentar todos los problemas encontrados
2. Crear issues/tareas para corregir cada repositorio
3. Marcar tarea 30.3 como bloqueada
4. Continuar con otras tareas del spec

**Ventajas**:
- No bloquea otras tareas
- Permite planificar correcciones adecuadamente

**Desventajas**:
- Pruebas de rendimiento no se ejecutan
- Tarea queda pendiente

## Infraestructura de Pruebas Existente

La infraestructura de pruebas de rendimiento está completamente implementada y lista para usar:

### Archivos Disponibles
- ✅ `docs/PERFORMANCE-TESTING.md` - Documentación completa
- ✅ `docs/PERFORMANCE-TESTING-QUICKSTART.md` - Guía rápida
- ✅ `scripts/run-performance-tests.sh` - Script de ejecución
- ✅ Simulaciones de Gatling implementadas:
  - `LoadTestSimulation` - Prueba de carga (100 usuarios)
  - `StressTestSimulation` - Prueba de estrés (500 usuarios)
  - `SpikeTestSimulation` - Prueba de pico (1000 usuarios)
  - `SoakTestSimulation` - Prueba de resistencia (1 hora)

### Configuración
- ✅ Gatling configurado en `pom.xml`
- ✅ Propiedades de prueba en `src/test/resources/gatling-test.properties`
- ✅ Endpoints de API documentados

## Objetivos de Rendimiento (TR-22, NFR-2)

Una vez que la aplicación inicie correctamente, las pruebas deben validar:

| Métrica | Objetivo | Crítico |
|---------|----------|---------|
| Tiempo de respuesta API (p95) | < 500ms | < 1000ms |
| Throughput | > 1000 req/sec | > 500 req/sec |
| Tiempo de consulta DB (p95) | < 100ms | < 200ms |
| Uso de memoria bajo carga | < 512MB | < 1GB |
| Tasa de éxito | > 95% | > 90% |

## Recomendación

**Recomiendo la Opción 1**: Corregir todos los repositorios problemáticos.

**Justificación**:
- Es la única solución que permite ejecutar las pruebas completas
- Los errores deben corregirse eventualmente de todos modos
- La aplicación no puede desplegarse en producción con estos errores
- El tiempo adicional (30-45 min) es razonable para una solución permanente

## Próximos Pasos

Si se aprueba la Opción 1:

1. **Corregir ProductoJpaRepository** (5-10 min)
   - Leer entidad `InvListaProductosGeneralTO`
   - Agregar anotaciones `@Query` a métodos problemáticos

2. **Corregir ConsumosDiariosJpaRepository** (5-10 min)
   - Agregar anotaciones `@Query` con nombres correctos de propiedades

3. **Corregir VentasJpaRepository** (5-10 min)
   - Similar al anterior

4. **Corregir ResumenPescaJpaRepository** (5-10 min)
   - Similar al anterior

5. **Corregir ResumenSiembraJpaRepository** (5-10 min)
   - Similar al anterior

6. **Verificar ComprasJpaRepository** (2-3 min)
   - Revisar si necesita correcciones

7. **Recompilar y Probar** (5 min)
   - Compilar aplicación
   - Iniciar y verificar que no hay errores
   - Confirmar que todos los endpoints están disponibles

8. **Ejecutar Pruebas de Rendimiento** (15-20 min)
   - Ejecutar prueba de carga (100 usuarios, 5 min)
   - Ejecutar prueba de estrés (configuración para 1000 req/sec, 10 min)
   - Analizar reportes de Gatling
   - Documentar resultados

## Archivos Modificados

### Ya Modificados
- `src/main/java/com/acosux/MSBiosuper/infrastructure/persistence/GrameajeJpaRepository.java`
- `src/main/java/com/acosux/MSBiosuper/domain/repository/GrameajeRepository.java`
- `src/main/java/com/acosux/MSBiosuper/infrastructure/persistence/LiquidacionJpaRepository.java`

### Pendientes de Modificar
- `src/main/java/com/acosux/MSBiosuper/infrastructure/persistence/ProductoJpaRepository.java`
- `src/main/java/com/acosux/MSBiosuper/infrastructure/persistence/ConsumosDiariosJpaRepository.java`
- `src/main/java/com/acosux/MSBiosuper/infrastructure/persistence/VentasJpaRepository.java`
- `src/main/java/com/acosux/MSBiosuper/infrastructure/persistence/ResumenPescaJpaRepository.java`
- `src/main/java/com/acosux/MSBiosuper/infrastructure/persistence/ResumenSiembraJpaRepository.java`
- `src/main/java/com/acosux/MSBiosuper/infrastructure/persistence/ComprasJpaRepository.java` (verificar)

## Conclusión

La tarea 30.3 está bloqueada debido a errores en el código que impiden el inicio de la aplicación. Se han corregido 2 de 8 repositorios problemáticos. Se requiere decisión sobre cómo proceder: corregir todos los repositorios (recomendado), implementar solución temporal, o posponer la tarea.

---

**Autor**: Kiro AI Assistant  
**Fecha**: 2026-04-24  
**Estado**: BLOQUEADA - Pendiente de decisión
