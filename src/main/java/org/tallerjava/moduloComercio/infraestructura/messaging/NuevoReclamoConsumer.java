package org.tallerjava.moduloComercio.infraestructura.messaging;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

import org.tallerjava.moduloComercio.aplicacion.ServicioComercio;
import org.tallerjava.moduloComercio.dominio.CategoriaReclamo;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;

/**
 * Clase encargada de procesar los mensajes que llegan a la queue.
 */
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destinationType",
                propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(
                propertyName = "destinationLookup",
                propertyValue = "java:app/jms/ReclamosMessageQueue"),
        @ActivationConfigProperty
                //Establece el número máximo de consumidores que estarán procesando los mensajes
                (propertyName = "maxSession", propertyValue = "1")})
public class NuevoReclamoConsumer implements MessageListener {

    @Inject
    ServicioComercio servicioComercio;
    
    @Inject
    EvaluadorDeReclamos evaluadorDeReclamos;

    @Override
    public void onMessage(Message message) {

        try {
            String body = message.getBody(String.class);
            //construyo nuevo objeto que representa un mensaje, a partir del string que recibo en el mensaje
            ReclamoMessage reclamoMessage = ReclamoMessage.buildFromJson(body);

            //llamo al evaluador de reclamos para obtener la categoria
            CategoriaReclamo categoriaReclamo = evaluadorDeReclamos.evaluarReclamo(reclamoMessage.texto());
            
            //llamo al servicioComercio para actualizar la categoria del reclamo
            servicioComercio.categorizarReclamo(
                reclamoMessage.idComercio(),
                reclamoMessage.idReclamo(), 
                categoriaReclamo
            );
        } catch (JMSException e) {
            System.err.println(String.format("Error al procesar el mensaje de reclamo: %s", e.getMessage()));
        }
    }
}
