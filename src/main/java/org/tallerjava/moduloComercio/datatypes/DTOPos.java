package org.tallerjava.moduloComercio.datatypes;

import org.tallerjava.moduloComercio.dominio.Pos;

import lombok.Data;

@Data
public class DTOPos {
    private Integer id;
    private String identificador;
    private boolean habilitado;

    public Pos buildPos() {
        Pos pos = new Pos();
        pos.setIdentificador(identificador);
        return pos;
    }
}
