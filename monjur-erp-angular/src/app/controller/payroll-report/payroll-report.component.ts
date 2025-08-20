import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Salary } from 'src/app/model/salary.model';
import { Employee } from 'src/app/model/employee.model';

@Component({
  selector: 'app-payroll-report',
  templateUrl: './payroll-report.component.html',
  styleUrls: ['./payroll-report.component.css']
})
export class PayrollReportComponent implements OnInit {

  salaries: Salary[] = [];
  employees: Employee[] = [];
  filteredSalaries: Salary[] = [];
  searchTerm: string = '';
  url = 'http://localhost:8080/salary';
  employeeUrl = 'http://localhost:8080/employee';

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAllSalaries();
    this.getAllEmployees();
  }

  getAllSalaries() {
    this.http.get<Salary[]>(this.url).subscribe(res => {
      this.salaries = res;
      this.filteredSalaries = res;
    });
  }

  getAllEmployees() {
    this.http.get<Employee[]>(this.employeeUrl).subscribe(res => {
      this.employees = res;
    });
  }

  filter() {
    const term = this.searchTerm.toLowerCase();
    this.filteredSalaries = this.salaries.filter(sal =>
      sal.employeeName?.toLowerCase().includes(term) ||
      sal.employeeCode?.toString().includes(term) ||
      sal.designationName?.toLowerCase().includes(term) ||
      sal.month?.toLowerCase().includes(term) ||
      sal.year?.toString().includes(term)
    );
  }

  get totalGrossSalary(): number {
    return this.filteredSalaries.reduce((sum, s) => sum + (s.grossSalary || 0), 0);
  }

  get totalDeductions(): number {
    return this.filteredSalaries.reduce((sum, s) => sum + (s.deductions || 0), 0);
  }

  get totalNetSalary(): number {
    return this.filteredSalaries.reduce((sum, s) => sum + (s.netSalary || 0), 0);
  }

}
