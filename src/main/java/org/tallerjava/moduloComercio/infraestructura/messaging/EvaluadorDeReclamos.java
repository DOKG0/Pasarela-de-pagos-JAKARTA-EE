package org.tallerjava.moduloComercio.infraestructura.messaging;

import org.tallerjava.moduloComercio.dominio.CategoriaReclamo;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface EvaluadorDeReclamos {

    public CategoriaReclamo evaluarReclamo(String textoReclamo);

}
