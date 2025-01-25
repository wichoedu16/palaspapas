export interface Supplier {
  id: number;
  name: string;
  documentNumber?: string;
  contactPerson?: string;
  phone?: string;
  email?: string;
  status: boolean;
  createdAt?: string;
  updatedAt?: string;
}
