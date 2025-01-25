import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sale } from '@features/sales/model/sale';

@Injectable({
  providedIn: 'root'
})
export class SaleService {
  private apiUrl = `/api/sales`;

  constructor(private http: HttpClient) {}

  // Obtener todas las ventas
  getSales(): Observable<Sale[]> {
    return this.http.get<Sale[]>(this.apiUrl);
  }

  // Obtener una venta por ID
  getSaleById(id: number): Observable<Sale> {
    return this.http.get<Sale>(`${this.apiUrl}/${id}`);
  }

  // Crear una nueva venta
  createSale(sale: Sale): Observable<Sale> {
    return this.http.post<Sale>(this.apiUrl, sale);
  }

  // Actualizar una venta existente
  updateSale(id: number, sale: Sale): Observable<Sale> {
    return this.http.put<Sale>(`${this.apiUrl}/${id}`, sale);
  }

  // Cambiar el estado de una venta
  updateSaleStatus(id: number, status: string): Observable<Sale> {
    return this.http.patch<Sale>(`${this.apiUrl}/${id}/status`, { status });
  }
}