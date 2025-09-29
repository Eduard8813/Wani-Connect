 document.addEventListener('DOMContentLoaded', function() {
            // Funcionalidad del chat
            const chatToggle = document.getElementById('chatToggle');
            const chatContainer = document.getElementById('chatContainer');
            const chatClose = document.getElementById('chatClose');
            
            chatToggle.addEventListener('click', function() {
                chatContainer.style.display = chatContainer.style.display === 'flex' ? 'none' : 'flex';
            });
            
            chatClose.addEventListener('click', function() {
                chatContainer.style.display = 'none';
            });
            
            // Funcionalidad de notificaciones
            const notificationsBtn = document.getElementById('notificationsBtn');
            const notificationsContainer = document.getElementById('notificationsContainer');
            
            notificationsBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                notificationsContainer.style.display = notificationsContainer.style.display === 'block' ? 'none' : 'block';
            });
            
            // Cerrar notificaciones al hacer clic fuera
            document.addEventListener('click', function(e) {
                if (!notificationsContainer.contains(e.target) && e.target !== notificationsBtn) {
                    notificationsContainer.style.display = 'none';
                }
            });
            
            // Funcionalidad de crear publicación
            const createPostInput = document.getElementById('createPostInput');
            const createPostBtn = document.getElementById('createPostBtn');
            const createPostModal = document.getElementById('createPostModal');
            const modalClose = document.getElementById('modalClose');
            const modalCancel = document.getElementById('modalCancel');
            const modalPost = document.getElementById('modalPost');
            
            createPostInput.addEventListener('focus', function() {
                createPostModal.classList.add('active');
            });
            
            createPostBtn.addEventListener('click', function() {
                createPostModal.classList.add('active');
            });
            
            modalClose.addEventListener('click', function() {
                createPostModal.classList.remove('active');
            });
            
            modalCancel.addEventListener('click', function() {
                createPostModal.classList.remove('active');
            });
            
            modalPost.addEventListener('click', function() {
                // Aquí iría la lógica para publicar
                alert('Publicación creada con éxito');
                createPostModal.classList.remove('active');
            });
            
            // Funcionalidad de likes
            const likeButtons = document.querySelectorAll('.post-action');
            likeButtons.forEach(button => {
                if (button.querySelector('.fa-heart')) {
                    button.addEventListener('click', function() {
                        this.classList.toggle('liked');
                        const icon = this.querySelector('i');
                        if (this.classList.contains('liked')) {
                            icon.classList.remove('far');
                            icon.classList.add('fas');
                        } else {
                            icon.classList.remove('fas');
                            icon.classList.add('far');
                        }
                    });
                }
            });
            
            // Funcionalidad de comentarios
            const commentInputs = document.querySelectorAll('.add-comment-input');
            commentInputs.forEach(input => {
                input.addEventListener('keypress', function(e) {
                    if (e.key === 'Enter' && this.value.trim() !== '') {
                        // Aquí iría la lógica para agregar el comentario
                        alert('Comentario agregado: ' + this.value);
                        this.value = '';
                    }
                });
            });
            
            // Funcionalidad de marcar notificaciones como leídas
            const markReadBtn = document.querySelector('.notifications-mark-read');
            markReadBtn.addEventListener('click', function() {
                const notifications = document.querySelectorAll('.notification');
                notifications.forEach(notification => {
                    notification.classList.remove('unread');
                });
            });
        });