# GlowApp

GlowApp es una aplicación móvil diseñada para facilitar el seguimiento diario del estado de la piel y ayudar al usuario a mantener una rutina saludable de cuidado personal.


## Características principales

- Registro y verificación de usuario mediante correo electrónico.
- Calendario de seguimiento diario con registros de:
  - Selfies
  - Estado y sentimientos de la piel
  - Productos usados
  - Alimentación
  - Consumo de agua
- Sección de reseñas para consultar y añadir opiniones sobre productos.
- Apartado informativo con consejos personalizados según el tipo de piel.


## Tecnologías utilizadas

- Java: Lenguaje principal para el desarrollo de la aplicación Android.
- Android Studio: Entorno de desarrollo integrado para Android.
- PostgreSQL: Sistema gestor de bases de datos local para almacenar información del usuario y seguimiento.
- Python + PyCharm: Para el envío automatizado de correos electrónicos de verificación.
- Canva: Herramienta para la creación de imágenes y elementos gráficos personalizados.


## Instalación

### Requisitos previos

- Android Studio instalado
- PostgreSQL instalado y configurado localmente
- Python y librerías necesarias para el envío de correos (SMTP)
- Clonar el repositorio en tu máquina local

### Servicio de envío de correos

El envío automatizado de correos electrónicos para la verificación de usuarios se realiza a través de un servicio independiente desarrollado en Python. 
Este servicio debe ejecutarse en paralelo para que la funcionalidad de verificación funcione correctamente.

1. Clonar el repositorio del servicio: https://github.com/abii0204/correosTFG.git
2. Configurar las credenciales SMTP en el archivo de configuración.
3. Ejecutar el servicio (desde PyCharm o consola).
4. Asegurarse de que el servicio esté activo antes de usar la aplicación.

### Pasos para ejecutar la aplicación

1. Clona este repositorio: https://github.com/abii0204/GlowApp_TFG.git
2. Abre el proyecto con Android Studio.
3. Configura la conexión a la base de datos PostgreSQL en la aplicación (ajusta parámetros en el código).
4. Ejecuta el servidor Python para el envío de correos (desde PyCharm o consola).
5. Compila y ejecuta la app en un emulador o dispositivo Android.

## Uso básico
- Regístrate con un correo válido para recibir un código de verificación.
- Verifica tu cuenta mediante el código recibido.
- Añade registros diarios de tu rutina y estado de piel.
- Consulta y añade reseñas de productos.
- Lee consejos personalizados según tu tipo de piel.

## Base de datos
En el archivo script_bbdd_tfg.txt (incluido en el repositorio) se encuentra:
- La creación completa de todas las tablas necesarias.
- Las relaciones, claves foráneas y triggers.
- Inserciones de datos iniciales.
**IMPORTANTE** Asegúrate de ejecutar este script en tu servidor PostgreSQL antes de lanzar la aplicación.

## Contacto
Autor: Abigail Alfageme Molinero
Correo: abialfa10@gmail.com

