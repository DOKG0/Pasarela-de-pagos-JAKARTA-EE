package org.tallerjava.moduloComercio.dominio;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Entity
@Table(name="comercio")
public class Comercio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String rut;
    private String direccion;
    private String contraseña;

    @OneToMany(
        mappedBy = "comercio",
        orphanRemoval = true, 
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    private List<Pos> poses = new ArrayList<Pos>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name="id_comercio")
    private List<Reclamo> reclamos = new ArrayList<Reclamo>();

    public void agregarPos(Pos pos) {
        this.poses.add(pos);
    }

    public void agregarReclamo(Reclamo reclamo) {
        this.reclamos.add(reclamo);
    }

    
}