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
  // Matches [(ngModel)]="user.name" in your HTML
  user = {
    name: '',
    email: '',
    password: ''
  };

  constructor(private http: HttpClient) {}

  register() {
    this.http.post('http://localhost:8080/api/auth/register', this.user)
      .subscribe({
        next: (res) => alert('✅ Registration Successful'),
        error: (err) => alert('❌ Registration Failed')
      });
  }
}