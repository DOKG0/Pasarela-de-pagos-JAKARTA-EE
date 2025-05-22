package org.tallerjava.moduloComercio.interfase.evento.in;

import java.util.logging.Logger;

import org.tallerjava.moduloComercio.aplicacion.ServicioComercio;
import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloComercio.dominio.CuentaBancoComercio;
import org.tallerjava.moduloComercio.dominio.Movimiento;
import org.tallerjava.moduloComercio.dominio.repo.RepositorioComercio;
import org.tallerjava.moduloTransferencia.interfase.evento.out.EventoDepositoFinalizado;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

@ApplicationScoped
public class ObserverModuloComercio {
    private static final Logger LOG = Logger.getLogger(ObserverModuloComercio.class.getName());

    @Inject
    ServicioComercio servicioComercio;

    @Inject
    RepositorioComercio comercioRepo;

    public void onDepositoRealizado(@Observes EventoDepositoFinalizado evento) throws Exception {
        try {
            LOG.info("Procesando depósito síncronico: " + evento.getIdDeposito());

            // 1. Buscar comercio
            Comercio comercio = comercioRepo.buscarPorId(evento.getIdComercio());
            if (comercio == null) {
                throw new Exception("Comercio no encontrado");
            }

            // 2. Buscar cuenta del comercio
            CuentaBancoComercio cuenta = comercio.getCuentaBancoComercio();
            if (cuenta == null) {
                throw new Exception("Cuenta bancaria no encontrada para el comercio");
            }

            // 3. Actualizar saldo
            cuenta.acreditar(evento.getMontoNeto());
    

            // 4. Registrar movimiento
            Movimiento movimiento = new Movimiento(
                "DEPOSITO_PASARELA",
                "Depósito desde pasarela - Ref: " + evento.getIdDeposito(),
                evento.getComision(),
                evento.getMontoNeto()
            );
            
            // 5. Agrega el movimiento a la cuenta y se actualiza la misma
            cuenta.getMovimientos().add(movimiento);
            boolean actualizado = comercioRepo.actualizarComercio(comercio);

            if (actualizado) {
                LOG.info("Comercio actualizado correctamente");
            } else {
                throw new Exception("Error al actualizar comercio");
                
            }
        }

        catch (Exception e) {
            LOG.severe("Error actualizando comercio: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
}
