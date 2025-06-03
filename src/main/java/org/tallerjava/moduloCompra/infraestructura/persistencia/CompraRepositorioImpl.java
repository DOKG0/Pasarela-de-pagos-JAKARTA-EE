package org.tallerjava.moduloCompra.infraestructura.persistencia;

import org.tallerjava.moduloCompra.dominio.CuentaBancoComercio;
import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.repo.CompraRepositorio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CompraRepositorioImpl implements CompraRepositorio {

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
            Comercio comercioExistente = buscarPorId(comercio.getId());
            if (comercioExistente != null) {
                em.merge(comercio);
                return true;
            }
            return false;
        } catch (Exception e) {
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

}