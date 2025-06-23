use tallerJakartaEE;

insert into Grupo_MOD_SEGURIDAD values ("comercio"), ("admin"), ("servicioExterno");

-- password:1234
-- usuario, passwordHash
insert into Usuario_MOD_SEGURIDAD values ("apiadmin", "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4", "admin");
insert into Usuario_MOD_SEGURIDAD values ("nextriguser", "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4", "comercio");
insert into Usuario_MOD_SEGURIDAD values ("servicioExterno", "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4", "servicioExterno");

-- id, banco, numeroCuenta
insert into cuenta_banco_comercio_MOD_COMERCIO values (1, null, "112233");
-- id, numeroCuenta
insert into cuenta_banco_comercio_MOD_COMPRA values (1, "112233");

--id, direccion, nombre, rut, usuario, id CuentaBancoComercio 
insert into comercio_MOD_COMERCIO values (1, "18 de Julio 111", "NextRig", "123451234512345", "nextriguser", 1);
--id, importeVentasDelDia, usuario, id CuentaBancoComercio
insert into comercio_MOD_COMPRA values (1, 0,"nextriguser", 1);

--id
insert into comercio_MOD_TRANSFERENCIA values (1);

-- id, habilitado, identificador, id comercio
insert into pos_MOD_COMERCIO values (1, 1, "pos1",1), (2, 0, "pos2", 1);
insert into pos_MOD_COMPRA values (1, 1, "pos1", 1), (2, 0, "pos2", 1);