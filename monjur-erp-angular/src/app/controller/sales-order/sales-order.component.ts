import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SalesOrder } from 'src/app/model/sales-order.model';
import { Customer } from 'src/app/model/customer.model';
import { Product } from 'src/app/model/product.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sales-order',
  templateUrl: './sales-order.component.html',
  styleUrls: ['./sales-order.component.css']
})
export class SalesOrderComponent implements OnInit {

  url = 'http://localhost:8080/sales-order';
  salesOrders: SalesOrder[] = [];
  salesOrder: SalesOrder = new SalesOrder();
  updateSalesOrder: SalesOrder = new SalesOrder();
  divStatus = false;

  customerUrl = 'http://localhost:8080/customer';
  customers: Customer[] = [];

  productUrl = 'http://localhost:8080/product';
  products: Product[] = [];  

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllCustomers();
    this.getAllProducts();    
    this.salesOrder.paymentStatus = 'Unpaid';
    this.salesOrder.status = 'Pending';
  }

  save(order: SalesOrder) {
    this.http.post(this.url, order).toPromise().then(() => {
      this.getAll();
      this.salesOrder = new SalesOrder();
      this.salesOrder.paymentStatus = 'Unpaid';
      this.salesOrder.status = 'Pending';

      Swal.fire('Saved!', 'Sales order has been created.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to create sales order.', 'error');
    });
  }

  getAll() {
    this.http.get<SalesOrder[]>(this.url).subscribe(
      (response) => this.salesOrders = response
    );
  }

  edit(order: SalesOrder) {
    this.updateSalesOrder = Object.assign({}, order);
    this.divStatus = true;
  }

  update(order: SalesOrder) {
    this.http.put(this.url + '/' + order.id, order).toPromise().then(() => {
      this.getAll();
      this.updateSalesOrder = new SalesOrder();
      this.divStatus = false;

      Swal.fire('Updated!', 'Sales order has been updated.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update sales order.', 'error');
    });
  }

  delete(order: SalesOrder) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this sales order?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + order.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Sales order has been deleted.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete sales order.', 'error');
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

  getAllProducts() {
    this.http.get<Product[]>(this.productUrl).subscribe(
      (response) => {
        this.products = response;
      }
    );
  }

  onCustomerSelect(event: any) {
    const customerCode = event.target.value;
    const selectedCustomer = this.customers.find(c => c.customerCode == customerCode);
    if (selectedCustomer) {
      this.salesOrder.customerCode = selectedCustomer.customerCode;
      this.salesOrder.customerName = selectedCustomer.customerName;
    }
  }

  onProductSelect(event: any) {
    const productCode = event.target.value;
    const selectedProduct = this.products.find(p => p.productCode == productCode);
    if (selectedProduct) {
      this.salesOrder.productCode = selectedProduct.productCode;
      this.salesOrder.productName = selectedProduct.productName;
    }
  }

  onUpdateCustomerSelect(event: any) {
    const customerCode = event.target.value;
    const selectedCustomer = this.customers.find(c => c.customerCode == customerCode);
    if (selectedCustomer) {
      this.updateSalesOrder.customerCode = selectedCustomer.customerCode;
      this.updateSalesOrder.customerName = selectedCustomer.customerName;
    }
  }

  onUpdateProductSelect(event: any) {
    const productCode = event.target.value;
    const selectedProduct = this.products.find(p => p.productCode == productCode);
    if (selectedProduct) {
      this.updateSalesOrder.productCode = selectedProduct.productCode;
      this.updateSalesOrder.productName = selectedProduct.productName;
    }
  }

  calculateTotalAmount() {
    if (this.salesOrder.unitPrice && this.salesOrder.salesQuantity) {
      this.salesOrder.totalAmount = this.salesOrder.unitPrice * this.salesOrder.salesQuantity;
    } else {
      this.salesOrder.totalAmount = 0;
    }
  }

  calculateUpdateTotalAmount() {
    if (this.updateSalesOrder.unitPrice && this.updateSalesOrder.salesQuantity) {
      this.updateSalesOrder.totalAmount = this.updateSalesOrder.unitPrice * this.updateSalesOrder.salesQuantity;
    } else {
      this.updateSalesOrder.totalAmount = 0;
    }
  }

}
