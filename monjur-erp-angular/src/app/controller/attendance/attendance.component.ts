import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Attendance } from 'src/app/model/attendance.model';
import { Employee } from 'src/app/model/employee.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-attendance',
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.css']
})
export class AttendanceComponent implements OnInit {
  url = 'http://localhost:8080/attendance';
  attendances: Attendance[] = [];
  filteredAttendances: Attendance[] = [];
  attendance: Attendance = new Attendance();
  updateAttendance: Attendance = new Attendance();
  divStatus = false;

  employeeUrl = 'http://localhost:8080/employee';
  employees: Employee[] = [];

  // Bulk attendance properties
  bulkDate: string = new Date().toISOString().split('T')[0];
  bulkAttendanceCode: number | undefined;
  empAttendanceMap: { [key: number]: { inTime: string, outTime: string, status: string } } = {};
  
  // Filter properties
  searchText: string = '';
  filterDate: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getAll();
    this.getAllEmployees();
  }

  // Existing methods
  save(att: Attendance) {
    this.http.post(this.url, att).toPromise().then(() => {
      this.getAll();
      this.attendance = new Attendance();

      Swal.fire('Success!', 'Attendance record has been saved.', 'success');
    }).catch(error => {
      Swal.fire('Error!', 'Failed to save attendance record.', 'error');
    });
  }

  getAll() {
    this.http.get<Attendance[]>(this.url).subscribe(
      (response) => {
        this.attendances = response;
        this.filteredAttendances = [...this.attendances];
      }
    );
  }

  edit(att: Attendance) {
    this.updateAttendance = Object.assign({}, att);
    this.divStatus = true;
  }

  update(att: Attendance) {
    this.http.put(this.url + '/' + att.id, att).toPromise().then(() => {
      this.getAll();
      this.updateAttendance = new Attendance();
      this.divStatus = false;

      Swal.fire('Success!', 'Attendance record has been updated.', 'success');
    }).catch(error => {
      Swal.fire('Error!', 'Failed to update attendance record.', 'error');
    });
  }

  delete(att: Attendance) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You won\'t be able to revert this!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + att.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Attendance record has been deleted.', 'success');
        }).catch(error => {
          Swal.fire('Error!', 'Failed to delete attendance record.', 'error');
        });
      }
    });
  }

  getAllEmployees() {
    this.http.get<Employee[]>(this.employeeUrl).subscribe(
      (response) => {
        this.employees = response;
        this.initializeAttendanceMap();
      }
    );
  }

  onEmployeeSelect(event: any) {
    const empCode = event.target.value;
    const selectedEmp = this.employees.find(e => e.employeeCode == empCode);
    if (selectedEmp) {
      this.attendance.employeeCode = selectedEmp.employeeCode;
      this.attendance.employeeName = selectedEmp.firstName + ' ' + selectedEmp.lastName;
    }
  }

  onUpdateEmployeeSelect(event: any) {
    const empCode = event.target.value;
    const selectedEmp = this.employees.find(e => e.employeeCode == empCode);
    if (selectedEmp) {
      this.updateAttendance.employeeCode = selectedEmp.employeeCode;
      this.updateAttendance.employeeName = selectedEmp.firstName + ' ' + selectedEmp.lastName;
    }
  }

  // Bulk attendance methods
  initializeAttendanceMap() {
    this.empAttendanceMap = {};
    this.employees.forEach(emp => {
      this.empAttendanceMap[emp.employeeCode] = {
        inTime: '09:00',
        outTime: '17:00',
        status: 'Present'
      };
    });
  }

  saveBulkAttendance() {

    const attendances: Attendance[] = [];
    
    this.employees.forEach(emp => {
      const attData = this.empAttendanceMap[emp.employeeCode];
      const attendance = new Attendance();
      
      attendance.attendanceCode = this.bulkAttendanceCode;
      attendance.employeeCode = emp.employeeCode;
      attendance.employeeName = emp.firstName + ' ' + emp.lastName;
      attendance.date = this.bulkDate;
      attendance.inTime = attData.inTime;
      attendance.outTime = attData.outTime;
      attendance.status = attData.status;
      
      attendances.push(attendance);
      
    });

    Swal.fire({
      title: 'Confirm Bulk Attendance',
      text: `You are about to save attendance for ${attendances.length} employees. Proceed?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Yes, save it!',
      cancelButtonText: 'No, cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.post(this.url + '/bulk', attendances).toPromise().then(() => {
          Swal.fire('Success!', 'Bulk attendance saved successfully!', 'success');
          this.getAll();
          this.resetForm();
        }).catch(error => {
          console.error('Error saving bulk attendance:', error);
          Swal.fire('Error!', 'Failed to save bulk attendance.', 'error');
        });
      }
    });
  }

  onStatusChange(empCode: number) {
    if (this.empAttendanceMap[empCode].status === 'Absent') {
      this.empAttendanceMap[empCode].inTime = '';
      this.empAttendanceMap[empCode].outTime = '';
    } else {
      this.empAttendanceMap[empCode].inTime = '09:00';
      this.empAttendanceMap[empCode].outTime = '17:00';
    }
  }

  markAllAsPresent() {
    this.employees.forEach(emp => {
      this.empAttendanceMap[emp.employeeCode].status = 'Present';
      this.empAttendanceMap[emp.employeeCode].inTime = '09:00';
      this.empAttendanceMap[emp.employeeCode].outTime = '17:00';
    });
  }

  markAllAsAbsent() {
    this.employees.forEach(emp => {
      this.empAttendanceMap[emp.employeeCode].status = 'Absent';
      this.empAttendanceMap[emp.employeeCode].inTime = '';
      this.empAttendanceMap[emp.employeeCode].outTime = '';
    });
  }

  resetAllTimes() {
    this.employees.forEach(emp => {
      if (this.empAttendanceMap[emp.employeeCode].status !== 'Absent') {
        this.empAttendanceMap[emp.employeeCode].inTime = '09:00';
        this.empAttendanceMap[emp.employeeCode].outTime = '17:00';
      }
    });
  }

  clearEmployeeAttendance(empCode: number) {
    this.empAttendanceMap[empCode] = {
      inTime: '09:00',
      outTime: '17:00',
      status: 'Present'
    };
  }

  resetForm() {
    this.bulkAttendanceCode = undefined;
    this.initializeAttendanceMap();
  }

  // Filter methods
  filterAttendances() {
    this.filteredAttendances = this.attendances.filter(att => {
      const matchesSearch = !this.searchText || 
        (att.employeeName?.toLowerCase().includes(this.searchText.toLowerCase()) || 
         att.employeeCode?.toString().includes(this.searchText) ||
         att.attendanceCode?.toString().includes(this.searchText));
      
      const matchesDate = !this.filterDate || 
        (att.date && new Date(att.date).toISOString().split('T')[0] === this.filterDate);
      
      return matchesSearch && matchesDate;
    });
  }

  setStatus(empCode: number, status: string) {
    this.empAttendanceMap[empCode].status = status;
    
    if (status === 'Absent') {
      this.empAttendanceMap[empCode].inTime = '';
      this.empAttendanceMap[empCode].outTime = '';
    } else {
      this.empAttendanceMap[empCode].inTime = '09:00';
      this.empAttendanceMap[empCode].outTime = '17:00';
    }
  }

}
