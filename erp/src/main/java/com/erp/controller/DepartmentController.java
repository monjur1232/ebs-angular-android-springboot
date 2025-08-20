package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Department;
import com.erp.repository.DepartmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/department")
public class DepartmentController implements ICommonController<Department> {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Override
	@GetMapping
	public List<Department> getAll() {
		return departmentRepository.findAll();
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<Department> getById(@PathVariable(value = "id") Long departmentId) {
		Department department = departmentRepository.getById(departmentId);
		return ResponseEntity.ok().body(department);
	}

	@Override
	@PostMapping
	public Department save(@Validated @RequestBody Department department) {
		return departmentRepository.save(department);
	}

	@Override
	@PutMapping("/{id}")
	public ResponseEntity<Department> update(@PathVariable(value = "id") Long departmentId,
											 @Validated @RequestBody Department departmentDetails) {
		Department department = departmentRepository.getById(departmentId);
		
		department.setDepartmentCode(departmentDetails.getDepartmentCode());
		department.setDepartmentName(departmentDetails.getDepartmentName());
		
		final Department updatedDepartment = departmentRepository.save(department);
		return ResponseEntity.ok(updatedDepartment);
	}

	@Override
	@DeleteMapping("/{id}")
	public Map<String, Boolean> delete(@PathVariable(value = "id") Long departmentId) {
		Department department = departmentRepository.getById(departmentId);
		departmentRepository.delete(department);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}
