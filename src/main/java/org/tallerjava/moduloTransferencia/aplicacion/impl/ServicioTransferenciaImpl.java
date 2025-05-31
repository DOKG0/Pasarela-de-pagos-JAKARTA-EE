package org.tallerjava.moduloTransferencia.aplicacion.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.repo.RepositorioComercio;
import org.tallerjava.moduloCompra.dominio.Compra;
import org.tallerjava.moduloCompra.dominio.datatypes.DTOCompra;
import org.tallerjava.moduloTransferencia.aplicacion.ServicioTransferencia;
import org.tallerjava.moduloTransferencia.dominio.CuentaBancariaPasarela;
import org.tallerjava.moduloTransferencia.dominio.Deposito;
import org.tallerjava.moduloTransferencia.dominio.Transferencia;
import org.tallerjava.moduloTransferencia.dominio.repo.TransferenciaRepositorio;
import org.tallerjava.moduloTransferencia.interfase.evento.out.EventoDepositoFinalizado;
import org.tallerjava.moduloTransferencia.interfase.evento.out.EventoPagoProcesado;
import org.tallerjava.moduloTransferencia.interfase.evento.out.EventoTransferenciaRecibida;
import org.tallerjava.moduloTransferencia.interfase.evento.out.PublicadorEvento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;

@ApplicationScoped
public class ServicioTransferenciaImpl implements ServicioTransferencia{

    @Inject
    private TransferenciaRepositorio repositorio;

    @Inject
    private PublicadorEvento publicadorEvento;

    @Inject
    CuentaBancariaPasarela cuentaPasarela;
    
    public boolean recibirNotificacionTransferenciaDesdeMedioPago(
        String nroCuentaBancoComercio, 
        double monto, 
        String codigoTransaccion,
        Integer idComercio) {
        
        //Se cambia despues
        BigDecimal montoBigDecimal = BigDecimal.valueOf(monto);
        
        //El comercio ya esta verificado desde el procesarPago de api compra y en el medio de pago externo
        // esta funcion se ejecuta siempre que se de como true el valor random por ende se hace la transferencia sin verificar mas.

        //Se crea la transferencia y se registra en la pasarela de pagos, a su vez se guarda en el banco del mismo
        Transferencia transferencia = new Transferencia(codigoTransaccion, montoBigDecimal, nroCuentaBancoComercio, idComercio);
        cuentaPasarela.registrarTransferenciaEntrante(transferencia);
        
        //Se envia el evento al modulo Monitores
        publicadorEvento.publicarTransferenciaRecibida(
            codigoTransaccion,
            idComercio,
            montoBigDecimal
        );

        //Se crea la el deposito y se registra en la pasarela de pagos, a su vez se guarda en la lista de depositos del comercio
        Deposito deposito = new Deposito(idComercio, montoBigDecimal, transferencia.getId());
        cuentaPasarela.registrarDepositoAComercio(deposito, idComercio);
        
        //Se envia al Modulo Monitoreo
        publicadorEvento.publicarDepositoFinalizado(
            deposito.getId(),
            idComercio,
            deposito.getMontoNeto(),
            deposito.getComision()
        );
        
        //Se envia el evento al modulo Monitoreo
        publicadorEvento.publicarPagoProcesado(
            codigoTransaccion,
            idComercio,
            montoBigDecimal,
            deposito.getMontoNeto()
        );

        return true;
    }

    public List<Deposito> consultarDepositos(Integer idComercio, LocalDate fechaInicial, LocalDate fechaFin){
        List<Deposito> depositos = repositorio.buscarDepositosPorComercioYFecha(idComercio, fechaInicial, fechaFin);
        return depositos;
    }

    public boolean notificarBancoConHttp(String numeroCuenta, double monto, String codigoTransaccion) {
        try {
            String soapEndpointUrl = "http://localhost:8080/TallerJakartaEEPasarelaPagos/NotificacionBancoService";
            String soapAction = "";

            String soapBody =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://soap.ws.interfase.moduloComercio.tallerjava.org/\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <ws:notificarSaldoEntrante>" +
                "         <numeroCuenta>" + numeroCuenta + "</numeroCuenta>" +
                "         <monto>" + monto + "</monto>" +
                "         <codigoTransaccion>" + codigoTransaccion + "</codigoTransaccion>" +
                "      </ws:notificarSaldoEntrante>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>";

            URL url = new URL(soapEndpointUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setRequestProperty("SOAPAction", soapAction);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(soapBody.getBytes());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // Leer la respuesta y buscar "ok"
                try (InputStream is = conn.getInputStream()) {
                    String response = new String(is.readAllBytes());
                    return response.contains(">ok<");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
