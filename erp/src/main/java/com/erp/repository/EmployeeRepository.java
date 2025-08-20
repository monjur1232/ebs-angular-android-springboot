package com.erp.repository;

import com.erp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "employeeRepository")
@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
    Employee findByEmployeeCode(Long employeeCode);
    
}
