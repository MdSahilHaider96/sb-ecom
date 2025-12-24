import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  // MUST match [(ngModel)]="loginData.username"
  loginData = {
    username: '', 
    password: ''
  };

  constructor(private http: HttpClient) {}

  login() {
    this.http.post('http://localhost:8080/api/auth/signin', this.loginData, { 
      withCredentials: true 
    })
    .subscribe({
      next: (res) => {
        console.log('Login Success', res);
        alert('✅ Login Successful');
      },
      error: (err) => {
        console.error('Login Error:', err);
        alert('❌ Login Failed');
      }
    });
  }
}