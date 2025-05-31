package org.tallerjava.servicioExterno;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
@ApplicationScoped

public class ServicioRepositorio {
    
    @PersistenceContext
    private EntityManager em;


    public Integer traerIdTransferencia(){
        try {
            String query = "SELECT MAX(t.id) FROM Transferencia t";
            Integer idTransferencia = (Integer) em.createQuery(query).getSingleResult();
            return idTransferencia;
        } catch (Exception e) {
            // Manejo de excepciones
            return null;
        }
    }
}
