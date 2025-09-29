// Variables globales
        let loginType = 'user'; // 'user' o 'company'
        let authToken = localStorage.getItem('authToken') || null;
        let userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
        let userRole = localStorage.getItem('userRole') || 'USER';
        
        // Inicializar al cargar la página
        window.onload = function() {
            // Verificar si hay token, si no, mostrar login
            if (authToken) {
                // Redirigir a inicio.html si hay token
                window.location.href = 'inicio.html';
            } else {
                // Mostrar formulario de login
                document.getElementById('loginForm').addEventListener('submit', function(event) {
                    event.preventDefault();
                    login();
                });
            }
        };
        
        // Función para seleccionar tipo de login
        function selectLoginType(type) {
            loginType = type;
            const userBtn = document.getElementById('userTypeBtn');
            const companyBtn = document.getElementById('companyTypeBtn');
            
            if (type === 'user') {
                userBtn.classList.add('active');
                companyBtn.classList.remove('active');
            } else {
                userBtn.classList.remove('active');
                companyBtn.classList.add('active');
            }
        }
        
        // Función para mostrar modal de registro
        function mostrarRegistro() {
            const modal = new bootstrap.Modal(document.getElementById('registroModal'));
            
            // Actualizar título según tipo de login
            const modalTitle = document.getElementById('registroModalTitle');
            if (loginType === 'company') {
                modalTitle.textContent = 'Registro de Empresa';
            } else {
                modalTitle.textContent = 'Registro de Usuario';
            }
            
            modal.show();
        }
        
        // Función para generar usuario aleatorio
        function generarUsuario() {
            const randomNum = Math.floor(Math.random() * 10000);
            const prefix = loginType === 'company' ? 'empresa' : 'usuario';
            document.getElementById('regUsername').value = `${prefix}${randomNum}`;
        }
        
        // Función para registrar usuario
        async function registrarUsuario() {
            const username = document.getElementById('regUsername').value.trim();
            const password = document.getElementById('regPassword').value.trim();
            const email = document.getElementById('regEmail').value.trim();
            const nombre = document.getElementById('regNombre').value.trim();
            const errorDiv = document.getElementById('registroError');
            
            if (!username || !password || !email || !nombre) {
                errorDiv.textContent = 'Todos los campos son obligatorios';
                errorDiv.style.display = 'block';
                return;
            }
            
            // Mostrar pantalla de carga
            document.getElementById('loadingOverlay').style.display = 'flex';
            
            try {
                // Determinar endpoint según tipo de registro
                const endpoint = loginType === 'company' ? 
                    'http://localhost:8080/api/auth/signup-company' : 
                    'http://localhost:8080/api/auth/signup';
                
                const response = await fetch(endpoint, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: username,
                        password: password,
                        email: email,
                        firstName: nombre,
                        lastName: loginType === 'company' ? 'Empresa' : 'Usuario',
                        phone: '00000000',
                        address: 'Dirección de ' + (loginType === 'company' ? 'empresa' : 'usuario'),
                        birthDate: '1990-01-01',
                        gender: 'M',
                        location: 'Nicaragua',
                        countryOfOrigin: 'Nicaragua',
                        language: 'Español',
                        touristInterest: 'Turismo general',
                        social: loginType === 'company' ? 'Empresa' : 'Usuario',
                        description: 'Cuenta de ' + (loginType === 'company' ? 'empresa' : 'usuario')
                    })
                });
                
                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(errorText);
                }
                
                showNotification('Registro exitoso. Ya puedes iniciar sesión.', 'success');
                
                // Cerrar modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('registroModal'));
                modal.hide();
                
                // Limpiar formulario
                document.getElementById('registroForm').reset();
                
            } catch (error) {
                console.error('Error al registrar:', error);
                errorDiv.textContent = error.message;
                errorDiv.style.display = 'block';
            } finally {
                document.getElementById('loadingOverlay').style.display = 'none';
            }
        }
        
        // Función para iniciar sesión
        async function login() {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const loginError = document.getElementById('loginError');
            
            if (!username || !password) {
                loginError.textContent = 'Por favor, completa todos los campos';
                loginError.style.display = 'block';
                return;
            }
            
            // Mostrar pantalla de carga
            document.getElementById('loadingOverlay').style.display = 'flex';
            loginError.style.display = 'none';
            
            // Determinar endpoint según tipo de login
            const endpoint = loginType === 'company' ? 
                'http://localhost:8080/api/auth/signin-company' : 
                'http://localhost:8080/api/auth/signin';
            
            try {
                const response = await fetch(endpoint, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ username, password })
                });
                
                if (!response.ok) {
                    let errorMessage = `Error ${response.status}: ${response.statusText}`;
                    try {
                        const errorData = await response.json();
                        errorMessage = errorData.message || errorMessage;
                    } catch (e) {
                        // Si no se puede parsear como JSON, usar el mensaje por defecto
                    }
                    throw new Error(errorMessage);
                }
                
                const data = await response.json();
                authToken = data.token;
                
                // Extraer rol del token JWT
                try {
                    const tokenParts = data.token.split('.');
                    const payload = JSON.parse(atob(tokenParts[1]));
                    userRole = payload.role || 'USER';
                    localStorage.setItem('userRole', userRole);
                } catch (e) {
                    console.error('Error al extraer rol del token:', e);
                    userRole = 'USER';
                    localStorage.setItem('userRole', userRole);
                }
                
                // Obtener información del usuario
                try {
                    const userResponse = await fetch('http://localhost:8080/api/user/me', {
                        headers: {
                            'Authorization': `Bearer ${data.token}`,
                            'Content-Type': 'application/json'
                        }
                    });
                    
                    if (userResponse.ok) {
                        const userData = await userResponse.json();
                        userInfo = {
                            username: username,
                            email: userData.email || username + '@example.com',
                            role: userRole
                        };
                    } else {
                        userInfo = {
                            username: username,
                            email: username + '@example.com',
                            role: userRole
                        };
                    }
                } catch (userError) {
                    console.error('Error al obtener información del usuario:', userError);
                    userInfo = {
                        username: username,
                        email: username + '@example.com',
                        role: userRole
                    };
                }
                
                // Guardar token y userInfo en localStorage con las claves correctas
                localStorage.setItem('authToken', authToken);  // Cambiado de 'authToken' a 'token'
                localStorage.setItem('userInfo', JSON.stringify(userInfo));
                localStorage.setItem('userEmail', userInfo.email);  // Guardar el email directamente
                localStorage.setItem('userRole', userRole);
                
                console.log('Token guardado en localStorage:', localStorage.getItem('authToken'));
                console.log('Email guardado en localStorage:', localStorage.getItem('userEmail'));
                
                // Redirigir a la página de hospedaje
                window.location.href = 'inicio.html';
                
            } catch (error) {
                console.error('Error al iniciar sesión:', error);
                loginError.textContent = `Error al iniciar sesión: ${error.message}`;
                loginError.style.display = 'block';
            } finally {
                document.getElementById('loadingOverlay').style.display = 'none';
            }
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