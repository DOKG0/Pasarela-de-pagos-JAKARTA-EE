package org.tallerjava.moduloSeguridad.infraestructura.persistencia;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloSeguridad.dominio.Grupo;
import org.tallerjava.moduloSeguridad.dominio.Usuario;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class RepositorioSeguridadImpl implements RepositorioSeguridad {
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public Usuario buscarUsuario(String nombreUsuario) {
        return em.find(Usuario.class, nombreUsuario);
    }

    @Override
    public Grupo buscarGrupo(String nombre) {
        return em.find(Grupo.class, nombre);
    }

    @Override
    public boolean guardarUsuario(String nombreUsuario, String passwordHash, Grupo grupo) {
        try {
            if (buscarUsuario(nombreUsuario) == null) {
                Usuario usuario = new Usuario(nombreUsuario, passwordHash, grupo);
                em.persist(usuario);
                em.flush();
                return true;
            } else {
                System.err.println("Ya existe un usuario con el nombre " + nombreUsuario);
                return false;
            }
        } catch (Exception e) {
           System.err.println("Error al guardar el usuario " + nombreUsuario);
           System.err.println(e.getMessage());
           return false;
        }
    }

    @Override
    public boolean cambiarPassword(String nombreUsuario, String passwordHash) {
        Usuario usuario = buscarUsuario(nombreUsuario);
        if (usuario == null) return false;

        usuario.setPasswordHash(passwordHash);

        try {
            em.merge(usuario);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
