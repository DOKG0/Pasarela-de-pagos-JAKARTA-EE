package org.tallerjava.moduloCompra.dominio.datatypes;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class DTOTransferencia {
    @Schema(name = "nroCuentaBancoComercio", example = "1234678", requiredMode = RequiredMode.REQUIRED)
    private String nroCuentaBancoComercio;

    @Schema(name = "idPos", example = "1", requiredMode = RequiredMode.REQUIRED)
    private Integer idPos;

    @Schema(name = "idComercio", example = "1", requiredMode = RequiredMode.REQUIRED)
    private Integer idComercio;

    @Schema(name = "monto", example = "599.99", requiredMode = RequiredMode.REQUIRED)
    private double monto;
    
    @Schema(name = "dtoPago", requiredMode = RequiredMode.REQUIRED, implementation = DTOPago.class)
    private DTOPago dtoPago;
}
