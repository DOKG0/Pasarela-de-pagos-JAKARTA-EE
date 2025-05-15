package org.tallerjava.moduloComercio.aplicacion;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.Pos;

public interface ServicioComercio {
    // Comentario de prueba para confirmar cambios
    public Integer altaComercio(Comercio comercio);
    public boolean modificarDatosComercio(Comercio datosComercio);
    public Integer altaPos(Integer idComercio, Pos pos);
    public boolean cambiarEstadoPos(Comercio comercio, Pos pos, boolean estado);
    public boolean cambioContraseña(String nuevaPass);
    public Integer realizarReclamo(String textoReclamo);
    public boolean realizarPago(double importe);
}