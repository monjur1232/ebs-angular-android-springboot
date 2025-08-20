package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Salary;
import com.erp.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/salary")
public class SalaryController implements ICommonController<Salary> {

    @Autowired
    private SalaryRepository salaryRepository;

    @Override
    @GetMapping
    public List<Salary> getAll() {
        return salaryRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Salary> getById(@PathVariable(value = "id") Long salaryId) {
        Salary salary = salaryRepository.getById(salaryId);
        return ResponseEntity.ok().body(salary);
    }

    @Override
    @PostMapping
    public Salary save(@Validated @RequestBody Salary salary) {
        return salaryRepository.save(salary);
    }

    @PostMapping("/bulk")
    public List<Salary> saveAll(@Validated @RequestBody List<Salary> salaries) {
        return salaryRepository.saveAll(salaries);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Salary> update(@PathVariable(value = "id") Long salaryId,
                                       @Validated @RequestBody Salary salaryDetails) {
        Salary salary = salaryRepository.getById(salaryId);

        salary.setSalaryCode(salaryDetails.getSalaryCode());
        salary.setEmployeeCode(salaryDetails.getEmployeeCode());
        salary.setEmployeeName(salaryDetails.getEmployeeName());
        salary.setDesignationCode(salaryDetails.getDesignationCode());
        salary.setDesignationName(salaryDetails.getDesignationName());
        salary.setMonth(salaryDetails.getMonth());
        salary.setYear(salaryDetails.getYear());
        salary.setSalaryStructureCode(salaryDetails.getSalaryStructureCode());
        salary.setBasicSalary(salaryDetails.getBasicSalary());
        salary.setHouseRent(salaryDetails.getHouseRent());
        salary.setMedicalAllowance(salaryDetails.getMedicalAllowance());
        salary.setTransportAllowance(salaryDetails.getTransportAllowance());
        salary.setOthers(salaryDetails.getOthers());
        salary.setGrossSalary(salaryDetails.getGrossSalary());
        salary.setDeductions(salaryDetails.getDeductions());
        salary.setNetSalary(salaryDetails.getNetSalary());
        salary.setPayDate(salaryDetails.getPayDate());
        salary.setPaymentMode(salaryDetails.getPaymentMode());
        salary.setPaymentRef(salaryDetails.getPaymentRef());

        final Salary updatedSalary = salaryRepository.save(salary);
        return ResponseEntity.ok(updatedSalary);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long salaryId) {
        Salary salary = salaryRepository.getById(salaryId);
        salaryRepository.delete(salary);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @GetMapping("/month/{month}/year/{year}")
    public List<Salary> getByMonthAndYear(@PathVariable String month, @PathVariable String year) {
        return salaryRepository.findByMonthAndYear(month, year);
    }

    @GetMapping("/exists/employee/{employeeCode}/month/{month}/year/{year}")
    public boolean checkSalaryExists(@PathVariable Long employeeCode, 
                                   @PathVariable String month, 
                                   @PathVariable String year) {
        return salaryRepository.existsByEmployeeCodeAndMonthAndYear(employeeCode, month, year);
    }
    
}
