# Reporte de Cobertura de Código - MSBioSuper Java 21 Migration

## Resumen Ejecutivo

**Fecha de Análisis**: 6 de mayo de 2026  
**Herramienta**: JaCoCo 0.8.12  
**Total de Clases Analizadas**: 63  
**Total de Métodos**: 1,073  
**Total de Líneas**: 2,936  

### Métricas Generales de Cobertura

| Métrica | Valor | Objetivo | Estado |
|---------|-------|----------|--------|
| **Cobertura de Instrucciones** | **24%** | 80% | ❌ **NO CUMPLE** |
| **Cobertura de Ramas** | **22%** | 80% | ❌ **NO CUMPLE** |
| **Cobertura de Líneas** | **25.8%** | 80% | ❌ **NO CUMPLE** |
| **Cobertura de Métodos** | **29.1%** | 80% | ❌ **NO CUMPLE** |
| **Cobertura de Clases** | **71.4%** | 80% | ❌ **NO CUMPLE** |

### Detalles de Cobertura

- **Instrucciones Cubiertas**: 2,180 de 8,963 (24%)
- **Ramas Cubiertas**: 130 de 581 (22%)
- **Líneas Cubiertas**: 758 de 2,936 (26%)
- **Métodos Cubiertos**: 312 de 1,073 (29%)
- **Clases Cubiertas**: 45 de 63 (71%)

## Análisis por Paquetes

### 🟢 Paquetes con Buena Cobertura (>80%)

#### 1. com.acosux.MSBioSuper.domain.exception
- **Cobertura de Instrucciones**: 100%
- **Cobertura de Líneas**: 100%
- **Clases**: 3/3 (100%)
- **Estado**: ✅ **EXCELENTE**

#### 2. com.acosux.MSBioSuper.infrastructure.security
- **Cobertura de Instrucciones**: 100%
- **Cobertura de Ramas**: 83%
- **Cobertura de Líneas**: 100%
- **Estado**: ✅ **EXCELENTE**

#### 3. com.acosux.MSBioSuper.domain.service
- **Cobertura de Instrucciones**: 99%
- **Cobertura de Ramas**: 86%
- **Cobertura de Líneas**: 99%
- **Estado**: ✅ **EXCELENTE**

### 🟡 Paquetes con Cobertura Moderada (50-80%)

#### 4. com.acosux.MSBioSuper.infrastructure.health
- **Cobertura de Instrucciones**: 65%
- **Cobertura de Ramas**: 28%
- **Cobertura de Líneas**: 59%
- **Estado**: ⚠️ **NECESITA MEJORA**

#### 5. com.acosux.MSBioSuper.infrastructure.metrics
- **Cobertura de Instrucciones**: 48%
- **Cobertura de Líneas**: 55%
- **Estado**: ⚠️ **NECESITA MEJORA**

### 🔴 Paquetes con Baja Cobertura (<50%)

#### 6. com.acosux.MSBioSuper.security
- **Cobertura de Instrucciones**: 28%
- **Cobertura de Ramas**: 37%
- **Cobertura de Líneas**: 24%
- **Estado**: ❌ **CRÍTICO**

#### 7. com.acosux.MSBioSuper.domain.model
- **Cobertura de Instrucciones**: 27%
- **Cobertura de Ramas**: 19%
- **Cobertura de Líneas**: 29%
- **Estado**: ❌ **CRÍTICO**

#### 8. com.acosux.MSBioSuper.application.usecase
- **Cobertura de Instrucciones**: 10%
- **Cobertura de Ramas**: 0%
- **Cobertura de Líneas**: 18%
- **Estado**: ❌ **CRÍTICO**

#### 9. com.acosux.MSBioSuper.infrastructure.rest
- **Cobertura de Instrucciones**: 10%
- **Cobertura de Ramas**: 0%
- **Cobertura de Líneas**: 13%
- **Estado**: ❌ **CRÍTICO**

#### 10. com.acosux.MSBioSuper.infrastructure.exception
- **Cobertura de Instrucciones**: 2%
- **Cobertura de Líneas**: 2%
- **Estado**: ❌ **CRÍTICO**

#### 11. com.acosux.MSBioSuper.application.mapper
- **Cobertura de Instrucciones**: 1%
- **Cobertura de Ramas**: 0%
- **Cobertura de Líneas**: 1%
- **Estado**: ❌ **CRÍTICO**

#### 12. com.acosux.MSBioSuper.util
- **Cobertura de Instrucciones**: 0%
- **Cobertura de Ramas**: 0%
- **Cobertura de Líneas**: 0%
- **Estado**: ❌ **CRÍTICO**

#### 13. com.acosux.MSBioSuper.application.validation
- **Cobertura de Instrucciones**: 0%
- **Cobertura de Ramas**: 0%
- **Cobertura de Líneas**: 0%
- **Estado**: ❌ **CRÍTICO**

## Clases con Mayor Impacto en la Cobertura

### Clases Completamente Sin Cobertura (0%)

1. **UtilsDate** (530 instrucciones sin cubrir)
2. **Encriptacion** (229 instrucciones sin cubrir)
3. **PrdLiquidacionDetalleProductoTO** (241 instrucciones sin cubrir)
4. **PrdListadoGrameajeTO** (231 instrucciones sin cubrir)
5. **InvComprasDetalleProductoTO** (227 instrucciones sin cubrir)
6. **ResumenPescaMapper** (214 instrucciones sin cubrir)
7. **ResumenSiembraMapper** (214 instrucciones sin cubrir)

