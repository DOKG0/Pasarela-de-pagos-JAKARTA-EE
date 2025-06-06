package org.tallerjava.servicioExterno.datatypes;

import lombok.Data;

@Data
public class DTOPago {
    private Integer nroTarjeta;
    private String marcaTarjeta;
    private String fechaVtoTarjeta;
}
