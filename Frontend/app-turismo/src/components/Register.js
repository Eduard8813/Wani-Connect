// src/components/Register.js
import React, { useState } from 'react';

const Register = ({ onLogin, onToggleLogin, onSwitchToCompany }) => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    firstName: '',
    lastName: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const generateUsername = () => {
    const randomNum = Math.floor(Math.random() * 10000);
    setFormData({
      ...formData,
      username: `usuario${randomNum}`
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    const requiredFields = ['username', 'password', 'email', 'firstName', 'lastName'];
    const missingFields = requiredFields.filter(field => !formData[field]);
    
    if (missingFields.length > 0) {
      setError('Todos los campos son obligatorios');
      setLoading(false);
      return;
    }

    try {
      const payload = {
        username: formData.username,
        password: formData.password,
        email: formData.email,
        firstName: formData.firstName,
        lastName: formData.lastName,
        phone: '00000000',
        address: 'Dirección de usuario',
        birthDate: '1990-01-01',
        gender: 'M',
        location: 'Nicaragua',
        countryOfOrigin: 'Nicaragua',
        language: 'Español',
        touristInterest: 'Turismo general',
        social: 'Usuario',
        description: 'Cuenta de usuario'
      };

      const response = await fetch('https://aplicacion-del-hackaton.onrender.com/api/auth/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
      }

      // Iniciar sesión automáticamente después del registro
      const loginResponse = await fetch('http://localhost:8080/api/auth/signin', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ 
          username: formData.username, 
          password: formData.password 
        })
      });

      if (!loginResponse.ok) {
        throw new Error('Registro exitoso, pero error al iniciar sesión');
      }

      const loginData = await loginResponse.json();
      const token = loginData.token;
      
      // Extraer rol del token JWT
      let role = 'USER';
      try {
        const tokenParts = token.split('.');
        const payload = JSON.parse(atob(tokenParts[1]));
        role = payload.role || 'USER';
      } catch (e) {
        console.error('Error al extraer rol del token:', e);
      }

      const userInfo = {
        username: formData.username,
        email: formData.email,
        role: role
      };

      onLogin(token, userInfo, role);
    } catch (error) {
      console.error('Error al registrar:', error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container user-register">
      <div className="login-header">
        <div className="login-logo">
          <i className="fas fa-user-plus"></i>
        </div>
        <h2>Registro de Usuario</h2>
        <p>Crea tu cuenta de usuario para acceder al sistema</p>
      </div>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Usuario:</label>
          <div className="input-with-button">
            <input
              type="text"
              id="username"
              name="username"
              className="form-control"
              placeholder="Nombre de usuario"
              value={formData.username}
              onChange={handleChange}
            />
            <button 
              type="button" 
              className="btn btn-sm btn-info"
              onClick={generateUsername}
            >
              Generar
            </button>
          </div>
        </div>
        
        <div className="form-group">
          <label htmlFor="password">Contraseña:</label>
          <input
            type="password"
            id="password"
            name="password"
            className="form-control"
            placeholder="Contraseña"
            value={formData.password}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            name="email"
            className="form-control"
            placeholder="tu@email.com"
            value={formData.email}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="firstName">Nombre:</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            className="form-control"
            placeholder="Tu nombre"
            value={formData.firstName}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="lastName">Apellido:</label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            className="form-control"
            placeholder="Tu apellido"
            value={formData.lastName}
            onChange={handleChange}
          />
        </div>
        
        <div className="action-buttons">
          <button 
            type="submit" 
            className="btn btn-primary" 
            disabled={loading}
            style={{ width: '100%' }}
          >
            {loading ? 'Registrando...' : 'Registrar Usuario'}
          </button>
        </div>
      </form>
      
      <div className="auth-switch">
        <div className="text-center" style={{ marginTop: '15px' }}>
          <small>
            <a href="#" onClick={onToggleLogin} style={{ color: '#3498db' }}>
              ¿Ya tienes cuenta? Inicia sesión
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

export default Register;