import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Supplier } from 'src/app/model/supplier.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-supplier',
  templateUrl: './supplier.component.html',
  styleUrls: ['./supplier.component.css']
})
export class SupplierComponent implements OnInit {

  url = 'http://localhost:8080/supplier';
  suppliers: Supplier[] = [];
  supplier: Supplier = new Supplier();
  updateSupplier: Supplier = new Supplier();
  divStatus = false;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getAll();
    this.supplier.status = 'Active';
  }

  save(supp: Supplier) {
    this.http.post(this.url, supp).toPromise().then(() => {
      this.getAll();
      this.supplier = new Supplier();
      this.supplier.status = 'Active';

      Swal.fire('Saved!', 'Supplier has been added successfully.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to add supplier.', 'error');
    });
  }

  getAll() {
    this.http.get<Supplier[]>(this.url).subscribe(
      (response) => this.suppliers = response
    );
  }

  edit(supp: Supplier) {
    this.updateSupplier = Object.assign({}, supp);
    this.divStatus = true;
  }

  update(supp: Supplier) {
    this.http.put(this.url + '/' + supp.id, supp).toPromise().then(() => {
      this.getAll();
      this.updateSupplier = new Supplier();
      this.divStatus = false;

      Swal.fire('Updated!', 'Supplier has been updated successfully.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update supplier.', 'error');
    });
  }

  delete(supp: Supplier) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this supplier?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + supp.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Supplier has been deleted successfully.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete supplier.', 'error');
        });
      }
    });
  }
}
