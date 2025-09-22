// src/components/ValidationSuccess.js
import React from 'react';

const ValidationSuccess = ({ reservationDetails, onBack, onLogout }) => {
  return (
    <div className="validation-success-container">
      <div className="user-info">
        <img src="https://picsum.photos/seed/user123/30/30.jpg" alt="Usuario" />
        <span>{reservationDetails.nombreUsuario}</span>
        <button className="btn btn-danger btn-sm" onClick={onLogout}>Salir</button>
      </div>
      
      <div className="container">
        <header>
          <h1>Validación de Reserva Exitosa</h1>
          <p className="subtitle">Wanni Connect - Detalles de la reserva validada</p>
        </header>
        
        <div className="validation-success-content">
          <div className="success-card">
            <div className="success-icon">
              <i className="fas fa-check-circle"></i>
            </div>
            <h2>¡Reserva Validada Correctamente!</h2>
            <p>La reserva ha sido verificada y está confirmada</p>
          </div>
          
          <div className="reservation-details-card">
            <h3>Detalles de la Reserva</h3>
            
            <div className="detail-section">
              <h4>Información de la Reserva</h4>
              <div className="detail-grid">
                <div className="detail-item">
                  <span className="detail-label">Código de Reserva:</span>
                  <span className="detail-value">{reservationDetails.codigoUnico}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Fecha de Reserva:</span>
                  <span className="detail-value">{new Date(reservationDetails.fechaReserva).toLocaleString()}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Estado:</span>
                  <span className="detail-value status-active">Activa</span>
                </div>
              </div>
            </div>
            
            <div className="detail-section">
              <h4>Información del Bus</h4>
              <div className="detail-grid">
                <div className="detail-item">
                  <span className="detail-label">Número de Bus:</span>
                  <span className="detail-value">{reservationDetails.busNumero}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Destino:</span>
                  <span className="detail-value">{reservationDetails.destino}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Hora de Salida:</span>
                  <span className="detail-value">{reservationDetails.horaSalida}</span>
                </div>
              </div>
            </div>
            
            <div className="detail-section">
              <h4>Información de la Terminal</h4>
              <div className="detail-grid">
                <div className="detail-item">
                  <span className="detail-label">Terminal:</span>
                  <span className="detail-value">{reservationDetails.terminalNombre}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Localidad:</span>
                  <span className="detail-value">{reservationDetails.terminalLocalidad}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Código Terminal:</span>
                  <span className="detail-value">{reservationDetails.terminalCodigo}</span>
                </div>
              </div>
            </div>
            
            <div className="detail-section">
              <h4>Información del Pasajero</h4>
              <div className="detail-grid">
                <div className="detail-item">
                  <span className="detail-label">Nombre:</span>
                  <span className="detail-value">{reservationDetails.nombreUsuario}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Email:</span>
                  <span className="detail-value">{reservationDetails.emailUsuario}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Asientos:</span>
                  <span className="detail-value">{reservationDetails.numerosLugar.join(', ')}</span>
                </div>
              </div>
            </div>
            
            <div className="detail-section">
              <h4>Información de Pago</h4>
              <div className="detail-grid">
                <div className="detail-item">
                  <span className="detail-label">Precio por Asiento:</span>
                  <span className="detail-value">C${reservationDetails.precioPorAsiento.toFixed(2)} NIO</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Total Pagado:</span>
                  <span className="detail-value">C${(reservationDetails.precioPorAsiento * reservationDetails.numerosLugar.length).toFixed(2)} NIO</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Método de Pago:</span>
                  <span className="detail-value">PayPal</span>
                </div>
              </div>
            </div>
          </div>
          
          <div className="action-buttons">
            <button className="btn btn-primary" onClick={onBack}>
              <i className="fas fa-arrow-left"></i> Volver al Mapa
            </button>
            <button className="btn btn-success" onClick={() => window.print()}>
              <i className="fas fa-print"></i> Imprimir Comprobante
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ValidationSuccess;