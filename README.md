# Pasarela de pagos JAKARTA EE

Pasos para construir y ejecutar el proyecto:
1. Crear la base de datos con el nombre 'tallerJakartaEE' en MYSQL.
2. Verificar que en la línea 24 del archivo ./config.cli estén las credenciales correctas para acceder al motor de base de datos.
Si el usuario no tiene contraseña asignada, dejar el valor del parámetro password vacío
3. Ejecutar el comando 'mvn clean package wildfly:run'

El proyecto se inicializa con 3 roles (grupos): comercio, admin, servicioExterno
También se inicializa con 1 administrador con el rol admin, cuyas credenciales son apiadmin:1234


Durante el desarrollo

Potenciales errores durante el deploy:
1. Si la base de datos ya estaba creada previamente, verificar que no hayan sido modificado las tablas desde la última vez que se ejecutó el servidor.
En caso de ya existir tablas creadas, hay que eliminarlas.
2. Si ocurre un error al crear las tablas, verificar que la siguiente propiedad del archivo persistence.xml en el directorio /src/main/webapp/WEB-INF/classes/META-INF no esté comentada: 
  <property name="jakarta.persistence.schema-generation.database.action" value="drop-and-create" />
