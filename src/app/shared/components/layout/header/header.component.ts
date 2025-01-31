// shared/components/layout/header/header.component.ts
import { Component, EventEmitter, Output } from '@angular/core';
import { AuthService } from '@core/auth/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  // Observable para obtener la información del usuario actual
  currentUser$ = this.authService.currentUser$;
  
  // EventEmitter para controlar la visibilidad del sidebar
  @Output() toggleSidebar = new EventEmitter<void>();

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  // Método para alternar la visibilidad del sidebar
  onToggleSidebar(): void {
    this.toggleSidebar.emit();
  }

  // Método para manejar el cierre de sesión
  onLogout() {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }
}