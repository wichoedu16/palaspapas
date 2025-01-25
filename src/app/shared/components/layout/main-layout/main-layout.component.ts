import { Component } from '@angular/core';
import { AuthService } from '@core/auth/services/auth.service';

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent {

  constructor(private authService: AuthService) {}

  // Estado del sidebar
  isSidebarExpanded = true;

  // Método para alternar el sidebar
  toggleSidebar(): void {
    this.isSidebarExpanded = !this.isSidebarExpanded;
  }
  
}
