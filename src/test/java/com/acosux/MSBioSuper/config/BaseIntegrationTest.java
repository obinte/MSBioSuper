package com.acosux.MSBioSuper.config;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Clase base para pruebas de integración con Testcontainers.
 * 
 * Esta clase configura un contenedor PostgreSQL para pruebas de integración,
 * proporcionando un entorno de base de datos real y aislado para cada ejecución de pruebas.
 * 
 * Características:
 * - Contenedor PostgreSQL compartido entre todas las pruebas (singleton)
 * - Configuración automática de propiedades de Spring Boot
 * - Rollback automático de transacciones después de cada prueba
 * - Aislamiento completo de datos entre ejecuciones
 * 
 * Uso:
 * Las clases de prueba de integración deben extender esta clase base:
 * 
 * <pre>
 * {@code
 * @Transactional
 * class MyRepositoryIntegrationTest extends BaseIntegrationTest {
 *     
 *     @Autowired
 *     private MyRepository repository;
 *     
 *     @Test
 *     void testRepositoryOperation() {
 *         // Prueba con base de datos real
 *     }
 * }
 * }
 * </pre>
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-24
 */
@SpringBootTest
@Testcontainers
public abstract class BaseIntegrationTest {

    /**
     * Contenedor PostgreSQL compartido para todas las pruebas.
     * 
     * Configuración:
     * - Imagen: postgres:16-alpine (versión ligera y moderna)
     * - Base de datos: testdb
     * - Usuario: testuser
     * - Contraseña: testpass
     * - Reutilizable: true (se mantiene entre pruebas para mejor rendimiento)
     */
    @Container
    protected static final PostgreSQLContainer<?> postgresContainer = 
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withReuse(true);

    /**
     * Inicializa el contenedor PostgreSQL antes de todas las pruebas.
     * 
     * Este método se ejecuta una sola vez antes de todas las pruebas,
     * iniciando el contenedor Docker con PostgreSQL.
     */
    @BeforeAll
    static void beforeAll() {
        postgresContainer.start();
    }

    /**
     * Configura dinámicamente las propiedades de Spring Boot para usar el contenedor PostgreSQL.
     * 
     * Este método inyecta las propiedades de conexión del contenedor en el contexto de Spring,
     * sobrescribiendo las propiedades definidas en application.properties o application-test.properties.
     * 
     * Propiedades configuradas:
     * - spring.datasource.url: URL JDBC del contenedor
     * - spring.datasource.username: Usuario del contenedor
     * - spring.datasource.password: Contraseña del contenedor
     * - spring.datasource.driver-class-name: Driver PostgreSQL
     * - spring.jpa.hibernate.ddl-auto: create-drop (recrea esquema en cada ejecución)
     * 
     * @param registry Registro de propiedades dinámicas de Spring
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        
        // Configuración JPA para pruebas
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
        
        // Configuración de HikariCP para pruebas (pool pequeño)
        registry.add("spring.datasource.hikari.maximum-pool-size", () -> "5");
        registry.add("spring.datasource.hikari.minimum-idle", () -> "2");
    }
}
