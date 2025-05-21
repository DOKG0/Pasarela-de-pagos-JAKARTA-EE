package org.tallerjava.moduloSeguridad.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity(name = "Grupo_MOD_SEGURIDAD")
@Table(name = "Grupo_MOD_SEGURIDAD")
@Data
public class Grupo {
    @Id
    private String nombre;
}
