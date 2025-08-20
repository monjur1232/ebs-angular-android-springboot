import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Return } from 'src/app/model/return.model';
import { PurchaseOrder } from 'src/app/model/purchase-order.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-return',
  templateUrl: './return.component.html',
  styleUrls: ['./return.component.css']
})
export class ReturnComponent implements OnInit {

  url = 'http://localhost:8080/return';
  returns: Return[] = [];
  return: Return = new Return();
  updateReturn: Return = new Return();
  divStatus = false;

  poUrl = 'http://localhost:8080/purchase-order';
  purchaseOrders: PurchaseOrder[] = [];  

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllPurchaseOrders();
    this.return.reason = 'Defective';
  }

  save(ret: Return) {
    this.http.post(this.url, ret).toPromise().then(() => {
      this.getAll();
      this.return = new Return();
      this.return.reason = 'Defective';

      Swal.fire('Saved!', 'Return has been processed successfully.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to process return.', 'error');
    });
  }

  getAll() {
    this.http.get<Return[]>(this.url).subscribe(
      (response) => this.returns = response
    );
  }

  edit(ret: Return) {
    this.updateReturn = Object.assign({}, ret);
    this.divStatus = true;
  }

  update(ret: Return) {
    this.http.put(this.url + '/' + ret.id, ret).toPromise().then(() => {
      this.getAll();
      this.updateReturn = new Return();
      this.divStatus = false;

      Swal.fire('Updated!', 'Return has been updated successfully.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update return.', 'error');
    });
  }

  delete(ret: Return) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this return record?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + ret.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Return record has been deleted successfully.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete return record.', 'error');
        });
      }
    });
  }

  getAllPurchaseOrders() {
    this.http.get<PurchaseOrder[]>(this.poUrl).subscribe(
      (response) => {
        this.purchaseOrders = response;
      }
    );
  }

  onPurchaseOrderSelect(event: any) {
    const poCode = event.target.value;
    const selectedPO = this.purchaseOrders.find(po => po.purchaseOrderCode == poCode);
    if (selectedPO) {
      this.return.purchaseOrderCode = selectedPO.purchaseOrderCode;
      this.return.supplierCode = selectedPO.supplierCode;
      this.return.supplierName = selectedPO.supplierName;
      this.return.productCode = selectedPO.productCode;
      this.return.productName = selectedPO.productName;
      this.return.purchaseQuantity = selectedPO.purchaseQuantity;
    }
  }

  onUpdatePurchaseOrderSelect(event: any) {
    const poCode = event.target.value;
    const selectedPO = this.purchaseOrders.find(po => po.purchaseOrderCode == poCode);
    if (selectedPO) {
      this.updateReturn.purchaseOrderCode = selectedPO.purchaseOrderCode;
      this.updateReturn.supplierCode = selectedPO.supplierCode;
      this.updateReturn.supplierName = selectedPO.supplierName;
      this.updateReturn.productCode = selectedPO.productCode;
      this.updateReturn.productName = selectedPO.productName;
      this.updateReturn.purchaseQuantity = selectedPO.purchaseQuantity;
    }
  }

}
