package org.tallerjava.moduloComercio.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="pos_MOD_COMERCIO")
@Table(name="pos_MOD_COMERCIO")
public class Pos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String identificador;
    private boolean habilitado = true; //cuando se crea el Pos se inicializa como habilitado -> sujeto a cambios
    @ManyToOne
    private Comercio comercio;


    public boolean isHabilitado() {
        if (habilitado) {
            return true;
        }
        return false;
    }
}