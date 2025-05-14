package org.tallerjava.moduloCompra.aplicacion;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.ResumenVentas;
import org.tallerjava.moduloCompra.dominio.Venta;
import java.time.LocalDate;

public interface ServicioCompra {

    public Boolean procesarPago(Compra datosCompra);
    public ResumenVentas resumenVentasDiarias(Comercio comercio);
    public ResumenVentas resumenVentasPorPeriodo(Comercio comercio, LocalDate fechaInicio, LocalDate fechaFin);
    public double montoActualVendido(Comercio comercio);

}
