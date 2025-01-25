// shared/components/layout/sidebar/sidebar.component.ts
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from '@core/auth/services/auth.service';

// Definimos una interfaz para los ítems del menú
interface MenuItem {
  label: string; // Texto que se mostrará
  icon: string; // Icono de Material
  route: string; // Ruta de navegación
  requiredRole?: string; // Rol requerido para ver este ítem
  children?: MenuItem[]; // Submenús si son necesarios
}

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  // Observable que contendrá los ítems del menú filtrados por rol
  menuItems$: Observable<MenuItem[]>;

  // Definimos todos los ítems del menú posibles
  private readonly ALL_MENU_ITEMS: MenuItem[] = [
    {
      label: 'Inicio',
      icon: 'dashboard',
      route: '/dashboard',
      requiredRole: 'ANY', 
    },
    {
      label: 'Categorias',
      icon: 'backup_table',
      route: '/category',
      requiredRole: 'ANY',
    },
    {
      label: 'Platos',
      icon: 'fastfood',
      route: '/product',
      requiredRole: 'ANY',
    },
    {
      label: 'Venta',
      icon: 'add_shopping_cart',
      route: '/sale',
      requiredRole: 'ANY',
    },
    {
      label: 'Ingredientes',
      icon: 'kitchen',
      route: '/inventory',
      requiredRole: 'ANY',
      children: [
        {
          label: 'Inventario',
          icon: 'inventory',
          route: '/inventory',
          requiredRole: 'ANY'
        },
        {
          label: 'Movimientos',
          icon: 'sync_alt',
          route: '/inventory/movements',
          requiredRole: 'ADMIN',
        },
      ],
    },
    {
      label: 'Proveedores',
      icon: 'groups',
      route: '/supplier',
      requiredRole: 'ANY',
      children: [
        {
          label: 'Lista de Proveedores',
          icon: 'list',
          route: '/supplier',
          requiredRole: 'ANY',
        },
        {
          label: 'Pagos',
          icon: 'payments',
          route: '/supplier-payment',
          requiredRole: 'ANY',
        },
      ],
    },
  ];

  constructor(private authService: AuthService) {
    // Filtramos los ítems del menú según el rol del usuario
    this.menuItems$ = this.authService.currentUser$.pipe(
      map((user) => this.filterMenuItems(this.ALL_MENU_ITEMS, user?.role?.name))
    );
  }

  ngOnInit() {
    console.log('Sidebar component initialized');
    this.menuItems$.subscribe(items => {
      console.log('Menu items:', items);
    });
  }

  // Método para filtrar los ítems del menú según el rol
  private filterMenuItems(items: MenuItem[], userRole?: string): MenuItem[] {
    return items.filter((item) => {
      // Si el ítem es visible para todos los roles o el usuario tiene el rol requerido
      const hasPermission =
        item.requiredRole === 'ANY' || item.requiredRole === userRole;

      // Si tiene submenús, los filtramos recursivamente
      if (hasPermission && item.children) {
        item.children = this.filterMenuItems(item.children, userRole);
      }

      return hasPermission;
    });
  }
}
