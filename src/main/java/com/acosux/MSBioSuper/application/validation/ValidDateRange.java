package com.acosux.MSBioSuper.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Anotación de validación personalizada para verificar que un rango de fechas es válido.
 * Valida que la fecha desde sea anterior o igual a la fecha hasta.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
@Documented
public @interface ValidDateRange {
    
    String message() default "El rango de fechas no es válido: la fecha desde debe ser anterior o igual a la fecha hasta";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Campo que contiene la fecha desde
     */
    String fechaDesde();
    
    /**
     * Campo que contiene la fecha hasta
     */
    String fechaHasta();
}
