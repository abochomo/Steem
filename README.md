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

Un cliente puede hacer una reseña de un juego que ha comprado, modificarla después si ha cambiado su opinión sobre el juego o eliminarla cuando desee.

Al igual que la biblioteca, esta tabla depende directamente del usuario, y en caso de que un usuario sea eliminado, todas sus reseñas también se eliminan.

## Implementación

### Login
La funcionalidad de login permite a los usuarios autenticarse en la plataforma utilizando su email y contraseña. Al iniciar sesión, se verifica el tipo de usuario (cliente o desarrollador) para redirigirlo a la interfaz correspondiente.

Esta funcionalidad utiliza la funcionalidad integrada de Spring Security para gestionar la autenticación y autorización de usuarios a las distintas páginas de la aplicación.

### Logout

La funcionalidad de logout permite a los usuarios cerrar su sesión de manera segura. Al hacer logout, se invalidan las credenciales del usuario y se redirige a la página de inicio de sesión.

### Registro

La funcionalidad de registro permite a nuevos usuarios crear una cuenta en la plataforma. Durante el proceso de registro, los usuarios deben proporcionar su email, nombre de usuario y contraseña.

Cuando el usuario se registra, se guardan sus datos de cliente en la base de datos y se crea una biblioteca vacía para el usuario.

La contraseña se almacena de forma segura utilizando técnicas de hashing para proteger la información del usuario.

### Modificación de perfil

La funcionalidad de modificación de perfil permite a los usuarios actualizar su información personal, como el nombre de usuario y la contraseña. Los usuarios pueden acceder a esta funcionalidad desde su perfil y realizar los cambios necesarios.

## Cliente

### Compra de juegos

La funcionalidad de compra de juegos permite a los clientes adquirir juegos disponibles en la plataforma. Cuando un cliente selecciona un juego para comprar, se verifica que tenga fondos suficientes y se procesa la transacción. Si la compra es exitosa, se añade el juego a la biblioteca del cliente y se actualizan sus fondos.

### Saldo de usuario

La funcionalidad de saldo de usuario permite a los clientes gestionar sus fondos dentro de la plataforma. Los usuarios pueden añadir dinero a su cuenta mediante diferentes métodos de pago.
El saldo se actualiza en tiempo real y se refleja en la interfaz de usuario para que los clientes puedan ver su saldo disponible antes de realizar una compra.

### Reseñas de juegos

La funcionalidad de reseñas de juegos permite a los clientes dejar opiniones y calificaciones sobre los juegos que han comprado. Los usuarios pueden escribir comentarios y decidir si recomiendan o no el juego. También pueden modificar o eliminar sus reseñas en cualquier momento.

### IA
La aplicación integra una funcionalidad de inteligencia artificial que permite a los usuarios preguntarle a la IA sobre recomendaciones de juegos, y obtener el juego más adecuado según sus preferencias.

## Desarrollador

### Subida de juegos

La funcionalidad de subida de juegos permite a los desarrolladores cargar nuevos juegos en la plataforma. Los desarrolladores deben proporcionar información detallada sobre el juego. Una vez que el juego se publica, se añade a la base de datos y está disponible para que los clientes lo compren. El desarrollador puede elegir el título, descripción, precio, y carátula del juego al subirlo, teniendo en cuenta que debe seguir unas normas básicas para evitar juegos con precios negativos o títulos vacíos. 

### Gestión de juegos

La funcionalidad de gestión de juegos permite a los desarrolladores administrar los juegos que han subido a la plataforma. Los desarrolladores pueden ver una lista de sus juegos, actualizar la información del juego (como el precio o la descripción).