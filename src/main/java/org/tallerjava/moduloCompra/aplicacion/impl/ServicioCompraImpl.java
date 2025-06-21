package org.tallerjava.moduloCompra.aplicacion.impl;

import java.time.LocalDateTime;

import javax.management.RuntimeErrorException;

import org.tallerjava.moduloCompra.interfase.evento.out.PublicadorEvento;
import org.tallerjava.moduloCompra.aplicacion.ServicioCompra;
import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.Pos;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;
import org.tallerjava.moduloCompra.dominio.repo.CompraRepositorio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServicioCompraImpl implements ServicioCompra{
    
    @Inject
    private CompraRepositorio repositorio;

    @Inject
    private PublicadorEvento publicador;

    @Override
    public boolean procesarPago(Integer idComercio, double importe, boolean resultado, Integer idPos) {
        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) return false;
        
        Pos pos = comercio.buscarPosPorId(idPos);
        //si el comercio no tiene ese pos o el pos esta deshabilitado
        if (pos == null || !pos.isHabilitado()) return false; 

        Compra nuevaCompra = new Compra(importe);
        comercio.agregarCompra(nuevaCompra);
        
        if (comercio.getCompras().contains(nuevaCompra)) {
            System.out.println("[ServicioCompra] Compra registrada en comercio");
        } else {
            System.out.println("[ServicioCompra] Error al registrar la compra en el comercio");
            throw new RuntimeErrorException(null, "Error al registrar la compra en el comercio");
        }

        if (resultado) {
            nuevaCompra.setEstado(EstadoCompra.APROBADA);
            comercio.setImporteVentasDelDia(comercio.getImporteVentasDelDia() + importe);
            System.out.println("[ServicioCompra] Compra Aprobada");
        } else {
            nuevaCompra.setEstado(EstadoCompra.RECHAZADA);
            System.out.println("[ServicioCompra] Compra Rechazada");
        }

        repositorio.actualizarComercio(comercio);
        publicador.publicarEventoPago(idComercio, repositorio.traerIdCompra(idComercio), nuevaCompra.getEstado());

        if(!resultado){
            publicador.publicarEventoPagoError(idComercio, repositorio.traerIdCompra(idComercio), nuevaCompra.getEstado());
        }

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
