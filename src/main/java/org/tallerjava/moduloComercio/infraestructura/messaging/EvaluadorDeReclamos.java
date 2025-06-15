package org.tallerjava.moduloComercio.infraestructura.messaging;

import java.util.Random;

import org.tallerjava.moduloComercio.dominio.CategoriaReclamo;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EvaluadorDeReclamos {

    //Esta funcion es la que deberia de cambiarse por una llamada a la IA elegida si se implementara
    public CategoriaReclamo evaluarReclamo(String textoReclamo) {
        //Hallo un numero aleatorio entre 1 y el numero total de categorias de reclamos existentes 
        //(sin contar la ultima categoria SIN_DEFINIR)
        int MAX_NUMBER = CategoriaReclamo.values().length - 1;
        Random random =  new Random();
        int randomNumber = random.nextInt(MAX_NUMBER) + 1;

        //obtengo una categoria aleatoria segun el numero aleatorio calculado
        CategoriaReclamo categoria = CategoriaReclamo.getById(randomNumber);

        return categoria;
    }
}
