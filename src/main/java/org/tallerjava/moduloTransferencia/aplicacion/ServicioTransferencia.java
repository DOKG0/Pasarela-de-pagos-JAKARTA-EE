package org.tallerjava.moduloTransferencia.aplicacion;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloTransferencia.dominio.Deposito;
import java.time.LocalDate;
import java.util.List;

public interface ServicioTransferencia {
    void recibirNotificacionTransferenciaDesdeMedioPago(
        Integer idComercio, 
        double monto, 
        String codigoTransaccion
    );
    List<Deposito> consultarDepositos(Comercio comercio, LocalDate fechaInicio, LocalDate fechaFin);
}