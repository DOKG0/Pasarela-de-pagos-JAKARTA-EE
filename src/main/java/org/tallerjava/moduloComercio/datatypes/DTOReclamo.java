package org.tallerjava.moduloComercio.datatypes;

import org.tallerjava.moduloComercio.dominio.Reclamo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class DTOReclamo {
    @Schema(name = "contenidoReclamo", example = "No puedo ver mis compras", requiredMode = RequiredMode.REQUIRED)
    private String contenidoReclamo;

    public Reclamo buildReclamo() {
        return new Reclamo(contenidoReclamo);
    }
}
