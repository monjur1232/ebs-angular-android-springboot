package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.PurchaseOrder;
import com.erp.repository.PurchaseOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/purchase-order")
public class PurchaseOrderController implements ICommonController<PurchaseOrder> {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Override
    @GetMapping
    public List<PurchaseOrder> getAll() {
        return purchaseOrderRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getById(@PathVariable(value = "id") Long id) {
        PurchaseOrder po = purchaseOrderRepository.getById(id);
        return ResponseEntity.ok().body(po);
    }

    @Override
    @PostMapping
    public PurchaseOrder save(@Validated @RequestBody PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrder> update(@PathVariable(value = "id") Long id,
                                                @Validated @RequestBody PurchaseOrder poDetails) {
        PurchaseOrder po = purchaseOrderRepository.getById(id);

        po.setPurchaseOrderCode(poDetails.getPurchaseOrderCode());
        po.setSupplierCode(poDetails.getSupplierCode());
        po.setSupplierName(poDetails.getSupplierName());
        po.setOrderDate(poDetails.getOrderDate());
        po.setReceivedDate(poDetails.getReceivedDate());
        po.setProductCode(poDetails.getProductCode());
        po.setProductName(poDetails.getProductName());
        po.setUnitPrice(poDetails.getUnitPrice());        
        po.setPurchaseQuantity(poDetails.getPurchaseQuantity());
        po.setTotalAmount(poDetails.getTotalAmount());
        po.setPaymentStatus(poDetails.getPaymentStatus());
        po.setStatus(poDetails.getStatus());

        final PurchaseOrder updatedPO = purchaseOrderRepository.save(po);
        return ResponseEntity.ok(updatedPO);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        PurchaseOrder po = purchaseOrderRepository.getById(id);
        purchaseOrderRepository.delete(po);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
