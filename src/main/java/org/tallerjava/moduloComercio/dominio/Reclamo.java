package org.tallerjava.moduloComercio.dominio;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reclamo {
    private Integer id;
    private String texto;
    private LocalDateTime fecha;
    private EstadoReclamo estado;

    public Reclamo(String texto) {
            this.texto = texto;
            this.fecha = LocalDateTime.now();
            this.estado = EstadoReclamo.PENDIENTE;
        }
}