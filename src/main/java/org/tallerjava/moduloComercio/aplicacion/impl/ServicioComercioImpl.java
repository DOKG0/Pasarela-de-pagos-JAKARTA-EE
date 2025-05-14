package org.tallerjava.moduloComercio.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.tallerjava.moduloComercio.aplicacion.ServicioComercio;
import org.tallerjava.moduloComercio.dominio.*;
import org.tallerjava.moduloComercio.dominio.repo.RepositorioComercio;

@ApplicationScoped
public class ServicioComercioImpl implements ServicioComercio {

    @Inject
    private RepositorioComercio repositorio;

    @Override
    public Integer altaComercio(Comercio datosComercio) {
        return repositorio.guardarComercio(datosComercio);
    }

    @Override
    public boolean modificarDatosComercio(Comercio datosComercio) {
        return repositorio.actualizarComercio(datosComercio);
    }

    @Override
    public Integer altaPos(Comercio comercio, Pos pos) {
        comercio.agregarPos(pos);
        repositorio.actualizarComercio(comercio);
        return pos.getId();
    }

    @Override
    public boolean cambiarEstadoPos(Comercio comercio, Pos pos, boolean estado) {
        pos.setHabilitado(estado);
        return repositorio.actualizarComercio(comercio);
    }

    @Override
    public boolean cambioContraseña(String nuevaPass) {
        Comercio comercio = new Comercio(); // Toca cambiarlo a un get Comercio por parametros o agregar id a este
        comercio.setContraseña(nuevaPass);
        return repositorio.actualizarComercio(comercio);
    }

    @Override
    public Integer realizarReclamo(String textoReclamo) {
        Comercio comercio = new Comercio(); // Toca cambiarlo a un get Comercio por parametros o agregar id a este
        Reclamo reclamo = new Reclamo(textoReclamo);
        comercio.agregarReclamo(reclamo);
        repositorio.actualizarComercio(comercio);
        return reclamo.getId();
    }

}