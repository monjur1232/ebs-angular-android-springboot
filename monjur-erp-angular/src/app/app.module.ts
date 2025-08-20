import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import appRoutes from './app-routing.module';
import { LoginComponent } from './controller/login/login.component';
import { AdminLayoutComponent } from './controller/admin-layout/admin-layout.component';
import { AdminDashboardComponent } from './controller/admin-dashboard/admin-dashboard.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NgChartsModule } from 'ng2-charts';
import { EmployeeComponent } from './controller/employee/employee.component';
import { AttendanceComponent } from './controller/attendance/attendance.component';
import { DepartmentComponent } from './controller/department/department.component';
import { DesignationComponent } from './controller/designation/designation.component';
import { LeaveComponent } from './controller/leave/leave.component';
import { SalaryStructureComponent } from './controller/salary-structure/salary-structure.component';
import { SalaryComponent } from './controller/salary/salary.component';
import { RegistrationComponent } from './controller/registration/registration.component';
import { UserLayoutComponent } from './controller/user-layout/user-layout.component';
import { UserDashboardComponent } from './controller/user-dashboard/user-dashboard.component';
import { LeaveRequestComponent } from './controller/leave-request/leave-request.component';
import { SupplierComponent } from './controller/supplier/supplier.component';
import { ProductCategoryComponent } from './controller/product-category/product-category.component';
import { ProductComponent } from './controller/product/product.component';
import { PurchaseOrderComponent } from './controller/purchase-order/purchase-order.component';
import { ReturnComponent } from './controller/return/return.component';
import { PaymentComponent } from './controller/payment/payment.component';
import { InventoryComponent } from './controller/inventory/inventory.component';
import { CustomerComponent } from './controller/customer/customer.component';
import { SalesOrderComponent } from './controller/sales-order/sales-order.component';
import { SalesReturnComponent } from './controller/sales-return/sales-return.component';
import { SalesPaymentComponent } from './controller/sales-payment/sales-payment.component';
import { AttendanceReportComponent } from './controller/attendance-report/attendance-report.component';
import { PayrollReportComponent } from './controller/payroll-report/payroll-report.component';
import { ProfitAndLossComponent } from './controller/profit-and-loss/profit-and-loss.component';
import { ClockAndCalendarComponent } from './controller/clock-and-calendar/clock-and-calendar.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminLayoutComponent,
    AdminDashboardComponent,
    EmployeeComponent,
    AttendanceComponent,
    DepartmentComponent,
    DesignationComponent,
    LeaveComponent,
    SalaryStructureComponent,
    SalaryComponent,
    RegistrationComponent,
    UserLayoutComponent,
    UserDashboardComponent,
    LeaveRequestComponent,
    SupplierComponent,
    ProductCategoryComponent,
    ProductComponent,
    PurchaseOrderComponent,
    ReturnComponent,
    PaymentComponent,
    InventoryComponent,
    CustomerComponent,
    SalesOrderComponent,
    SalesReturnComponent,
    SalesPaymentComponent,
    AttendanceReportComponent,
    PayrollReportComponent,
    ProfitAndLossComponent,
    ClockAndCalendarComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    FormsModule,
    HttpClientModule,
    NgChartsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
