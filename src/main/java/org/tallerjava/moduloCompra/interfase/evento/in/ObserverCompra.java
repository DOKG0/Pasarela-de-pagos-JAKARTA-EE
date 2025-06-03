package org.tallerjava.moduloCompra.interfase.evento.in;

import org.tallerjava.moduloComercio.interfase.evento.out.EventoAltaPos;
import org.tallerjava.moduloComercio.interfase.evento.out.EventoComercio;
import org.tallerjava.moduloComercio.interfase.evento.out.EventoModificacionPos;
import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Pos;
import org.tallerjava.moduloCompra.dominio.repo.CompraRepositorio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class ObserverCompra {
    @Inject
    CompraRepositorio repositorioCompra;

    public void altaComercio(@Observes EventoComercio evento) {
        Integer resultado = repositorioCompra.guardarComercio(
            evento.getIdComercio(), 
            evento.getNroCuentaBancoComercio(),
            evento.getIdCuentaBanco(),
            evento.getUsuarioComercio());
        
        if (resultado == -1) {
            System.err.println("Error en el alta de comercio en el MÓDULO COMPRA");
        }
    }

    public void altaPos(@Observes EventoAltaPos evento) {
        Comercio comercio = repositorioCompra.buscarPorId(evento.getIdComercio());
        if (comercio != null) {
            Pos nuevoPos = new Pos(
                evento.getId(), 
                evento.getIdentificador(),
                true,
                comercio
            );
            comercio.agregarPos(nuevoPos);
            boolean resultado = repositorioCompra.actualizarComercio(comercio);

            if (!resultado) {
                System.err.println("Error en el alta de pos en el MÓDULO COMPRA");
            }
        }
    }

    public void modificacionPos(@Observes EventoModificacionPos evento) {
        Comercio comercio = repositorioCompra.buscarPorId(evento.getIdComercio());
        if (comercio != null) {
            Pos pos = comercio.buscarPosPorId(evento.getIdPos());
            
            if (pos != null) {
                pos.setHabilitado(evento.isEstadoPos());
            }
            
            boolean resultado = repositorioCompra.actualizarComercio(comercio);

            if (!resultado) {
                System.err.println("Error en la modificación del pos en el MÓDULO COMPRA");
            }
        }
    }
}
