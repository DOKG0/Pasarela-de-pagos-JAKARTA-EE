package org.tallerjava.moduloCompra.dominio.datatypes;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class DTOResumenVentas {
    private Integer idComercio;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private int cantidadVentas;
    private double montoTotal;
    private List<DTOCompra> ventas;
}
