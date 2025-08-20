package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.SalesOrder;
import com.erp.repository.SalesOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sales-order")
public class SalesOrderController implements ICommonController<SalesOrder> {

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Override
    @GetMapping
    public List<SalesOrder> getAll() {
        return salesOrderRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SalesOrder> getById(@PathVariable(value = "id") Long id) {
        SalesOrder salesOrder = salesOrderRepository.getById(id);
        return ResponseEntity.ok().body(salesOrder);
    }

    @Override
    @PostMapping
    public SalesOrder save(@Validated @RequestBody SalesOrder salesOrder) {
        return salesOrderRepository.save(salesOrder);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<SalesOrder> update(@PathVariable(value = "id") Long id,
                                             @Validated @RequestBody SalesOrder details) {
        SalesOrder salesOrder = salesOrderRepository.getById(id);

        salesOrder.setSalesOrderCode(details.getSalesOrderCode());
        salesOrder.setCustomerCode(details.getCustomerCode());
        salesOrder.setCustomerName(details.getCustomerName());
        salesOrder.setOrderDate(details.getOrderDate());
        salesOrder.setDeliveryDate(details.getDeliveryDate());
        salesOrder.setProductCode(details.getProductCode());
        salesOrder.setProductName(details.getProductName());
        salesOrder.setUnitPrice(details.getUnitPrice());
        salesOrder.setSalesQuantity(details.getSalesQuantity());
        salesOrder.setTotalAmount(details.getTotalAmount());
        salesOrder.setPaymentStatus(details.getPaymentStatus());
        salesOrder.setStatus(details.getStatus());

        final SalesOrder updatedOrder = salesOrderRepository.save(salesOrder);
        return ResponseEntity.ok(updatedOrder);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        SalesOrder salesOrder = salesOrderRepository.getById(id);
        salesOrderRepository.delete(salesOrder);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
