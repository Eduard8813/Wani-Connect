package com.eduard.registro.turismo.app.dto;

import java.time.LocalDateTime;

public class ConfirmPaymentResponse {
    private String paymentId;
    private String status;
    private LocalDateTime completedAt;
    
    // Getters y Setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}