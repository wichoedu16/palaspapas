import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProductService } from '../../services/product.service';
import { Product } from '@features/products/model/product';
import { CategoryService } from '@features/categories/services/category.service';
import { Category } from '@features/categories/model/category';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss']
})
export class ProductFormComponent implements OnInit {
  form!: FormGroup;
  isEditing: boolean = false;
  categories: Category[] = [];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ProductFormComponent>,
    private productService: ProductService,
    private categoryService: CategoryService,
    @Inject(MAT_DIALOG_DATA) public data?: Product
  ) {
    this.isEditing = !!data;
    this.initForm();
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  private initForm(): void {
    this.form = this.fb.group({
      name: [this.data?.name || '', Validators.required],
      description: [this.data?.description || ''],
      price: [this.data?.price || 0, [Validators.required, Validators.min(0)]],
      categoryId: [this.data?.categoryId || null, Validators.required]
    });
  }

  private loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (categories) => (this.categories = categories),
      error: (error) => console.error('Error al cargar categorías:', error)
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const request = this.isEditing
      ? this.productService.updateProduct(this.data!.id, this.form.value)
      : this.productService.createProduct(this.form.value);

    request.subscribe({
      next: (result) => this.dialogRef.close(result),
      error: (error) => console.error('Error:', error)
    });
  }
}