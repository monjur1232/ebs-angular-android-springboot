import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Leave } from 'src/app/model/leave.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-leave',
  templateUrl: './leave.component.html',
  styleUrls: ['./leave.component.css']
})
export class LeaveComponent implements OnInit {
  url = 'http://localhost:8080/leave';
  leaves: Leave[] = [];
  leave: Leave = new Leave();
  updateLeave: Leave = new Leave();
  divStatus = false;

  currentView: 'all' | 'pending' | 'approved' | 'rejected' = 'all'; // Default

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAllLeaves();

  const employeeData = localStorage.getItem('currentEmployee');
  if (employeeData) {
    const currentEmployee = JSON.parse(employeeData);
    this.leave.employeeCode = currentEmployee.employeeCode;
    this.leave.employeeName = currentEmployee.firstName + ' ' + currentEmployee.lastName;
  }
  }

  getAllLeaves() {
    this.currentView = 'all';
    this.http.get<Leave[]>(this.url).subscribe(
      (response) => this.leaves = response
    );
  }

  saveLeave() {
    this.http.post(this.url, this.leave).subscribe(() => {
      this.getAllLeaves();
      this.leave = new Leave();

    const employeeData = localStorage.getItem('currentEmployee');
    if (employeeData) {
      const currentEmployee = JSON.parse(employeeData);
      this.leave.employeeCode = currentEmployee.employeeCode;
      this.leave.employeeName = currentEmployee.firstName + ' ' + currentEmployee.lastName;
    }

    });
  }

  editLeave(leave: Leave) {
    this.updateLeave = { ...leave };
    this.divStatus = true;
  }

  updateLeaveM() {
    this.http.put(`${this.url}/${this.updateLeave.id}`, this.updateLeave).subscribe(() => {
      this.getAllLeaves();
      this.divStatus = false;
    });
  }

  deleteLeave(id: number) {
    if (confirm('Are you sure you want to delete this leave?')) {
      this.http.delete(`${this.url}/${id}`).subscribe(() => this.getAllLeaves());
    }
  }

  approveLeave(id: number) {
  this.http.put(`${this.url}/approve/${id}`, {}).toPromise()
    .then(() => {
      Swal.fire('Approved!', 'Leave has been approved.', 'success');

      if (this.currentView === 'pending') this.getLeavesByStatus(0);
      else if (this.currentView === 'approved') this.getLeavesByStatus(1);
      else if (this.currentView === 'rejected') this.getLeavesByStatus(2);
      else this.getAllLeaves(); // currentView === 'all'
    })
    .catch(() => {
      Swal.fire('Error!', 'Failed to approve leave.', 'error');
    });
  }

  rejectLeave(id: number) {
  this.http.put(`${this.url}/reject/${id}`, {}).toPromise()
    .then(() => {
      Swal.fire('Rejected!', 'Leave has been rejected.', 'success');
      
      if (this.currentView === 'pending') this.getLeavesByStatus(0);
      else if (this.currentView === 'approved') this.getLeavesByStatus(1);
      else if (this.currentView === 'rejected') this.getLeavesByStatus(2);
      else this.getAllLeaves(); // currentView === 'all'
    })
    .catch(() => {
      Swal.fire('Error!', 'Failed to reject leave.', 'error');
    });
  }

  getLeavesByStatus(status: number) {


    if (status === 0) this.currentView = 'pending';
    else if (status === 1) this.currentView = 'approved';
    else if (status === 2) this.currentView = 'rejected';

    this.http.get<Leave[]>(`${this.url}/status/${status}`).subscribe(
      (response) => this.leaves = response
    );
  }
}