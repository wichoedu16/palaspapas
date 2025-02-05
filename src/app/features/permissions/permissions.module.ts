import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PermissionsRoutingModule } from './permissions-routing.module';
import { PermissionListComponent } from './components/permission-list/permission-list.component';
import { RouterModule, Routes } from '@angular/router';
import { PermissionFormComponent } from './components/permission-form/permission-form.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';

const routes: Routes = [
  {
    path: '',
    component: PermissionListComponent,
  },
];

@NgModule({
  declarations: [PermissionListComponent, PermissionFormComponent],
  imports: [
    CommonModule,
    PermissionsRoutingModule,
    RouterModule.forChild(routes),
    ReactiveFormsModule,
    FormsModule, // Importa FormsModule
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatMenuModule,
    MatSelectModule,
  ],
})
export class PermissionsModule {}
