// sidebar.component.ts
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { AuthService } from '@core/auth/services/auth.service';
import { Router } from '@angular/router';

interface MenuItem {
  label: string;
  icon: string;
  route: string;
  requiredRole?: string;
  requiredPermission?: string;
  children?: MenuItem[];
  expanded?: boolean; // Para controlar la expansión de submenús
}

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  menuItems$: Observable<MenuItem[]> | undefined;
  currentUser$ = this.authService.currentUser$;
  isAdmin = false;

  private readonly ADMIN_MENU_ITEMS: MenuItem[] = [
    {
      label: 'Inicio',
      icon: 'dashboard',
      route: '/dashboard',
    },
    {
      label: 'Administración',
      icon: 'admin_panel_settings',
      route: '/admin',
      children: [
        {
          label: 'Usuarios',
          icon: 'people',
          route: '/admin/users',
        },
        {
          label: 'Roles',
          icon: 'security',
          route: '/admin/roles',
        },
        {
          label: 'Permisos',
          icon: 'key',
          route: '/admin/permissions',
        },
      ],
    },
    {
      label: 'Proveedores',
      icon: 'people',
      route: '/suppliers',
      children: [
        {
          label: 'Listado Proveedores',
          icon: 'people',
          route: '/supplier',
        },
        {
          label: 'Pago Proveedores',
          icon: 'payment',
          route: '/supplier-payment',
        },
      ],
    },
    {
      label: 'Inventario',
      icon: 'inventory',
      route: '/inventory',
      children: [
        {
          label: 'Ingredientes',
          icon: 'kitchen',
          route: '/inventory/ingredients',
        },
        {
          label: 'Productos',
          icon: 'fastfood',
          route: '/inventory/products',
        },
        {
          label: 'Categorías',
          icon: 'category',
          route: '/inventory/categories',
        },
      ],
    },
    {
      label: 'Ventas',
      icon: 'point_of_sale',
      route: '/reports',
      children: [
        {
          label: 'Venta',
          icon: 'add_shopping_cart',
          route: '/sale',
        },
        {
          label: 'Reporte',
          icon: 'assessment',
          route: '/sale/inventory',
        },
      ],
    },
  ];

  private readonly USER_MENU_ITEMS: MenuItem[] = [
    {
      label: 'Inicio',
      icon: 'dashboard',
      requiredRole: 'ROLE_USER',
      route: '/dashboard',
    },
    {
      label: 'Proveedores',
      icon: 'people',
      route: '/suppliers',
      children: [
        {
          label: 'Listado Proveedores',
          icon: 'people',
          route: '/supplier',
        },
        {
          label: 'Pago Proveedores',
          icon: 'payment',
          route: '/supplier-payment',
        },
      ],
    },
    {
      label: 'Inventario',
      icon: 'inventory',
      route: '/inventory',
      children: [
        {
          label: 'Ingredientes',
          icon: 'kitchen',
          route: '/inventory/ingredients',
        },
        {
          label: 'Productos',
          icon: 'fastfood',
          route: '/inventory/products',
        },
        {
          label: 'Categorías',
          icon: 'category',
          route: '/inventory/categories',
        },
      ],
    },
    {
      label: 'Ventas',
      icon: 'add_shopping_cart',
      route: '/sale',
    },
  ];

  constructor(private authService: AuthService, private router: Router) {
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN');
    const menuItems = this.isAdmin
      ? this.ADMIN_MENU_ITEMS
      : this.USER_MENU_ITEMS;

    this.menuItems$ = this.authService.currentUser$.pipe(
      map(() => this.filterMenuItems(menuItems))
    );
    this.initializeMenu();
  }

  ngOnInit(): void {
    this.updateExpandedState(this.router.url);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initializeMenu(): void {
    this.menuItems$ = this.authService.currentUser$.pipe(
      tap((user) => {
        console.log('Usuario conectado:', user?.firstName, user?.role?.name); // Debug
        this.isAdmin = this.authService.hasRole('ROLE_ADMIN');
      }),
      map(() =>
        this.filterMenuItems(
          this.isAdmin ? this.ADMIN_MENU_ITEMS : this.USER_MENU_ITEMS
        )
      )
    );
  }

  private filterMenuItems(items: MenuItem[]): MenuItem[] {
    return items.filter((item) => {
      const hasRole =
        !item.requiredRole ||
        item.requiredRole === 'ANY' ||
        this.authService.hasRole(item.requiredRole);

      if (item.children) {
        const filteredChildren = this.filterMenuItems(item.children);
        item.children = filteredChildren;
        return filteredChildren.length > 0;
      }

      return hasRole;
    });
  }

  private updateExpandedState(currentRoute: string): void {
    // Obtener los ítems según el rol
    const menuItems = this.authService.hasRole('ROLE_ADMIN')
      ? this.ADMIN_MENU_ITEMS
      : this.USER_MENU_ITEMS;

    menuItems.forEach((item) => {
      if (item.children) {
        // Expande el menú si la ruta actual comienza con la ruta del ítem
        item.expanded = currentRoute.startsWith(item.route);

        // Recursivamente verifica los hijos
        item.children.forEach((child) => {
          if (currentRoute.startsWith(child.route)) {
            item.expanded = true;
          }
        });
      }
    });
  }

  onPanelOpened(selectedItem: MenuItem): void {
    const menuItems = this.authService.hasRole('ROLE_ADMIN')
      ? this.ADMIN_MENU_ITEMS
      : this.USER_MENU_ITEMS;

    menuItems.forEach((item) => {
      if (item.children && item !== selectedItem) {
        item.expanded = false;
      }
    });

    selectedItem.expanded = true;
  }

  onLogout(): void {
    this.authService.logout();
  }

  // private updateExpandedState(items: MenuItem[], currentRoute: string) {
  //   items.forEach((item) => {
  //     if (item.children) {
  //       item.expanded = currentRoute.startsWith(item.route);
  //       this.updateExpandedState(item.children, currentRoute);
  //     }
  //   });
  // }

  toggleExpanded(item: MenuItem) {
    if (item.children) {
      item.expanded = !item.expanded;
    }
  }

  // isActive(route: string): boolean {
  //   return this.activeRoute === route;
  // }
}
