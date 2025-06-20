package org.tallerjava.moduloSeguridad.interfase.local;

import org.tallerjava.moduloSeguridad.aplicacion.ServicioSeguridad;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServicioSeguridadFacade {
    @Inject
    ServicioSeguridad servicioSeguridad;

    public boolean altaComercio(String nombreUsuario, String password) {
        return servicioSeguridad.altaComercio(nombreUsuario, password);
    }
    public boolean cambiarPassword(String nombreUsuario, String password) {
        return servicioSeguridad.cambiarPassword(nombreUsuario, password);
    }
}
