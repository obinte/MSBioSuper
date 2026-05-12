package com.acosux.MSBioSuper.domain.service;

import com.acosux.MSBioSuper.domain.model.InvVentasBioSuperTO;
import com.acosux.MSBioSuper.domain.model.InvVentasDetalleBioSuperTO;
import com.acosux.MSBioSuper.domain.model.InvVentasLiquidacionBioSuperTO;

import java.util.List;

/**
 * Servicio de dominio para operaciones de negocio relacionadas con ventas.
 * 
 * Este servicio encapsula la lógica de negocio compleja que involucra
 * múltiples entidades relacionadas con ventas (ventas, detalles, liquidaciones).
 * 
 * Principios de diseño:
 * - No tiene dependencias de infraestructura (Spring, JPA, etc.)
 * - Contiene lógica de negocio pura
 * - Opera sobre entidades de dominio
 * - Es stateless (sin estado)
 * 
 * @author Josias Rubén Aldaz Arcalles
 */
public class VentasService {

    /**
     * Procesa las relaciones bidireccionales entre ventas y sus entidades relacionadas.
     * 
     * Este método establece las referencias bidireccionales necesarias entre:
     * - InvVentasBioSuperTO (venta principal)
     * - InvVentasDetalleBioSuperTO (detalles de la venta)
     * - InvVentasLiquidacionBioSuperTO (liquidaciones asociadas)
     * 
     * Regla de negocio: Cada detalle y liquidación debe mantener una referencia
     * a su venta padre para garantizar la integridad referencial en el modelo de dominio.
     * 
     * @param ventas Lista de ventas a procesar
     * @return La misma lista de ventas con las relaciones bidireccionales establecidas
     * @throws IllegalArgumentException si la lista de ventas es nula
     */
    public List<InvVentasBioSuperTO> procesarRelacionesBidireccionales(List<InvVentasBioSuperTO> ventas) {
        if (ventas == null) {
            throw new IllegalArgumentException("La lista de ventas no puede ser nula");
        }

        for (InvVentasBioSuperTO venta : ventas) {
            procesarRelacionesVenta(venta);
        }

        return ventas;
    }

    /**
     * Procesa las relaciones bidireccionales de una venta individual.
     * 
     * @param venta Venta a procesar
     */
    private void procesarRelacionesVenta(InvVentasBioSuperTO venta) {
        if (venta == null) {
            return;
        }

        // Procesar detalles de venta
        if (venta.getInvVentasDetalleBioSuperTO() != null && !venta.getInvVentasDetalleBioSuperTO().isEmpty()) {
            for (InvVentasDetalleBioSuperTO detalle : venta.getInvVentasDetalleBioSuperTO()) {
                // Establecer referencia bidireccional: detalle -> venta
                detalle.setInvVentas(new InvVentasBioSuperTO(venta.getInvVentasPK()));
            }
        }

        // Procesar liquidaciones de venta
        if (venta.getInvVentasLiquidacionBioSuperTO() != null && !venta.getInvVentasLiquidacionBioSuperTO().isEmpty()) {
            for (InvVentasLiquidacionBioSuperTO liquidacion : venta.getInvVentasLiquidacionBioSuperTO()) {
                // Establecer referencia bidireccional: liquidación -> venta
                liquidacion.setInvVentas(new InvVentasBioSuperTO(venta.getInvVentasPK()));
            }
        }
    }

    /**
     * Valida que una venta tenga la estructura mínima requerida.
     * 
     * Reglas de validación:
     * - La venta debe tener una clave primaria válida
     * - La venta debe tener al menos un detalle o una liquidación
     * 
     * @param venta Venta a validar
     * @return true si la venta es válida, false en caso contrario
     */
    public boolean esVentaValida(InvVentasBioSuperTO venta) {
        if (venta == null || venta.getInvVentasPK() == null) {
            return false;
        }

        boolean tieneDetalles = venta.getInvVentasDetalleBioSuperTO() != null 
                && !venta.getInvVentasDetalleBioSuperTO().isEmpty();
        
        boolean tieneLiquidaciones = venta.getInvVentasLiquidacionBioSuperTO() != null 
                && !venta.getInvVentasLiquidacionBioSuperTO().isEmpty();

        return tieneDetalles || tieneLiquidaciones;
    }

    /**
     * Filtra ventas que cumplan con criterios de validación de negocio.
     * 
     * @param ventas Lista de ventas a filtrar
     * @return Lista de ventas válidas
     */
    public List<InvVentasBioSuperTO> filtrarVentasValidas(List<InvVentasBioSuperTO> ventas) {
        if (ventas == null) {
            return List.of();
        }

        return ventas.stream()
                .filter(this::esVentaValida)
                .toList();
    }
}
