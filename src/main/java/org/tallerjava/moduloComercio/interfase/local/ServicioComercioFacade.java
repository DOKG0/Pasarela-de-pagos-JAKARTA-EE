package org.tallerjava.moduloComercio.interfase.local;

import org.tallerjava.moduloComercio.aplicacion.ServicioComercio;

import jakarta.inject.Inject;

public class ServicioComercioFacade {
    
    @Inject
    private ServicioComercio servicioPago;

    public boolean realizarPago(double importe, Integer pos) {
        return servicioPago.realizarPago(importe);
    }
}
