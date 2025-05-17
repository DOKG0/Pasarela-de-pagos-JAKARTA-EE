package org.tallerjava.moduloCompra.aplicacion.impl;

import java.time.LocalDateTime;

import org.tallerjava.moduloComercio.interfase.local.ServicioComercioFacade;
import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;
import org.tallerjava.moduloCompra.dominio.repo.CompraRepositorio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServicioCompraImpl implements ServicioCompra{
    
    @Inject
    private CompraRepositorio repositorio;
    
    @Inject
    private ServicioComercioFacade serviceComercio;

    @Override
    public boolean procesarPago(Compra datosCompra) {
        if (serviceComercio.realizarPago(0, null)) {
            datosCompra.setEstado(EstadoCompra.APROBADA);
            return true;
        }
        datosCompra.setEstado(EstadoCompra.RECHAZADA);
        return false;
    }

    @Override
    public DTOResumenVentas resumenVentasDiarias(Integer idComercio) {
        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) return null;
        //to do: resolver fechas de un dia con localdatetime
        return null;
    }

    @Override
    public DTOResumenVentas resumenVentasPorPeriodo(
        Integer idComercio, LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) return null;

        return comercio.getResumenVentasPorPeriodo(fechaInicio, fechaFin);
    }

    @Override
    public DTOResumenVentas resumenVentasPorEstado(Integer idComercio, EstadoCompra estado) {
        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) return null;

        return comercio.getResumenVentasPorEstado(estado);
    }


    @Override
    public double montoActualVendido(Comercio comercio) {
        throw new UnsupportedOperationException("Unimplemented method 'montoActualVendido'");
    }

}
