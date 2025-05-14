package org.tallerjava.moduloComercio.aplicacion.impl;

import org.jboss.logging.annotations.Pos;
import org.tallerjava.moduloComercio.dominio.Comercio;

public class ServicioComercioImpl {
     
    public Integer altaComercio(Comercio datosComercio) {
        return null;
    }

    public boolean modificarDatosComercio(Comercio datosComercio) {
        return false;
    }

    public Integer altaPos(Comercio comercio, Pos pos) {
        return null;
    }

    public boolean cambiarEstadoPos(Comercio comercio, Pos pos, boolean estado) {
        return false;
    }

    public boolean cambioContraseña(String nuevaPass) {
        return false;
    }

    public Integer realizarReclamo(String textoReclamo) {
        return null;
    }
}
