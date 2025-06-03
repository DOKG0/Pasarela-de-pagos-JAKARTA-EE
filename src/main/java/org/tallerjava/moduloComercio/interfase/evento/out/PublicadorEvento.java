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

    @Inject
    private Event<EventoAltaPos> eventoAltaPos;

    @Inject 
    private Event<EventoModificacionPos> eventoModificacionPos;

    public void publicarEventoComercio(
        Integer idComercio, 
        String nroCuentaBancoComercio, 
        Integer idCuentaBanco, 
        String usuarioComercio) {

        EventoComercio evento = new EventoComercio(idComercio, nroCuentaBancoComercio, idCuentaBanco, usuarioComercio);
        eventoComercio.fire(evento);
    }

    public void publicarEventoReclamoComercio(Integer idComercio, Integer idReclamo) {
        EventoReclamoComercio evento = new EventoReclamoComercio(idComercio, idReclamo);
        eventoReclamoComercio.fire(evento);
    }

    public void publicarEventoAltaPos(Integer idPos, String identificadorPos, boolean estadoPos, Integer idComercio) {
        EventoAltaPos evento = new EventoAltaPos(idPos, identificadorPos, estadoPos, idComercio);
        eventoAltaPos.fire(evento);
    }

    public void publicarEventoModificacionPos(
        Integer idPos, boolean estadoPos, Integer idComercio) {

        EventoModificacionPos evento = new EventoModificacionPos(idPos, idComercio, estadoPos);
        eventoModificacionPos.fire(evento);
    }
}
