# Cheesecake Place

<p align="center">
  <img width="512" alt="Logo2v2" src="https://github.com/pablo-kotlin/cheesecake-place/assets/128930557/948c1fe9-3067-497e-98af-3a0244fdca80">
</p>

Partiendo de mi propia curiosidad, me decanté desde el primer momento por diseñar una aplicación móvil, ya que es un campo que me parece tremendamente atractivo y con enorme proyección de futuro, además de una disciplina donde me planteo emplearme laboralmente a medio plazo. Construir un programa para un dispositivo móvil tiene asimismo un componente estético que convierte el proceso de desarrollo en una tarea con mayor impacto creativo, enriqueciendo de este modo la experiencia.

Adicionalmente, enfocar este proyecto en el diseño de una aplicación móvil, me ha permitido profundizar en terrenos que a lo largo de ciclo solo he podido ver de forma tangencial, como serían las bases de datos en la nube o el almacenamiento de imágenes. A este respecto también podríamos añadir el uso de determinadas herramientas y librerías de Android Studio muy modernas que se utilizan de forma masiva en entornos laborales reales.

La aplicación que se ha desarrollado es una app de valoración de tartas de queso, con alguna semejanza a los contenidos que puede ofrecer una web como TripAdvisor, solo que mucho más centrada en el universo de los postres. Además de las reseñas o calificaciones que podamos ver por parte del resto de usuarios, podremos hacer uso de un buscador por área geográfica.

## Objetivos

La propuesta original no era otra que diseñar una app móvil que permitiera la valoración del postre que sea servido en un determinado establecimiento, pudiendo acompañar esa evaluación con una fotografía. Además, el usuario también podría consultar cuáles son las mejores y las peores tartas de queso en un área predeterminada por el usuario, como, por ejemplo, su ciudad.

De forma más específica, también nos marcamos los siguientes objetivos:

1.	Definir la arquitectura de la aplicación en Android Studio, haciendo uso del lenguaje Kotlin.
2.	Visualizar en un listado todas nuestras reseñas junto con sus fotografías.
3.	Construir las bases de datos necesarias para nuestra app con Firebase Realtime Database.
4.	Verificar el correcto funcionamiento de todos los elementos.
5.	Validar la introducción de cualquier dato por parte del usuario antes de guardar cualquier actualización en la base de datos.
6.	Consultar la eliminación de un elemento mediante la confirmación a través de un cuadro de diálogo.
7.	Poder acceder al sitio web de un restaurante pulsando sobre el nombre del establecimiento.
8.	Hacer accesible la aplicación a cualquier usuario desde la Play Store para poder instalarla sin necesidad de disponer del apk.

## Tecnologías y herramientas utilizadas en el proyecto

Firebase Realtime Database (Base de datos en tiempo real): Base de datos NoSQL con estructura de árbol, e integrada con Android Studio, lo que nos permite la sincronización en tiempo real desde cualquier móvil. De este modo, en caso de que un usuario modificara algún dato desde su dispositivo, dicho cambio se traslada automáticamente al resto.

Firebase Storage (Almacenamiento): Almacén de imágenes integrado con Coogle Cloud Storage. Imprescindible para guardar y descargar las imágenes que nuestros usuarios tomen en nuestra aplicación.

Firebase Authentication (Autenticación): Proporciona servicios y métodos para crear y gestionar usuarios a través de su correo electrónico, o de proveedores como Facebook o Google. Permite al administrador resetear sus contraseñas en caso de pérdida.

## Capturas de Pantalla

A continuación se muestran algunas capturas de pantalla de las diferentes actividades por las que navegaremos en nuestra aplicación:

<img width="300" alt="Screenshot_20220609_211305" src="https://github.com/pablo-kotlin/cheesecake-place/assets/128930557/f36a0fb0-b3dd-4f12-bb4f-67385287792d">

<img width="300" alt="Screenshot_20220609_211328" src="https://github.com/pablo-kotlin/cheesecake-place/assets/128930557/146f6cc9-3e28-41ba-a313-4205ce6ba539">

<img width="300" alt="Screenshot_20220609_211345" src="https://github.com/pablo-kotlin/cheesecake-place/assets/128930557/0aaa72c9-01d9-472d-95a1-94d59d136cf8">

## Requisitos funcionales

•	La primera pantalla de la aplicación será la de Login, donde el usuario tendrá la opción de identificarse, o de crear una nueva cuenta.

•	La identificación del usuario se hará con una cuenta de correo electrónico.

•	En caso de introducir mal la contraseña o su e-mail, no se permitirá el acceso y se lanzará un mensaje de aviso.

•	Tras la correcta identificación del usuario, se accederá a la pantalla principal de la aplicación.

•	A través de la barra de navegación inferior, el usuario podrá desplazarse entre su perfil, la opción de explorar para encontrar los restaurantes mejor valorados a su alrededor, o el fragmento en el que verá las reseñas que haya llevado a cabo previamente.

•	La vista por defecto tras hacer Login será la de las valoraciones de usuario.

•	A través de un icono elegido a tal efecto, el usuario podrá eliminar una reseña.

•	Gracias a un botón flotante, el usuario podrá añadir una nueva tarta desde la pestaña donde aparecen las tartas que ha valorado.

•	En la vista para añadir una nueva tarta, se garantizará que no se puede actualizar la base de datos sin validar que los campos que garantizan la integridad de la base de datos no son nulos ni están vacíos.

•	En el listado de restaurantes, se podrá acceder a la web del mismo presionando sobre su ficha.

•	Desde el fragmento del perfil de usuario, se podrá cerrar la sesión haciendo Logout.


## Contacto

E-mail de contacto: pablokotlin@gmail.com



