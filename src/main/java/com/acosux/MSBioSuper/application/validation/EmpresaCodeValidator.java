package com.acosux.MSBioSuper.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador para verificar que un código de empresa es válido.
 * El código debe tener entre 2 y 10 caracteres alfanuméricos.
 */
public class EmpresaCodeValidator implements ConstraintValidator<ValidEmpresaCode, String> {
    
    private static final String EMPRESA_CODE_PATTERN = "^[A-Za-z0-9]{2,10}$";
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Dejar que @NotNull/@NotBlank manejen valores nulos/vacíos
        }
        
        return value.matches(EMPRESA_CODE_PATTERN);
    }
}
