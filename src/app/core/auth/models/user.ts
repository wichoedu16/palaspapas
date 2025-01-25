import { Role } from "./role";

export interface User {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    role: Role;         // Relación con rol
    status: boolean;
    createdAt: Date;
    updatedAt?: Date;
  }