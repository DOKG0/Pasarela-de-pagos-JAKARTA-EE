package org.tallerjava.moduloComercio.infraestructura.messaging.implementation;

import java.util.Random;

import org.tallerjava.moduloComercio.dominio.CategoriaReclamo;
import org.tallerjava.moduloComercio.infraestructura.messaging.EvaluadorDeReclamos;

import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.enterprise.inject.Vetoed;

@ApplicationScoped
// @Vetoed
public class EvaluadorDeReclamosRandom implements EvaluadorDeReclamos {

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
