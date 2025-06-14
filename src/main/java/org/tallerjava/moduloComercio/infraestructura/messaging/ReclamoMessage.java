package org.tallerjava.moduloComercio.infraestructura.messaging;

import java.io.StringReader;
import java.io.StringWriter;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;

public record ReclamoMessage(Integer idComercio, Integer idReclamo, String texto) {
    
    public String toJson() {
        JsonObject jsonObject = Json.createObjectBuilder()
            .add("idComercio", this.idComercio)
            .add("idReclamo", this.idReclamo)
            .add("texto", this.texto)
            .build();
        
        StringWriter sWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(sWriter);

        jsonWriter.write(jsonObject);
        jsonWriter.close();

        return sWriter.toString();
    }

    public static ReclamoMessage buildFromJson(String jsonReclamoMessage) {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonReclamoMessage));
        JsonObject jsonObject = jsonReader.readObject();
        ReclamoMessage reclamoMessage = new ReclamoMessage(
            jsonObject.getInt("idComercio"),
            jsonObject.getInt("idReclamo"),
            jsonObject.getString("texto")
        );

        return reclamoMessage;
    }
}
