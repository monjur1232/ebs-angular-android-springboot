package com.erp.repository;

import com.erp.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "designationRepository")
@Transactional
public interface DesignationRepository extends JpaRepository<Designation, Long> {

}
