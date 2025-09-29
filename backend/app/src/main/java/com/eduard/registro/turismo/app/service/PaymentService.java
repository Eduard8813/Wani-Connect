package com.eduard.registro.turismo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eduard.registro.turismo.app.dto.ConfirmPaymentResponse;
import com.eduard.registro.turismo.app.dto.CreatePaymentRequest;
import com.eduard.registro.turismo.app.dto.CreatePaymentResponse;
import com.eduard.registro.turismo.app.model.Payment;
import com.eduard.registro.turismo.app.model.User;
import com.eduard.registro.turismo.app.repository.PaymentRepository;
import com.eduard.registro.turismo.app.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired 
    private PaymentRepository paymentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // URL base de PayPal.me
    private static final String PAYPAL_ME_BASE_URL = "https://paypal.me/Wanniconnect";
    
    @Transactional
    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
        try {
            // Validar que los datos requeridos estén presentes
            if (request.getUserEmail() == null || request.getUserEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("El email del usuario es requerido");
            }
            
            if (request.getAmount() == null || request.getAmount() <= 0) {
                throw new IllegalArgumentException("El monto debe ser mayor que cero");
            }
            
            if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
                throw new IllegalArgumentException("La moneda es requerida");
            }
            
            // Buscar usuario por email - manejar el caso cuando devuelve Object
            Object userObj = userRepository.findByEmail(request.getUserEmail());
            if (userObj == null) {
                throw new RuntimeException("Usuario no encontrado: " + request.getUserEmail());
            }
            
            // Convertir el objeto a User
            User user;
            if (userObj instanceof User) {
                user = (User) userObj;
            } else {
                throw new RuntimeException("El objeto devuelto por el repositorio no es de tipo User: " + userObj.getClass().getName());
            }
            
            // Crear nuevo pago
            Payment payment = new Payment();
            payment.setReservationCode(request.getReservationCode() != null ? request.getReservationCode() : "RES-" + UUID.randomUUID().toString());
            payment.setAmount(request.getAmount());
            payment.setCurrency(request.getCurrency());
            payment.setUserEmail(request.getUserEmail());
            payment.setUser(user); // Asignar el usuario encontrado
            payment.setStatus("CREATED");
            payment.setCreatedAt(LocalDateTime.now());
            
            // Generar un ID único para PayPal
            String paypalPaymentId = "PAY_" + UUID.randomUUID().toString().substring(0, 8);
            payment.setPaypalPaymentId(paypalPaymentId);
            
            // Guardar en la base de datos
            payment = paymentRepository.save(payment);
            
            // Construir URL de PayPal.me con monto y moneda
            String paypalUrl = String.format("%s/%.2f%s", PAYPAL_ME_BASE_URL, request.getAmount(), request.getCurrency());
            
            // Preparar respuesta
            CreatePaymentResponse response = new CreatePaymentResponse();
            response.setPaymentId(paypalPaymentId);
            response.setStatus("CREATED");
            response.setAmount(request.getAmount());
            response.setCurrency(request.getCurrency());
            response.setPaypalUrl(paypalUrl);
            
            return response;
            
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error de validación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el pago: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public ConfirmPaymentResponse confirmPayment(String paypalPaymentId) {
        try {
            // Validar que el ID de pago no sea nulo o vacío
            if (paypalPaymentId == null || paypalPaymentId.trim().isEmpty()) {
                throw new IllegalArgumentException("El ID de pago es requerido");
            }
            
            // Buscar pago en la base de datos
            Object paymentObj = paymentRepository.findByPaypalPaymentId(paypalPaymentId);
            if (paymentObj == null) {
                throw new RuntimeException("Pago no encontrado con ID: " + paypalPaymentId);
            }
            
            // Convertir el objeto a Payment
            Payment payment;
            if (paymentObj instanceof Payment) {
                payment = (Payment) paymentObj;
            } else {
                throw new RuntimeException("El objeto devuelto por el repositorio no es de tipo Payment: " + paymentObj.getClass().getName());
            }
            
            // Verificar que el pago no esté ya completado
            if ("COMPLETED".equals(payment.getStatus())) {
                throw new RuntimeException("El pago ya está completado");
            }
            
            // Actualizar estado a COMPLETED
            payment.setStatus("COMPLETED");
            payment.setCompletedAt(LocalDateTime.now());
            payment = paymentRepository.save(payment);
            
            // Preparar respuesta
            ConfirmPaymentResponse response = new ConfirmPaymentResponse();
            response.setPaymentId(paypalPaymentId);
            response.setStatus("COMPLETED");
            response.setCompletedAt(payment.getCompletedAt());
            
            return response;
            
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error de validación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al confirmar el pago: " + e.getMessage(), e);
        }
    }
}