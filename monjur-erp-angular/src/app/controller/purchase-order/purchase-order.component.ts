import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PurchaseOrder } from 'src/app/model/purchase-order.model';
import { Supplier } from 'src/app/model/supplier.model';
import { Product } from 'src/app/model/product.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-purchase-order',
  templateUrl: './purchase-order.component.html',
  styleUrls: ['./purchase-order.component.css']
})
export class PurchaseOrderComponent implements OnInit {

  url = 'http://localhost:8080/purchase-order';
  purchaseOrders: PurchaseOrder[] = [];
  purchaseOrder: PurchaseOrder = new PurchaseOrder();
  updatePurchaseOrder: PurchaseOrder = new PurchaseOrder();
  divStatus = false;

  supplierUrl = 'http://localhost:8080/supplier';
  suppliers: Supplier[] = [];

  productUrl = 'http://localhost:8080/product';
  products: Product[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllSuppliers();
    this.getAllProducts();
    this.purchaseOrder.paymentStatus = 'Unpaid';
    this.purchaseOrder.status = 'Pending';
  }

  save(po: PurchaseOrder) {
    this.http.post(this.url, po).toPromise().then(() => {
      this.getAll();
      this.purchaseOrder = new PurchaseOrder();
      this.purchaseOrder.paymentStatus = 'Unpaid';
      this.purchaseOrder.status = 'Pending';

      Swal.fire('Saved!', 'Purchase order has been created.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to create purchase order.', 'error');
    });
  }

  getAll() {
    this.http.get<PurchaseOrder[]>(this.url).subscribe(
      (response) => this.purchaseOrders = response
    );
  }

  edit(po: PurchaseOrder) {
    this.updatePurchaseOrder = Object.assign({}, po);
    this.divStatus = true;
  }

  update(po: PurchaseOrder) {
    this.http.put(this.url + '/' + po.id, po).toPromise().then(() => {
      this.getAll();
      this.updatePurchaseOrder = new PurchaseOrder();
      this.divStatus = false;

      Swal.fire('Updated!', 'Purchase order has been updated.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update purchase order.', 'error');
    });
  }

  delete(po: PurchaseOrder) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this purchase order?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + po.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Purchase order has been deleted.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete purchase order.', 'error');
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

  getAllProducts() {
    this.http.get<Product[]>(this.productUrl).subscribe(
      (response) => {
        this.products = response;
      }
    );
  }

  onSupplierSelect(event: any) {
    const supplierCode = event.target.value;
    const selectedSupplier = this.suppliers.find(s => s.supplierCode == supplierCode);
    if (selectedSupplier) {
      this.purchaseOrder.supplierCode = selectedSupplier.supplierCode;
      this.purchaseOrder.supplierName = selectedSupplier.supplierName;
    }
  }

  onProductSelect(event: any) {
    const productCode = event.target.value;
    const selectedProduct = this.products.find(p => p.productCode == productCode);
    if (selectedProduct) {
      this.purchaseOrder.productCode = selectedProduct.productCode;
      this.purchaseOrder.productName = selectedProduct.productName;
    }
  }

  onUpdateSupplierSelect(event: any) {
    const supplierCode = event.target.value;
    const selectedSupplier = this.suppliers.find(s => s.supplierCode == supplierCode);
    if (selectedSupplier) {
      this.updatePurchaseOrder.supplierCode = selectedSupplier.supplierCode;
      this.updatePurchaseOrder.supplierName = selectedSupplier.supplierName;
    }
  }

  onUpdateProductSelect(event: any) {
    const productCode = event.target.value;
    const selectedProduct = this.products.find(p => p.productCode == productCode);
    if (selectedProduct) {
      this.updatePurchaseOrder.productCode = selectedProduct.productCode;
      this.updatePurchaseOrder.productName = selectedProduct.productName;
    }
  }

  calculateTotalAmount() {
    if (this.purchaseOrder.unitPrice && this.purchaseOrder.purchaseQuantity) {
      this.purchaseOrder.totalAmount = this.purchaseOrder.unitPrice * this.purchaseOrder.purchaseQuantity;
    } else {
      this.purchaseOrder.totalAmount = 0;
    }
  }

  calculateUpdateTotalAmount() {
    if (this.updatePurchaseOrder.unitPrice && this.updatePurchaseOrder.purchaseQuantity) {
      this.updatePurchaseOrder.totalAmount = this.updatePurchaseOrder.unitPrice * this.updatePurchaseOrder.purchaseQuantity;
    } else {
      this.updatePurchaseOrder.totalAmount = 0;
    }
  }

}
