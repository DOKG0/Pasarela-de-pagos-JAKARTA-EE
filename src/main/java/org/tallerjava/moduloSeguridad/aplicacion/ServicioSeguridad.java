package org.tallerjava.moduloSeguridad.aplicacion;

public interface ServicioSeguridad {
    boolean altaUsuario(String nombreUsuario, String password);
    boolean altaUsuarioComercio(String nombreUsuario, String password, Integer idComercio);
    boolean cambiarPassword(String nombreUsuario, String password);
}
