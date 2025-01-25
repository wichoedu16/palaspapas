import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { SaleService } from '../../services/sale.service';
import { Sale } from '@features/sales/model/sale';
import { SaleFormComponent } from '../sale-form/sale-form.component';

@Component({
  selector: 'app-sale-list',
  templateUrl: './sale-list.component.html',
  styleUrls: ['./sale-list.component.scss']
})
export class SaleListComponent implements OnInit {
  displayedColumns: string[] = ['saleDate', 'totalAmount', 'discountAmount', 'finalAmount', 'status', 'actions'];
  dataSource: MatTableDataSource<Sale>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private saleService: SaleService,
    private dialog: MatDialog
  ) {
    this.dataSource = new MatTableDataSource<Sale>([]);
  }

  ngOnInit(): void {
    this.loadSales();
  }

  // Cargar ventas desde el servicio
  loadSales(): void {
    this.saleService.getSales().subscribe({
      next: (sales) => {
        this.dataSource.data = sales;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => console.error('Error al cargar ventas:', error)
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

  // Abrir el formulario para crear/editar una venta
  openSaleForm(sale?: Sale): void {
    const dialogRef = this.dialog.open(SaleFormComponent, {
      width: '800px',
      data: sale ? { ...sale } : null
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadSales(); // Recargar la lista después de guardar
      }
    });
  }

  // Cambiar el estado de una venta
  updateStatus(sale: Sale, status: string): void {
    this.saleService.updateSaleStatus(sale.id, status).subscribe({
      next: () => this.loadSales(), // Recargar la lista después de actualizar
      error: (error) => console.error('Error al cambiar el estado:', error)
    });
  }
}