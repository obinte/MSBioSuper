package com.acosux.MSBioSuper.domain.service;

import com.acosux.MSBioSuper.domain.model.InvVentasBioSuperTO;
import com.acosux.MSBioSuper.domain.model.InvVentasDetalleBioSuperTO;
import com.acosux.MSBioSuper.domain.model.InvVentasLiquidacionBioSuperTO;
import com.acosux.MSBioSuper.domain.model.InvVentasPK;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pruebas unitarias para VentasService.
 * 
 * Valida la lógica de negocio de procesamiento de relaciones bidireccionales
 * y validación de ventas.
 * 
 * @author Josias Rubén Aldaz Arcalles
 */
@DisplayName("VentasService - Pruebas Unitarias")
class VentasServiceTest {

    private VentasService ventasService;

    @BeforeEach
    void setUp() {
        ventasService = new VentasService();
    }

    // ==================== Pruebas de procesarRelacionesBidireccionales ====================

    @Test
    @DisplayName("Debe lanzar excepción cuando lista de ventas es null")
    void procesarRelacionesBidireccionales_VentasNull_LanzaExcepcion() {
        // Given
        List<InvVentasBioSuperTO> ventas = null;

        // When & Then
        assertThatThrownBy(() -> ventasService.procesarRelacionesBidireccionales(ventas))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La lista de ventas no puede ser nula");
    }

