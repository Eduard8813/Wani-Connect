 document.addEventListener('DOMContentLoaded', function() {
            const resultadosDiv = document.getElementById('resultados');
            const cargandoDiv = document.getElementById('cargando');
            const sinResultadosDiv = document.getElementById('sin-resultados');
            const buscarBtn = document.getElementById('buscar-btn');
            
            // Variables globales
            let todosLosHospedajes = [];
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
            
            // Función para obtener hospedajes desde la API
            async function obtenerHospedajes() {
                cargandoDiv.classList.remove('d-none');
                resultadosDiv.innerHTML = '';
                sinResultadosDiv.classList.add('d-none');
                
                // Usar AbortController para implementar timeout
                const controller = new AbortController();
                const timeoutId = setTimeout(() => controller.abort(), 10000); // 10 segundos de timeout
                
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
                    
                    const response = await fetch('https://wani-connect.onrender.com/api/hospedajes', {
                        signal: controller.signal,
                        method: 'GET',
                        headers: headers
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
                    
                    // Intentar parsear la respuesta como JSON
                    let data;
                    try {
                        data = await response.json();
                    } catch (jsonError) {
                        console.error('Error al parsear JSON:', jsonError);
                        throw new Error('Error al procesar la respuesta del servidor');
                    }
                    
                    // Verificar que los datos sean un array
                    if (!Array.isArray(data)) {
                        console.error('Los datos recibidos no son un array:', data);
                        throw new Error('Formato de datos incorrecto');
                    }
                    
                    // Guardar todos los hospedajes para filtrar después
                    todosLosHospedajes = data;
                    
                    if (data.length === 0) {
                        sinResultadosDiv.classList.remove('d-none');
                    } else {
                        mostrarHospedajes(data);
                    }
                } catch (error) {
                    clearTimeout(timeoutId);
                    console.error('Error al obtener hospedajes:', error);
                    
                    // Mostrar mensaje de no hay hospedajes disponibles en caso de cualquier error
                    sinResultadosDiv.classList.remove('d-none');
                } finally {
                    cargandoDiv.classList.add('d-none');
                }
            }
            
            // Función para mostrar los hospedajes en tarjetas
            function mostrarHospedajes(hospedajes) {
                resultadosDiv.innerHTML = '';
                
                if (hospedajes.length === 0) {
                    sinResultadosDiv.classList.remove('d-none');
                    return;
                }
                
                hospedajes.forEach(hospedaje => {
                    // Verificar que cada hospedaje tenga la estructura esperada
                    if (!hospedaje || !hospedaje.nombre) {
                        console.error('Uno de los hospedajes no tiene la estructura esperada:', hospedaje);
                        return;
                    }
                    
                    const card = document.createElement('div');
                    card.className = 'col-md-6 col-lg-4';
                    
                    // Generar estrellas según la categoría
                    let estrellas = '';
                    const categoria = hospedaje.categoria || 0;
                    for (let i = 1; i <= 5; i++) {
                        if (i <= categoria) {
                            estrellas += '<i class="bi bi-star-fill"></i>';
                        } else {
                            estrellas += '<i class="bi bi-star"></i>';
                        }
                    }
                    
                    // Generar lista de servicios
                    let servicios = '';
                    if (hospedaje.serviciosIncluidos) {
                        const serviciosArray = hospedaje.serviciosIncluidos.split(',');
                        servicios = serviciosArray.slice(0, 3).join(', ');
                        if (serviciosArray.length > 3) {
                            servicios += '...';
                        }
                    }
                    
                    // Obtener la URL de la imagen o usar una imagen por defecto
                    let imagenUrl = '';
                    if (hospedaje.imagenes && hospedaje.imagenes.length > 0 && hospedaje.imagenes[0].url) {
                        imagenUrl = hospedaje.imagenes[0].url;
                    }
                    
                    // Obtener el código único del hospedaje
                    const codigoUnico = hospedaje.codigoUnico || hospedaje.nombre.replace(/\s+/g, '-').toLowerCase();
                    
                    card.innerHTML = `
                        <div class="card">
                            <div class="card-img-container">
                                ${imagenUrl ? 
                                    `<img src="${imagenUrl}" class="card-img-top" alt="${hospedaje.nombre}" 
                                     onerror="this.onerror=null; this.src='https://picsum.photos/seed/${codigoUnico}/400/300.jpg';">` : 
                                    `<div class="bg-secondary text-white text-center d-flex align-items-center justify-content-center" style="height: 100%;">
                                        <i class="bi bi-image" style="font-size: 3rem;"></i>
                                    </div>`
                                }
                                <div class="card-badge">${hospedaje.tipo || 'Sin tipo'}</div>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">${hospedaje.nombre}</h5>
                                <div class="card-location">
                                    <i class="bi bi-geo-alt-fill"></i>
                                    <span>${hospedaje.ubicacion ? hospedaje.ubicacion.direccion : 'Dirección no disponible'}</span>
                                </div>
                                <div class="card-rating">${estrellas}</div>
                                <div class="card-services">
                                    <i class="bi bi-check-circle-fill"></i>
                                    <span>${servicios || 'Servicios no especificados'}</span>
                                </div>
                            </div>
                            <div class="card-footer">
                                <div>
                                    <span class="price-tag">$${(hospedaje.precioPorNoche || 0).toFixed(2)}</span>
                                    <small>/noche</small>
                                </div>
                                <button class="btn btn-outline-primary reserva-btn" data-codigo="${codigoUnico}" data-precio="${hospedaje.precioPorNoche || 0}">
                                    Reserva
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
                        const fechaEntrada = document.getElementById('fecha-entrada').value;
                        const fechaSalida = document.getElementById('fecha-salida').value;
                        const habitaciones = document.getElementById('habitaciones').value;
                        
                        procesarReserva(codigo, precio, tipo, fechaEntrada, fechaSalida, habitaciones);
                    });
                });
            }
            
            // Función para filtrar hospedajes
            function filtrarHospedajes() {
                const tipo = document.getElementById('tipo').value;
                const fechaEntrada = document.getElementById('fecha-entrada').value;
                const fechaSalida = document.getElementById('fecha-salida').value;
                const habitaciones = document.getElementById('habitaciones').value;
                
                let hospedajesFiltrados = [...todosLosHospedajes];
                
                // Filtrar por tipo
                if (tipo) {
                    hospedajesFiltrados = hospedajesFiltrados.filter(h => h.tipo === tipo);
                }
                

                if (habitaciones) {
                
                }
                
                
                if (fechaEntrada && fechaSalida) {
                
                }
                
                mostrarHospedajes(hospedajesFiltrados);
            }
            
            // Función para validar fechas
            function validarFechas(fechaEntrada, fechaSalida) {
                if (!fechaEntrada || !fechaSalida) {
                    return { valido: false, mensaje: 'Por favor, selecciona ambas fechas' };
                }
                
                const fechaInicio = new Date(fechaEntrada);
                const fechaFin = new Date(fechaSalida);
                
                // Restamos un día a la fecha de fin para comparar solo fechas (sin hora)
                fechaFin.setHours(0, 0, 0, 0);
                fechaInicio.setHours(0, 0, 0, 0);
                
                if (fechaFin <= fechaInicio) {
                    return { valido: false, mensaje: 'La fecha de salida debe ser posterior a la fecha de entrada' };
                }
                
                return { valido: true };
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
                const fechaEntrada = document.getElementById('fecha-entrada').value;
                const fechaSalida = document.getElementById('fecha-salida').value;
                
                const validacion = validarFechas(fechaEntrada, fechaSalida);
                if (!validacion.valido) {
                    alert(validacion.mensaje);
                    return;
                }
                
                // Si ya tenemos todos los hospedajes cargados, filtrarlos
                if (todosLosHospedajes.length > 0) {
                    filtrarHospedajes();
                } else {
                    // Si no, obtenerlos y luego filtrar
                    obtenerHospedajes().then(() => {
                        filtrarHospedajes();
                    });
                }
            });
            
            // Función para procesar la reserva
            async function procesarReserva(codigo, precio, tipo, fechaEntrada, fechaSalida, habitaciones) {
                try {
                    // Validar fechas
                    const validacion = validarFechas(fechaEntrada, fechaSalida);
                    if (!validacion.valido) {
                        alert(validacion.mensaje);
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
                    
                    // Validar si el hospedaje existe
                    const controller = new AbortController();
                    const timeoutId = setTimeout(() => controller.abort(), 10000); // 10 segundos de timeout
                    
                    const response = await fetch(`https://wani-connect.onrender.com/api/hospedajes/${codigo}`, {
                        signal: controller.signal,
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json',
                            'Authorization': `Bearer ${token}`
                        }
                    });
                    
                    clearTimeout(timeoutId);
                    
                    if (!response.ok) {
                        throw new Error('El hospedaje no existe');
                    }
                    
                    const hospedaje = await response.json();
                    
                    // Calcular el número de noches
                    let noches = 1;
                    if (fechaEntrada && fechaSalida) {
                        const fechaInicio = new Date(fechaEntrada);
                        const fechaFin = new Date(fechaSalida);
                        const diferenciaTiempo = fechaFin.getTime() - fechaInicio.getTime();
                        noches = Math.ceil(diferenciaTiempo / (1000 * 3600 * 24));
                        // La validación anterior asegura que noches será al menos 1
                    }
                    
                    // Calcular el costo total
                    const costoTotal = precio * noches;
                    
                    // Guardar información de la reserva actual
                    reservaActual = {
                        hospedaje: hospedaje,
                        fechaEntrada: fechaEntrada,
                        fechaSalida: fechaSalida,
                        noches: noches,
                        precioPorNoche: precio,
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
                document.getElementById('reserva-nombre').textContent = reservaActual.hospedaje.nombre;
                document.getElementById('reserva-tipo').textContent = reservaActual.hospedaje.tipo;
                document.getElementById('reserva-ubicacion').textContent = reservaActual.hospedaje.ubicacion.direccion;
                document.getElementById('reserva-fecha-entrada').textContent = formatearFecha(reservaActual.fechaEntrada);
                document.getElementById('reserva-fecha-salida').textContent = formatearFecha(reservaActual.fechaSalida);
                document.getElementById('reserva-noches').textContent = reservaActual.noches;
                document.getElementById('reserva-precio-noche').textContent = reservaActual.precioPorNoche.toFixed(2);
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
                document.getElementById('pago-hospedaje').textContent = reservaActual.hospedaje.nombre;
                document.getElementById('pago-fechas').textContent = `${formatearFecha(reservaActual.fechaEntrada)} - ${formatearFecha(reservaActual.fechaSalida)}`;
                document.getElementById('pago-noches').textContent = reservaActual.noches;
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
                    const token = localStorage.getItem('token');
                    const response = await fetch('https://wani-connect.onrender.com/api/payments/create', {
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
                document.getElementById('confirmacion-codigo').textContent = pagoResponse.reservationCode || `RES-${Date.now()}`;
                document.getElementById('confirmacion-hospedaje').textContent = reservaActual.hospedaje.nombre;
                document.getElementById('confirmacion-fechas').textContent = `${formatearFecha(reservaActual.fechaEntrada)} - ${formatearFecha(reservaActual.fechaSalida)}`;
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
            
            // Verificar si hay una URL de redirección al cargar la página
            const redirectUrl = localStorage.getItem('redirectUrl');
            if (redirectUrl && estaAutenticado()) {
                console.log('Redirigiendo a:', redirectUrl);
                localStorage.removeItem('redirectUrl');
                window.location.href = redirectUrl;
            }
            
            // Cargar hospedajes al iniciar la página
            obtenerHospedajes();
        });