// features/inventory/services/inventory.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/enviroment/enviroment';

// Definimos la interfaz para los filtros que usaremos en las búsquedas
interface InventoryFilters {
  searchTerm?: string;
  type?: 'kitchen' | 'additional' | 'all';
  categoryId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly API_URL = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  getDashboardData(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/summary`);
  }
}