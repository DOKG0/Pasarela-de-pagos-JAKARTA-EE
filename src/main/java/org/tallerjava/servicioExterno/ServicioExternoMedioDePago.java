package org.tallerjava.servicioExterno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.tallerjava.servicioExterno.datatypes.Marcas;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServicioExternoMedioDePago {
    List<String> marcasAceptadas = Arrays.stream(Marcas.values())
                                         .map(Enum::name)
                                         .collect(Collectors.toList());


    public boolean procesarPago(String marcaTarjeta) {

        boolean resultado = true;
        if (!marcasAceptadas.contains(marcaTarjeta.toUpperCase())) {
            resultado = false;
        }
        
        
        return resultado;
    }
}
