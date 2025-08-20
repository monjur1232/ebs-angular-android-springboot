package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Registration;
import com.erp.repository.RegistrationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/registration")
public class RegistrationController implements ICommonController<Registration> {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Override
    @GetMapping
    public List<Registration> getAll() {
        return registrationRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Registration> getById(@PathVariable(value = "id") Long registrationId) {
        Registration registration = registrationRepository.getById(registrationId);
        return ResponseEntity.ok().body(registration);
    }

    @Override
    @PostMapping
    public Registration save(@Validated @RequestBody Registration registration) {
        return registrationRepository.save(registration);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Registration> update(@PathVariable(value = "id") Long registrationId,
                                               @Validated @RequestBody Registration registrationDetails) {
        Registration registration = registrationRepository.getById(registrationId);
        
        registration.setRegistrationCode(registrationDetails.getRegistrationCode());
        registration.setEmployeeCode(registrationDetails.getEmployeeCode());
        registration.setEmployeeName(registrationDetails.getEmployeeName());
        registration.setEmail(registrationDetails.getEmail());
        registration.setPassword(registrationDetails.getPassword());
        
        final Registration updatedRegistration = registrationRepository.save(registration);
        return ResponseEntity.ok(updatedRegistration);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long registrationId) {
        Registration registration = registrationRepository.getById(registrationId);
        registrationRepository.delete(registration);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
