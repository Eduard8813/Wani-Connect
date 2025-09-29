# Wani Connect

Wani Connect es una plataforma web que funciona como asistente guía personal, diseñado para transformar el turismo en Nicaragua al ofrecer la logística completa y segura de un viaje. Funciona proporcionando un mapa inteligente con rutas verificadas que conectan los sitios patrimoniales y las Ciudades Creativas, además de facilitar la gestión del transporte, el hospedaje y la localización de experiencias exclusivas. La plataforma se distingue por su enfoque en la experiencia de usuario y la comunidad, incluyendo un espacio social para que los viajeros compartan reseñas y fotos, fomentando así un turismo auténtico, justo y transformador.

## Estructura del Repositorio 

La estructura básica del proyecto es la siguiente:

```
Rama main
/Backend
  /app                  # Repositorio raíz del backend
    /src                # Abreviatura de source (código fuente)
      /main             # Raíz de la aplicación
        /java/com/eduard/registro/turismo/app  # Ruta base del sistema
          /config         # Configuraciones generales (CORS, beans, seguridad)
          /controller     # Controladores que gestionan rutas y redireccionan APIs
          /dto            # Objetos de transferencia entre capas (DTOs)
          /model          # Entidades del dominio turístico
          /repository     # Interfaces JPA para acceso a datos
          /security       # Autenticación, autorización y filtros
          /service        # Lógica de negocio y conexión entre capas
      /resources
        application.properties  # Configuración del entorno (puertos, credenciales, etc.)
    /Dockerfile           # Contenedor para despliegue
    /pom.xml              # Dependencias y configuración Maven
    /README.md            # Documentación técnica del backend

/Frontend
  /resource
    /templates
      /portafolios
        /Eduard
          /static
            /images       # Imágenes del portafolio
            /css          # Estilos personalizados
            /js           # Scripts de interacción
          /templates      # Vistas HTML de Eduard
        /Mercedez
          /static         
            /images       #Imágenes del portafolio
            /css          #Estilos personalizados
            /js           #Scripts de interacción
          /templates      #Vistas HTML de Mercedez
        /Julisa
          /static         
            /images       #Imágenes del portafolio
            /css          #Estilos personalizados
            /js           #Scripts de interacción
          /templates      #Vistas HTML de Julisa
        /Farubich
          /static
            /images       #Imágenes del portafolio
            /css          #Estilos personalizados
            /js           #Scripts de interacción
          /templates      #Vistas HTML de Farubich
        /Marjuri
          /static
            /images       #Imágenes del portafolio
            /css          #Estilos personalizados 
            /js           #Scripts de interacción
          /templates      #Vistas HTML de Marjuri
      /static
        /css              # Estilos globales
        /images           # Recursos visuales compartidos
        /js               # Scripts comunes
        /videos           # Material audiovisual

```

### Pre-requisitos 📋

A continuacion le mostramos los pre-requisitos que necesita del sistema

```
Pre-requisitos y Configuración del Sistema

Entorno de Despliegue

| Componente    | Plataforma         |
|---------------|--------------------|
| Backend       | Render             |
| Frontend      | Netlify            |
| Base de Datos | somee.com (SQL Server) |

Recursos del Servidor

- **CPU**: 2 núcleos  
- **RAM**: 4 GB  
- **Almacenamiento**: 20 GB SSD  
- **Ancho de banda**: 100 Mbps  

Puertos Abiertos (Firewall)

- `80` – HTTP  
- `443` – HTTPS  
- `1433` – SQL Server (somee.com)

Dependencias Técnicas

- **Java**: versión 17 o superior  
- **Spring Boot**: versión estable recomendada  
- **Maven/Gradle**: para gestión de dependencias  
- **Node.js**: para compilación del frontend (si aplica)

---

Seguridad del Sistema

Actualizaciones Críticas

- **Sistema Operativo**: actualizaciones automáticas activadas  
- **Java**: versión 17+ con parches de seguridad vigentes  

---

Políticas de Seguridad

CORS Restringido – Panel de Protección de Origen
CORS (Cross-Origin Resource Sharing) es una política de seguridad que regula cómo el navegador permite que un frontend (por ejemplo, en Netlify) se comunique con un backend ubicado en otro dominio (como Render). Es como un guardián que decide qué orígenes externos pueden acceder a los recursos del servidor.

- **Origen permitido**: Solo el dominio del frontend oficial puede hacer peticiones (por ejemplo, `https://wanniconnect.netlify.app/`).  
- **Métodos permitidos**: Se definen explícitamente los métodos HTTP que pueden usarse (GET, POST, PUT, DELETE).  
- **Credenciales**: Se permite el envío de cookies o tokens solo si el origen es confiable.  
- **Cabeceras**: Se controlan las cabeceras que pueden ser enviadas o recibidas para evitar fugas de información.

