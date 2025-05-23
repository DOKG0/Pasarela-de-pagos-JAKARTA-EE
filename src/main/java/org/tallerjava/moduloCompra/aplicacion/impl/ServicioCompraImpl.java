package org.tallerjava.moduloCompra.aplicacion.impl;

import java.time.LocalDateTime;

import javax.management.RuntimeErrorException;

import org.tallerjava.moduloCompra.interfase.evento.out.PublicadorEvento;
import org.tallerjava.moduloComercio.interfase.local.ServicioComercioFacade;
import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.Tarjeta;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;
import org.tallerjava.moduloCompra.dominio.repo.CompraRepositorio;
import org.tallerjava.servicioExterno.ServicioExternoMedioDePago;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServicioCompraImpl implements ServicioCompra{
    
    @Inject
    private CompraRepositorio repositorio;
    
    @Inject
    private ServicioComercioFacade serviceComercio;

    @Inject
    private PublicadorEvento publicador;

    @Inject
    private ServicioExternoMedioDePago servicioExterno;

    @Override
    public boolean procesarPago(Integer idComercio, double importe, boolean resultado) {
        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) return false;
        
        Compra nuevaCompra = new Compra(importe);
        repositorio.actualizarComercio(comercio);
        publicador.publicarEventoPago(idComercio, nuevaCompra.getId(), nuevaCompra.getEstado()); // envial null como id, hay que arreglar
    

        if (resultado) {
            nuevaCompra.setEstado(EstadoCompra.APROBADA);
            comercio.setImporteVentasDelDia(comercio.getImporteVentasDelDia() + importe);
        } else {
            nuevaCompra.setEstado(EstadoCompra.RECHAZADA);
            publicador.publicarEventoPagoError(idComercio, nuevaCompra.getId(), nuevaCompra.getEstado());
        }

        comercio.agregarCompra(nuevaCompra);
        repositorio.actualizarComercio(comercio);

        return resultado;
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
    public double montoActualVendido(Integer idComercio) {
        Comercio comercio = repositorio.buscarPorId(idComercio);

        return comercio.getImporteVentasDelDia();
    }

}
