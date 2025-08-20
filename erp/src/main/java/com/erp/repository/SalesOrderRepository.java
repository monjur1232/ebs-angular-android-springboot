package com.erp.repository;

import com.erp.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "salesOrderRepository")
@Transactional
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

}
