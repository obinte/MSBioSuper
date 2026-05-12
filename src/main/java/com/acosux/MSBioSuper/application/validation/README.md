# Validadores Personalizados

Este paquete contiene validadores personalizados para las reglas de negocio de MSBioSuper.

## Validadores Disponibles

### @ValidDateFormat

Valida que una fecha tenga un formato válido. Soporta múltiples formatos:
- `yyyy-MM-dd` (ISO 8601)
- `dd/MM/yyyy`
- `yyyy/MM/dd`

**Uso:**
```java
public record MiRequest(
    @ValidDateFormat
    String fecha
) {}
```

**Mensaje de error por defecto:**
"El formato de fecha no es válido. Formatos aceptados: yyyy-MM-dd, dd/MM/yyyy, yyyy/MM/dd"

---

### @ValidDateRange

Valida que un rango de fechas sea válido (fecha desde ≤ fecha hasta).
Esta anotación se aplica a nivel de clase.

**Uso:**
```java
@ValidDateRange(fechaDesde = "fechaDesde", fechaHasta = "fechaHasta")
public record MiRequest(
    @ValidDateFormat String fechaDesde,
    @ValidDateFormat String fechaHasta
) {}
```

**Mensaje de error por defecto:**
"El rango de fechas no es válido: la fecha desde debe ser anterior o igual a la fecha hasta"

---

### @ValidEmpresaCode

Valida que un código de empresa sea válido:
- Entre 2 y 10 caracteres
- Solo caracteres alfanuméricos (A-Z, a-z, 0-9)

**Uso:**
```java
public record MiRequest(
    @ValidEmpresaCode
    String empresa
) {}
```

**Mensaje de error por defecto:**
"El código de empresa no es válido. Debe tener entre 2 y 10 caracteres alfanuméricos"

---

## Ejemplos de Uso Completo

### Ejemplo 1: Request con validación de fechas

```java
@ValidDateRange(fechaDesde = "fechaDesde", fechaHasta = "fechaHasta")
public record ResumenPescaRequest(
    @NotBlank(message = "El código de empresa es obligatorio")
    @ValidEmpresaCode
    String empresa,
    
    @NotNull(message = "La fecha desde es obligatoria")
    @NotBlank(message = "La fecha desde no puede estar vacía")
    @ValidDateFormat
    String fechaDesde,
    
    @NotNull(message = "La fecha hasta es obligatoria")
    @NotBlank(message = "La fecha hasta no puede estar vacía")
    @ValidDateFormat
    String fechaHasta
) {}
```

### Ejemplo 2: Request con validación de empresa

```java
public record ProductoRequest(
    @NotBlank(message = "El código de empresa es obligatorio")
    @ValidEmpresaCode
    String empresa,
    
    @NotBlank(message = "El código de bodega es obligatorio")
    @Size(min = 1, max = 20)
    String bodega
) {}
```

---

## Mensajes de Error Personalizados

Todos los validadores permiten personalizar el mensaje de error:

```java
@ValidEmpresaCode(message = "Código de empresa inválido")
String empresa
```

---

## Notas Técnicas

1. **Valores nulos**: Los validadores personalizados permiten valores nulos. Use `@NotNull` o `@NotBlank` para validar que el campo no sea nulo.

2. **Orden de validación**: Las validaciones se ejecutan en el siguiente orden:
   - Validaciones de campo (`@NotNull`, `@NotBlank`, etc.)
   - Validaciones personalizadas de campo (`@ValidDateFormat`, `@ValidEmpresaCode`)
   - Validaciones de clase (`@ValidDateRange`)

3. **Formatos de fecha**: Los validadores de fecha intentan parsear con múltiples formatos en orden. El primer formato que funcione será aceptado.

4. **Rendimiento**: Los validadores están optimizados para minimizar el impacto en el rendimiento. Las expresiones regulares se compilan una sola vez.

---

## Extensión

Para crear un nuevo validador personalizado:

1. Crear la anotación:
```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MiValidator.class)
public @interface MiValidacion {
    String message() default "Mensaje por defecto";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

2. Crear el validador:
```java
public class MiValidator implements ConstraintValidator<MiValidacion, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Lógica de validación
        return true;
    }
}
```

3. Usar la anotación en los DTOs.
