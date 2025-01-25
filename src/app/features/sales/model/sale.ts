export interface Sale {
  saleDetails: any;
  id: number;
  saleDate: string;
  totalAmount: number;
  discountAmount: number;
  finalAmount: number;
  status: string; // PENDING, COMPLETED, CANCELLED
  paymentType: string;
  notes?: string;
  createdBy: number;
  approvedBy?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface SaleDetail {
  id?: number; // Opcional porque se genera en la base de datos
  saleId?: number; // Se asignará al crear la venta
  productId: number | null; // Permitir null
  quantity: number; // Cantidad del producto
  unitPrice: number; // Precio unitario del producto
  subtotal: number; // Cantidad * Precio unitario
  createdAt?: Date; // Opcional, se genera en la base de datos
}