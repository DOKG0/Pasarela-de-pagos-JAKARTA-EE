package org.tallerjava.moduloComercio.dominio.repo;

import org.tallerjava.moduloComercio.dominio.Comercio;

public interface RepositorioComercio {
    Integer guardarComercio(Comercio comercio);
    boolean actualizarComercio(Comercio comercio);
    Comercio buscarPorId(Integer id);
    boolean eliminarComercio(Integer idComercio);
}