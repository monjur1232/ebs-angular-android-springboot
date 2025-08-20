package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.SalesPayment;
import com.erp.repository.SalesPaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sales-payment")
public class SalesPaymentController implements ICommonController<SalesPayment> {

    @Autowired
    private SalesPaymentRepository salesPaymentRepository;

    @Override
    @GetMapping
    public List<SalesPayment> getAll() {
        return salesPaymentRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SalesPayment> getById(@PathVariable(value = "id") Long id) {
        SalesPayment salesPayment = salesPaymentRepository.getById(id);
        return ResponseEntity.ok().body(salesPayment);
    }

    @Override
    @PostMapping
    public SalesPayment save(@Validated @RequestBody SalesPayment salesPayment) {
        return salesPaymentRepository.save(salesPayment);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<SalesPayment> update(@PathVariable(value = "id") Long id,
                                               @Validated @RequestBody SalesPayment details) {
        SalesPayment salesPayment = salesPaymentRepository.getById(id);

        salesPayment.setSalesPaymentCode(details.getSalesPaymentCode());
        salesPayment.setCustomerCode(details.getCustomerCode());
        salesPayment.setCustomerName(details.getCustomerName());
        salesPayment.setSalesOrderCode(details.getSalesOrderCode());
        salesPayment.setAmountPaid(details.getAmountPaid());
        salesPayment.setPayDate(details.getPayDate());
        salesPayment.setPaymentMode(details.getPaymentMode());
        salesPayment.setPaymentRef(details.getPaymentRef());

        final SalesPayment updated = salesPaymentRepository.save(salesPayment);
        return ResponseEntity.ok(updated);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        SalesPayment salesPayment = salesPaymentRepository.getById(id);
        salesPaymentRepository.delete(salesPayment);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
