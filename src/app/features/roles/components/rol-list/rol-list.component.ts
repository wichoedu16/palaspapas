import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Role } from '@features/roles/model/role';
import { RolService } from '@features/roles/services/rol.service';
import { RolFormComponent } from '../rol-form/rol-form.component';

@Component({
  selector: 'app-rol-list',
  templateUrl: './rol-list.component.html',
  styleUrls: ['./rol-list.component.scss'],
})
export class RolListComponent implements OnInit {
  displayedColumns: string[] = ['name', 'description', 'status', 'actions'];
  dataSource: MatTableDataSource<Role>;
  filterType: string = 'all'; // Filtro por defecto: todos los roles

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private roleService: RolService, private dialog: MatDialog) {
    this.dataSource = new MatTableDataSource<Role>([]);
  }

  ngOnInit(): void {
    this.loadRoles();
  }

  // Cargar los roles desde el servicio
  loadRoles(): void {
    this.roleService.getRoles().subscribe({
      next: (roles) => {
        this.dataSource.data = roles;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => console.error('Error al cargar roles:', error),
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

  // Filtrar por estado (todas, activas, inactivas)
  filterRoles(): void {
    if (this.filterType === 'all') {
      this.dataSource.filter = '';
    } else {
      this.dataSource.filter = this.filterType === 'active' ? 'true' : 'false';
    }

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  // Abrir el formulario para crear/editar
  openRoleForm(role?: Role): void {
    const dialogRef = this.dialog.open(RolFormComponent, {
      width: '600px',
      data: role ? { ...role } : null,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadRoles(); // Recargar la lista después de guardar
      }
    });
  }

  //Borrar
  deleteRole(id: number): void {
    this.roleService.deleteRole(id).subscribe({
      next: () => this.loadRoles(), // Recargar la lista después de eliminar
      error: (error) => console.error('Error deleting role:', error),
    });
  }

  // Cambiar el estado de un rol (activar/desactivar)
    toggleStatus(role: Role): void {
      // Cambiar el estado: si es "A" lo cambia a "I", y viceversa
      const newStatus = role.status === 'A' ? 'I' : 'A';
      const updatedRole = { ...role, status: newStatus };
  
      this.roleService
        .updateRole(role.id, updatedRole)
        .subscribe({
          next: () => this.loadRoles(),
          error: (error) => console.error('Error al cambiar el estado:', error),
        });
    }
}
