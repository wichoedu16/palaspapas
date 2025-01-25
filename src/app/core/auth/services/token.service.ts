import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private readonly TOKEN_KEY = 'auth_token';
  private readonly SESSION_ID_KEY = 'session_id';

  saveToken(token: string, sessionId: number): void {
    console.log('Guardando token:', token);
    localStorage.setItem(this.TOKEN_KEY, token);
    localStorage.setItem(this.SESSION_ID_KEY, sessionId.toString());
  }

  isTokenExpired(): boolean {
    const token = localStorage.getItem(this.TOKEN_KEY);
    console.log('Verificando token:', token);
    return !token;
  }

  // isTokenExpired(): boolean {
  //   const token = this.getToken();
  //   if (!token) return true;

  //   try {
  //     const decoded = this.decodeToken(token);
  //     return new Date(decoded.exp * 1000) <= new Date();
  //   } catch {
  //     return true;
  //   }
  // }

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

  

  private decodeToken(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      return JSON.parse(window.atob(base64));
    } catch {
      return null;
    }
  }
}
