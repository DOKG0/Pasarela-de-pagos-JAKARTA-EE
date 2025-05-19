package org.tallerjava.moduloCompra.dominio;
import java.time.LocalDateTime;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOCompra;

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
@Table(name="compra_MOD_COMPRA")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double monto;
    private LocalDateTime fecha;
    private EstadoCompra estado;
    
    //private String codigoAutorizacion;
    
    public Compra(double monto) {
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoCompra.PENDIENTE;
    }
    
    public DTOCompra getDTOCompra() {
        return new DTOCompra(
            this.getId(),
            this.getMonto(),
            this.getFecha().toString(),
            this.getEstado());
    }
}