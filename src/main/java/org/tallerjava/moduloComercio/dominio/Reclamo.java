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
@Table(name="reclamo_MOD_COMERCIO")
public class Reclamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String texto;
    private LocalDateTime fecha;
    private EstadoReclamo estado;
    private CategoriaReclamo categoria;

    public Reclamo(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto del reclamo no puede estar vacío");
        }
        this.texto = texto;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoReclamo.PENDIENTE;
         /*
         * Los reclamos se crean con categoria sin definir, 
         * pero se les asigna de forma asincronica con la cola de mensajes
         */
        this.categoria = CategoriaReclamo.SIN_DEFINIR; 
    }
}