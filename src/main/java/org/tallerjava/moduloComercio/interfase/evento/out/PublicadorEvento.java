package org.tallerjava.moduloComercio.interfase.evento.out;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class PublicadorEvento {
    @Inject
    private Event<EventoComercio> eventoComercio;

    @Inject
    private Event<EventoReclamoComercio> eventoReclamoComercio;

    public void publicarEventoComercio(Integer idComercio, String nroCuentaBancoComercio, Integer idCuentaBanco) {
        EventoComercio evento = new EventoComercio(idComercio, nroCuentaBancoComercio, idCuentaBanco);
        eventoComercio.fire(evento);
    }

    public void publicarEventoReclamoComercio(Integer idComercio, Integer idReclamo) {
        EventoReclamoComercio evento = new EventoReclamoComercio(idComercio, idReclamo);
        eventoReclamoComercio.fire(evento);
    }
}
