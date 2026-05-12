package com.acosux.MSBioSuper.infrastructure.exception;

import com.acosux.MSBioSuper.domain.exception.BusinessException;
import com.acosux.MSBioSuper.domain.exception.ResourceNotFoundException;
import com.acosux.MSBioSuper.domain.exception.ValidationException;
import com.acosux.MSBioSuper.util.RespuestaWebTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para GlobalExceptionHandler
 * 
 * Cobertura:
 * - Manejo de MethodArgumentNotValidException
 * - Manejo de ConstraintViolationException
 * - Manejo de IllegalArgumentException
 * - Manejo de ResourceNotFoundException
 * - Manejo de ValidationException
 * - Manejo de BusinessException
 * - Manejo de excepciones generales
 * - Códigos de estado HTTP correctos
 * - Formato de respuesta estandarizado
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler - Pruebas de Manejo de Excepciones")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private BindingResult bindingResult;

    @Nested
    @DisplayName("Manejo de Errores de Validación")
    class ValidationErrorHandling {

        @Test
        @DisplayName("Debe manejar MethodArgumentNotValidException correctamente")
        void debeManejarMethodArgumentNotValidException() {
            // Given
            FieldError fieldError1 = new FieldError("object", "campo1", "Campo1 es requerido");
            FieldError fieldError2 = new FieldError("object", "campo2", "Campo2 debe ser válido");
            
            when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));
            
            MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                null, bindingResult);

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleValidationExceptions(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
            assertTrue(body.getOperacionMensaje().contains("Error de validación"));
            assertTrue(body.getOperacionMensaje().contains("campo1"));
            assertTrue(body.getOperacionMensaje().contains("campo2"));
            
            @SuppressWarnings("unchecked")
            Map<String, String> errors = (Map<String, String>) body.getExtraInfo();
            assertNotNull(errors);
            assertEquals(2, errors.size());
            assertEquals("Campo1 es requerido", errors.get("campo1"));
            assertEquals("Campo2 debe ser válido", errors.get("campo2"));
        }

        @Test
        @DisplayName("Debe manejar ConstraintViolationException correctamente")
        void debeManejarConstraintViolationException() {
            // Given
            Set<ConstraintViolation<?>> violations = new HashSet<>();
            ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
            ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);
            
            when(violation1.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
            when(violation1.getPropertyPath().toString()).thenReturn("empresa");
            when(violation1.getMessage()).thenReturn("Empresa es requerida");
            
            when(violation2.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
            when(violation2.getPropertyPath().toString()).thenReturn("fechaInicio");
            when(violation2.getMessage()).thenReturn("Fecha inicio debe ser válida");
            
            violations.add(violation1);
            violations.add(violation2);
            
            ConstraintViolationException exception = new ConstraintViolationException(violations);

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleConstraintViolationException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
            assertTrue(body.getOperacionMensaje().contains("Error de validación"));
        }

        @Test
        @DisplayName("Debe manejar IllegalArgumentException correctamente")
        void debeManejarIllegalArgumentException() {
            // Given
            IllegalArgumentException exception = new IllegalArgumentException(
                "Parámetro inválido: empresa no puede estar vacía");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleIllegalArgumentException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
            assertTrue(body.getOperacionMensaje().contains("Error de validación"));
            assertTrue(body.getOperacionMensaje().contains("empresa no puede estar vacía"));
        }
    }

    @Nested
    @DisplayName("Manejo de Excepciones de Negocio")
    class BusinessExceptionHandling {

        @Test
        @DisplayName("Debe manejar ResourceNotFoundException correctamente")
        void debeManejarResourceNotFoundException() {
            // Given
            ResourceNotFoundException exception = new ResourceNotFoundException(
                "Recurso no encontrado", "RESOURCE_NOT_FOUND");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleResourceNotFoundException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
            assertEquals("Recurso no encontrado", body.getOperacionMensaje());
            
            @SuppressWarnings("unchecked")
            Map<String, String> extraInfo = (Map<String, String>) body.getExtraInfo();
            assertNotNull(extraInfo);
            assertEquals("RESOURCE_NOT_FOUND", extraInfo.get("errorCode"));
        }

        @Test
        @DisplayName("Debe manejar ValidationException correctamente")
        void debeManejarValidationException() {
            // Given
            ValidationException exception = new ValidationException(
                "Validación de negocio fallida", "VALIDATION_ERROR");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleValidationException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
            assertEquals("Validación de negocio fallida", body.getOperacionMensaje());
            
            @SuppressWarnings("unchecked")
            Map<String, String> extraInfo = (Map<String, String>) body.getExtraInfo();
            assertNotNull(extraInfo);
            assertEquals("VALIDATION_ERROR", extraInfo.get("errorCode"));
        }

        @Test
        @DisplayName("Debe manejar BusinessException correctamente")
        void debeManejarBusinessException() {
            // Given
            BusinessException exception = new BusinessException(
                "Regla de negocio violada", "BUSINESS_RULE_VIOLATION");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleBusinessException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
            
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
            assertEquals("Regla de negocio violada", body.getOperacionMensaje());
            
            @SuppressWarnings("unchecked")
            Map<String, String> extraInfo = (Map<String, String>) body.getExtraInfo();
            assertNotNull(extraInfo);
            assertEquals("BUSINESS_RULE_VIOLATION", extraInfo.get("errorCode"));
        }
    }

    @Nested
    @DisplayName("Manejo de Excepciones de Estado")
    class StateExceptionHandling {

        @Test
        @DisplayName("Debe manejar IllegalStateException correctamente")
        void debeManejarIllegalStateException() {
            // Given
            IllegalStateException exception = new IllegalStateException(
                "Estado inválido: operación no permitida");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleIllegalStateException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
            assertTrue(body.getOperacionMensaje().contains("Error de estado"));
            assertTrue(body.getOperacionMensaje().contains("operación no permitida"));
        }

        @Test
        @DisplayName("Debe manejar NullPointerException correctamente")
        void debeManejarNullPointerException() {
            // Given
            NullPointerException exception = new NullPointerException("Referencia nula detectada");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleNullPointerException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
            assertEquals("Error interno: referencia nula detectada.", body.getOperacionMensaje());
        }
    }

    @Nested
    @DisplayName("Manejo de Excepciones Generales")
    class GeneralExceptionHandling {

        @Test
        @DisplayName("Debe manejar Exception genérica correctamente")
        void debeManejarExceptionGenerica() {
            // Given
            Exception exception = new Exception("Error inesperado del sistema");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleGlobalException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
            assertEquals("Error interno del servidor. Por favor, contacte al administrador.", 
                body.getOperacionMensaje());
        }

        @Test
        @DisplayName("Debe ocultar detalles técnicos en excepciones generales")
        void debeOcultarDetallesTecnicos() {
            // Given
            Exception exception = new Exception("Detalles técnicos sensibles: conexión DB fallida");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleGlobalException(
                exception, webRequest);

            // Then
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertFalse(body.getOperacionMensaje().contains("Detalles técnicos sensibles"));
            assertFalse(body.getOperacionMensaje().contains("conexión DB fallida"));
            assertEquals("Error interno del servidor. Por favor, contacte al administrador.", 
                body.getOperacionMensaje());
        }

        @Test
        @DisplayName("Debe manejar RuntimeException correctamente")
        void debeManejarRuntimeException() {
            // Given
            RuntimeException exception = new RuntimeException("Error de runtime");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleGlobalException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
        }
    }

    @Nested
    @DisplayName("Formato de Respuesta")
    class ResponseFormat {

        @Test
        @DisplayName("Debe retornar formato de respuesta consistente para validación")
        void debeRetornarFormatoConsistenteParaValidacion() {
            // Given
            IllegalArgumentException exception = new IllegalArgumentException("Error de validación");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleIllegalArgumentException(
                exception, webRequest);

            // Then
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertNotNull(body.getEstadoOperacion());
            assertNotNull(body.getOperacionMensaje());
            assertEquals(RespuestaWebTO.EstadoOperacionEnum.ERROR.getValor(), 
                body.getEstadoOperacion());
        }

        @Test
        @DisplayName("Debe incluir información adicional en excepciones de negocio")
        void debeIncluirInformacionAdicionalEnExcepcionesNegocio() {
            // Given
            BusinessException exception = new BusinessException(
                "Error de negocio", "BUSINESS_ERROR_001");

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleBusinessException(
                exception, webRequest);

            // Then
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            assertNotNull(body.getExtraInfo());
            
            @SuppressWarnings("unchecked")
            Map<String, String> extraInfo = (Map<String, String>) body.getExtraInfo();
            assertTrue(extraInfo.containsKey("errorCode"));
            assertEquals("BUSINESS_ERROR_001", extraInfo.get("errorCode"));
        }

        @Test
        @DisplayName("Debe retornar códigos de estado HTTP apropiados")
        void debeRetornarCodigosEstadoHTTPApropiados() {
            // Given & When & Then
            assertEquals(HttpStatus.BAD_REQUEST, 
                exceptionHandler.handleIllegalArgumentException(
                    new IllegalArgumentException("test"), webRequest).getStatusCode());
            
            assertEquals(HttpStatus.NOT_FOUND, 
                exceptionHandler.handleResourceNotFoundException(
                    new ResourceNotFoundException("test", "CODE"), webRequest).getStatusCode());
            
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, 
                exceptionHandler.handleBusinessException(
                    new BusinessException("test", "CODE"), webRequest).getStatusCode());
            
            assertEquals(HttpStatus.CONFLICT, 
                exceptionHandler.handleIllegalStateException(
                    new IllegalStateException("test"), webRequest).getStatusCode());
            
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, 
                exceptionHandler.handleGlobalException(
                    new Exception("test"), webRequest).getStatusCode());
        }
    }

    @Nested
    @DisplayName("Casos Edge")
    class EdgeCases {

        @Test
        @DisplayName("Debe manejar excepción con mensaje nulo")
        void debeManejarExcepcionConMensajeNulo() {
            // Given
            Exception exception = new Exception((String) null);

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleGlobalException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Debe manejar múltiples errores de validación")
        void debeManejarMultiplesErroresValidacion() {
            // Given
            FieldError error1 = new FieldError("obj", "campo1", "Error 1");
            FieldError error2 = new FieldError("obj", "campo2", "Error 2");
            FieldError error3 = new FieldError("obj", "campo3", "Error 3");
            
            when(bindingResult.getAllErrors()).thenReturn(List.of(error1, error2, error3));
            
            MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                null, bindingResult);

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleValidationExceptions(
                exception, webRequest);

            // Then
            RespuestaWebTO body = response.getBody();
            assertNotNull(body);
            
            @SuppressWarnings("unchecked")
            Map<String, String> errors = (Map<String, String>) body.getExtraInfo();
            assertEquals(3, errors.size());
        }

        @Test
        @DisplayName("Debe manejar excepción con mensaje muy largo")
        void debeManejarExcepcionConMensajeMuyLargo() {
            // Given
            String longMessage = "Error ".repeat(100);
            IllegalArgumentException exception = new IllegalArgumentException(longMessage);

            // When
            ResponseEntity<RespuestaWebTO> response = exceptionHandler.handleIllegalArgumentException(
                exception, webRequest);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().getOperacionMensaje().contains("Error"));
        }
    }
}
