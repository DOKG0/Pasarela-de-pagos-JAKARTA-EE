package org.tallerjava.moduloCompra.aplicacion;

import org.tallerjava.moduloCompra.dominio.Comercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;

import java.time.LocalDateTime;

public interface ServicioCompra {

    public boolean procesarPago(Compra datosCompra);
    public DTOResumenVentas resumenVentasDiarias(Integer idComercio);
    public DTOResumenVentas resumenVentasPorPeriodo(Integer idComercio, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    public DTOResumenVentas resumenVentasPorEstado(Integer idComercio, EstadoCompra estado);
    public double montoActualVendido(Comercio comercio);
}
