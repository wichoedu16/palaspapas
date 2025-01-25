import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { SupplierPaymentService } from '../../services/supplier-payment.service';
import { SupplierPayment } from '@features/supplier-payment/model/supplierPayment';
import { MatDialog } from '@angular/material/dialog';
import { SupplierPaymentFormComponent } from '../supplier-payment-form/supplier-payment-form.component';

@Component({
  selector: 'app-supplier-payment-list',
  templateUrl: './supplier-payment-list.component.html',
  styleUrls: ['./supplier-payment-list.component.scss']
})
export class SupplierPaymentListComponent implements OnInit {
  displayedColumns: string[] = ['supplier', 'amount', 'paymentDate', 'status', 'actions'];
  dataSource: MatTableDataSource<SupplierPayment>;
  payments: SupplierPayment[] = [];
  searchText: string = '';
  filterStatus: string = 'ALL'; // Filtro por estado

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private supplierPaymentService: SupplierPaymentService,
    public dialog: MatDialog
  ) {
    this.dataSource = new MatTableDataSource(this.payments);
  }

  ngOnInit(): void {
    this.loadPayments();
  }

  loadPayments(): void {
    this.supplierPaymentService.getPayments().subscribe(payments => {
      this.payments = payments;
      this.applyFilter(); // Aplicar filtro después de cargar los datos
    });
  }

  applyFilter(): void {
    let filteredPayments = this.payments;

    // Filtrar por estado
    if (this.filterStatus !== 'ALL') {
      filteredPayments = this.payments.filter(payment => payment.status === this.filterStatus);
    }

    this.dataSource.data = filteredPayments;
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  onSearchChange(): void {
    this.applyFilter();
  }

  onFilterChange(): void {
    this.applyFilter();
  }

  openPaymentForm(payment?: SupplierPayment): void {
    const dialogRef = this.dialog.open(SupplierPaymentFormComponent, {
      width: '600px',
      data: { payment }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadPayments(); // Recargar la lista después de guardar
      }
    });
  }

}