package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Attendance;
import com.erp.repository.AttendanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/attendance")
public class AttendanceController implements ICommonController<Attendance> {

	@Autowired
	private AttendanceRepository attendanceRepository;

	// ✅ Get all attendance records
	@Override
	@GetMapping
	public List<Attendance> getAll() {
		return attendanceRepository.findAll();
	}

	// ✅ Get single attendance by ID
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<Attendance> getById(@PathVariable(value = "id") Long attendanceId) {
		Attendance attendance = attendanceRepository.getById(attendanceId);
		return ResponseEntity.ok().body(attendance);
	}

	// ✅ Save single attendance (not used in bulk entry but kept for compatibility)
	@Override
	@PostMapping
	public Attendance save(@Validated @RequestBody Attendance attendance) {
		return attendanceRepository.save(attendance);
	}

	// ✅ Bulk save attendance for multiple employees
	@PostMapping("/bulk")
	public List<Attendance> saveAll(@RequestBody List<Attendance> attendances) {
		return attendanceRepository.saveAll(attendances);
	}

	// ✅ Update attendance by ID
	@Override
	@PutMapping("/{id}")
	public ResponseEntity<Attendance> update(@PathVariable(value = "id") Long attendanceId,
			@Validated @RequestBody Attendance attendanceDetails) {
		Attendance attendance = attendanceRepository.getById(attendanceId);

		attendance.setAttendanceCode(attendanceDetails.getAttendanceCode());
		attendance.setEmployeeCode(attendanceDetails.getEmployeeCode());
		attendance.setEmployeeName(attendanceDetails.getEmployeeName());
		attendance.setDate(attendanceDetails.getDate());
		attendance.setInTime(attendanceDetails.getInTime());
		attendance.setOutTime(attendanceDetails.getOutTime());
		attendance.setStatus(attendanceDetails.getStatus());

		final Attendance updatedAttendance = attendanceRepository.save(attendance);
		return ResponseEntity.ok(updatedAttendance);
	}

	// ✅ Delete attendance by ID
	@Override
	@DeleteMapping("/{id}")
	public Map<String, Boolean> delete(@PathVariable(value = "id") Long attendanceId) {
		Attendance attendance = attendanceRepository.getById(attendanceId);
		attendanceRepository.delete(attendance);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	// Payroll

	@GetMapping("/absent-count/{employeeCode}")
	public ResponseEntity<Map<String, Integer>> getAbsentCount(@PathVariable Long employeeCode) {
		List<Attendance> attendances = attendanceRepository.findByEmployeeCode(employeeCode);

		int absentCount = 0;
		int lateCount = 0;

		for (Attendance att : attendances) {
			if ("Absent".equals(att.getStatus())) {
				absentCount++;
			} else if ("Late".equals(att.getStatus())) {
				lateCount++;
			}
		}

		// 3 Late = 1 Absent
		absentCount += lateCount / 3;

		Map<String, Integer> response = new HashMap<>();
		response.put("absentCount", absentCount);
		return ResponseEntity.ok(response);
	}
}
