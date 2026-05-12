package com.acosux.MSBioSuper.domain.service;

import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para operaciones de negocio relacionadas con cuentas.
 * 
 * Este servicio encapsula la lógica de negocio compleja para el procesamiento,
 * validación y cálculos relacionados con cuentas por cobrar y cuentas por pagar.
 * 
 * Principios de diseño:
 * - No tiene dependencias de infraestructura (Spring, JPA, etc.)
 * - Contiene lógica de negocio pura
 * - Opera sobre entidades de dominio
 * - Es stateless (sin estado)
 * 
 * @author Josias Rubén Aldaz Arcalles
 */
public class CuentasService {

    /**
     * Calcula el total de saldo pendiente en cuentas por cobrar.
     * 
     * Regla de negocio: Suma todos los saldos pendientes de las cuentas,
     * considerando solo valores no nulos.
     * 
     * @param cuentas Lista de cuentas por cobrar
     * @return Total de saldo pendiente
     */
    public BigDecimal calcularTotalSaldoCobrar(List<CuentasPorCobrarDetalladoApiTO> cuentas) {
        if (cuentas == null || cuentas.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return cuentas.stream()
                .map(CuentasPorCobrarDetalladoApiTO::getCxcdSaldo)
                .filter(saldo -> saldo != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula el total de saldo pendiente en cuentas por pagar.
     * 
     * Regla de negocio: Suma todos los saldos pendientes de las cuentas,
     * considerando solo valores no nulos.
     * 
     * @param cuentas Lista de cuentas por pagar
     * @return Total de saldo pendiente
     */
    public BigDecimal calcularTotalSaldoPagar(List<CuentasPorPagarDetalladoTO> cuentas) {
        if (cuentas == null || cuentas.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return cuentas.stream()
                .map(CuentasPorPagarDetalladoTO::getCxpdSaldo)
                .filter(saldo -> saldo != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Filtra cuentas por cobrar por cliente.
     * 
     * @param cuentas Lista de cuentas por cobrar
     * @param cliente Código de cliente a buscar
     * @return Lista de cuentas del cliente
     */
    public List<CuentasPorCobrarDetalladoApiTO> filtrarCuentasCobrarPorCliente(
            List<CuentasPorCobrarDetalladoApiTO> cuentas,
            String cliente) {

        if (cuentas == null || cliente == null || cliente.isBlank()) {
            return cuentas != null ? cuentas : List.of();
        }

        return cuentas.stream()
                .filter(c -> cliente.equals(c.getCxcdClienteId()))
                .collect(Collectors.toList());
    }

    /**
     * Agrupa cuentas por cobrar por cliente.
     * 
     * @param cuentas Lista de cuentas por cobrar
     * @return Mapa con cliente como clave y lista de cuentas como valor
     */
    public java.util.Map<String, List<CuentasPorCobrarDetalladoApiTO>> agruparCuentasCobrarPorCliente(
            List<CuentasPorCobrarDetalladoApiTO> cuentas) {

        if (cuentas == null) {
            return java.util.Map.of();
        }

        return cuentas.stream()
                .filter(c -> c.getCxcdClienteId() != null)
                .collect(Collectors.groupingBy(CuentasPorCobrarDetalladoApiTO::getCxcdClienteId));
    }

    /**
     * Valida que una cuenta por cobrar tenga los datos mínimos requeridos.
     * 
     * @param cuenta Cuenta a validar
     * @return true si la cuenta es válida, false en caso contrario
     */
    public boolean esCuentaCobrarValida(CuentasPorCobrarDetalladoApiTO cuenta) {
        if (cuenta == null) {
            return false;
        }

        return cuenta.getCxcdNumero() != null 
                && !cuenta.getCxcdNumero().isEmpty()
                && cuenta.getCxcdClienteId() != null 
                && !cuenta.getCxcdClienteId().isEmpty();
    }

    /**
     * Valida que una cuenta por pagar tenga los datos mínimos requeridos.
     * 
     * @param cuenta Cuenta a validar
     * @return true si la cuenta es válida, false en caso contrario
     */
    public boolean esCuentaPagarValida(CuentasPorPagarDetalladoTO cuenta) {
        if (cuenta == null) {
            return false;
        }

        return cuenta.getCxpdNumero() != null 
                && !cuenta.getCxpdNumero().isEmpty();
    }

    /**
     * Filtra cuentas por cobrar válidas.
     * 
     * @param cuentas Lista de cuentas por cobrar
     * @return Lista de cuentas válidas
     */
    public List<CuentasPorCobrarDetalladoApiTO> filtrarCuentasCobrarValidas(
            List<CuentasPorCobrarDetalladoApiTO> cuentas) {

        if (cuentas == null) {
            return List.of();
        }

        return cuentas.stream()
                .filter(this::esCuentaCobrarValida)
                .collect(Collectors.toList());
    }

    /**
     * Filtra cuentas por pagar válidas.
     * 
     * @param cuentas Lista de cuentas por pagar
     * @return Lista de cuentas válidas
     */
    public List<CuentasPorPagarDetalladoTO> filtrarCuentasPagarValidas(
            List<CuentasPorPagarDetalladoTO> cuentas) {

        if (cuentas == null) {
            return List.of();
        }

        return cuentas.stream()
                .filter(this::esCuentaPagarValida)
                .collect(Collectors.toList());
    }
}
