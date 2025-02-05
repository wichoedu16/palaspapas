import { Permission } from "@features/permissions/model/permission";

export interface Role {
  id: number;
  name: string;
  description: string;
  permissions: Permission[];
  status: string;
  createdAt: Date;
  updatedAt?: Date;
}
