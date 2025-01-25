import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Supplier } from '@features/suppliers/model/supplier';

@Injectable({
  providedIn: 'root' // El servicio está disponible en toda la aplicación
})
export class SupplierService {
  private readonly apiUrl = `/api/suppliers`; // URL de la API supplier

  constructor(private http: HttpClient) {}

  // Obtener todos los proveedores
  getSuppliers(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(this.apiUrl);
  }

  // Obtener un proveedor por ID
  getSupplierById(id: number): Observable<Supplier> {
    return this.http.get<Supplier>(`${this.apiUrl}/${id}`);
  }

  // Crear un nuevo proveedor
  createSupplier(supplier: Supplier): Observable<Supplier> {
    return this.http.post<Supplier>(this.apiUrl, supplier);
  }

  // Actualizar un proveedor existente
  updateSupplier(id: number, supplier: Supplier): Observable<Supplier> {
    return this.http.put<Supplier>(`${this.apiUrl}/${id}`, supplier);
  }

  // Cambiar el estado de un proveedor (activar/desactivar)
  toggleSupplierStatus(id: number, status: boolean): Observable<Supplier> {
    return this.http.patch<Supplier>(`${this.apiUrl}/${id}/status`, { status });
  }

}