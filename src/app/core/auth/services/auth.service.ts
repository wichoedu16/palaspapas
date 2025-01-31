import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, tap } from 'rxjs';
import { TokenService } from './token.service';
import { LoginRequest } from '../models/login-request';
import { AuthResponse } from '../models/auth-response';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { SessionService } from './session.service';
import { environment } from 'src/enviroment/enviroment';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly TIMEOUT_DURATION = 30 * 60 * 1000; // 30 minutos
  private timeoutId: any;
  private readonly USER_KEY = 'current_user';
  private readonly API_URL = `${environment.apiUrl}/auth`;
  // private currentUserSubject = new BehaviorSubject<any>(null);
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  // Nuevo BehaviorSubject para el estado de autenticación
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private tokenService: TokenService,
    private http: HttpClient,
    private sessionService: SessionService,
    private router: Router
  ) {
    this.checkAuthStatus();
    this.setupAutoLogout();
  }

  private checkAuthStatus(): void {
    const token = this.tokenService.getToken();
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    try {
      const user = this.sessionService.getCurrentUser();
      if (user) {
        this.currentUserSubject.next(user);
        this.isAuthenticatedSubject.next(true);
      } else {
        this.router.navigate(['/login']);
      }
    } catch (error) {
      console.error('Error loading user:', error);
      this.router.navigate(['/login']);
    }
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.API_URL}/authenticate`, credentials)
      .pipe(
        tap((response) => {
          console.info('ingresando con el usuario: .', response.user?.firstName);
          if (response?.token && response?.user) {
            this.tokenService.saveToken(response.token);
            this.sessionService.setCurrentUser(response.user);
            this.currentUserSubject.next(response.user);
            this.isAuthenticatedSubject.next(true);
            this.resetTimer();
            this.router.navigate(['']);
          }
        }),
        catchError((error) => {
          console.error('Error during login:', error);
          throw error; // O maneja el error de otra manera
        })
      );
  }

  private setupAutoLogout() {
    document.addEventListener('mousemove', () => this.resetTimer());
    document.addEventListener('keypress', () => this.resetTimer());
  }

  private resetTimer() {
    clearTimeout(this.timeoutId);
    this.timeoutId = setTimeout(() => {
      this.logout();
    }, this.TIMEOUT_DURATION);
  }

  logout(): void {
    this.tokenService.removeToken();
    this.sessionService.clearCurrentUser();
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.isAuthenticatedSubject.value;
  }

  getCurrentUser(): User | null {
    try {
      const userStr = localStorage.getItem(this.USER_KEY);
      return userStr ? JSON.parse(userStr) : null;
    } catch (error) {
      console.error('Error getting user:', error);
      return null;
    }
  }

  setCurrentUser(user: User): void {
    if (user) {
      localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    }
  }

  clearCurrentUser(): void {
    localStorage.removeItem(this.USER_KEY);
  }

  getCurrentUserRole(): string | undefined {
    return this.currentUserSubject.value?.role?.name;
  }

  hasRole(roleName: string): boolean {
    if (roleName === 'ANY') return this.isAuthenticated();
    const currentUser = this.getCurrentUser();
    return currentUser?.role?.name === roleName || false;
  }

  hasPermission(permission: string): boolean {
    if (permission === 'ANY') return this.isAuthenticated();
    const currentUser = this.getCurrentUser();
    return (
      currentUser?.role?.permissions?.some(
        (p: { code: string }) => p.code === permission
      ) || false
    );
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some((role) => this.hasRole(role));
  }
}
