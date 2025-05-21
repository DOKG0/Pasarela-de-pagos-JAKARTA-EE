# Pasarela de pagos JAKARTA EE

### Pasos para construir y ejecutar el proyecto
1. Crear la base de datos con el nombre `tallerJakartaEE` en MYSQL.
2. Verificar que en la línea 24 del archivo `./config.cli` estén las credenciales correctas para acceder al motor de base de datos.
Si el usuario no tiene contraseña asignada, dejar el valor del parámetro password vacío
3. Ejecutar el comando `mvn clean package wildfly:run` o `mvn clean package wildfly:dev`

El proyecto se inicializa con 3 roles (grupos): comercio, admin, servicioExterno
También se inicializa con 1 administrador con el rol admin, cuyas credenciales son **apiadmin:1234**

---

### Durante el desarrollo

**Potenciales errores durante el deploy**
1. Si la base de datos ya estaba creada previamente, verificar que no hayan sido modificado las tablas desde la última vez que se ejecutó el servidor.
En caso de ya existir tablas creadas, pueden interferir con la creación de las tablas actualizadas del mismo nombre. En ese caso hay que eliminarlas.
2. Si ocurre un error al crear las tablas, verificar que la propiedad de nombre `jakarta.persistence.schema-generation.database.action` del archivo `persistence.xml` en el directorio `/src/main/webapp/WEB-INF/classes/META-INF` no esté comentada.
Esta propiedad indica que las tablas son eliminadas y creadas desde cero al inicializar el contexto de persistencia, por lo que todos los datos guardados serán eliminados. Realizar un respaldo de los datos previo a la ejecución del servidor si se quiere conservar los datos.

---

### Documentación de las APIs con Swagger

Ejecutar el servidor e ingresar a http://localhost:8080/TallerJakartaEEPasarelaPagos/dist/

---

### Modulo Comercio API

1. Alta de Comercio

```bash
curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/alta -H "Content-Type: application/json" -d '{"direccion":"18 de Julio 111", "usuario": "nextriguser" ,"nombre":"NextRig", "rut": "432151234513212", "password": "1234", "nroCuentaBanco": "112233"}'
```
Registra un nuevo comercio en el sistema con sus datos básicos y credenciales.


2. Modificación de Comercio - Actualizar RUT

```bash
curl -v --user nextriguser:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/modificacion -H "Content-Type: application/json" -d '{"direccion":"18 de Julio 111","nombre":"NextRig", "rut": "88998899889912"}'
```
Actualiza el RUT de un comercio existente manteniendo los demás datos.

3. Modificación de Comercio - Solo Dirección

```bash
curl -v --user nextriguser:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/modificacion -H "Content-Type: application/json" -d '{"direccion":"25 de Mayo 111"}'
```
Actualiza únicamente la dirección del comercio.

4. Modificación de Comercio - Campo Nulo
```bash
curl -v --user nextriguser:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/modificacion -H "Content-Type: application/json" -d '{"direccion":"18 de Julio 111","nombre":"NextRig", "rut": null}'
```
Demuestra que los campos enviados como null no se actualizan.

5. Modificación de Comercio - Credenciales Incorrectas
```bash
curl -v --user nextriguser:1432 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/modificacion -H "Content-Type: application/json" -d '{"direccion":"25 de Mayo 111"}'
```
Muestra el error 403 (Forbidden) al usar credenciales incorrectas.

6. Alta de POS
```bash
curl -v --user nextriguser:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/pos/alta -H "Content-Type: application/json" -d '{ "identificador":"pos1"}'
```
Registra un nuevo punto de venta (POS) para un comercio específico.

7. Cambio de Estado de POS (por Comercio)
```bash
curl -v --user nextriguser:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/pos/1/estado -H "Content-Type: application/json" -d '{"estado": "false"}'
```
Permite a un comercio activar/desactivar su propio POS.

8. Cambio de Estado de POS (por Admin)
```bash
curl -v --user apiadmin:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/pos/1/estado -H "Content-Type: application/json" -d '{"estado": "false"}'
```
Permite a un administrador activar/desactivar el POS de cualquier comercio.

9. Cambio de Contraseña
```bash
curl -v --user nextriguser:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/password -H "Content-Type: application/json" -d '{"passwordNueva": "9999"}'
```
Permite a un comercio cambiar su contraseña.

10. Realizar Reclamo
```bash
curl -v --user nextriguser:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/reclamo -H "Content-Type: application/json" -d '{"contenidoReclamo": "no anda el pos"}'
```
Permite a un comercio enviar un reclamo al sistema.


---

### Modulo Compra API

1. Procesar Nuevo Pago
```bash
curl -v http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/nueva-compra -H "Content-Type: application/json" -d '{"nroTarjeta": "123", "marcaTarjeta": "visa", "fechaVtoTarjeta": "2025-05-17", "importe": 10000 }'
```
Procesa un nuevo pago para el comercio especificado utilizando los datos de tarjeta proporcionados e importe.

2. Obtener Resumen de Ventas por Periodo
```bash
curl -v "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/resumen/periodo?fechaInicio=2025-05-18&fechaFin=2025-05-18"
```
Genera un informe de ventas para el comercio especificado dentro del rango de fechas indicado.

3. Obtener Resumen de Ventas por Estado - Aprobadas
```bash
curl -v "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/resumen/por-estado?estado=APROBADA"
```
Obtiene un resumen de todas las ventas aprobadas para el comercio especificado.

4. Obtener Resumen de Ventas por Estado - Rechazadas
```bash
curl -v "http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/resumen/por-estado?estado=RECHAZADA"
```
Obtiene un resumen de todas las ventas rechazadas para el comercio especificado.

---