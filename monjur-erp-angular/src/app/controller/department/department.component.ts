import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Department } from 'src/app/model/department.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-department',
  templateUrl: './department.component.html',
  styleUrls: ['./department.component.css']
})
export class DepartmentComponent implements OnInit {

  url = 'http://localhost:8080/department';
  departments: Department[] = [];
  department: Department = new Department();
  updateDepartment: Department = new Department();
  divStatus = false;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
  }

  save(dept: Department) {
    this.http.post(this.url, dept).toPromise().then(() => {
      this.getAll();
      this.department = new Department();

      Swal.fire('Saved!', 'Department has been added.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to add department.', 'error');
    });
  }

  getAll() {
    this.http.get<Department[]>(this.url).subscribe(
      (response) => this.departments = response
    );
  }

  edit(dept: Department) {
    this.updateDepartment = Object.assign({}, dept);
    this.divStatus = true;
  }

  update(dept: Department) {
    this.http.put(this.url + '/' + dept.id, dept).toPromise().then(() => {
      this.getAll();
      this.updateDepartment = new Department();
      this.divStatus = false;

      Swal.fire('Updated!', 'Department has been updated.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update department.', 'error');
    });
  }

  delete(dept: Department) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this department?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + dept.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Department has been deleted.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete department.', 'error');
        });
      }
    });
  }
}
