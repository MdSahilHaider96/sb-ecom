import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {

  constructor(private http: HttpClient) {}

  loginData = {
    email: '',
    password: ''
  };

  onLogin() {
    console.log('Login clicked', this.loginData);

    // TEMP: backend call will come later
    // this.http.post('http://localhost:8080/api/auth/login', this.loginData)
    //   .subscribe(res => console.log(res));
  }
}
