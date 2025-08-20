import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SalesPayment } from 'src/app/model/sales-payment.model';
import { Customer } from 'src/app/model/customer.model';
import { SalesOrder } from 'src/app/model/sales-order.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sales-payment',
  templateUrl: './sales-payment.component.html',
  styleUrls: ['./sales-payment.component.css']
})
export class SalesPaymentComponent implements OnInit {

  url = 'http://localhost:8080/sales-payment';
  salesPayments: SalesPayment[] = [];
  salesPayment: SalesPayment = new SalesPayment();
  updateSalesPayment: SalesPayment = new SalesPayment();
  divStatus = false;

  customerUrl = 'http://localhost:8080/customer';
  customers: Customer[] = [];
  
  salesOrderUrl = 'http://localhost:8080/sales-order';
  salesOrders: SalesOrder[] = [];
  filteredSalesOrders: SalesOrder[] = [];  

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllCustomers();
    this.getAllSalesOrders();
    this.salesPayment.paymentMode = 'Bank Transfer';
  }

  save(p: SalesPayment) {
    this.http.post(this.url, p).toPromise().then(() => {
      this.getAll();
      this.salesPayment = new SalesPayment();
      this.filteredSalesOrders = [];
      this.resetCustomerDropdown();
      this.salesPayment.paymentMode = 'Bank Transfer';

      Swal.fire('Saved!', 'Sales payment has been processed.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to process sales payment.', 'error');
    });
  }

  resetCustomerDropdown() {
    const customerSelect = document.querySelector('select[name="customer"]') as HTMLSelectElement;
    if (customerSelect) {
      customerSelect.selectedIndex = 0;
    }
  }

  getAll() {
    this.http.get<SalesPayment[]>(this.url).subscribe(
      (response) => this.salesPayments = response
    );
  }

  edit(p: SalesPayment) {
    this.updateSalesPayment = Object.assign({}, p);
    this.divStatus = true;

    if (this.updateSalesPayment.customerCode) {
      this.filteredSalesOrders = this.salesOrders.filter(so => so.customerCode == this.updateSalesPayment.customerCode);
    }
  }

  update(payment: SalesPayment) {
    this.http.put(this.url + '/' + payment.id, payment).toPromise().then(() => {
      this.getAll();
      this.updateSalesPayment = new SalesPayment();
      this.divStatus = false;

      Swal.fire('Updated!', 'Sales payment has been updated.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update sales payment.', 'error');
    });
  }

  delete(payment: SalesPayment) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this payment?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + payment.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Payment has been deleted.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete payment.', 'error');
        });
      }
    });
  }

  getAllCustomers() {
    this.http.get<Customer[]>(this.customerUrl).subscribe(
      (response) => {
        this.customers = response;
      }
    );
  }

  getAllSalesOrders() {
    this.http.get<SalesOrder[]>(this.salesOrderUrl).subscribe(
      (response) => {
        this.salesOrders = response;
      }
    );
  }

  onCustomerSelect(event: any) {
    const customerCode = event.target.value;
    this.filteredSalesOrders = this.salesOrders.filter(so => so.customerCode == customerCode);
    this.resetPaymentForm();
  }

  onSalesOrderSelect(event: any) {
    const salesOrderCode = event.target.value;
    const selectedSO = this.salesOrders.find(so => so.salesOrderCode == salesOrderCode);
    if (selectedSO) {
      this.salesPayment.salesOrderCode = selectedSO.salesOrderCode;
      this.salesPayment.customerCode = selectedSO.customerCode;
      this.salesPayment.customerName = selectedSO.customerName;
      this.salesPayment.amountPaid = selectedSO.totalAmount;
    }
  }

  resetPaymentForm() {
    this.salesPayment.salesOrderCode = undefined;
    this.salesPayment.customerCode = undefined;
    this.salesPayment.customerName = undefined;
    this.salesPayment.amountPaid = undefined;
  }

  onUpdateCustomerSelect(event: any) {
    const customerCode = event.target.value;
    this.filteredSalesOrders = this.salesOrders.filter(so => so.customerCode == customerCode);
    this.resetUpdatePaymentForm();
  }

  onUpdateSalesOrderSelect(event: any) {
    const salesOrderCode = event.target.value;
    const selectedSO = this.salesOrders.find(so => so.salesOrderCode == salesOrderCode);
    if (selectedSO) {
      this.updateSalesPayment.salesOrderCode = selectedSO.salesOrderCode;
      this.updateSalesPayment.customerCode = selectedSO.customerCode;
      this.updateSalesPayment.customerName = selectedSO.customerName;
      this.updateSalesPayment.amountPaid = selectedSO.totalAmount;
    }
  }

  resetUpdatePaymentForm() {
    this.updateSalesPayment.salesOrderCode = undefined;
    this.updateSalesPayment.customerCode = undefined;
    this.updateSalesPayment.customerName = undefined;
    this.updateSalesPayment.amountPaid = undefined;
  }

}
