// src/components/Register.js
import React, { useState } from 'react';

const Register = ({ onLogin, onToggleLogin, loginType, onToggleLoginType }) => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    firstName: '',
    lastName: '',
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
    const prefix = loginType === 'company' ? 'empresa' : 'usuario';
    setFormData({
      ...formData,
      username: `${prefix}${randomNum}`
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    const requiredFields = loginType === 'company' 
      ? ['username', 'password', 'email', 'companyName']
      : ['username', 'password', 'email', 'firstName', 'lastName'];
    
    const missingFields = requiredFields.filter(field => !formData[field]);
    
    if (missingFields.length > 0) {
      setError('Todos los campos son obligatorios');
      setLoading(false);
      return;
    }

    try {
      const endpoint = loginType === 'company' 
        ? 'http://localhost:8080/api/auth/signup-company'
        : 'http://localhost:8080/api/auth/signup';

      const payload = loginType === 'company'
        ? {
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
          }
        : {
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

      const response = await fetch(endpoint, {
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
      const loginEndpoint = loginType === 'company' 
        ? 'http://localhost:8080/api/auth/signin-company'
        : 'http://localhost:8080/api/auth/signin';

      const loginResponse = await fetch(loginEndpoint, {
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
    <div className="login-container">
      <div className="login-header">
        <div className="login-logo">
          <i className="fas fa-user-plus"></i>
        </div>
        <h2>Registro de {loginType === 'company' ? 'Empresa' : 'Usuario'}</h2>
        <p>Crea tu cuenta para acceder al sistema</p>
      </div>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Tipo de cuenta:</label>
          <div className="account-type-selector">
            <button 
              type="button" 
              className={`btn ${loginType === 'user' ? 'btn-primary' : 'btn-outline-primary'}`}
              onClick={() => onToggleLoginType('user')}
            >
              Usuario
            </button>
            <button 
              type="button" 
              className={`btn ${loginType === 'company' ? 'btn-warning' : 'btn-outline-warning'}`}
              onClick={() => onToggleLoginType('company')}
            >
              Empresa
            </button>
          </div>
        </div>
        
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
        
        {loginType === 'company' ? (
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
        ) : (
          <>
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
          </>
        )}
        
        <div className="action-buttons">
          <button 
            type="submit" 
            className="btn btn-primary" 
            disabled={loading}
            style={{ width: '100%' }}
          >
            {loading ? 'Registrando...' : 'Registrar'}
          </button>
        </div>
      </form>
      
      <div className="text-center" style={{ marginTop: '15px' }}>
        <small>
          <a href="#" onClick={onToggleLogin} style={{ color: '#3498db' }}>
            ¿Ya tienes cuenta? Inicia sesión
          </a>
        </small>
      </div>
      
      {error && <div className="error">{error}</div>}
      
      <div className="login-footer">
        <p>&copy; 2023 Wanni Connect. Todos los derechos reservados.</p>
      </div>
    </div>
  );
};

export default Register;