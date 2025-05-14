package org.tallerjava.moduloTransferencia.dominio.repo;

import org.tallerjava.moduloComercio.dominio.Comercio;
import org.tallerjava.moduloTransferencia.dominio.Deposito;
import org.tallerjava.moduloTransferencia.dominio.Transferencia;
import java.time.LocalDate;
import java.util.List;

public interface TransferenciaRepositorio {
    void guardarTransferencia(Transferencia transferencia);
    void guardarDeposito(Deposito deposito);
    List<Deposito> buscarDepositosPorComercioYFecha(Comercio comercio, LocalDate fechaInicio, LocalDate fechaFin);
}