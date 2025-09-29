 // Funcionalidad de scroll horizontal
        const updatesRow = document.getElementById('updatesRow');
        const scrollDots = document.querySelectorAll('.scroll-dot');
        
        scrollDots.forEach(dot => {
            dot.addEventListener('click', function() {
                const slideIndex = this.getAttribute('data-slide');
                const cardWidth = document.querySelector('.update-card').offsetWidth;
                const gap = 30; // Espacio entre tarjetas
                
                updatesRow.scrollTo({
                    left: slideIndex * (cardWidth + gap),
                    behavior: 'smooth'
                });
                
                // Actualizar indicador activo
                scrollDots.forEach(d => d.classList.remove('active'));
                this.classList.add('active');
            });
        });
        
        // Actualizar indicador al hacer scroll
        updatesRow.addEventListener('scroll', function() {
            const cardWidth = document.querySelector('.update-card').offsetWidth;
            const gap = 30;
            const scrollPosition = this.scrollLeft;
            const activeIndex = Math.round(scrollPosition / (cardWidth + gap));
            
            scrollDots.forEach((dot, index) => {
                if (index === activeIndex) {
                    dot.classList.add('active');
                } else {
                    dot.classList.remove('active');
                }
            });
        });