export interface Permission {
    id: number;
    code: string;        // Código único para identificar el permiso
    name: string;        // Nombre descriptivo del permiso
    description?: string;
    status: boolean;
    createdAt: Date;
    updatedAt?: Date;
  }