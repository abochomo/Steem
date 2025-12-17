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

### Valores predeterminados

Al iniciar la aplicación por primera vez, se crean automáticamente ciertos valores predeterminados en la base de datos para facilitar las pruebas y el uso inicial de la plataforma. Estos valores incluyen:
- **Usuarios**: Se crean varios usuarios de prueba, tanto clientes como desarrolladores, con credenciales predefinidas. Algunos ejemplos de estos usuarios son:
  - **Cliente**: email: juan@gmail.com, contraseña: juan
  - **Cliente**: email: maria@gmail.com, contraseña: maria
  - **Desarrollador**: email:fernandodev@gmail.com, contraseña:fernando
  - **Desarrollador**: email:anagames@gmail.com, contraseña:ana
- **Juegos**: Se añaden varios juegos de ejemplo a la base de datos, asociados a los desarrolladores creados.
- **Bibliotecas**: Se asignan juegos a las bibliotecas de los clientes de prueba para simular compras previas.
- **Reseñas**: Se crean reseñas de ejemplo para algunos juegos, realizadas por los clientes de prueba.

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

### Eliminación de cuenta
La funcionalidad de eliminación de cuenta permite a los usuarios borrar su cuenta de la plataforma. Al eliminar la cuenta, se eliminan todos los datos asociados al usuario, incluyendo su biblioteca de juegos y reseñas.

En el caso de los desarrolladores, como los juegos no se pueden eliminar, estos permanecen en la base de datos aunque el desarrollador haya eliminado su cuenta. De esta manera los juegos quedan "huérfanos" pero accesibles para los clientes que los hayan comprado. Y disponibles para nuevas compras.
## Cliente

### Compra de juegos

#### Compra directa
La funcionalidad de compra de juegos permite a los clientes adquirir juegos disponibles en la plataforma. Cuando un cliente selecciona un juego para comprar, se verifica que tenga fondos suficientes y se procesa la transacción. Si la compra es exitosa, se añade el juego a la biblioteca del cliente y se actualizan sus fondos.

La lógica de la compra además tiene seguridad transaccional, lo que significa que si ocurre algún error durante el proceso de compra, la transacción se revierte y no se realizan cambios en la base de datos.
De esta manera se garantiza que el usuario no pierda dinero ni se añadan juegos a su biblioteca de forma incorrecta.

En el caso de que un usuario no tenga fondos suficientes, se le redirige inmediatamente a la página de saldo de usuario para que pueda añadir fondos a su cuenta.

#### Carrito de compra

La funcionalidad de carrito de compra permite a los clientes añadir múltiples juegos a un carrito virtual antes de proceder a la compra. Los usuarios pueden revisar los juegos en su carrito, y realizar la compra de todos los juegos en el carrito de una sola vez. Esta función además provee de la capacidad de seleccionar o deseleccionar juegos antes de la compra final.

En caso de que un usuario no tenga fondos suficientes para completar la compra del carrito, se le notifica y se le redirige a la página de saldo de usuario para que pueda añadir fondos a su cuenta.

La funcionalidad de carrito de compra no se almacena en la base de datos, sino que se mantiene en la sesión del usuario hasta que se complete la compra o se cierre la sesión.

De igual manera que en la compra directa, la lógica de la compra del carrito tiene seguridad transaccional para garantizar la integridad de los datos.

### Reembolso de juegos

De la misma manera que un usuario puede adquirir juegos para su biblioteca, puede reembolsarlos. La funcionalidad de reembolso de juegos permite a los clientes devolver un juego que han comprado dentro de un período de tiempo específico (por ejemplo, 14 días) y recibir un reembolso completo.

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

## Administrador
### Gestión de usuarios
Permite al administrador ver, modificar y eliminar usuarios de la plataforma. El administrador puede gestionar tanto clientes como desarrolladores, asegurándose de que se cumplan las políticas de la plataforma.
### Gestión de juegos
Permite al administrador supervisar todos los juegos disponibles en la plataforma. El administrador puede revisar la información del juego, asegurarse de que cumple con las normas de la plataforma y gestionar cualquier problema relacionado con los juegos.

También permite al administrador eliminar juegos en caso de que sea necesario. Sin embargo, un juego solo se podrá eliminar en el caso de que no haya sido comprado por ningún usuario. En caso contrario, el juego permanecerá en la base de datos para garantizar el acceso a los usuarios que lo hayan comprado.
### Gestión de bibliotecas
Permite al administrador supervisar las bibliotecas de los usuarios. El administrador puede ver qué juegos han comprado los clientes y gestionar cualquier problema relacionado con las bibliotecas de los usuarios.

En caso que el administrador quite un juego de la biblioteca de un usuario, se le efectuará un reembolso inmediato en saldo al usuario.
### Gestión de reseñas
Permite al administrador revisar y gestionar las reseñas dejadas por los clientes sobre los juegos. El administrador puede eliminar reseñas inapropiadas.