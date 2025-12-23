import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html'
})
export class RegisterComponent {

  constructor(private http: HttpClient) {}

  registerData = {
    name: '',
    email: '',
    password: ''
  };

  onRegister() {
    console.log('Register clicked', this.registerData);

    // TEMP: backend call will come later
    // this.http.post('http://localhost:8080/api/auth/register', this.registerData)
    //   .subscribe(res => console.log(res));
  }
}