    @Test
    @DisplayName("Debe procesar lista vacía sin errores")
    void procesarRelacionesBidireccionales_VentasVacia_ProcesaSinErrores() {
        // Given
        List<InvVentasBioSuperTO> ventas = new ArrayList<>();

        // When
        List<InvVentasBioSuperTO> resultado = ventasService.procesarRelacionesBidireccionales(ventas);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe establecer relaciones bidireccionales en detalles de venta")
    void procesarRelacionesBidireccionales_VentaConDetalles_EstableceRelaciones() {
        // Given
        InvVentasPK ventaPK = new InvVentasPK("EMP01", "2024", "FAC", "001");
        InvVentasBioSuperTO venta = new InvVentasBioSuperTO(ventaPK);
        
        List<InvVentasDetalleBioSuperTO> detalles = new ArrayList<>();
        InvVentasDetalleBioSuperTO detalle1 = new InvVentasDetalleBioSuperTO();
        detalle1.setDetSecuencial(1);
        detalles.add(detalle1);
        
        venta.setInvVentasDetalleBioSuperTO(detalles);
        
        List<InvVentasBioSuperTO> ventas = List.of(venta);

        // When
        ventasService.procesarRelacionesBidireccionales(ventas);

        // Then
        assertThat(detalle1.getInvVentas()).isNotNull();
        assertThat(detalle1.getInvVentas().getInvVentasPK()).isEqualTo(ventaPK);
    }

    @Test
    @DisplayName("Debe establecer relaciones bidireccionales en liquidaciones")
    void procesarRelacionesBidireccionales_VentaConLiquidaciones_EstableceRelaciones() {
        // Given
        InvVentasPK ventaPK = new InvVentasPK("EMP01", "2024", "FAC", "001");
        InvVentasBioSuperTO venta = new InvVentasBioSuperTO(ventaPK);
        
        List<InvVentasLiquidacionBioSuperTO> liquidaciones = new ArrayList<>();
        InvVentasLiquidacionBioSuperTO liquidacion1 = new InvVentasLiquidacionBioSuperTO();
        liquidacion1.setDetSecuencial(1);
        liquidaciones.add(liquidacion1);
        
        venta.setInvVentasLiquidacionBioSuperTO(liquidaciones);
        
        List<InvVentasBioSuperTO> ventas = List.of(venta);

        // When
        ventasService.procesarRelacionesBidireccionales(ventas);

        // Then
        assertThat(liquidacion1.getInvVentas()).isNotNull();
        assertThat(liquidacion1.getInvVentas().getInvVentasPK()).isEqualTo(ventaPK);
    }

    @Test
    @DisplayName("Debe procesar venta con detalles y liquidaciones")
    void procesarRelacionesBidireccionales_VentaCompleta_EstableceTodasRelaciones() {
        // Given
        InvVentasPK ventaPK = new InvVentasPK("EMP01", "2024", "FAC", "001");
        InvVentasBioSuperTO venta = new InvVentasBioSuperTO(ventaPK);
        
        // Agregar detalles
        List<InvVentasDetalleBioSuperTO> detalles = new ArrayList<>();
        InvVentasDetalleBioSuperTO detalle1 = new InvVentasDetalleBioSuperTO();
        detalle1.setDetSecuencial(1);
        detalles.add(detalle1);
        venta.setInvVentasDetalleBioSuperTO(detalles);
        
        // Agregar liquidaciones
        List<InvVentasLiquidacionBioSuperTO> liquidaciones = new ArrayList<>();
        InvVentasLiquidacionBioSuperTO liquidacion1 = new InvVentasLiquidacionBioSuperTO();
        liquidacion1.setDetSecuencial(1);
        liquidaciones.add(liquidacion1);
        venta.setInvVentasLiquidacionBioSuperTO(liquidaciones);
        
        List<InvVentasBioSuperTO> ventas = List.of(venta);

        // When
        ventasService.procesarRelacionesBidireccionales(ventas);

        // Then
        assertThat(detalle1.getInvVentas()).isNotNull();
        assertThat(liquidacion1.getInvVentas()).isNotNull();
    }

    // ==================== Pruebas de esVentaValida ====================

    @Test
    @DisplayName("Debe retornar false cuando venta es null")
    void esVentaValida_VentaNull_RetornaFalse() {
        // Given
        InvVentasBioSuperTO venta = null;

        // When
        boolean resultado = ventasService.esVentaValida(venta);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar false cuando venta no tiene PK")
    void esVentaValida_VentaSinPK_RetornaFalse() {
        // Given
        InvVentasBioSuperTO venta = new InvVentasBioSuperTO();

        // When
        boolean resultado = ventasService.esVentaValida(venta);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar false cuando venta no tiene detalles ni liquidaciones")
    void esVentaValida_VentaSinDetallesNiLiquidaciones_RetornaFalse() {
        // Given
        InvVentasPK ventaPK = new InvVentasPK("EMP01", "2024", "FAC", "001");
        InvVentasBioSuperTO venta = new InvVentasBioSuperTO(ventaPK);

        // When
        boolean resultado = ventasService.esVentaValida(venta);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar true cuando venta tiene detalles")
    void esVentaValida_VentaConDetalles_RetornaTrue() {
        // Given
        InvVentasPK ventaPK = new InvVentasPK("EMP01", "2024", "FAC", "001");
        InvVentasBioSuperTO venta = new InvVentasBioSuperTO(ventaPK);
        
        List<InvVentasDetalleBioSuperTO> detalles = new ArrayList<>();
        InvVentasDetalleBioSuperTO detalle1 = new InvVentasDetalleBioSuperTO();
        detalles.add(detalle1);
        venta.setInvVentasDetalleBioSuperTO(detalles);

        // When
        boolean resultado = ventasService.esVentaValida(venta);

        // Then
        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("Debe retornar true cuando venta tiene liquidaciones")
    void esVentaValida_VentaConLiquidaciones_RetornaTrue() {
        // Given
        InvVentasPK ventaPK = new InvVentasPK("EMP01", "2024", "FAC", "001");
        InvVentasBioSuperTO venta = new InvVentasBioSuperTO(ventaPK);
        
        List<InvVentasLiquidacionBioSuperTO> liquidaciones = new ArrayList<>();
        InvVentasLiquidacionBioSuperTO liquidacion1 = new InvVentasLiquidacionBioSuperTO();
        liquidaciones.add(liquidacion1);
        venta.setInvVentasLiquidacionBioSuperTO(liquidaciones);

        // When
        boolean resultado = ventasService.esVentaValida(venta);

        // Then
        assertThat(resultado).isTrue();
    }

    // ==================== Pruebas de filtrarVentasValidas ====================

    @Test
    @DisplayName("Debe retornar lista vacía cuando ventas es null")
    void filtrarVentasValidas_VentasNull_RetornaListaVacia() {
        // Given
        List<InvVentasBioSuperTO> ventas = null;

        // When
        List<InvVentasBioSuperTO> resultado = ventasService.filtrarVentasValidas(ventas);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe filtrar ventas inválidas")
    void filtrarVentasValidas_VentasInvalidas_FiltraCorrectamente() {
        // Given
        List<InvVentasBioSuperTO> ventas = new ArrayList<>();
        
        // Venta válida con detalles
        InvVentasPK ventaPK1 = new InvVentasPK("EMP01", "2024", "FAC", "001");
        InvVentasBioSuperTO ventaValida = new InvVentasBioSuperTO(ventaPK1);
        List<InvVentasDetalleBioSuperTO> detalles = new ArrayList<>();
        detalles.add(new InvVentasDetalleBioSuperTO());
        ventaValida.setInvVentasDetalleBioSuperTO(detalles);
        ventas.add(ventaValida);
        
        // Venta inválida sin detalles ni liquidaciones
        InvVentasPK ventaPK2 = new InvVentasPK("EMP01", "2024", "FAC", "002");
        InvVentasBioSuperTO ventaInvalida = new InvVentasBioSuperTO(ventaPK2);
        ventas.add(ventaInvalida);

        // When
        List<InvVentasBioSuperTO> resultado = ventasService.filtrarVentasValidas(ventas);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getInvVentasPK()).isEqualTo(ventaPK1);
    }
}
