package org.tallerjava.moduloSeguridad.aplicacion;

import org.tallerjava.moduloSeguridad.dominio.Grupo;
import org.tallerjava.moduloSeguridad.infraestructura.persistencia.RepositorioSeguridad;
import org.tallerjava.moduloSeguridad.infraestructura.seguridad.HashFunctionUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServicioSeguridadImpl implements ServicioSeguridad {
    
    @Inject
    RepositorioSeguridad repositorio;

    @Override
    public boolean altaComercio(String nombreUsuario, String password) {
        
        Grupo grupoComercio = repositorio.buscarGrupo("comercio");
        if (grupoComercio == null) {
            return false;
        }

        String passwordHash = HashFunctionUtil.convertToHash(password);
        return repositorio.guardarUsuario(nombreUsuario, passwordHash, grupoComercio);
    }

    @Override
    public boolean cambiarPassword(String nombreUsuario, String password) {
        String nuevaPasswordHash = HashFunctionUtil.convertToHash(password);
        return repositorio.cambiarPassword(nombreUsuario, nuevaPasswordHash);
    }

}
