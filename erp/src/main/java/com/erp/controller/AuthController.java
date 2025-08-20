package com.erp.controller;

import com.erp.model.Registration;
import com.erp.model.Employee;
import com.erp.repository.RegistrationRepository;
import com.erp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Find users from registration
        Registration registration = registrationRepository.findByEmailAndPassword(
            loginRequest.getEmail(), 
            loginRequest.getPassword()
        );

        if (registration == null) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        // Find employee information
        Employee employee = employeeRepository.findByEmployeeCode(registration.getEmployeeCode());
        
        if (employee == null) {
            return ResponseEntity.badRequest().body("Employee not found");
        }

        // Create a response object
        LoginResponse response = new LoginResponse();
        response.setAuthenticated(true);
        response.setEmployee(employee);
        
        return ResponseEntity.ok(response);
    }
}

// Helper class
class LoginRequest {
    private String email;
    private String password;
    
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

class LoginResponse {
    private boolean authenticated;
    private Employee employee;
    
	public boolean isAuthenticated() {
		return authenticated;
	}
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
    
}