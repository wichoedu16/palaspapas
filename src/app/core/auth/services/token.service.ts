import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'auth_user';
  private readonly SESSION_ID_KEY = 'session_id';

  saveToken(token: string, sessionId?: number): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    if (sessionId !== undefined) {
      localStorage.setItem(this.SESSION_ID_KEY, sessionId.toString());
    }
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getSessionId(): number | null {
    const sessionId = localStorage.getItem(this.SESSION_ID_KEY);
    return sessionId ? parseInt(sessionId) : null;
  }

  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.SESSION_ID_KEY);
  }

  saveUser(user: any): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  getUser(): any | null {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  removeUser(): void {
    localStorage.removeItem(this.USER_KEY);
  }

  clear(): void {
    this.removeToken();
    this.removeUser();
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) return true;

    try {
      const decoded = this.decodeToken(token);
      return new Date(decoded.exp * 1000) <= new Date();
    } catch {
      return true;
    }
  }

  getRoles(): string[] {
    const token = this.getToken();
    if (token) {
      const decodedToken = this.decodeToken(token);
      return decodedToken?.roles || [];
    }
    return [];
  }

  getPermissions(): string[] {
    const token = this.getToken();
    if (token) {
      const decodedToken = this.decodeToken(token);
      return decodedToken?.permissions || [];
    }
    return [];
  }

  decodeToken(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      return JSON.parse(window.atob(base64));
    } catch {
      return null;
    }
  }
}
