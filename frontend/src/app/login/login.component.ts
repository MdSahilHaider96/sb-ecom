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
  // Matches your HTML [(ngModel)]
  loginData = {
    email: '',
    password: ''
  };

  constructor(private http: HttpClient) {}

  login() {
    this.http.post('http://localhost:8080/api/auth/login', this.loginData)
      .subscribe({
        next: (res) => {
          console.log('Login Success', res);
          alert('✅ Login Successful');
        },
        error: () => alert('❌ Invalid Credentials')
      });
  }
}