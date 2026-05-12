/*
 * Manejador global de excepciones para la aplicación MSBioSuper.
 * Captura y procesa excepciones de validación, negocio y técnicas.
 */
package com.acosux.MSBioSuper.infrastructure.exception;

import com.acosux.MSBioSuper.domain.exception.BusinessException;
import com.acosux.MSBioSuper.domain.exception.ResourceNotFoundException;
import com.acosux.MSBioSuper.domain.exception.ValidationException;
import com.acosux.MSBioSuper.util.RespuestaWebTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para todos los controladores REST.
 * Proporciona respuestas consistentes y estandarizadas para diferentes tipos de errores.
 * 
 * Maneja:
 * - Errores de validación de entrada (@Valid)
 * - Errores de validación de constraints
 * - Errores de argumentos ilegales
 * - Errores generales de la aplicación
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0 - Java 21 Migration
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja excepciones de validación de argumentos de métodos.
     * Se dispara cuando @Valid falla en un @RequestBody.
     * 
     * @param ex Excepción de validación
     * @param request Contexto de la solicitud web
     * @return Respuesta con detalles de los errores de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RespuestaWebTO> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("; "));

        logger.warn("Error de validación: {}", errorMessage);

        RespuestaWebTO response = new RespuestaWebTO();
        response.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
        response.setOperacionMensaje("Error de validación: " + errorMessage);
        response.setExtraInfo(errors);

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones de violación de constraints.
     * Se dispara cuando las validaciones de Jakarta Validation fallan.
     * 
     * @param ex Excepción de violación de constraints
     * @param request Contexto de la solicitud web
     * @return Respuesta con detalles de las violaciones
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RespuestaWebTO> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        }

        String errorMessage = errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("; "));

        logger.warn("Error de constraint: {}", errorMessage);

        RespuestaWebTO response = new RespuestaWebTO();
        response.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
        response.setOperacionMensaje("Error de validación: " + errorMessage);
        response.setExtraInfo(errors);

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones de argumentos ilegales.
     * Se dispara cuando se pasan argumentos inválidos a métodos.
     * 
     * @param ex Excepción de argumento ilegal
     * @param request Contexto de la solicitud web
     * @return Respuesta con mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RespuestaWebTO> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {
        
        logger.warn("Argumento ilegal: {}", ex.getMessage());

        RespuestaWebTO response = new RespuestaWebTO();
        response.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
        response.setOperacionMensaje("Error de validación: " + ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones generales no capturadas por otros manejadores.
     * Proporciona una respuesta genérica sin exponer detalles internos.
     * 
     * NOTA: No captura NoResourceFoundException para permitir que SpringDoc
     * maneje correctamente los endpoints de documentación (/v3/api-docs, /swagger-ui).
     * 
     * @param ex Excepción general
     * @param request Contexto de la solicitud web
     * @return Respuesta con mensaje de error genérico
     * @throws org.springframework.web.servlet.resource.NoResourceFoundException si es una excepción de recurso no encontrado
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<RespuestaWebTO> handleGlobalException(
            Exception ex,
            WebRequest request) throws org.springframework.web.servlet.resource.NoResourceFoundException {
        
        // No capturar NoResourceFoundException para permitir que SpringDoc funcione
        if (ex instanceof org.springframework.web.servlet.resource.NoResourceFoundException) {
            // Re-lanzar la excepción para que Spring la maneje correctamente
            throw (org.springframework.web.servlet.resource.NoResourceFoundException) ex;
        }
        
        logger.error("Error interno del servidor: {}", ex.getMessage(), ex);

        RespuestaWebTO response = new RespuestaWebTO();
        response.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
        response.setOperacionMensaje("Error interno del servidor. Por favor, contacte al administrador.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Maneja excepciones de estado ilegal.
     * Se dispara cuando el estado de un objeto no es válido para una operación.
     * 
     * @param ex Excepción de estado ilegal
     * @param request Contexto de la solicitud web
     * @return Respuesta con mensaje de error
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<RespuestaWebTO> handleIllegalStateException(
            IllegalStateException ex,
            WebRequest request) {
        
        logger.warn("Estado ilegal: {}", ex.getMessage());

        RespuestaWebTO response = new RespuestaWebTO();
        response.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
        response.setOperacionMensaje("Error de estado: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Maneja excepciones de puntero nulo.
     * Se dispara cuando se intenta acceder a un objeto nulo.
     * 
     * @param ex Excepción de puntero nulo
     * @param request Contexto de la solicitud web
     * @return Respuesta con mensaje de error
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<RespuestaWebTO> handleNullPointerException(
            NullPointerException ex,
            WebRequest request) {
        
        logger.error("Error de puntero nulo: {}", ex.getMessage(), ex);

        RespuestaWebTO response = new RespuestaWebTO();
        response.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
        response.setOperacionMensaje("Error interno: referencia nula detectada.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Maneja excepciones de recurso no encontrado.
     * Se dispara cuando un recurso solicitado no existe.
     * 
     * @param ex Excepción de recurso no encontrado
     * @param request Contexto de la solicitud web
     * @return Respuesta con mensaje de error
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RespuestaWebTO> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {
        
        logger.warn("Recurso no encontrado: {}", ex.getMessage());

        RespuestaWebTO response = new RespuestaWebTO();
        response.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
        response.setOperacionMensaje(ex.getMessage());
        
        Map<String, String> extraInfo = new HashMap<>();
        extraInfo.put("errorCode", ex.getErrorCode());
        response.setExtraInfo(extraInfo);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Maneja excepciones de validación de negocio.
     * Se dispara cuando una regla de negocio no se cumple.
     * 
     * @param ex Excepción de validación de negocio
     * @param request Contexto de la solicitud web
     * @return Respuesta con mensaje de error
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RespuestaWebTO> handleValidationException(
            ValidationException ex,
            WebRequest request) {
        
        logger.warn("Error de validación de negocio: {}", ex.getMessage());

        RespuestaWebTO response = new RespuestaWebTO();
        response.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
        response.setOperacionMensaje(ex.getMessage());
        
        Map<String, String> extraInfo = new HashMap<>();
        extraInfo.put("errorCode", ex.getErrorCode());
        response.setExtraInfo(extraInfo);

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja excepciones de negocio genéricas.
     * Se dispara cuando ocurre un error de lógica de negocio.
     * 
     * @param ex Excepción de negocio
     * @param request Contexto de la solicitud web
     * @return Respuesta con mensaje de error
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<RespuestaWebTO> handleBusinessException(
            BusinessException ex,
            WebRequest request) {
        
        logger.warn("Error de negocio: {}", ex.getMessage());

        RespuestaWebTO response = new RespuestaWebTO();
        response.setEstadoOperacion(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor());
        response.setOperacionMensaje(ex.getMessage());
        
        Map<String, String> extraInfo = new HashMap<>();
        extraInfo.put("errorCode", ex.getErrorCode());
        response.setExtraInfo(extraInfo);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
}
