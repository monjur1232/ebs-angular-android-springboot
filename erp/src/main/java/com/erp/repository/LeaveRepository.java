package com.erp.repository;

import com.erp.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository(value = "leaveRepository")
@Transactional
public interface LeaveRepository extends JpaRepository<Leave, Long> {
	
    List<Leave> findByStatus(Integer status);
    List<Leave> findByEmployeeCode(Long employeeCode);
    List<Leave> findByEmployeeCodeAndStatus(Long employeeCode, Integer status);
    
}
