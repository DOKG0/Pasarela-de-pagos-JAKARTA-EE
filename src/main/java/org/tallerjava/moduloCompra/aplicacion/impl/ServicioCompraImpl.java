package org.tallerjava.moduloCompra.aplicacion.impl;

import java.time.LocalDate;
import org.tallerjava.moduloComercio.interfase.local.ServicioComercioFacade;
import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.ResumenVentas;
import org.tallerjava.moduloCompra.dominio.repo.CompraRepositorio;

import jakarta.inject.Inject;

public class ServicioCompraImpl implements ServicioCompra{
    
    @Inject
    private CompraRepositorio repositorio;
    
    @Inject
    private ServicioComercioFacade serviceComercio;

    @Override
    public boolean procesarPago(Compra datosCompra) {
        if (serviceComercio.realizarPago(0, null)) {
            datosCompra.marcarComoAprobada();
            return true;
        }
        datosCompra.marcarComoRechazada();
        return false;
    }

    @Override
    public ResumenVentas resumenVentasDiarias(Comercio comercio) {
        throw new UnsupportedOperationException("Unimplemented method 'resumenVentasDiarias'");
    }

    @Override
    public ResumenVentas resumenVentasPorPeriodo(Comercio comercio, LocalDate fechaInicio, LocalDate fechaFin) {
        throw new UnsupportedOperationException("Unimplemented method 'resumenVentasPorPeriodo'");
    }

    @Override
    public double montoActualVendido(Comercio comercio) {
        throw new UnsupportedOperationException("Unimplemented method 'montoActualVendido'");
    }

}
