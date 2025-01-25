import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SupplierService } from '@features/suppliers/services/supplier.service';
import { SupplierPaymentService } from '../../services/supplier-payment.service';
import { SupplierPayment } from '@features/supplier-payment/model/supplierPayment';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-supplier-payment-form',
  templateUrl: './supplier-payment-form.component.html',
  styleUrls: ['./supplier-payment-form.component.scss'],
  providers: [DatePipe]
})
export class SupplierPaymentFormComponent implements OnInit {
  form!: FormGroup;
  suppliers: any[] = [];
  totalAmount: number = 0; // Monto total de la factura
  remainingAmount: number = 0; // Monto restante por pagar
  isEditing: boolean = false;

  constructor(
    private fb: FormBuilder,
    private supplierService: SupplierService,
    private supplierPaymentService: SupplierPaymentService,
    private datePipe: DatePipe,
    public dialogRef: MatDialogRef<SupplierPaymentFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { payment: SupplierPayment }
  ) {
    this.form = this.fb.group({
      supplierId: ['', Validators.required],
      amount: [0, [Validators.required, Validators.min(0)]],
      paymentDate: [this.datePipe.transform(new Date(), 'yyyy-MM-dd'), Validators.required],
      documentNumber: [''],
      referenceNumber: [''],
      paymentType: ['CASH', Validators.required],
      notes: [''],
      status: ['PARTIAL', Validators.required] // Por defecto, el pago es parcial
    });

    if (this.data && this.data.payment) {
      this.isEditing = true;
      this.loadFormData(this.data.payment);
    }
  }

  ngOnInit(): void {
    this.loadSuppliers();
    this.calculateRemainingAmount();
  }

  loadSuppliers(): void {
    this.supplierService.getSuppliers().subscribe(suppliers => {
      this.suppliers = suppliers;
    });
  }

  loadFormData(payment: SupplierPayment): void {
    this.form.patchValue({
      supplierId: payment.supplierId,
      amount: payment.amount,
      paymentDate: this.datePipe.transform(payment.paymentDate, 'yyyy-MM-dd'),
      documentNumber: payment.documentNumber,
      referenceNumber: payment.referenceNumber,
      paymentType: payment.paymentType,
      notes: payment.notes,
      status: payment.status
    });
  }

  calculateRemainingAmount(): void {
    const amountPaid = this.form.get('amount')?.value || 0;
    this.remainingAmount = this.totalAmount - amountPaid;

    // Si el monto pagado es igual o mayor al total, cambia el estado a "COMPLETED"
    if (this.remainingAmount <= 0) {
      this.form.get('status')?.setValue('COMPLETED');
    } else {
      this.form.get('status')?.setValue('PARTIAL');
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const paymentData = {
        ...this.form.value,
        paymentDate: new Date(this.form.value.paymentDate)
      };
  
      if (this.isEditing) {
        // Verificamos si el ID está definido
        if (this.data.payment.id !== undefined) {
          this.supplierPaymentService.updatePayment(this.data.payment.id, paymentData).subscribe({
            next: () => this.dialogRef.close(true),
            error: (err) => console.error('Error updating payment:', err)
          });
        } else {
          console.error('Payment ID is undefined');
        }
      } else {
        this.supplierPaymentService.createPayment(paymentData).subscribe({
          next: () => this.dialogRef.close(true),
          error: (err) => console.error('Error creating payment:', err)
        });
      }
    }
  }
}