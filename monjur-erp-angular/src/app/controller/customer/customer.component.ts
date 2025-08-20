import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Customer } from 'src/app/model/customer.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})
export class CustomerComponent implements OnInit {

  url = 'http://localhost:8080/customer';
  customers: Customer[] = [];
  customer: Customer = new Customer();
  updateCustomer: Customer = new Customer();
  divStatus = false;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.customer.status = 'Active';
  }

  save(cust: Customer) {
    this.http.post(this.url, cust).toPromise().then(() => {
      this.getAll();
      this.customer = new Customer();
      this.customer.status = 'Active';

      Swal.fire('Saved!', 'Customer has been added.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to add customer.', 'error');
    });
  }

  getAll() {
    this.http.get<Customer[]>(this.url).subscribe(
      (response) => this.customers = response
    );
  }

  edit(cust: Customer) {
    this.updateCustomer = Object.assign({}, cust);
    this.divStatus = true;
  }

  update(cust: Customer) {
    this.http.put(this.url + '/' + cust.id, cust).toPromise().then(() => {
      this.getAll();
      this.updateCustomer = new Customer();
      this.divStatus = false;

      Swal.fire('Updated!', 'Customer has been updated.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update customer.', 'error');
    });
  }

  delete(cust: Customer) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this customer?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + cust.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Customer has been deleted.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete customer.', 'error');
        });
      }
    });
  }
}
