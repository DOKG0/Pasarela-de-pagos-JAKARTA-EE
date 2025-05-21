package org.tallerjava.moduloComercio.datatypes;

import org.tallerjava.moduloComercio.dominio.Pos;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class DTOPos {
    @Schema(hidden = true)
    private Integer id;
    @Schema(name = "identificador", example = "mipos1", requiredMode = RequiredMode.REQUIRED)
    private String identificador;
    @Schema(name = "habilitado", example = "true", requiredMode = RequiredMode.NOT_REQUIRED)
    private boolean habilitado;

    public Pos buildPos() {
        Pos pos = new Pos();
        pos.setIdentificador(identificador);
        return pos;
    }
}
