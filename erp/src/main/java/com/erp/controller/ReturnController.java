package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Return;
import com.erp.repository.ReturnRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/return")
public class ReturnController implements ICommonController<Return> {

    @Autowired
    private ReturnRepository returnRepository;

    @Override
    @GetMapping
    public List<Return> getAll() {
        return returnRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Return> getById(@PathVariable(value = "id") Long id) {
        Return returnData = returnRepository.getById(id);
        return ResponseEntity.ok().body(returnData);
    }

    @Override
    @PostMapping
    public Return save(@Validated @RequestBody Return returnData) {
        return returnRepository.save(returnData);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Return> update(@PathVariable(value = "id") Long id,
                                         @Validated @RequestBody Return returnDetails) {
        Return returnData = returnRepository.getById(id);

        returnData.setReturnCode(returnDetails.getReturnCode());
        returnData.setPurchaseOrderCode(returnDetails.getPurchaseOrderCode());
        returnData.setSupplierCode(returnDetails.getSupplierCode());
        returnData.setSupplierName(returnDetails.getSupplierName());
        returnData.setProductCode(returnDetails.getProductCode());
        returnData.setProductName(returnDetails.getProductName());
        returnData.setPurchaseQuantity(returnDetails.getPurchaseQuantity());
        returnData.setReturnQuantity(returnDetails.getReturnQuantity());
        returnData.setReturnDate(returnDetails.getReturnDate());
        returnData.setReason(returnDetails.getReason());

        final Return updatedReturn = returnRepository.save(returnData);
        return ResponseEntity.ok(updatedReturn);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        Return returnData = returnRepository.getById(id);
        returnRepository.delete(returnData);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
