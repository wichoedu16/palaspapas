import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { CategoryService } from '../../services/category.service';
import { Category } from '@features/categories/model/category';
import { CategoryFormComponent } from '../category-form/category-form.component';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.scss']
})
export class CategoryListComponent implements OnInit {
  displayedColumns: string[] = ['name', 'status', 'actions'];
  dataSource: MatTableDataSource<Category>;
  filterType: string = 'all'; // Filtro por defecto: todas las categorías

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private categoryService: CategoryService,
    private dialog: MatDialog
  ) {
    this.dataSource = new MatTableDataSource<Category>([]);
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  // Cargar las categorías desde el servicio
  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (categories) => {
        this.dataSource.data = categories;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => console.error('Error al cargar categorías:', error)
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

  // Filtrar categorías por estado (todas, activas, inactivas)
  filterCategories(): void {
    if (this.filterType === 'all') {
      this.dataSource.filter = '';
    } else {
      this.dataSource.filter = this.filterType === 'active' ? 'true' : 'false';
    }

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  // Abrir el formulario para crear/editar una categoría
  openCategoryForm(category?: Category): void {
    const dialogRef = this.dialog.open(CategoryFormComponent, {
      width: '600px',
      data: category ? { ...category } : null
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadCategories(); // Recargar la lista después de guardar
      }
    });
  }

  // Cambiar el estado de una categoría (activar/desactivar)
  toggleStatus(category: Category): void {
    const updatedCategory = { ...category, status: !category.status };
    this.categoryService.updateCategory(category.id, updatedCategory).subscribe({
      next: () => this.loadCategories(), // Recargar la lista después de actualizar
      error: (error) => console.error('Error al cambiar el estado:', error)
    });
  }
}