package org.tallerjava.moduloTransferencia.datatypes;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;
@Data
public class DTONotificacionTransferencia {

    @Schema(name = "nroCuentaBancoComercio", example = "12345678", requiredMode = RequiredMode.REQUIRED)
    private String nroCuentaBancoComercio;

    @Schema(name = "idComercio", example = "1", requiredMode = RequiredMode.REQUIRED)
    private Integer idComercio;

    @Schema(name = "monto", example = "199.99", requiredMode = RequiredMode.REQUIRED)
    private double monto;
    
    @Schema(name = "codigoTransaccion", example = "t512439", requiredMode = RequiredMode.REQUIRED)
    private String codigoTransaccion;

}
