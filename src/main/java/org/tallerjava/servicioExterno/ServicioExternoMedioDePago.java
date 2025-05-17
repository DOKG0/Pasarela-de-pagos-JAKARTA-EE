package org.tallerjava.servicioExterno;

import org.tallerjava.moduloCompra.dominio.Tarjeta;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServicioExternoMedioDePago {
    
    public boolean procesarPago(String nroCuentaBancoComercio, double importe, Tarjeta datosTarjeta) {

        boolean resultado = ( (int) (Math.random() * 11) < 7);
        //aca se podria llamar a la API del modulo transferencia notificando el resultado de procesar pago

        return resultado;
    }
}
