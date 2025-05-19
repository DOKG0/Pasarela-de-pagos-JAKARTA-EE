package org.tallerjava.moduloCompra.dominio.datatypes;

import java.time.LocalDateTime;

import org.tallerjava.moduloCompra.dominio.EstadoCompra;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DTOCompra {
    private Integer id;
    private double monto;
    private String fecha;
    private EstadoCompra estado;
}
