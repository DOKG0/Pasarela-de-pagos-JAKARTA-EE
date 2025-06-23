package org.tallerjava.moduloCompra.infraestructura.persistencia;

import org.tallerjava.moduloCompra.dominio.CuentaBancoComercio;
import org.jboss.logging.Logger;
import org.tallerjava.moduloCompra.aplicacion.impl.ServicioCompraImpl;
import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.repo.CompraRepositorio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CompraRepositorioImpl implements CompraRepositorio {

    private static final Logger LOG = Logger.getLogger(CompraRepositorioImpl.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Override
    public Integer guardarCompra(Compra compra) {
        try {
            if (compra.getId() == null) {
                em.persist(compra);
                em.flush(); 
                return compra.getId();
            } else {
                Compra merged = em.merge(compra);
                return merged.getId();
            }
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public double obtenerMontoVentasDiaActual(Integer idComercio) {
        return 0;
    }

    @Override
    public Integer guardarComercio(
        Integer idComercio, 
        String nroCuentaBancoComercio, 
        Integer idCuentaBanco, 
        String usuarioComercio) {

        Comercio comercio = new Comercio();
        comercio.setId(idComercio);
        comercio.setUsuario(usuarioComercio);

        CuentaBancoComercio cuentaBancoComercio = new CuentaBancoComercio();
        cuentaBancoComercio.setId(idCuentaBanco);
        cuentaBancoComercio.setNumeroCuenta(nroCuentaBancoComercio);

        comercio.setCuentaBanco(cuentaBancoComercio);;

        try {
            if (comercio.getId() == null) {
                return -1; //la id del comercio debe venir seteada ya que debe ser la misma utilizada en el otro modulo
            } else {
                em.persist(comercio);
                em.flush(); 
                return comercio.getId();
            }
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public boolean actualizarComercio(Comercio comercio) {
        try {
            em.merge(comercio);
            return true;
        } catch (Exception e) {
            LOG.error("Error al actualizar comercio", e);
            return false;
        }
    }

    @Override
    public Comercio buscarPorId(Integer id) {
        try {
            return em.find(Comercio.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Comercio buscarPorIdConBloqueo(Integer id) {
        try {
            return em.find(Comercio.class, id, LockModeType.PESSIMISTIC_WRITE);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public Integer traerIdCompra(Integer id) {
        try {
            Comercio comercio = buscarPorId(id);
            return comercio.getCompras().get(comercio.getCompras().size()-1).getId();
        } catch (Exception e) {
            return null;
        }
    }
}