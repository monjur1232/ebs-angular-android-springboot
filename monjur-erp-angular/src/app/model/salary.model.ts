export class Salary {
  id: number | undefined;
  salaryCode: number | undefined;
  employeeCode: number | undefined;
  employeeName: string | undefined;
  designationCode: number | undefined;
  designationName: String | undefined;
  month: string | undefined;
  year: number | undefined;
  salaryStructureCode: number | undefined;
  basicSalary!: number;
  houseRent!: number;
  medicalAllowance!: number;
  transportAllowance!: number;
  others!: number;
  grossSalary!: number;
  deductions!: number;
  netSalary!: number;
  payDate!: string;
  paymentMode: string | undefined;
  paymentRef: string | undefined;
}
