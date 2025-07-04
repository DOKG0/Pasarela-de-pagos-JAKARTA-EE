package org.tallerjava.moduloCompra.aplicacion;

import org.tallerjava.moduloCompra.dominio.EstadoCompra;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOResumenVentas;

import java.time.LocalDateTime;

public interface ServicioCompra {

    public boolean ingresarNuevaCompra(Integer idComercio, double importe, boolean resultado, Integer idPos);
    public DTOResumenVentas resumenVentasPorPeriodo(Integer idComercio, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    public DTOResumenVentas resumenVentasPorEstado(Integer idComercio, EstadoCompra estado);
    public double montoActualVendido(Integer idComercio);
}
