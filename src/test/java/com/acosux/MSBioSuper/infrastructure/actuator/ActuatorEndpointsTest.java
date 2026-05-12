package com.acosux.MSBioSuper.infrastructure.actuator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para endpoints de Spring Boot Actuator.
 * 
 * Verifica que los endpoints de observabilidad estén configurados correctamente:
 * - Health checks (general, liveness, readiness)
 * - Métricas (general y Prometheus)
 * - Información de la aplicación
 * 
 * Estas pruebas son críticas para el Punto de Control 31.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Pruebas de Endpoints de Actuator")
class ActuatorEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    @EnableWebSecurity
    static class TestSecurityConfig {
        
        @Bean
        @Primary
        public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                    .anyRequest().permitAll()
                );
            return http.build();
        }
    }

    // ========================================
    // PRUEBAS DE HEALTH CHECKS
    // ========================================

    @Test
    @WithMockUser
    @DisplayName("Debe exponer endpoint de health check general")
    void debeExponerEndpointHealthGeneral() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(content().contentType("application/vnd.spring-boot.actuator.v3+json"));
    }

    @Test
    @WithMockUser
    @DisplayName("Debe exponer endpoint de liveness probe")
    void debeExponerEndpointLiveness() throws Exception {
        mockMvc.perform(get("/actuator/health/liveness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @WithMockUser
    @DisplayName("Debe exponer endpoint de readiness probe")
    void debeExponerEndpointReadiness() throws Exception {
        mockMvc.perform(get("/actuator/health/readiness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    @DisplayName("Debe incluir componentes de health en respuesta detallada")
    void debeIncluirComponentesDeHealth() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components").exists())
                .andExpect(jsonPath("$.components.db").exists())
                .andExpect(jsonPath("$.components.diskSpace").exists());
    }

    // ========================================
    // PRUEBAS DE MÉTRICAS
    // ========================================

    @Test
    @DisplayName("Debe exponer endpoint de métricas generales")
    void debeExponerEndpointMetricas() throws Exception {
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names").isArray())
                .andExpect(content().contentType("application/vnd.spring-boot.actuator.v3+json"));
    }

    @Test
    @DisplayName("Debe exponer endpoint de métricas Prometheus")
    void debeExponerEndpointPrometheus() throws Exception {
        mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/plain"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("# HELP")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("# TYPE")));
    }

    @Test
    @DisplayName("Debe incluir métricas JVM en Prometheus")
    void debeIncluirMetricasJVMEnPrometheus() throws Exception {
        mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("jvm_memory")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("jvm_threads")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("jvm_gc")));
    }

    @Test
    @DisplayName("Debe incluir métricas de sistema en Prometheus")
    void debeIncluirMetricasSistemaEnPrometheus() throws Exception {
        mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("system_cpu")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("process_uptime")));
    }

    @Test
    @DisplayName("Debe incluir métricas de HikariCP en Prometheus")
    void debeIncluirMetricasHikariCPEnPrometheus() throws Exception {
        mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("hikaricp")));
    }

    @Test
    @DisplayName("Debe incluir tags de aplicación en métricas")
    void debeIncluirTagsAplicacionEnMetricas() throws Exception {
        mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("application=\"msbiosuper\"")));
    }

    // ========================================
    // PRUEBAS DE MÉTRICAS ESPECÍFICAS
    // ========================================

    @Test
    @DisplayName("Debe exponer métrica de memoria JVM")
    void debeExponerMetricaMemoriaJVM() throws Exception {
        mockMvc.perform(get("/actuator/metrics/jvm.memory.used"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("jvm.memory.used"))
                .andExpect(jsonPath("$.measurements").isArray())
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @Test
    @DisplayName("Debe exponer métrica de threads JVM")
    void debeExponerMetricaThreadsJVM() throws Exception {
        mockMvc.perform(get("/actuator/metrics/jvm.threads.live"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("jvm.threads.live"))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @Test
    @DisplayName("Debe exponer métrica de CPU del sistema")
    void debeExponerMetricaCPUSistema() throws Exception {
        mockMvc.perform(get("/actuator/metrics/system.cpu.usage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("system.cpu.usage"))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    @Test
    @DisplayName("Debe exponer métrica de conexiones HikariCP")
    void debeExponerMetricaConexionesHikariCP() throws Exception {
        mockMvc.perform(get("/actuator/metrics/hikaricp.connections.active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("hikaricp.connections.active"))
                .andExpect(jsonPath("$.measurements[0].value").isNumber());
    }

    // ========================================
    // PRUEBAS DE INFORMACIÓN
    // ========================================

    @Test
    @DisplayName("Debe exponer endpoint de información de la aplicación")
    void debeExponerEndpointInfo() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.spring-boot.actuator.v3+json"));
    }

    // ========================================
    // PRUEBAS DE SEGURIDAD DE ENDPOINTS
    // ========================================

    @Test
    @DisplayName("No debe exponer endpoints no configurados")
    void noDebeExponerEndpointsNoConfigurados() throws Exception {
        // Verificar que endpoints no incluidos en management.endpoints.web.exposure.include
        // no estén disponibles
        mockMvc.perform(get("/actuator/beans"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe permitir CORS en endpoints de actuator")
    void debePermitirCORSEnEndpointsActuator() throws Exception {
        mockMvc.perform(get("/actuator/health")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
}
