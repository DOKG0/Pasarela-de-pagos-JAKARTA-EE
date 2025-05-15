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
    public Integer altaComercio(Comercio comercio) {
        return repositorio.guardarComercio(comercio);
    }

    @Override
    public boolean modificarDatosComercio(Comercio datosComercio) {
        return repositorio.actualizarComercio(datosComercio);
    }

    @Override
    public Integer altaPos(Integer idComercio, Pos pos) {
        Comercio comercio = repositorio.buscarPorId(idComercio);
        comercio.agregarPos(pos);
        pos.setComercio(comercio);
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

    @Override
    public boolean realizarPago(double importe) {
        return true;
    }
}