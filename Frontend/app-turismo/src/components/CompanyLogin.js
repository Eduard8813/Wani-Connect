// src/components/CompanyLogin.js
import React, { useState } from 'react';

const CompanyLogin = ({ onLogin, onToggleRegister, onSwitchToUser }) => {
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
      const response = await fetch('http://https://aplicacion-del-hackaton.onrender.com/api/auth/signin-company', {
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
      let role = 'COMPANY';
      try {
        const tokenParts = token.split('.');
        const payload = JSON.parse(atob(tokenParts[1]));
        role = payload.role || 'COMPANY';
      } catch (e) {
        console.error('Error al extraer rol del token:', e);
      }

      // Obtener información del usuario
      let userInfo = { username, email: `${username}@empresa.com`, role };
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
            email: userData.email || username + '@empresa.com',
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
    <div className="login-container company-login">
      <div className="login-header">
        <div className="login-logo company-logo">
          <i className="fas fa-building"></i>
        </div>
        <h2>Wanni Connect Empresas</h2>
        <p>Inicia sesión como empresa para acceder al sistema</p>
      </div>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Usuario de Empresa:</label>
          <input
            type="text"
            id="username"
            className="form-control"
            placeholder="Nombre de usuario de empresa"
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
            className="btn btn-warning" 
            disabled={loading}
            style={{ width: '100%' }}
          >
            {loading ? 'Iniciando sesión...' : 'Iniciar Sesión como Empresa'}
          </button>
        </div>
      </form>
      
      <div className="auth-switch">
        <div className="text-center" style={{ marginTop: '15px' }}>
          <small>
            <a href="#" onClick={onToggleRegister} style={{ color: '#f39c12' }}>
              ¿No tienes cuenta empresarial? Regístrate aquí
            </a>
          </small>
        </div>
        
        <div className="divider">
          <span>o</span>
        </div>
        
        <div className="text-center">
          <button 
            className="btn btn-outline-primary"
            onClick={onSwitchToUser}
          >
            <i className="fas fa-user"></i> Acceder como Usuario
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

export default CompanyLogin;