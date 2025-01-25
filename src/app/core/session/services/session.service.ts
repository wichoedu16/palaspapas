import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { UserSession } from '../models/user-session';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private readonly API_URL = '/api/sessions';

  constructor(private http: HttpClient) {}

  registerLoginSession(): Observable<UserSession> {
    const deviceInfo = this.getDeviceInfo();

    const sessionData = {
      loginDate: new Date(),
      deviceInfo: deviceInfo,
    };

    return this.http.post<UserSession>(`${this.API_URL}/start`, sessionData);
  }

  registerLogout(sessionId: number): Observable<void> {
    // Convertimos la respuesta a void usando map
    return this.http
      .post<UserSession>(`${this.API_URL}/${sessionId}/end`, {
        logoutDate: new Date(),
      })
      .pipe(
        map(() => void 0) // Transformamos la respuesta a void
      );
  }

  getUserSessions(): Observable<UserSession[]> {
    return this.http.get<UserSession[]>(`${this.API_URL}/user`);
  }

  private getDeviceInfo(): string {
    const deviceInfo = {
      userAgent: window.navigator.userAgent,
      platform: window.navigator.platform,
      language: window.navigator.language,
      screenResolution: `${window.screen.width}x${window.screen.height}`,
      timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone,
    };

    return JSON.stringify(deviceInfo);
  }
}
