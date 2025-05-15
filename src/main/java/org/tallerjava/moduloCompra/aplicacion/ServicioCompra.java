package org.tallerjava.moduloCompra.aplicacion;

import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.ResumenVentas;
import java.time.LocalDate;

public interface ServicioCompra {

    public boolean procesarPago(Compra datosCompra);
    public ResumenVentas resumenVentasDiarias(Comercio comercio);
    public ResumenVentas resumenVentasPorPeriodo(Comercio comercio, LocalDate fechaInicio, LocalDate fechaFin);
    public double montoActualVendido(Comercio comercio);

}
