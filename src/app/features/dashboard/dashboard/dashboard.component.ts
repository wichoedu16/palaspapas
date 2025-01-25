import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/auth/services/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  // Propiedades para mostrar información del usuario
  currentUser$ = this.authService.currentUser$;
  
  // Datos para las tarjetas de resumen (mock data por ahora)
  summaryCards = [
    {
      title: 'Ventas del Día Anterior',
      value: '$1,234.56',
      icon: 'point_of_sale',
      color: 'primary',
      // Esta información solo debería ser visible para administradores
      requiresAdmin: true
    },
    {
      title: 'Estado de Caja',
      value: 'Por Cerrar',
      icon: 'account_balance',
      color: 'warn',
      requiresAdmin: true
    },
    {
      title: 'Productos Bajo Stock',
      value: '5',
      icon: 'inventory',
      color: 'accent',
      requiresAdmin: false
    }
  ];
  
  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // Aquí cargaremos la información inicial del dashboard
  }
}