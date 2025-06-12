package org.tallerjava.moduloComercio.interfase.evento.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoAltaPos {
    private Integer id;
    private String identificador;
    private boolean habilitado;
    private Integer idComercio;
}
