import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SupplierPayment } from '../model/supplierPayment';

@Injectable({
  providedIn: 'root',
})
export class SupplierPaymentService {
  private readonly apiUrl = `/api/suppliers`; // URL de la API supplier-payment

  constructor(private http: HttpClient) {}

  getPayments(): Observable<SupplierPayment[]> {
    return this.http.get<SupplierPayment[]>(this.apiUrl);
  }

  createPayment(payment: SupplierPayment): Observable<SupplierPayment> {
    return this.http.post<SupplierPayment>(this.apiUrl, payment);
  }

  updatePayment(id: number, payment: SupplierPayment): Observable<SupplierPayment> {
    return this.http.put<SupplierPayment>(`${this.apiUrl}/${id}`, payment);
  }

  getPaymentsBySupplier(supplierId: number): Observable<SupplierPayment[]> {
    return this.http.get<SupplierPayment[]>(`${this.apiUrl}?supplierId=${supplierId}`);
  }
}