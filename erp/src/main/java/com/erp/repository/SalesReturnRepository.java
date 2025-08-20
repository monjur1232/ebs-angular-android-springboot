package com.erp.repository;

import com.erp.model.SalesReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "salesReturnRepository")
@Transactional
public interface SalesReturnRepository extends JpaRepository<SalesReturn, Long> {

}
