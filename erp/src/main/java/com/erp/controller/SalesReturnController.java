package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.SalesReturn;
import com.erp.repository.SalesReturnRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sales-return")
public class SalesReturnController implements ICommonController<SalesReturn> {

    @Autowired
    private SalesReturnRepository salesReturnRepository;

    @Override
    @GetMapping
    public List<SalesReturn> getAll() {
        return salesReturnRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SalesReturn> getById(@PathVariable(value = "id") Long id) {
        SalesReturn salesReturn = salesReturnRepository.getById(id);
        return ResponseEntity.ok().body(salesReturn);
    }

    @Override
    @PostMapping
    public SalesReturn save(@Validated @RequestBody SalesReturn salesReturn) {
        return salesReturnRepository.save(salesReturn);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<SalesReturn> update(@PathVariable(value = "id") Long id,
                                              @Validated @RequestBody SalesReturn details) {
        SalesReturn salesReturn = salesReturnRepository.getById(id);

        salesReturn.setSalesReturnCode(details.getSalesReturnCode());
        salesReturn.setSalesOrderCode(details.getSalesOrderCode());
        salesReturn.setCustomerCode(details.getCustomerCode());
        salesReturn.setCustomerName(details.getCustomerName());
        salesReturn.setProductCode(details.getProductCode());
        salesReturn.setProductName(details.getProductName());
        salesReturn.setSalesQuantity(details.getSalesQuantity());
        salesReturn.setReturnQuantity(details.getReturnQuantity());
        salesReturn.setReturnDate(details.getReturnDate());
        salesReturn.setReason(details.getReason());

        final SalesReturn updatedSalesReturn = salesReturnRepository.save(salesReturn);
        return ResponseEntity.ok(updatedSalesReturn);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        SalesReturn salesReturn = salesReturnRepository.getById(id);
        salesReturnRepository.delete(salesReturn);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
