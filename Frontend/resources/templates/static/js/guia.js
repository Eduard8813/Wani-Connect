  // Funcionalidad del menú desplegable
        const menuToggle = document.getElementById('menuToggle');
        const dropdownMenu = document.getElementById('dropdownMenu');
        
        menuToggle.addEventListener('click', function() {
            menuToggle.classList.toggle('active');
            dropdownMenu.classList.toggle('active');
        });
        
        // Cerrar el menú al hacer clic fuera de él
        document.addEventListener('click', function(event) {
            if (!menuToggle.contains(event.target) && !dropdownMenu.contains(event.target)) {
                menuToggle.classList.remove('active');
                dropdownMenu.classList.remove('active');
            }
        });
        
        // Función para inicializar los mapas
        function inicializarMapas() {
            // Coordenadas centrales de Nicaragua
            const centroNicaragua = [12.8654, -85.2072];
            
            // Inicializar mapa de terminales
            const mapaTerminales = L.map('mapa-terminales').setView(centroNicaragua, 7);
            
            // Añadir capa de OpenStreetMap al mapa de terminales
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(mapaTerminales);
            
            // Añadir marcadores de terminales principales
            const terminales = [
                {nombre: "Terminal de Managua", coords: [12.1328, -86.2504]},
                {nombre: "Terminal de León", coords: [12.4379, -86.8780]},
                {nombre: "Terminal de Granada", coords: [11.9299, -85.9590]},
                {nombre: "Terminal de Masaya", coords: [11.9744, -86.0942]},
                {nombre: "Terminal de Rivas", coords: [11.4348, -85.8255]},
                {nombre: "Terminal de Bluefields", coords: [12.0145, -83.7679]},
                {nombre: "Terminal de Bilwi", coords: [14.0226, -83.3939]}
            ];
            
            terminales.forEach(terminal => {
                L.marker(terminal.coords)
                    .addTo(mapaTerminales)
                    .bindPopup(`<b>${terminal.nombre}</b>`);
            });
            
            // Inicializar mapa de rutas turísticas
            const mapaRutas = L.map('mapa-rutas').setView(centroNicaragua, 7);
            
            // Añadir capa de OpenStreetMap al mapa de rutas
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(mapaRutas);
            
            // Añadir marcadores de rutas turísticas
            const rutas = [
                {nombre: "Volcán Masaya", coords: [11.9846, -86.1603]},
                {nombre: "Isla de Ometepe", coords: [11.4941, -85.5167]},
                {nombre: "Granada", coords: [11.9299, -85.9590]},
                {nombre: "León", coords: [12.4379, -86.8780]},
                {nombre: "San Juan del Sur", coords: [11.2529, -85.8702]},
                {nombre: "Corn Islands", coords: [12.1625, -83.0389]},
                {nombre: "Reserva Bosawás", coords: [13.8333, -84.8333]}
            ];
            
            rutas.forEach(ruta => {
                L.marker(ruta.coords)
                    .addTo(mapaRutas)
                    .bindPopup(`<b>${ruta.nombre}</b>`);
            });
            
            // Añadir evento de clic al mapa de terminales
            document.getElementById('mapa-terminales').addEventListener('click', function() {
                window.location.href = 'terminales-nacionales.html';
            });
            
            // Añadir evento de clic al mapa de rutas
            document.getElementById('mapa-rutas').addEventListener('click', function() {
                window.location.href = 'rutas-turisticas-nacionales.html';
            });
        }
        
        // Llamar a la función cuando el DOM esté cargado
        document.addEventListener('DOMContentLoaded', function() {
            // Cargar la librería Leaflet dinámicamente
            const leafletCSS = document.createElement('link');
            leafletCSS.rel = 'stylesheet';
            leafletCSS.href = 'https://unpkg.com/leaflet@1.7.1/dist/leaflet.css';
            document.head.appendChild(leafletCSS);
            
            const leafletJS = document.createElement('script');
            leafletJS.src = 'https://unpkg.com/leaflet@1.7.1/dist/leaflet.js';
            leafletJS.onload = inicializarMapas;
            document.head.appendChild(leafletJS);
        });