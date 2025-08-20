package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Payment;
import com.erp.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payment")
public class PaymentController implements ICommonController<Payment> {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    @GetMapping
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable(value = "id") Long id) {
        Payment payment = paymentRepository.getById(id);
        return ResponseEntity.ok().body(payment);
    }

    @Override
    @PostMapping
    public Payment save(@Validated @RequestBody Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable(value = "id") Long id,
                                          @Validated @RequestBody Payment paymentDetails) {
        Payment payment = paymentRepository.getById(id);

        payment.setPaymentCode(paymentDetails.getPaymentCode());
        payment.setSupplierCode(paymentDetails.getSupplierCode());
        payment.setSupplierName(paymentDetails.getSupplierName());
        payment.setPurchaseOrderCode(paymentDetails.getPurchaseOrderCode());
        payment.setAmountPaid(paymentDetails.getAmountPaid());
        payment.setPayDate(paymentDetails.getPayDate());
        payment.setPaymentMode(paymentDetails.getPaymentMode());
        payment.setPaymentRef(paymentDetails.getPaymentRef());

        final Payment updatedPayment = paymentRepository.save(payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        Payment payment = paymentRepository.getById(id);
        paymentRepository.delete(payment);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
