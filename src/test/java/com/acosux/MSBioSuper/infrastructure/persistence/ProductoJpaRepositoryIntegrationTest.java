package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.config.BaseIntegrationTest;
import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para ProductoJpaRepository.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-24
 */
@Transactional
@DisplayName("Pruebas de Integración - ProductoJpaRepository")
class ProductoJpaRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProductoJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        // Crear datos de prueba
        InvListaProductosGeneralTO producto1 = createProducto(1, "PROD-001", "Balanceado Premium", "ALIMENTO", "BALANCEADO", true);
        InvListaProductosGeneralTO producto2 = createProducto(2, "PROD-002", "Larva PL10", "LARVA", "POSTLARVA", true);
        InvListaProductosGeneralTO producto3 = createProducto(3, "PROD-003", "Balanceado Estándar", "ALIMENTO", "BALANCEADO", false);

        repository.save(producto1);
        repository.save(producto2);
        repository.save(producto3);
    }

    @Test
    @DisplayName("Debe buscar producto por código principal")
    void testFindByCodigoPrincipal() {
        // When
        Optional<InvListaProductosGeneralTO> resultado = repository.findByCodigoPrincipal("PROD-001");

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getProNombre()).isEqualTo("Balanceado Premium");
    }

    @Test
    @DisplayName("Debe buscar productos por nombre conteniendo texto")
    void testFindByNombreContaining() {
        // When
        List<InvListaProductosGeneralTO> resultados = repository.findByNombreContaining("Balanceado");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(p -> p.getProNombre().contains("Balanceado"));
    }

    @Test
    @DisplayName("Debe buscar productos por categoría")
    void testFindByCategoria() {
        // When
        List<InvListaProductosGeneralTO> resultados = repository.findByCategoria("ALIMENTO");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(p -> p.getProCategoria().equals("ALIMENTO"));
    }

    @Test
    @DisplayName("Debe buscar productos activos")
    void testFindByActivoTrue() {
        // When
        List<InvListaProductosGeneralTO> resultados = repository.findByActivoTrue();

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(InvListaProductosGeneralTO::isProActivo);
    }

    @Test
    @DisplayName("Debe buscar productos inactivos")
    void testFindByActivoFalse() {
        // When
        List<InvListaProductosGeneralTO> resultados = repository.findByActivoFalse();

        // Then
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getProCodigoPrincipal()).isEqualTo("PROD-003");
    }

    @Test
    @DisplayName("Debe verificar existencia por código principal")
    void testExistsByCodigoPrincipal() {
        // When
        boolean existe = repository.existsByCodigoPrincipal("PROD-001");
        boolean noExiste = repository.existsByCodigoPrincipal("PROD-999");

        // Then
        assertThat(existe).isTrue();
        assertThat(noExiste).isFalse();
    }

    @Test
    @DisplayName("Debe buscar productos activos por categoría ordenados")
    void testFindByActivoTrueAndCategoriaOrderByNombreAsc() {
        // When
        List<InvListaProductosGeneralTO> resultados = 
                repository.findByActivoTrueAndCategoriaOrderByNombreAsc("ALIMENTO");

        // Then
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getProNombre()).isEqualTo("Balanceado Premium");
    }

    @Test
    @DisplayName("Debe contar productos por categoría")
    void testCountByCategoria() {
        // When
        long count = repository.countByCategoria("ALIMENTO");

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar productos activos")
    void testCountByActivoTrue() {
        // When
        long count = repository.countByActivoTrue();

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe hacer rollback de transacción")
    void testTransactionRollback() {
        // Given
        long countInicial = repository.count();

        // When
        try {
            InvListaProductosGeneralTO nuevo = createProducto(99, "PROD-999", "Producto Test", "TEST", "TEST", true);
            repository.save(nuevo);
            throw new RuntimeException("Error simulado");
        } catch (RuntimeException e) {
            // Expected
        }

        // Then
        assertThat(repository.count()).isEqualTo(countInicial);
    }

    private InvListaProductosGeneralTO createProducto(Integer id, String codigo, String nombre,
            String categoria, String subcategoria, boolean activo) {
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();
        producto.setId(id);
        producto.setProCodigoPrincipal(codigo);
        producto.setProNombre(nombre);
        producto.setProCategoria(categoria);
        producto.setProSubcategoria(subcategoria);
        producto.setProActivo(activo);
        producto.setProPrecio(new BigDecimal("100.00"));
        producto.setProGravaIva("S");
        return producto;
    }
}
