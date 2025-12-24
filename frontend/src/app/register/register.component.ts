import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  // These fields must match the [(ngModel)] names in your register.component.html
  user = {
    name: '',
    email: '',
    password: ''
  };

  constructor(private http: HttpClient) {}

  register() {
    // 1. Create a payload that matches your Java SignupRequest class exactly
    // Your backend expects 'username', not 'name'
    const signupPayload = {
      username: this.user.name, 
      email: this.user.email,
      password: this.user.password
    };

    // 2. Use the /signup endpoint as defined in your AuthController @PostMapping
  this.http.post('http://localhost:8080/api/auth/signup', signupPayload)
      .subscribe({
        next: (res) => {
          console.log('Registration Success:', res);
          alert('✅ Registration Successful!');
        },
        error: (err) => {
          console.error('Registration Error:', err);
          // Shows the specific error message from your MessageResponse if available
          const errorMsg = err.error?.message || 'Check if Backend is running';
          alert('❌ Registration Failed: ' + errorMsg);
        }
      });
  }
}