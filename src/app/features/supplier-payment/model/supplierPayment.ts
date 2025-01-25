export interface SupplierPayment {
  id?: number; // Opcional porque se genera en la base de datos
  supplierId: number; // ID del proveedor
  amount: number; // Monto del pago
  paymentDate: Date; // Fecha del pago
  documentNumber?: string; // Número de comprobante (opcional)
  referenceNumber?: string; // Número de referencia (opcional)
  paymentType: string; // Tipo de pago (Efectivo, Transferencia, etc.)
  notes?: string; // Notas adicionales (opcional)
  status: string; // Estado del pago (PENDING, PARTIAL, COMPLETED)
  createdBy?: number; // ID del usuario que creó el pago
  createdAt?: Date; // Fecha de creación (opcional)
  updatedAt?: Date; // Fecha de actualización (opcional)
}