package org.tallerjava.moduloTransferencia.infraestructura.persistencia;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.tallerjava.moduloTransferencia.dominio.Comercio;
import org.tallerjava.moduloTransferencia.dominio.Deposito;
import org.tallerjava.moduloTransferencia.dominio.Transferencia;
import org.tallerjava.moduloTransferencia.dominio.repo.TransferenciaRepositorio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class TransferenciaRepositorioImpl implements TransferenciaRepositorio{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Integer guardarTransferencia(Transferencia transferencia) {
        try {
            if (transferencia.getId() == null) {
                em.persist(transferencia);
                em.flush(); 
                return transferencia.getId();
            } else {
                Transferencia merged = em.merge(transferencia);
                return merged.getId();
            }
        } catch (Exception e) {
            // throw new RuntimeException("Error al guardar comercio", e);
            return -1;
        }
    }

    @Override
    public Integer guardarDeposito(Deposito deposito) {
        try {
            if (deposito.getId() == null) {
                em.persist(deposito);
                em.flush(); 
                return deposito.getId();
            } else {
                Deposito merged = em.merge(deposito);
                return merged.getId();
            }
        } catch (Exception e) {
            // throw new RuntimeException("Error al guardar comercio", e);
            return -1;
        }
    }

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
            // throw new RuntimeException("Error al guardar comercio", e);
            return -1;
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
            // throw new RuntimeException("Error al actualizar comercio", e);
            return false;
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

    @Override
    public List<Deposito> buscarDepositosPorComercioYFecha(Integer idComercio, LocalDate fechaInicio,
        LocalDate fechaFin) {
        Comercio comercio = buscarPorId(idComercio);
        
        List<Deposito> depositos = comercio.getDepositos();
        List<Deposito> depositosFiltrados = new ArrayList<>();

        for (Deposito d : depositos) { 
            LocalDate fechaDeposito = LocalDate.parse(d.getFecha());

            if (
                (fechaDeposito.isEqual(fechaInicio) || fechaDeposito.isAfter(fechaInicio)) &&
                (fechaDeposito.isEqual(fechaFin) || fechaDeposito.isBefore(fechaFin)) &&
                d.getComercioId().equals(idComercio)
            ) {
                depositosFiltrados.add(d);
            }
        }
        return depositosFiltrados;
    }

    @Override
    public BigDecimal sumaTotalTransferencia() {
        return em.createQuery(
            "SELECT COALESCE(SUM(t.monto), 0) FROM Transferencia t", 
            BigDecimal.class)
            .getSingleResult();
    }

      @Override
        public BigDecimal sumaTotalNetoDeposito() {
        return em.createQuery(
            "SELECT COALESCE(SUM(d.montoNeto), 0) FROM Deposito d", 
            BigDecimal.class)
            .getSingleResult();
    }

    @Override
    public List<Deposito> traerDepositos() {
        return em.createQuery(
            "SELECT d FROM Deposito d ORDER BY d.fecha DESC", 
            Deposito.class)
            .getResultList();
    }

    @Override
    public List<Transferencia> traerTransferencias() {
        return em.createQuery(
            "SELECT t FROM Transferencia t ORDER BY t.fecha DESC", 
            Transferencia.class)
            .getResultList();
    }
}
