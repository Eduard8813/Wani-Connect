package com.eduard.registro.turismo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eduard.registro.turismo.app.dto.ConfirmPaymentResponse;
import com.eduard.registro.turismo.app.dto.CreatePaymentRequest;
import com.eduard.registro.turismo.app.dto.CreatePaymentResponse;
import com.eduard.registro.turismo.app.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/create")
    public ResponseEntity<CreatePaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        CreatePaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/confirm/{paymentId}")
    public ResponseEntity<ConfirmPaymentResponse> confirmPayment(@PathVariable String paymentId) {
        ConfirmPaymentResponse response = paymentService.confirmPayment(paymentId);
        return ResponseEntity.ok(response);
    }
}