package org.tallerjava.moduloComercio.infraestructura.messaging;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import org.tallerjava.moduloComercio.dominio.CategoriaReclamo;
import org.tallerjava.moduloComercio.interfase.remota.ClienteHTTPComercio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EvaluadorDeReclamos {

    //Esta funcion es la que deberia de cambiarse por una llamada a la IA elegida si se implementara
    public CategoriaReclamo evaluarReclamo(String textoReclamo) {
       
            ClienteHTTPComercio llamado = new ClienteHTTPComercio();
            String categoria = llamado.apiIA(textoReclamo);
            
            //Dependiendo de la categoria recibida envio un tipo de categoria u otro
            switch (categoria) {
                case "positivo":
                    System.out.println("Estoy en positivo");
                    return CategoriaReclamo.POSITIVO;
                case "neutro":
                    System.out.println("Estoy en neutro");
                    return CategoriaReclamo.NEUTRO;
                case "negativo":
                    System.out.println("Estoy en negativo");
                    return CategoriaReclamo.NEGATIVO;
                default:
                    System.out.println("Estoy en default");
                    return CategoriaReclamo.SIN_DEFINIR; 
            }

        
    }


}
