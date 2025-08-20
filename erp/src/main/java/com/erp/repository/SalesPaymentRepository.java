package com.erp.repository;

import com.erp.model.SalesPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "salesPaymentRepository")
@Transactional
public interface SalesPaymentRepository extends JpaRepository<SalesPayment, Long> {

}
