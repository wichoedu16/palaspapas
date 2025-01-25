import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, catchError, delay, Observable, of, tap, throwError } from 'rxjs';
import { User } from '../models/user';
import { AuthResponse } from '../models/auth-response';
import { TokenService } from './token.service';
import { SessionService } from '../../session/services/session.service';
import { LoginRequest } from '../models/login-request';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  
  private readonly API_URL = '/api/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  // Observable público para que los componentes puedan suscribirse a cambios
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private sessionService: SessionService
  ) {
    this.checkAuthStatus();
  }

  // login(credentials: LoginRequest): Observable<AuthResponse> {
  //   return this.http
  //     .post<AuthResponse>(`${this.API_URL}/login`, credentials)
  //     .pipe(
  //       tap((response) => {
  //         // Guardamos el token y el sessionId
  //         this.tokenService.saveToken(response.token, response.sessionId);
  //         this.currentUserSubject.next(response.user);

  //         // Registramos la sesión
  //         this.sessionService.registerLoginSession().subscribe();
  //       })
  //     );
  // }
  login(credentials: LoginRequest): Observable<AuthResponse> {
    // Simulamos credenciales válidas
    if (credentials.username === 'admin' && credentials.password === 'admin123') {
      const mockResponse: AuthResponse = {
        user: {
          id: 1,
          username: 'admin',
          firstName: 'Admin',
          lastName: 'User',
          email: 'admin@example.com',
          role: {
            id: 1,
            name: 'ADMIN',
            permissions: [],
            status: true,
          },
          status: true,
          createdAt: new Date()
        },
        token: 'mock-jwt-token',
        sessionId: 1
      };
  
      return of(mockResponse).pipe(
        delay(1000), // Simulamos delay de red
        tap(response => {
          this.tokenService.saveToken(response.token, response.sessionId);
          this.currentUserSubject.next(response.user);
        })
      );
    }
  
    // Si las credenciales son incorrectas, devolvemos error
    return throwError(() => new Error('Credenciales inválidas'));
  }

  logout(): Observable<void> {
    const sessionId = this.tokenService.getSessionId();

    if (sessionId) {
      return this.sessionService.registerLogout(sessionId).pipe(
        // Si el logout es exitoso, limpiamos los datos locales
        tap(() => this.cleanupAuth()),
        // Manejamos diferentes tipos de errores
        catchError(error => {
          // Si es un error de red, aún así limpiamos localmente
          if (error.status === 0) {
            console.warn('No se pudo contactar con el servidor. Limpiando datos locales.');
            this.cleanupAuth();
            return of(void 0);
          }
          
          // Si es un error de sesión inválida, solo limpiamos
          if (error.status === 401 || error.status === 404) {
            this.cleanupAuth();
            return of(void 0);
          }

          // Para otros errores, notificamos pero mantenemos los datos
          return throwError(() => new Error('Error al cerrar sesión. Por favor intente nuevamente.'));
        })
      );
    }

    this.cleanupAuth();
    return of(void 0);
  }

  // Método auxiliar para limpiar datos de autenticación
  private cleanupAuth(): void {
    this.tokenService.removeToken();
    this.currentUserSubject.next(null);
    // Limpiamos cualquier otro dato de sesión
    localStorage.clear(); // O una limpieza más específica si es necesario
  }

  private checkAuthStatus(): void {
    if (this.tokenService.isTokenExpired()) {
      this.cleanupAuth();
      return;
    }

    // Si hay token válido, obtenemos el usuario actual
    this.getCurrentUser().subscribe({
      next: (user) => this.currentUserSubject.next(user),
      error: () => this.cleanupAuth(),
    });
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.API_URL}/me`);
  }

  isAuthenticated(): boolean {
    return !this.tokenService.isTokenExpired();
  }
}
