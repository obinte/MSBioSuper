package com.acosux.MSBioSuper.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Anotación de validación personalizada para verificar que un código de empresa es válido.
 * El código debe tener entre 2 y 10 caracteres alfanuméricos.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmpresaCodeValidator.class)
@Documented
public @interface ValidEmpresaCode {
    
    String message() default "El código de empresa no es válido. Debe tener entre 2 y 10 caracteres alfanuméricos";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
