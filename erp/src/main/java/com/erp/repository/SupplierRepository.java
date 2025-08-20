package com.erp.repository;

import com.erp.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "supplierRepository")
@Transactional
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
	
}
