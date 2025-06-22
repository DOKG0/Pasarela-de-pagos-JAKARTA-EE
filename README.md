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

### Documentacion proyecto

#### 1. Arquitectura general

EL sistema esta organizado en una arquitectura de monolito modular

- ***Modulo comercio***: Gestiona el registro y mantenimiento de comercio, POS y reclamos.

- ***Modulo compra***: Procesa las transacciones de pago, gestiona las tarjetas y genera resumenes de ventas

- ***Modulo seguridad***: Maneja la autenticacion, autorizacion y gestion de usuarios y contraseñas

- ***Servicios externos***: Simulan la interaccion con proveedores de procesamiento de pagos

#### 2. Tecnologias utilizadas

- Jakarta EE 10: Framework principal para el desarrollo de la aplicacion

- Hibernate 6.1.5: ORM para el mapeo Objeto-relacional

- Wildfly 27.0.1: Servidor de aplicaciones JavaEE

- MariaDB: Sistema de gestion de bases de datos

- JAX-RS: ApPI para servicios web REST

- CDI: Inyeccion de dependencias

- JPA: Persistencia de datos

- Security API - Mecanismo de autenticacion y autorizacion

- jUnit 5 y Mockito: Framework para testing

- Lombok: Reduccion de codigo boilerplate

- Swagger: Documentacion de APIs

- RateLimiter: Evitar sobrecarga del servidor y prevenir ataque DDoS regulando el acceso

- Docker: Se utiliza para la creacion, despliegue y ejecucion de aplicaciones en contenedores

- Micrometer: Framework para exponer metricas

- InfluxDB: Base de datos para almacenar las metricas

- Grafana: Plataforma que se usa para visualizar las metricas almacenadas en influxDB en graficas en tiempo real

- Jakarta Messaging: Api de java para enviar, recibir y procesar mensajes de forma asincrona

#### 3. API REST

**Modulo Comercio API**

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

**Modulo Compra API**

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

#### 4. Flujo principal de negocio

***Proceso de alta de comercio***

1. El Comercio envia sus datos a traves de la API
2. El sistema valida los datos requeridos
3. Se crea un usuario con rol "comercio" en el sistema de seguridad
4. Se registra el comercio en el sistema
5. Se emite un evento de comercio creado que se propaga a otros modulos

***Flujo de procesamiento de pagos***

1. Se reciben los datos de la tarjeta y el importe
2. Se valida el formato de la fecha de vencimiento
3. Se crea un objeto de Tarjeta con los datos proporcionados
4. Se solicita al servicio externo el procesamiento del pago
5. Segun el resultado, se marca la compra como **aprobada** o **rechazada**
6. Se actualiza el registro de ventas del comercio
7. Se devuelve el resultado al cliente

***Generacion de resumenes de ventas***

1. Se recibe la solicitud con el ID del comercio y parametros de filtrado
2. Se recupera el comercio desde el repositorio
3. Se filtran las compras segun los criterios **periodos** o **estado**
4. Se calcula el importe total y la cantidad de ventas
5. Se devuelve el DTO con el resumen completo

#### 5. Test y validacion

***Ejecucion de Tests unitarios***

Para ejecutar todos los test unitarios utilizar `mvn test`

Para ejecutar test especificos utilizar por ejemplo `mvn test -Dtest=CompraApiTest`

#### 6. Seguridad

***Roles de usuario***

- ***comercio***: Acceso a operaciones especificas del comercio
- ***admin***: Acceso completo a todas las operaciones
- ***servicioExterno***: Acceso limitado para integraciones externas

***Mecanismos de autenticacion***

- `Basic Authentication` para todas las APIs protegidas
- `Hashing` de contraseñas utilizando algoritmos seguros
- `Validacion de permisos` basada en roles con anotaciones **@RolesAllowed**

***RateLimiter***

