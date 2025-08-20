import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Leave } from 'src/app/model/leave.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-leave-request',
  templateUrl: './leave-request.component.html',
  styleUrls: ['./leave-request.component.css']
})
export class LeaveRequestComponent implements OnInit {
  url = 'http://localhost:8080/leave';
  leaves: Leave[] = [];
  leave: Leave = new Leave();
  updateLeave: Leave = new Leave();
  divStatus = false;

  currentEmployeeCode: number | null = null;
  currentView: 'all' | 'pending' | 'approved' | 'rejected' = 'all';

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    const employeeData = localStorage.getItem('currentEmployee');
    if (employeeData) {
      const currentEmployee = JSON.parse(employeeData);
      this.leave.employeeCode = currentEmployee.employeeCode;
      this.leave.employeeName = currentEmployee.firstName + ' ' + currentEmployee.lastName;
      this.currentEmployeeCode = currentEmployee.employeeCode;
      this.getLeavesByEmployee();
    }
  }

  getAllLeaves() {
    this.currentView = 'all';
    if (this.currentEmployeeCode) {
      this.http.get<Leave[]>(`${this.url}/employee/${this.currentEmployeeCode}`).subscribe(
        (response) => this.leaves = response
      );
    }
  }

  getLeavesByEmployee() {
    if (this.currentEmployeeCode) {
      this.http.get<Leave[]>(`${this.url}/employee/${this.currentEmployeeCode}`).subscribe(
        (response) => this.leaves = response
      );
    }
  }

  saveLeave() {
    this.http.post(this.url, this.leave).toPromise()
      .then(() => {
        this.getLeavesByEmployee();
        this.leave = new Leave();


      const employeeData = localStorage.getItem('currentEmployee');
      if (employeeData) {
        const currentEmployee = JSON.parse(employeeData);
        this.leave.employeeCode = currentEmployee.employeeCode;
        this.leave.employeeName = currentEmployee.firstName + ' ' + currentEmployee.lastName;
      }

        Swal.fire('Saved!', 'Leave request has been submitted.', 'success');
      })
      .catch(() => {
        Swal.fire('Error!', 'Failed to submit leave request.', 'error');
      });
  }

  editLeave(leave: Leave) {
    this.updateLeave = { ...leave };
    this.divStatus = true;
  }

  updateLeaveM() {
    this.http.put(`${this.url}/${this.updateLeave.id}`, this.updateLeave).toPromise()
      .then(() => {
      this.getLeavesByEmployee();
      this.divStatus = false;

        Swal.fire('Updated!', 'Leave request updated successfully.', 'success');
      })
      .catch(() => {
        Swal.fire('Error!', 'Failed to update leave request.', 'error');
      });
  }

  deleteLeave(id: number) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this leave request?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(`${this.url}/${id}`).toPromise()
          .then(() => {
            this.getLeavesByEmployee();
            Swal.fire('Deleted!', 'Leave request deleted successfully.', 'success');
          })
          .catch(() => {
            Swal.fire('Error!', 'Failed to delete leave request.', 'error');
          });
      }
    });
  }

  approveLeave(id: number) {
    this.http.put(`${this.url}/approve/${id}`, {}).subscribe(() => {
      alert('Leave Approved!');
      this.getLeavesByEmployee();
    });
  }

  rejectLeave(id: number) {
    this.http.put(`${this.url}/reject/${id}`, {}).subscribe(() => {
      alert('Leave Rejected!');
      this.getLeavesByEmployee();
    });
  }

  getLeavesByStatus(status: number) {
    if (status === 0) this.currentView = 'pending';
    else if (status === 1) this.currentView = 'approved';
    else if (status === 2) this.currentView = 'rejected';

    if (this.currentEmployeeCode) {
      this.http.get<Leave[]>(`${this.url}/employee/${this.currentEmployeeCode}`).subscribe(
        (response) => {
          this.leaves = response.filter(leave => leave.status === status);
        }
      );
    }
  }
}