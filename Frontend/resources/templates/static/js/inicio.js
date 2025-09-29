 // Funcionalidad del menú desplegable
        const menuToggle = document.getElementById('menuToggle');
        const dropdownMenu = document.getElementById('dropdownMenu');
        const searchForm = document.getElementById('searchForm');
        const searchInput = document.getElementById('searchInput');
        const departmentContent = document.getElementById('departmentContent');
        const departmentVideo = document.getElementById('departmentVideo');
        const infoTitle = document.getElementById('infoTitle');
        const infoText = document.getElementById('infoText');
        const attractionsList = document.getElementById('attractionsList');
        const closeContent = document.getElementById('closeContent');
        const filterTags = document.querySelectorAll('.filter-tag');
        const filtersContainer = document.getElementById('filtersContainer');
        const filtersScroll = document.getElementById('filtersScroll');
        const staffContainer = document.getElementById('staffContainer');
        const staffScroll = document.getElementById('staffScroll');
        
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
        
        // Funcionalidad del buscador
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const searchTerm = searchInput.value.trim().toLowerCase();
            
            if (!searchTerm) return;
            
            // Mapeo de términos de búsqueda a páginas
            const searchMap = {
                // Reservas
                'reserva': 'reservas.html',
                'reservar': 'reservas.html',
                'tour': 'reservas.html',
                'tours': 'reservas.html',
                'actividad': 'reservas.html',
                'actividades': 'reservas.html',
                'experiencia': 'reservas.html',
                'experiencias': 'reservas.html',
                'aventura': 'reservas.html',
                'volcan': 'reservas.html',
                'volcán': 'reservas.html',
                'volcanes': 'reservas.html',
                
                // Hospedaje
                'hospedaje': 'hospedaje.html',
                'hotel': 'hospedaje.html',
                'hoteles': 'hospedaje.html',
                'alojamiento': 'hospedaje.html',
                'cabaña': 'hospedaje.html',
                'cabañas': 'hospedaje.html',
                'resort': 'hospedaje.html',
                'resorts': 'hospedaje.html',
                'eco lodge': 'hospedaje.html',
                'eco-lodge': 'hospedaje.html',
                
                // Transporte
                'transporte': 'transporte.html',
                'bus': 'transporte.html',
                'autobus': 'transporte.html',
                'autobús': 'transporte.html',
                'coche': 'transporte.html',
                'carro': 'transporte.html',
                'vehiculo': 'transporte.html',
                'vehículo': 'transporte.html',
                'alquiler': 'transporte.html',
                'traslado': 'transporte.html',
                'vuelo': 'transporte.html',
                'vuelos': 'transporte.html',
                
                // Guía
                'guia': 'guia.html',
                'guía': 'guia.html',
                'informacion': 'guia.html',
                'información': 'guia.html',
                'turismo': 'guia.html',
                'destino': 'guia.html',
                'destinos': 'guia.html',
                'ruta': 'guia.html',
                'rutas': 'guia.html',
                'gastronomia': 'guia.html',
                'gastronomía': 'guia.html',
                'cultura': 'guia.html',
                
                // Comunidad
                'comunidad': 'comunidad.html',
                'foro': 'comunidad.html',
                'galeria': 'comunidad.html',
                'galería': 'comunidad.html',
                'foto': 'comunidad.html',
                'fotos': 'comunidad.html',
                'evento': 'comunidad.html',
                'eventos': 'comunidad.html',
                'fiesta': 'comunidad.html',
                'fiestas': 'comunidad.html',
                
                // Perfil
                'perfil': 'perfil.html',
                'cuenta': 'perfil.html',
                'usuario': 'perfil.html',
                'registro': 'perfil.html',
                'login': 'perfil.html',
                'sesion': 'perfil.html',
                'sesión': 'perfil.html',
                
                // Sobre Nosotros
                'sobre': 'about.html',
                'nosotros': 'about.html',
                'empresa': 'about.html',
                'historia': 'about.html',
                'mision': 'about.html',
                'misión': 'about.html',
                'vision': 'about.html',
                'visión': 'about.html',
                'equipo': 'about.html',
                
                // Actualizaciones
                'actualizacion': 'updates.html',
                'actualización': 'updates.html',
                'actualizaciones': 'updates.html',
                'novedades': 'updates.html',
                'noticias': 'updates.html',
                'nuevo': 'updates.html',
                'nueva': 'updates.html',
                'promocion': 'updates.html',
                'promoción': 'updates.html',
                'oferta': 'updates.html',
                
                // Destinos populares
                'granada': 'granada.html',
                'leon': 'leon.html',
                'león': 'leon.html',
                'ometepe': 'ometepe.html',
                'san juan': 'sanjuan.html',
                'san juan del sur': 'sanjuan.html',
                'corn islands': 'cornislands.html',
                'masaya': 'masaya.html'
            };
            
            // Buscar coincidencias
            let foundPage = null;
            
            // Buscar coincidencia exacta
            if (searchMap[searchTerm]) {
                foundPage = searchMap[searchTerm];
            } else {
                // Buscar coincidencias parciales
                for (const term in searchMap) {
                    if (searchTerm.includes(term) || term.includes(searchTerm)) {
                        foundPage = searchMap[term];
                        break;
                    }
                }
            }
            
            // Redirigir a la página encontrada o a resultados de búsqueda
            if (foundPage) {
                window.location.href = foundPage;
            } else {
                // Si no hay coincidencia, ir a página de resultados con el término de búsqueda
                window.location.href = `resultados.html?q=${encodeURIComponent(searchTerm)}`;
            }
        });
        
        // Datos de los departamentos
        const departments = [
            {
                name: 'Managua',
                video: 'https://www.youtube.com/embed/4q0gGqY2K5A',
                description: 'Managua es la capital de Nicaragua, una ciudad vibrante que combina historia moderna con cultura tradicional. Ubicada junto al lago Xolotlán, ofrece una mezcla única de arquitectura colonial y contemporánea. Es el centro político, económico y cultural del país, con una vida nocturna activa y numerosos centros comerciales.',
                attractions: [
                    'Plaza de la Revolución',
                    'Antigua Catedral de Managua',
                    'Lago Xolotlán',
                    'Puerto Salvador Allende',
                    'Palacio Nacional de la Cultura',
                    'Tiscapa'
                ]
            },
            {
                name: 'Granada',
                video: 'https://www.youtube.com/embed/2s2t3v5tX7c',
                description: 'Granada es una de las ciudades más antiguas de América, fundada en 1524. Con su arquitectura colonial bien conservada, iglesias históricas y coloridas casas, es uno de los destinos turísticos más populares de Nicaragua. Su casco antiguo es un tesoro histórico con calles empedradas y hermosos edificios coloniales.',
                attractions: [
                    'Iglesia La Merced',
                    'Catedral de Granada',
                    'Las Isletas',
                    'Volcán Mombacho',
                    'Casa de los Tres Mundos',
                    'Convento San Francisco'
                ]
            },
            {
                name: 'León',
                video: 'https://www.youtube.com/embed/6s6t7v7uY9d',
                description: 'León es la segunda ciudad más antigua de Nicaragua y fue la capital del país hasta 1852. Conocida por su rica historia, sus impresionantes iglesias coloniales y su vibrante vida cultural y estudiantil. Es la cuna de poetas y revolucionarios, con una fuerte identidad cultural.',
                attractions: [
                    'Catedral de León',
                    'Ruinas de León Viejo',
                    'Volcán Cerro Negro',
                    'Museo de Arte Fundación Ortiz-Gurdián',
                    'Iglesia El Calvario',
                    'Centro de Arte Fundación Ortiz-Gurdián'
                ]
            },
            {
                name: 'Masaya',
                video: 'https://www.youtube.com/embed/8s8t9v9wY1e',
                description: 'Masaya es conocida como la "Ciudad de las Flores" y es famosa por su artesanía, especialmente los hamacas y cerámicas. El Parque Nacional Volcán Masaya es una de sus principales atracciones, donde se puede ver un lago de lava activo. Es el corazón del folclore nicaragüense.',
                attractions: [
                    'Volcán Masaya',
                    'Mercado de Artesanías',
                    'Laguna de Masaya',
                    'Pueblo Blanco',
                    'San Juan de Oriente',
                    'Coyotepe'
                ]
            },
            {
                name: 'Rivas',
                video: 'https://www.youtube.com/embed/9s9t0v0xY2f',
                description: 'Rivas es un departamento con una gran diversidad geográfica que incluye playas del Pacífico, islas en el lago Cocibolca y áreas agrícolas. San Juan del Sur es su destino más famoso, conocido por sus playas ideales para el surf y su vibrante vida nocturna. La isla de Ometepe, con sus dos volcanes, es una joya natural.',
                attractions: [
                    'San Juan del Sur',
                    'Playa El Coco',
                    'Isla Ometepe',
                    'Playa La Flor',
                    'Volcán Concepción',
                    'Volcán Maderas'
                ]
            },
            {
                name: 'Carazo',
                video: 'https://www.youtube.com/embed/1s1t2v3yY4g',
                description: 'Carazo es conocido por su clima fresco y sus paisajes montañosos. La ciudad de Diriamba es su cabecera departamental, famosa por su arquitectura colonial y tradiciones culturales. Es una región cafetalera con hermosas cascadas y reservas naturales.',
                attractions: [
                    'Cascada Blanca',
                    'Reserva Natural El Chocoyero',
                    'Diriamba',
                    'Casares',
                    'San Marcos',
                    'La Boquita'
                ]
            },
            {
                name: 'Chinandega',
                video: 'https://www.youtube.com/embed/2s2t3v4zY5h',
                description: 'Chinandega es un departamento agrícola conocido como la "Capital del Azúcar". Sus playas en el Pacífico, como Jiquilillo y Poneloya, son populares entre los surfistas. El volcán San Cristóbal es el más alto de Nicaragua y la reserva natural Cosigüina ofrece un impresionante paisaje.',
                attractions: [
                    'Volcán San Cristóbal',
                    'Playa Jiquilillo',
                    'Reserva Natural Cosigüina',
                    'El Viejo',
                    'Chichigalpa',
                    'Corinto'
                ]
            },
            {
                name: 'Estelí',
                video: 'https://www.youtube.com/embed/3s3t4v5aY6j',
                description: 'Estelí es conocido como el "Diamante de las Segovias" por su belleza natural y su importancia económica. Es famoso por sus cigarros puros y su producción de café de alta calidad. La reserva natural Miraflor es un paraíso para los amantes del ecoturismo.',
                attractions: [
                    'Reserva Natural Miraflor',
                    'Estación Biológica',
                    'Cascada Estanzuela',
                    'Museo de Arqueología',
                    'Tisey',
                    'La Garnacha'
                ]
            },
            {
                name: 'Matagalpa',
                video: 'https://www.youtube.com/embed/4s4t5v6bY7k',
                description: 'Matagalpa es conocido como la "Perla del Septentrión" por su belleza natural. Es el principal productor de café de Nicaragua y tiene un clima fresco y montañoso. La reserva natural Selva Negra es un ejemplo de conservación y turismo sostenible.',
                attractions: [
                    'Reserva Natural Selva Negra',
                    'Cascada Santa Emilia',
                    'Apante',
                    'Ciudad Darío',
                    'El Arenal',
                    'San Ramón'
                ]
            },
            {
                name: 'Jinotega',
                video: 'https://www.youtube.com/embed/5s5t6v7cY8l',
                description: 'Jinotega es conocido como la "Ciudad de las Brumas" por su clima fresco y neblinoso. Es uno de los principales productores de café de Nicaragua y tiene impresionantes paisajes montañosos. El lago Apanás es un importante recurso hídrico y atractivo turístico.',
                attractions: [
                    'Reserva Natural Datanlí - El Diablo',
                    'Lago Apanás',
                    'Cerro El Bosque',
                    'Cascada La Bujona',
                    'San Rafael del Norte',
                    'Wiwilí'
                ]
            },
            {
                name: 'Nueva Segovia',
                video: 'https://www.youtube.com/embed/6s6t7v8dY9m',
                description: 'Nueva Segovia es conocido por sus montañas y su producción de café. Ocotal es su cabecera departamental y la ciudad de Jalapa es famosa por sus artesanías y su clima fresco. El Mogotón es el punto más alto de Nicaragua.',
                attractions: [
                    'Reserva Natural Tisey-Estanzuela',
                    'Mogotón',
                    'Ocotal',
                    'Jalapa',
                    'Murra',
                    'Ciudad Antigua'
                ]
            },
            {
                name: 'Madriz',
                video: 'https://www.youtube.com/embed/7s7t8v9eY0n',
                description: 'Madriz es conocido como la "Tierra de los Acuerdos" por su historia de paz y reconciliación. Somoto es su cabecera departamental y es famosa por el cañón del río Coco, una formación geológica impresionante. Es una región con gran producción de tabaco y café.',
                attractions: [
                    'Cañón de Somoto',
                    'Cerro El Tuma',
                    'Reserva Natural Cerro Tisey',
                    'Santa María',
                    'Totogalpa',
                    'Yalagüina'
                ]
            },
            {
                name: 'Boaco',
                video: 'https://www.youtube.com/embed/8s8t9v0fY1p',
                description: 'Boaco es conocido como la "Ciudad de las Dos Piernas" por su topografía montañosa. Tiene un clima agradable y es famoso por su ganado y productos lácteos. La región combina paisajes de montaña con valles fértiles.',
                attractions: [
                    'Cerro La Vieja',
                    'Tepeyac',
                    'Camoapa',
                    'San José de los Remates',
                    'Boaco',
                    'San Lorenzo'
                ]
            },
            {
                name: 'Chontales',
                video: 'https://www.youtube.com/embed/9s9t0v1gY2q',
                description: 'Chontales es conocido por su ganado y sus minas de oro. Juigalpa es su cabecera departamental y el lago Cocibolca bordea parte de su territorio. Es una región con una rica historia precolombina y colonial.',
                attractions: [
                    'Lago Cocibolca',
                    'Museo Paleontológico',
                    'Amerrisque',
                    'Comalapa',
                    'Acoyapa',
                    'Santo Domingo'
                ]
            },
            {
                name: 'Río San Juan',
                video: 'https://www.youtube.com/embed/0s0t1v2hY3r',
                description: 'Río San Juan es conocido por su biodiversidad y su historia. El río San Juan conecta el lago Cocibolca con el Mar Caribe y fue una ruta importante durante la época colonial. Es una región de exuberante selva tropical y rica vida silvestre.',
                attractions: [
                    'Reserva Biológica Indio Maíz',
                    'Castillo de la Inmaculada Concepción',
                    'El Castillo',
                    'Refugio Bartola',
                    'San Juan del Norte',
                    'Isla de Ometepe'
                ]
            },
            {
                name: 'Atlántico Norte',
                video: 'https://www.youtube.com/embed/1s1t2v3iY4s',
                description: 'La Región Autónoma del Caribe Norte es conocida por su diversidad cultural y natural. Puerto Cabezas es su principal ciudad y es hogar de comunidades indígenas y afrodescendientes. La reserva biosfera Bosawás es uno de los pulmones de Centroamérica.',
                attractions: [
                    'Reserva Biosfera Bosawás',
                    'Puerto Cabezas',
                    'Cayos Miskitos',
                    'Waspam',
                    'Prinzapolka',
                    'Rosita'
                ]
            },
            {
                name: 'Atlántico Sur',
                video: 'https://www.youtube.com/embed/2s2t3v4jY5t',
                description: 'La Región Autónoma del Caribe Sur es conocida por sus playas caribeñas y su cultura creole. Bluefields es su capital y las Corn Islands son sus principales destinos turísticos. Es una región con una fuerte influencia afrocaribeña.',
                attractions: [
                    'Corn Islands',
                    'Bluefields',
                    'Pearl Lagoon',
                    'Karawala',
                    'Kukra Hill',
                    'Laguna de Perlas'
                ]
            }
        ];
        
        // Variable para almacenar el filtro seleccionado
        let selectedFilter = null;
        
        // Funcionalidad de deslizamiento manual para departamentos
        let isDown = false;
        let startX;
        let scrollLeft;
        let currentTransform = 0;
        let animationStartTime = Date.now();
        let animationDuration = 40000; // 40 segundos para un ciclo completo
        let isDragging = false;
        let clickTimeout;
        
        // Función para obtener la posición actual de la animación
        function getCurrentAnimationPosition() {
            const elapsed = Date.now() - animationStartTime;
            const progress = (elapsed % animationDuration) / animationDuration;
            return progress * -50; // -50% porque es la mitad del contenido duplicado
        }
        
        // Funcionalidad de deslizamiento para filtros de departamentos
        filtersContainer.addEventListener('mousedown', (e) => {
            // Si el clic es en un filtro, no iniciar el arrastre inmediatamente
            if (e.target.classList.contains('filter-tag')) {
                // Esperar un poco para ver si es un arrastre o un clic
                clickTimeout = setTimeout(() => {
                    isDown = true;
                    filtersScroll.classList.add('dragging');
                    startX = e.pageX - filtersContainer.offsetLeft;
                    scrollLeft = currentTransform;
                    filtersContainer.style.cursor = 'grabbing';
                }, 100);
            } else {
                // Si no es un filtro, iniciar el arrastre inmediatamente
                isDown = true;
                filtersScroll.classList.add('dragging');
                startX = e.pageX - filtersContainer.offsetLeft;
                scrollLeft = currentTransform;
                filtersContainer.style.cursor = 'grabbing';
            }
        });
        
        filtersContainer.addEventListener('mouseleave', () => {
            isDown = false;
            isDragging = false;
            clearTimeout(clickTimeout);
            filtersScroll.classList.remove('dragging');
            filtersContainer.style.cursor = 'grab';
            // Reanudar la animación desde la posición actual
            animationStartTime = Date.now() - (Math.abs(currentTransform) / 50 * animationDuration);
            filtersScroll.style.transform = `translateX(${currentTransform}%)`;
        });
        
        filtersContainer.addEventListener('mouseup', () => {
            isDown = false;
            isDragging = false;
            clearTimeout(clickTimeout);
            filtersScroll.classList.remove('dragging');
            filtersContainer.style.cursor = 'grab';
            // Reanudar la animación desde la posición actual
            animationStartTime = Date.now() - (Math.abs(currentTransform) / 50 * animationDuration);
            filtersScroll.style.transform = `translateX(${currentTransform}%)`;
        });
        
        filtersContainer.addEventListener('mousemove', (e) => {
            if (!isDown) return;
            e.preventDefault();
            isDragging = true;
            clearTimeout(clickTimeout);
            
            const x = e.pageX - filtersContainer.offsetLeft;
            const walk = (x - startX) * 0.5; // Velocidad de deslizamiento
            const containerWidth = filtersContainer.offsetWidth;
            const scrollWidth = filtersScroll.offsetWidth;
            
            // Calcular el nuevo porcentaje de transformación
            const walkPercentage = (walk / containerWidth) * 100;
            currentTransform = scrollLeft + walkPercentage;
            
            // Limitar el deslizamiento para no pasar de los límites
            if (currentTransform > 0) currentTransform = 0;
            if (currentTransform < -50) currentTransform = -50;
            
            filtersScroll.style.transform = `translateX(${currentTransform}%)`;
        });
        
        // Soporte para dispositivos táctiles para filtros de departamentos
        filtersContainer.addEventListener('touchstart', (e) => {
            // Si el toque es en un filtro, no iniciar el arrastre inmediatamente
            if (e.target.classList.contains('filter-tag')) {
                clickTimeout = setTimeout(() => {
                    isDown = true;
                    filtersScroll.classList.add('dragging');
                    startX = e.touches[0].pageX - filtersContainer.offsetLeft;
                    scrollLeft = currentTransform;
                }, 100);
            } else {
                isDown = true;
                filtersScroll.classList.add('dragging');
                startX = e.touches[0].pageX - filtersContainer.offsetLeft;
                scrollLeft = currentTransform;
            }
        });
        
        filtersContainer.addEventListener('touchend', () => {
            isDown = false;
            isDragging = false;
            clearTimeout(clickTimeout);
            filtersScroll.classList.remove('dragging');
            // Reanudar la animación desde la posición actual
            animationStartTime = Date.now() - (Math.abs(currentTransform) / 50 * animationDuration);
            filtersScroll.style.transform = `translateX(${currentTransform}%)`;
        });
        
        filtersContainer.addEventListener('touchmove', (e) => {
            if (!isDown) return;
            isDragging = true;
            clearTimeout(clickTimeout);
            
            const x = e.touches[0].pageX - filtersContainer.offsetLeft;
            const walk = (x - startX) * 0.5;
            const containerWidth = filtersContainer.offsetWidth;
            
            const walkPercentage = (walk / containerWidth) * 100;
            currentTransform = scrollLeft + walkPercentage;
            
            if (currentTransform > 0) currentTransform = 0;
            if (currentTransform < -50) currentTransform = -50;
            
            filtersScroll.style.transform = `translateX(${currentTransform}%)`;
        });
        
        // Actualizar la posición de la animación periódicamente cuando no se está arrastrando
        setInterval(() => {
            if (!isDown && !filtersScroll.classList.contains('dragging')) {
                currentTransform = getCurrentAnimationPosition();
                filtersScroll.style.transform = `translateX(${currentTransform}%)`;
            }
        }, 50);
        
        // Funcionalidad de deslizamiento para el staff
        let staffIsDown = false;
        let staffStartX;
        let staffScrollLeft;
        let staffCurrentTransform = 0;
        let staffAnimationStartTime = Date.now();
        let staffAnimationDuration = 35000; // 35 segundos para un ciclo completo
        let staffIsDragging = false;
        let staffClickTimeout;
        
        // Función para obtener la posición actual de la animación del staff
        function getStaffCurrentAnimationPosition() {
            const elapsed = Date.now() - staffAnimationStartTime;
            const progress = (elapsed % staffAnimationDuration) / staffAnimationDuration;
            return progress * -50; // -50% porque es la mitad del contenido duplicado
        }
        
        // Funcionalidad de deslizamiento para el staff
        staffContainer.addEventListener('mousedown', (e) => {
            // Si el clic es en una tarjeta de staff, no iniciar el arrastre inmediatamente
            if (e.target.closest('.staff-card')) {
                staffClickTimeout = setTimeout(() => {
                    staffIsDown = true;
                    staffScroll.classList.add('dragging');
                    staffStartX = e.pageX - staffContainer.offsetLeft;
                    staffScrollLeft = staffCurrentTransform;
                    staffContainer.style.cursor = 'grabbing';
                }, 100);
            } else {
                staffIsDown = true;
                staffScroll.classList.add('dragging');
                staffStartX = e.pageX - staffContainer.offsetLeft;
                staffScrollLeft = staffCurrentTransform;
                staffContainer.style.cursor = 'grabbing';
            }
        });
        
        staffContainer.addEventListener('mouseleave', () => {
            staffIsDown = false;
            staffIsDragging = false;
            clearTimeout(staffClickTimeout);
            staffScroll.classList.remove('dragging');
            staffContainer.style.cursor = 'grab';
            // Reanudar la animación desde la posición actual
            staffAnimationStartTime = Date.now() - (Math.abs(staffCurrentTransform) / 50 * staffAnimationDuration);
            staffScroll.style.transform = `translateX(${staffCurrentTransform}%)`;
        });
        
        staffContainer.addEventListener('mouseup', () => {
            staffIsDown = false;
            staffIsDragging = false;
            clearTimeout(staffClickTimeout);
            staffScroll.classList.remove('dragging');
            staffContainer.style.cursor = 'grab';
            // Reanudar la animación desde la posición actual
            staffAnimationStartTime = Date.now() - (Math.abs(staffCurrentTransform) / 50 * staffAnimationDuration);
            staffScroll.style.transform = `translateX(${staffCurrentTransform}%)`;
        });
        
        staffContainer.addEventListener('mousemove', (e) => {
            if (!staffIsDown) return;
            e.preventDefault();
            staffIsDragging = true;
            clearTimeout(staffClickTimeout);
            
            const x = e.pageX - staffContainer.offsetLeft;
            const walk = (x - staffStartX) * 0.5;
            const containerWidth = staffContainer.offsetWidth;
            
            const walkPercentage = (walk / containerWidth) * 100;
            staffCurrentTransform = staffScrollLeft + walkPercentage;
            
            if (staffCurrentTransform > 0) staffCurrentTransform = 0;
            if (staffCurrentTransform < -50) staffCurrentTransform = -50;
            
            staffScroll.style.transform = `translateX(${staffCurrentTransform}%)`;
        });
        
        // Soporte para dispositivos táctiles para el staff
        staffContainer.addEventListener('touchstart', (e) => {
            if (e.target.closest('.staff-card')) {
                staffClickTimeout = setTimeout(() => {
                    staffIsDown = true;
                    staffScroll.classList.add('dragging');
                    staffStartX = e.touches[0].pageX - staffContainer.offsetLeft;
                    staffScrollLeft = staffCurrentTransform;
                }, 100);
            } else {
                staffIsDown = true;
                staffScroll.classList.add('dragging');
                staffStartX = e.touches[0].pageX - staffContainer.offsetLeft;
                staffScrollLeft = staffCurrentTransform;
            }
        });
        
        staffContainer.addEventListener('touchend', () => {
            staffIsDown = false;
            staffIsDragging = false;
            clearTimeout(staffClickTimeout);
            staffScroll.classList.remove('dragging');
            // Reanudar la animación desde la posición actual
            staffAnimationStartTime = Date.now() - (Math.abs(staffCurrentTransform) / 50 * staffAnimationDuration);
            staffScroll.style.transform = `translateX(${staffCurrentTransform}%)`;
        });
        
        staffContainer.addEventListener('touchmove', (e) => {
            if (!staffIsDown) return;
            staffIsDragging = true;
            clearTimeout(staffClickTimeout);
            
            const x = e.touches[0].pageX - staffContainer.offsetLeft;
            const walk = (x - staffStartX) * 0.5;
            const containerWidth = staffContainer.offsetWidth;
            
            const walkPercentage = (walk / containerWidth) * 100;
            staffCurrentTransform = staffScrollLeft + walkPercentage;
            
            if (staffCurrentTransform > 0) staffCurrentTransform = 0;
            if (staffCurrentTransform < -50) staffCurrentTransform = -50;
            
            staffScroll.style.transform = `translateX(${staffCurrentTransform}%)`;
        });
        
        // Actualizar la posición de la animación del staff periódicamente
        setInterval(() => {
            if (!staffIsDown && !staffScroll.classList.contains('dragging')) {
                staffCurrentTransform = getStaffCurrentAnimationPosition();
                staffScroll.style.transform = `translateX(${staffCurrentTransform}%)`;
            }
        }, 50);
        
        // Mostrar información del departamento
        function showDepartmentInfo(dept, filterElement) {
            // Si ya hay un filtro seleccionado, quitar la selección
            if (selectedFilter) {
                selectedFilter.classList.remove('selected');
            }
            
            // Seleccionar el nuevo filtro
            selectedFilter = filterElement;
            filterElement.classList.add('selected');
            
            // Actualizar el contenido
            infoTitle.textContent = dept.name;
            departmentVideo.src = dept.video;
            infoText.textContent = dept.description;
            
            // Limpiar y generar lista de atractivos
            attractionsList.innerHTML = '';
            dept.attractions.forEach(attraction => {
                const li = document.createElement('li');
                li.innerHTML = `<i class="fas fa-map-marker-alt"></i> ${attraction}`;
                attractionsList.appendChild(li);
            });
            
            // Mostrar la sección de contenido
            departmentContent.classList.add('active');
            
            // Agregar evento para detectar cuando el video termina
            departmentVideo.onload = function() {
                departmentVideo.contentWindow.postMessage('{"event":"command","func":"addEventListener","args":["onStateChange", "onPlayerStateChange"]}', '*');
            };
            
            // Función para manejar el estado del video
            window.onPlayerStateChange = function(event) {
                if (event.data === 0) { // 0 = video terminado
                    // Cerrar la sección de contenido después de un pequeño retraso
                    setTimeout(() => {
                        departmentContent.classList.remove('active');
                        filterElement.classList.remove('selected');
                        selectedFilter = null;
                        departmentVideo.src = ''; // Detener el video
                    }, 1000);
                }
            };
        }
        
        // Cerrar contenido manualmente
        closeContent.addEventListener('click', () => {
            departmentContent.classList.remove('active');
            if (selectedFilter) {
                selectedFilter.classList.remove('selected');
                selectedFilter = null;
            }
            departmentVideo.src = ''; // Detener el video
        });
        
        // Funcionalidad de los filtros
        filterTags.forEach(tag => {
            tag.addEventListener('click', function(e) {
                // Prevenir que el clic en el filtro active el arrastre
                e.stopPropagation();
                
                // Si se estaba arrastrando, no hacer nada
                if (isDragging) {
                    return;
                }
                
                const deptName = this.textContent;
                const dept = departments.find(d => d.name === deptName);
                if (dept) {
                    showDepartmentInfo(dept, this);
                }
            });
        });

        document.addEventListener('DOMContentLoaded', function() {
            const carruseles = document.querySelectorAll('.carrusel-superior, .carrusel-medio, .carrusel-inferior');
            carruseles.forEach(carrusel => {
                carrusel.addEventListener('mouseenter', function() {
                    this.style.animationPlayState = 'paused';
                });
                carrusel.addEventListener('mouseleave', function() {
                    this.style.animationPlayState = 'running';
                });
            });
        });

        document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const searchResults = document.getElementById('searchResults');
    const dropdownMenu = document.getElementById('dropdownMenu');
    const menuItems = dropdownMenu.querySelectorAll('.dropdown-item');
    
    // Mapeo de categorías para búsqueda más efectiva
    const categoryMap = {
        'reservas': ['reservas', 'booking', 'reservar'],
        'hospedaje': ['hospedaje', 'hotel', 'alojamiento', 'hospedarse'],
        'transporte': ['transporte', 'bus', 'coche', 'moverse', 'viajar'],
        'restaurantes': ['restaurantes', 'comida', 'gastronomía', 'comer'],
        'guia': ['guía', 'turismo', 'visitar', 'atractivos'],
        'comunidad': ['comunidad', 'foro', 'opiniones', 'experiencias'],
        'perfil': ['perfil', 'cuenta', 'usuario'],
        'sobre nosotros': ['sobre nosotros', 'información', 'empresa'],
        'actualizaciones': ['actualizaciones', 'novedades', 'noticias']
    };

    // Función para mostrar resultados
    function showResults(query) {
        // Limpiar resultados anteriores
        searchResults.innerHTML = '';
        
        if (query.trim() === '') {
            searchResults.style.display = 'none';
            return;
        }
        
        const normalizedQuery = query.toLowerCase();
        const matchedItems = [];
        
        // Buscar coincidencias en los elementos del menú
        menuItems.forEach(item => {
            const itemText = item.textContent.toLowerCase();
            const itemHref = item.getAttribute('href');
            
            // Verificar si hay coincidencia directa
            if (itemText.includes(normalizedQuery)) {
                matchedItems.push({
                    text: itemText,
                    href: itemHref,
                    icon: item.querySelector('i').className,
                    exactMatch: true
                });
            }
            
            // Verificar coincidencias en categorías
            for (const [category, keywords] of Object.entries(categoryMap)) {
                if (itemText.includes(category) && keywords.some(keyword => keyword.includes(normalizedQuery))) {
                    matchedItems.push({
                        text: itemText,
                        href: itemHref,
                        icon: item.querySelector('i').className,
                        exactMatch: false
                    });
                }
            }
        });
        
        // Eliminar duplicados
        const uniqueItems = matchedItems.filter((item, index, self) => 
            index === self.findIndex(t => t.href === item.href)
        );
        
        if (uniqueItems.length === 0) {
            const noResults = document.createElement('div');
            noResults.className = 'no-results';
            noResults.textContent = 'No se encontraron resultados para "' + query + '"';
            searchResults.appendChild(noResults);
        } else {
            // Ordenar: coincidencias exactas primero
            uniqueItems.sort((a, b) => b.exactMatch - a.exactMatch);
            
            uniqueItems.forEach(item => {
                const resultItem = document.createElement('a');
                resultItem.href = item.href;
                resultItem.className = 'search-result-item';
                
                // Capitalizar primera letra
                const displayText = item.text.charAt(0).toUpperCase() + item.text.slice(1);
                
                resultItem.innerHTML = `
                    <i class="${item.icon}"></i>
                    <span>${displayText}</span>
                `;
                
                searchResults.appendChild(resultItem);
            });
        }
        
        searchResults.style.display = 'block';
    }
    
    // Evento input en el campo de búsqueda
    searchInput.addEventListener('input', function() {
        showResults(this.value);
    });
    
    // Ocultar resultados al hacer clic fuera
    document.addEventListener('click', function(event) {
        if (!searchInput.contains(event.target) && !searchResults.contains(event.target)) {
            searchResults.style.display = 'none';
        }
    });
    
    // Prevenir el envío del formulario
    document.getElementById('searchForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Si hay resultados, redirigir al primero
        const firstResult = searchResults.querySelector('.search-result-item');
        if (firstResult) {
            window.location.href = firstResult.href;
        }
    });
});