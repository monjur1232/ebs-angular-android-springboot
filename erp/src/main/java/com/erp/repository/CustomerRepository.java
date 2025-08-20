package com.erp.repository;

import com.erp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "customerRepository")
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
