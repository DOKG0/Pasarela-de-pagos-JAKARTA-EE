package org.tallerjava.moduloComercio.infraestructura.messaging.implementation;

import org.tallerjava.moduloComercio.dominio.CategoriaReclamo;
import org.tallerjava.moduloComercio.infraestructura.messaging.EvaluadorDeReclamos;
import org.tallerjava.moduloComercio.interfase.remota.ClienteHTTPComercio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Vetoed;

@ApplicationScoped
@Vetoed
public class EvaluadorDeReclamosIA implements EvaluadorDeReclamos {
    
    @Override
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
