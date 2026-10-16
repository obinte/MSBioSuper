package com.acosux.MSBiosuper.config;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proveedor de credenciales desde AWS Secrets Manager.
 * <p>
 * Copia de MSVitapro (config/SecretsProvider.java, AWS SDK v1, US-190) sin cambios de
 * lógica. Único cambio: el package. Se usa java.util.logging para no depender de la
 * configuración de slf4j/logback.
 * <p>
 * Lee un secret de AWS y extrae valores individuales por key.
 * Usa las credenciales de la cadena por defecto del SDK (variables de entorno,
 * ~/.aws/credentials del usuario de Tomcat o rol de instancia).
 * <p>
 * Si AWS no está disponible, retorna null y el caller usa el fallback.
 *
 * Ticket: US-198 (patrón US-190).
 */
public class SecretsProvider {

    private static final Logger logger = Logger.getLogger(SecretsProvider.class.getName());

    private static final String REGION = "us-east-1";

    private String secretJson;
    private boolean loaded = false;

    /**
     * Carga un secret de AWS Secrets Manager.
     *
     * @param secretName nombre del secret (ej: "data.acosux.com/msbiosuper")
     */
    public SecretsProvider(String secretName) {
        try {
            AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                    .withRegion(REGION)
                    .build();

            GetSecretValueRequest request = new GetSecretValueRequest()
                    .withSecretId(secretName);

            GetSecretValueResult result = client.getSecretValue(request);
            this.secretJson = result.getSecretString();
            this.loaded = true;

            logger.log(Level.INFO, "[SecretsProvider] Secret ''{0}'' loaded successfully", secretName);
        } catch (Exception e) {
            logger.log(Level.WARNING, "[SecretsProvider] Could not load secret ''{0}'': {1}. Using fallback from properties file.",
                    new Object[]{secretName, e.getMessage()});
            this.loaded = false;
        }
    }

    /**
     * Obtiene un valor del secret por su key.
     *
     * @param key      la key dentro del JSON del secret
     * @param fallback valor a retornar si el secret no se cargó o la key no existe
     * @return el valor del secret, o el fallback si no está disponible
     */
    public String get(String key, String fallback) {
        if (!loaded || secretJson == null) {
            return fallback;
        }

        String value = extractJsonValue(secretJson, key);
        return value != null ? value : fallback;
    }

    /**
     * Indica si el secret se cargó correctamente desde AWS.
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Extrae un valor string de un JSON plano sin dependencias externas.
     * Soporta JSON del tipo: {"key1":"value1","key2":"value2"}
     */
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) {
            return null;
        }

        int colonIndex = json.indexOf(':', keyIndex + searchKey.length());
        if (colonIndex == -1) {
            return null;
        }

        int valueStart = json.indexOf('"', colonIndex + 1);
        if (valueStart == -1) {
            return null;
        }

        int valueEnd = json.indexOf('"', valueStart + 1);
        if (valueEnd == -1) {
            return null;
        }

        return json.substring(valueStart + 1, valueEnd);
    }
}
