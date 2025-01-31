import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Supplier } from '@features/suppliers/model/supplier';
import { environment } from 'src/enviroment/enviroment';

@Injectable({
  providedIn: 'root' // El servicio está disponible en toda la aplicación
})
export class SupplierService {
  private readonly API_URL = `${environment.apiUrl}/suppliers`; // Usa la URL del entorno

  constructor(private http: HttpClient) {}

  // Obtener todos los proveedores
  getSuppliers(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(this.API_URL);
  }

  // Obtener un proveedor por ID
  getSupplierById(id: number): Observable<Supplier> {
    return this.http.get<Supplier>(`${this.API_URL}/${id}`);
  }

  // Crear un nuevo proveedor
  createSupplier(supplier: Supplier): Observable<Supplier> {
    return this.http.post<Supplier>(this.API_URL, supplier);
  }

  // Actualizar un proveedor existente
  updateSupplier(id: number, supplier: Supplier): Observable<Supplier> {
    return this.http.put<Supplier>(`${this.API_URL}/${id}`, supplier);
  }

  // Cambiar el estado de un proveedor (activar/desactivar)
  toggleSupplierStatus(id: number, status: boolean): Observable<Supplier> {
    return this.http.patch<Supplier>(`${this.API_URL}/${id}/status`, { status });
  }

}