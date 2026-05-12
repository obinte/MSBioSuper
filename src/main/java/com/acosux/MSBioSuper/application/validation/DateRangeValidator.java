package com.acosux.MSBioSuper.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validador para verificar que un rango de fechas es válido.
 * La fecha desde debe ser anterior o igual a la fecha hasta.
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {
    
    private String fechaDesdeField;
    private String fechaHastaField;
    
    private static final DateTimeFormatter[] FORMATTERS = {
        DateTimeFormatter.ISO_LOCAL_DATE,           // yyyy-MM-dd
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),  // dd/MM/yyyy
        DateTimeFormatter.ofPattern("yyyy/MM/dd")   // yyyy/MM/dd
    };
    
    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.fechaDesdeField = constraintAnnotation.fechaDesde();
        this.fechaHastaField = constraintAnnotation.fechaHasta();
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Dejar que @NotNull maneje valores nulos
        }
        
        BeanWrapper wrapper = new BeanWrapperImpl(value);
        Object fechaDesdeValue = wrapper.getPropertyValue(fechaDesdeField);
        Object fechaHastaValue = wrapper.getPropertyValue(fechaHastaField);
        
        // Si alguna fecha es nula, dejar que @NotNull lo maneje
        if (fechaDesdeValue == null || fechaHastaValue == null) {
            return true;
        }
        
        try {
            LocalDate fechaDesde = parseDate(fechaDesdeValue.toString());
            LocalDate fechaHasta = parseDate(fechaHastaValue.toString());
            
            if (fechaDesde == null || fechaHasta == null) {
                return false;
            }
            
            return !fechaDesde.isAfter(fechaHasta);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Intenta parsear una fecha usando múltiples formatos
     */
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                // Intentar con el siguiente formato
            }
        }
        
        return null;
    }
}
