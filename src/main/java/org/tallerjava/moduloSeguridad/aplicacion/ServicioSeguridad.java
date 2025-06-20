package org.tallerjava.moduloSeguridad.aplicacion;

public interface ServicioSeguridad {
    public boolean altaComercio(String nombreUsuario, String password);
    public boolean cambiarPassword(String nombreUsuario, String password);
}
