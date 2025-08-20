import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Employee } from 'src/app/model/employee.model';
import { Department } from 'src/app/model/department.model';
import { Designation } from 'src/app/model/designation.model';
import Swal from 'sweetalert2';
declare var bootstrap: any;

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {

  selectedEmployee: Employee = new Employee();

  url = 'http://localhost:8080/employee';
  employees: Employee[] = [];
  employee: Employee = new Employee();
  updateEmployee: Employee = new Employee();
  divStatus = false;

  // For Department Name & Code Start
  deptUrl = 'http://localhost:8080/department';
  departments: Department[] = [];
  // For Department Name & Code End
  desgUrl = 'http://localhost:8080/designation';
  designations: Designation[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllDepartments();
    this.getAllDesignations();
    this.employee.gender = 'Male';
    this.employee.status = 'Active';
  }

  save(emp: Employee) {
    this.http.post(this.url, emp).toPromise().then(() => {
      this.getAll();
      this.employee = new Employee();
      this.employee.gender = 'Male';
      this.employee.status = 'Active';

    Swal.fire('Saved!', 'Employee has been added.', 'success');
  }).catch(() => {
    Swal.fire('Error!', 'Failed to add employee.', 'error');
  });
  }

  getAll() {
    this.http.get<Employee[]>(this.url).subscribe(
      (response) => this.employees = response
    );
  }

  edit(emp: Employee) {
    this.updateEmployee = Object.assign({}, emp);
    this.divStatus = true;
  }

  update(emp: Employee) {
    this.http.put(this.url + '/' + emp.id, emp).toPromise().then(() => {
      this.getAll();
      this.updateEmployee = new Employee();
      this.divStatus = false;

    Swal.fire('Updated!', 'Employee has been updated.', 'success');
  }).catch(() => {
    Swal.fire('Error!', 'Failed to update employee.', 'error');
  });
  }

  delete(emp: Employee) {
  Swal.fire({
    title: 'Are you sure?',
    text: 'Do you want to delete this employee?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Yes, delete it!',
    cancelButtonText: 'No, cancel',
  }).then((result) => {
    if (result.isConfirmed) {
      this.http.delete(this.url + '/' + emp.id).toPromise().then(() => {
        this.getAll();
        Swal.fire('Deleted!', 'Employee has been deleted.', 'success');
      }).catch(() => {
        Swal.fire('Error!', 'Failed to delete employee.', 'error');
      });
    }
  });
  }

  // For Department Name & Code Start
  // Add this new method to fetch departments
  getAllDepartments() {
    this.http.get<Department[]>(this.deptUrl).subscribe(
      (response) => {
        this.departments = response;
      }
    );
  }

  // Add this method to set department details when a department is selected
  onDepartmentSelect(event: any) {
    const deptCode = event.target.value;
    const selectedDept = this.departments.find(d => d.departmentCode == deptCode);
    if (selectedDept) {
      this.employee.departmentCode = selectedDept.departmentCode;
      this.employee.departmentName = selectedDept.departmentName;
    }
  }

  // Similar method for update form
  onUpdateDepartmentSelect(event: any) {
    const deptCode = event.target.value;
    const selectedDept = this.departments.find(d => d.departmentCode == deptCode);
    if (selectedDept) {
      this.updateEmployee.departmentCode = selectedDept.departmentCode;
      this.updateEmployee.departmentName = selectedDept.departmentName;
    }
  }
  // For Department Name & Code End

  getAllDesignations() {
    this.http.get<Designation[]>(this.desgUrl).subscribe(
      (response) => {
        this.designations = response;
      }
    );
  }

  onDesignationSelect(event: any) {
    const desgCode = event.target.value;
    const selectedDesg = this.designations.find(d => d.designationCode == desgCode);
    if (selectedDesg) {
      this.employee.designationCode = selectedDesg.designationCode;
      this.employee.designationName = selectedDesg.designationName;
    }
  }

  onUpdateDesignationSelect(event: any) {
    const desgCode = event.target.value;
    const selectedDesg = this.designations.find(d => d.designationCode == desgCode);
    if (selectedDesg) {
      this.updateEmployee.designationCode = selectedDesg.designationCode;
      this.updateEmployee.designationName = selectedDesg.designationName;
    }
  }

  openDetailsModal(emp: Employee) {

    this.selectedEmployee = emp;
    const modal = new bootstrap.Modal(document.getElementById('employeeDetailsModal')!);
    modal.show();
  }

}
