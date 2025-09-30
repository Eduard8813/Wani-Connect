# Wani Connect

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/java-17+-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/spring%20boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Node.js](https://img.shields.io/badge/node.js-18.x-green.svg)](https://nodejs.org/)

**Wani Connect** es una plataforma web innovadora que funciona como asistente guía personal, diseñada para transformar el turismo en Nicaragua mediante la provisión de logística completa y segura para viajes. Ofrece un mapa inteligente con rutas verificadas que conectan sitios patrimoniales y Ciudades Creativas, facilitando la gestión de transporte, hospedaje y experiencias exclusivas.

##  Características Principales

- **Mapa Inteligente**: Rutas verificadas que conectan sitios patrimoniales y Ciudades Creativas
- **Gestión Integral**: Facilita transporte, hospedaje y experiencias exclusivas
- **Comunidad Integrada**: Espacio social para compartir reseñas y fotos
- **Enfoque en UX**: Experiencia de usuario optimizada para un turismo auténtico y transformador
- **Seguridad Avanzada**: Protocolos robustos de protección de datos

## Utilidada

Wani Connect es una plataforma web con un enorme potencial para transformar la experiencia turística en Nicaragua. Su principal utilidad radica en centralizar toda la información relevante para turistas nacionales e internacionales, permitiéndoles planificar sus viajes de forma eficiente y personalizada. La app integra sitios históricos, culturales y turísticos del país, ofreciendo una vitrina digital que promueve tanto destinos populares como aquellos menos conocidos, fomentando así un turismo más sostenible y distribuido. Además, facilita la gestión del transporte público mediante rutas, horarios y paradas actualizadas, al tiempo que conecta a los usuarios con agencias de vehículos privados para reservas rápidas. Uno de sus mayores aportes es el impulso al comercio local: cada emprendedor o comerciante puede registrar su negocio en la plataforma, aumentando su visibilidad ante miles de potenciales clientes. Desde el punto de vista técnico, Wani Connect puede incorporar funcionalidades como perfiles de usuario, recomendaciones inteligentes, integración con APIs externas (mapas, clima, pagos), y un panel administrativo para comerciantes. También puede escalar fácilmente hacia nuevas áreas como reservas de hospedaje, experiencias gastronómicas o contratación de guías turísticos. En conjunto, esta aplicación no solo mejora la experiencia del viajero, sino que también dinamiza la economía local, digitaliza negocios rurales y ofrece datos valiosos para la toma de decisiones estratégicas en el sector turismo.

##  Estructura del Repositorio

```
/Backend
└── app
    ├── src
    │   └── main
    │       ├── java/com/eduard/registro/turismo/app
    │       │   ├── config        #     Círculo de protección (CORS, beans, seguridad)
    │       │   ├── controller    #     Guías del flujo (rutas y APIs)
    │       │   ├── dto           #     Mensajeros entre capas
    │       │   ├── model         #     Entidades del dominio turístico
    │       │   ├── repository    #     Portales de acceso a datos
    │       │   ├── security      #     Guardianes de autenticación y filtros
    │       │   └── service       #     Alquimia de la lógica de negocio
    │       └── resources
    │           └── application.properties #    Configuración del entorno
    ├── Dockerfile     #    Contenedor para despliegue ritual
    ├── pom.xml        #    ADN del proyecto (dependencias Maven)
    └── README.md      #    Este mapa espiritual y técnico

/Frontend
├── resource           #    Recursos compartidos
├── templates          #    Vistas generales
├── portafolios        #    Altares personalizados
│   ├── Eduard         #    Desarrollador del sistema
│   ├── Mercedez       #    Diseñadora
│   ├── Julisa         #    Comunicadora
│   ├── Farubich       #    Desarrollador
│   └── Marjuri        #    Markenting
│       └── static     #    Imágenes, estilos, scripts únicos
│       └── templates  #    Vistas personalizadas
└── static             #    Recursos globales (CSS, JS, imágenes, videos)
```

##  Pre-requisitos y Configuración

### Entorno de Despliegue

| Componente    | Plataforma             |
|---------------|------------------------|
| Backend       | Render                 |
| Frontend      | Netlify                |
| Base de Datos | somee.com (SQL Server) |

### Recursos del Servidor

El sistema se esta ejecutando como **Version gratuita**
- **CPU**: 0.1 núcleos
- **RAM**: 512 MB
- **Almacenamiento**: 1 GB SSD 
- **Ancho de banda**: 50 Mbps

### Puertos Abiertos (Firewall)

- `80` – HTTP
- `443` – HTTPS
- `1433` – SQL Server (somee.com)

### Dependencias Técnicas

- **Java**: versión 17 o superior
- **Spring Boot**: versión estable recomendada
- **Maven/Gradle**: para gestión de dependencias
- **Node.js**: para compilación del frontend (si aplica)

### Seguridad del Sistema

#### Actualizaciones Críticas

- **Sistema Operativo**: actualizaciones automáticas activadas
- **Java**: versión 17+ con parches de seguridad vigentes

#### Políticas de Seguridad

**CORS Restringido – Panel de Protección de Origen**

CORS (Cross-Origin Resource Sharing) regula cómo el navegador permite que un frontend (Netlify) se comunique con un backend en otro dominio (Render). Implementamos las siguientes medidas de seguridad:

- **Origen permitido**: Solo el dominio oficial (`https://wanniconnect.netlify.app/`) puede realizar peticiones
- **Métodos permitidos**: Definición explícita de métodos HTTP (GET, POST, PUT, DELETE)
- **Credenciales**: Envío seguro de cookies y tokens solo para orígenes confiables
- **Cabeceras**: Control estricto de cabeceras para prevenir fugas de información

##  Guía de Inicio Rápido

### Paso 1: Encender el servidor (Backend)

Verifica que el servidor esté activo accediendo a:

🔗 [https://wani-connect.onrender.com](https://wani-connect.onrender.com)

- Si ves `"Whitelabel Error Page"`, el backend está funcionando correctamente.
- Este componente es el núcleo del sistema que gestiona los datos.

### Paso 2: Acceder al frontend (Interfaz visual)

Abre la interfaz del proyecto en:

🔗 [https://wanniconnect.netlify.app/](https://wanniconnect.netlify.app/)

- Aquí comienza la experiencia de usuario.
- Con el backend activo, podrás navegar y acceder a todas las funcionalidades.

### Paso 3: Verificar conexión entre frontend y backend

Realiza una acción que consuma datos (inicio de sesión, consulta de lista).

- Si los datos se muestran correctamente, la integración está funcionando.
- Si hay problemas, revisa los pasos anteriores para asegurar el funcionamiento.

## Despliegue

Una vez iniciados los servicios, la aplicación establecerá comunicación entre el frontend y backend. **Nota importante**: Si el servidor no recibe peticiones durante 15 minutos, se apagará automáticamente y deberás repetir los pasos de la guía de inicio.

##  Accesibilidad y Usabilidad

Buenas prácticas implementadas:

- **Escalabilidad**: Arquitectura escalable tanto en backend como frontend
- **Seguridad**: Protocolos robustos para prevenir accesos no autorizados
- **Rendimiento**: Optimizado para baja latencia y manejo eficiente de datos
- **Diseño Responsivo**: Interfaz adaptable a diferentes dispositivos y tamaños de pantalla

##  Tecnologías Utilizadas

### Frontend
- HTML5, CSS, JavaScript
- Bootstrap 5 para diseño responsivo
- Librerías de mapas interactivos con openstreetmap

### Backend
- Java 17+
- Spring Boot Framework
- Maven para gestión de dependencias
- Docker para contenedorización
- JPA/Hibernate para persistencia de datos

# Flujo de Trabajo de Desarrollo

# Clonar repositorio
git clone https://github.com/Eduard8813/Wani-Connect.git

# Configurar backend
cd Backend/app
mvn clean install

# Configurar frontend
cd ../Frontend/resource
npm install

##  Contribuyendo

El proyecto utiliza un flujo de trabajo basado en ramas:

1. Crea una nueva rama para tu feature
2. Realiza los cambios necesarios
3. Envía un pull request hacia la rama main
4. Espera la revisión y aprobación del equipo

# Proceso de Contribución:

- Crear rama feature desde main
- Desarrollar funcionalidad con pruebas unitarias
- Solicitar Pull Request con revisión de código
- Integración continua en GitHub Actions

# Despliegue Automático:

- Backend: Despliegue automático en Render al mergear a main
- Frontend: Despliegue automático en Netlify al mergear a main

**Nota importante:** Los servidores realizan automáticamente el despliegue de actualizaciones provenientes de las ramas backend y frontend de forma independiente. Es importante destacar que, en caso de que alguna actualización contenga errores, el servidor cancelará el despliegue y restaurará la versión estable anterior de manera automática.

# Roadmap del Proyecto

## Próximas Entregas
 - Integración con pasarelas de pago (PayPal)
 - Aplicación Web
 - Soporte multiidioma (español, inglés) 
 - Dashboard para administradores

##  Wiki

Documentación adicional y código fuente disponible en:
[GitHub Repository](https://github.com/Eduard8813/Wani-Connect.git)

##  Versionado

Utilizamos Git para el control de versiones. Todas las versiones están disponibles en:
[Commits History](https://github.com/Eduard8813/Wani-Connect/commits/main)

## 📹 Video Demo

[![Video Demostrativo de Wani Connect](https://youtu.be/PIL1UY0GyPM)

*Haga clic en la imagen para ver el video completo en YouTube*

##  Autores

El proyecto fue desarrollado por un equipo multidisciplinario:

| Nombre           | Rol                          | Portafolio                                       |
|------------------|------------------------------|--------------------------------------------------|
| **Eduard Mora**  | Ingeniero Electrónico & Backend | [Ver Portafolio](https://wanniconnect.netlify.app/portafolios/eduard/templates) |
| **Farubich Hanon**| Ingeniero Electrónico        | [Ver Portafolio](#) *(En construcción)*         |
| **Julissa Aguilar**| Química Industrial          | [Ver Portafolio](#) *(En construcción)*         |
| **Marjiori Jimenaz**| Administración de Empresas | [Ver Portafolio](#) *(En construcción)*         |
| **Mercedez Bermudez**| Diseño Gráfico Multimedia  | [Ver Portafolio](#) *(En construcción)*         |

##  Contacto

Para consultas o colaboración:

- **Email**: Eduardmora88@gmail.com
- **Web**: [https://wanniconnect.netlify.app/](https://wanniconnect.netlify.app/)
- **GitHub**: [https://github.com/Eduard8813/Wani-Connect](https://github.com/Eduard8813/Wani-Connect)

---

**Wani Connect** © 2025. Todos los derechos reservados.