```

## Comenzando
_Esta guía te acompañará en el encendido del sistema como un ritual técnico-emocional, sin necesidad de comandos. Solo sigue los enlaces y observa cómo el proyecto cobra vida._

### Guia de iniciar el programa 
---

### Paso 1: Encender el servidor (Backend)

Accede al siguiente enlace para verificar que el servidor esté activo:

🔗 [https://wani-connect.onrender.com](https://wani-connect.onrender.com)

- Si ves `  "Whitelabel Error Page"`, el corazón del sistema está latiendo.
- Este backend es el guardián de los datos, el fuego que alimenta la experiencia.
---

### Paso 2: Acceder al frontend (Interfaz visual)

Dirígete al siguiente enlace para abrir la interfaz del proyecto:

🔗 [https://wanniconnect.netlify.app/](https://wanniconnect.netlify.app/)

- Aquí comienza el recorrido visual del usuario.
- Si el backend está encendido y conectado, podrás navegar, consultar datos y vivir la experiencia completa.
---

### Paso 3: Verificar conexión entre frontend y backend

Desde el navegador, realiza una acción que consuma datos (por ejemplo, iniciar sesión o ver una lista).

- Si los datos aparecen correctamente, el puente entre el templo visual y el fuego de datos está funcionando.
- Si no, revisa los pasos uno y paso dos para ver bien el funciomiento.

## Despliegue

Una vez que hayas iniciado los enlaces se presentara aplicacion donde se inicia la comunicacion entre el frontend y el backend. Cabe recalcar que si el servidor no recibe peticiciones en 15 minutos el procedera a apagarse y tendra que volver a repetir los pasos de la guia de iniciar el programa.


## Accesibilidad y usabilidad

Buenas prácticas implementadas:

- El programa es escalable tanto como su backend a como su frontend haciendo facil el manejo y el envio de datos con protocolos de seguridad para evitar acceso no autorizados.
- Tiene un rendimiento optimo debido a su baja complejidad y efectuo de datos de forma independiente.

---

## Construido 

_El trabajo fue construido con las siguientes tecnologias:_

- Frontend: html, css, javascript, bootscrap
- Backend: Java, mavel, Dockerfile y el framework Spring Boot.

## Contribuyendo 🖇️

Para construirlo se utilizaron ramas que estan unidas con la rama main, al momento de desclochar va a depender de la rama a donde la envias y en github tenes que esperar darle un pull request.

## Wiki

Puedes ver el codigo fuente [github](https://github.com/Eduard8813/Wani-Connect.git)

## Versionado

Usamos [github](https://github.com/Eduard8813/Wani-Connect/commits/main) para el versionado. Para todas las versiones disponibles.

## Autores 

El proyecto fue creado por distintas personas las cuales son:

* **Eduard Mora** - *Ingeniero electronico & Backend* - [portafolio](https://wanniconnect.netlify.app/portafolios/eduard/templates)

* **Farubich Hanon** - *Ingeniero electronico* - [portafolio](#fulanito-de-tal)

* **Julissa Aguilar** - *Quimica industrial* - [portafolio]()

* **Marjiori Jimenaz** - *Administracion de empresa* - [portafolio]()

* **Mercedez Bermudez** - *Diseño Grafico Multimedia* - [portafolio]()