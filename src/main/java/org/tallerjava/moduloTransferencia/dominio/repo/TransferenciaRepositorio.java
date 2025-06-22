package org.tallerjava.moduloTransferencia.dominio.repo;

import org.tallerjava.moduloTransferencia.dominio.Comercio;
import org.tallerjava.moduloTransferencia.dominio.Deposito;
import org.tallerjava.moduloTransferencia.dominio.Transferencia;

import java.math.BigDecimal;
import java.util.List;


public interface TransferenciaRepositorio {
    Integer guardarTransferencia(Transferencia transferencia);
    Integer guardarDeposito(Deposito deposito);
    Integer guardarComercio(Comercio comercio);
    boolean actualizarComercio(Comercio comercio);
    Comercio buscarPorId(Integer id);
    BigDecimal sumaTotalTransferencia();
    BigDecimal sumaTotalNetoDeposito();
    List<Deposito> traerDepositos();
    List<Transferencia> traerTransferencias();
}