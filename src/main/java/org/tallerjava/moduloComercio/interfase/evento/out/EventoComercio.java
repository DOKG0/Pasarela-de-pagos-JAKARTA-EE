package org.tallerjava.moduloComercio.interfase.evento.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoComercio {
    private Integer idComercio;
    private String nroCuentaBancoComercio;
    private Integer idCuentaBanco;
}
