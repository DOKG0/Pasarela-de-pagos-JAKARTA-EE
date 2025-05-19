package org.tallerjava.moduloCompra.dominio.datatypes;

import java.util.List;

import lombok.Data;

@Data
public class DTOResumenVentas {
    private Integer idComercio;
    private String fechaInicio;
    private String fechaFin;
    private int cantidadVentas;
    private double montoTotal;
    private List<DTOCompra> ventas;
}
