package com.erp.repository;

import com.erp.model.Attendance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "attendanceRepository")
@Transactional
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	List<Attendance> findByEmployeeCode(Long employeeCode);

}
