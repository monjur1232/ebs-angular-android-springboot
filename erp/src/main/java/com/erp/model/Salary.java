package com.erp.model;

import java.util.Date;

import javax.persistence.*;

@Entity(name = "salary")
@Table(name = "salary")
public class Salary {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "salary_code")
	private Long salaryCode;

	@Column(name = "employee_code")
	private Long employeeCode;

	@Column(name = "employee_name")
	private String employeeName;

	@Column(name = "designation_code")
	private Long designationCode;

	@Column(name = "designation_name")
	private String designationName;

	@Column(name = "month")
	private String month;

	@Column(name = "year")
	private String year;

	@Column(name = "salary_structure_code")
	private Long salaryStructureCode;

	@Column(name = "basic_salary")
	private Double basicSalary;

	@Column(name = "house_rent")
	private Double houseRent;

	@Column(name = "medical_allowance")
	private Double medicalAllowance;

	@Column(name = "transport_allowance")
	private Double transportAllowance;

	@Column(name = "others")
	private Double others;

	@Column(name = "gross_salary")
	private Double grossSalary;

	@Column(name = "deductions")
	private Double deductions;

	@Column(name = "net_salary")
	private Double netSalary;

	@Column(name = "pay_date")
	@Temporal(TemporalType.DATE)
	private Date payDate;

	@Column(name = "payment_mode")
	private String paymentMode;

	@Column(name = "payment_ref")
	private String paymentRef;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSalaryCode() {
		return salaryCode;
	}

	public void setSalaryCode(Long salaryCode) {
		this.salaryCode = salaryCode;
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

	public Long getDesignationCode() {
		return designationCode;
	}

	public void setDesignationCode(Long designationCode) {
		this.designationCode = designationCode;
	}

	public String getDesignationName() {
		return designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Long getSalaryStructureCode() {
		return salaryStructureCode;
	}

	public void setSalaryStructureCode(Long salaryStructureCode) {
		this.salaryStructureCode = salaryStructureCode;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Double getHouseRent() {
		return houseRent;
	}

	public void setHouseRent(Double houseRent) {
		this.houseRent = houseRent;
	}

	public Double getMedicalAllowance() {
		return medicalAllowance;
	}

	public void setMedicalAllowance(Double medicalAllowance) {
		this.medicalAllowance = medicalAllowance;
	}

	public Double getTransportAllowance() {
		return transportAllowance;
	}

	public void setTransportAllowance(Double transportAllowance) {
		this.transportAllowance = transportAllowance;
	}

	public Double getOthers() {
		return others;
	}

	public void setOthers(Double others) {
		this.others = others;
	}

	public Double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(Double grossSalary) {
		this.grossSalary = grossSalary;
	}

	public Double getDeductions() {
		return deductions;
	}

	public void setDeductions(Double deductions) {
		this.deductions = deductions;
	}

	public Double getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(Double netSalary) {
		this.netSalary = netSalary;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentRef() {
		return paymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}

}
