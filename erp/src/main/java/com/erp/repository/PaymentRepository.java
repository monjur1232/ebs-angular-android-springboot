package com.erp.repository;

import com.erp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "paymentRepository")
@Transactional
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
