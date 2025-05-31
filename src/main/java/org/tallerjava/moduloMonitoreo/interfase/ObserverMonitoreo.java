package org.tallerjava.moduloMonitoreo.interfase;

import java.util.logging.Logger;

import org.tallerjava.moduloComercio.interfase.evento.out.EventoReclamoComercio;
import org.tallerjava.moduloCompra.interfase.evento.out.EventoPago;
import org.tallerjava.moduloCompra.interfase.evento.out.EventoPagoError;
import org.tallerjava.moduloTransferencia.interfase.evento.out.EventoDepositoFinalizado;
import org.tallerjava.moduloTransferencia.interfase.evento.out.EventoPagoProcesado;
import org.tallerjava.moduloTransferencia.interfase.evento.out.EventoTransferenciaRecibida;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class ObserverMonitoreo {
    private static final Logger LOG = Logger.getLogger(ObserverMonitoreo.class.getName());

    // moduloComercio
    // Observador para EventoReclamoComercio  CU notificarReclamoComercio()
    public void onReclamoComercio(@Observes EventoReclamoComercio evento) {
        LOG.info("[Monitoreo] Reclamo realizado por Comercio  - ID: " + evento.getIdComercio() 
                + ", Reclamo - ID: " + evento.getIdReclamo());
    }



     // moduloCompra
    // Observador para EventoPago  CU notificarPago()
    public void onPago(@Observes EventoPago evento) {
        LOG.info("[Monitoreo] Pago realizado - Comercio: " + evento.getIdComercio() + " - Compra: " + evento.getIdCompra() 
                + ", Estado: " + evento.getEstadoCompra());
    }

    // Observador para EventoPagoError  CU notificarPagoError()
    public void onPagoError(@Observes EventoPagoError evento) {
        LOG.info("[Monitoreo] Pago rechazado - Comercio: " + evento.getIdComercio() + " - Compra: " + evento.getIdCompra() 
                + ", Estado: " + evento.getEstadoCompra());
    }



    // moduloTransferencia
    // Observador para EventoPagoProcesado  CU notificarPagoOk()
    public void onPagoProcesado(@Observes EventoPagoProcesado evento) {
        LOG.info("[Monitoreo] Pago procesado - Comercio: " + evento.getIdComercio() 
                + ", Monto: " + evento.getMontoNeto());
    }

    // Observador para EventoTransferenciaRecibida  CU notificarTransferencia()
    public void onTransferenciaRecibida(@Observes EventoTransferenciaRecibida evento) {
        LOG.info("[Monitoreo] Transferencia recibida - Código: " + evento.getCodigoTransaccion() 
                + ", Monto: " + evento.getMonto());
    }

    // Observador para EventoDepositoFinalizado
    public void onDepositoFinalizado(@Observes EventoDepositoFinalizado evento) {
        LOG.info("[Monitoreo] Depósito finalizado - ID: " + evento.getIdDeposito() 
                + ", Comisión: " + evento.getComision());
    }
    
}
