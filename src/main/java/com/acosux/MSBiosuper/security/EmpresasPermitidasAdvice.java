package com.acosux.MSBiosuper.security;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

/**
 * Limita, por despliegue, las empresas que se pueden consultar.
 * <p>
 * El token no está atado a ningún contexto ni empresa, y todos los endpoints reciben la
 * empresa en el campo "empresa" del body. Si empresas.permitidas está definido en el
 * application.properties del despliegue (lista separada por comas, p. ej. SA01,SA05), una
 * petición con otra empresa responde 403 antes de llegar al controlador. Sin la propiedad
 * no se restringe nada (MSBioSuper y MSAbricmar siguen igual).
 */
@ControllerAdvice
public class EmpresasPermitidasAdvice extends RequestBodyAdviceAdapter {

    private static final Logger LOG = Logger.getLogger(EmpresasPermitidasAdvice.class.getName());

    private final Set<String> empresasPermitidas;

    public EmpresasPermitidasAdvice(Environment env) {
        String valor = env.getProperty("empresas.permitidas", "").trim();
        Set<String> empresas = new LinkedHashSet<>();
        for (String empresa : Arrays.asList(valor.split(","))) {
            if (!empresa.trim().isEmpty()) {
                empresas.add(empresa.trim());
            }
        }
        this.empresasPermitidas = Collections.unmodifiableSet(empresas);
        LOG.log(Level.INFO, "[EmpresasPermitidas] {0}",
                empresasPermitidas.isEmpty() ? "sin restricción (empresas.permitidas no definido)" : empresasPermitidas);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !empresasPermitidas.isEmpty();
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        // Comparación exacta: solo pasa un String idéntico a un código de la lista, que es
        // el mismo valor que llega a la consulta.
        Object empresa = body instanceof Map ? ((Map<?, ?>) body).get("empresa") : null;
        if (!(empresa instanceof String) || !empresasPermitidas.contains(empresa)) {
            LOG.log(Level.WARNING, "[EmpresasPermitidas] 403 {0}: empresa no permitida ({1})",
                    new Object[]{parameter.getExecutable().getName(), empresa});
            throw new EmpresaNoPermitidaException();
        }
        return body;
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Empresa no permitida")
    public static class EmpresaNoPermitidaException extends RuntimeException {

        private static final long serialVersionUID = 1L;
    }
}
