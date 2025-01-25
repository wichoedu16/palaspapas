import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { InventoryService } from '../../services/inventory.service';
import { Ingredient } from '@features/inventory/model/ingredient';
import { InventoryFormComponent } from '../inventory-form/inventory-form.component';

@Component({
  selector: 'app-inventory-list',
  templateUrl: './inventory-list.component.html',
  styleUrls: ['./inventory-list.component.scss']
})
export class InventoryListComponent implements OnInit {
  displayedColumns: string[] = ['name', 'stock', 'actions'];
  dataSource: MatTableDataSource<Ingredient>;
  filterType: string = 'all'; // Filtro por defecto: todos los ingredientes

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private inventoryService: InventoryService,
    private dialog: MatDialog
  ) {
    this.dataSource = new MatTableDataSource<Ingredient>([]);
  }

  ngOnInit(): void {
    this.loadIngredients();
  }

  // Cargar los ingredientes desde el servicio
  loadIngredients(): void {
    this.inventoryService.getIngredients().subscribe({
      next: (ingredients) => {
        this.dataSource.data = ingredients;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => console.error('Error al cargar ingredientes:', error)
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

  // Filtrar ingredientes por tipo (todos, cocina, adicionales)
  filterIngredients(): void {
    if (this.filterType === 'all') {
      this.dataSource.filter = '';
    } else {
      this.dataSource.filter = this.filterType;
    }

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  // Abrir el formulario para crear/editar un ingrediente
  openIngredientForm(ingredient?: Ingredient): void {
    const dialogRef = this.dialog.open(InventoryFormComponent, {
      width: '600px',
      data: ingredient ? { ...ingredient } : null
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadIngredients(); // Recargar la lista después de guardar
      }
    });
  }

  // Abrir el diálogo para ajustar el stock
  updateStock(ingredient: Ingredient): void {
    // const dialogRef = this.dialog.open(InventoryFormComponent, {
    //   width: '400px',
    //   data: { ...ingredient }
    // });

    // dialogRef.afterClosed().subscribe((result) => {
    //   if (result) {
    //     this.loadIngredients(); // Recargar la lista después de ajustar el stock
    //   }
    // });
  }
}