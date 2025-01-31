import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/auth/services/auth.service';
import { DashboardService } from '../dashboard.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  currentUser$ = this.authService.currentUser$;
  isAdmin = false;

  constructor(
    private authService: AuthService,
    private dashboardService: DashboardService,
    private router: Router
  ) {  }

  // Datos para las tarjetas de resumen
  summaryCards = [
    {
      title: 'Ventas del Día Anterior',
      value: 'Cargando...',
      icon: 'point_of_sale',
      color: 'primary',
      requiresAdmin: this.isAdmin,
    },
    {
      title: 'Estado de Caja',
      value: 'Cargando...',
      icon: 'account_balance',
      color: 'warn',
      requiresAdmin: this.isAdmin,
    },
    {
      title: 'Productos Bajo Stock',
      value: 'Cargando...',
      icon: 'inventory',
      color: 'accent',
      requiresAdmin: false,
    },
  ];

  ngOnInit(): void {
    this.loadDashboardData();
  }

  // Método para navegar a Pagar a Proveedores
  navigateToPaySuppliers(): void {
    this.router.navigate(['/supplier-payment']);
  }

  // Método para navegar a Realizar Venta
  navigateToMakeSale(): void {
    this.router.navigate(['/sale']);
  }

  // Método para navegar a Revisar Inventario
  navigateToCheckInventory(): void {
    this.router.navigate(['/inventory']);
  }

  private loadDashboardData(): void {
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN');
    console.log('es administrador:', this.isAdmin)
    this.dashboardService.getDashboardData().subscribe({
      next: (data) => {
        this.summaryCards[0].value = `$${data.yesterdaySales}`; // Ventas del día anterior
        this.summaryCards[1].value = data.cashStatus; // Estado de la caja
        this.summaryCards[2].value = data.lowStockProducts.toString(); // Productos bajo stock
      },
      error: (error) => {
        console.error('Error al cargar los datos del dashboard:', error);
        this.summaryCards.forEach((card) => (card.value = 'Error'));
      },
    });
  }

}