import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Salary } from 'src/app/model/salary.model';
import { Employee } from 'src/app/model/employee.model';
import { SalaryStructure } from 'src/app/model/salary-structure.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-salary',
  templateUrl: './salary.component.html',
  styleUrls: ['./salary.component.css']
})
export class SalaryComponent implements OnInit {

  url = 'http://localhost:8080/salary';
  salaries: Salary[] = [];
  salary: Salary = new Salary();
  updateSalary: Salary = new Salary();
  divStatus = false;

  employeeUrl = 'http://localhost:8080/employee';
  salaryStructureUrl = 'http://localhost:8080/salary-structure';
  attendanceUrl = 'http://localhost:8080/attendance';
  leaveUrl = 'http://localhost:8080/leave';
  employees: Employee[] = [];
  salaryStructures: SalaryStructure[] = [];

  months = ['January', 'February', 'March', 'April', 'May', 'June', 
           'July', 'August', 'September', 'October', 'November', 'December'];
  years = [2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030];
  selectedMonth: string = 'May';
  selectedYear: number = 2025;
  processedSalaries: any[] = [];
  isProcessing = false;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getAll();
    this.getAllEmployees();
    this.getAllSalaryStructures();
    this.salary.month = 'May';
    this.salary.year = 2025;
    this.salary.paymentMode = 'Bank Transfer';
  }

  save(s: Salary) {
    this.http.post(this.url, s).toPromise().then(() => {
      this.getAll();
      this.salary = new Salary();
      this.salary.month = 'May';
      this.salary.year = 2025;
      this.salary.paymentMode = 'Bank Transfer';

      Swal.fire('Saved!', 'Salary record has been processed successfully.', 'success');
    }).catch(error => {
      console.error('Error saving salary:', error);
      Swal.fire('Error!', 'Failed to process salary record.', 'error');
    });
  }

  getAll() {
    this.http.get<Salary[]>(this.url).subscribe(
      (response) => this.salaries = response
    );
  }

  edit(s: Salary) {
    this.updateSalary = Object.assign({}, s);
    this.divStatus = true;
  }

  update(s: Salary) {
    this.http.put(this.url + '/' + s.id, s).toPromise().then(() => {
      this.getAll();
      this.updateSalary = new Salary();
      this.divStatus = false;

      Swal.fire('Updated!', 'Salary record has been updated successfully.', 'success');
    }).catch(error => {
      console.error('Error updating salary:', error);
      Swal.fire('Error!', 'Failed to update salary record.', 'error');
    });
  }

  delete(s: Salary) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this salary record?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(this.url + '/' + s.id).toPromise().then(() => {
          this.getAll();
          Swal.fire('Deleted!', 'Salary record has been deleted successfully.', 'success');
        }).catch(error => {
          console.error('Error deleting salary:', error);
          Swal.fire('Error!', 'Failed to delete salary record.', 'error');
        });
      }
    });
  }

  getAllEmployees() {
    this.http.get<Employee[]>(this.employeeUrl).subscribe(
      (response) => this.employees = response
    );
  }

  getAllSalaryStructures() {
    this.http.get<SalaryStructure[]>(this.salaryStructureUrl).subscribe(
      (response) => this.salaryStructures = response
    );
  }

  async processPayroll() {
    this.isProcessing = true;
    this.processedSalaries = [];
    
    Swal.fire({
      title: 'Processing Payroll',
      html: 'Please wait while we calculate salaries for all employees...',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    try {
      for (const emp of this.employees) {
        const exists = await this.http.get<boolean>(
          `${this.url}/exists/employee/${emp.employeeCode}/month/${this.selectedMonth}/year/${this.selectedYear}`
        ).toPromise();

        if (!exists) {
          const salaryData = await this.calculateEmployeeSalary(emp);
          this.processedSalaries.push(salaryData);
        }
      }
      
      Swal.close();
      if (this.processedSalaries.length > 0) {
        Swal.fire('Success!', 'Payroll processed successfully', 'success');
      } else {
        Swal.fire('Info', 'All employees already have salary records for the selected period', 'info');
      }
    } catch (error) {
      console.error('Error processing payroll:', error);
      Swal.fire('Error!', 'Failed to process payroll', 'error');
    } finally {
      this.isProcessing = false;
    }
  }

  async calculateEmployeeSalary(emp: Employee): Promise<any> {
    const salaryStructure = this.salaryStructures.find(
      s => s.designationName === emp.designationName
    );
    
    if (!salaryStructure) {
      throw new Error(`Salary structure not found for ${emp.designationName}`);
    }
    
    const absentResponse: any = await this.http.get(
      `${this.attendanceUrl}/absent-count/${emp.employeeCode}?month=${this.selectedMonth}&year=${this.selectedYear}`
    ).toPromise();
    
    const leaveResponse: any = await this.http.get(
      `${this.leaveUrl}/approved-leave-count/${emp.employeeCode}?month=${this.selectedMonth}&year=${this.selectedYear}`
    ).toPromise();
    
    const totalAbsent = absentResponse.absentCount || 0;
    const approvedLeave = leaveResponse.approvedLeaveCount || 0;
    const netAbsentDays = Math.max(0, totalAbsent - approvedLeave);
    const perDaySalary = salaryStructure.basicSalary / 24;
    const deductions = Math.round(perDaySalary * netAbsentDays);
    const netSalary = salaryStructure.grossSalary - deductions;
    
    return {
      employeeCode: emp.employeeCode,
      employeeName: `${emp.firstName} ${emp.lastName}`,
      designationName: emp.designationName,
      designationCode: emp.designationCode,
      basicSalary: salaryStructure.basicSalary,
      houseRent: salaryStructure.houseRent,
      medicalAllowance: salaryStructure.medicalAllowance,
      transportAllowance: salaryStructure.transportAllowance,
      others: salaryStructure.others,
      grossSalary: salaryStructure.grossSalary,
      totalAbsent: totalAbsent,
      approvedLeave: approvedLeave,
      deductions: deductions,
      netSalary: netSalary,
      month: this.selectedMonth,
      year: this.selectedYear.toString(),
      salaryStructureCode: salaryStructure.salaryStructureCode,
      paymentMode: 'Bank Transfer',
      payDate: new Date().toISOString().split('T')[0]
    };
  }

  getTotal(property: string): number {
    return this.processedSalaries.reduce((sum, sal) => sum + (sal[property] || 0), 0);
  }

  async saveAllSalaries() {
    if (this.processedSalaries.length === 0) {
      Swal.fire('Warning!', 'No payroll data to save', 'warning');
      return;
    }
    
    try {
      await this.http.post(`${this.url}/bulk`, this.processedSalaries).toPromise();
      Swal.fire('Success!', 'All salary records saved successfully', 'success');
      this.processedSalaries = [];
      this.getAll(); // Refresh the salary records table
    } catch (error) {
      console.error('Error saving salaries:', error);
      Swal.fire('Error!', 'Failed to save salary records', 'error');
    }
  }

  onEmployeeSelect(event: any) {
    const empCode = event.target.value;
    const selectedEmp = this.employees.find(e => e.employeeCode == empCode);
    
    if (selectedEmp) {
      this.salary.employeeCode = selectedEmp.employeeCode;
      this.salary.employeeName = selectedEmp.firstName + ' ' + selectedEmp.lastName;
      this.salary.designationCode = selectedEmp.designationCode;
      this.salary.designationName = selectedEmp.designationName;
      
      const selectedSalaryStructure = this.salaryStructures.find(
        s => s.designationName === selectedEmp.designationName
      );
      
      if (selectedSalaryStructure) {
        this.salary.salaryStructureCode = selectedSalaryStructure.salaryStructureCode;
        this.salary.basicSalary = selectedSalaryStructure.basicSalary;
        this.salary.houseRent = selectedSalaryStructure.houseRent;
        this.salary.medicalAllowance = selectedSalaryStructure.medicalAllowance;
        this.salary.transportAllowance = selectedSalaryStructure.transportAllowance;
        this.salary.others = selectedSalaryStructure.others;
        this.salary.grossSalary = selectedSalaryStructure.grossSalary;
        
        this.calculateDeductions(selectedEmp.employeeCode);
      }
    }
  }

  onEmployeeSelectForUpdate(event: any) {
    const empCode = event.target.value;
    const selectedEmp = this.employees.find(e => e.employeeCode == empCode);
    
    if (selectedEmp) {
      this.updateSalary.employeeCode = selectedEmp.employeeCode;
      this.updateSalary.employeeName = selectedEmp.firstName + ' ' + selectedEmp.lastName;
      this.updateSalary.designationCode = selectedEmp.designationCode;
      this.updateSalary.designationName = selectedEmp.designationName;
      
      const selectedSalaryStructure = this.salaryStructures.find(
        s => s.designationName === selectedEmp.designationName
      );
      
      if (selectedSalaryStructure) {
        this.updateSalary.salaryStructureCode = selectedSalaryStructure.salaryStructureCode;
        this.updateSalary.basicSalary = selectedSalaryStructure.basicSalary;
        this.updateSalary.houseRent = selectedSalaryStructure.houseRent;
        this.updateSalary.medicalAllowance = selectedSalaryStructure.medicalAllowance;
        this.updateSalary.transportAllowance = selectedSalaryStructure.transportAllowance;
        this.updateSalary.others = selectedSalaryStructure.others;
        this.updateSalary.grossSalary = selectedSalaryStructure.grossSalary;
        
        this.calculateDeductionsForUpdate(selectedEmp.employeeCode);
      }
    }
  }

  async calculateDeductions(employeeCode: number) {
    try {
      const absentResponse: any = await this.http.get(
        `${this.attendanceUrl}/absent-count/${employeeCode}?month=${this.salary.month}&year=${this.salary.year}`
      ).toPromise();
      
      const leaveResponse: any = await this.http.get(
        `${this.leaveUrl}/approved-leave-count/${employeeCode}?month=${this.salary.month}&year=${this.salary.year}`
      ).toPromise();
      
      const totalAbsent = absentResponse.absentCount || 0;
      const approvedLeaveCount = leaveResponse.approvedLeaveCount || 0;
      const netAbsentDays = Math.max(0, totalAbsent - approvedLeaveCount);
      const perDaySalary = this.salary.basicSalary / 24;
      const deductions = Math.round(perDaySalary * netAbsentDays);
      
      this.salary.deductions = deductions;
      this.salary.netSalary = this.salary.grossSalary - deductions;
      
    } catch (error) {
      console.error('Error calculating deductions:', error);
      this.salary.deductions = 0;
      this.salary.netSalary = this.salary.grossSalary;
    }
  }

  async calculateDeductionsForUpdate(employeeCode: number) {
    try {
      const absentResponse: any = await this.http.get(
        `${this.attendanceUrl}/absent-count/${employeeCode}?month=${this.updateSalary.month}&year=${this.updateSalary.year}`
      ).toPromise();
      
      const leaveResponse: any = await this.http.get(
        `${this.leaveUrl}/approved-leave-count/${employeeCode}?month=${this.updateSalary.month}&year=${this.updateSalary.year}`
      ).toPromise();
      
      const totalAbsent = absentResponse.absentCount || 0;
      const approvedLeaveCount = leaveResponse.approvedLeaveCount || 0;
      const netAbsentDays = Math.max(0, totalAbsent - approvedLeaveCount);
      const perDaySalary = this.updateSalary.basicSalary / 24;
      const deductions = Math.round(perDaySalary * netAbsentDays);
      
      this.updateSalary.deductions = deductions;
      this.updateSalary.netSalary = this.updateSalary.grossSalary - deductions;
      
    } catch (error) {
      console.error('Error calculating deductions for update:', error);
      this.updateSalary.deductions = 0;
      this.updateSalary.netSalary = this.updateSalary.grossSalary;
    }
  }

  generateInvoice(salary: Salary) {
    const invoiceWindow = window.open('', '_blank');
    
    if (invoiceWindow) {


















    // ======== NEWLY ADDED FUNCTIONS START ======== //
    const formatCurrency = (amount: number) => {
      return 'BDT ' + amount.toLocaleString('en-US', { 
        minimumFractionDigits: 2, 
        maximumFractionDigits: 2 
      });
    };

    const formatDate = (dateString: string) => {
      if (!dateString) return new Date().toLocaleDateString('en-US', {
        month: 'short', 
        day: 'numeric', 
        year: 'numeric'
      });
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', {
        month: 'short', 
        day: 'numeric', 
        year: 'numeric'
      });
    };
    // ======== NEWLY ADDED FUNCTIONS END ======== //
















      const invoiceContent = `
      <!DOCTYPE html>
      <html lang="en">
      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Salary Invoice - ${salary.employeeName}</title>
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css" rel="stylesheet">
        <style>
          /* Base Styles */
          * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
          }
          
          body {
            font-family: 'Poppins', sans-serif;
            background: #f5f7fa;
            color: #333;
            line-height: 1.6;
          }
          
          /* Invoice Container */
          .invoice-container {
            max-width: 900px;
            margin: 30px auto;
            background: #fff;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            border-radius: 12px;
            overflow: hidden;
            position: relative;
          }
          
          /* Header Section */
          .invoice-header {
            background: linear-gradient(135deg, #6B73FF 0%, #000DFF 100%);
            color: white;
            padding: 30px;
            text-align: center;
            position: relative;
            overflow: hidden;
          }
          
          .invoice-title {
            font-size: 32px;
            font-weight: 700;
            margin-bottom: 5px;
            position: relative;
            z-index: 1;
          }
          
          .invoice-number {
            font-size: 16px;
            opacity: 0.9;
            position: relative;
            z-index: 1;
          }
          
          /* Info Sections */
          .info-sections {
            display: flex;
            justify-content: space-between;
            padding: 30px;
            background: #fff;
            border-bottom: 1px solid #eee;
          }
          
          .info-box {
            flex: 1;
            padding: 20px;
            background: #f9fafc;
            border-radius: 8px;
            margin: 0 10px;
          }
          
          .info-box h3 {
            font-size: 18px;
            color: #4a5568;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid #e2e8f0;
          }
          
          .info-item {
            margin-bottom: 8px;
            display: flex;
          }
          
          .info-label {
            font-weight: 500;
            color: #718096;
            min-width: 120px;
          }
          
          .info-value {
            color: #2d3748;
            font-weight: 500;
          }
          
          /* Salary Details Table */
          .invoice-details {
            padding: 0 30px 30px;
          }
          
          .details-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
          }
          
          .details-table th {
            background: #4a6cf7;
            color: white;
            padding: 12px 15px;
            text-align: left;
            font-weight: 500;
          }
          
          .details-table td {
            padding: 12px 15px;
            border-bottom: 1px solid #eee;
          }
          
          .details-table tr:last-child td {
            border-bottom: none;
          }
          
          .details-table tr:nth-child(even) {
            background: #f8fafc;
          }
          
          .details-table .text-right {
            text-align: right;
          }
          
          .details-table .total-row td {
            font-weight: 600;
            background: #f0f4ff;
            color: #4a6cf7;
          }
          
          /* Footer */
          .invoice-footer {
            padding: 30px;
            background: #f9fafc;
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
          }
          
          .payment-method {
            flex: 1;
          }
          
          .payment-method h3 {
            font-size: 16px;
            margin-bottom: 10px;
            color: #4a5568;
          }
          
          .signature-box {
            text-align: center;
            margin-top: 50px;
          }
          
          .signature-line {
            width: 200px;
            height: 1px;
            background: #cbd5e0;
            margin: 0 auto 10px;
          }
          
          /* Action Buttons */
          .action-buttons {
            text-align: center;
            padding: 20px;
            background: #fff;
            border-top: 1px solid #eee;
          }
          
          .btn {
            padding: 12px 30px;
            border-radius: 6px;
            font-weight: 500;
            cursor: pointer;
            border: none;
            margin: 0 10px;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            font-size: 15px;
          }
          
          .btn-print {
            background: #f56565;
            color: white;
          }
          
          .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
          }
          
          /* Print Styles - Fixed Issues */
          @media print {
            body {
              background: none;
              font-size: 14px;
              -webkit-print-color-adjust: exact !important;
              print-color-adjust: exact !important;
            }
            
            .invoice-container {
              box-shadow: none;
              margin: 0;
              border-radius: 0;
              max-width: 100%;
              page-break-after: avoid;
              page-break-inside: avoid;
              break-inside: avoid;
            }
            
            .action-buttons {
              display: none;
            }
            
            .info-sections {
              display: flex !important;
              flex-direction: row !important;
              page-break-inside: avoid;
              break-inside: avoid;
            }
            
            .info-box {
              background: #f9fafc !important;
              -webkit-print-color-adjust: exact;
              print-color-adjust: exact;
              margin: 0 10px !important;
              page-break-inside: avoid;
              break-inside: avoid;
              padding: 15px !important;
            }
            
            .invoice-header {
              background: linear-gradient(135deg, #6B73FF 0%, #000DFF 100%) !important;
              -webkit-print-color-adjust: exact;
              print-color-adjust: exact;
              color: white !important;
              padding: 20px !important;
              page-break-after: avoid;
            }
            
            .details-table th {
              background: #4a6cf7 !important;
              color: white !important;
              -webkit-print-color-adjust: exact;
              print-color-adjust: exact;
            }
            
            .details-table .total-row td {
              background: #f0f4ff !important;
              color: #4a6cf7 !important;
              -webkit-print-color-adjust: exact;
              print-color-adjust: exact;
            }
            
            .invoice-title {
              font-size: 24px !important;
              color: white !important;
            }
            
            @page {
              size: A4 portrait;
              margin: 15mm 10mm;
            }
          }
          
          /* Responsive Styles */
          @media (max-width: 768px) {
            .info-sections {
              flex-direction: column;
            }
            
            .info-box {
              margin: 10px 0 !important;
            }
            
            .invoice-header {
              padding: 20px;
            }
            
            .invoice-title {
              font-size: 24px;
            }
            
            .details-table td, 
            .details-table th {
              padding: 8px 5px;
              font-size: 14px;
            }
            
            .invoice-footer {
              display: flex !important;
              justify-content: space-between !important;
              flex-direction: row !important;
              page-break-inside: avoid;
              break-inside: avoid;
              align-items: flex-end !important;
            }
            
            .signature-box {
              margin-top: 30px;
            }
            
            .btn {
              padding: 10px 20px;
              margin-bottom: 10px;
              width: 80%;
            }
          }
        </style>
      </head>
      <body>
        <div class="invoice-container">
          <!-- Header -->
          <div class="invoice-header">
            <h1 class="invoice-title">SALARY INVOICE</h1>
            <p class="invoice-number">Invoice #SAL-${salary.id}-${new Date().getTime().toString().slice(-4)}</p>
          </div>
          
          <!-- Company and Employee Info -->
          <div class="info-sections">
            <div class="info-box">
              <h3>Company Information</h3>
              <div class="info-item">
                <span class="info-label">Company :</span>
                <span class="info-value">Ha-Meem Group</span>
              </div>
              <div class="info-item">
                <span class="info-label">Address:</span>
                <span class="info-value">387 (South), Tejgaon, Dhaka</span>
              </div>
              <div class="info-item">
                <span class="info-label">Phone:</span>
                <span class="info-value">+880-2-8170592</span>
              </div>
              <div class="info-item">
                <span class="info-label">Email:</span>
                <span class="info-value">delwar@hameemgroup.com</span>
              </div>
            </div>
            
            <div class="info-box">
              <h3>Employee Information</h3>
              <div class="info-item">
                <span class="info-label">Name:</span>
                <span class="info-value">${salary.employeeName}</span>
              </div>
              <div class="info-item">
                <span class="info-label">ID:</span>
                <span class="info-value">${salary.employeeCode}</span>
              </div>
              <div class="info-item">
                <span class="info-label">Designation:</span>
                <span class="info-value">${salary.designationName}</span>
              </div>
              <div class="info-item">
                <span class="info-label">Period:</span>
                <span class="info-value">${salary.month}, ${salary.year}</span>
              </div>
            </div>
          </div>
          
          <!-- Salary Details -->
          <div class="invoice-details">
            <table class="details-table">
              <thead>
                <tr>
                  <th>Description</th>
                  <th class="text-right">Amount (BDT)</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>Basic Salary</td>
                  <td class="text-right">${formatCurrency(salary.basicSalary)}</td>
                </tr>
                <tr>
                  <td>House Rent Allowance</td>
                  <td class="text-right">${formatCurrency(salary.houseRent)}</td>
                </tr>
                <tr>
                  <td>Medical Allowance</td>
                  <td class="text-right">${formatCurrency(salary.medicalAllowance)}</td>
                </tr>
                <tr>
                  <td>Transport Allowance</td>
                  <td class="text-right">${formatCurrency(salary.transportAllowance)}</td>
                </tr>
                <tr>
                  <td>Other Allowances</td>
                  <td class="text-right">${formatCurrency(salary.others)}</td>
                </tr>
                <tr class="total-row">
                  <td><strong>Gross Salary</strong></td>
                  <td class="text-right"><strong>${formatCurrency(salary.grossSalary)}</strong></td>
                </tr>
                <tr>
                  <td>Deductions (Absent/Late)</td>
                  <td class="text-right">- ${formatCurrency(salary.deductions)}</td>
                </tr>
                <tr class="total-row">
                  <td><strong>Net Payable Amount</strong></td>
                  <td class="text-right"><strong><strong>${formatCurrency(salary.netSalary)}</strong></td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <!-- Footer -->
          <div class="invoice-footer">
            <div class="payment-method">
              <h3>Payment Method</h3>
              <p>${salary.paymentMode} ${salary.paymentRef ? '(Reference: ' + salary.paymentRef + ')' : ''}</p>
              <p>Payment Date: ${formatDate(salary.payDate)}</p>
            </div>
            
            <div class="signature-box">
              <div class="signature-line"></div>
              <p>Authorized Signature</p>
            </div>
          </div>
          
          <!-- Action Buttons -->
          <div class="action-buttons no-print">
            <button class="btn btn-print" onclick="window.print()">
              <i class="bi bi-printer"></i> Print Invoice
            </button>
          </div>
        </div>
        <script>
          
          // Print Preview Optimization
          window.onbeforeprint = function() {
            document.querySelector('.invoice-container').style.boxShadow = 'none';
            document.querySelector('.invoice-container').style.margin = '0';
            document.querySelector('.invoice-container').style.borderRadius = '0';
          };
          
          window.onafterprint = function() {
            document.querySelector('.invoice-container').style.boxShadow = '0 10px 30px rgba(0, 0, 0, 0.1)';
            document.querySelector('.invoice-container').style.margin = '30px auto';
            document.querySelector('.invoice-container').style.borderRadius = '12px';
          };
        </script>
      </body>
      </html>
      `;
      
      invoiceWindow.document.write(invoiceContent);
      invoiceWindow.document.close();
    } else {
      Swal.fire('Error', 'Popup blocker may be preventing the invoice from opening. Please allow popups for this site.', 'error');
    }
  }

}
