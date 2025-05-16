package org.tallerjava.moduloComercio.dominio;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="reclamo")
public class Reclamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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