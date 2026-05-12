package com.acosux.MSBioSuper.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validador para verificar que una fecha tiene un formato válido.
 * Soporta múltiples formatos comunes de fecha.
 */
public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {
    
    private static final DateTimeFormatter[] FORMATTERS = {
        DateTimeFormatter.ISO_LOCAL_DATE,           // yyyy-MM-dd
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),  // dd/MM/yyyy
        DateTimeFormatter.ofPattern("yyyy/MM/dd")   // yyyy/MM/dd
    };
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Dejar que @NotNull/@NotBlank manejen valores nulos/vacíos
        }
        
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                LocalDate.parse(value, formatter);
                return true; // Formato válido encontrado
            } catch (DateTimeParseException e) {
                // Intentar con el siguiente formato
            }
        }
        
        return false; // Ningún formato válido
    }
}
