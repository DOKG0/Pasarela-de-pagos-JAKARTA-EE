package org.tallerjava.moduloComercio.interfase.remota;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClienteHTTPComercio {
    public String apiIA(String textoReclamo){
             //Configuro el prompt que le envio a la IA para que me responda solo una de las tres opciones, y le mando el reclamo.
        String prompt = "Analiza el siguiente texto y responde ÚNICAMENTE con una de las siguientes palabras: positivo, negativo o neutro. "
                      + "NO expliques, NO agregues ningún otro texto. Solo responde una de esas tres opciones, sin comillas ni signos de puntuación. "
                      + "Texto: " + textoReclamo;

        String jsonBody = String.format("""
            {
                "model": "llama2",
                "prompt": "%s",
                "stream": false
            }
        """, prompt.replace("\"", "\\\""));

        try {
            //Envio la peticion 
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            //Recibo la peticion y la re-formateo para usarla en el switch
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(body);
            String categoria = node.get("response").asText().trim().toLowerCase();

            return categoria;
        } catch (Exception e) {
            e.printStackTrace();
            return "SIN_DEFINIR"; 
        }
    }
    
}