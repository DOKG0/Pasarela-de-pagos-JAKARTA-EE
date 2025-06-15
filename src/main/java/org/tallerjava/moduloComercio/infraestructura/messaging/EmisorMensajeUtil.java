package org.tallerjava.moduloComercio.infraestructura.messaging;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@ApplicationScoped
public class EmisorMensajeUtil {

    //permite interactuar con la queue
    @Inject
    private JMSContext jmsContext;

    //Se inyecta un recurso (queue) disponible
    //El parámetro del lookup es el mismo utilizado para crear la queue en el script de configuración
    @Resource (lookup = "java:jboss/exported/jms/queue/reclamos") //direccion jndi
    private Queue queueReclamosDeComercio ;

    public void enviarMensajeReclamo(Integer idComercio, Integer idReclamo, String texto) {
        ReclamoMessage reclamoMessage = new ReclamoMessage(idComercio, idReclamo, texto);
        jmsContext.createProducer().send(queueReclamosDeComercio, reclamoMessage.toJson());
    }
}
