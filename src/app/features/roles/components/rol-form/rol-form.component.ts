import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Permission } from '@features/permissions/model/permission';
import { PermissionService } from '@features/permissions/services/permission.service';
import { Role } from '@features/roles/model/role';
import { RolService } from '@features/roles/services/rol.service';

@Component({
  selector: 'app-rol-form',
  templateUrl: './rol-form.component.html',
  styleUrls: ['./rol-form.component.scss'],
})
export class RolFormComponent implements OnInit {
  form!: FormGroup;
  isEditing: boolean = false;
  permissions: Permission[] = [];
  allPermissionsSelected: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<RolFormComponent>,
    private roleService: RolService,
    private permissionService: PermissionService,
    @Inject(MAT_DIALOG_DATA) public data?: Role
  ) {
    this.isEditing = !!data;
  }

  ngOnInit() {
    this.loadPermissions();
  }

  private loadPermissions() {
    this.permissionService.getPermissions().subscribe((permissions) => {
      this.permissions = permissions;
      this.initForm();
    });
  }

  private initForm(): void {
    this.form = this.formBuilder.group({
      name: [this.data?.name || '', [Validators.required, Validators.minLength(3)],
      ],
      description: [this.data?.description || ''],
      status: [this.data?.status || 'A'],
      permissions: this.formBuilder.array([]),
    });

    this.permissions.forEach((permission) => {
      const isSelected =
        this.data?.permissions?.some((p) => p.id === permission.id) || false;
      this.permissionsFormArray.push(this.formBuilder.control(isSelected));
    });
  }

  get permissionsFormArray(): FormArray {
    return this.form.get('permissions') as FormArray;
  }

  get selectedPermissions(): number[] {
    return this.permissions
      .filter((_, index) => this.permissionsFormArray.at(index).value)
      .map((permission) => permission.id);
  }

  onSubmit(): void {
    if (this.form.invalid) return;
  
    const roleData = {
      ...this.form.value,
      permissionIds: this.selectedPermissions
    };
  
    const request = this.isEditing
      ? this.roleService.updateRole(this.data!.id, roleData)
      : this.roleService.createRole(roleData);
  
    request.subscribe({
      next: (result) => this.dialogRef.close(result),
      error: (error) => console.error('Error:', error),
    });
  }
}
