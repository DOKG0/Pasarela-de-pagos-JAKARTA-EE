package org.tallerjava.moduloComercio.aplicacion;

import org.tallerjava.moduloComercio.dominio.CategoriaReclamo;
import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.Pos;
import org.tallerjava.moduloComercio.dominio.Reclamo;

public interface ServicioComercio {
    public Integer altaComercio(Comercio comercio, String password);
    public boolean modificarDatosComercio(Integer id, String rut, String nombre, String direccion);
    public Integer altaPos(Integer idComercio, Pos pos);
    public boolean cambiarEstadoPos(Integer idComercio, Integer identificadorPos, boolean estado);
    public boolean cambioContraseña(Integer idComercio, String nuevaPass);
    public Integer realizarReclamo(Integer idComercio, Reclamo reclamo);
    public boolean categorizarReclamo(Integer idComercio, Integer idReclamo, CategoriaReclamo categoria);
}