package org.tallerjava.moduloTransferencia.aplicacion;

import org.tallerjava.moduloTransferencia.dominio.Deposito;
import java.time.LocalDate;
import java.util.List;

public interface ServicioTransferencia {
    boolean recibirNotificacionTransferenciaDesdeMedioPago(
        String nroCuentaBancoComercio, 
        double monto, 
        String codigoTransaccion,
        Integer idComercio
    );
    List<Deposito> consultarDepositos(Integer idComercio, LocalDate fechaInicio, LocalDate fechaFin);

}