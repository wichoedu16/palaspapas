import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@core/auth/services/auth.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  hidePassword = true;
  errorMessage: string = '';
  loading = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    // Redirigir si ya está autenticado
    this.authService.isAuthenticated$.subscribe((isAuthenticated) => {
      if (isAuthenticated) {
        this.router.navigate(['/dashboard']);
      }
    });

    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  ngOnInit(): void {
    // Si ya está autenticado, redirigir al dashboard
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  get formControls() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    if (this.loginForm.invalid || this.loading) {
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService
      .login(this.loginForm.value)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: () => {
        },
        error: (error) => {
          console.error('Login error:', error);
          this.errorMessage = 'Usuario o contraseña incorrectos';
        },
      });
  }
}
