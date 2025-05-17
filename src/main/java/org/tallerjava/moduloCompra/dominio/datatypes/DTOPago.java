package org.tallerjava.moduloCompra.dominio.datatypes;

import lombok.Data;

@Data
public class DTOPago {
    private Integer nroTarjeta;
    private String marcaTarjeta;
    private String fechaVtoTarjeta;
    private double importe;
}
