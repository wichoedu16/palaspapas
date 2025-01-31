// core/auth/services/user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { User } from '../models/user';
import { Permission } from '../models/permission';
import { environment } from 'src/enviroment/enviroment';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  // Usamos BehaviorSubject para mantener el estado del usuario actual
  // y permitir que múltiples componentes se suscriban a los cambios
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();
  private readonly API_URL = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {
    // Al iniciar el servicio, intentamos recuperar el usuario del localStorage
    this.loadStoredUser();
  }

  /**
   * Obtiene el perfil del usuario actual desde el servidor
   * Útil para actualizar la información después del login
   */
  getCurrentUser(): User | null {
    try {
      const userStr = localStorage.getItem('current_user');
      return userStr ? JSON.parse(userStr) : null;
    } catch (error) {
      console.error('Error parsing user data:', error);
      return null;
    }
  }

  /**
   * Actualiza la información del usuario en el estado de la aplicación
   * y en el almacenamiento local
   */
  setCurrentUser(user: User): void {
    localStorage.setItem('current_user', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  /**
   * Intenta cargar el usuario almacenado en localStorage
   * Se llama automáticamente al iniciar el servicio
   */
  private loadStoredUser(): void {
    const storedUser = localStorage.getItem('current_user');
    if (storedUser) {
      try {
        const user = JSON.parse(storedUser);
        this.currentUserSubject.next(user);
      } catch (error) {
        console.error('Error parsing stored user:', error);
        localStorage.removeItem('current_user');
      }
    }
  }

  /**
   * Limpia la información del usuario actual
   * Se usa principalmente durante el logout
   */
  clearCurrentUser(): void {
    localStorage.removeItem('current_user');
    this.currentUserSubject.next(null);
  }

  /**
   * Verifica si el usuario actual tiene un permiso específico
   */
  hasPermission(permissionCode: string): boolean {
    const currentUser = this.getCurrentUser();
    if (!currentUser?.role?.permissions) return false;
    return currentUser.role.permissions.some(
      (permission: Permission) => permission.code === permissionCode
    );
  }

  /**
   * Verifica si el usuario actual tiene un rol específico
   */
  hasRole(roleName: string): boolean {
    const currentUser = this.getCurrentUser();
    if (!currentUser?.role) return false;
    return currentUser.role.name === roleName;
  }

  // User Status Management
  updateUserStatus(id: number, status: boolean): Observable<User> {
    return this.http.patch<User>(`${this.API_URL}/${id}/status`, { status });
  }

  // Role Assignment
  assignRole(userId: number, roleId: number): Observable<User> {
    return this.http.put<User>(`${this.API_URL}/${userId}/roles/${roleId}`, {});
  }
}
