package com.erp.repository;

import com.erp.model.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "salaryStructureRepository")
@Transactional
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {

}
