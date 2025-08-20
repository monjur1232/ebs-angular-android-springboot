import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Attendance } from 'src/app/model/attendance.model';

@Component({
  selector: 'app-attendance-report',
  templateUrl: './attendance-report.component.html',
  styleUrls: ['./attendance-report.component.css']
})
export class AttendanceReportComponent implements OnInit {
  url = 'http://localhost:8080/attendance';
  allAttendances: Attendance[] = [];
  filteredAttendances: Attendance[] = [];
  employeeSummaries: any[] = [];

  searchName: string = '';
  searchDate: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadAllAttendance();
  }

  loadAllAttendance() {
    this.http.get<Attendance[]>(this.url).subscribe((response) => {
      this.allAttendances = response;
      this.generateSummary(); // Summary তৈরির জন্য সবসময়ই call হবে
    });
  }

  generateSummary() {
    const summaryMap: { [key: string]: any } = {};

    this.allAttendances.forEach(att => {
      if (att.employeeCode === undefined || !att.employeeName) return;
      const key = att.employeeCode.toString();

      if (!summaryMap[key]) {
        summaryMap[key] = {
          employeeCode: att.employeeCode,
          employeeName: att.employeeName,
          totalDays: 0,
          presentDays: 0,
          absentDays: 0,
          lateDays: 0
        };
      }

      summaryMap[key].totalDays += 1;
      if (att.status === 'Present') summaryMap[key].presentDays += 1;
      else if (att.status === 'Absent') summaryMap[key].absentDays += 1;
      else if (att.status === 'Late') summaryMap[key].lateDays += 1;
    });

    this.employeeSummaries = Object.values(summaryMap);
  }

  searchReport() {
    this.filteredAttendances = this.allAttendances.filter(att => {
      const matchesName = !this.searchName || 
        (att.employeeName?.toLowerCase().includes(this.searchName.toLowerCase()));
      const matchesDate = !this.searchDate || 
        (att.date?.split('T')[0] === this.searchDate);
      return matchesName && matchesDate;
    });
  }
}
