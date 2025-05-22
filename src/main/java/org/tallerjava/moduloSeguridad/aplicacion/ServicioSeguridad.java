package org.tallerjava.moduloSeguridad.aplicacion;

public interface ServicioSeguridad {
    boolean altaComercio(String nombreUsuario, String password);
    boolean cambiarPassword(String nombreUsuario, String password);
}
