package org.tallerjava.servicioExterno;

import org.tallerjava.moduloComercio.interfase.local.ServicioComercioFacade;
import org.tallerjava.moduloCompra.dominio.Tarjeta;
import org.tallerjava.moduloTransferencia.interfase.local.ServicioTransferenciaFacade;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServicioExternoMedioDePago {
    
    @Inject
    private ServicioTransferenciaFacade serviceTransferencia;

    public boolean procesarPago(String nroCuentaBancoComercio, double importe, Integer idComercio) {

        boolean resultado = ( (int) (Math.random() * 11) < 7);
        
        return resultado;
    }
}
