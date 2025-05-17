package org.tallerjava.moduloComercio.datatypes;

import org.tallerjava.moduloComercio.dominio.Reclamo;

import lombok.Data;

@Data
public class DTOReclamo {
    private String contenidoReclamo;

    public Reclamo buildReclamo() {
        return new Reclamo(contenidoReclamo);
    }
}
