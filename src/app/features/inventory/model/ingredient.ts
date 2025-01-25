export interface Ingredient {
  id: number;
  name: string;
  description: string;
  categoryId: number;
  unitMeasure: string;  // Por ejemplo: kg, litros, unidades
  stock: number;
  minimumStock: number;
  isAdditional: boolean;  // Diferencia entre ingredientes de cocina y adicionales
  isKitchen: boolean;
  cost: number;
  status: boolean;
  createdAt: Date;
  updatedAt?: Date;
}