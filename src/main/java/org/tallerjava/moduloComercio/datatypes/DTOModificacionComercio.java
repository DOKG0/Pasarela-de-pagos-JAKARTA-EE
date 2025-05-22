package org.tallerjava.moduloComercio.datatypes;

import org.tallerjava.moduloComercio.dominio.Comercio;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class DTOModificacionComercio {
    @Schema(name = "nombre", example = "Mi nuevo comercio", requiredMode = RequiredMode.NOT_REQUIRED)
    private String nombre;
    @Schema(name = "rut", example = "123412341234", requiredMode = RequiredMode.NOT_REQUIRED)
    private String rut;
    @Schema(name = "direccion", example = "25 de Agosto 999", requiredMode = RequiredMode.NOT_REQUIRED)
    private String direccion;

    public Comercio buildComercio() {
        Comercio comercio = new Comercio();
        comercio.setNombre(nombre);
        comercio.setDireccion(direccion);
        comercio.setRut(rut);

        return comercio;
    }
}
