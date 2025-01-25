import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SupplierService } from '../../services/supplier.service';
import { Supplier } from '@features/suppliers/model/supplier';

@Component({
  selector: 'app-supplier-form',
  templateUrl: './supplier-form.component.html',
  styleUrls: ['./supplier-form.component.scss']
})
export class SupplierFormComponent {
  form!: FormGroup;
  isEditing: boolean = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<SupplierFormComponent>,
    private supplierService: SupplierService,
    @Inject(MAT_DIALOG_DATA) public data?: Supplier
  ) {
    this.isEditing = !!data;
    this.initForm();
  }

  private initForm(): void {
    this.form = this.fb.group({
      name: [this.data?.name || '', Validators.required],
      documentNumber: [this.data?.documentNumber || ''],
      contactPerson: [this.data?.contactPerson || ''],
      phone: [this.data?.phone || ''],
      email: [this.data?.email || '']
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const request = this.isEditing
      ? this.supplierService.updateSupplier(this.data!.id, this.form.value)
      : this.supplierService.createSupplier(this.form.value);

    request.subscribe({
      next: (result) => this.dialogRef.close(result),
      error: (error) => console.error('Error:', error)
    });
  }
}