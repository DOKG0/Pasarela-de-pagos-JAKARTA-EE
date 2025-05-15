package org.tallerjava.moduloComercio.dominio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pos {
    private Integer id;
    private String identificador;
    private boolean habilitado;
    private Comercio comercio;
}