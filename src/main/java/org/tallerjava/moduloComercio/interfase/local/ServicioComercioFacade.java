package org.tallerjava.moduloComercio.interfase.local;

import org.tallerjava.moduloComercio.aplicacion.ServicioComercio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServicioComercioFacade {
    
    @Inject
    private ServicioComercio servicioPago;

}
