package com.erp.controller;

import com.erp.model.Leave;
import com.erp.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "*")
public class LeaveController {

	@Autowired
	private LeaveRepository leaveRepository;

	@GetMapping
	public List<Leave> getAllLeaves() {
		return leaveRepository.findAll();
	}

	@PostMapping
	public Leave createLeave(@RequestBody Leave leave) {
		leave.setStatus(0); // Default status as Pending
		return leaveRepository.save(leave);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Leave> getLeaveById(@PathVariable Long id) {
		return leaveRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Leave> updateLeave(@PathVariable Long id, @RequestBody Leave leaveDetails) {
		return leaveRepository.findById(id).map(leave -> {
			leave.setLeaveCode(leaveDetails.getLeaveCode());
			leave.setEmployeeCode(leaveDetails.getEmployeeCode());
			leave.setEmployeeName(leaveDetails.getEmployeeName());
			leave.setLeaveType(leaveDetails.getLeaveType());
			leave.setStartDate(leaveDetails.getStartDate());
			leave.setEndDate(leaveDetails.getEndDate());
			leave.setReason(leaveDetails.getReason());
			leave.setLeaveProposal(leaveDetails.getLeaveProposal());
			leave.setStatus(leaveDetails.getStatus());
			return ResponseEntity.ok(leaveRepository.save(leave));
		}).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/approve/{id}")
	public ResponseEntity<Leave> approveLeave(@PathVariable Long id) {
		return leaveRepository.findById(id).map(leave -> {
			leave.setStatus(1); // Approved
			return ResponseEntity.ok(leaveRepository.save(leave));
		}).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/reject/{id}")
	public ResponseEntity<Leave> rejectLeave(@PathVariable Long id) {
		return leaveRepository.findById(id).map(leave -> {
			leave.setStatus(2); // Rejected
			return ResponseEntity.ok(leaveRepository.save(leave));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteLeave(@PathVariable Long id) {
		return leaveRepository.findById(id).map(leave -> {
			leaveRepository.delete(leave);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/status/{status}")
	public List<Leave> getLeavesByStatus(@PathVariable Integer status) {
		return leaveRepository.findByStatus(status);
	}

	@GetMapping("/employee/{employeeCode}")
	public List<Leave> getLeavesByEmployee(@PathVariable Long employeeCode) {
		return leaveRepository.findByEmployeeCode(employeeCode);
	}

	@GetMapping("/approved-leave-count/{employeeCode}")
	public ResponseEntity<Map<String, Integer>> getApprovedLeaveCount(@PathVariable Long employeeCode) {
		List<Leave> approvedLeaves = leaveRepository.findByEmployeeCodeAndStatus(employeeCode, 1);

		int approvedLeaveCount = 0;
		for (Leave leave : approvedLeaves) {
			// Calculate days between start and end date
			long diffInMillis = leave.getEndDate().getTime() - leave.getStartDate().getTime();
			approvedLeaveCount += (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1;
		}

		Map<String, Integer> response = new HashMap<>();
		response.put("approvedLeaveCount", approvedLeaveCount);
		return ResponseEntity.ok(response);
	}
	
}
