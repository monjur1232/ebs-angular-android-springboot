package com.erp.repository;

import com.erp.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "purchaseOrderRepository")
@Transactional
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

}
