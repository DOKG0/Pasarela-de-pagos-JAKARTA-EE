package org.tallerjava.moduloComercio.interfase.evento.out;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class PublicadorEvento {
    @Inject
    private Event<EventoComercio> eventoComercio;

    public void publicarEventoComercio(Integer idComercio) {
        EventoComercio evento = new EventoComercio(idComercio);
        eventoComercio.fire(evento);
    }
}
