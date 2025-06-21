package org.tallerjava.moduloComercio.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.tallerjava.moduloComercio.aplicacion.ServicioComercio;
import org.tallerjava.moduloComercio.dominio.CategoriaReclamo;
import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.CuentaBancoComercio;
import org.tallerjava.moduloComercio.dominio.Pos;
import org.tallerjava.moduloComercio.dominio.Reclamo;
import org.tallerjava.moduloComercio.dominio.repo.RepositorioComercio;
import org.tallerjava.moduloComercio.infraestructura.messaging.EmisorMensajeUtil;
import org.tallerjava.moduloComercio.interfase.evento.out.PublicadorEvento;
import org.tallerjava.moduloSeguridad.interfase.local.ServicioSeguridadFacade;

@ApplicationScoped
public class ServicioComercioImpl implements ServicioComercio {

    @Inject
    private RepositorioComercio repositorio;
    @Inject
    private PublicadorEvento publicador;
    @Inject
    private ServicioSeguridadFacade servicioSeguridad;
    @Inject
    private EmisorMensajeUtil emisorMensajeReclamo;

    @Override
    public Integer altaComercio(Comercio comercio, String password) {

        Integer idComercio = repositorio.guardarComercio(comercio);

        //solo si se creo correctamente el comercio y su usuario
        if (idComercio != -1 && servicioSeguridad.altaComercio(comercio.getUsuario(), password)) { 

            Comercio nuevoComercio = repositorio.buscarPorId(idComercio);
            CuentaBancoComercio nuevaCuentaBanco = nuevoComercio.getCuentaBancoComercio();

            publicador.publicarEventoComercio(
                idComercio, 
                nuevaCuentaBanco.getNumeroCuenta(),
                nuevaCuentaBanco.getId(),
                nuevoComercio.getUsuario()
                );
        } else {
            repositorio.eliminarComercio(idComercio);
        }

        System.out.println("[ServicioComercio] Comercio creado con id: " + idComercio);
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

        System.out.println("[ServicioComercio] Comercio con id: " + id + " actualizado" );
        return repositorio.actualizarComercio(comercio);
    }

    @Override
    public Integer altaPos(Integer idComercio, Pos pos) {
        Comercio comercio = repositorio.buscarPorId(idComercio);

        if (comercio == null) return -1;

        Pos posPreexistente = comercio.buscarPosPorIdentificador(pos.getIdentificador());
        if (posPreexistente != null) return -1; //un pos con el mismo nombre ya existe para ese comercio

        comercio.agregarPos(pos);
        pos.setComercio(comercio);
        boolean resultado = repositorio.actualizarComercio(comercio);

        if (resultado) {
            Comercio comercioActualizado = repositorio.buscarPorId(idComercio);
            Pos nuevoPos = comercioActualizado.buscarPosPorIdentificador(pos.getIdentificador());
            Integer idPos = nuevoPos.getId();

            publicador.publicarEventoAltaPos(
                idPos, 
                nuevoPos.getIdentificador(), 
                nuevoPos.isHabilitado(), 
                comercioActualizado.getId()
            );

            System.out.println("[ServicioComercio] Pos creado con id: " + nuevoPos.getId() + " en comercio con id: " + idComercio);
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
        System.out.println("[ServicioComercio] Pos creado con id: " + pos.getId() + " actualizado a estado: " + estado);
        return repositorio.actualizarComercio(comercio);
    }

    @Override
    public boolean cambioContraseña(Integer idComercio, String nuevaPass) {
        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) return false;
        
        String nombreUsuario = comercio.getUsuario();

        System.out.println("[ServicioComercio] Contraseña de comercio con id: " + idComercio + " actualizada" );
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

            publicador.publicarEventoReclamoComercio(
                idComercio, 
                reclamo.getId()
                );
            
            //se envia el reclamo a la cola de mensajes para que sea evaluado de forma asincronica
            emisorMensajeReclamo.enviarMensajeReclamo(idComercio, idNuevoReclamo, reclamo.getTexto());

            System.out.println("[ServicioComercio] Reclamo creado con id: " + idNuevoReclamo + " en comercio con id: " + idComercio); 
            return idNuevoReclamo;
        } else {
            return -1;
        }
    }

    @Override
    public boolean categorizarReclamo(Integer idComercio, Integer idReclamo, CategoriaReclamo categoriaReclamo) {
        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) return false;

        Reclamo reclamo = comercio.buscarReclamoPorId(idReclamo);
        if (reclamo == null) return false;

        reclamo.setCategoria(categoriaReclamo);
        return repositorio.actualizarComercio(comercio);
    }
}