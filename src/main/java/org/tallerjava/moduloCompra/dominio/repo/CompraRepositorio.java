package org.tallerjava.moduloCompra.dominio.repo;

import org.tallerjava.moduloComercio.interfase.evento.out.EventoComercio;
import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;

import jakarta.enterprise.event.Observes;

public interface CompraRepositorio {
    Integer guardarCompra(Compra compra);
    double obtenerMontoVentasDiaActual(Integer idComercio);
    Integer guardarComercio(@Observes EventoComercio eventoComercio);
    boolean actualizarComercio(Comercio comercio);
    Comercio buscarPorId(Integer id);
    Integer traerIdCompra(Integer id);
}