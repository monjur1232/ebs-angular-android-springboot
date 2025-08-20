import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SalaryStructure } from 'src/app/model/salary-structure.model';
import { Designation } from 'src/app/model/designation.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-salary-structure',
  templateUrl: './salary-structure.component.html',
  styleUrls: ['./salary-structure.component.css']
})
export class SalaryStructureComponent implements OnInit {

  url = 'http://localhost:8080/salary-structure';
  salaryStructures: SalaryStructure[] = [];
  salaryStructure: SalaryStructure = new SalaryStructure();
  updateSalaryStructure: SalaryStructure = new SalaryStructure();
  divStatus = false;

  desgUrl = 'http://localhost:8080/designation';
  designations: Designation[] = [];  

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllDesignations();
  }

  save(sal: SalaryStructure) {
    this.http.post(this.url, sal).toPromise().then(() => {
      this.getAll();
      this.salaryStructure = new SalaryStructure();

      Swal.fire('Saved!', 'Salary structure has been created successfully.', 'success');
    }).catch(error => {
      console.error('Error saving salary structure:', error);
      Swal.fire('Error!', 'Failed to create salary structure.', 'error');
    });
  }

  getAll() {
    this.http.get<SalaryStructure[]>(this.url).subscribe(
      (response) => this.salaryStructures = response
    );
  }

  edit(sal: SalaryStructure) {
    this.updateSalaryStructure = Object.assign({}, sal);
    this.divStatus = true;
  }

  update(sal: SalaryStructure) {
    this.http.put(this.url + '/' + sal.id, sal).toPromise().then(() => {
      this.getAll();
      this.updateSalaryStructure = new SalaryStructure();
      this.divStatus = false;

      Swal.fire('Updated!', 'Salary structure has been updated successfully.', 'success');
    }).catch(error => {
      console.error('Error updating salary structure:', error);
      Swal.fire('Error!', 'Failed to update salary structure.', 'error');
    });
  }

  delete(sal: SalaryStructure) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this salary structure?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + sal.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Salary structure has been deleted successfully.', 'success');
        }).catch(error => {
          console.error('Error deleting salary structure:', error);
          Swal.fire('Error!', 'Failed to delete salary structure.', 'error');
        });
      }
    });
  }

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
      this.salaryStructure.designationCode = selectedDesg.designationCode;
      this.salaryStructure.designationName = selectedDesg.designationName;
    }
  }

  onUpdateDesignationSelect(event: any) {
    const desgCode = event.target.value;
    const selectedDesg = this.designations.find(d => d.designationCode == desgCode);
    if (selectedDesg) {
      this.updateSalaryStructure.designationCode = selectedDesg.designationCode;
      this.updateSalaryStructure.designationName = selectedDesg.designationName;
    }
  }

  // Calculate all fields based on Basic Salary
  calculateSalaryFields() {
    if (this.salaryStructure.basicSalary) {
      this.salaryStructure.houseRent = Math.round(this.salaryStructure.basicSalary * 0.5);
      this.salaryStructure.medicalAllowance = Math.round(this.salaryStructure.basicSalary * 0.1);
      this.salaryStructure.transportAllowance = Math.round(this.salaryStructure.basicSalary * 0.15);
      this.calculateGrossSalary();
    }
  }

  // Calculate Gross Salary
  calculateGrossSalary() {
    const basic = this.salaryStructure.basicSalary || 0;
    const houseRent = this.salaryStructure.houseRent || 0;
    const medical = this.salaryStructure.medicalAllowance || 0;
    const transport = this.salaryStructure.transportAllowance || 0;
    const others = this.salaryStructure.others || 0;
    
    this.salaryStructure.grossSalary = Math.round(basic + houseRent + medical + transport + others);
  }

  // For update form
  calculateUpdateSalaryFields() {
    if (this.updateSalaryStructure.basicSalary) {
      this.updateSalaryStructure.houseRent = Math.round(this.updateSalaryStructure.basicSalary * 0.5);
      this.updateSalaryStructure.medicalAllowance = Math.round(this.updateSalaryStructure.basicSalary * 0.1);
      this.updateSalaryStructure.transportAllowance = Math.round(this.updateSalaryStructure.basicSalary * 0.15);
      this.calculateUpdateGrossSalary();
    }
  }

  // For update form
  calculateUpdateGrossSalary() {
    const basic = this.updateSalaryStructure.basicSalary || 0;
    const houseRent = this.updateSalaryStructure.houseRent || 0;
    const medical = this.updateSalaryStructure.medicalAllowance || 0;
    const transport = this.updateSalaryStructure.transportAllowance || 0;
    const others = this.updateSalaryStructure.others || 0;
    
    this.updateSalaryStructure.grossSalary = Math.round(basic + houseRent + medical + transport + others);
  }

}
