package org.tallerjava.servicioExterno;

import org.tallerjava.moduloComercio.interfase.local.ServicioComercioFacade;
import org.tallerjava.moduloCompra.dominio.Tarjeta;
import org.tallerjava.moduloTransferencia.interfase.local.ServicioTransferenciaFacade;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServicioExternoMedioDePago {
    
    //Momentaneo
    
    Integer idTransaccion;
    
    @Inject
    private ServicioTransferenciaFacade serviceTransferencia;

    @Inject
    private ServicioRepositorio servicioRepositorio;
    

    public boolean procesarPago(String nroCuentaBancoComercio, double importe, Tarjeta datosTarjeta, Integer idComercio) {

        boolean resultado = ( (int) (Math.random() * 11) < 7);

        if (resultado){ 
            if (servicioRepositorio.traerIdTransferencia() == null) {
                idTransaccion = 1;
            } else {
                idTransaccion = servicioRepositorio.traerIdTransferencia() + 1;
            }
            serviceTransferencia.recibirNotificacionTransferenciaDesdeMedioPago(nroCuentaBancoComercio, importe, idTransaccion.toString(), idComercio);
            this.idTransaccion++;
        }
        return resultado;
    }
}
