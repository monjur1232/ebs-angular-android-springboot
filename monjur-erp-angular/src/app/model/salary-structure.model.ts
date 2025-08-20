export class SalaryStructure {
  id: number | undefined;
  salaryStructureCode: number | undefined;
  designationCode: number | undefined;
  designationName: string | undefined;
  basicSalary!: number;
  houseRent!: number;
  medicalAllowance!: number;
  transportAllowance!: number;
  others!: number;
  grossSalary!: number;
}
