package org.tallerjava.servicioExterno;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServicioExternoMedioDePago {

    public boolean procesarPago() {

        boolean resultado = ( (int) (Math.random() * 11) < 8);
        
        return resultado;
    }
}
