package org.tallerjava.moduloCompra.aplicacion.impl;

import java.time.LocalDateTime;
import java.util.logging.Logger;

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
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ServicioCompraImpl implements ServicioCompra{
    
    private static final Logger LOG = Logger.getLogger(ServicioCompraImpl.class.getName());
    
    @Inject
    private CompraRepositorio repositorio;

    @Inject
    private PublicadorEvento publicador;

    @Override
    @Transactional
    public boolean ingresarNuevaCompra(Integer idComercio, double importe, boolean resultado, Integer idPos) {
        Comercio comercio = repositorio.buscarPorIdConBloqueo(idComercio);
        if (comercio == null) return false;
        
        Pos pos = comercio.buscarPosPorId(idPos);
        //si el comercio no tiene ese pos o el pos esta deshabilitado
        if (pos == null || !pos.isHabilitado()) return false; 

        Compra nuevaCompra = new Compra(importe);
        comercio.agregarCompra(nuevaCompra);

        if (resultado) {
            nuevaCompra.setEstado(EstadoCompra.APROBADA);
            comercio.setImporteVentasDelDia(comercio.getImporteVentasDelDia() + importe);
            LOG.info("[ServicioCompra] Compra Aprobada");
        } else {
            nuevaCompra.setEstado(EstadoCompra.RECHAZADA);
            LOG.info("[ServicioCompra] Compra Rechazada");
        }

        boolean actualizacionExitosa = repositorio.actualizarComercio(comercio);

        if (actualizacionExitosa) {
            publicador.publicarEventoPago(idComercio, repositorio.traerIdCompra(idComercio), nuevaCompra.getEstado());

            if(!resultado){
                publicador.publicarEventoPagoError(
                    idComercio, 
                    repositorio.traerIdCompra(idComercio), 
                    nuevaCompra.getEstado());
            }
        }
        
        
        return actualizacionExitosa;
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
