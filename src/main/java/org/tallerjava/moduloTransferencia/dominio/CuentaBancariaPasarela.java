package org.tallerjava.moduloTransferencia.dominio;

import java.math.BigDecimal;
import java.util.List;

import org.tallerjava.moduloTransferencia.dominio.repo.TransferenciaRepositorio;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Data;

@Data
@ApplicationScoped
public class CuentaBancariaPasarela {
    private final String cbu = "CBU_PASARELA_001";
    private final String moneda = "UYU";
    private BigDecimal saldo;
    private List<Transferencia> transferencias;
    private List<Deposito> depositos;

    @Inject
    private TransferenciaRepositorio repositorio;

    @PostConstruct
    void init() {
        this.saldo = repositorio.sumaTotalTransferencia()
                      .subtract(repositorio.sumaTotalNetoDeposito());
        this.transferencias = repositorio.traerTransferencias();
        this.depositos = repositorio.traerDepositos();
    }


    @Transactional
    public void depositar(BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser positivo");
        }
        this.saldo = this.saldo.add(monto);
    }

    @Transactional
    public void retirar(BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser positivo");
        }
        if (monto.compareTo(this.saldo) > 0) { // Nunca deberia pasar
            throw new IllegalArgumentException("Saldo insuficiente para realizar la operación");
        }
        this.saldo = this.saldo.subtract(monto);
    }

    public void registrarTransferenciaEntrante(Transferencia transferencia) {
        this.depositar(transferencia.getMonto());
        this.transferencias.add(transferencia);
        transferencia.marcarComoCompletada();

        repositorio.guardarTransferencia(transferencia);
    }

    public void registrarDepositoAComercio(Deposito deposito) {
        BigDecimal montoNeto = deposito.getMonto().subtract(deposito.getComision());
        this.retirar(montoNeto);
        this.depositos.add(deposito);
        
        repositorio.guardarDeposito(deposito);

    }
}
