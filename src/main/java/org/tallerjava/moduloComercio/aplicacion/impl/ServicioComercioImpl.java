package org.tallerjava.moduloComercio.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.tallerjava.moduloComercio.aplicacion.ServicioComercio;
import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.CuentaBancoComercio;
import org.tallerjava.moduloComercio.dominio.Pos;
import org.tallerjava.moduloComercio.dominio.Reclamo;
import org.tallerjava.moduloComercio.dominio.repo.RepositorioComercio;
import org.tallerjava.moduloComercio.interfase.evento.out.PublicadorEvento;
import org.tallerjava.moduloSeguridad.aplicacion.ServicioSeguridad;

@ApplicationScoped
public class ServicioComercioImpl implements ServicioComercio {

    @Inject
    private RepositorioComercio repositorio;
    @Inject
    private PublicadorEvento publicador;
    @Inject 
    ServicioSeguridad servicioSeguridad;

    @Override
    public Integer altaComercio(Comercio comercio, String password) {

        Integer idComercio = repositorio.guardarComercio(comercio);

        if (idComercio != -1 && servicioSeguridad.altaComercio(comercio.getUsuario(), password)) { 
            //solo si se creo correctamente el comercio y su usuario
            Comercio nuevoComercio = repositorio.buscarPorId(idComercio);
            CuentaBancoComercio nuevaCuentaBanco = nuevoComercio.getCuentaBancoComercio();

            publicador.publicarEventoComercio(
                idComercio, 
                nuevaCuentaBanco.getNumeroCuenta(),
                nuevaCuentaBanco.getId()
                );
        } else {
            repositorio.eliminarComercio(idComercio);
        }
       
        return idComercio;
    }

    @Override
    public boolean modificarDatosComercio(Integer id, String rut, String nombre, String direccion) {
        Comercio comercio = repositorio.buscarPorId(id);
        if (comercio == null) return false;

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

        if (comercio == null) return -1;

        comercio.agregarPos(pos);
        pos.setComercio(comercio);
        boolean resultado = repositorio.actualizarComercio(comercio);

        if (resultado) {
            Comercio comercioActualizado = repositorio.buscarPorId(idComercio);
            Pos nuevoPos = comercioActualizado.buscarPosPorIdentificador(pos.getIdentificador());
            return nuevoPos.getId();
        } else {
            return -1;
        }
    }

    @Override
    public boolean cambiarEstadoPos(Integer idComercio, Integer identificadorPos, boolean estado) {

        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) return false;

        Pos pos = comercio.buscarPosPorId(identificadorPos);

        if (pos == null) return false;

        pos.setHabilitado(estado);
        return repositorio.actualizarComercio(comercio);
    }

    @Override
    public boolean cambioContraseña(Integer idComercio, String nuevaPass) {
        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) return false;
        
        String nombreUsuario = comercio.getUsuario();

        return servicioSeguridad.cambiarPassword(nombreUsuario, nuevaPass);
    }

    @Override
    public Integer realizarReclamo(Integer idComercio, Reclamo reclamo) {
        Comercio comercio = repositorio.buscarPorId(idComercio);

        if (comercio == null) return -1;

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
}