export interface Category {
    id: number;
    name: string;
    description?: string;
    status: boolean;
    createdAt: Date;
    updatedAt?: Date;
  }