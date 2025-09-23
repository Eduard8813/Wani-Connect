// src/components/CompanyRegister.js
import React, { useState } from 'react';

const CompanyRegister = ({ onLogin, onToggleLogin, onSwitchToUser }) => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    companyName: ''
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
      username: `empresa${randomNum}`
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    const requiredFields = ['username', 'password', 'email', 'companyName'];
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
        firstName: formData.companyName,
        lastName: 'Empresa',
        phone: '00000000',
        address: 'Dirección empresarial',
        birthDate: '2000-01-01',
        gender: 'M',
        location: 'Nicaragua',
        countryOfOrigin: 'Nicaragua',
        language: 'Español',
        touristInterest: 'Turismo empresarial',
        social: 'Empresa',
        description: 'Cuenta empresarial'
      };

      const response = await fetch('https://aplicacion-del-hackaton.onrender.com/api/auth/signup-company', {
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
      const loginResponse = await fetch('https://aplicacion-del-hackaton.onrender.com/api/auth/signin-company', {
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
      let role = 'COMPANY';
      try {
        const tokenParts = token.split('.');
        const payload = JSON.parse(atob(tokenParts[1]));
        role = payload.role || 'COMPANY';
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
      console.error('Error al registrar empresa:', error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container company-register">
      <div className="login-header">
       <div className="login-logo">
          <img src="https://www.shutterstock.com/image-vector/bird-vector-modren-logo-600nw-2457229219.jpg" alt="Wanni Connect" />
        </div>
        <h2>Registro de Empresa</h2>
        <p>Crea tu cuenta empresarial para acceder al sistema</p>
      </div>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Usuario de Empresa:</label>
          <div className="input-with-button">
            <input
              type="text"
              id="username"
              name="username"
              className="form-control"
              placeholder="Nombre de usuario de empresa"
              value={formData.username}
              onChange={handleChange}
            />
            <button 
              type="button" 
              className="btn btn-sm btn-warning"
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
          <label htmlFor="email">Email Empresarial:</label>
          <input
            type="email"
            id="email"
            name="email"
            className="form-control"
            placeholder="email@empresa.com"
            value={formData.email}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="companyName">Nombre de la Empresa:</label>
          <input
            type="text"
            id="companyName"
            name="companyName"
            className="form-control"
            placeholder="Nombre de la empresa"
            value={formData.companyName}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-group">
          <label>Información Adicional:</label>
          <div className="info-box">
            <p>Al registrarte como empresa, obtendrás acceso a:</p>
            <ul>
              <li>Panel de conductor para gestionar buses</li>
              <li>Liberación de asientos cuando el bus sale</li>
              <li>Validación de códigos de reserva</li>
              <li>Acceso a reportes y estadísticas</li>
            </ul>
          </div>
        </div>
        
        <div className="action-buttons">
          <button 
            type="submit" 
            className="btn btn-warning" 
            disabled={loading}
            style={{ width: '100%' }}
          >
            {loading ? 'Registrando Empresa...' : 'Registrar Empresa'}
          </button>
        </div>
      </form>
      
      <div className="auth-switch">
        <div className="text-center" style={{ marginTop: '15px' }}>
          <small>
            <a href="#" onClick={onToggleLogin} style={{ color: '#f39c12' }}>
              ¿Ya tienes cuenta empresarial? Inicia sesión
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

export default CompanyRegister;