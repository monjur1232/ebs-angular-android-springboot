package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.erp.model.Department;

@Repository(value = "departmentRepository")
@Transactional
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
