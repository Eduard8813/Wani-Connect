        document.addEventListener('DOMContentLoaded', function() {
            const resultadosDiv = document.getElementById('resultados');
            const cargandoDiv = document.getElementById('cargando');
            const sinResultadosDiv = document.getElementById('sin-resultados');
            const buscarBtn = document.getElementById('buscar-btn');
            const fechaVisitaInput = document.getElementById('fecha-visita');
            
            // Variables globales
            let todosLosSitios = [];
            let reservaActual = null;
            
            // Función para verificar si el usuario está autenticado
            function estaAutenticado() {
                const authToken = localStorage.getItem('authToken');
                const userEmail = localStorage.getItem('userEmail');
                
                // Depuración: Mostrar en consola el estado de autenticación
                console.log('Verificando autenticación...');
                console.log('authToken:', authToken ? 'Existe' : 'No existe');
                console.log('UserEmail:', userEmail ? userEmail : 'No existe');
                
                // Verificar que ambos existan y no estén vacíos
                return authToken && authToken.trim() !== '' && userEmail && userEmail.trim() !== '';
            }
            
            // Función para obtener sitios turísticos desde la API
            async function obtenerSitiosTuristicos() {
                cargandoDiv.classList.remove('d-none');
                resultadosDiv.innerHTML = '';
                sinResultadosDiv.classList.add('d-none');
                
                // Usar AbortController para implementar timeout
                const controller = new AbortController();
                const timeoutId = setTimeout(() => controller.abort(), 15000); // Aumentado a 15 segundos
                
                try {
                    const authToken = localStorage.getItem('authToken');
                    const headers = {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    };
                    
                    // Si hay token, agregarlo al header
                    if (authToken) {
                        headers['Authorization'] = `Bearer ${authToken}`;
                    }
                    
                    // Agregar cache control para evitar problemas de caché
                    headers['Cache-Control'] = 'no-cache';
                    
                    const response = await fetch('https://wani-connect.onrender.com/api/sitios-turisticos', {
                        signal: controller.signal,
                        method: 'GET',
                        headers: headers,
                        // Agregar modo cors y credenciales
                        mode: 'cors',
                        credentials: 'include'
                    });
                    
                    clearTimeout(timeoutId);
                    
                    // Verificar si la respuesta es OK (200-299)
                    if (!response.ok) {
                        throw new Error(`Error en la petición: ${response.status} ${response.statusText}`);
                    }
                    
                    // Verificar el tipo de contenido
                    const contentType = response.headers.get('content-type');
                    if (!contentType || !contentType.includes('application/json')) {
                        throw new Error('La respuesta no es JSON válido');
                    }
                    
                    // Clonar la respuesta para poder leerla varias veces si es necesario
                    const clonedResponse = response.clone();
                    
                    // Intentar parsear la respuesta como JSON
                    let data;
                    try {
                        // Obtener el texto de la respuesta primero
                        const responseText = await response.text();
                        
                        // Verificar si la respuesta está vacía
                        if (!responseText || responseText.trim() === '') {
                            throw new Error('La respuesta está vacía');
                        }
                        
                        // Intentar parsear el texto como JSON
                        data = JSON.parse(responseText);
                    } catch (jsonError) {
                        console.error('Error al parsear JSON:', jsonError);
                        
                        // Intentar obtener el texto de la respuesta clonada para depuración
                        try {
                            const debugText = await clonedResponse.text();
                            console.error('Texto de la respuesta:', debugText.substring(0, 500) + (debugText.length > 500 ? '...' : ''));
                        } catch (e) {
                            console.error('No se pudo obtener el texto de la respuesta para depuración');
                        }
                        
                        throw new Error('Error al procesar la respuesta del servidor');
                    }
                    
                    // Verificar que los datos sean un array
                    if (!Array.isArray(data)) {
                        console.error('Los datos recibidos no son un array:', data);
                        throw new Error('Formato de datos incorrecto');
                    }
                    
                    // Guardar todos los sitios para filtrar después
                    todosLosSitios = data;
                    
                    if (data.length === 0) {
                        sinResultadosDiv.classList.remove('d-none');
                    } else {
                        mostrarSitiosTuristicos(data);
                    }
                } catch (error) {
                    clearTimeout(timeoutId);
                    console.error('Error al obtener sitios turísticos:', error);
                    
                    // Mostrar mensaje de no hay sitios disponibles en caso de cualquier error
                    sinResultadosDiv.classList.remove('d-none');
                    
                    // Si el error es por aborto (timeout), mostrar un mensaje específico
                    if (error.name === 'AbortError') {
                        console.error('La petición fue abortada por timeout');
                    }
                } finally {
                    cargandoDiv.classList.add('d-none');
                }
            }
            
            // Función para mostrar los sitios turísticos en tarjetas
            function mostrarSitiosTuristicos(sitios) {
                resultadosDiv.innerHTML = '';
                
                if (sitios.length === 0) {
                    sinResultadosDiv.classList.remove('d-none');
                    return;
                }
                
                sitios.forEach(sitio => {
                    // Verificar que cada sitio tenga la estructura esperada
                    if (!sitio || !sitio.nombre) {
                        console.error('Uno de los sitios no tiene la estructura esperada:', sitio);
                        return;
                    }
                    
                    const card = document.createElement('div');
                    card.className = 'col-md-6 col-lg-4';
                    
                    // Obtener la URL de la imagen o usar una imagen por defecto
                    let imagenUrl = '';
                    if (sitio.imagenes && sitio.imagenes.length > 0 && sitio.imagenes[0].url) {
                        imagenUrl = sitio.imagenes[0].url;
                    }
                    
                    // Obtener el código único del sitio
                    const codigoUnico = sitio.codigoUnico || sitio.nombre.replace(/\s+/g, '-').toLowerCase();
                    
                    card.innerHTML = `
                        <div class="card">
                            <div class="card-img-container">
                                ${imagenUrl ? 
                                    `<img src="${imagenUrl}" class="card-img-top" alt="${sitio.nombre}" 
                                     onerror="this.onerror=null; this.src='https://picsum.photos/seed/${codigoUnico}/400/300.jpg';">` : 
                                    `<div class="bg-secondary text-white text-center d-flex align-items-center justify-content-center" style="height: 100%;">
                                        <i class="bi bi-image" style="font-size: 3rem;"></i>
                                    </div>`
                                }
                                <div class="card-badge">${sitio.tipoLugar || 'Sin tipo'}</div>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">${sitio.nombre}</h5>
                                <div class="card-location">
                                    <i class="bi bi-geo-alt-fill"></i>
                                    <span>${sitio.ubicacionGeografica ? sitio.ubicacionGeografica.direccion : 'Dirección no disponible'}</span>
                                </div>
                                <div class="card-schedule">
                                    <i class="bi bi-clock-fill"></i>
                                    <span>${sitio.horarioAtencion || 'Horario no disponible'}</span>
                                </div>
                                <div class="card-services">
                                    <i class="bi bi-info-circle-fill"></i>
                                    <span>${sitio.serviciosDisponibles || 'Servicios no especificados'}</span>
                                </div>
                            </div>
                            <div class="card-footer">
                                <div>
                                    <span class="price-tag">$${(sitio.costoEntrada || 0).toFixed(2)}</span>
                                    <small>/entrada</small>
                                </div>
                                <button class="btn btn-outline-primary reserva-btn" data-codigo="${codigoUnico}" data-precio="${sitio.costoEntrada || 0}">
                                    Comprar
                                </button>
                            </div>
                        </div>
                    `;
                    
                    resultadosDiv.appendChild(card);
                });
                
                // Agregar event listeners a los botones de reserva
                document.querySelectorAll('.reserva-btn').forEach(btn => {
                    btn.addEventListener('click', function() {
                        const codigo = this.getAttribute('data-codigo');
                        const precio = parseFloat(this.getAttribute('data-precio'));
                        
                        // Obtener los datos del formulario de búsqueda
                        const tipo = document.getElementById('tipo').value;
                        const fechaVisita = document.getElementById('fecha-visita').value;
                        const visitantes = document.getElementById('visitantes').value;
                        
                        procesarReserva(codigo, precio, tipo, fechaVisita, visitantes);
                    });
                });
            }
            
            // Función para filtrar sitios turísticos
            function filtrarSitiosTuristicos() {
                const tipo = document.getElementById('tipo').value;
                const fechaVisita = document.getElementById('fecha-visita').value;
                const visitantes = document.getElementById('visitantes').value;
                
                let sitiosFiltrados = [...todosLosSitios];
                
                // Filtrar por tipo
                if (tipo) {
                    sitiosFiltrados = sitiosFiltrados.filter(s => s.tipoLugar === tipo);
                }
                mostrarSitiosTuristicos(sitiosFiltrados);
            }
            
            // Función para formatear fechas
            function formatearFecha(fecha) {
                if (!fecha) return 'No especificada';
                const date = new Date(fecha);
                return date.toLocaleDateString('es-ES', { 
                    year: 'numeric', 
                    month: 'long', 
                    day: 'numeric' 
                });
            }
            
            // Evento para el botón de búsqueda
            buscarBtn.addEventListener('click', function() {
                // Validar que se haya seleccionado una fecha de visita
                const fechaVisita = document.getElementById('fecha-visita').value;
                if (!fechaVisita) {
                    alert('Por favor, selecciona una fecha de visita');
                    document.getElementById('fecha-visita').focus();
                    return;
                }
                
                // Si ya tenemos todos los sitios cargados, filtrarlos
                if (todosLosSitios.length > 0) {
                    filtrarSitiosTuristicos();
                } else {
                    // Si no, obtenerlos y luego filtrar
                    obtenerSitiosTuristicos().then(() => {
                        filtrarSitiosTuristicos();
                    });
                }
            });
            
            // Función para procesar la reserva
            async function procesarReserva(codigo, precio, tipo, fechaVisita, visitantes) {
                try {
                    // Validar que se haya seleccionado una fecha de visita
                    if (!fechaVisita) {
                        alert('Por favor, selecciona una fecha de visita');
                        document.getElementById('fecha-visita').focus();
                        return;
                    }
                    
                    // Verificar si el usuario está autenticado
                    if (!estaAutenticado()) {
                        console.log('Usuario no autenticado. Redirigiendo a iniciosesion.html...');
                        // Guardar la URL actual para redirigir después del inicio de sesión
                        localStorage.setItem('redirectUrl', window.location.href);
                        window.location.href = 'iniciosesion.html';
                        return;
                    }
                    
                    // Obtener el token y el correo del usuario del localStorage
                    const authToken = localStorage.getItem('token');
                    const userEmail = localStorage.getItem('userEmail');
                    
                    console.log('Usuario autenticado. Procesando reserva...');
                    console.log('authToken:', authToken);
                    console.log('UserEmail:', userEmail);
                    
                    // Validar si el sitio existe
                    const controller = new AbortController();
                    const timeoutId = setTimeout(() => controller.abort(), 15000); // Aumentado a 15 segundos
                    
                    const response = await fetch(`https://wani-connect.onrender.com/api/sitios-turisticos/${codigo}`, {
                        signal: controller.signal,
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json',
                            'Authorization': `Bearer ${authToken}`,
                            'Cache-Control': 'no-cache'
                        },
                        mode: 'cors',
                        credentials: 'include'
                    });
                    
                    clearTimeout(timeoutId);
                    
                    if (!response.ok) {
                        throw new Error('El sitio turístico no existe');
                    }
                    
                    const sitio = await response.json();
                    console.log('Sitio obtenido:', sitio);
                    
                    // Calcular el número de visitantes (por defecto 1)
                    let numVisitantes = 1;
                    if (visitantes) {
                        if (visitantes === '1') numVisitantes = 1;
                        else if (visitantes === '2') numVisitantes = 2;
                        else if (visitantes === '3-5') numVisitantes = 4; // Promedio
                        else if (visitantes === '6+') numVisitantes = 6; // Mínimo
                    }
                    
                    // Calcular el costo total
                    const costoTotal = precio * numVisitantes;
                    
                    // Guardar información de la reserva actual
                    reservaActual = {
                        sitio: sitio,
                        fechaVisita: fechaVisita,
                        numVisitantes: numVisitantes,
                        precioEntrada: precio,
                        subtotal: costoTotal,
                        iva: costoTotal * 0.15,
                        total: costoTotal + (costoTotal * 0.15)
                    };
                    
                    // Mostrar modal de reserva
                    mostrarModalReserva();
                    
                } catch (error) {
                    console.error('Error al procesar la reserva:', error);
                    alert('Error al procesar la reserva: ' + error.message);
                }
            }
            
            // Función para mostrar el modal de reserva
            function mostrarModalReserva() {
                if (!reservaActual) return;
                
                // Función auxiliar para obtener propiedades anidadas de forma segura
                function getNestedValue(obj, path, defaultValue = '') {
                    return path.split('.').reduce((o, p) => (o && o[p] ? o[p] : defaultValue), obj);
                }
                
                // Llenar los datos del modal con fechas formateadas
                document.getElementById('reserva-nombre').textContent = reservaActual.sitio.nombre || 'Nombre no disponible';
                document.getElementById('reserva-tipo').textContent = reservaActual.sitio.tipoLugar || 'Tipo no disponible';
                document.getElementById('reserva-ubicacion').textContent = getNestedValue(reservaActual.sitio, 'ubicacionGeografica.direccion', 'Dirección no disponible');
                document.getElementById('reserva-fecha-visita').textContent = formatearFecha(reservaActual.fechaVisita);
                document.getElementById('reserva-visitantes').textContent = reservaActual.numVisitantes;
                document.getElementById('reserva-precio-entrada').textContent = reservaActual.precioEntrada.toFixed(2);
                document.getElementById('reserva-subtotal').textContent = reservaActual.subtotal.toFixed(2);
                document.getElementById('reserva-iva').textContent = reservaActual.iva.toFixed(2);
                document.getElementById('reserva-total').textContent = reservaActual.total.toFixed(2);
                
                // Mostrar el modal
                const reservaModal = new bootstrap.Modal(document.getElementById('reservaModal'));
                reservaModal.show();
                
                // Configurar el botón de procesar pago
                document.getElementById('procesar-pago-btn').onclick = function() {
                    reservaModal.hide();
                    // Pequeña demora para asegurar que el modal se ha cerrado
                    setTimeout(() => {
                        mostrarModalPago();
                    }, 300);
                };
                
                // Evento para cuando se oculta el modal
                document.getElementById('reservaModal').addEventListener('hidden.bs.modal', function () {
                    // Mover el foco al botón de búsqueda
                    document.getElementById('buscar-btn').focus();
                });
            }
            
            // Función para mostrar el modal de pago
            function mostrarModalPago() {
                if (!reservaActual) return;
                
                // Función auxiliar para obtener propiedades anidadas de forma segura
                function getNestedValue(obj, path, defaultValue = '') {
                    return path.split('.').reduce((o, p) => (o && o[p] ? o[p] : defaultValue), obj);
                }
                
                // Llenar los datos del modal con fechas formateadas
                document.getElementById('pago-email').value = localStorage.getItem('userEmail') || '';
                document.getElementById('pago-sitio').textContent = reservaActual.sitio.nombre || 'Sitio no disponible';
                document.getElementById('pago-fecha').textContent = formatearFecha(reservaActual.fechaVisita);
                document.getElementById('pago-visitantes').textContent = reservaActual.numVisitantes;
                document.getElementById('pago-subtotal').textContent = reservaActual.subtotal.toFixed(2);
                document.getElementById('pago-iva').textContent = reservaActual.iva.toFixed(2);
                document.getElementById('pago-total').textContent = reservaActual.total.toFixed(2);
                
                // Mostrar el modal
                const pagoModal = new bootstrap.Modal(document.getElementById('pagoModal'));
                pagoModal.show();
                
                // Configurar el botón de confirmar pago
                document.getElementById('confirmar-pago-btn').onclick = function() {
                    procesarPago();
                };
                
                // Configurar el cambio de moneda
                document.getElementById('pago-monedas').onchange = function() {
                    actualizarMontosPago();
                };
                
                // Evento para cuando se oculta el modal
                document.getElementById('pagoModal').addEventListener('hidden.bs.modal', function () {
                    // Mover el foco al botón de búsqueda
                    document.getElementById('buscar-btn').focus();
                });
            }
            
            // Función para actualizar los montos según la moneda
            function actualizarMontosPago() {
                if (!reservaActual) return;
                
                const moneda = document.getElementById('pago-monedas').value;
                let subtotal, iva, total;
                
                if (moneda === 'NIO') {
                    // Suponiendo una tasa de cambio de 36.5 NIO por 1 USD
                    const tasaCambio = 36.5;
                    subtotal = reservaActual.subtotal * tasaCambio;
                    iva = reservaActual.iva * tasaCambio;
                    total = reservaActual.total * tasaCambio;
                } else {
                    subtotal = reservaActual.subtotal;
                    iva = reservaActual.iva;
                    total = reservaActual.total;
                }
                
                document.getElementById('pago-subtotal').textContent = subtotal.toFixed(2);
                document.getElementById('pago-iva').textContent = iva.toFixed(2);
                document.getElementById('pago-total').textContent = total.toFixed(2);
            }
            
            // Función para procesar el pago
            async function procesarPago() {
                if (!reservaActual) return;
                
                const email = document.getElementById('pago-email').value.trim();
                const metodo = document.getElementById('pago-metodo').value;
                const moneda = document.getElementById('pago-monedas').value;
                
                if (!email) {
                    alert('Por favor, ingresa un correo electrónico válido');
                    return;
                }
                
                try {
                    // Mostrar indicador de carga
                    const pagoModal = bootstrap.Modal.getInstance(document.getElementById('pagoModal'));
                    pagoModal.hide();
                    
                    // Crear objeto de pago - solo con los campos que espera el backend
                    const pagoData = {
                        reservationCode: `RES-${Date.now()}`,
                        amount: reservaActual.total,
                        currency: moneda,
                        userEmail: email
                    };
                    
                    console.log('Enviando solicitud de pago:', JSON.stringify(pagoData));
                    
                    // Enviar petición de pago
                    const authToken = localStorage.getItem('authToken');
                    const response = await fetch('https://wani-connect.onrender.com/api/payments/create', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json',
                            'Authorization': `Bearer ${authToken}`
                        },
                        body: JSON.stringify(pagoData)
                    });
                    
                    console.log('Respuesta del servidor:', response);
                    
                    if (!response.ok) {
                        const errorText = await response.text();
                        console.error('Error en respuesta:', errorText);
                        throw new Error(`Error ${response.status}: ${errorText}`);
                    }
                    
                    const pagoResponse = await response.json();
                    console.log('Respuesta de pago:', pagoResponse);
                    
                    // Si el método de pago es PayPal y hay una URL, redirigir
                    if (metodo === 'paypal' && pagoResponse.paypalUrl) {
                        // Mostrar modal de confirmación
                        mostrarModalConfirmacion(pagoResponse);
                        
                        // Abrir PayPal en una nueva ventana después de un breve retraso
                        setTimeout(() => {
                            window.open(pagoResponse.paypalUrl, '_blank');
                        }, 1000);
                    } else {
                        // Para otros métodos de pago, mostrar confirmación directamente
                        mostrarModalConfirmacion(pagoResponse);
                    }
                    
                } catch (error) {
                    console.error('Error al procesar el pago:', error);
                    alert('Error al procesar el pago: ' + error.message);
                }
            }
            
            // Función para mostrar el modal de confirmación
            function mostrarModalConfirmacion(pagoResponse) {
                if (!reservaActual) return;
                
                // Llenar los datos del modal con fechas formateadas
                document.getElementById('confirmacion-codigo').textContent = pagoResponse.reservationCode || `RES-${Date.now()}`;
                document.getElementById('confirmacion-sitio').textContent = reservaActual.sitio.nombre || 'Sitio no disponible';
                document.getElementById('confirmacion-fecha').textContent = formatearFecha(reservaActual.fechaVisita);
                document.getElementById('confirmacion-total').textContent = reservaActual.total.toFixed(2);
                document.getElementById('confirmacion-metodo').textContent = document.getElementById('pago-metodo').value === 'paypal' ? 'PayPal' : 'Tarjeta de Crédito/Débito';
                
                // Mostrar el modal
                const confirmacionModal = new bootstrap.Modal(document.getElementById('confirmacionModal'));
                confirmacionModal.show();
                
                // Evento para cuando se oculta el modal
                document.getElementById('confirmacionModal').addEventListener('hidden.bs.modal', function () {
                    // Enfocar el botón de búsqueda cuando se cierra el modal de confirmación
                    document.getElementById('buscar-btn').focus();
                });
                
                // Limpiar la reserva actual
                reservaActual = null;
            }
            
            // Establecer fecha mínima como hoy
            const today = new Date().toISOString().split('T')[0];
            fechaVisitaInput.setAttribute('min', today);
            
            // Verificar si hay una URL de redirección al cargar la página
            const redirectUrl = localStorage.getItem('redirectUrl');
            if (redirectUrl && estaAutenticado()) {
                console.log('Redirigiendo a:', redirectUrl);
                localStorage.removeItem('redirectUrl');
                window.location.href = redirectUrl;
            }
            
            // Cargar sitios turísticos al iniciar la página
            obtenerSitiosTuristicos();
        });