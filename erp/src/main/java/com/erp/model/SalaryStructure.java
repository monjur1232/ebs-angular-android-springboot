package com.erp.model;

import javax.persistence.*;

@Entity(name = "salary_structure")
@Table(name = "salary_structure")
public class SalaryStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "salary_structure_code")
    private Long salaryStructureCode;

    @Column(name = "designation_code")
    private Long designationCode;
    
    @Column(name = "designation_name")
    private String designationName;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSalaryStructureCode() {
		return salaryStructureCode;
	}

	public void setSalaryStructureCode(Long salaryStructureCode) {
		this.salaryStructureCode = salaryStructureCode;
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

}
