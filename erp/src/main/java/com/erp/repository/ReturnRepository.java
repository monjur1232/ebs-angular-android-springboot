package com.erp.repository;

import com.erp.model.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "returnRepository")
@Transactional
public interface ReturnRepository extends JpaRepository<Return, Long> {

}
