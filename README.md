# STEEM Web App
## Introducción
Steem es una aplicación web diseñada para dar a los usuarios una tienda de juegos confiable que garantiza que sus juegos
nunca desaparecerán.

## Base de datos
Steem utiliza una base de datos relacional MariaDB para almacenar información sobre los usuarios, juegos y transacciones realizadas.

### Usuarios
La tabla usuarios almacena el id, email, nombre de usuario y password de todos los usuarios registrados en la plataforma. Hay 2 tipos de usuarios:
- **Cliente**: Puede comprar y descargar juegos. Tiene como atributos adicionales una lista de biblioteca de juegos y una lista de reseñas realizadas.
- **Desarrollador**: Puede subir juegos a la plataforma. Tiene como atributos adicionales una lista de juegos subidos y un Estudio asociado.

Tanto los clientes como los desarrolladores están almacenados en la tabla de usuarios, diferenciándose por el campo "tipo_usuario". Así, ambos tipos 
de usuarios comparten la misma estructura básica, pero tienen atributos y permisos específicos según su rol. Esta estructura nos permite hacer que
ambos tipos de usuario compartan funcionalidades comunes, como el inicio de sesión y la gestión de perfil, mientras que se mantienen sus características únicas.

### Juegos
La tabla juegos almacena el id, título, descripción, precio, fecha de lanzamiento, desarrollador (relación con la tabla usuarios) y género de cada juego disponible en la plataforma.

Es parte de la premisa de nuestra plataforma que los juegos NO se pueden eliminar bajo ningún concepto, para garantizar que los usuarios siempre puedan acceder a los juegos que han comprado.
Por esto, no existe ninguna funcionalidad para eliminar juegos de la base de datos, además de que en caso de la eliminación de un desarrollador, sus juegos asociados persisten en la base de datos.

### Biblioteca
La tabla biblioteca almacena las relaciones entre los clientes y los juegos que han comprado. Cada entrada en esta tabla contiene el id del usuario y el id del juego, indicando que el usuario posee ese juego.

La biblioteca depende directamente del usuario, ya que cada cliente tiene su propia biblioteca de juegos adquiridos. Cuando un cliente compra un juego, se crea una nueva entrada en la tabla biblioteca que asocia al usuario con el juego comprado.

Si un usuario es eliminado de la plataforma, todas las entradas correspondientes en la tabla biblioteca también se eliminan, ya que la biblioteca es una extensión directa del usuario.

### Reseñas
La tabla reseñas almacena las opiniones y calificaciones que los clientes dejan sobre los juegos que han comprado. Cada reseña contiene el id del usuario, el id del juego, la calificación (de 1 a 5 estrellas) y un comentario opcional.

Al igual que la biblioteca, esta tabla depende directamente del usuario, y en caso de que un usuario sea eliminado, todas sus reseñas también se eliminan.

## Implementación

### Login
La funcionalidad de login permite a los usuarios autenticarse en la plataforma utilizando su email y contraseña. Al iniciar sesión, se verifica el tipo de usuario (cliente o desarrollador) para redirigirlo a la interfaz correspondiente.

Esta funcionalidad utiliza la funcionalidad integrada de Spring Security para gestionar la autenticación y autorización de usuarios a las distintas páginas de la aplicación.
