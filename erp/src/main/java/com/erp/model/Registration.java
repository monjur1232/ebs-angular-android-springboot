package com.erp.model;

import javax.persistence.*;

@Entity(name = "registration")
@Table(name = "registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "registration_code")
    private Long registrationCode;
    
    @Column(name = "employee_code")
    private Long employeeCode;
    
    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRegistrationCode() {
		return registrationCode;
	}

	public void setRegistrationCode(Long registrationCode) {
		this.registrationCode = registrationCode;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
