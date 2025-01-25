import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DatePipe } from '@angular/common';
import { SaleService } from '../../services/sale.service';
import { UserService } from '@core/auth/services/user.service';
import { ProductService } from '@features/products/services/product.service';
import { Sale, SaleDetail } from '@features/sales/model/sale';

@Component({
  selector: 'app-sale-form',
  templateUrl: './sale-form.component.html',
  styleUrls: ['./sale-form.component.scss'], // Asegúrate de que la ruta sea correcta
  providers: [DatePipe]
})
export class SaleFormComponent implements OnInit {
  form: FormGroup;
  isEditing: boolean = false;
  currentUser: any;
  products: any[] = [];
  saleDetails: SaleDetail[] = [];

  constructor(
    private fb: FormBuilder,
    private datePipe: DatePipe,
    private saleService: SaleService,
    private userService: UserService,
    private productService: ProductService,
    public dialogRef: MatDialogRef<SaleFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { sale: Sale }
  ) {
    this.form = this.fb.group({
      saleDate: [this.datePipe.transform(new Date(), 'yyyy-MM-dd'), Validators.required],
      totalAmount: [0, [Validators.required, Validators.min(0)]],
      discountAmount: [0, [Validators.required, Validators.min(0)]],
      finalAmount: [0, [Validators.required, Validators.min(0)]],
      status: ['PENDING', Validators.required],
      paymentType: ['CASH', Validators.required],
      notes: ['']
    });

    if (this.data && this.data.sale) {
      this.isEditing = true;
      this.loadFormData(this.data.sale);
    }
  }

  ngOnInit(): void {
    this.currentUser = this.userService.getCurrentUser();
    this.loadProducts();
    this.calculateFinalAmount();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe(products => {
      this.products = products;
    });
  }

  loadFormData(sale: Sale): void {
    this.form.patchValue({
      saleDate: this.datePipe.transform(sale.saleDate, 'yyyy-MM-dd'),
      totalAmount: sale.totalAmount,
      discountAmount: sale.discountAmount,
      finalAmount: sale.finalAmount,
      status: sale.status,
      paymentType: sale.paymentType,
      notes: sale.notes
    });

    // Cargar detalles de la venta si estamos editando
    if (sale.saleDetails) {
      this.saleDetails = sale.saleDetails;
      this.calculateTotal();
    }
  }

  addDetail(): void {
    this.saleDetails.push({ productId: null, quantity: 1, unitPrice: 0, subtotal: 0 });
  }

  removeDetail(index: number): void {
    this.saleDetails.splice(index, 1);
    this.calculateTotal();
  }

  calculateTotal(): void {
    let total = 0;
    this.saleDetails.forEach(detail => {
      const product = this.products.find(p => p.id === detail.productId);
      if (product) {
        detail.unitPrice = product.price;
        detail.subtotal = product.price * detail.quantity;
        total += detail.subtotal;
      }
    });
    this.form.get('totalAmount')?.setValue(total);
    this.calculateFinalAmount();
  }

  calculateFinalAmount(): void {
    const total = this.form.get('totalAmount')?.value || 0;
    const discount = this.form.get('discountAmount')?.value || 0;
    this.form.get('finalAmount')?.setValue(total - discount, { emitEvent: false });
  }

  onSubmit(): void {
    if (this.form.valid) {
      const saleData = {
        ...this.form.value,
        saleDate: new Date(this.form.value.saleDate),
        saleDetails: this.saleDetails,
        createdBy: this.currentUser.id,
        approvedBy: this.currentUser.role === 'ADMIN' ? this.currentUser.id : null
      };

      if (this.isEditing) {
        this.saleService.updateSale(this.data.sale.id, saleData).subscribe({
          next: () => this.dialogRef.close(true),
          error: (err) => console.error('Error updating sale:', err)
        });
      } else {
        this.saleService.createSale(saleData).subscribe({
          next: () => this.dialogRef.close(true),
          error: (err) => console.error('Error creating sale:', err)
        });
      }
    }
  }
}