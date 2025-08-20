package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.SalaryStructure;
import com.erp.repository.SalaryStructureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/salary-structure")
public class SalaryStructureController implements ICommonController<SalaryStructure> {

    @Autowired
    private SalaryStructureRepository salaryStructureRepository;

    @Override
    @GetMapping
    public List<SalaryStructure> getAll() {
        return salaryStructureRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SalaryStructure> getById(@PathVariable(value = "id") Long id) {
        SalaryStructure salaryStructure = salaryStructureRepository.getById(id);
        return ResponseEntity.ok().body(salaryStructure);
    }

    @Override
    @PostMapping
    public SalaryStructure save(@Validated @RequestBody SalaryStructure salaryStructure) {
        return salaryStructureRepository.save(salaryStructure);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<SalaryStructure> update(@PathVariable(value = "id") Long id,
                                                  @Validated @RequestBody SalaryStructure salaryDetails) {
        SalaryStructure salaryStructure = salaryStructureRepository.getById(id);

        salaryStructure.setSalaryStructureCode(salaryDetails.getSalaryStructureCode());
        salaryStructure.setDesignationCode(salaryDetails.getDesignationCode());
        salaryStructure.setDesignationName(salaryDetails.getDesignationName());
        salaryStructure.setBasicSalary(salaryDetails.getBasicSalary());
        salaryStructure.setHouseRent(salaryDetails.getHouseRent());
        salaryStructure.setMedicalAllowance(salaryDetails.getMedicalAllowance());
        salaryStructure.setTransportAllowance(salaryDetails.getTransportAllowance());
        salaryStructure.setOthers(salaryDetails.getOthers());
        salaryStructure.setGrossSalary(salaryDetails.getGrossSalary());

        final SalaryStructure updatedSalary = salaryStructureRepository.save(salaryStructure);
        return ResponseEntity.ok(updatedSalary);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        SalaryStructure salaryStructure = salaryStructureRepository.getById(id);
        salaryStructureRepository.delete(salaryStructure);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
