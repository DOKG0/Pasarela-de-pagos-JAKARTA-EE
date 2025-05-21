package org.tallerjava.moduloComercio.datatypes;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class DTOPassword {
    @Schema(name = "passwordNueva", example = "nuevapass12", requiredMode = RequiredMode.REQUIRED)
    private String passwordNueva;
}
