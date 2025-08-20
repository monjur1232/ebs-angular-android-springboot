export class Attendance {
  id: number | undefined;
  attendanceCode: number | undefined;
  employeeCode!: number;
  employeeName: string | undefined;
  date: string | undefined;
  inTime: string | undefined;
  outTime: string | undefined;
  status: string | undefined;
}
