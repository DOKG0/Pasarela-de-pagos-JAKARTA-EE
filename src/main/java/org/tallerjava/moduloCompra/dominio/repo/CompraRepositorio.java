package org.tallerjava.moduloCompra.dominio.repo;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.ResumenVentas;
import java.time.LocalDate;

public interface CompraRepositorio {
    void guardarCompra(Compra compra);
    ResumenVentas obtenerResumenVentas(Comercio comercio, LocalDate fechaInicio, LocalDate fechaFin);
    double obtenerMontoVentasDiaActual(Comercio comercio);
}