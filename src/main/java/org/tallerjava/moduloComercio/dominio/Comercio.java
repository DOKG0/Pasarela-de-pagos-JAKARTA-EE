package org.tallerjava.moduloComercio.dominio;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="comercio_MOD_COMERCIO")
@Table(name="comercio_MOD_COMERCIO")
public class Comercio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String usuario;
    private String nombre;
    private String rut;
    private String direccion;

    @OneToMany(
        mappedBy = "comercio",
        orphanRemoval = true, 
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    private List<Pos> poses = new ArrayList<Pos>();

    @OneToMany(
        orphanRemoval = true,
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    @JoinColumn(name="id_comercio")
    private List<Reclamo> reclamos = new ArrayList<Reclamo>();

    @OneToOne(
        orphanRemoval = true,
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL)
    private CuentaBancoComercio cuentaBancoComercio;


    public Comercio(String rut, String nombre, String direccion) {
        this.nombre = nombre;
        this.rut = rut;
        this.direccion = direccion;

    }

    public void agregarPos(Pos pos) {
        this.poses.add(pos);
    }

    public void agregarReclamo(Reclamo reclamo) {
        this.reclamos.add(reclamo);
    }

    public Pos buscarPosPorId(Integer id) {
        for (Pos pos : this.poses) {
            if (pos.getId().equals(id)) {
                return pos;
            }
        }
        return null;
    }

    public Pos buscarPosPorIdentificador(String identificadorPos) {
        for (Pos pos : this.poses) {
            if (pos.getIdentificador().equals(identificadorPos)) {
                return pos;
            }
        }
        return null;
    }
    
}