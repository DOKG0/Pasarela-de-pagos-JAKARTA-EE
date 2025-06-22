package org.tallerjava.servicioExterno.datatypes;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class DTOPago {

    @Schema(name = "nroTarjeta", example = "12341234", requiredMode = RequiredMode.REQUIRED)
    private Integer nroTarjeta;

    @Schema(name = "marcaTarjeta", example = "VISA", requiredMode = RequiredMode.REQUIRED)
    private String marcaTarjeta;

    @Schema(name = "fechaVtoTarjeta", example = "2029-12-12", requiredMode = RequiredMode.REQUIRED)
    private String fechaVtoTarjeta;
}
