export interface Product {
    id: number;
    name: string;
    description?: string;
    categoryId: number;
    price: number;
    status: boolean;
    createdAt?: string;
    updatedAt?: string;
  }