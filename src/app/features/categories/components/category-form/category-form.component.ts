import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CategoryService } from '../../services/category.service';
import { Category } from '@features/categories/model/category';

@Component({
  selector: 'app-category-form',
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.scss']
})
export class CategoryFormComponent {
  form!: FormGroup;
  isEditing: boolean = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CategoryFormComponent>,
    private categoryService: CategoryService,
    @Inject(MAT_DIALOG_DATA) public data?: Category
  ) {
    this.isEditing = !!data;
    this.initForm();
  }

  private initForm(): void {
    this.form = this.fb.group({
      name: [this.data?.name || '', [Validators.required, Validators.minLength(3)]],
      description: [this.data?.description || ''],
      status: [this.data?.status ?? true] // Estado como booleano
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const request = this.isEditing
      ? this.categoryService.updateCategory(this.data!.id, this.form.value)
      : this.categoryService.createCategory(this.form.value);

    request.subscribe({
      next: (result) => this.dialogRef.close(result),
      error: (error) => console.error('Error:', error)
    });
  }
}