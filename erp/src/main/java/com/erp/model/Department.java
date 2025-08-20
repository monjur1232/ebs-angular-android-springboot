package com.erp.model;

import javax.persistence.*;

@Entity(name = "department")
@Table(name = "department")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "department_code")
	private Long departmentCode;

	@Column(name = "department_name")
	private String departmentName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(Long departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

}
