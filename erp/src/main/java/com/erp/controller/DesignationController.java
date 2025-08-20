package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Designation;
import com.erp.repository.DesignationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/designation")
public class DesignationController implements ICommonController<Designation> {

    @Autowired
    private DesignationRepository designationRepository;

    @Override
    @GetMapping
    public List<Designation> getAll() {
        return designationRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Designation> getById(@PathVariable(value = "id") Long id) {
        Designation designation = designationRepository.getById(id);
        return ResponseEntity.ok().body(designation);
    }

    @Override
    @PostMapping
    public Designation save(@Validated @RequestBody Designation designation) {
        return designationRepository.save(designation);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Designation> update(@PathVariable(value = "id") Long id,
                                              @Validated @RequestBody Designation designationDetails) {
        Designation designation = designationRepository.getById(id);

        designation.setDesignationCode(designationDetails.getDesignationCode());
        designation.setDesignationName(designationDetails.getDesignationName());
        designation.setLevel(designationDetails.getLevel());

        final Designation updatedDesignation = designationRepository.save(designation);
        return ResponseEntity.ok(updatedDesignation);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        Designation designation = designationRepository.getById(id);
        designationRepository.delete(designation);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
