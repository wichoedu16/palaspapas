import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Permission } from '@features/permissions/model/permission';
import { PermissionService } from '@features/permissions/services/permission.service';

@Component({
  selector: 'app-permission-form',
  templateUrl: './permission-form.component.html',
  styleUrls: ['./permission-form.component.scss']
})
export class PermissionFormComponent {
  form!: FormGroup;
  isEditing: boolean = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<PermissionFormComponent>,
    private permissionService: PermissionService,
    @Inject(MAT_DIALOG_DATA) public data?: Permission
  ) {
    this.isEditing = !!data;
    this.initForm();
  }

  private initForm(): void {
    this.form = this.fb.group({
      code: [this.data?.code || '', [Validators.required, Validators.minLength(3)]],
      name: [this.data?.name || ''],
      description: [this.data?.description || ''],
      status: [this.data?.status ?? true] 
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const request = this.isEditing
      ? this.permissionService.updatePermission(this.data!.id, this.form.value)
      : this.permissionService.createPermission(this.form.value);

    request.subscribe({
      next: (result) => this.dialogRef.close(result),
      error: (error) => console.error('Error:', error)
    });
  }
}