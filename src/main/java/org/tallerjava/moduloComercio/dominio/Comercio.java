package org.tallerjava.moduloComercio.dominio;


import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comercio {
    private Integer id;
    private String nombre;
    private String rut;
    private String direccion;
    private String contraseña;
    private List<Pos> poses = new ArrayList<>();
    private List<Reclamo> reclamos = new ArrayList<>();


    // Métodos de negocio
    public void agregarPos(Pos pos) {
        this.poses.add(pos);
    }

    public void agregarReclamo(Reclamo reclamo) {
        this.reclamos.add(reclamo);
    }

    
}