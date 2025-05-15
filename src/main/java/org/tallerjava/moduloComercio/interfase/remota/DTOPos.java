package org.tallerjava.moduloComercio.interfase.remota;

import org.tallerjava.moduloComercio.dominio.Pos;

import lombok.Data;

@Data
public class DTOPos {
    private Integer id;
    private String identificador;
    private boolean habilitado;
    private Integer idComercio;

    public Pos buildPos() {
        Pos pos = new Pos();
        pos.setIdentificador(identificador);
        return pos;
    }
}
