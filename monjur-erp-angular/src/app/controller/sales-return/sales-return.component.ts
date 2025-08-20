import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SalesReturn } from 'src/app/model/sales-return.model';
import { SalesOrder } from 'src/app/model/sales-order.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sales-return',
  templateUrl: './sales-return.component.html',
  styleUrls: ['./sales-return.component.css']
})
export class SalesReturnComponent implements OnInit {

  url = 'http://localhost:8080/sales-return';
  salesReturns: SalesReturn[] = [];
  salesReturn: SalesReturn = new SalesReturn();
  updateSalesReturn: SalesReturn = new SalesReturn();
  divStatus = false;

  soUrl = 'http://localhost:8080/sales-order';
  salesOrders: SalesOrder[] = [];    

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllSalesOrders();
    this.salesReturn.reason = 'Defective';
  }

  save(sr: SalesReturn) {
    this.http.post(this.url, sr).toPromise().then(() => {
      this.getAll();
      this.salesReturn = new SalesReturn();
      this.salesReturn.reason = 'Defective';

      Swal.fire('Saved!', 'Sales return has been processed.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to process sales return.', 'error');
    });
  }

  getAll() {
    this.http.get<SalesReturn[]>(this.url).subscribe(
      (response) => this.salesReturns = response
    );
  }

  edit(sr: SalesReturn) {
    this.updateSalesReturn = Object.assign({}, sr);
    this.divStatus = true;
  }

  update(sr: SalesReturn) {
    this.http.put(this.url + '/' + sr.id, sr).toPromise().then(() => {
      this.getAll();
      this.updateSalesReturn = new SalesReturn();
      this.divStatus = false;

      Swal.fire('Updated!', 'Sales return has been updated.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update sales return.', 'error');
    });
  }

  delete(sr: SalesReturn) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this return?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + sr.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Return has been deleted.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete return.', 'error');
        });
      }
    });
  }

  getAllSalesOrders() {
    this.http.get<SalesOrder[]>(this.soUrl).subscribe(
      (response) => {
        this.salesOrders = response;
      }
    );
  }

  onSalesOrderSelect(event: any) {
    const soCode = event.target.value;
    const selectedSO = this.salesOrders.find(so => so.salesOrderCode == soCode);
    if (selectedSO) {
      this.salesReturn.salesOrderCode = selectedSO.salesOrderCode;
      this.salesReturn.customerCode = selectedSO.customerCode;
      this.salesReturn.customerName = selectedSO.customerName;
      this.salesReturn.productCode = selectedSO.productCode;
      this.salesReturn.productName = selectedSO.productName;
      this.salesReturn.salesQuantity = selectedSO.salesQuantity;
    }
  }

  onUpdateSalesOrderSelect(event: any) {
    const soCode = event.target.value;
    const selectedSO = this.salesOrders.find(so => so.salesOrderCode == soCode);
    if (selectedSO) {
      this.updateSalesReturn.salesOrderCode = selectedSO.salesOrderCode;
      this.updateSalesReturn.customerCode = selectedSO.customerCode;
      this.updateSalesReturn.customerName = selectedSO.customerName;
      this.updateSalesReturn.productCode = selectedSO.productCode;
      this.updateSalesReturn.productName = selectedSO.productName;
      this.updateSalesReturn.salesQuantity = selectedSO.salesQuantity;
    }
  }

}
