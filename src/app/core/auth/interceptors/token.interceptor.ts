// core/auth/interceptors/token.interceptor.ts
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { TokenService } from '../services/token.service';
import { AuthService } from '../services/auth.service';
import { AuthResponse } from '../models/auth-response';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  // Rutas que no necesitan token de autenticación
  private readonly PUBLIC_ROUTES = [
    '/api/auth/login',
    '/api/auth/refresh-token',
  ];

  constructor(
    private tokenService: TokenService,
    private authService: AuthService
  ) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Verificamos si la ruta actual es pública
    if (this.isPublicRoute(request.url)) {
      return next.handle(request);
    }

    // Añadimos el token si existe
    const token = this.tokenService.getToken();
    if (token) {
      request = this.addToken(request, token);
    }

    // Manejamos la respuesta y los posibles errores
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        // Si el error es 401 (No autorizado)
        if (error.status === 401) {
          // Verificamos si el error es por token expirado
          if (this.tokenService.isTokenExpired()) {
            // Intentamos refrescar el token
            return this.handleExpiredToken(request, next);
          }
          // Si no es por expiración, hacemos logout
          this.authService.logout();
        }
        return throwError(() => error);
      })
    );
  }

  /**
   * Añade el token de autorización al encabezado de la petición
   */
  private addToken(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  /**
   * Verifica si la ruta es pública (no requiere autenticación)
   */
  private isPublicRoute(url: string): boolean {
    return this.PUBLIC_ROUTES.some((route) => url.includes(route));
  }

  /**
   * Maneja el caso de token expirado intentando obtener uno nuevo
   */
  private handleExpiredToken(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const refreshToken = this.tokenService.getRefreshToken();

    if (!refreshToken) {
      this.authService.logout();
      return throwError(() => new Error('No refresh token available'));
    }

    // Ahora TypeScript sabrá que la respuesta es de tipo AuthResponse
    return this.authService.refreshToken(refreshToken).pipe(
      switchMap((response: AuthResponse) => {
        // Verificamos que tengamos los datos necesarios
        if (!response.token) {
          throw new Error('Invalid token response');
        }

        // Guardamos el nuevo token
        this.tokenService.saveToken(response.token, response.refreshToken);
        // Reintentamos la petición original con el nuevo token
        return next.handle(this.addToken(request, response.token));
      }),
      catchError((error) => {
        this.authService.logout();
        return throwError(() => error);
      })
    );
  }
}
