package org.tallerjava.moduloComercio.aplicacion;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.Pos;

public interface ServicioComercio {
    
    public Integer altaComercio(Comercio datosComercio);
    public boolean modificarDatosComercio(Comercio datosComercio);
    public Integer altaPos(Comercio comercio, Pos pos);
    public boolean cambiarEstadoPos(Comercio comercio, Pos pos, boolean estado);
    public boolean cambioContraseña(String nuevaPass);
    public Integer realizarReclamo(String textoReclamo);
}