### Clases con Cobertura Parcial Significativa

1. **InvVentasBioSuperTO**: 5% cobertura (556 instrucciones sin cubrir)
2. **MSBioSuperController**: 9% cobertura (382 instrucciones sin cubrir)
3. **PrdResumenCorridaTO**: 39% cobertura (301 instrucciones sin cubrir)
4. **InvVentasDetalleBioSuperTO**: 5% cobertura (290 instrucciones sin cubrir)

## Análisis de Fallos en Pruebas

### Pruebas Fallidas (17 fallos)

Todas las pruebas fallidas están relacionadas con **ActuatorEndpointsTest** debido a problemas de autenticación (HTTP 401):

1. `debeExponerEndpointHealthGeneral`
2. `debeExponerEndpointInfo`
3. `debeExponerEndpointLiveness`
4. `debeExponerEndpointMetricas`
5. `debeExponerEndpointPrometheus`
6. `debeExponerEndpointReadiness`
7. `debeExponerMetricaCPUSistema`
8. `debeExponerMetricaConexionesHikariCP`
9. `debeExponerMetricaMemoriaJVM`
10. `debeExponerMetricaThreadsJVM`
11. `debeIncluirComponentesDeHealth`
12. `debeIncluirMetricasHikariCPEnPrometheus`
13. `debeIncluirMetricasJVMEnPrometheus`
14. `debeIncluirMetricasSistemaEnPrometheus`
15. `debeIncluirTagsAplicacionEnMetricas`
16. `debePermitirCORSEnEndpointsActuator`
17. `noDebeExponerEndpointsNoConfigurados`

**Causa**: Los endpoints de Actuator están protegidos por seguridad y las pruebas no están configuradas correctamente para la autenticación.

## Recomendaciones Prioritarias

### 🚨 Acciones Inmediatas (Críticas)

1. **Escribir pruebas para casos de uso** (application.usecase)
   - Actualmente 0% de cobertura de ramas
   - Son el núcleo de la lógica de negocio
   - **Impacto**: Alto

2. **Implementar pruebas para mappers** (application.mapper)
   - Solo 1% de cobertura
   - Críticos para la transformación de datos
   - **Impacto**: Alto

3. **Crear pruebas para controladores REST** (infrastructure.rest)
   - Solo 10% de cobertura
   - Puntos de entrada de la API
   - **Impacto**: Alto

4. **Corregir pruebas de Actuator**
   - Configurar autenticación correcta en pruebas
   - **Impacto**: Medio

### 🔧 Acciones de Mejora (Importantes)

5. **Pruebas para clases de dominio** (domain.model)
   - 27% de cobertura actual
   - Muchas clases TO sin pruebas
   - **Impacto**: Medio

6. **Pruebas para utilidades** (util)
   - 0% de cobertura
   - Clases como UtilsDate y Encriptacion son críticas
   - **Impacto**: Medio

7. **Pruebas para validadores** (application.validation)
   - 0% de cobertura
   - Importantes para la validación de entrada
   - **Impacto**: Medio

8. **Mejorar pruebas de seguridad** (security)
   - 28% de cobertura actual
   - Componente crítico para la seguridad
   - **Impacto**: Alto

### 📊 Métricas de Seguimiento

Para alcanzar el objetivo del 80% de cobertura, se necesita:

- **Instrucciones adicionales a cubrir**: ~5,600 instrucciones
- **Ramas adicionales a cubrir**: ~350 ramas
- **Líneas adicionales a cubrir**: ~1,600 líneas

### 🎯 Plan de Acción Sugerido

#### Fase 1: Cobertura Básica (Objetivo: 40%)
1. Implementar pruebas básicas para todos los casos de uso
2. Crear pruebas para mappers principales
3. Corregir pruebas de Actuator

#### Fase 2: Cobertura Intermedia (Objetivo: 60%)
1. Pruebas completas para controladores REST
2. Pruebas para clases de dominio más utilizadas
3. Pruebas para utilidades críticas

#### Fase 3: Cobertura Objetivo (Objetivo: 80%)
1. Pruebas exhaustivas para todos los componentes
2. Pruebas de casos edge y manejo de errores
3. Optimización de cobertura de ramas

## Conclusiones

El proyecto **MSBioSuper** actualmente tiene una **cobertura de código del 24%**, lo cual está **significativamente por debajo del objetivo del 80%**. 

### Puntos Positivos
- ✅ Excelente cobertura en servicios de dominio (99%)
- ✅ Buena cobertura en excepciones de dominio (100%)
- ✅ Infraestructura de seguridad bien cubierta (100%)

### Áreas Críticas
- ❌ Casos de uso sin cobertura de ramas (0%)
- ❌ Mappers prácticamente sin pruebas (1%)
- ❌ Controladores REST con baja cobertura (10%)
- ❌ Utilidades completamente sin pruebas (0%)

### Recomendación Final

**Es necesario implementar un plan integral de pruebas** antes de considerar el proyecto listo para producción. La baja cobertura actual representa un **riesgo significativo** para la estabilidad y mantenibilidad del sistema.

**Prioridad**: 🔴 **ALTA** - Implementar pruebas para casos de uso y mappers como primera prioridad.

---

**Generado por**: Kiro - Agente de Ejecución de Tareas  
**Fecha**: 6 de mayo de 2026  
**Tarea**: 28.5 - Verificar cobertura de código  
**Requisito**: TR-19 (Testing Requirements - Unit Testing)