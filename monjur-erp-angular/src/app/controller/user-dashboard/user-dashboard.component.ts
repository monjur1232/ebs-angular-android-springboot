import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css']
})
export class UserDashboardComponent implements OnInit {

  employeeName: string = '';
  currentEmployee: any = {};

  constructor() { }

  ngOnInit(): void {
    const employeeData = localStorage.getItem('currentEmployee');
    if (employeeData) {
      this.currentEmployee = JSON.parse(employeeData);
      this.employeeName = this.currentEmployee.firstName + ' ' + this.currentEmployee.lastName;
    }    
  }

  getYearsOfService(): string {
    if (!this.currentEmployee?.hireDate) return '0';
    
    const hireDate = new Date(this.currentEmployee.hireDate);
    const today = new Date();
    const years = today.getFullYear() - hireDate.getFullYear();
    
    // Adjust if anniversary hasn't occurred yet this year
    if (today.getMonth() < hireDate.getMonth() || 
        (today.getMonth() === hireDate.getMonth() && today.getDate() < hireDate.getDate())) {
      return (years - 1).toString();
    }
    
    return years.toString();
  }
  
}
