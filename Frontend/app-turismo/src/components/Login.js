// src/components/Login.js
import React, { useState } from 'react';

const Login = ({ onLogin, onToggleRegister, onSwitchToCompany }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (!username || !password) {
      setError('Por favor, completa todos los campos');
      setLoading(false);
      return;
    }

    try {
      const response = await fetch('https://aplicacion-del-hackaton.onrender.com/api/auth/signin', {
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
      const token = data.token;
      
      // Extraer rol del token JWT
      let role = 'USER';
      try {
        const tokenParts = token.split('.');
        const payload = JSON.parse(atob(tokenParts[1]));
        role = payload.role || 'USER';
      } catch (e) {
        console.error('Error al extraer rol del token:', e);
      }

      // Obtener información del usuario
      let userInfo = { username, email: `${username}@example.com`, role };
      try {
        const userResponse = await fetch('https://aplicacion-del-hackaton.onrender.com/api/user/me', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
        
        if (userResponse.ok) {
          const userData = await userResponse.json();
          userInfo = {
            username: username,
            email: userData.email || username + '@example.com',
            role: role
          };
        }
      } catch (userError) {
        console.error('Error al obtener información del usuario:', userError);
      }

      onLogin(token, userInfo, role);
    } catch (error) {
      console.error('Error al iniciar sesión:', error);
      setError(`Error al iniciar sesión: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container user-login">
      <div className="login-header">
          <div className="login-logo">
        <img src="https://www.shutterstock.com/image-vector/bird-vector-modren-logo-600nw-2457229219.jpg" alt="Wanni Connect" />
      </div>
        <h2>Wanni Connect</h2>
        <p>Inicia sesión como usuario para acceder al sistema</p>
      </div>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Usuario:</label>
          <input
            type="text"
            id="username"
            className="form-control"
            placeholder="Nombre de usuario"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="password">Contraseña:</label>
          <input
            type="password"
            id="password"
            className="form-control"
            placeholder="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        
        <div className="action-buttons">
          <button 
            type="submit" 
            className="btn btn-primary" 
            disabled={loading}
            style={{ width: '100%' }}
          >
            {loading ? 'Iniciando sesión...' : 'Iniciar Sesión'}
          </button>
        </div>
      </form>
      
      <div className="auth-switch">
        <div className="text-center" style={{ marginTop: '15px' }}>
          <small>
            <a href="#" onClick={onToggleRegister} style={{ color: '#3498db' }}>
              ¿No tienes cuenta? Regístrate aquí
            </a>
          </small>
        </div>
        
        <div className="divider">
          <span>o</span>
        </div>
        
        <div className="text-center">
          <button 
            className="btn btn-outline-warning"
            onClick={onSwitchToCompany}
          >
            <i className="fas fa-building"></i> Acceder como Empresa
          </button>
        </div>
      </div>
      
      {error && <div className="error">{error}</div>}
      
      <div className="login-footer">
        <p>&copy; 2023 Wanni Connect. Todos los derechos reservados.</p>
      </div>
    </div>
  );
};

export default Login;