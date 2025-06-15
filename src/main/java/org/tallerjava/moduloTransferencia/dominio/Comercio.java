package org.tallerjava.moduloTransferencia.dominio;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="comercio_MOD_TRANSFERENCIA")
@Table(name="comercio_MOD_TRANSFERENCIA")
public class Comercio {
    @Id
    Integer id;

    @OneToMany(
        orphanRemoval = true, 
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    @JoinColumn(name = "id_comercio")
    List<Deposito> depositos = new ArrayList<Deposito>();
}
