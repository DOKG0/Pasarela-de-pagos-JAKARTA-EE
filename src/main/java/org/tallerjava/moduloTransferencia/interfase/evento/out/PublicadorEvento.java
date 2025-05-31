package org.tallerjava.moduloTransferencia.interfase.evento.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class PublicadorEvento {
    
    private static final Logger LOG = Logger.getLogger(PublicadorEvento.class.getName());

    @Inject
    private Event<EventoPagoProcesado> pagoProcesado;

    @Inject
    private Event<EventoTransferenciaRecibida> transferenciaRecibida ;

    @Inject
    private Event<EventoDepositoFinalizado > depositoFinalizado ;

     public void publicarPagoProcesado(String codigoTransaccion, Integer idComercio, 
                                     BigDecimal montoBruto, BigDecimal montoNeto) {
        pagoProcesado.fire(new EventoPagoProcesado(
            codigoTransaccion,
            idComercio,
            montoBruto,
            montoNeto,
            LocalDateTime.now()
        ));
    }

    public void publicarTransferenciaRecibida(String codigoTransaccion, Integer idComercio, 
                                            BigDecimal monto) {
        transferenciaRecibida.fire(new EventoTransferenciaRecibida(
            codigoTransaccion,
            idComercio,
            monto,
            LocalDateTime.now()
        ));
    }

    public void publicarDepositoFinalizado(Integer idDeposito, Integer idComercio,
                                         BigDecimal montoNeto, BigDecimal comision) {
        depositoFinalizado.fire(new EventoDepositoFinalizado(
            idDeposito,
            idComercio,
            montoNeto,
            comision,
            LocalDateTime.now()
        ));

        LOG.info("Publicando evento de depósito: " + depositoFinalizado);
    }
}
