import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '@features/products/model/product';
import { environment } from 'src/enviroment/enviroment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly API_URL = `${environment.apiUrl}/products`; // Usa la URL del entorno

  constructor(private http: HttpClient) {}

  // Obtener todos los productos
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.API_URL);
  }

  // Obtener un producto por ID
  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.API_URL}/${id}`);
  }

  // Crear un nuevo producto
  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.API_URL, product);
  }

  // Actualizar un producto existente
  updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.API_URL}/${id}`, product);
  }

  // Cambiar el estado de un producto (activar/desactivar)
  toggleProductStatus(id: number, status: boolean): Observable<Product> {
    return this.http.patch<Product>(`${this.API_URL}/${id}/status`, { status });
  }
}