package com.acosux.MSBioSuper.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Anotación de validación personalizada para verificar que una fecha tiene un formato válido.
 * Soporta formatos: yyyy-MM-dd, dd/MM/yyyy, yyyy/MM/dd
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateFormatValidator.class)
@Documented
public @interface ValidDateFormat {
    
    String message() default "El formato de fecha no es válido. Formatos aceptados: yyyy-MM-dd, dd/MM/yyyy, yyyy/MM/dd";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
