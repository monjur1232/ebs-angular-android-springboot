import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Payment } from 'src/app/model/payment.model';
import { PurchaseOrder } from 'src/app/model/purchase-order.model';
import { Supplier } from 'src/app/model/supplier.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {

  url = 'http://localhost:8080/payment';
  payments: Payment[] = [];
  payment: Payment = new Payment();
  updatePayment: Payment = new Payment();
  divStatus = false;

  supplierUrl = 'http://localhost:8080/supplier';
  suppliers: Supplier[] = [];
  
  poUrl = 'http://localhost:8080/purchase-order';
  purchaseOrders: PurchaseOrder[] = [];
  filteredPurchaseOrders: PurchaseOrder[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllSuppliers();
    this.getAllPurchaseOrders();
    this.payment.paymentMode = 'Bank Transfer';
  }

  save(p: Payment) {
    this.http.post(this.url, p).toPromise().then(() => {
      this.getAll();
      this.payment = new Payment();
      this.filteredPurchaseOrders = [];
      this.resetSupplierDropdown();
      this.payment.paymentMode = 'Bank Transfer';

      Swal.fire('Saved!', 'Payment has been added.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to add payment.', 'error');
    });
  }

  resetSupplierDropdown() {
    const supplierSelect = document.querySelector('select[name="supplier"]') as HTMLSelectElement;
    if (supplierSelect) {
      supplierSelect.selectedIndex = 0;
    }
  }

  getAll() {
    this.http.get<Payment[]>(this.url).subscribe(
      (response) => this.payments = response
    );
  }

  edit(p: Payment) {
    this.updatePayment = Object.assign({}, p);
    this.divStatus = true;

    if (this.updatePayment.supplierCode) {
      this.filteredPurchaseOrders = this.purchaseOrders.filter(po => po.supplierCode == this.updatePayment.supplierCode);
    }
  }

  update(p: Payment) {
    this.http.put(this.url + '/' + p.id, p).toPromise().then(() => {
      this.getAll();
      this.updatePayment = new Payment();
      this.divStatus = false;

      Swal.fire('Updated!', 'Payment has been updated.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update payment.', 'error');
    });
  }

  delete(p: Payment) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this payment?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + p.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Payment has been deleted.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete payment.', 'error');
        });
      }
    });
  }

  getAllSuppliers() {
    this.http.get<Supplier[]>(this.supplierUrl).subscribe(
      (response) => {
        this.suppliers = response;
      }
    );
  }

  getAllPurchaseOrders() {
    this.http.get<PurchaseOrder[]>(this.poUrl).subscribe(
      (response) => {
        this.purchaseOrders = response;
      }
    );
  }

  onSupplierSelect(event: any) {
    const supplierCode = event.target.value;
    this.filteredPurchaseOrders = this.purchaseOrders.filter(po => po.supplierCode == supplierCode);
    this.resetPaymentForm();
  }

  onPurchaseOrderSelect(event: any) {
    const poCode = event.target.value;
    const selectedPO = this.purchaseOrders.find(po => po.purchaseOrderCode == poCode);
    if (selectedPO) {
      this.payment.purchaseOrderCode = selectedPO.purchaseOrderCode;
      this.payment.supplierCode = selectedPO.supplierCode;
      this.payment.supplierName = selectedPO.supplierName;
      this.payment.amountPaid = selectedPO.totalAmount;
    }
  }

  resetPaymentForm() {
    this.payment.purchaseOrderCode = undefined;
    this.payment.supplierCode = undefined;
    this.payment.supplierName = undefined;
    this.payment.amountPaid = undefined;
  }

  onUpdateSupplierSelect(event: any) {
    const supplierCode = event.target.value;
    this.filteredPurchaseOrders = this.purchaseOrders.filter(po => po.supplierCode == supplierCode);
    this.resetUpdatePaymentForm();
  }

  onUpdatePurchaseOrderSelect(event: any) {
    const poCode = event.target.value;
    const selectedPO = this.purchaseOrders.find(po => po.purchaseOrderCode == poCode);
    if (selectedPO) {
      this.updatePayment.purchaseOrderCode = selectedPO.purchaseOrderCode;
      this.updatePayment.supplierCode = selectedPO.supplierCode;
      this.updatePayment.supplierName = selectedPO.supplierName;
      this.updatePayment.amountPaid = selectedPO.totalAmount;
    }
  }

  resetUpdatePaymentForm() {
    this.updatePayment.purchaseOrderCode = undefined;
    this.updatePayment.supplierCode = undefined;
    this.updatePayment.supplierName = undefined;
    this.updatePayment.amountPaid = undefined;
  }

}
