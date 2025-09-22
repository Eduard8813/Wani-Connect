package com.eduard.registro.turismo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduard.registro.turismo.app.dto.ConfirmPaymentResponse;
import com.eduard.registro.turismo.app.dto.CreatePaymentRequest;
import com.eduard.registro.turismo.app.dto.CreatePaymentResponse;
import com.eduard.registro.turismo.app.model.Payment;
import com.eduard.registro.turismo.app.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    // URL base de PayPal.me (proporcionada por el usuario)
    private static final String PAYPAL_ME_BASE_URL = "https://paypal.me/Wanniconnect";
    
    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
        // Crear nuevo pago
        Payment payment = new Payment();
        payment.setReservationCode(request.getReservationCode());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setUserEmail(request.getUserEmail());
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
    }
    
    public ConfirmPaymentResponse confirmPayment(String paypalPaymentId) {
        // Buscar pago en la base de datos
        Payment payment = paymentRepository.findByPaypalPaymentId(paypalPaymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        // Actualizar estado a COMPLETED
        payment.setStatus("COMPLETED");
        payment.setCompletedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        
        // Preparar respuesta
        ConfirmPaymentResponse response = new ConfirmPaymentResponse();
        response.setPaymentId(paypalPaymentId);
        response.setStatus("COMPLETED");
        response.setCompletedAt(payment.getCompletedAt());
        
        return response;
    }
}