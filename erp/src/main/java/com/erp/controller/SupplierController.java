package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Supplier;
import com.erp.repository.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/supplier")
public class SupplierController implements ICommonController<Supplier> {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    @GetMapping
    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getById(@PathVariable(value = "id") Long supplierId) {
        Supplier supplier = supplierRepository.getById(supplierId);
        return ResponseEntity.ok().body(supplier);
    }

    @Override
    @PostMapping
    public Supplier save(@Validated @RequestBody Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> update(@PathVariable(value = "id") Long supplierId,
                                           @Validated @RequestBody Supplier supplierDetails) {
        Supplier supplier = supplierRepository.getById(supplierId);

        supplier.setSupplierCode(supplierDetails.getSupplierCode());
        supplier.setSupplierName(supplierDetails.getSupplierName());
        supplier.setContactPerson(supplierDetails.getContactPerson());
        supplier.setPhone(supplierDetails.getPhone());
        supplier.setEmail(supplierDetails.getEmail());
        supplier.setAddress(supplierDetails.getAddress());
        supplier.setTaxId(supplierDetails.getTaxId());
        supplier.setStatus(supplierDetails.getStatus());

        final Supplier updatedSupplier = supplierRepository.save(supplier);
        return ResponseEntity.ok(updatedSupplier);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long supplierId) {
        Supplier supplier = supplierRepository.getById(supplierId);
        supplierRepository.delete(supplier);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
