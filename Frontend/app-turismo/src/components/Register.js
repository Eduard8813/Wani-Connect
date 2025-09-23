// src/components/Register.js
import React, { useState } from 'react';

const Register = ({ onLogin, onToggleLogin, onSwitchToCompany }) => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    firstName: '',
    lastName: '',
    phone: '',
    address: '',
    birthDate: '',
    gender: 'M',
    location: '',
    countryOfOrigin: '',
    language: 'Español',
    touristInterest: '',
    social: '',
    description: ''
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

    const requiredFields = ['username', 'password', 'email', 'firstName', 'lastName', 'phone', 'address', 'birthDate', 'location', 'countryOfOrigin'];
    const missingFields = requiredFields.filter(field => !formData[field]);
    
    if (missingFields.length > 0) {
      setError('Todos los campos obligatorios deben ser completados');
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
        phone: formData.phone,
        address: formData.address,
        birthDate: formData.birthDate,
        gender: formData.gender,
        location: formData.location,
        countryOfOrigin: formData.countryOfOrigin,
        language: formData.language,
        touristInterest: formData.touristInterest,
        social: formData.social,
        description: formData.description
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
      const loginResponse = await fetch('https://aplicacion-del-hackaton.onrender.com/api/auth/signin', {
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
          <img src="https://www.shutterstock.com/image-vector/bird-vector-modren-logo-600nw-2457229219.jpg" alt="Wanni Connect" />
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
        
        <div className="form-row">
          <div className="form-group half-width">
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
          
          <div className="form-group half-width">
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
        </div>
        
        <div className="form-group">
          <label htmlFor="phone">Teléfono:</label>
          <input
            type="tel"
            id="phone"
            name="phone"
            className="form-control"
            placeholder="+505 8900 7093"
            value={formData.phone}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="address">Dirección:</label>
          <input
            type="text"
            id="address"
            name="address"
            className="form-control"
            placeholder="Cuidad jardin"
            value={formData.address}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-row">
          <div className="form-group half-width">
            <label htmlFor="birthDate">Fecha de Nacimiento:</label>
            <input
              type="date"
              id="birthDate"
              name="birthDate"
              className="form-control"
              value={formData.birthDate}
              onChange={handleChange}
            />
          </div>
          
          <div className="form-group half-width">
            <label htmlFor="gender">Género:</label>
            <select
              id="gender"
              name="gender"
              className="form-control"
              value={formData.gender}
              onChange={handleChange}
            >
              <option value="M">Masculino (M)</option>
              <option value="F">Femenino (F)</option>
            </select>
          </div>
        </div>
        
        <div className="form-group">
          <label htmlFor="location">Ubicación:</label>
          <input
            type="text"
            id="location"
            name="location"
            className="form-control"
            placeholder="Managua, Nicaragua"
            value={formData.location}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="countryOfOrigin">País de Origen:</label>
          <input
            type="text"
            id="countryOfOrigin"
            name="countryOfOrigin"
            className="form-control"
            placeholder="Nicaragua"
            value={formData.countryOfOrigin}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="language">Idioma:</label>
          <select
            id="language"
            name="language"
            className="form-control"
            value={formData.language}
            onChange={handleChange}
          >
            <option value="Español">Español</option>
            <option value="Inglés">Inglés</option>
            <option value="Francés">Francés</option>
            <option value="Portugués">Portugués</option>
          </select>
        </div>
        
        <div className="form-group">
          <label htmlFor="touristInterest">Intereses Turísticos:</label>
          <input
            type="text"
            id="touristInterest"
            name="touristInterest"
            className="form-control"
            placeholder="Playas, Aventura, Gastronomía"
            value={formData.touristInterest}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="social">Redes Sociales:</label>
          <input
            type="text"
            id="social"
            name="social"
            className="form-control"
            placeholder="@Eduard8813"
            value={formData.social}
            onChange={handleChange}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="description">Descripción:</label>
          <textarea
            id="description"
            name="description"
            className="form-control"
            placeholder="Viajera apasionada que busca descubrir las maravillas de Centroamérica."
            value={formData.description}
            onChange={handleChange}
            rows="3"
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
            <a href="#" onClick={onToggleLogin} style={{ color: '#436cc5' }}>
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