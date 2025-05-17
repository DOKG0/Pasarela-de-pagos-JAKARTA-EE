package org.tallerjava.moduloCompra.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="cuenta_banco_comercio_MOD_COMPRA")
@Table(name="cuenta_banco_comercio_MOD_COMPRA")
public class CuentaBancoComercio {
    @Id
    private Integer id;
    private String numeroCuenta;
}