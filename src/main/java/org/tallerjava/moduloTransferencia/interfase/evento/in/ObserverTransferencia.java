package org.tallerjava.moduloTransferencia.interfase.evento.in;

import org.tallerjava.moduloTransferencia.dominio.Comercio;

import java.util.logging.Logger;

import org.tallerjava.moduloComercio.interfase.evento.out.EventoComercio;
import org.tallerjava.moduloTransferencia.dominio.repo.TransferenciaRepositorio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class ObserverTransferencia {
    private static final Logger LOG = Logger.getLogger(ObserverTransferencia.class.getName());
    @Inject
    TransferenciaRepositorio transferenciaRepositorio;

    public void altaComercio(@Observes EventoComercio eventoComercio) {
        Comercio comercio = new Comercio();
        comercio.setId(eventoComercio.getIdComercio());

        transferenciaRepositorio.guardarComercio(comercio);
        LOG.info("Se ha dado de alta un comercio con ID: " + eventoComercio.getIdComercio());
    }
}
