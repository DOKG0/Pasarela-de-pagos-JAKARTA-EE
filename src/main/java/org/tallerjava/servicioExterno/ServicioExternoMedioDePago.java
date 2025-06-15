package org.tallerjava.servicioExterno;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServicioExternoMedioDePago {

    public boolean procesarPago(String nroCuentaBancoComercio, double importe, Integer idComercio) {

        boolean resultado = ( (int) (Math.random() * 11) < 7);
        
        return resultado;
    }
}
