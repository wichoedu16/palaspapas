// core/auth/services/user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  // Usamos BehaviorSubject para mantener el estado del usuario actual
  // y permitir que múltiples componentes se suscriban a los cambios
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  private readonly API_URL = '/api/users';

  constructor(private http: HttpClient) {
    // Al iniciar el servicio, intentamos recuperar el usuario del localStorage
    this.loadStoredUser();
  }

  /**
   * Obtiene el perfil del usuario actual desde el servidor
   * Útil para actualizar la información después del login
   */
  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.API_URL}/me`);
  }

  /**
   * Actualiza la información del usuario en el estado de la aplicación
   * y en el almacenamiento local
   */
  setCurrentUser(user: User): void {
    // Guardamos en localStorage para persistencia entre recargas
    localStorage.setItem('current_user', JSON.stringify(user));
    // Actualizamos el BehaviorSubject para notificar a los suscriptores
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
    const currentUser = this.currentUserSubject.value;
    if (!currentUser?.role?.permissions) return false;
    
    return currentUser.role.permissions.some(
      (      permission: { code: string; }) => permission.code === permissionCode
    );
  }

  /**
   * Verifica si el usuario actual tiene un rol específico
   */
  hasRole(roleName: string): boolean {
    const currentUser = this.currentUserSubject.value;
    if (!currentUser?.role) return false;
    
    return currentUser.role.name === roleName;
  }
}