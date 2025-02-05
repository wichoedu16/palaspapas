import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Permission } from '@features/permissions/model/permission';
import { PermissionService } from '@features/permissions/services/permission.service';
import { PermissionFormComponent } from '../permission-form/permission-form.component';

@Component({
  selector: 'app-permission-list',
  templateUrl: './permission-list.component.html',
  styleUrls: ['./permission-list.component.scss'],
})
export class PermissionListComponent implements OnInit {
  displayedColumns: string[] = ['code', 'name', 'status', 'actions'];
  dataSource: MatTableDataSource<Permission>;
  filterType: string = 'all'; // Filtro por defecto: todas las categorías

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private permissionService: PermissionService,
    private dialog: MatDialog
  ) {
    this.dataSource = new MatTableDataSource<Permission>([]);
  }

  ngOnInit(): void {
    this.loadPermissions();
  }

  // Cargar los permisos desde el servicio
  loadPermissions(): void {
    this.permissionService.getPermissions().subscribe({
      next: (permissions) => {
        this.dataSource.data = permissions;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => console.error('Error al cargar permisos:', error),
    });
  }

  // Aplicar filtro de búsqueda
  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  // Filtrar permisos por estado (todas, activas, inactivas)
  filterPermissions(): void {
    if (this.filterType === 'all') {
      this.dataSource.filter = '';
    } else {
      this.dataSource.filter = this.filterType === 'active' ? 'true' : 'false';
    }

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  // Abrir el formulario para crear/editar un permiso
  openPermissionForm(permission?: Permission): void {
    const dialogRef = this.dialog.open(PermissionFormComponent, {
      width: '600px',
      data: permission ? { ...permission } : null,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadPermissions(); // Recargar la lista después de guardar
      }
    });
  }

  // Cambiar el estado de un permiso (activar/desactivar)
  toggleStatus(permission: Permission): void {
    // Cambiar el estado: si es "A" lo cambia a "I", y viceversa
    const newStatus = permission.status === 'A' ? 'I' : 'A';
    const updatedPermission = { ...permission, status: newStatus };

    this.permissionService
      .updatePermission(permission.id, updatedPermission)
      .subscribe({
        next: () => this.loadPermissions(),
        error: (error) => console.error('Error al cambiar el estado:', error),
      });
  }
}
