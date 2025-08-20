export class Leave {
    id?: number;
    leaveCode?: number;
    employeeCode?: number;
    employeeName?: string;
    leaveType?: string;
    startDate?: Date;
    endDate?: Date;
    reason?: string;
    leaveProposal?: string;
    status?: number; // 0=Pending, 1=Approved, 2=Rejected
}