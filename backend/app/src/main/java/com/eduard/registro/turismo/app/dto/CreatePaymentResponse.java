package com.eduard.registro.turismo.app.dto;

public class CreatePaymentResponse {
    private String paymentId;
    private String status;
    private Double amount;
    private String currency;
    private String paypalUrl;
    
    // Getters y Setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; } 
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getPaypalUrl() { return paypalUrl; }
    public void setPaypalUrl(String paypalUrl) { this.paypalUrl = paypalUrl; }
}