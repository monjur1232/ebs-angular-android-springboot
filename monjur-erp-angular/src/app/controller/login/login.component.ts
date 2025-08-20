import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  constructor(private router: Router, private http: HttpClient) {}

  // onLogin(): void {
  //   // Demo login logic (replace with real authentication)
  //   // if (this.email === 'admin@example.com' && this.password === 'admin123') {
  //   //   alert('Login successful!');
  //   //   this.router.navigate(['/dashboard']); // route to your dashboard
  //   // } else {
  //   //   alert('Invalid credentials!');
  //   // }
  //   this.router.navigate(["/admin-dashboard"]);
  // }

  onLogin(): void {
    // Check your specific conditions
    if (this.email === 'admin.erp@ha-meemgroup.com' && this.password === '123456789') {
      localStorage.setItem('isAdmin', 'true');
      this.router.navigate(["/admin-dashboard"]);
      return;
    }

    // Normal login process
    this.http.post<any>('http://localhost:8080/auth/login', {
      email: this.email,
      password: this.password
    }).subscribe(
      (response) => {
        if (response.authenticated) {
          localStorage.setItem('currentEmployee', JSON.stringify(response.employee));
          this.router.navigate(["/user-dashboard"]);
        } else {
          alert('Invalid credentials!');
        }
      },
      (error) => {
        alert('Login failed!');
      }
    );
  }

}
