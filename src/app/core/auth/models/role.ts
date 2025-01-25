import { Permission } from "./permission";

export interface Role {
    id: number;
    name: string;
    // description?: string;
    permissions?: Permission[];
    status: boolean;
    // createdAt: Date;
    // updatedAt?: Date;
  }