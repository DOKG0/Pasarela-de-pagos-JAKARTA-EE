package org.tallerjava.moduloMonitoreo.interfase;

import java.util.logging.Logger;

import org.tallerjava.moduloComercio.interfase.evento.out.EventoReclamoComercio;
import org.tallerjava.moduloCompra.interfase.evento.out.EventoPago;
import org.tallerjava.moduloCompra.interfase.evento.out.EventoPagoError;
import org.tallerjava.moduloMonitoreo.infraestructura.RegistroMetricasConfig;
import org.tallerjava.moduloTransferencia.interfase.evento.out.EventoDepositoFinalizado;
import org.tallerjava.moduloTransferencia.interfase.evento.out.EventoPagoProcesado;
import org.tallerjava.moduloTransferencia.interfase.evento.out.EventoTransferenciaRecibida;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import jakarta.inject.Inject;

@ApplicationScoped
public class ObserverMonitoreo {
    private static final Logger LOG = Logger.getLogger(ObserverMonitoreo.class.getName());

    @Inject
    RegistroMetricasConfig metricas;


    // moduloComercio
    // Observador para EventoReclamoComercio  CU notificarReclamoComercio()
    public void onReclamoComercio(@Observes EventoReclamoComercio evento) {
        LOG.info("[Monitoreo] Reclamo realizado por Comercio  - ID: " + evento.getIdComercio() 
                + ", Reclamo - ID: " + evento.getIdReclamo());
                metricas.getRegistry().counter("reclamos_comercio_total").increment();
    }



     // moduloCompra
    // Observador para EventoPago  CU notificarPago()
    public void onPago(@Observes EventoPago evento) {
        LOG.info("[Monitoreo] Solicitud de pago realizada - Comercio: " + evento.getIdComercio() + " - Compra: " + evento.getIdCompra());
        metricas.getRegistry().counter("pagos_realizados_total").increment();
    }

    // Observador para EventoPagoError  CU notificarPagoError()
    public void onPagoError(@Observes EventoPagoError evento) {
        LOG.info("[Monitoreo] Pago rechazado - Comercio: " + evento.getIdComercio() + " - Compra: " + evento.getIdCompra() 
                + ", Estado: " + evento.getEstadoCompra());
                metricas.getRegistry().counter("pagos_rechazados_total").increment();
    }



    // moduloTransferencia
    // Observador para EventoPagoProcesado  CU notificarPagoOk()
    public void onPagoProcesado(@Observes EventoPagoProcesado evento) {
        LOG.info("[Monitoreo] Pago procesado - Comercio: " + evento.getIdComercio() 
                + ", Monto: " + evento.getMontoNeto());
                metricas.getRegistry().counter("pagos_procesados_total").increment();
    }

    // Observador para EventoTransferenciaRecibida  CU notificarTransferencia()
    public void onTransferenciaRecibida(@Observes EventoTransferenciaRecibida evento) {
        LOG.info("[Monitoreo] Transferencia recibida - Código: " + evento.getCodigoTransaccion() 
                + ", Monto: " + evento.getMonto());
                metricas.getRegistry().counter("transferencias_recibidas_total").increment();
    }

    // Observador para EventoDepositoFinalizado
    public void onDepositoFinalizado(@Observes EventoDepositoFinalizado evento) {
        LOG.info("[Monitoreo] Depósito finalizado - ID: " + evento.getIdDeposito() 
                + ", Comisión: " + evento.getComision());
                metricas.getRegistry().counter("depositos_finalizados_total").increment();
    }
    
}
