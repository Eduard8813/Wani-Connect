
        document.addEventListener('DOMContentLoaded', function() {
            const resultadosDiv = document.getElementById('resultados');
            const cargandoDiv = document.getElementById('cargando');
            const sinResultadosDiv = document.getElementById('sin-resultados');
            const buscarBtn = document.getElementById('buscar-btn');
            const fechaInicioInput = document.getElementById('fecha-inicio');
            const fechaFinInput = document.getElementById('fecha-fin');
            
            // Variables globales
            let todosLosVehiculos = [];
            let reservaActual = null;
            
            // Función para verificar si el usuario está autenticado
            function estaAutenticado() {
                const token = localStorage.getItem('token');
                const userEmail = localStorage.getItem('userEmail');
                
                // Depuración: Mostrar en consola el estado de autenticación
                console.log('Verificando autenticación...');
                console.log('Token:', token ? 'Existe' : 'No existe');
                console.log('UserEmail:', userEmail ? userEmail : 'No existe');
                
                // Verificar que ambos existan y no estén vacíos
                return token && token.trim() !== '' && userEmail && userEmail.trim() !== '';
            }
            
            // Función para obtener vehículos desde la API
            async function obtenerVehiculos() {
                cargandoDiv.classList.remove('d-none');
                resultadosDiv.innerHTML = '';
                sinResultadosDiv.classList.add('d-none');
                
                // Usar AbortController para implementar timeout
                const controller = new AbortController();
                const timeoutId = setTimeout(() => controller.abort(), 15000); // Aumentado a 15 segundos
                
                try {
                    const token = localStorage.getItem('token');
                    const headers = {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    };
                    
                    // Si hay token, agregarlo al header
                    if (token) {
                        headers['Authorization'] = `Bearer ${token}`;
                    }
                    
                    // Agregar cache control para evitar problemas de caché
                    headers['Cache-Control'] = 'no-cache';
                    
                    const response = await fetch('http://localhost:8080/api/alquiler-vehiculos', {
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
                    
                    // Guardar todos los vehículos para filtrar después
                    todosLosVehiculos = data;
                    
                    if (data.length === 0) {
                        sinResultadosDiv.classList.remove('d-none');
                    } else {
                        mostrarVehiculos(data);
                    }
                } catch (error) {
                    clearTimeout(timeoutId);
                    console.error('Error al obtener vehículos:', error);
                    
                    // Mostrar mensaje de no hay vehículos disponibles en caso de cualquier error
                    sinResultadosDiv.classList.remove('d-none');
                    
                    // Si el error es por aborto (timeout), mostrar un mensaje específico
                    if (error.name === 'AbortError') {
                        console.error('La petición fue abortada por timeout');
                    }
                } finally {
                    cargandoDiv.classList.add('d-none');
                }
            }
            
            // Función para mostrar los vehículos en tarjetas
            function mostrarVehiculos(vehiculos) {
                resultadosDiv.innerHTML = '';
                
                if (vehiculos.length === 0) {
                    sinResultadosDiv.classList.remove('d-none');
                    return;
                }
                
                vehiculos.forEach(vehiculo => {
                    // Verificar que tengamos los datos necesarios
                    if (!vehiculo || !vehiculo.nombreEmpresa) {
                        console.error('Uno de los vehículos no tiene la estructura esperada:', vehiculo);
                        return;
                    }
                    
                    const card = document.createElement('div');
                    card.className = 'col-md-6 col-lg-4';
                    
                    // Obtener la URL de la imagen o usar una imagen por defecto
                    let imagenUrl = '';
                    if (vehiculo.imagenes && vehiculo.imagenes.length > 0 && vehiculo.imagenes[0].url) {
                        imagenUrl = vehiculo.imagenes[0].url;
                    }
                    
                    // Obtener el código único del vehículo
                    const codigoUnico = vehiculo.codigoUnico || vehiculo.nombreEmpresa.replace(/\s+/g, '-').toLowerCase();
                    
                    card.innerHTML = `
                        <div class="card">
                            <div class="card-img-container">
                                ${imagenUrl ? 
                                    `<img src="${imagenUrl}" class="card-img-top" alt="${vehiculo.nombreEmpresa}" 
                                     onerror="this.onerror=null; this.src='https://picsum.photos/seed/${codigoUnico}/400/300.jpg';">` : 
                                    `<div class="bg-secondary text-white text-center d-flex align-items-center justify-content-center" style="height: 100%;">
                                        <i class="bi bi-image" style="font-size: 3rem;"></i>
                                    </div>`
                                }
                                <div class="card-badge">${vehiculo.tipoVehiculo || 'Sin tipo'}</div>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">${vehiculo.nombreEmpresa}</h5>
                                <div class="card-location">
                                    <i class="bi bi-geo-alt-fill"></i>
                                    <span>${vehiculo.ubicacion ? vehiculo.ubicacion.direccion : 'Dirección no disponible'}</span>
                                </div>
                                <div class="card-schedule">
                                    <i class="bi bi-people-fill"></i>
                                    <span>Capacidad: ${vehiculo.capacidad || 'No especificada'} personas</span>
                                </div>
                                <div class="card-services">
                                    <i class="bi bi-info-circle-fill"></i>
                                    <span>${vehiculo.caracteristicasTecnicas ? vehiculo.caracteristicasTecnicas.substring(0, 80) + '...' : 'Características no especificadas'}</span>
                                </div>
                            </div>
                            <div class="card-footer">
                                <div>
                                    <span class="price-tag">$${(vehiculo.precioPorDia || 0).toFixed(2)}</span>
                                    <small>/día</small>
                                </div>
                                <button class="btn btn-outline-primary reserva-btn" data-codigo="${codigoUnico}" data-precio="${vehiculo.precioPorDia || 0}">
                                    Reservar
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
                        const fechaInicio = document.getElementById('fecha-inicio').value;
                        const fechaFin = document.getElementById('fecha-fin').value;
                        const capacidad = document.getElementById('capacidad').value;
                        
                        procesarReserva(codigo, precio, tipo, fechaInicio, fechaFin, capacidad);
                    });
                });
            }
            
            // Función para filtrar vehículos
            function filtrarVehiculos() {
                const tipo = document.getElementById('tipo').value;
                const fechaInicio = document.getElementById('fecha-inicio').value;
                const fechaFin = document.getElementById('fecha-fin').value;
                const capacidad = document.getElementById('capacidad').value;
                
                let vehiculosFiltrados = [...todosLosVehiculos];
                
                // Filtrar por tipo
                if (tipo) {
                    vehiculosFiltrados = vehiculosFiltrados.filter(v => v.tipoVehiculo === tipo);
                }
                
                // Filtrar por capacidad
                if (capacidad) {
                    if (capacidad === '7+') {
                        vehiculosFiltrados = vehiculosFiltrados.filter(v => v.capacidad >= 7);
                    } else {
                        vehiculosFiltrados = vehiculosFiltrados.filter(v => v.capacidad === parseInt(capacidad));
                    }
                }
                
                // Por ahora, no filtramos por fechas porque no tenemos esa información
                // En una aplicación real, deberías tener información de disponibilidad
                
                mostrarVehiculos(vehiculosFiltrados);
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
            
            // Función para calcular días entre dos fechas
            function calcularDias(fechaInicio, fechaFin) {
                if (!fechaInicio || !fechaFin) return 1;
                
                const inicio = new Date(fechaInicio);
                const fin = new Date(fechaFin);
                
                // Asegurarse de que la fecha de fin sea posterior a la de inicio
                if (fin < inicio) return 0;
                
                // Calcular la diferencia en milisegundos y convertirla a días
                const diferenciaMs = fin - inicio;
                const diferenciaDias = Math.ceil(diferenciaMs / (1000 * 60 * 60 * 24));
                
                // Asegurarse de que al menos sea 1 día
                return Math.max(1, diferenciaDias);
            }
            
            // Evento para el botón de búsqueda
            buscarBtn.addEventListener('click', function() {
                // Validar que se hayan seleccionado fechas de alquiler
                const fechaInicio = document.getElementById('fecha-inicio').value;
                const fechaFin = document.getElementById('fecha-fin').value;
                
                if (!fechaInicio) {
                    alert('Por favor, selecciona una fecha de inicio');
                    document.getElementById('fecha-inicio').focus();
                    return;
                }
                
                if (!fechaFin) {
                    alert('Por favor, selecciona una fecha de fin');
                    document.getElementById('fecha-fin').focus();
                    return;
                }
                
                // Validar que la fecha de fin sea posterior a la de inicio
                if (new Date(fechaFin) <= new Date(fechaInicio)) {
                    alert('La fecha de fin debe ser posterior a la fecha de inicio');
                    document.getElementById('fecha-fin').focus();
                    return;
                }
                
                // Si ya tenemos todos los vehículos cargados, filtrarlos
                if (todosLosVehiculos.length > 0) {
                    filtrarVehiculos();
                } else {
                    // Si no, obtenerlos y luego filtrar
                    obtenerVehiculos().then(() => {
                        filtrarVehiculos();
                    });
                }
            });
            
            // Función para procesar la reserva
            async function procesarReserva(codigo, precio, tipo, fechaInicio, fechaFin, capacidad) {
                try {
                    // Validar que se hayan seleccionado fechas de alquiler
                    if (!fechaInicio || !fechaFin) {
                        alert('Por favor, selecciona las fechas de alquiler');
                        if (!fechaInicio) document.getElementById('fecha-inicio').focus();
                        else document.getElementById('fecha-fin').focus();
                        return;
                    }
                    
                    // Validar que la fecha de fin sea posterior a la de inicio
                    if (new Date(fechaFin) <= new Date(fechaInicio)) {
                        alert('La fecha de fin debe ser posterior a la fecha de inicio');
                        document.getElementById('fecha-fin').focus();
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
                    const token = localStorage.getItem('token');
                    const userEmail = localStorage.getItem('userEmail');
                    
                    console.log('Usuario autenticado. Procesando reserva...');
                    console.log('Token:', token);
                    console.log('UserEmail:', userEmail);
                    
                    // Validar si el vehículo existe
                    const controller = new AbortController();
                    const timeoutId = setTimeout(() => controller.abort(), 15000); // Aumentado a 15 segundos
                    
                    const response = await fetch(`http://localhost:8080/api/alquiler-vehiculos/${codigo}`, {
                        signal: controller.signal,
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json',
                            'Authorization': `Bearer ${token}`,
                            'Cache-Control': 'no-cache'
                        },
                        mode: 'cors',
                        credentials: 'include'
                    });
                    
                    clearTimeout(timeoutId);
                    
                    if (!response.ok) {
                        throw new Error('El vehículo no existe');
                    }
                    
                    const vehiculo = await response.json();
                    console.log('Vehículo obtenido:', vehiculo);
                    
                    // Calcular el número de días de alquiler
                    const diasAlquiler = calcularDias(fechaInicio, fechaFin);
                    
                    // Calcular el costo total
                    const costoTotal = precio * diasAlquiler;
                    
                    // Guardar información de la reserva actual
                    reservaActual = {
                        vehiculo: vehiculo,
                        fechaInicio: fechaInicio,
                        fechaFin: fechaFin,
                        diasAlquiler: diasAlquiler,
                        precioPorDia: precio,
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
                
                // Llenar los datos del modal con fechas formateadas
                document.getElementById('reserva-nombre').textContent = reservaActual.vehiculo.nombreEmpresa || 'Empresa no disponible';
                document.getElementById('reserva-tipo').textContent = reservaActual.vehiculo.tipoVehiculo || 'Tipo no disponible';
                document.getElementById('reserva-ubicacion').textContent = reservaActual.vehiculo.ubicacion ? reservaActual.vehiculo.ubicacion.direccion : 'Dirección no disponible';
                document.getElementById('reserva-fechainicio').textContent = formatearFecha(reservaActual.fechaInicio);
                document.getElementById('reserva-fechafin').textContent = formatearFecha(reservaActual.fechaFin);
                document.getElementById('reserva-dias').textContent = reservaActual.diasAlquiler;
                document.getElementById('reserva-precio').textContent = reservaActual.precioPorDia.toFixed(2);
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
                
                // Llenar los datos del modal con fechas formateadas
                document.getElementById('pago-email').value = localStorage.getItem('userEmail') || '';
                document.getElementById('pago-empresa').textContent = reservaActual.vehiculo.nombreEmpresa || 'Empresa no disponible';
                document.getElementById('pago-tipo').textContent = reservaActual.vehiculo.tipoVehiculo || 'Tipo no disponible';
                document.getElementById('pago-fechas').textContent = `${formatearFecha(reservaActual.fechaInicio)} - ${formatearFecha(reservaActual.fechaFin)}`;
                document.getElementById('pago-dias').textContent = reservaActual.diasAlquiler;
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
                        reservationCode: `ALQ-${Date.now()}`,
                        amount: reservaActual.total,
                        currency: moneda,
                        userEmail: email
                    };
                    
                    console.log('Enviando solicitud de pago:', JSON.stringify(pagoData));
                    
                    // Enviar petición de pago
                    const token = localStorage.getItem('token');
                    const response = await fetch('http://localhost:8080/api/payments/create', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json',
                            'Authorization': `Bearer ${token}`
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
                document.getElementById('confirmacion-codigo').textContent = pagoResponse.reservationCode || `ALQ-${Date.now()}`;
                document.getElementById('confirmacion-empresa').textContent = reservaActual.vehiculo.nombreEmpresa || 'Empresa no disponible';
                document.getElementById('confirmacion-tipo').textContent = reservaActual.vehiculo.tipoVehiculo || 'Tipo no disponible';
                document.getElementById('confirmacion-fechas').textContent = `${formatearFecha(reservaActual.fechaInicio)} - ${formatearFecha(reservaActual.fechaFin)}`;
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
            fechaInicioInput.setAttribute('min', today);
            
            // Establecer fecha mínima de fin como la fecha de inicio
            fechaInicioInput.addEventListener('change', function() {
                fechaFinInput.setAttribute('min', this.value);
            });
            
            // Verificar si hay una URL de redirección al cargar la página
            const redirectUrl = localStorage.getItem('redirectUrl');
            if (redirectUrl && estaAutenticado()) {
                console.log('Redirigiendo a:', redirectUrl);
                localStorage.removeItem('redirectUrl');
                window.location.href = redirectUrl;
            }
            
            // Cargar vehículos al iniciar la página
            obtenerVehiculos();
        });