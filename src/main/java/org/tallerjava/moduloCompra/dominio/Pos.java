package org.tallerjava.moduloCompra.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="pos_MOD_COMPRA")
@Table(name="pos_MOD_COMPRA")
public class Pos {
    @Id
    private Integer id;
    private String identificador;
    private boolean habilitado = true;
    @ManyToOne
    private Comercio comercio;
}
