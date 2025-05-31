package org.tallerjava.moduloCompra.interfase.evento.out;

import org.tallerjava.moduloCompra.dominio.EstadoCompra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoPago {
    private Integer idComercio;
    private Integer idCompra;
    private EstadoCompra estadoCompra;
}
