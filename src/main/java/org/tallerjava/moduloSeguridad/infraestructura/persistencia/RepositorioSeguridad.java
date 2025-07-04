package org.tallerjava.moduloSeguridad.infraestructura.persistencia;

import org.tallerjava.moduloSeguridad.dominio.Grupo;
import org.tallerjava.moduloSeguridad.dominio.Usuario;

public interface RepositorioSeguridad {
    Usuario buscarUsuario(String nombreUsuario);
    boolean guardarUsuario(String nombreUsuario, String passwordHash, Grupo grupo);
    boolean cambiarPassword(String nombreUsuario, String passwordHash);
    Grupo buscarGrupo(String nombre);
}
