package com.erp.repository;

import com.erp.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository(value = "salaryRepository")
@Transactional
public interface SalaryRepository extends JpaRepository<Salary, Long> {
	
    List<Salary> findByMonthAndYear(String month, String year);
    boolean existsByEmployeeCodeAndMonthAndYear(Long employeeCode, String month, String year);
    
}
