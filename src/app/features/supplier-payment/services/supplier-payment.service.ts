import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SupplierPayment } from '../model/supplierPayment';
import { environment } from 'src/enviroment/enviroment';

@Injectable({
  providedIn: 'root',
})
export class SupplierPaymentService {
  private readonly API_URL = `${environment.apiUrl}/suppliers`; // Usa la URL del entorno

  constructor(private http: HttpClient) {}

  getPayments(): Observable<SupplierPayment[]> {
    return this.http.get<SupplierPayment[]>(this.API_URL);
  }

  createPayment(payment: SupplierPayment): Observable<SupplierPayment> {
    return this.http.post<SupplierPayment>(this.API_URL, payment);
  }

  updatePayment(id: number, payment: SupplierPayment): Observable<SupplierPayment> {
    return this.http.put<SupplierPayment>(`${this.API_URL}/${id}`, payment);
  }

  getPaymentsBySupplier(supplierId: number): Observable<SupplierPayment[]> {
    return this.http.get<SupplierPayment[]>(`${this.API_URL}?supplierId=${supplierId}`);
  }
}