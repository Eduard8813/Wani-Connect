    // Array con los videos locales, sus títulos y tiempos de visualización (en milisegundos)
        const videos = [
        {
            src: '../static/Videos/managua.mp4',
            titulo: 'Nicaragua',
            duracion: 8000 // 8 segundos
        },
        {
            src: '../static/Videos/nicaragua.mp4',
            titulo: 'Nicaragua',
            duracion: 12000 // 12 segundos
        },
        {
            src: '../static/Videos/Granada.mp4',
            titulo: 'Granada',
            duracion: 10000 // 10 segundos
        },
        {
            src: '../static/Videos/managua1.mp4',
            titulo: 'Managua',
            duracion: 15000 // 15 segundos
        }
        ];
        
        let indiceActual = 0;
        const videoElement = document.getElementById('bgVideo');
        const videoContainer = document.getElementById('videoContainer');
        const tituloElement = document.getElementById('videoTitulo');
        const loadingMessage = document.getElementById('loadingMessage');
        const transitionOverlay = document.getElementById('transitionOverlay');
        let timeoutId;
        let isTransitioning = false;
        
        // Función para cambiar al siguiente video con transición de deformación
        function cambiarVideo() {
        // Evitar múltiples transiciones simultáneas
        if (isTransitioning) return;
        isTransitioning = true;
        
        // Limpiar cualquier timeout existente
        if (timeoutId) {
            clearTimeout(timeoutId);
        }
        
        // Iniciar la deformación (zoom out)
        videoContainer.classList.add('zoom-out');
        
        // Iniciar el fundido a negro
        transitionOverlay.classList.add('active');
        
        // Desvanecer el título actual
        tituloElement.style.opacity = '0';
        
        // Esperar a que la deformación y el fundido se completen (tiempo reducido)
        setTimeout(() => {
            // Incrementar el índice y volver al principio si llega al final
            indiceActual = (indiceActual + 1) % videos.length;
            
            // Obtener el video y título actual
            const videoActual = videos[indiceActual];
            
            // Actualizar el título
            tituloElement.textContent = videoActual.titulo;
            
            // Actualizar la fuente del video
            videoElement.src = videoActual.src;
            
            // Cargar el video
            videoElement.load();
            
            // Intentar reproducir el video
            const playPromise = videoElement.play();
            
            if (playPromise !== undefined) {
            playPromise.then(() => {
                // Reproducción exitosa
                loadingMessage.style.display = 'none';
                
                // Finalizar el fundido desde negro (tiempo reducido)
                setTimeout(() => {
                transitionOverlay.classList.remove('active');
                
                // Aplicar la deformación inversa (zoom in)
                videoContainer.classList.remove('zoom-out');
                videoContainer.classList.add('zoom-in');
                
                // Mostrar el nuevo título
                tituloElement.style.opacity = '1';
                
                // Establecer un timeout para cambiar al siguiente video después de la duración especificada
                timeoutId = setTimeout(cambiarVideo, videoActual.duracion);
                
                // Permitir nuevas transiciones después de un breve retraso
                setTimeout(() => {
                    isTransitioning = false;
                }, 600); // Tiempo reducido
                }, 200); // Tiempo reducido
            }).catch(error => {
                // Error en la reproducción
                console.error("Error al reproducir el video:", error);
                loadingMessage.style.display = 'none';
                transitionOverlay.classList.remove('active');
                videoContainer.classList.remove('zoom-out');
                videoContainer.classList.add('zoom-in');
                tituloElement.style.opacity = '1';
                
                // Establecer un timeout para cambiar al siguiente video incluso si hay un error
                timeoutId = setTimeout(cambiarVideo, videoActual.duracion);
                
                // Permitir nuevas transiciones después de un breve retraso
                setTimeout(() => {
                isTransitioning = false;
                }, 600);
            });
            }
        }, 400); // Tiempo reducido de 800ms a 400ms
        }
        
        // Evento que se activa cuando el video actual termina (como respaldo)
        videoElement.addEventListener('ended', () => {
        // Solo cambiar si no hay un timeout activo y no estamos en transición
        if (!timeoutId && !isTransitioning) {
            cambiarVideo();
        }
        });
        
        // Iniciar la reproducción cuando la página se carga
        window.addEventListener('load', () => {
        // Intentar reproducir el video inicial
        const playPromise = videoElement.play();
        
        if (playPromise !== undefined) {
            playPromise.then(() => {
            // Reproducción exitosa
            loadingMessage.style.display = 'none';
            
            // Establecer un timeout para cambiar al siguiente video después de la duración especificada
            timeoutId = setTimeout(cambiarVideo, videos[0].duracion);
            }).catch(error => {
            // Error en la reproducción
            console.error("Error al reproducir el video inicial:", error);
            loadingMessage.style.display = 'none';
            
            // Establecer un timeout para cambiar al siguiente video incluso si hay un error
            timeoutId = setTimeout(cambiarVideo, videos[0].duracion);
            });
        }
        });
        
        // Agregar evento de error para el video
        videoElement.addEventListener('error', () => {
        loadingMessage.style.display = 'none';
        console.error("Error al cargar el video. Verifica la ruta del archivo.");
        
        // Establecer un timeout para cambiar al siguiente video incluso si hay un error
        if (!timeoutId && !isTransitioning) {
            timeoutId = setTimeout(cambiarVideo, videos[0].duracion);
        }
        });