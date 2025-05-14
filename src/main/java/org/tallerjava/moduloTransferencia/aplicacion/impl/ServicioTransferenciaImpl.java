package org.tallerjava.moduloTransferencia.aplicacion.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloTransferencia.dominio.Deposito;

public class ServicioTransferenciaImpl {
    public Boolean recibirNotificacionTransferenciaDesdeMedioPago(Deposito datosTransferencia){
        return false;
    }
    public List<Deposito> consultarDepositos(Comercio comercio, LocalDate fechaInicial, LocalDate fechaFin){
        return new ArrayList<>();
    }
}
