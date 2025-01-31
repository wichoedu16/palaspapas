import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sale } from '@features/sales/model/sale';
import { environment } from 'src/enviroment/enviroment';

@Injectable({
  providedIn: 'root'
})
export class SaleService {
  private readonly API_URL = `${environment.apiUrl}/sales`; // Usa la URL del entorno

  constructor(private http: HttpClient) {}

  // Obtener todas las ventas
  getSales(): Observable<Sale[]> {
    return this.http.get<Sale[]>(this.API_URL);
  }

  // Obtener una venta por ID
  getSaleById(id: number): Observable<Sale> {
    return this.http.get<Sale>(`${this.API_URL}/${id}`);
  }

  // Crear una nueva venta
  createSale(sale: Sale): Observable<Sale> {
    return this.http.post<Sale>(this.API_URL, sale);
  }

  // Actualizar una venta existente
  updateSale(id: number, sale: Sale): Observable<Sale> {
    return this.http.put<Sale>(`${this.API_URL}/${id}`, sale);
  }

  // Cambiar el estado de una venta
  updateSaleStatus(id: number, status: string): Observable<Sale> {
    return this.http.patch<Sale>(`${this.API_URL}/${id}/status`, { status });
  }
}