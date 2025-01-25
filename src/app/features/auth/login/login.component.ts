// features/auth/login/login.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/auth/services/auth.service';
import { LoginRequest } from '../../../core/auth/models/login-request';

interface LoginFormControls {
  username: FormControl<string>;  // Note que ahora es string, no string | null
  password: FormControl<string>;  // Note que ahora es string, no string | null
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  loginForm: FormGroup<LoginFormControls>;
  hidePassword = true;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    // Inicializamos con valores por defecto vacíos pero no null
    this.loginForm = this.fb.group<LoginFormControls>({
      username: this.fb.control('', {
        validators: [Validators.required, Validators.minLength(3)],
        nonNullable: true  // Esto asegura que el valor nunca será null
      }),
      password: this.fb.control('', {
        validators: [Validators.required, Validators.minLength(6)],
        nonNullable: true  // Esto asegura que el valor nunca será null
      })
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.loginForm.disable();
    this.errorMessage = null;

    // Convertimos explícitamente los valores a LoginRequest
    const loginData: LoginRequest = {
      username: this.loginForm.getRawValue().username,
      password: this.loginForm.getRawValue().password
    };

    this.authService.login(loginData).subscribe({
      next: (response) => {
        console.log('Login exitoso:', response);
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        console.error('Error en login:', error);
        this.errorMessage = 'Credenciales inválidas';
        this.loginForm.enable();
      }
    });
  }

  get formControls() {
    return {
      username: this.loginForm.get('username'),
      password: this.loginForm.get('password')
    };
  }
}