package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.erp.model.Registration;

@Repository(value = "registrationRepository")
@Transactional
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
	
    Registration findByEmailAndPassword(String email, String password);
    Registration findByEmployeeCode(Integer employeeCode);
    
}
