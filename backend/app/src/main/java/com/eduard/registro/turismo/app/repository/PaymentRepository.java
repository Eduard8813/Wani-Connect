package com.eduard.registro.turismo.app.repository;

import com.eduard.registro.turismo.app.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaypalPaymentId(String paypalPaymentId);
}