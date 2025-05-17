package org.tallerjava.moduloComercio.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.tallerjava.moduloComercio.aplicacion.ServicioComercio;
import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.Pos;
import org.tallerjava.moduloComercio.dominio.Reclamo;
import org.tallerjava.moduloComercio.dominio.repo.RepositorioComercio;
import org.tallerjava.moduloComercio.interfase.evento.out.PublicadorEvento;

@ApplicationScoped
public class ServicioComercioImpl implements ServicioComercio {

    @Inject
    private RepositorioComercio repositorio;
    @Inject
    private PublicadorEvento publicador;

    @Override
    public Integer altaComercio(Comercio comercio) {

        Integer idComercio = repositorio.guardarComercio(comercio);
        publicador.publicarEventoComercio(idComercio);

        return idComercio;
    }

    @Override
    public boolean modificarDatosComercio(Integer id, String rut, String nombre, String direccion) {
        Comercio comercio = repositorio.buscarPorId(id);
        comercio.setNombre(
            nombre == null ? comercio.getNombre() : nombre
        );
        comercio.setDireccion(
            direccion == null ? comercio.getDireccion() : direccion
        );
        comercio.setRut(
            rut == null ? comercio.getRut() : rut
        );

        return repositorio.actualizarComercio(comercio);
    }

    @Override
    public Integer altaPos(Integer idComercio, Pos pos) {
        Comercio comercio = repositorio.buscarPorId(idComercio);

        if (comercio == null) {
            return -1;
        }

        comercio.agregarPos(pos);
        pos.setComercio(comercio);
        repositorio.actualizarComercio(comercio);

        return pos.getId();
    }

    @Override
    public boolean cambiarEstadoPos(Integer idComercio, Integer identificadorPos, boolean estado) {

        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) {
            return false;
        }

        Pos pos = comercio.buscarPosPorId(identificadorPos);

        if (pos == null) {
            return false;
        }

        pos.setHabilitado(estado);
        return repositorio.actualizarComercio(comercio);
    }

    @Override
    public boolean cambioContraseña(Integer idComercio, String nuevaPass) {
        Comercio comercio = repositorio.buscarPorId(idComercio);

        if (comercio == null) {
            return false;
        }

        comercio.setContraseña(nuevaPass);
        return repositorio.actualizarComercio(comercio);
    }

    @Override
    public Integer realizarReclamo(Integer idComercio, Reclamo reclamo) {
        Comercio comercio = repositorio.buscarPorId(idComercio);

        if (comercio == null) {
            return -1;
        }

        comercio.agregarReclamo(reclamo);
        boolean actualizacionCorrecta = repositorio.actualizarComercio(comercio);

        if (actualizacionCorrecta) {
            Comercio comercioPostActualizacion = repositorio.buscarPorId(idComercio);
            Reclamo ultimoReclamo = comercioPostActualizacion
                .getReclamos()
                .get(comercioPostActualizacion.getReclamos().size()-1);
            Integer idNuevoReclamo = ultimoReclamo.getId();
            return idNuevoReclamo;
        } else {
            return -1;
        }
    }

    @Override
    public boolean realizarPago(double importe) {
        return true;
    }
}