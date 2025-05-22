package org.tallerjava.moduloComercio.datatypes;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class DTOEstadoPos {
    @Schema(name = "estado", example = "true", requiredMode = RequiredMode.REQUIRED)
    private boolean estado;
}