// sidebar.component.ts
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from '@core/auth/services/auth.service';
import { Router } from '@angular/router';
import { MenuItem } from '@shared/models/menu-item';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  menuItems$!: Observable<MenuItem[]>;
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
          label: 'Permisos',
          icon: 'key',
          route: '/admin/permission',
        },
        {
          label: 'Roles',
          icon: 'security',
          route: '/admin/rol',
        },
        {
          label: 'Usuarios',
          icon: 'people',
          route: '/admin/user',
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

  constructor(
    private authService: AuthService, 
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}
 
  ngOnInit(): void {
    this.menuItems$ = this.authService.currentUser$.pipe(
      map((user) => {
        this.isAdmin = this.authService.hasRole('ROLE_ADMIN');
        const menuItems = this.isAdmin 
          ? this.ADMIN_MENU_ITEMS 
          : this.USER_MENU_ITEMS;
        
        this.updateExpandedState(this.router.url);
        
        return this.filterMenuItems(menuItems);
      })
    );
  }
 
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
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
    const menuItems = this.authService.hasRole('ROLE_ADMIN')
      ? this.ADMIN_MENU_ITEMS
      : this.USER_MENU_ITEMS;
 
    menuItems.forEach((item) => {
      if (item.children) {
        item.expanded = currentRoute.startsWith(item.route);
 
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
 
  toggleExpanded(item: MenuItem): void {
    if (item.children) {
      item.expanded = !item.expanded;
    }
  }
 }