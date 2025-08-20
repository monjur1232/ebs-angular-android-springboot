import { Routes } from "@angular/router";
import { LoginComponent } from "./controller/login/login.component";
import { AdminLayoutComponent } from "./controller/admin-layout/admin-layout.component";
import { AdminDashboardComponent } from "./controller/admin-dashboard/admin-dashboard.component";
import { DepartmentComponent } from "./controller/department/department.component";
import { EmployeeComponent } from "./controller/employee/employee.component";
import { AttendanceComponent } from "./controller/attendance/attendance.component";
import { DesignationComponent } from "./controller/designation/designation.component";
import { LeaveComponent } from "./controller/leave/leave.component";
import { SalaryStructureComponent } from "./controller/salary-structure/salary-structure.component";
import { SalaryComponent } from "./controller/salary/salary.component";
import { RegistrationComponent } from "./controller/registration/registration.component";
import { UserLayoutComponent } from "./controller/user-layout/user-layout.component";
import { UserDashboardComponent } from "./controller/user-dashboard/user-dashboard.component";
import { LeaveRequestComponent } from "./controller/leave-request/leave-request.component";
import { SupplierComponent } from "./controller/supplier/supplier.component";
import { ProductCategoryComponent } from "./controller/product-category/product-category.component";
import { ProductComponent } from "./controller/product/product.component";
import { PurchaseOrderComponent } from "./controller/purchase-order/purchase-order.component";
import { ReturnComponent } from "./controller/return/return.component";
import { PaymentComponent } from "./controller/payment/payment.component";
import { InventoryComponent } from "./controller/inventory/inventory.component";
import { CustomerComponent } from "./controller/customer/customer.component";
import { SalesOrderComponent } from "./controller/sales-order/sales-order.component";
import { SalesReturnComponent } from "./controller/sales-return/sales-return.component";
import { SalesPaymentComponent } from "./controller/sales-payment/sales-payment.component";
import { AttendanceReportComponent } from "./controller/attendance-report/attendance-report.component";
import { PayrollReportComponent } from "./controller/payroll-report/payroll-report.component";
import { ProfitAndLossComponent } from "./controller/profit-and-loss/profit-and-loss.component";
import { ClockAndCalendarComponent } from "./controller/clock-and-calendar/clock-and-calendar.component";

const appRoutes: Routes = [
    {path: "", redirectTo: "login", pathMatch: "full"},
    {path: "login", component: LoginComponent},
    {path: "", component: AdminLayoutComponent, children: [
        {path: "", redirectTo: "admin-dashboard", pathMatch: "full"},
        {path: "admin-dashboard", component: AdminDashboardComponent},
        {path: "department", component: DepartmentComponent},
        {path: "employee", component: EmployeeComponent},
        {path: "attendance", component: AttendanceComponent},
        {path: "designation", component: DesignationComponent},
        {path: "leave", component: LeaveComponent},
        {path: "salary-structure", component: SalaryStructureComponent},
        {path: "salary", component: SalaryComponent},
        {path: "registration", component: RegistrationComponent},
        {path: "supplier", component: SupplierComponent},
        {path: "product-category", component: ProductCategoryComponent},
        {path: "product", component: ProductComponent},
        {path: "purchase-order", component: PurchaseOrderComponent},
        {path: "return", component: ReturnComponent},
        {path: "payment", component: PaymentComponent},
        {path: "inventory", component: InventoryComponent},
        {path: "customer", component: CustomerComponent},
        {path: "sales-order", component: SalesOrderComponent},
        {path: "sales-return", component: SalesReturnComponent},
        {path: "sales-payment", component: SalesPaymentComponent},
        {path: "attendance-report", component: AttendanceReportComponent},
        {path: "payroll-report", component: PayrollReportComponent},
        {path: "profit-and-loss", component: ProfitAndLossComponent},
        {path: "clock-and-calendar", component: ClockAndCalendarComponent}
    ]},
    {path: "", component: UserLayoutComponent, children: [
        {path: "", redirectTo: "user-dashboard", pathMatch: "full"},
        {path: "user-dashboard", component: UserDashboardComponent},
        {path: "leave-request", component: LeaveRequestComponent}
    ]}
];

export default appRoutes;
