package org.tallerjava.moduloTransferencia.interfase.local;


import org.tallerjava.moduloTransferencia.aplicacion.ServicioTransferencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServicioTransferenciaFacade{

    @Inject
    private ServicioTransferencia servicioTransferencia;

    public void recibirNotificacionTransferenciaDesdeMedioPago(
        String nroCuentaBancoComercio, 
        double monto, 
        String codigoTransaccion,
        Integer idComercio
    ){
        servicioTransferencia.recibirNotificacionTransferenciaDesdeMedioPago(nroCuentaBancoComercio, monto, codigoTransaccion, idComercio);
    }
}
