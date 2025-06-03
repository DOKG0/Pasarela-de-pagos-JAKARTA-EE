package org.tallerjava.moduloComercio.interfase.evento.out;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoModificacionPos {
    private Integer idPos;
    private Integer idComercio;
    private boolean estadoPos;
}