- Se utiliza `bucket4j` para implementar un ratelimiter que limita las peticiones de los usuarios evitando una sobrecarga en el servidor asi como tambien previniendo ataques DDoS leves definiendo una cuota de 100 peticiones como limite por minuto y en algunos endpoint una cuota de 50.

---

### 7. Modulo monitoreo

El modulo de monitoreo permite registrar y visualizar metricas del sistema en tiempo real, facilitando el seguimiento de la actividad de la aplicacion.

**¿Como funciona?**

1. Se utiliza Micrometer como framework de instrumentacion para exponer metricas personalizadas.
2. La clase `RegistroMetricasConfig` configura un `MeterRegistry` conectado a `influxDB` donde se almacenan las metricas.
3. El observador `ObserverMonitoreo` escucha eventos relevantes del sistema como (pagos, reclamos, transferencias) y aumenta los contadores definidos cada vez que ocurre un evento.
4. Las metricas registradas incluyen:
    - Reclamos de comercio: (`reclamos_comercio_total`)
    - Pagos realizados: (`pagos_realizados_total`)
    - Pagos rechazados: (`pagos_rechazados_total`)
    - Pagos procesados: (`pagos_procesados_total`)
    - Transferencias recibidas: (`transferencias_recibidas_total`)
    - Depositos finalizados: (`depositos_finalizados_total`)

**Cosas destacadas** 
- Integracion automatica: El  monitoreo es transparente para el resto de la aplicacion ya que se basa en eventos y observadores CDI.
- Persistencia y visualizacion: Las metricas se almacenan en influxDB y son visualizadas utilizando grafana
- Se utiliza docker compose para desplegar imagenes de influx db y grafana.

---

### 8. Casos de prueba

**Endpoints y flujo**

1. Realizar pago ok:

    `curl -v --user nextriguser:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/nueva-compra -H "Content-Type: application/json" -d '{"nroCuentaBancoComercio":"112233","idComercio":1, "idPos": 1, "monto":10000.0,"dtoPago":{"nroTarjeta":123456,"marcaTarjeta":"visa","fechaVtoTarjeta":"2025-05-17"}}'`

    ---
2. Realizar pago error:
    (Credenciales de otro comercio)

    `curl -v --user otrocomercio:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/nueva-compra -H "Content-Type: application/json" -d '{"nroCuentaBancoComercio":"112233","idComercio":1, "idPos": 1, "monto":10000.0,"dtoPago":{"nroTarjeta":123456,"marcaTarjeta":"visa","fechaVtoTarjeta":"2025-05-17"}}'`

    ---
3. Realizar pago con POS no habilitado:

    `curl -v --user apiadmin:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/pos/1/estado -H "Content-Type: application/json" -d '{"estado": "true"}'`

    ---
4. Realizar pago con credenciales incorrectas

    `curl -v --user nextriguser:1 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/compra/1/nueva-compra -H "Content-Type: application/json" -d '{"nroCuentaBancoComercio":"112233","idComercio":1, "idPos": 1, "monto":10000.0,"dtoPago":{"nroTarjeta":123456,"marcaTarjeta":"visa","fechaVtoTarjeta":"2025-05-17"}}'`

    ---
5. Cambio de contraseña de comercio

    `curl -v --user nextriguser:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/password -H "Content-Type: application/json" -d '{"passwordNueva": "9999"}'`

    ---
6. Pagos concurrentes


7. Prueba de RateLimiter

    `for i in {1..105}
do
  echo "Petición $i"
  curl -s -o /dev/null -w "%{http_code}\n" \
    --user nextriguser:1234 \
    -H "Content-Type: application/json" \
    -d '{"contenidoReclamo": "no anda el pos"}' \
    http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/reclamo
done`

    ---
8. Un comercio realizar un reclamo

    `curl -v --user nextriguser:1234 http://localhost:8080/TallerJakartaEEPasarelaPagos/api/comercio/1/reclamo -H "Content-Type: application/json" -d '{"contenidoReclamo": "no anda el pos"}'`

---
