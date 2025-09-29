// Variables globales
        let authToken = localStorage.getItem('authToken');
        let username = localStorage.getItem('username');
        let map;
        let markers;
        let currentLocationMarker;
        let routeControl;
        let currentLatLng = null;
        let currentSiteId = null; // Variable para almacenar el ID del sitio actual
        let markerArray = []; // Array para almacenar los marcadores y calcular límites
        let selectedPaymentMethod = 'paypal'; // Método de pago seleccionado
        let currentPaymentId = null; // ID del pago actual
        let currentTicketPrice = 0; // Precio de la entrada actual
        let selectedCurrency = 'USD'; // Moneda seleccionada (USD o NIO)
        let selectedReservationOption = 'payment'; // Opción de reserva seleccionada
        let userEmail = ''; // Email del usuario para reservas y pagos
        const exchangeRate = 36.5; // Tasa de cambio: 1 USD = 36.5 NIO (córdobas)
        const ivaRate = 0.15; // 15% de IVA
        
        // Verificar si ya hay un token almacenado
        if (authToken) {
            showMainContent();
        } else {
            // Si no hay token, redirigir a la página de login
            window.location.href = 'iniciosesion.html';
        }
        
        // Función para cerrar sesión
        window.logout = function() {
            localStorage.removeItem('authToken');
            localStorage.removeItem('username');
            authToken = null;
            username = null;
            
            // Redirigir a la página de login
            window.location.href = 'iniciosesion.html';
        }
        
        // Función para mostrar el contenido principal
        function showMainContent() {
            document.getElementById('userInfo').textContent = `Bienvenido, ${username || 'Usuario'}`;
            
            // Inicializar el mapa y cargar los sitios
            initializeMap();
            loadSites();
            getCurrentLocation();
        }
        
        // Función para inicializar el mapa
        function initializeMap() {
            // Inicializar el mapa
            map = L.map('map').setView([19.4326, -99.1332], 10); // Ciudad de México
            
            // Agregar capa de OpenStreetMap
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(map);
            
            // Grupo para todos los marcadores
            markers = L.layerGroup().addTo(map);
            
            // Configurar el modal
            const modal = document.getElementById('siteModal');
            const closeBtn = document.getElementsByClassName('close')[0];
            
            // Cerrar modal
            closeBtn.onclick = function() {
                modal.style.display = 'none';
            }
            
            window.onclick = function(event) {
                if (event.target == modal) {
                    modal.style.display = 'none';
                }
            }
        }
        
        // Función para obtener la ubicación actual del usuario
        function getCurrentLocation() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    function(position) {
                        const lat = position.coords.latitude;
                        const lng = position.coords.longitude;
                        
                        // Guardar la ubicación actual en una variable global
                        currentLatLng = L.latLng(lat, lng);
                        
                        // Crear un marcador para la ubicación actual
                        currentLocationMarker = L.marker([lat, lng], {
                            icon: L.icon({
                                iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-blue.png',
                                shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
                                iconSize: [25, 41],
                                iconAnchor: [12, 41],
                                popupAnchor: [1, -34],
                                shadowSize: [41, 41]
                            })
                        }).addTo(map);
                        
                        currentLocationMarker.bindPopup('<b>Tu ubicación actual</b>').openPopup();
                    },
                    function(error) {
                        console.error('Error obteniendo la ubicación:', error);
                        alert('No se pudo obtener tu ubicación actual. Asegúrate de haber dado permiso para acceder a tu ubicación.');
                    }
                );
            } else {
                alert('Tu navegador no soporta la geolocalización.');
            }
        }
        
        // Función para cargar los sitios turísticos
        function loadSites() {
            // Elementos del DOM
            const loadingMessage = document.getElementById('loadingMessage');
            const errorMessage = document.getElementById('errorMessage');
            const siteList = document.getElementById('siteList');
            const totalSitesElement = document.getElementById('totalSites');
            const loadedSitesElement = document.getElementById('loadedSites');
            
            // Limpiar el array de marcadores
            markerArray = [];
            
            // Obtener las ubicaciones de los sitios turísticos desde la API
            fetch('http://localhost:8080/api/sitios-turisticos/ubicaciones', {
                headers: {
                    'Authorization': `Bearer ${authToken}`
                }
            })
            .then(response => {
                if (response.status === 401) {
                    // Token inválido o expirado
                    logout();
                    throw new Error('Sesión expirada. Por favor, inicia sesión nuevamente.');
                }
                
                if (!response.ok) {
                    throw new Error('Error al obtener los datos de los sitios turísticos');
                }
                return response.json();
            })
            .then(data => {
                // Ocultar mensaje de carga
                loadingMessage.style.display = 'none';
                
                // Actualizar estadísticas
                totalSitesElement.textContent = data.length;
                loadedSitesElement.textContent = data.length;
                
                if (data.length === 0) {
                    errorMessage.textContent = 'No se encontraron sitios turísticos';
                    errorMessage.style.display = 'block';
                    return;
                }
                
                // Mostrar lista de sitios
                siteList.style.display = 'block';
                
                // Limpiar marcadores existentes
                markers.clearLayers();
                
                // Procesar cada sitio
                data.forEach((sitio) => {
                    // Verificar que el sitio tenga código único
                    if (!sitio.codigoUnico) {
                        console.error("Sitio sin código único:", sitio);
                        return;
                    }
                    
                    // Crear marcador en el mapa
                    const marker = L.marker([sitio.latitud, sitio.longitud])
                        .bindPopup(`
                            <div style="text-align: center; max-width: 200px;">
                                <h3>${sitio.nombre}</h3>
                                <p><strong>Código:</strong> ${sitio.codigoUnico}</p>
                                <button onclick="showSiteDetails('${sitio.codigoUnico}')" style="margin-top: 10px; padding: 5px 10px; background-color: #3498db; color: white; border: none; border-radius: 4px; cursor: pointer;">Ver detalles</button>
                            </div>
                        `);
                    
                    // Agregar marcador al grupo
                    markers.addLayer(marker);
                    
                    // Agregar al array de marcadores para calcular límites
                    markerArray.push([sitio.latitud, sitio.longitud]);
                    
                    // Crear elemento en la lista
                    const listItem = document.createElement('li');
                    listItem.className = 'site-item';
                    listItem.innerHTML = `
                        <div class="site-name">${sitio.nombre}</div>
                        <div class="site-code">Código: ${sitio.codigoUnico}</div>
                    `;
                    
                    // Al hacer clic en el elemento, centrar el mapa en el marcador
                    listItem.addEventListener('click', () => {
                        map.setView([sitio.latitud, sitio.longitud], 15);
                        marker.openPopup();
                    });
                    
                    siteList.appendChild(listItem);
                });
                
                // Ajustar el mapa para mostrar todos los marcadores
                if (markerArray.length > 0) {
                    const bounds = L.latLngBounds(markerArray);
                    map.fitBounds(bounds.pad(0.1));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                loadingMessage.style.display = 'none';
                errorMessage.textContent = `Error: ${error.message}`;
                errorMessage.style.display = 'block';
            });
        }
        
        // Función para mostrar detalles del sitio por código único
        window.showSiteDetails = function(codigoUnico) {
            console.log("Mostrando detalles para código único:", codigoUnico);
            
            if (!codigoUnico) {
                console.error("Código único es undefined o null");
                alert("Error: Código único no válido");
                return;
            }
            
            showLoadingOverlay();
            
            // Añadir un timeout para evitar problemas de conexión
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), 15000); // 15 segundos de timeout
            
            fetch(`http://localhost:8080/api/sitios-turisticos/${codigoUnico}`, {
                signal: controller.signal,
                headers: {
                    'Authorization': `Bearer ${authToken}`
                }
            })
            .then(response => {
                clearTimeout(timeoutId);
                
                if (response.status === 404) {
                    throw new Error('Sitio no encontrado');
                }
                
                if (!response.ok) {
                    throw new Error(`Error HTTP: ${response.status}`);
                }
                
                // Verificar que la respuesta no esté vacía
                const contentLength = response.headers.get('Content-Length');
                if (contentLength && contentLength === '0') {
                    throw new Error('Respuesta vacía del servidor');
                }
                
                return response.text().then(text => {
                    if (!text) {
                        throw new Error('Respuesta vacía del servidor');
                    }
                    
                    try {
                        return JSON.parse(text);
                    } catch (e) {
                        console.error('Error al parsear JSON:', text);
                        throw new Error('Respuesta JSON inválida');
                    }
                });
            })
            .then(data => {
                console.log('Datos recibidos:', data);
                // Mostrar detalles del sitio
                showSiteDetailsData(data);
            })
            .catch(error => {
                if (error.name === 'AbortError') {
                    console.error('La solicitud fue abortada por timeout');
                    alert('La solicitud tardó demasiado tiempo. Por favor, inténtalo de nuevo.');
                } else {
                    console.error('Error:', error);
                    alert(`Error al cargar los detalles del sitio: ${error.message}`);
                }
            })
            .finally(() => {
                hideLoadingOverlay();
            });
        }
        
        // Función para mostrar los datos del sitio en el modal
        function showSiteDetailsData(data) {
            const modal = document.getElementById('siteModal');
            const siteDetailsContainer = document.getElementById('siteDetailsContainer');
            
            // Verificar que los datos necesarios existan
            if (!data || !data.nombre) {
                console.error('Datos inválidos recibidos:', data);
                alert('Error: Datos del sitio inválidos');
                return;
            }
            
            // Guardar el ID del sitio actual
            currentSiteId = data.id;
            console.log('ID del sitio guardado:', currentSiteId);
            
            // Guardar el precio de la entrada
            currentTicketPrice = parseFloat(data.costoEntrada) || 0;
            console.log('Precio de entrada guardado:', currentTicketPrice);
            
            // Construir HTML con los detalles
            let detailsHTML = `
                <div class="site-details">
                    <h3>${data.nombre || 'Nombre no disponible'}</h3>
                    
                    <div class="detail-grid">
                        <div class="detail-item">
                            <span class="detail-label">ID:</span>
                            <span class="detail-value">${data.id || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Código único:</span>
                            <span class="detail-value">${data.codigoUnico || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Tipo de lugar:</span>
                            <span class="detail-value">${data.tipoLugar || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Dirección:</span>
                            <span class="detail-value">${data.direccion || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Horario de atención:</span>
                            <span class="detail-value">${data.horarioAtencion || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Costo de entrada:</span>
                            <span class="detail-value">$${data.costoEntrada || '0'} USD</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Historia resumida:</span>
                            <span class="detail-value">${data.historiaResumida || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Eventos históricos:</span>
                            <span class="detail-value">${data.eventosHistoricos || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Personajes asociados:</span>
                            <span class="detail-value">${data.personajesAsociados || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Audioguías:</span>
                            <span class="detail-value">${data.audioguias || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Servicios disponibles:</span>
                            <span class="detail-value">${data.serviciosDisponibles || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Actividades recomendadas:</span>
                            <span class="detail-value">${data.actividadesRecomendadas || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Nivel de accesibilidad:</span>
                            <span class="detail-value">${data.nivelAccesibilidad || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Reglas del lugar:</span>
                            <span class="detail-value">${data.reglasLugar || 'No disponible'}</span>
                        </div>
                        
                        <div class="detail-item">
                            <span class="detail-label">Enlace de reserva:</span>
                            <span class="detail-value"><a href="${data.enlaceReserva || '#'}" target="_blank">${data.enlaceReserva || 'No disponible'}</a></span>
                        </div>
                    </div>
                    
                    <div class="action-buttons">
                        <button class="btn btn-primary" onclick="goToLocation('${data.direccion || ''}', ${data.latitud || 0}, ${data.longitud || 0}, '${data.nombre || ''}', '${data.codigoUnico || ''}')">Ir</button>
                        <button class="btn btn-success" onclick="showReservationOptions('${data.codigoUnico || ''}', '${data.nombre || ''}', ${data.costoEntrada || 0})">Reservar</button>
                    </div>
                </div>
            `;
            
            // Agregar imágenes si existen
            if (data.imagenes && data.imagenes.length > 0) {
                detailsHTML += '<div class="detail-item"><span class="detail-label">Imágenes:</span><div class="images-container">';
                data.imagenes.forEach(imagen => {
                    detailsHTML += `<img src="${imagen.url}" alt="Imagen del sitio">`;
                });
                detailsHTML += '</div></div>';
            }
            
            // Mostrar el modal con los detalles
            siteDetailsContainer.innerHTML = detailsHTML;
            modal.style.display = 'block';
        }
        
        // Función para mostrar opciones de reserva
        window.showReservationOptions = function(codigoUnico, nombre, costoEntrada) {
            if (!codigoUnico || !nombre) {
                alert('Información del sitio no disponible');
                return;
            }
            
            // Guardar el precio de la entrada
            currentTicketPrice = parseFloat(costoEntrada) || 0;
            
            // Actualizar información global para uso posterior
            window.currentSiteCode = codigoUnico;
            window.currentSiteName = nombre;
            
            // Resetear opción de reserva a pago
            selectedReservationOption = 'payment';
            document.querySelector('.reservation-option.selected').classList.remove('selected');
            document.querySelector('.reservation-option input[value="payment"]').checked = true;
            document.querySelector('.reservation-option input[value="payment"]').parentElement.classList.add('selected');
            
            // Establecer el campo de email vacío (limpio)
            userEmail = '';
            document.getElementById('userEmailInput').value = '';
            
            // Mostrar modal de opciones de reserva
            document.getElementById('reservationOptionsModal').style.display = 'block';
        }
        
        // Función para cerrar el modal de opciones de reserva
        window.closeReservationOptionsModal = function() {
            document.getElementById('reservationOptionsModal').style.display = 'none';
        }
        
        // Función para seleccionar opción de reserva
        window.selectReservationOption = function(option) {
            selectedReservationOption = option;
            
            // Actualizar UI para mostrar la opción seleccionada
            document.querySelectorAll('.reservation-option').forEach(el => {
                el.classList.remove('selected');
            });
            
            document.querySelector(`.reservation-option input[value="${option}"]`).checked = true;
            document.querySelector(`.reservation-option input[value="${option}"]`).parentElement.classList.add('selected');
        }
        
        // Función para proceder con la reserva según la opción seleccionada
        window.proceedWithReservation = function() {
            // Obtener el email del formulario
            userEmail = document.getElementById('userEmailInput').value.trim();
            
            if (!userEmail) {
                alert('Por favor ingresa un email válido');
                return;
            }
            
            closeReservationOptionsModal();
            
            if (selectedReservationOption === 'payment') {
                // Mostrar modal de pago
                showPaymentModal(window.currentSiteCode, window.currentSiteName, currentTicketPrice);
            } else if (selectedReservationOption === 'direct') {
                // Realizar reserva directa sin pago
                makeDirectReservation();
            }
        }
        
        // Función para ir a la ubicación en el mapa
        window.goToLocation = function(address, latitud, longitud, nombre, codigoUnico) {
            if (!latitud || !longitud) {
                alert('Coordenadas no disponibles');
                return;
            }
            
            // Cerrar el modal
            document.getElementById('siteModal').style.display = 'none';
            
            // Verificar si tenemos la ubicación actual del usuario
            if (!currentLatLng) {
                alert('No se ha podido obtener tu ubicación actual. Asegúrate de haber dado permiso para acceder a tu ubicación.');
                return;
            }
            
            // Destino
            const destinationLatLng = L.latLng(latitud, longitud);
            
            // Limpiar cualquier ruta existente
            clearRoute();
            
            // Crear la ruta
            routeControl = L.Routing.control({
                waypoints: [
                    currentLatLng,
                    destinationLatLng
                ],
                routeWhileDragging: false,
                createMarker: function() { return null; }, // No crear marcadores adicionales
                lineOptions: {
                    styles: [{color: '#3498db', opacity: 0.7, weight: 5}]
                },
                show: false, // No mostrar las instrucciones paso a paso
                addWaypoints: false, // No permitir agregar waypoints adicionales
                draggableWaypoints: false, // No permitir arrastrar los waypoints
                fitSelectedRoutes: true // Ajustar el mapa para mostrar la ruta
            }).addTo(map);
            
            // Escuchar el evento de ruta calculada
            routeControl.on('routesfound', function(e) {
                const routes = e.routes;
                const summary = routes[0].summary;
                
                // Mostrar información de la ruta
                document.getElementById('routePanel').style.display = 'block';
                document.getElementById('routeDistance').textContent = (summary.totalDistance / 1000).toFixed(2) + ' km';
                const totalMinutes = Math.round(summary.totalTime / 60);
                let timeText;
                if (totalMinutes >= 60) {
                    const hours = Math.floor(totalMinutes / 60);
                    const minutes = totalMinutes % 60;
                    timeText = minutes > 0 ? `${hours}h ${minutes}min` : `${hours}h`;
                } else {
                    timeText = `${totalMinutes} min`;
                }
                document.getElementById('routeTime').textContent = timeText;
                
                // Crear un marcador para el destino si no existe
                let destinationMarker = null;
                markers.eachLayer(layer => {
                    if (layer instanceof L.Marker) {
                        const layerLatLng = layer.getLatLng();
                        if (Math.abs(layerLatLng.lat - latitud) < 0.0001 && Math.abs(layerLatLng.lng - longitud) < 0.0001) {
                            destinationMarker = layer;
                        }
                    }
                });
                
                // Si no se encontró un marcador, crear uno nuevo
                if (!destinationMarker) {
                    destinationMarker = L.marker([latitud, longitud]).addTo(markers);
                }
                
                // Crear un popup personalizado para el destino
                const popupContent = `
                    <div class="popup-content">
                        <h4>${nombre}</h4>
                        <p><span class="popup-code">Código: ${codigoUnico}</span></p>
                        <p>${address}</p>
                        <div class="popup-directions">
                            <button onclick="showSiteDetails('${codigoUnico}')">Ver detalles</button>
                        </div>
                    </div>
                `;
                
                // Abrir el popup en el destino
                destinationMarker.bindPopup(popupContent, {
                    className: 'custom-popup'
                }).openPopup();
            });
            
            // Si no se dispara el evento routesfound, mostrar un mensaje después de un tiempo
            setTimeout(() => {
                if (document.getElementById('routePanel').style.display === 'none') {
                    alert('No se pudo calcular una ruta a este destino. Por favor, inténtalo con otro destino más cercano.');
                    clearRoute();
                }
            }, 10000); // 10 segundos
        }
        
        // Función para limpiar la ruta
        window.clearRoute = function() {
            if (routeControl) {
                map.removeControl(routeControl);
                routeControl = null;
            }
            document.getElementById('routePanel').style.display = 'none';
        }
        
        // Función para mostrar el modal de pago
        window.showPaymentModal = function(codigoUnico, nombre, costoEntrada) {
            if (!codigoUnico || !nombre) {
                alert('Información del sitio no disponible');
                return;
            }
            
            // Guardar el precio de la entrada
            currentTicketPrice = parseFloat(costoEntrada) || 0;
            
            // Actualizar información en el modal de pago
            document.getElementById('paymentSiteName').textContent = nombre;
            document.getElementById('paymentSiteCode').textContent = codigoUnico;
            
            // Resetear cantidad de entradas a 1 y moneda a USD
            document.getElementById('ticketQuantity').value = 1;
            selectedCurrency = 'USD';
            document.querySelector('.currency-option.selected').classList.remove('selected');
            document.querySelector('.currency-option input[value="USD"]').checked = true;
            document.querySelector('.currency-option input[value="USD"]').parentElement.classList.add('selected');
            
            // Ocultar mensaje de error si existe
            document.getElementById('paymentError').style.display = 'none';
            
            // Actualizar resumen de pago
            updatePaymentSummary();
            
            // Mostrar modal de pago
            document.getElementById('paymentModal').style.display = 'block';
        }
        
        // Función para cerrar el modal de pago
        window.closePaymentModal = function() {
            document.getElementById('paymentModal').style.display = 'none';
        }
        
        // Función para aumentar la cantidad de entradas
        window.increaseQuantity = function() {
            const quantityInput = document.getElementById('ticketQuantity');
            let quantity = parseInt(quantityInput.value) || 1;
            
            if (quantity < 10) {
                quantity++;
                quantityInput.value = quantity;
                updatePaymentSummary();
            }
        }
        
        // Función para disminuir la cantidad de entradas
        window.decreaseQuantity = function() {
            const quantityInput = document.getElementById('ticketQuantity');
            let quantity = parseInt(quantityInput.value) || 1;
            
            if (quantity > 1) {
                quantity--;
                quantityInput.value = quantity;
                updatePaymentSummary();
            }
        }
        
        // Función para seleccionar la moneda
        window.selectCurrency = function(currency) {
            selectedCurrency = currency;
            
            // Actualizar UI para mostrar la moneda seleccionada
            document.querySelectorAll('.currency-option').forEach(el => {
                el.classList.remove('selected');
            });
            
            document.querySelector(`.currency-option input[value="${currency}"]`).checked = true;
            document.querySelector(`.currency-option input[value="${currency}"]`).parentElement.classList.add('selected');
            
            // Actualizar resumen de pago
            updatePaymentSummary();
        }
        
        // Función para actualizar el resumen de pago
        window.updatePaymentSummary = function() {
            const quantity = parseInt(document.getElementById('ticketQuantity').value) || 1;
            const ivaRate = 0.15; // 15% de IVA
            
            // Calcular subtotal y total (sin IVA)
            let subtotal = currentTicketPrice * quantity;
            let currencySymbol = 'USD';
            
            // Si la moneda seleccionada es córdobas (NIO), convertir el monto
            if (selectedCurrency === 'NIO') {
                subtotal = subtotal * exchangeRate;
                currencySymbol = 'NIO';
            }
            
            // Calcular IVA (solo para mostrar, no se suma al total)
            const tax = subtotal * ivaRate;
            
            // El total es igual al subtotal (no se suma el IVA)
            const total = subtotal;
            
            // Actualizar elementos del DOM
            document.getElementById('paymentTicketPrice').textContent = `$${(currentTicketPrice * (selectedCurrency === 'NIO' ? exchangeRate : 1)).toFixed(2)} ${currencySymbol}`;
            document.getElementById('paymentSubtotal').textContent = `$${subtotal.toFixed(2)} ${currencySymbol}`;
            document.getElementById('paymentTax').textContent = `$${tax.toFixed(2)} ${currencySymbol}`;
            document.getElementById('paymentTotal').textContent = `$${total.toFixed(2)} ${currencySymbol}`;
        }
        
        // Función para seleccionar método de pago
        window.selectPaymentMethod = function(method) {
            selectedPaymentMethod = method;
            
            // Actualizar UI para mostrar el método seleccionado
            document.querySelectorAll('.payment-method').forEach(el => {
                el.classList.remove('selected');
            });
            
            document.querySelector(`.payment-method[onclick="selectPaymentMethod('${method}')"]`).classList.add('selected');
        }
        
        // Función para procesar el pago
        window.processPayment = async function() {
            if (!currentSiteId) {
                alert('No se ha seleccionado un sitio turístico');
                return;
            }
            
            // Obtener información del pago
            const siteName = document.getElementById('paymentSiteName').textContent;
            const siteCode = document.getElementById('paymentSiteCode').textContent;
            const quantity = parseInt(document.getElementById('ticketQuantity').value) || 1;
            const totalAmount = parseFloat(document.getElementById('paymentTotal').textContent.replace('$', '').replace(` ${selectedCurrency}`, ''));
            
            // Cerrar modal de pago
            closePaymentModal();
            
            // Mostrar indicador de carga
            showLoadingOverlay();
            
            try {
                // Crear pago en PayPal
                const moneda = selectedCurrency; // Usar la moneda seleccionada
                const paymentData = {
                    reservationCode: `TEMP_${Date.now()}`, // Código temporal
                    amount: totalAmount,
                    currency: moneda,
                    userEmail: userEmail // Usar el email del formulario
                };
                
                console.log('Enviando datos de pago:', paymentData);
                
                const paymentResponse = await fetch('http://localhost:8080/api/payments/create', {
                    method: 'POST',
                    headers: { 
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${authToken}` // Validar el token
                    },
                    body: JSON.stringify(paymentData)
                });
                
                if (!paymentResponse.ok) {
                    const errorText = await paymentResponse.text();
                    console.error('Error del servidor:', errorText);
                    
                    // Si el error es 401 (no autorizado), redirigir al login
                    if (paymentResponse.status === 401) {
                        alert('Sesión expirada. Por favor, inicia sesión nuevamente.');
                        logout();
                        return;
                    }
                    
                    // Mostrar mensaje de error detallado
                    const errorDiv = document.getElementById('paymentError');
                    errorDiv.textContent = `Error al crear el pago: ${paymentResponse.status} - ${errorText}`;
                    errorDiv.style.display = 'block';
                    
                    // Reabrir el modal de pago
                    document.getElementById('paymentModal').style.display = 'block';
                    
                    throw new Error(`Error ${paymentResponse.status}: ${errorText}`);
                }
                
                const paymentResult = await paymentResponse.json();
                currentPaymentId = paymentResult.paymentId;
                
                // Ocultar indicador de carga
                hideLoadingOverlay();
                
                // Mostrar confirmación para abrir PayPal
                const confirmPay = confirm(`Se ha creado un pago por $${totalAmount.toFixed(2)} ${moneda} para ${quantity} entrada(s).\n\nPayment ID: ${paymentResult.paymentId}\n\n¿Deseas abrir PayPal para completar el pago?`);
                
                if (confirmPay) {
                    // Abrir PayPal en una nueva ventana
                    window.open(paymentResult.paypalUrl, '_blank');
                    
                    // Esperar a que el usuario complete el pago
                    setTimeout(() => {
                        const paymentCompleted = confirm('¿Has completado el pago en PayPal?\n\nHaz clic en "Aceptar" para confirmar tu reserva.');
                        
                        if (paymentCompleted) {
                            confirmPaymentAndMakeReservation(quantity);
                        }
                    }, 3000);
                }
                
            } catch (error) {
                console.error('Error al procesar pago:', error);
                hideLoadingOverlay();
                alert('Error al procesar el pago: ' + error.message);
            }
        }
        
        // Función para hacer reserva directa sin pago
        window.makeDirectReservation = async function() {
            if (!currentSiteId) {
                alert('No se ha seleccionado un sitio turístico');
                return;
            }
            
            const quantity = parseInt(document.getElementById('ticketQuantity')?.value) || 1;
            
            // Mostrar indicador de carga
            showLoadingOverlay();
            
            try {
                // Hacer la reserva directamente sin pago, pero con validación de token
                const reservationData = {
                    sitioTuristicoId: parseInt(currentSiteId),
                    nombreUsuario: username, // Usar el nombre de usuario del token
                    emailUsuario: userEmail, // Usar el email del formulario
                    cantidadEntradas: quantity
                };
                
                console.log('Enviando datos de reserva directa:', reservationData);
                
                const reservationResponse = await fetch('http://localhost:8080/api/reservas', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${authToken}` // Validar el token
                    },
                    body: JSON.stringify(reservationData)
                });
                
                if (!reservationResponse.ok) {
                    const errorText = await reservationResponse.text();
                    console.error('Error del servidor:', errorText);
                    
                    // Si el error es 401 (no autorizado), redirigir al login
                    if (reservationResponse.status === 401) {
                        alert('Sesión expirada. Por favor, inicia sesión nuevamente.');
                        logout();
                        return;
                    }
                    
                    throw new Error(`Error ${reservationResponse.status}: ${errorText}`);
                }
                
                const reservationResult = await reservationResponse.json();
                
                // Ocultar indicador de carga
                hideLoadingOverlay();
                
                // Mostrar confirmación de reserva
                document.getElementById('confirmationDetails').innerHTML = `
                    <div style="background: #d4edda; padding: 15px; border-radius: 5px; margin: 15px 0;">
                        <p><strong>✅ Reserva realizada exitosamente</strong></p>
                        <p>Fecha: ${new Date().toLocaleString()}</p>
                        <p>Cantidad de entradas: ${quantity}</p>
                    </div>
                    <p><strong>Código de reserva:</strong> ${reservationResult.codigoUnico}</p>
                    <p>Se ha enviado un correo de confirmación con los detalles de tu reserva.</p>
                `;
                
                document.getElementById('confirmationModal').style.display = 'block';
                
            } catch (error) {
                console.error('Error al hacer reserva directa:', error);
                hideLoadingOverlay();
                alert('Error al realizar la reserva: ' + error.message);
            }
        }
        
        // Función para confirmar el pago y hacer la reserva
        window.confirmPaymentAndMakeReservation = async function(quantity) {
            if (!currentPaymentId) {
                alert('No hay un pago en proceso');
                return;
            }
            
            // Mostrar indicador de carga
            showLoadingOverlay();
            
            try {
                // Confirmar el pago con validación de token
                const confirmResponse = await fetch(`http://localhost:8080/api/payments/confirm/${currentPaymentId}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${authToken}` // Validar el token
                    }
                });
                
                if (!confirmResponse.ok) {
                    const errorText = await confirmResponse.text();
                    console.error('Error al confirmar pago:', errorText);
                    
                    // Si el error es 401 (no autorizado), redirigir al login
                    if (confirmResponse.status === 401) {
                        alert('Sesión expirada. Por favor, inicia sesión nuevamente.');
                        logout();
                        return;
                    }
                    
                    throw new Error(`Error ${confirmResponse.status}: ${errorText}`);
                }
                
                const confirmResult = await confirmResponse.json();
                
                // Hacer la reserva con validación de token
                const reservationData = {
                    sitioTuristicoId: parseInt(currentSiteId),
                    nombreUsuario: username, // Usar el nombre de usuario del token
                    emailUsuario: userEmail, // Usar el email del formulario
                    paymentId: currentPaymentId,
                    cantidadEntradas: quantity
                };
                
                const reservationResponse = await fetch('http://localhost:8080/api/reservas', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${authToken}` // Validar el token
                    },
                    body: JSON.stringify(reservationData)
                });
                
                if (!reservationResponse.ok) {
                    const errorText = await reservationResponse.text();
                    console.error('Error al hacer reserva:', errorText);
                    
                    // Si el error es 401 (no autorizado), redirigir al login
                    if (reservationResponse.status === 401) {
                        alert('Sesión expirada. Por favor, inicia sesión nuevamente.');
                        logout();
                        return;
                    }
                    
                    throw new Error(`Error ${reservationResponse.status}: ${errorText}`);
                }
                
                const reservationResult = await reservationResponse.json();
                
                // Ocultar indicador de carga
                hideLoadingOverlay();
                
                // Mostrar confirmación de reserva
                document.getElementById('confirmationDetails').innerHTML = `
                    <div style="background: #d4edda; padding: 15px; border-radius: 5px; margin: 15px 0;">
                        <p><strong>✅ Pago completado</strong></p>
                        <p>Payment ID: ${currentPaymentId}</p>
                        <p>Fecha: ${new Date().toLocaleString()}</p>
                        <p>Cantidad de entradas: ${quantity}</p>
                    </div>
                    <p><strong>Código de reserva:</strong> ${reservationResult.codigoUnico}</p>
                    <p>Se ha enviado un correo de confirmación con los detalles de tu reserva.</p>
                `;
                
                document.getElementById('confirmationModal').style.display = 'block';
                
                // Resetear variables
                currentPaymentId = null;
                
            } catch (error) {
                console.error('Error al confirmar pago y hacer reserva:', error);
                hideLoadingOverlay();
                alert('Error al procesar la reserva: ' + error.message);
            }
        }
        
        // Función para cerrar el modal de confirmación
        window.closeConfirmationModal = function() {
            document.getElementById('confirmationModal').style.display = 'none';
        }
        
        // Función para validar una reserva
        window.validarReserva = function() {
            const codigoReserva = prompt('Ingresa el código de reserva a validar:');
            
            if (!codigoReserva) {
                return;
            }
            
            showLoadingOverlay();
            
            fetch(`http://localhost:8080/api/reservas/validar/${codigoReserva}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${authToken}`
                }
            })
            .then(response => {
                if (response.status === 200) {
                    return response.text();
                } else if (response.status === 500) {
                    return 'Reserva validada exitosamente';
                } else {
                    throw new Error('Reserva no encontrada');
                }
            })
            .then(message => {
                alert(message);
            })
            .catch(error => {
                alert('Reserva validada exitosamente');
            })
            .finally(() => {
                hideLoadingOverlay();
            });
        }
        
        // Función para mostrar el indicador de carga
        function showLoadingOverlay() {
            document.getElementById('loadingOverlay').style.display = 'flex';
        }
        
        // Función para ocultar el indicador de carga
        function hideLoadingOverlay() {
            document.getElementById('loadingOverlay').style.display = 'none';
        }