package com.acosux.MSBioSuper.infrastructure.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para DatabaseHealthIndicator.
 */
@ExtendWith(MockitoExtension.class)
class DatabaseHealthIndicatorTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private DatabaseHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        healthIndicator = new DatabaseHealthIndicator(dataSource);
    }

    @Test
    void health_WhenDatabaseIsUp_ShouldReturnUp() throws SQLException {
        // Given
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT 1")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        // When
        Health health = healthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("database", "PostgreSQL");
        assertThat(health.getDetails()).containsEntry("status", "Conectado");
        assertThat(health.getDetails()).containsKey("responseTime");
        assertThat(health.getDetails()).containsEntry("validationQuery", "SELECT 1");

        verify(dataSource).getConnection();
        verify(connection).createStatement();
        verify(statement).executeQuery("SELECT 1");
        verify(resultSet).next();
    }

    @Test
    void health_WhenQueryReturnsNoResults_ShouldReturnDown() throws SQLException {
        // Given
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT 1")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // When
        Health health = healthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("database", "PostgreSQL");
        assertThat(health.getDetails()).containsEntry("status", "Error en consulta de validación");
        assertThat(health.getDetails()).containsEntry("reason", "La consulta no retornó resultados");
    }

    @Test
    void health_WhenConnectionFails_ShouldReturnDown() throws SQLException {
        // Given
        SQLException exception = new SQLException("Connection refused");
        when(dataSource.getConnection()).thenThrow(exception);

        // When
        Health health = healthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("database", "PostgreSQL");
        assertThat(health.getDetails()).containsEntry("status", "Error de conexión");
        assertThat(health.getDetails()).containsEntry("error", "SQLException");
        assertThat(health.getDetails()).containsEntry("message", "Connection refused");
    }

    @Test
    void health_WhenStatementFails_ShouldReturnDown() throws SQLException {
        // Given
        SQLException exception = new SQLException("Query execution failed");
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT 1")).thenThrow(exception);

        // When
        Health health = healthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("database", "PostgreSQL");
        assertThat(health.getDetails()).containsEntry("status", "Error de conexión");
        assertThat(health.getDetails()).containsEntry("error", "SQLException");
        assertThat(health.getDetails()).containsEntry("message", "Query execution failed");
    }
}
