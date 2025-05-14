package org.tallerjava.moduloCompra.dominio;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tarjeta {
    private Integer nro;
    private String marca;
    private LocalDate fechaVto;
}
