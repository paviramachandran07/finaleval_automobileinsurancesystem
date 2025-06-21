import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        const role = this.authService.getRole();
        alert('Login successful!');
        if (role === 'USER') {
          this.router.navigate(['/userdashboard']);
        } else if (role === 'OFFICER') {
          this.router.navigate(['/officerdashboard']);
        }
      },
      error: (err) => {
        this.errorMessage = err.error || 'Login failed. Please try again.';
        alert(this.errorMessage);
      }
    });
  }
}


