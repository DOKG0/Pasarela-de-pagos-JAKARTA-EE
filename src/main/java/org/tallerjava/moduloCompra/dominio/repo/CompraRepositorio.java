package org.tallerjava.moduloCompra.dominio.repo;

import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;

public interface CompraRepositorio {
    Integer guardarCompra(Compra compra);
    double obtenerMontoVentasDiaActual(Integer idComercio);
    Integer guardarComercio(Integer idComercio, String nroCuentaBancoComercio, Integer idCuentaBanco, String usuarioComercio);
    boolean actualizarComercio(Comercio comercio);
    Comercio buscarPorId(Integer id);
    Integer traerIdCompra(Integer id);
    Comercio buscarPorIdConBloqueo(Integer id);
}