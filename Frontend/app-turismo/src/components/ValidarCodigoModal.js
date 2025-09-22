// src/components/ValidarCodigoModal.js
import React, { useState } from 'react';
import ValidationSuccess from './ValidationSuccess';

const ValidarCodigoModal = ({ onClose, authToken, onValidationSuccess }) => {
  const [codigo, setCodigo] = useState('');
  const [resultado, setResultado] = useState(null);
  const [loading, setLoading] = useState(false);
  const [validationSuccess, setValidationSuccess] = useState(false);
  const [reservationDetails, setReservationDetails] = useState(null);

  const handleValidarCodigo = async () => {
    if (!codigo.trim()) {
      setResultado({ success: false, message: 'Ingresa un código de reserva' });
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/api/reservas-bus/validar/${codigo}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        const mensaje = await response.text();
        setResultado({ success: true, message: mensaje });
        
        // Obtener detalles de la reserva
        try {
          const detailsResponse = await fetch(`http://localhost:8080/api/reservas-bus/codigo/${codigo}`, {
            headers: {
              'Authorization': `Bearer ${authToken}`
            }
          });
          
          if (detailsResponse.ok) {
            const details = await detailsResponse.json();
            setReservationDetails(details);
            setValidationSuccess(true);
            
            // Notificar al componente padre que la validación fue exitosa
            if (onValidationSuccess) {
              onValidationSuccess(details);
            }
          } else {
            setResultado({ 
              success: false, 
              message: 'Reserva validada pero no se pudieron obtener los detalles' 
            });
          }
        } catch (error) {
          console.error('Error al obtener detalles de la reserva:', error);
          setResultado({ 
            success: false, 
            message: 'Reserva validada pero no se pudieron obtener los detalles' 
          });
        }
      } else {
        const error = await response.text();
        setResultado({ success: false, message: error });
      }
    } catch (error) {
      setResultado({ success: false, message: `Error de conexión: ${error.message}` });
    } finally {
      setLoading(false);
    }
  };

  const handleValidarQR = async () => {
    // Implementación similar para QR
    // ...
  };

  const resetValidation = () => {
    setValidationSuccess(false);
    setResultado(null);
    setReservationDetails(null);
    setCodigo('');
  };

  return (
    <div className="modal-content">
      <span className="close" onClick={onClose}>&times;</span>
      
      {validationSuccess ? (
        <ValidationSuccess 
          reservationDetails={reservationDetails} 
          onBack={resetValidation}
          onLogout={onClose}
        />
      ) : (
        <>
          <h2>Validar Reserva</h2>
          
          {/* Selector de método de validación */}
          <div className="validation-method-selector">
            <button className="btn btn-primary method-btn active" id="btnCodigo">
              <i className="fas fa-keyboard"></i> Código
            </button>
            <button className="btn btn-secondary method-btn" id="btnQR">
              <i className="fas fa-qrcode"></i> QR
            </button>
          </div>
          
          {/* Validación por código */}
          <div className="validation-section">
            <div className="form-group">
              <label htmlFor="codigoValidar">Código de Reserva:</label>
              <input 
                type="text" 
                id="codigoValidar" 
                className="form-control" 
                placeholder="Ingresa el código de reserva"
                value={codigo}
                onChange={(e) => setCodigo(e.target.value)}
              />
            </div>
            <div className="action-buttons">
              <button 
                className="btn btn-primary" 
                onClick={handleValidarCodigo}
                disabled={loading}
              >
                {loading ? 'Validando...' : 'Validar Código'}
              </button>
            </div>
          </div>
          
          {/* Validación por QR */}
          <div className="validation-section" style={{ display: 'none' }}>
            <div className="form-group">
              <label htmlFor="qrFile">Seleccionar imagen QR:</label>
              <input 
                type="file" 
                id="qrFile" 
                className="form-control" 
                accept="image/*" 
              />
            </div>
            <div className="action-buttons">
              <button className="btn btn-primary" onClick={handleValidarQR}>
                Validar QR
              </button>
            </div>
          </div>
          
          {/* Resultado de la validación */}
          {resultado && (
            <div className={`endpoint-status ${resultado.success ? 'endpoint-success' : 'endpoint-error'}`}>
              <i className={`fas ${resultado.success ? 'fa-check-circle' : 'fa-times-circle'}`}></i> 
              {resultado.message}
            </div>
          )}
          
          <div className="action-buttons">
            <button className="btn btn-secondary" onClick={onClose}>Cancelar</button>
          </div>
        </>
      )}
    </div>
  );
};

export default ValidarCodigoModal;