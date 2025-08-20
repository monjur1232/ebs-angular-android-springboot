package com.erp.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "leave")
@Table(name = "leaves")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "leave_code")
    private Long leaveCode;

    @Column(name = "employee_code")
    private Long employeeCode;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "leave_type")
    private String leaveType;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "reason")
    private String reason;
    
    @Column(name = "leave_proposal")
    private String leaveProposal;

    @Column(name = "status")
    private Integer status = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLeaveCode() {
		return leaveCode;
	}

	public void setLeaveCode(Long leaveCode) {
		this.leaveCode = leaveCode;
	}

	public Long getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(Long employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getLeaveProposal() {
		return leaveProposal;
	}

	public void setLeaveProposal(String leaveProposal) {
		this.leaveProposal = leaveProposal;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
