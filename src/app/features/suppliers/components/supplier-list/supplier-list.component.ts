import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { SupplierService } from '../../services/supplier.service';
import { Supplier } from '@features/suppliers/model/supplier';
import { SupplierFormComponent } from '../supplier-form/supplier-form.component';

@Component({
  selector: 'app-supplier-list',
  templateUrl: './supplier-list.component.html',
  styleUrls: ['./supplier-list.component.scss']
})
export class SupplierListComponent implements OnInit {
applyFilter($event: KeyboardEvent) {
throw new Error('Method not implemented.');
}
  displayedColumns: string[] = ['name', 'contactPerson', 'phone', 'email', 'status', 'actions'];
  dataSource: MatTableDataSource<Supplier>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private supplierService: SupplierService,
    private dialog: MatDialog
  ) {
    this.dataSource = new MatTableDataSource<Supplier>([]);
  }

  ngOnInit(): void {
    this.loadSuppliers();
  }

  // Cargar proveedores desde el servicio
  loadSuppliers(): void {
    this.supplierService.getSuppliers().subscribe({
      next: (suppliers) => {
        this.dataSource.data = suppliers;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => console.error('Error al cargar proveedores:', error)
    });
  }

  // Abrir el formulario para crear/editar un proveedor
  openSupplierForm(supplier?: Supplier): void {
    const dialogRef = this.dialog.open(SupplierFormComponent, {
      width: '600px',
      data: supplier ? { ...supplier } : null
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadSuppliers(); // Recargar la lista después de guardar
      }
    });
  }

  // Cambiar el estado de un proveedor (activar/desactivar)
  toggleStatus(supplier: Supplier): void {
    this.supplierService.toggleSupplierStatus(supplier.id, !supplier.status).subscribe({
      next: () => this.loadSuppliers(), // Recargar la lista después de actualizar
      error: (error) => console.error('Error al cambiar el estado:', error)
    });
  }
}