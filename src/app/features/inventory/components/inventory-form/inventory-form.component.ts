import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { InventoryService } from '../../services/inventory.service';
import { Ingredient } from '@features/inventory/model/ingredient';

@Component({
  selector: 'app-inventory-form',
  templateUrl: './inventory-form.component.html',
  styleUrls: ['./inventory-form.component.scss']
})
export class InventoryFormComponent {
  form!: FormGroup;
  isEditing: boolean = false;
  unitMeasures: string[] = ['kg', 'g', 'l', 'ml', 'unidad']; // Ejemplo de unidades de medida

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<InventoryFormComponent>,
    private inventoryService: InventoryService,
    @Inject(MAT_DIALOG_DATA) public data?: Ingredient
  ) {
    this.isEditing = !!data;
    this.initForm();
  }

  private initForm(): void {
    this.form = this.fb.group({
      name: [this.data?.name || '', [Validators.required, Validators.minLength(3)]],
      description: [this.data?.description || ''],
      unitMeasure: [this.data?.unitMeasure || '', Validators.required],
      stock: [this.data?.stock || 0, [Validators.required, Validators.min(0)]]
    });
  }

  get nameControl() {
    return this.form.get('name');
  }

  get stockControl() {
    return this.form.get('stock');
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const request = this.isEditing
      ? this.inventoryService.updateIngredient(this.data!.id, this.form.value)
      : this.inventoryService.createIngredient(this.form.value);

    request.subscribe({
      next: (result) => this.dialogRef.close(result),
      error: (error) => console.error('Error:', error)
    });
  }
}