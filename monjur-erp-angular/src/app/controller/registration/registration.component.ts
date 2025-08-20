import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Registration } from 'src/app/model/registration.model';
import { Employee } from 'src/app/model/employee.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  url = 'http://localhost:8080/registration';
  registrations: Registration[] = [];
  registration: Registration = new Registration();
  updateRegistration: Registration = new Registration();
  divStatus = false;

  employeeUrl = 'http://localhost:8080/employee';
  employees: Employee[] = [];  

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllEmployees();
  }

  save(reg: Registration) {
    this.http.post(this.url, reg).toPromise().then(() => {
      this.getAll();
      this.registration = new Registration();
      
      Swal.fire('Saved!', 'Registration has been created successfully.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to create registration.', 'error');
    });
  }

  getAll() {
    this.http.get<Registration[]>(this.url).subscribe(
      (response) => this.registrations = response
    );
  }

  edit(reg: Registration) {
    this.updateRegistration = Object.assign({}, reg);
    this.divStatus = true;
  }

  update(reg: Registration) {
    this.http.put(this.url + '/' + reg.id, reg).toPromise().then(() => {
      this.getAll();
      this.updateRegistration = new Registration();
      this.divStatus = false;

      Swal.fire('Updated!', 'Registration has been updated successfully.', 'success');
    }).catch(() => {
      Swal.fire('Error!', 'Failed to update registration.', 'error');
    });
  }

  delete(reg: Registration) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this registration?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + reg.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Registration has been deleted successfully.', 'success');
        }).catch(() => {
          Swal.fire('Error!', 'Failed to delete registration.', 'error');
        });
      }
    });
  }

  getAllEmployees() {
    this.http.get<Employee[]>(this.employeeUrl).subscribe(
      (response) => {
        this.employees = response;
      }
    );
  }

  onEmployeeSelect(event: any) {
    const empCode = event.target.value;
    const selectedEmp = this.employees.find(e => e.employeeCode == empCode);
    if (selectedEmp) {
      this.registration.employeeCode = selectedEmp.employeeCode;
      this.registration.employeeName = selectedEmp.firstName + ' ' + selectedEmp.lastName;
    }
  }

  onUpdateEmployeeSelect(event: any) {
    const empCode = event.target.value;
    const selectedEmp = this.employees.find(e => e.employeeCode == empCode);
    if (selectedEmp) {
      this.updateRegistration.employeeCode = selectedEmp.employeeCode;
      this.updateRegistration.employeeName = selectedEmp.firstName + ' ' + selectedEmp.lastName;
    }
  }

}
