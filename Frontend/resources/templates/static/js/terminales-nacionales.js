 // Variables globales
        let map;
        let markers;
        let selectedTerminal = null;
        let selectedBus = null;
        let selectedLugares = [];
        let terminalesData = [];
        let busesData = [];
        let authToken = localStorage.getItem('authToken') || null;
        let lugaresInterval = null;
        let reservaTimeout = null;
        let userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
        let userRole = localStorage.getItem('userRole') || 'USER';
        let userLocation = null;
        let routeControl = null;
        const TASA_CAMBIO = 36.84; // 1 USD = 36.84 NIO (tasa real de Nicaragua)
        const IVA_CORDOBAS = 50; // IVA fijo de 50 córdobas
        
        // Función para verificar el token
        function verificarToken() {
            const token = localStorage.getItem('authToken');
            
            if (!token) {
                // Redirigir a la página de login
                window.location.href = 'iniciosesion.html';
                return false;
            }
            
            // Verificar si el token ha expirado
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                const currentTime = Date.now() / 1000;
                
                if (payload.exp && payload.exp < currentTime) {
                    // Token expirado
                    localStorage.removeItem('authToken');
                    localStorage.removeItem('userInfo');
                    localStorage.removeItem('userRole');
                    window.location.href = '/login.html';
                    return false;
                }
                
                return true;
            } catch (e) {
                // Error al decodificar el token
                localStorage.removeItem('authToken');
                localStorage.removeItem('userInfo');
                localStorage.removeItem('userRole');
                window.location.href = '/login.html';
                return false;
            }
        }
        
        // Inicializar al cargar la página
        window.onload = function() {
            // Verificar token antes de cargar la aplicación
            if (!verificarToken()) {
                return;
            }
            
            // Mostrar la aplicación si el token es válido
            document.getElementById('appContainer').style.display = 'block';
            document.getElementById('userInfo').textContent = userInfo.username || 'Usuario';
            initMap();
            
            // Obtener ubicación del usuario al iniciar
            setTimeout(obtenerUbicacion, 1000);
        };
        
        // Función para mostrar/ocultar pantalla de carga
        function showLoadingScreen(show, text = 'Cargando...', subtext = 'Por favor espera') {
            const loadingOverlay = document.getElementById('loadingOverlay');
            const loadingText = document.getElementById('loadingText');
            const loadingSubtext = document.getElementById('loadingSubtext');
            
            if (show) {
                loadingText.textContent = text;
                loadingSubtext.textContent = subtext;
                loadingOverlay.style.display = 'flex';
            } else {
                loadingOverlay.style.display = 'none';
            }
        }
        
        // Función para cerrar sesión
        function logout() {
            authToken = null;
            userInfo = {};
            userRole = 'USER';
            localStorage.removeItem('authToken');
            localStorage.removeItem('userInfo');
            localStorage.removeItem('userRole');
            
            showNotification('Sesión cerrada correctamente', 'info');
            
            // Redirigir a la página de login
            window.location.href = '/login.html';
        }
        
        // Función para mostrar notificaciones
        function showNotification(message, type = 'info') {
            const notification = document.getElementById('notification');
            notification.textContent = message;
            notification.className = `notification ${type}`;
            notification.classList.add('show');
            
            setTimeout(() => {
                notification.classList.remove('show');
            }, 3000);
        }
        
        // Inicializar el mapa
        function initMap() {
            // Si el mapa ya existe, no reinicializarlo
            if (map) {
                return;
            }
            
            map = L.map('map').setView([12.1350, -86.2650], 8); // Nicaragua
            
            // Agregar capa de OpenStreetMap
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(map);
            
            // Grupo para todos los marcadores
            markers = L.layerGroup().addTo(map);
            
            // Cargar datos
            cargarDatos();
        }
        
        // Función para cargar todos los datos
        async function cargarDatos() {
            const endpointStatus = document.getElementById('endpointStatus');
            const errorMessage = document.getElementById('errorMessage');
            const loadingMessage = document.getElementById('loadingMessage');
            
            // Mostrar pantalla de carga
            showLoadingScreen(true, 'Cargando terminales...', 'Obteniendo ubicaciones del mapa');
            
            endpointStatus.innerHTML = '';
            errorMessage.style.display = 'none';
            
            try {
                // Cargar terminales
                const terminalesResponse = await verificarEndpoint('https://wani-connect.onrender.com/api/terminales-buses', 'Terminales');
                
                if (terminalesResponse.success) {
                    terminalesData = terminalesResponse.data;
                    procesarTerminales();
                }
                
                // Ocultar mensaje de carga
                loadingMessage.style.display = 'none';
                
                // Ajustar el mapa para mostrar todos los marcadores
                if (markers.getLayers().length > 0) {
                    // Crear un grupo de capas para obtener los límites
                    const group = new L.featureGroup(markers.getLayers());
                    map.fitBounds(group.getBounds().pad(0.1));
                } else {
                    errorMessage.textContent = 'No se encontraron terminales para mostrar en el mapa';
                    errorMessage.style.display = 'block';
                }
                
            } catch (error) {
                console.error('Error al cargar datos:', error);
                loadingMessage.style.display = 'none';
                errorMessage.innerHTML = `
                    <p>Error al cargar datos: ${error.message}</p>
                    <button class="retry-button" onclick="location.reload()">Reintentar</button>
                `;
                errorMessage.style.display = 'block';
            } finally {
                showLoadingScreen(false);
            }
        }
        
        // Función para verificar y procesar un endpoint
        async function verificarEndpoint(url, nombre) {
            const endpointStatus = document.getElementById('endpointStatus');
            
            try {
                const controller = new AbortController();
                const timeoutId = setTimeout(() => controller.abort(), 10000);
                
                const headers = {
                    'Accept': 'application/json',
                    'Cache-Control': 'no-cache'
                };
                
                // Si hay token, agregarlo al header
                if (authToken) {
                    headers['Authorization'] = `Bearer ${authToken}`;
                }
                
                const response = await fetch(url, {
                    signal: controller.signal,
                    headers: headers
                });
                
                clearTimeout(timeoutId);
                
                const contentType = response.headers.get('content-type');
                
                if (contentType && contentType.includes('text/html')) {
                    const text = await response.text();
                    throw new Error('El servidor devolvió una página de error');
                }
                
                if (!response.ok) {
                    let errorMessage = `Error ${response.status}: ${response.statusText}`;
                    
                    try {
                        const errorData = await response.json();
                        if (errorData.message) {
                            errorMessage += ` - ${errorData.message}`;
                        }
                    } catch (parseError) {
                        // No se pudo parsear el error
                    }
                    
                    throw new Error(errorMessage);
                }
                
                let data;
                try {
                    data = await response.json();
                } catch (jsonError) {
                    try {
                        const text = await response.text();
                        throw new Error(`Error al parsear JSON: ${jsonError.message}. Respuesta: ${text.substring(0, 200)}...`);
                    } catch (textError) {
                        throw new Error(`Error al procesar la respuesta: ${jsonError.message}`);
                    }
                }
                
                if (!Array.isArray(data)) {
                    throw new Error(`La respuesta no es un array válido: ${typeof data}`);
                }
                
                endpointStatus.innerHTML += `
                    <div class="endpoint-status endpoint-success">
                        <i class="fas fa-check-circle"></i> 
                        ${nombre} cargados correctamente (${data.length} elementos)
                    </div>
                `;
                
                return { success: true, data };
                
            } catch (error) {
                let errorMsg = error.message;
                
                if (error.name === 'AbortError') {
                    errorMsg = 'Timeout: El servidor tardó demasiado tiempo en responder';
                }
                
                // Si el error es 401, redirigir a login
                if (error.message.includes('401')) {
                    logout();
                    return { success: false, error: 'Sesión expirada. Por favor, inicia sesión nuevamente.' };
                }
                
                endpointStatus.innerHTML += `
                    <div class="endpoint-status endpoint-error">
                        <i class="fas fa-times-circle"></i> 
                        Error al cargar ${nombre}: ${errorMsg}
                    </div>
                `;
                
                return { success: false, error: errorMsg };
            }
        }
        
        // Función para procesar terminales
        function procesarTerminales() {
            if (!terminalesData || terminalesData.length === 0) return;
            
            terminalesData.forEach((terminal, index) => {
                if (terminal.ubicacionGeografica && terminal.ubicacionGeografica.latitud && terminal.ubicacionGeografica.longitud) {
                    const busIcon = L.divIcon({
                        html: '<i class="fas fa-bus" style="color: #e74c3c; font-size: 24px;"></i>',
                        iconSize: [24, 24],
                        className: 'custom-div-icon'
                    });
                    
                    const marker = L.marker(
                        [terminal.ubicacionGeografica.latitud, terminal.ubicacionGeografica.longitud], 
                        { icon: busIcon }
                    )
                    .bindPopup(`
                        <div style="text-align: center;">
                            <h3>${terminal.nombre}</h3>
                            <p><strong>Localidad:</strong> ${terminal.localidad}</p>
                            <p><strong>Código:</strong> ${terminal.codigoUnico}</p>
                            <button class="btn btn-primary btn-sm" onclick="showTerminalDetails(${index})">Ver detalles</button>
                        </div>
                    `);
                    
                    markers.addLayer(marker);
                }
            });
        }
        
        // Función para mostrar detalles de la terminal
        function showTerminalDetails(index) {
            const terminal = terminalesData[index];
            selectedTerminal = terminal;
            
            // Actualizar modal con todos los detalles
            document.getElementById('terminalName').textContent = terminal.nombre;
            document.getElementById('terminalLocation').textContent = terminal.localidad;
            document.getElementById('terminalCodigo').textContent = terminal.codigoUnico;
            document.getElementById('terminalLatitud').textContent = terminal.ubicacionGeografica.latitud;
            document.getElementById('terminalLongitud').textContent = terminal.ubicacionGeografica.longitud;
            document.getElementById('terminalDireccion').textContent = terminal.ubicacionGeografica.direccion || 'No especificada';
            
            // Generar imagen SVG para la terminal
            document.getElementById('terminalImage').src = createTerminalImage(terminal.nombre, terminal.localidad);
            
            // Mostrar modal
            document.getElementById('terminalModal').style.display = 'block';
            
            // Ocultar secciones que se muestran después de acciones
            document.getElementById('lugaresContainer').style.display = 'none';
            
            // Mostrar la sección de buses con un indicador de carga
            const busDetails = document.getElementById('busDetails');
            busDetails.style.display = 'block';
            document.getElementById('busesList').innerHTML = '<div class="bus-loading"><i class="fas fa-spinner fa-spin"></i> Cargando buses...</div>';
            
            // Cargar buses de esta terminal usando el código único
            cargarBusesPorTerminal(terminal.codigoUnico);
        }
        
        // Función para cargar buses por terminal usando el código único
        async function cargarBusesPorTerminal(codigoUnico) {
            // Mostrar pantalla de carga
            showLoadingScreen(true, 'Cargando buses...', 'Obteniendo horarios y destinos');
            
            try {
                // Primero intentar con el endpoint de código único
                let response = await fetch(`https://wani-connect.onrender.com/api/buses/terminal/codigo/${codigoUnico}`, {
                    headers: {
                        'Authorization': `Bearer ${authToken}`
                    }
                });
                
                // Si el endpoint de código único falla, intentar con el ID
                if (!response.ok) {
                    console.log('Endpoint de código único falló, intentando con ID');
                    
                    // Buscar la terminal por código único en los datos cargados
                    const terminal = terminalesData.find(t => t.codigoUnico === codigoUnico);
                    if (terminal && terminal.id) {
                        response = await fetch(`https://wani-connect.onrender.com/api/buses/terminal/${terminal.id}`, {
                            headers: {
                                'Authorization': `Bearer ${authToken}`
                            }
                        });
                    }
                }
                
                if (!response.ok) {
                    throw new Error(`Error ${response.status}: ${response.statusText}`);
                }
                
                let buses;
                try {
                    buses = await response.json();
                } catch (jsonError) {
                    throw new Error(`Error al procesar la respuesta: ${jsonError.message}`);
                }
                
                busesData = buses;
                
                const busesList = document.getElementById('busesList');
                busesList.innerHTML = '';
                
                if (busesData.length === 0) {
                    busesList.innerHTML = '<p>No hay buses disponibles en esta terminal</p>';
                    return;
                }
                
                // Crear tarjetas para cada bus
                busesData.forEach((bus, index) => {
                    const busCard = document.createElement('div');
                    busCard.className = 'card mb-3';
                    busCard.innerHTML = `
                        <div class="card-body">
                            <div class="bus-info">
                                <div class="bus-info-item">
                                    <div class="bus-info-label">Número de Bus</div>
                                    <div class="bus-info-value">${bus.numeroBus}</div>
                                </div>
                                <div class="bus-info-item">
                                    <div class="bus-info-label">Destino</div>
                                    <div class="bus-info-value">${bus.destino}</div>
                                </div>
                                <div class="bus-info-item">
                                    <div class="bus-info-label">Hora Salida</div>
                                    <div class="bus-info-value">${bus.horaSalida}</div>
                                </div>
                                <div class="bus-info-item">
                                    <div class="bus-info-label">Total de Lugares</div>
                                    <div class="bus-info-value">${bus.totalLugares}</div>
                                </div>
                                <div class="bus-info-item">
                                    <div class="bus-info-label">Precio por Asiento</div>
                                    <div class="bus-info-value">C$${bus.precio ? bus.precio.toFixed(2) : '921.00'} NIO</div>
                                </div>
                            </div>
                            <button class="btn btn-primary btn-sm" onclick="seleccionarBus(${index})">Seleccionar Bus</button>
                        </div>
                    `;
                    busesList.appendChild(busCard);
                });
                
            } catch (error) {
                console.error('Error al cargar buses:', error);
                document.getElementById('busesList').innerHTML = `
                    <div class="error">
                        <p>Error al cargar los buses: ${error.message}</p>
                        <p>Verifica que la terminal exista y tenga buses asociados.</p>
                        <button class="retry-button" onclick="cargarBusesPorTerminal('${codigoUnico}')">Reintentar</button>
                    </div>
                `;
            } finally {
                showLoadingScreen(false);
            }
        }
        
        // Función para crear imagen SVG dinámica
        function createTerminalImage(nombre, localidad) {
            const svg = `
                <svg xmlns="http://www.w3.org/2000/svg" width="400" height="300" viewBox="0 0 400 300">
                    <defs>
                        <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="100%">
                            <stop offset="0%" style="stop-color:#3498db;stop-opacity:1" />
                            <stop offset="100%" style="stop-color:#2980b9;stop-opacity:1" />
                        </linearGradient>
                    </defs>
                    <rect width="400" height="300" fill="url(#grad1)"/>
                    <rect x="50" y="50" width="300" height="200" fill="white" opacity="0.9" rx="10"/>
                    <text x="200" y="120" font-family="Arial" font-size="24" font-weight="bold" text-anchor="middle" fill="#2c3e50">${nombre}</text>
                    <text x="200" y="150" font-family="Arial" font-size="16" text-anchor="middle" fill="#7f8c8d">Terminal de Buses</text>
                    <text x="200" y="180" font-family="Arial" font-size="14" text-anchor="middle" fill="#7f8c8d">${localidad}</text>
                    <circle cx="100" cy="220" r="8" fill="#e74c3c"/>
                    <circle cx="200" cy="220" r="8" fill="#e74c3c"/>
                    <circle cx="300" cy="220" r="8" fill="#e74c3c"/>
                </svg>
            `;
            
            return 'data:image/svg+xml;base64,' + btoa(unescape(encodeURIComponent(svg)));
        }
        
        // Seleccionar un bus específico
        function seleccionarBus(index) {
            selectedBus = busesData[index];
            selectedLugares = []; // Resetear selección de lugares
            document.getElementById('confirmarReservaBtn').disabled = true;
            document.getElementById('resumenSeleccion').style.display = 'none';
            
            // Mostrar lugares y cargar desde el backend
            mostrarLugares();
        }
        
        // Mostrar lugares disponibles en cuadrícula
        function mostrarLugares() {
            if (!selectedBus) {
                showNotification('Debe seleccionar un bus primero', 'error');
                return;
            }
            
            // Mostrar contenedor de lugares
            document.getElementById('lugaresContainer').style.display = 'block';
            
            // Actualizar contador de lugares disponibles
            document.getElementById('lugaresDisponiblesValor').textContent = selectedBus.lugaresDisponibles;
            
            // Mostrar indicador de carga
            const lugaresGrid = document.getElementById('lugaresGrid');
            lugaresGrid.innerHTML = '<div class="bus-loading"><i class="fas fa-spinner fa-spin"></i> Cargando lugares...</div>';
            
            // Configurar actualización periódica
            if (lugaresInterval) {
                clearInterval(lugaresInterval);
            }
            
            lugaresInterval = setInterval(() => {
                cargarLugaresBus();
            }, 10000);
            
            // Cargar lugares inmediatamente
            cargarLugaresBus();
        }
        
        // Función para cargar los lugares de un bus
        async function cargarLugaresBus() {
            const lugaresGrid = document.getElementById('lugaresGrid');
            
            // Mostrar pantalla de carga solo en la primera carga, no en actualizaciones
            const isFirstLoad = lugaresGrid.innerHTML.includes('Cargando lugares...');
            if (isFirstLoad) {
                showLoadingScreen(true, 'Cargando asientos...', 'Verificando disponibilidad');
            }
            
            try {
                // Verificar que selectedBus existe y tiene un ID
                if (!selectedBus || !selectedBus.id) {
                    throw new Error('No se ha seleccionado un bus válido');
                }
                
                const response = await fetch(`https://wani-connect.onrender.com/api/reservas-bus/buses/${selectedBus.id}/lugares`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${authToken}`,
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    }
                });
                
                if (!response.ok) {
                    let errorMessage = `Error ${response.status}: ${response.statusText}`;
                    try {
                        const errorText = await response.text();
                        if (errorText) {
                            errorMessage += ` - ${errorText}`;
                        }
                    } catch (e) {
                        // Ignore parsing errors
                    }
                    throw new Error(errorMessage);
                }
                
                const data = await response.json();
                console.log('Datos recibidos del backend:', data);
                
                const totalLugares = data.totalLugares;
                const lugares = data.lugares;
                const lugaresDisponibles = lugares.filter(lugar => lugar.disponible === true).length;
                
                lugaresGrid.innerHTML = '';
                
                // Crear layout de bus con filas horizontales
                const busLayout = document.createElement('div');
                busLayout.className = 'bus-layout';
                
                let seatNumber = 1;
                const totalRows = Math.ceil(totalLugares / 2);
                
                for (let row = 0; row < totalRows; row++) {
                    const rowContainer = document.createElement('div');
                    rowContainer.className = 'bus-row';
                    
                    // Crear hasta 2 asientos por fila
                    for (let col = 0; col < 2 && seatNumber <= totalLugares; col++) {
                        const currentSeatNumber = seatNumber;
                        const lugarItem = document.createElement('div');
                        
                        // Buscar el lugar en la respuesta del backend
                        const lugarInfo = lugares.find(lugar => lugar.numero === currentSeatNumber);
                        const disponible = lugarInfo ? lugarInfo.disponible : true;
                        
                        console.log(`Asiento ${currentSeatNumber}: lugarInfo =`, lugarInfo, 'disponible =', disponible);
                        
                        lugarItem.className = 'lugar-item';
                        if (disponible) {
                            lugarItem.classList.add('disponible');
                        } else {
                            lugarItem.classList.add('reservado');
                        }
                        
                        lugarItem.textContent = currentSeatNumber;
                        lugarItem.dataset.lugar = JSON.stringify({ numero: currentSeatNumber, disponible });
                        
                        if (disponible) {
                            lugarItem.addEventListener('click', () => {
                                const lugarData = { numero: currentSeatNumber, disponible: true };
                                
                                if (lugarItem.classList.contains('selected')) {
                                    lugarItem.classList.remove('selected');
                                    selectedLugares = selectedLugares.filter(l => l.numero !== lugarData.numero);
                                } else {
                                    lugarItem.classList.add('selected');
                                    selectedLugares.push(lugarData);
                                }
                                
                                document.getElementById('confirmarReservaBtn').disabled = selectedLugares.length === 0;
                                actualizarResumenSeleccion();
                            });
                        }
                        
                        rowContainer.appendChild(lugarItem);
                        seatNumber++;
                    }
                    
                    // Agregar pasillo después de cada fila (excepto la última)
                    if (row < totalRows - 1) {
                        const aisle = document.createElement('div');
                        aisle.className = 'bus-aisle-horizontal';
                        aisle.textContent = 'PASILLO';
                        rowContainer.appendChild(aisle);
                    }
                    
                    busLayout.appendChild(rowContainer);
                }
                
                lugaresGrid.appendChild(busLayout);
                
                // Actualizar contador de lugares disponibles
                document.getElementById('lugaresDisponiblesValor').textContent = lugaresDisponibles;
                
            } catch (error) {
                console.error('Error al cargar lugares:', error);
                console.error('Bus seleccionado:', selectedBus);
                
                // Fallback: usar totalLugares del bus para mostrar asientos
                if (selectedBus && selectedBus.totalLugares) {
                    console.log('Usando fallback con totalLugares del bus:', selectedBus.totalLugares);
                    
                    lugaresGrid.innerHTML = '';
                    const busLayout = document.createElement('div');
                    busLayout.className = 'bus-layout';
                    
                    const totalLugares = selectedBus.totalLugares;
                    let seatNumber = 1;
                    const totalRows = Math.ceil(totalLugares / 2);
                    
                    for (let row = 0; row < totalRows; row++) {
                        const rowContainer = document.createElement('div');
                        rowContainer.className = 'bus-row';
                        
                        for (let col = 0; col < 2 && seatNumber <= totalLugares; col++) {
                            const currentSeatNumber = seatNumber;
                            const lugarItem = document.createElement('div');
                            lugarItem.className = 'lugar-item disponible';
                            lugarItem.textContent = currentSeatNumber;
                            
                            lugarItem.addEventListener('click', () => {
                                const lugarData = { numero: currentSeatNumber, disponible: true };
                                
                                if (lugarItem.classList.contains('selected')) {
                                    lugarItem.classList.remove('selected');
                                    selectedLugares = selectedLugares.filter(l => l.numero !== lugarData.numero);
                                } else {
                                    lugarItem.classList.add('selected');
                                    selectedLugares.push(lugarData);
                                }
                                
                                document.getElementById('confirmarReservaBtn').disabled = selectedLugares.length === 0;
                                actualizarResumenSeleccion();
                            });
                            
                            rowContainer.appendChild(lugarItem);
                            seatNumber++;
                        }
                        
                        if (row < totalRows - 1) {
                            const aisle = document.createElement('div');
                            aisle.className = 'bus-aisle-horizontal';
                            aisle.textContent = 'PASILLO';
                            rowContainer.appendChild(aisle);
                        }
                        
                        busLayout.appendChild(rowContainer);
                    }
                    
                    lugaresGrid.appendChild(busLayout);
                    document.getElementById('lugaresDisponiblesValor').textContent = totalLugares;
                    
                } else {
                    lugaresGrid.innerHTML = `
                        <div class="error">
                            <p>Error al cargar los lugares: ${error.message}</p>
                            <p>Bus ID: ${selectedBus?.id || 'No disponible'}</p>
                            <button class="retry-button" onclick="cargarLugaresBus()">Reintentar</button>
                        </div>
                    `;
                }
            } finally {
                if (isFirstLoad) {
                    showLoadingScreen(false);
                }
            }
        }
        
        // Función para actualizar el resumen de selección
        function actualizarResumenSeleccion() {
            const resumenSeleccion = document.getElementById('resumenSeleccion');
            
            if (selectedBus && selectedLugares.length > 0) {
                // Mostrar el resumen
                resumenSeleccion.style.display = 'block';
                
                // Actualizar los valores
                document.getElementById('resumenBus').textContent = selectedBus.numeroBus;
                document.getElementById('resumenDestino').textContent = selectedBus.destino;
                document.getElementById('resumenHora').textContent = selectedBus.horaSalida;
                document.getElementById('resumenPrecio').textContent = `C$${(selectedBus.precio || 921.00).toFixed(2)} NIO`;
                
                const lugaresTexto = selectedLugares.map(l => l.numero).sort((a, b) => a - b).join(', ');
                document.getElementById('resumenLugar').textContent = `Lugares: ${lugaresTexto}`;
            } else {
                // Ocultar el resumen
                resumenSeleccion.style.display = 'none';
            }
        }
        
        // Mostrar modal de pago
        function confirmarReserva() {
            // Validación para asegurar que se haya seleccionado un bus
            if (!selectedBus) {
                showNotification('Por favor, selecciona un bus antes de confirmar la reserva', 'error');
                return;
            }
            
            if (!selectedTerminal || selectedLugares.length === 0) {
                showNotification('Por favor, selecciona un terminal y al menos un lugar', 'error');
                return;
            }
            
            document.getElementById('pagoModal').style.display = 'block';
            document.getElementById('pagoEmail').value = userInfo.email || '';
            
            // Actualizar resumen de pago
            document.getElementById('pagoMoneda').value = 'USD'; // Resetear a USD por defecto
            actualizarMontoTotal();
            document.getElementById('pagoCantidadAsientos').textContent = selectedLugares.length;
            document.getElementById('pagoAsientos').textContent = selectedLugares.map(l => l.numero).join(', ');
            document.getElementById('pagoBus').textContent = selectedBus.numeroBus;
            document.getElementById('pagoDestino').textContent = selectedBus.destino;
        }
        
        function closePagoModal() {
            document.getElementById('pagoModal').style.display = 'none';
        }
        
        // Función para actualizar el monto total según la moneda
        function actualizarMontoTotal() {
            if (!selectedBus || selectedLugares.length === 0) return;
            
            const moneda = document.getElementById('pagoMoneda').value;
            const precioPorAsientoNIO = selectedBus.precio || 921.00; // Precio en córdobas (25 USD * 36.84)
            let subtotal = selectedLugares.length * precioPorAsientoNIO;
            
            if (moneda === 'NIO') {
                const totalAmount = subtotal + IVA_CORDOBAS;
                document.getElementById('pagoSubtotal').textContent = `C$${subtotal.toFixed(2)} NIO`;
                document.getElementById('pagoIVA').textContent = `C$${IVA_CORDOBAS.toFixed(2)} NIO`;
                document.getElementById('pagoMonto').textContent = `C$${totalAmount.toFixed(2)} NIO`;
            } else {
                const subtotalUSD = subtotal / TASA_CAMBIO;
                const ivaUSD = IVA_CORDOBAS / TASA_CAMBIO;
                const totalAmount = subtotalUSD + ivaUSD;
                document.getElementById('pagoSubtotal').textContent = `$${subtotalUSD.toFixed(2)} USD`;
                document.getElementById('pagoIVA').textContent = `$${ivaUSD.toFixed(2)} USD`;
                document.getElementById('pagoMonto').textContent = `$${totalAmount.toFixed(2)} USD`;
            }
        }
        
        // Procesar pago primero, luego reserva
        async function procesarPago() {
            const email = document.getElementById('pagoEmail').value.trim();
            
            if (!email) {
                showNotification('Por favor, ingresa un email válido', 'error');
                return;
            }
            
            closePagoModal();
            
            const moneda = document.getElementById('pagoMoneda').value;
            const precioPorAsientoNIO = selectedBus.precio || 921.00; // Precio en córdobas (25 USD * 36.84)
            let subtotal = selectedLugares.length * precioPorAsientoNIO;
            let totalAmount;
            
            // Calcular total según moneda seleccionada
            if (moneda === 'NIO') {
                totalAmount = subtotal + IVA_CORDOBAS;
            } else {
                const subtotalUSD = subtotal / TASA_CAMBIO;
                const ivaUSD = IVA_CORDOBAS / TASA_CAMBIO;
                totalAmount = subtotalUSD + ivaUSD;
            }
            
            // Mostrar pantalla de carga
            showLoadingScreen(true, 'Procesando pago...', 'Creando orden de pago en PayPal');
            
            try {
                // Crear pago en PayPal
                const paymentData = {
                    reservationCode: `TEMP_${Date.now()}`, // Código temporal
                    amount: totalAmount,
                    currency: moneda,
                    userEmail: email
                };
                
                const paymentResponse = await fetch('https://wani-connect.onrender.com/api/payments/create', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(paymentData)
                });
                
                if (!paymentResponse.ok) {
                    throw new Error('Error al crear el pago');
                }
                
                const paymentResult = await paymentResponse.json();
                
                showLoadingScreen(false);
                
                // Mostrar modal de confirmación de pago
                const confirmPago = confirm(`Pago creado exitosamente.\n\nPayment ID: ${paymentResult.paymentId}\n\n¿Deseas abrir PayPal para completar el pago?\n\nDespués del pago, la reserva se creará automáticamente.`);
                
                if (confirmPago) {
                    // Abrir PayPal en nueva ventana
                    window.open(paymentResult.paypalUrl, '_blank');
                    
                    // Esperar confirmación del usuario
                    setTimeout(() => {
                        const pagoCompletado = confirm('¿Has completado el pago en PayPal?\n\nHaz clic en "Aceptar" si ya pagaste para crear tu reserva.');
                        
                        if (pagoCompletado) {
                            procesarReservaConPago(email, paymentResult.paymentId);
                        }
                    }, 3000);
                }
                
            } catch (error) {
                console.error('Error al procesar pago:', error);
                showNotification('Error al procesar el pago: ' + error.message, 'error');
                showLoadingScreen(false);
            }
        }
        
        // Procesar reserva después del pago
        async function procesarReservaConPago(email, paymentId) {
            if (window.reservaInProgress) {
                showNotification('Ya hay una reserva en proceso. Por favor espera.', 'info');
                return;
            }
            window.reservaInProgress = true;
            
            const lugaresInvalidos = selectedLugares.filter(lugar => lugar.numero > selectedBus.totalLugares);
            if (lugaresInvalidos.length > 0) {
                showNotification(`Lugares inválidos seleccionados: ${lugaresInvalidos.map(l => l.numero).join(', ')}`, 'error');
                selectedLugares = selectedLugares.filter(lugar => lugar.numero <= selectedBus.totalLugares);
                actualizarResumenSeleccion();
                return;
            }
            
            // Mostrar pantalla de carga
            showLoadingScreen(true, 'Procesando reserva...', 'Confirmando asientos y enviando email');
            
            // Generar un identificador único del cliente para evitar duplicados
            const clientId = Date.now() + '-' + Math.random().toString(36).substr(2, 9);
            
            // Crear una sola reserva con múltiples lugares
            const reservaRequest = {
                terminalId: selectedTerminal.id,
                busId: selectedBus.id,
                numerosLugar: selectedLugares.map(l => l.numero),
                nombreUsuario: userInfo.username,
                emailUsuario: email,
                clientRequestId: clientId // Identificador único del cliente
            };
            
            // Validar que todos los campos requeridos estén presentes
            if (!reservaRequest.terminalId || !reservaRequest.busId || 
                !reservaRequest.numerosLugar || reservaRequest.numerosLugar.length === 0 || 
                !reservaRequest.nombreUsuario || !reservaRequest.emailUsuario) {
                console.error('Datos faltantes en reserva:', reservaRequest);
                showNotification('Datos faltantes en la reserva', 'error');
                return;
            }
            
            console.log('Token:', authToken);
            console.log('Bus seleccionado:', selectedBus);
            console.log('Terminal seleccionada:', selectedTerminal);
            
            // Verificar que el token existe
            if (!authToken) {
                showNotification('No hay token de autenticación. Inicia sesión nuevamente.', 'error');
                logout();
                return;
            }
            
            try {
                console.log('Enviando reserva:', reservaRequest);
                console.log('Validando campos:', {
                    terminalId: typeof reservaRequest.terminalId,
                    busId: typeof reservaRequest.busId,
                    numerosLugar: typeof reservaRequest.numerosLugar,
                    nombreUsuario: typeof reservaRequest.nombreUsuario,
                    emailUsuario: typeof reservaRequest.emailUsuario
                });
                
                const response = await fetch('https://wani-connect.onrender.com/api/reservas-bus', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        'Authorization': `Bearer ${authToken}`
                    },
                    body: JSON.stringify(reservaRequest)
                });
                
                if (!response.ok) {
                    let errorMessage = `Error ${response.status}: ${response.statusText}`;
                    
                    if (response.status === 401 || response.status === 403) {
                        showNotification('Sesión expirada. Inicia sesión nuevamente.', 'error');
                        logout();
                        return;
                    }
                    
                    try {
                        const errorText = await response.text();
                        console.log('Error del servidor (texto):', errorText);
                        
                        // Intentar parsear como JSON primero
                        try {
                            const errorData = JSON.parse(errorText);
                            errorMessage = errorData.message || errorText;
                        } catch (jsonError) {
                            // Si no es JSON, usar el texto directamente
                            errorMessage = errorText || errorMessage;
                        }
                    } catch (textError) {
                        console.error('No se pudo obtener el error del servidor:', textError);
                        errorMessage = `Error ${response.status}: No se pudo leer la respuesta del servidor`;
                    }
                    
                    throw new Error(errorMessage);
                }
                
                const data = await response.json();
                const codigosReserva = data.map(reserva => `Lugar ${reserva.numeroLugar}: ${reserva.codigoUnico}`);
                
                // Actualizar los lugares inmediatamente
                cargarLugaresBus();
                
                // Mostrar modal de confirmación con todos los códigos
                document.getElementById('codigoReserva').innerHTML = `
                    <div style="background: #d4edda; padding: 10px; border-radius: 5px; margin: 10px 0;">
                        <strong>✅ Pago completado</strong><br>
                        Payment ID: ${paymentId}
                    </div>
                    ${codigosReserva.join('<br>')}
                `;
                document.getElementById('confirmacionModal').style.display = 'block';
                
                // Mostrar notificación
                showNotification(`${selectedLugares.length} reserva(s) realizada(s) con éxito después del pago`, 'success');
                
                // Limpiar selección
                selectedLugares = [];
                
            } catch (error) {
                console.error('Error al realizar reserva:', error);
                showNotification('Error al realizar la reserva: ' + error.message, 'error');
            } finally {
                showLoadingScreen(false);
                window.reservaInProgress = false;
            }
        }
        
        // Funciones para cerrar modales
        function closeTerminalModal() {
            document.getElementById('terminalModal').style.display = 'none';
            document.getElementById('lugaresContainer').style.display = 'none';
            document.getElementById('busDetails').style.display = 'none';
            selectedTerminal = null;
            selectedBus = null;  // Asegurarse de que selectedBus se establezca en null
            selectedLugares = [];
            document.getElementById('confirmarReservaBtn').disabled = true; // Deshabilitar el botón
            
            // Limpiar intervalo de actualización de lugares
            if (lugaresInterval) {
                clearInterval(lugaresInterval);
                lugaresInterval = null;
            }
        }
        
        function closeLugares() {
            document.getElementById('lugaresContainer').style.display = 'none';
            selectedLugares = [];
            document.getElementById('confirmarReservaBtn').disabled = true;
            document.getElementById('resumenSeleccion').style.display = 'none';
        }
        
        function closeConfirmacionModal() {
            document.getElementById('confirmacionModal').style.display = 'none';
        }
        
        // Funciones para validar código
        function mostrarValidarCodigo() {
            document.getElementById('validarCodigoModal').style.display = 'block';
            document.getElementById('codigoValidar').value = '';
            document.getElementById('resultadoValidacion').style.display = 'none';
            selectValidationMethod('codigo');
        }
        
        function selectValidationMethod(method) {
            // Actualizar botones
            document.getElementById('btnCodigo').className = method === 'codigo' ? 'btn btn-primary method-btn active' : 'btn btn-secondary method-btn';
            document.getElementById('btnQR').className = method === 'qr' ? 'btn btn-primary method-btn active' : 'btn btn-secondary method-btn';
            
            // Mostrar/ocultar secciones
            document.getElementById('validacionCodigo').style.display = method === 'codigo' ? 'block' : 'none';
            document.getElementById('validacionQR').style.display = method === 'qr' ? 'block' : 'none';
            
            // Limpiar resultados
            document.getElementById('resultadoValidacion').style.display = 'none';
        }
        
        function previewQR(input) {
            const file = input.files[0];
            const preview = document.getElementById('qrPreview');
            const image = document.getElementById('qrImage');
            const btn = document.getElementById('validarQRBtn');
            
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    image.src = e.target.result;
                    preview.style.display = 'block';
                    btn.disabled = false;
                };
                reader.readAsDataURL(file);
            } else {
                preview.style.display = 'none';
                btn.disabled = true;
            }
        }
        
        async function validarQR() {
            const fileInput = document.getElementById('qrFile');
            const resultado = document.getElementById('resultadoValidacion');
            
            if (!fileInput.files[0]) {
                showNotification('Selecciona una imagen QR', 'error');
                return;
            }
            
            // Mostrar pantalla de carga
            showLoadingScreen(true, 'Procesando QR...', 'Decodificando imagen y validando');
            
            // Leer el archivo QR y extraer el código
            const file = fileInput.files[0];
            const canvas = document.createElement('canvas');
            const ctx = canvas.getContext('2d');
            const img = new Image();
            
            img.onload = async function() {
                canvas.width = img.width;
                canvas.height = img.height;
                ctx.drawImage(img, 0, 0);
                
                try {
                    const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
                    
                    showLoadingScreen(false);
                    const codigoExtraido = prompt('Ingresa el código que aparece en el QR:');
                    
                    if (!codigoExtraido) {
                        showNotification('Código QR requerido', 'error');
                        return;
                    }
                    
                    showLoadingScreen(true, 'Validando código...', 'Verificando en el servidor');
                    
                    // Validar el código extraído
                    const response = await fetch(`https://wani-connect.onrender.com/api/reservas-bus/validar/${codigoExtraido}`, {
                        method: 'POST',
                        headers: {
                            'Authorization': `Bearer ${authToken}`,
                            'Content-Type': 'application/json'
                        }
                    });
                    
                    if (response.ok) {
                        const mensaje = await response.text();
                        resultado.className = 'endpoint-success';
                        resultado.innerHTML = `<i class="fas fa-check-circle"></i> ${mensaje}`;
                        showNotification('QR validado correctamente', 'success');
                    } else {
                        const error = await response.text();
                        resultado.className = 'endpoint-error';
                        resultado.innerHTML = `<i class="fas fa-times-circle"></i> ${error}`;
                        showNotification('QR inválido', 'error');
                    }
                    
                    resultado.style.display = 'block';
                    
                } catch (error) {
                    console.error('Error al validar QR:', error);
                    resultado.className = 'endpoint-error';
                    resultado.innerHTML = `<i class="fas fa-times-circle"></i> Error de conexión: ${error.message}`;
                    resultado.style.display = 'block';
                    showNotification('Error al validar QR', 'error');
                } finally {
                    showLoadingScreen(false);
                }
            };
            
            img.src = URL.createObjectURL(file);
        }
        
        function closeValidarCodigoModal() {
            document.getElementById('validarCodigoModal').style.display = 'none';
        }
        
        // Funciones para conductor
        function mostrarConductorModal() {
            console.log('Rol actual del usuario:', userRole);
            if (userRole !== 'COMPANY') {
                showNotification('Solo las cuentas empresariales pueden liberar buses', 'error');
                return;
            }
            document.getElementById('conductorModal').style.display = 'block';
            cargarBusesParaConductor();
        }
        
        function closeConductorModal() {
            document.getElementById('conductorModal').style.display = 'none';
        }
        
        async function cargarBusesParaConductor() {
            const busesList = document.getElementById('conductorBusesList');
            busesList.innerHTML = '<div class="loading">Cargando buses...</div>';
            
            try {
                if (!selectedTerminal) {
                    throw new Error('No hay terminal seleccionada');
                }
                
                const response = await fetch(`https://wani-connect.onrender.com/api/buses/terminal/codigo/${selectedTerminal.codigoUnico}`, {
                    headers: {
                        'Authorization': `Bearer ${authToken}`
                    }
                });
                
                if (!response.ok) {
                    throw new Error(`Error ${response.status}: ${response.statusText}`);
                }
                
                const buses = await response.json();
                busesList.innerHTML = '';
                
                if (buses.length === 0) {
                    busesList.innerHTML = '<p>No hay buses en esta terminal</p>';
                    return;
                }
                
                buses.forEach(bus => {
                    const busCard = document.createElement('div');
                    busCard.className = 'card mb-2';
                    busCard.innerHTML = `
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <strong>Bus ${bus.numeroBus}</strong> - ${bus.destino}<br>
                                    <small>Salida: ${bus.horaSalida} | Lugares: ${bus.lugaresDisponibles}/${bus.totalLugares}</small>
                                </div>
                                <button class="btn btn-danger btn-sm" onclick="liberarBus(${bus.id}, '${bus.numeroBus}')">
                                    Liberar Bus
                                </button>
                            </div>
                        </div>
                    `;
                    busesList.appendChild(busCard);
                });
                
            } catch (error) {
                console.error('Error al cargar buses:', error);
                busesList.innerHTML = `<div class="error">Error: ${error.message}</div>`;
            }
        }
        
        async function liberarBus(busId, numeroBus) {
            if (!confirm(`¿Estás seguro de que el bus ${numeroBus} ha salido de la terminal? Esto liberará TODOS los asientos reservados.`)) {
                return;
            }
            
            console.log('=== DEBUG LIBERAR BUS ===');
            console.log('Token:', authToken);
            console.log('Rol del usuario:', userRole);
            console.log('Bus ID:', busId);
            
            showLoadingScreen(true, 'Liberando bus...', 'Eliminando todas las reservas');
            
            try {
                const response = await fetch(`https://wani-connect.onrender.com/api/reservas-bus/conductor/liberar-bus/${busId}`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${authToken}`,
                        'Content-Type': 'application/json'
                    }
                });
                
                console.log('Respuesta del servidor:', response.status, response.statusText);
                
                if (response.status === 403) {
                    const errorText = await response.text();
                    console.log('Error 403:', errorText);
                    showNotification('Acceso denegado: ' + errorText, 'error');
                    return;
                }
                
                if (!response.ok) {
                    const error = await response.text();
                    console.log('Error del servidor:', error);
                    throw new Error(error);
                }
                
                const mensaje = await response.text();
                console.log('Respuesta exitosa:', mensaje);
                showNotification(`Bus ${numeroBus} liberado correctamente`, 'success');
                
                // Recargar la lista de buses
                cargarBusesParaConductor();
                
                // Si hay lugares cargados, actualizarlos
                if (selectedBus && selectedBus.id === busId) {
                    cargarLugaresBus();
                }
                
            } catch (error) {
                console.error('Error al liberar bus:', error);
                if (error.message.includes('403') || error.message.includes('Acceso denegado')) {
                    showNotification('Solo las cuentas empresariales pueden liberar buses', 'error');
                } else {
                    showNotification('Error al liberar bus: ' + error.message, 'error');
                }
            } finally {
                showLoadingScreen(false);
            }
        }
        
        async function validarCodigo() {
            const codigo = document.getElementById('codigoValidar').value.trim();
            const resultado = document.getElementById('resultadoValidacion');
            
            if (!codigo) {
                showNotification('Ingresa un código de reserva', 'error');
                return;
            }
            
            // Mostrar pantalla de carga
            showLoadingScreen(true, 'Validando código...', 'Verificando reserva en el sistema');
            
            try {
                const response = await fetch(`https://wani-connect.onrender.com/api/reservas-bus/validar/${codigo}`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${authToken}`,
                        'Content-Type': 'application/json'
                    }
                });
                
                if (response.ok) {
                    const mensaje = await response.text();
                    resultado.className = 'endpoint-success';
                    resultado.innerHTML = `<i class="fas fa-check-circle"></i> ${mensaje}`;
                    showNotification('Código validado correctamente', 'success');
                } else {
                    const error = await response.text();
                    resultado.className = 'endpoint-error';
                    resultado.innerHTML = `<i class="fas fa-times-circle"></i> ${error}`;
                    showNotification('Código inválido', 'error');
                }
                
                resultado.style.display = 'block';
                
            } catch (error) {
                console.error('Error al validar código:', error);
                resultado.className = 'endpoint-error';
                resultado.innerHTML = `<i class="fas fa-times-circle"></i> Error de conexión: ${error.message}`;
                resultado.style.display = 'block';
                showNotification('Error al validar código', 'error');
            } finally {
                showLoadingScreen(false);
            }
        }
        
        // Función para obtener ubicación del usuario
        function obtenerUbicacion() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    function(position) {
                        userLocation = {
                            lat: position.coords.latitude,
                            lng: position.coords.longitude
                        };
                        
                        // Agregar marcador de ubicación del usuario
                        const userIcon = L.divIcon({
                            html: '<i class="fas fa-user-circle" style="color: #e74c3c; font-size: 24px;"></i>',
                            iconSize: [24, 24],
                            className: 'custom-div-icon'
                        });
                        
                        L.marker([userLocation.lat, userLocation.lng], { icon: userIcon })
                            .bindPopup('Tu ubicación')
                            .addTo(markers);
                        
                        showNotification('Ubicación obtenida correctamente', 'success');
                    },
                    function(error) {
                        console.error('Error al obtener ubicación:', error);
                        showNotification('No se pudo obtener tu ubicación', 'error');
                    }
                );
            } else {
                showNotification('Geolocalización no soportada', 'error');
            }
        }
        
        // Función para ir a la terminal
        function irATerminal() {
            if (!selectedTerminal) {
                showNotification('No hay terminal seleccionada', 'error');
                return;
            }
            
            if (!userLocation) {
                const obtenerUbi = confirm('Necesitamos tu ubicación para crear la ruta. ¿Permitir acceso a la ubicación?');
                if (obtenerUbi) {
                    obtenerUbicacion();
                    setTimeout(() => {
                        if (userLocation) {
                            crearRuta();
                        }
                    }, 2000);
                }
                return;
            }
            
            crearRuta();
        }
        
        // Función para crear la ruta
        function crearRuta() {
            if (!userLocation || !selectedTerminal) return;
            
            // Limpiar ruta anterior si existe
            if (routeControl) {
                map.removeControl(routeControl);
            }
            
            const terminalLat = selectedTerminal.ubicacionGeografica.latitud;
            const terminalLng = selectedTerminal.ubicacionGeografica.longitud;
            
            // Crear control de ruta
            routeControl = L.Routing.control({
                waypoints: [
                    L.latLng(userLocation.lat, userLocation.lng),
                    L.latLng(terminalLat, terminalLng)
                ],
                routeWhileDragging: false,
                addWaypoints: false,
                createMarker: function() { return null; },
                show: false,
                lineOptions: {
                    styles: [{ color: '#3498db', weight: 6, opacity: 0.8 }]
                }
            }).addTo(map);
            
            // Ocultar el panel de instrucciones
            setTimeout(() => {
                const routingContainer = document.querySelector('.leaflet-routing-container');
                if (routingContainer) {
                    routingContainer.style.display = 'none';
                }
            }, 100);
            
            // Ajustar vista del mapa para mostrar la ruta
            const group = new L.featureGroup([
                L.marker([userLocation.lat, userLocation.lng]),
                L.marker([terminalLat, terminalLng])
            ]);
            map.fitBounds(group.getBounds().pad(0.1));
            
            showNotification(`Ruta creada hacia ${selectedTerminal.nombre}`, 'success');
            closeTerminalModal();
        }
        
        // Cerrar modales al hacer clic fuera
        window.onclick = function(event) {
            if (event.target == document.getElementById('terminalModal')) {
                closeTerminalModal();
            }
            if (event.target == document.getElementById('confirmacionModal')) {
                closeConfirmacionModal();
            }
            if (event.target == document.getElementById('validarCodigoModal')) {
                closeValidarCodigoModal();
            }
            if (event.target == document.getElementById('pagoModal')) {
                closePagoModal();
            }
            if (event.target == document.getElementById('conductorModal')) {
                closeConductorModal();
            }
        }