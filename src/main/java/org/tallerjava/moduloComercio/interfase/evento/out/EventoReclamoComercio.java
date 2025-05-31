package org.tallerjava.moduloComercio.interfase.evento.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoReclamoComercio {
   private Integer idComercio;
   private Integer idReclamo;
}
