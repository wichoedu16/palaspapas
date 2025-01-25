// features/inventory/services/inventory.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

// Definimos la interfaz para los filtros que usaremos en las búsquedas
interface InventoryFilters {
  searchTerm?: string;
  type?: 'kitchen' | 'additional' | 'all';
  categoryId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  // Definimos la URL base para las operaciones de inventario
  private readonly API_URL = '/api/inventory';

  constructor(private http: HttpClient) {}

  // Obtener todos los ingredientes con opciones de filtrado
  getIngredients(filters?: InventoryFilters): Observable<any[]> {
    // Construimos los parámetros de búsqueda de forma dinámica
    let params = new HttpParams();
    if (filters) {
      Object.entries(filters).forEach(([key, value]) => {
        if (value) params = params.set(key, value.toString());
      });
    }

    return this.http.get<any[]>(this.API_URL, { params });
  }

  // Obtener un ingrediente específico por ID
  getIngredientById(id: number): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/${id}`);
  }

  // Crear un nuevo ingrediente
  createIngredient(ingredient: any): Observable<any> {
    return this.http.post<any>(this.API_URL, ingredient);
  }

  // Actualizar un ingrediente existente
  updateIngredient(id: number, ingredient: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/${id}`, ingredient);
  }

  // Actualizar solo el stock de un ingrediente
  updateStock(id: number, quantity: number, type: 'increment' | 'decrement'): Observable<any> {
    return this.http.patch<any>(`${this.API_URL}/${id}/stock`, {
      quantity,
      type
    });
  }

  // Obtener ingredientes con stock bajo
  getLowStockIngredients(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/low-stock`);
  }

  // Obtener las categorías disponibles
  getCategories(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/categories`);
  }
}