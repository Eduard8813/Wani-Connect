   // Variables globales
        let authToken = localStorage.getItem('authToken') || null;
        let userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
        let userRole = localStorage.getItem('userRole') || 'USER';
        let originalProfileData = {};
        let currentProfileData = {};
        let editModal;

        // API URLs
        const API_BASE_URL = 'https://wani-connect.onrender.com/api';
        const PROFILE_URL = `${API_BASE_URL}/user/profile`;
        const UPLOAD_PHOTO_URL = `${API_BASE_URL}/fotos/subir`;
        const VIEW_PHOTO_URL = `${API_BASE_URL}/fotos/ver`;

        // Mapeo de intereses a iconos
        const interestIcons = {
            'playas': { icon: 'fa-umbrella-beach', tooltip: 'Playas' },
            'aventura': { icon: 'fa-mountain', tooltip: 'Aventura' },
            'gastronomia': { icon: 'fa-utensils', tooltip: 'Gastronomía' },
            'cultura': { icon: 'fa-landmark', tooltip: 'Cultura' },
            'naturaleza': { icon: 'fa-tree', tooltip: 'Naturaleza' },
            'deportes': { icon: 'fa-football-ball', tooltip: 'Deportes' },
            'fotografía': { icon: 'fa-camera', tooltip: 'Fotografía' },
            'música': { icon: 'fa-music', tooltip: 'Música' },
            'arte': { icon: 'fa-palette', tooltip: 'Arte' },
            'historia': { icon: 'fa-book', tooltip: 'Historia' },
            'compras': { icon: 'fa-shopping-bag', tooltip: 'Compras' },
            'relajación': { icon: 'fa-spa', tooltip: 'Relajación' },
            'fiesta': { icon: 'fa-cocktail', tooltip: 'Fiesta' },
            'familia': { icon: 'fa-users', tooltip: 'Familia' },
            'romance': { icon: 'fa-heart', tooltip: 'Romance' },
            'negocios': { icon: 'fa-briefcase', tooltip: 'Negocios' }
        };

        // Función para verificar si el token es válido
        function isTokenValid(token) {
            if (!token) return false;
            
            try {
                // Un token JWT tiene tres partes separadas por puntos
                const parts = token.split('.');
                if (parts.length !== 3) return false;
                
                // Decodificar la parte del payload (segunda parte)
                const payload = JSON.parse(atob(parts[1]));
                
                // Verificar si el token ha expirado
                const currentTime = Math.floor(Date.now() / 1000);
                if (payload.exp && payload.exp < currentTime) {
                    return false;
                }
                
                return true;
            } catch (error) {
                console.error('Error al validar token:', error);
                return false;
            }
        }

        // Inicializar al cargar la página
        window.onload = function() {
            // Verificar si hay token y si es válido
            if (!authToken || !isTokenValid(authToken)) {
                logout();
                return;
            }
            
            editModal = new bootstrap.Modal(document.getElementById('editProfileModal'));
            updateProfileStatus();
            loadProfileData();
            setupEventListeners();
        };

        function setupEventListeners() {
            document.getElementById('imageUpload').addEventListener('change', function(event) {
                if (event.target.files && event.target.files[0]) {
                    uploadProfilePicture(event.target.files[0]);
                }
            });
        }

        function updateProfileStatus() {
            const statusElement = document.getElementById('profileStatus');
            if (authToken) {
                statusElement.textContent = 'Activo';
                statusElement.className = 'profile-status active';
            } else {
                statusElement.textContent = 'Inactivo';
                statusElement.className = 'profile-status inactive';
            }
        }

        function openEditModal() {
            populateModalFields();
            editModal.show();
        }

        function populateModalFields() {
            document.getElementById('modalFirstName').value = currentProfileData.firstName || '';
            document.getElementById('modalLastName').value = currentProfileData.lastName || '';
            document.getElementById('modalEmail').value = currentProfileData.email || '';
            document.getElementById('modalPhone').value = currentProfileData.phone || '';
            document.getElementById('modalBirthDate').value = currentProfileData.birthDate || '';
            document.getElementById('modalGender').value = currentProfileData.gender || '';
            document.getElementById('modalLocation').value = currentProfileData.location || '';
            document.getElementById('modalCountry').value = currentProfileData.countryOfOrigin || '';
            document.getElementById('modalLanguage').value = currentProfileData.language || '';
            document.getElementById('modalInterests').value = currentProfileData.touristInterest || '';
            document.getElementById('modalSocial').value = currentProfileData.social || '';
            document.getElementById('modalDescription').value = currentProfileData.description || '';
        }

        async function saveModalData() {
            showLoading(true, 'Guardando datos...', 'Actualizando tu información');
            try {
                const profileData = {
                    firstName: document.getElementById('modalFirstName').value,
                    lastName: document.getElementById('modalLastName').value,
                    email: document.getElementById('modalEmail').value,
                    phone: document.getElementById('modalPhone').value,
                    birthDate: document.getElementById('modalBirthDate').value,
                    gender: document.getElementById('modalGender').value,
                    location: document.getElementById('modalLocation').value,
                    countryOfOrigin: document.getElementById('modalCountry').value,
                    language: document.getElementById('modalLanguage').value,
                    touristInterest: document.getElementById('modalInterests').value,
                    social: document.getElementById('modalSocial').value,
                    description: document.getElementById('modalDescription').value
                };
                const response = await fetch(PROFILE_URL, {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${authToken}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(profileData)
                });
                if (!response.ok) {
                    if (response.status === 401) {
                        logout();
                        return;
                    }
                    throw new Error(`Error ${response.status}: ${response.statusText}`);
                }
                const updatedData = await response.json();
                originalProfileData = {...updatedData};
                currentProfileData = {...updatedData};
                updateProfileUI(updatedData);
                updateInfoCard(updatedData);
                updateProfileInterests(updatedData);
                userInfo.email = updatedData.email;
                userInfo.firstName = updatedData.firstName;
                userInfo.lastName = updatedData.lastName;
                localStorage.setItem('userInfo', JSON.stringify(userInfo));
                editModal.hide();
                showNotification('Datos actualizados correctamente', 'success');
            } catch (error) {
                console.error('Error al guardar datos:', error);
                showNotification('Error al guardar tus datos: ' + error.message, 'error');
            } finally {
                showLoading(false);
            }
        }

        async function loadProfileData() {
            showLoading(true, 'Cargando perfil...', 'Obteniendo tus datos');
            try {
                const response = await fetch(PROFILE_URL, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${authToken}`,
                        'Content-Type': 'application/json'
                    }
                });
                if (!response.ok) {
                    if (response.status === 401) {
                        // Token inválido o expirado
                        showNotification('Tu sesión ha expirado. Por favor, inicia sesión nuevamente.', 'error');
                        logout();
                        return;
                    }
                    throw new Error(`Error ${response.status}: ${response.statusText}`);
                }
                const profileData = await response.json();
                originalProfileData = {...profileData};
                currentProfileData = {...profileData};
                updateProfileUI(profileData);
                updateInfoCard(profileData);
                updateProfileInterests(profileData);
                loadProfilePicture();
            } catch (error) {
                console.error('Error al cargar perfil:', error);
                showNotification('Error al cargar tu perfil: ' + error.message, 'error');
            } finally {
                showLoading(false);
            }
        }

        function updateProfileUI(data) {
            document.getElementById('profileName').textContent = `${data.firstName || ''} ${data.lastName || ''}`.trim() || data.username;
            document.getElementById('profileEmail').textContent = data.email || 'No disponible';
        }

        function updateProfileInterests(data) {
            const interestsContainer = document.getElementById('profileInterests');
            interestsContainer.innerHTML = '';
            const userInterests = data.touristInterest || '';
            if (userInterests) {
                const interestsArray = userInterests.toLowerCase().split(',').map(interest => interest.trim());
                interestsArray.forEach(interest => {
                    const matchedIcon = Object.keys(interestIcons).find(key => interest.includes(key));
                    if (matchedIcon) {
                        const iconInfo = interestIcons[matchedIcon];
                        const iconElement = document.createElement('div');
                        iconElement.className = 'interest-icon';
                        iconElement.innerHTML = `<i class="fas ${iconInfo.icon}"></i>`;
                        iconElement.setAttribute('data-tooltip', iconInfo.tooltip);
                        interestsContainer.appendChild(iconElement);
                    }
                });
            }
            if (interestsContainer.children.length === 0) {
                const defaultInterests = ['fa-compass', 'fa-map-marked-alt', 'fa-globe-americas'];
                defaultInterests.forEach(iconClass => {
                    const iconElement = document.createElement('div');
                    iconElement.className = 'interest-icon';
                    iconElement.innerHTML = `<i class="fas ${iconClass}"></i>`;
                    iconElement.setAttribute('data-tooltip', 'Explorador');
                    interestsContainer.appendChild(iconElement);
                });
            }
        }

        function updateInfoCard(data) {
            let formattedBirthDate = data.birthDate || 'No especificada';
            if (data.birthDate) {
                const birthDate = new Date(data.birthDate);
                formattedBirthDate = birthDate.toLocaleDateString('es-ES', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric'
                });
            }
            let formattedGender = data.gender || 'No especificado';
            if (data.gender === 'M') formattedGender = 'Masculino';
            else if (data.gender === 'F') formattedGender = 'Femenino';
            else if (data.gender === 'Otro') formattedGender = 'Otro';

            document.getElementById('infoName').textContent = `${data.firstName || ''} ${data.lastName || ''}`.trim() || 'No disponible';
            document.getElementById('infoEmail').textContent = data.email || 'No disponible';
            document.getElementById('infoPhone').textContent = data.phone || 'No disponible';
            document.getElementById('infoBirthDate').textContent = formattedBirthDate;
            document.getElementById('infoGender').textContent = formattedGender;
            document.getElementById('infoLocation').textContent = data.location || 'No disponible';
            document.getElementById('infoCountry').textContent = data.countryOfOrigin || 'No disponible';
            document.getElementById('infoLanguage').textContent = data.language || 'No disponible';
            document.getElementById('infoInterestsText').textContent = data.touristInterest || 'No especificados';
            document.getElementById('infoSocial').textContent = data.social || 'No disponible';
            document.getElementById('infoDescription').textContent = data.description || 'No disponible';
        }

        async function loadProfilePicture() {
            try {
                const response = await fetch(VIEW_PHOTO_URL, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${authToken}`
                    }
                });
                if (response.ok) {
                    const blob = await response.blob();
                    const imageUrl = URL.createObjectURL(blob);
                    document.getElementById('profileImage').src = imageUrl;
                }
            } catch (error) {
                console.error('Error al cargar imagen de perfil:', error);
            }
        }

        async function uploadProfilePicture(file) {
            showLoading(true, 'Subiendo imagen...', 'Actualizando tu foto de perfil');
            try {
                const formData = new FormData();
                formData.append('file', file);
                const response = await fetch(UPLOAD_PHOTO_URL, {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${authToken}`
                    },
                    body: formData
                });
                if (!response.ok) {
                    if (response.status === 401) {
                        logout();
                        return;
                    }
                    throw new Error(`Error ${response.status}: ${response.statusText}`);
                }
                loadProfilePicture();
                showNotification('Foto de perfil actualizada correctamente', 'success');
                document.getElementById('imageUpload').value = '';
            } catch (error) {
                console.error('Error al subir imagen:', error);
                showNotification('Error al subir tu foto: ' + error.message, 'error');
            } finally {
                showLoading(false);
            }
        }

        function showLoading(show, title = 'Cargando...', text = 'Por favor espera') {
            const loadingOverlay = document.getElementById('loadingOverlay');
            const loadingTitle = document.getElementById('loadingTitle');
            const loadingText = document.getElementById('loadingText');
            if (show) {
                loadingTitle.textContent = title;
                loadingText.textContent = text;
                loadingOverlay.style.display = 'flex';
            } else {
                loadingOverlay.style.display = 'none';
            }
        }

        function showNotification(message, type = 'info') {
            const notification = document.getElementById('notification');
            let icon = '';
            if (type === 'success') icon = '<i class="fas fa-check-circle"></i>';
            else if (type === 'error') icon = '<i class="fas fa-exclamation-circle"></i>';
            else icon = '<i class="fas fa-info-circle"></i>';
            notification.innerHTML = `${icon} ${message}`;
            notification.className = `notification ${type}`;
            notification.classList.add('show');
            setTimeout(() => {
                notification.classList.remove('show');
            }, 3000);
        }

        // Función de cierre de sesión (destruye token y redirige)
        function logout() {
            // Eliminar datos del localStorage
            localStorage.removeItem('authToken');
            localStorage.removeItem('userInfo');
            localStorage.removeItem('userRole');

            // Limpiar variables
            authToken = null;
            userInfo = {};
            userRole = 'USER';

            // Mostrar notificación
            showNotification('Sesión cerrada correctamente', 'success');

            // Redirigir después de un breve retraso
            setTimeout(() => {
                window.location.href = 'iniciosesion.html';
            }, 1500);
        }