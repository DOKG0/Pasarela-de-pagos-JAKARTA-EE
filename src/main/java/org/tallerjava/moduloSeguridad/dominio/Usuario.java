package org.tallerjava.moduloSeguridad.dominio;

import java.util.HashSet;
import java.util.Set;

import org.tallerjava.moduloSeguridad.infraestructura.seguridad.HashFunctionUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Usuario_MOD_SEGURIDAD")
@Table(name = "Usuario_MOD_SEGURIDAD")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    private String usuario;
    private String passwordHash;

    @ManyToOne(
        fetch = FetchType.EAGER
    )
    private Grupo grupo;

    public boolean passwordCorrecta(String pass) {
		return HashFunctionUtil.convertToHash(pass).equals(passwordHash);
	}

    public Set<String> obtenerGrupoComoSet() {
        Set<String> grupos = new HashSet<String>();
        grupos.add(this.getGrupo().getNombre());

        return grupos;
    }
}
