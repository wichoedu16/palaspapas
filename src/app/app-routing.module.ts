import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '@core/auth/guards/auth.guard';
import { MainLayoutComponent } from '@shared/components/layout/main-layout/main-layout.component';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () =>
      import('./features/auth/auth.module').then((m) => m.AuthModule),
  },
  {
    path: '',
    component: MainLayoutComponent, // Este es el punto clave
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./features/dashboard/dashboard.module').then(
            (m) => m.DashboardModule
          ),
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full',
      },
      {
        path: 'inventory',
        loadChildren: () =>
          import('./features/inventory/inventory.module').then(
            (m) => m.InventoryModule
          ),
      },
      {
        path: 'category',
        loadChildren: () =>
          import('./features/categories/categories.module').then(
            (m) => m.CategoriesModule
          ),
      },
      {
        path: 'supplier',
        loadChildren: () =>
          import('./features/suppliers/suppliers.module').then(
            (m) => m.SuppliersModule
          ),
      },
      {
        path: 'supplier-payment',
        loadChildren: () =>
          import('./features/supplier-payment/supplier-payment.module').then(
            (m) => m.SupplierPaymentModule
          ),
      },
      {
        path: 'product',
        loadChildren: () =>
          import('./features/products/products.module').then(
            (m) => m.ProductModule
          ),
      },
      {
        path: 'sale',
        loadChildren: () =>
          import('./features/sales/sales.module').then((m) => m.SaleModule),
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
