
package org.tallerjava.cliente.soap;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.tallerjava.cliente.soap package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _NotificarSaldoEntrante_QNAME = new QName("http://soap.ws.interfase.moduloComercio.tallerjava.org/", "notificarSaldoEntrante");
    private static final QName _NotificarSaldoEntranteResponse_QNAME = new QName("http://soap.ws.interfase.moduloComercio.tallerjava.org/", "notificarSaldoEntranteResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.tallerjava.cliente.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NotificarSaldoEntrante }
     * 
     * @return
     *     the new instance of {@link NotificarSaldoEntrante }
     */
    public NotificarSaldoEntrante createNotificarSaldoEntrante() {
        return new NotificarSaldoEntrante();
    }

    /**
     * Create an instance of {@link NotificarSaldoEntranteResponse }
     * 
     * @return
     *     the new instance of {@link NotificarSaldoEntranteResponse }
     */
    public NotificarSaldoEntranteResponse createNotificarSaldoEntranteResponse() {
        return new NotificarSaldoEntranteResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotificarSaldoEntrante }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link NotificarSaldoEntrante }{@code >}
     */
    @XmlElementDecl(namespace = "http://soap.ws.interfase.moduloComercio.tallerjava.org/", name = "notificarSaldoEntrante")
    public JAXBElement<NotificarSaldoEntrante> createNotificarSaldoEntrante(NotificarSaldoEntrante value) {
        return new JAXBElement<>(_NotificarSaldoEntrante_QNAME, NotificarSaldoEntrante.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotificarSaldoEntranteResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link NotificarSaldoEntranteResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://soap.ws.interfase.moduloComercio.tallerjava.org/", name = "notificarSaldoEntranteResponse")
    public JAXBElement<NotificarSaldoEntranteResponse> createNotificarSaldoEntranteResponse(NotificarSaldoEntranteResponse value) {
        return new JAXBElement<>(_NotificarSaldoEntranteResponse_QNAME, NotificarSaldoEntranteResponse.class, null, value);
    }

}
