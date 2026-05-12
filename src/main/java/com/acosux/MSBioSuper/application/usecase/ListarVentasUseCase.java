package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ResumenPescaRequest;
import com.acosux.MSBioSuper.domain.model.InvVentasBioSuperTO;
import com.acosux.MSBioSuper.domain.model.InvVentasDetalleBioSuperTO;
import com.acosux.MSBioSuper.domain.model.InvVentasLiquidacionBioSuperTO;
import com.acosux.MSBioSuper.domain.repository.VentasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para listar ventas de BioSuper.
 * Implementa la lógica de negocio para consultar ventas electrónicas autorizadas
 * según los criterios especificados en la solicitud.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 */
@Service
@Transactional(readOnly = false)
public class ListarVentasUseCase {

    private final VentasRepository ventasRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param ventasRepository Repositorio de ventas
     */
    public ListarVentasUseCase(VentasRepository ventasRepository) {
        this.ventasRepository = ventasRepository;
    }

    /**
     * Ejecuta el caso de uso para listar ventas de BioSuper.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Rango de fechas (obligatorio)
     * - Tipo de documento (fijo: "18" para facturas electrónicas)
     * 
     * Además, procesa las relaciones bidireccionales entre ventas, detalles y liquidaciones
     * para asegurar la integridad de las referencias en el grafo de objetos.
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de ventas que cumplen los criterios con relaciones procesadas
     */
    public List<InvVentasBioSuperTO> execute(ResumenPescaRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Obtener las ventas desde el repositorio usando la función de PostgreSQL
        // Tipo de documento "18" corresponde a facturas electrónicas autorizadas
        List<InvVentasBioSuperTO> ventasEntidad = ventasRepository.findVentasElectronicasAutorizadas(
                request.empresa(),
                request.fechaDesde(),
                request.fechaHasta(),
                "'18'" // Tipo de documento para facturas electrónicas
        );

        // Lógica de negocio: Procesar relaciones bidireccionales
        // Esto es necesario para mantener la integridad referencial en el grafo de objetos
        // cuando se serializan las entidades a JSON
        if (ventasEntidad != null && !ventasEntidad.isEmpty()) {
            for (InvVentasBioSuperTO venta : ventasEntidad) {
                // Procesar detalles de venta
                if (venta.getInvVentasDetalleBioSuperTO() != null 
                        && !venta.getInvVentasDetalleBioSuperTO().isEmpty()) {
                    for (InvVentasDetalleBioSuperTO detalle : venta.getInvVentasDetalleBioSuperTO()) {
                        // Establecer referencia bidireccional: detalle -> venta
                        detalle.setInvVentas(new InvVentasBioSuperTO(venta.getInvVentasPK()));
                    }
                }
                
                // Procesar liquidaciones de venta
                if (venta.getInvVentasLiquidacionBioSuperTO() != null 
                        && !venta.getInvVentasLiquidacionBioSuperTO().isEmpty()) {
                    for (InvVentasLiquidacionBioSuperTO liquidacion : venta.getInvVentasLiquidacionBioSuperTO()) {
                        // Establecer referencia bidireccional: liquidación -> venta
                        liquidacion.setInvVentas(new InvVentasBioSuperTO(venta.getInvVentasPK()));
                    }
                }
            }
        }

        return ventasEntidad;
    }
}
