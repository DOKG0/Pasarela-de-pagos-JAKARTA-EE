package org.tallerjava.moduloCompra.interfase.evento.out;

import org.tallerjava.moduloCompra.dominio.EstadoCompra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class PublicadorEvento {
    @Inject
    private Event<EventoPago> eventoPago;

    @Inject
    private Event<EventoPagoError> eventoPagoError;

    public void publicarEventoPago(Integer idComercio, Integer idCompra, EstadoCompra estadoCompra) {
        EventoPago evento = new EventoPago(idComercio, idCompra, estadoCompra);
        eventoPago.fire(evento);
    }

    public void publicarEventoPagoError(Integer idComercio, Integer idCompra, EstadoCompra estadoCompra) {
        EventoPagoError evento = new EventoPagoError(idComercio, idCompra, estadoCompra);
        eventoPagoError.fire(evento);
}
}
