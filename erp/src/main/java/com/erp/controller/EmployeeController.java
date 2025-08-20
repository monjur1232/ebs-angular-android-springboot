package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Employee;
import com.erp.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// @CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employee")
public class EmployeeController implements ICommonController<Employee> {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    @GetMapping
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable(value = "id") Long employeeId) {
        Employee employee = employeeRepository.getById(employeeId);
        return ResponseEntity.ok().body(employee);
    }

    @Override
    @PostMapping
    public Employee save(@Validated @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable(value = "id") Long employeeId,
                                           @Validated @RequestBody Employee employeeDetails) {
        Employee employee = employeeRepository.getById(employeeId);

        employee.setEmployeeCode(employeeDetails.getEmployeeCode());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setGender(employeeDetails.getGender());
        employee.setPhone(employeeDetails.getPhone());
        employee.setEmail(employeeDetails.getEmail());
        employee.setAddress(employeeDetails.getAddress());
        employee.setDateOfBirth(employeeDetails.getDateOfBirth());
        employee.setHireDate(employeeDetails.getHireDate());
        employee.setSalary(employeeDetails.getSalary());
        employee.setDepartmentCode(employeeDetails.getDepartmentCode());
        employee.setDepartmentName(employeeDetails.getDepartmentName());
        employee.setDesignationCode(employeeDetails.getDesignationCode());
        employee.setDesignationName(employeeDetails.getDesignationName());
        employee.setStatus(employeeDetails.getStatus());

        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long employeeId) {
        Employee employee = employeeRepository.getById(employeeId);
        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
