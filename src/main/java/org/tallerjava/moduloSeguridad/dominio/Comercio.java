package org.tallerjava.moduloSeguridad.dominio;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="comercio_MOD_SEGURIDAD")
@Table(name="comercio_MOD_SEGURIDAD")
public class Comercio {
    @Id
    private Integer id;

    @OneToOne(
        orphanRemoval = true,
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    private Usuario usuario;
}
