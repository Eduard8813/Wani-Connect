// src/components/Dashboard.js
import React from 'react';

const Dashboard = ({ userInfo, userRole, onLogout }) => {
  return (
    <div className="dashboard-container">
      <div className="user-info">
        <img src="https://picsum.photos/seed/user123/30/30.jpg" alt="Usuario" />
        <span>{userInfo.username}</span>
        <button className="btn btn-danger btn-sm" onClick={onLogout}>Salir</button>
      </div>
      
      <div className="container">
        <header>
          <h1>Sistema de Reservas de Terminales</h1>
          <p className="subtitle">Wanni Connect - Reserva tu lugar en las terminales de buses de Nicaragua</p>
        </header>
        
        <div className="dashboard-content">
          <div className="welcome-card">
            <h2>Bienvenido, {userInfo.username}!</h2>
            <p>Tu tipo de cuenta: {userRole === 'COMPANY' ? 'Empresa' : 'Usuario'}</p>
            <p>Email: {userInfo.email}</p>
          </div>
          
          <div className="features-container">
            <div className="feature-card">
              <i className="fas fa-map-marked-alt"></i>
              <h3>Mapa de Terminales</h3>
              <p>Visualiza todas las terminales disponibles en Nicaragua</p>
              <button className="btn btn-primary">Ver Mapa</button>
            </div>
            
            <div className="feature-card">
              <i className="fas fa-bus"></i>
              <h3>Reservas</h3>
              <p>Reserva tu asiento en el bus de tu elección</p>
              <button className="btn btn-primary">Reservar</button>
            </div>
            
            {userRole === 'COMPANY' && (
              <div className="feature-card">
                <i className="fas fa-user-tie"></i>
                <h3>Panel de Conductor</h3>
                <p>Gestiona los buses y libera asientos</p>
                <button className="btn btn-warning">Acceder</button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;