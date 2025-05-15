package org.tallerjava.moduloComercio.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.repo.RepositorioComercio;

@ApplicationScoped
@Transactional
public class ComercioRepositorioImpl implements RepositorioComercio {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Integer guardarComercio(Comercio comercio) {
        try {
            if (comercio.getId() == null) {
                em.persist(comercio);
                em.flush(); 
                return comercio.getId();
            } else {
                Comercio merged = em.merge(comercio);
                return merged.getId();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar comercio", e);
        }
    }

    @Override
    public boolean actualizarComercio(Comercio comercio) {
        try {
            Comercio comercioExistente = buscarPorId(comercio.getId());
            if (comercioExistente != null) {
                em.merge(comercio);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar comercio", e);
        }
    }

    @Override
    public Comercio buscarPorId(Integer id) {
        try {
            return em.find(Comercio.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar comercio por ID", e);
        }
    }
}