package org.tallerjava.moduloTransferencia.aplicacion.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.tallerjava.moduloTransferencia.aplicacion.ServicioTransferencia;
import org.tallerjava.moduloTransferencia.dominio.Comercio;
import org.tallerjava.moduloTransferencia.dominio.CuentaBancariaPasarela;
import org.tallerjava.moduloTransferencia.dominio.Deposito;
import org.tallerjava.moduloTransferencia.dominio.Transferencia;
import org.tallerjava.moduloTransferencia.dominio.repo.TransferenciaRepositorio;
import org.tallerjava.moduloTransferencia.interfase.evento.out.PublicadorEvento;
import org.tallerjava.moduloTransferencia.interfase.remota.ClienteHttpTransferencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServicioTransferenciaImpl implements ServicioTransferencia{

    private static final Logger LOG = Logger.getLogger(ServicioTransferenciaImpl.class.getName());
    
    @Inject
    private TransferenciaRepositorio repositorio;

    @Inject
    private PublicadorEvento publicadorEvento;

    @Inject
    CuentaBancariaPasarela cuentaPasarela;

    @Inject
    private ClienteHttpTransferencia clienteHttpTransferencia;

    
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
        
        if(cuentaPasarela.getTransferencias().contains(transferencia)){
            LOG.info("[ServicioTransferencia] Transferencia registrada en pasarela de pagos");
        } else {
            LOG.warning("[ServicioTransferencia] Error al registrar la transferencia en la pasarela de pagos");
            return false; //Si no se pudo registrar la transferencia, se retorna false
        }

        //Se envia el evento al modulo Monitores
        publicadorEvento.publicarTransferenciaRecibida(
            codigoTransaccion,
            idComercio,
            montoBigDecimal
        );

        //Se crea la el deposito y se registra en la pasarela de pagos, a su vez se guarda en la lista de depositos del comercio
        Deposito deposito = new Deposito(idComercio, montoBigDecimal, transferencia.getId());
        cuentaPasarela.registrarDepositoAComercio(deposito, idComercio);
        clienteHttpTransferencia.notificarBancoSoapHttp(nroCuentaBancoComercio, monto, codigoTransaccion);
        
        if (cuentaPasarela.getDepositos().contains(deposito)) {
            LOG.info("[ServicioTransferencia] Deposito registrado en pasarela de pagos");
        } else {
            LOG.warning("[ServicioTransferencia] Error al registrar el deposito en la pasarela de pagos");
            return false; //Si no se pudo registrar el deposito, se retorna false
        }
        
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

        Comercio comercio = repositorio.buscarPorId(idComercio);
        if (comercio == null) {
            return null;
        }

        List<Deposito> depositos = comercio.getDepositos();
        List<Deposito> depositosFiltrados = new ArrayList<>();

        for (Deposito d : depositos) { 
            LocalDate fechaDeposito = LocalDate.parse(d.getFecha());

            if ((fechaDeposito.isEqual(fechaInicial) || fechaDeposito.isAfter(fechaInicial)) &&
                (fechaDeposito.isEqual(fechaFin) || fechaDeposito.isBefore(fechaFin))) {
                depositosFiltrados.add(d);
            }
        }
        return depositosFiltrados;
    }

    
}
