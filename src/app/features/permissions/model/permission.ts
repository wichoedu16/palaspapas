
export interface Permission {
  id: number;
  code: string;
  name: string;
  description?: string; 
  status?: string; 
  createdAt?: Date; 
  updatedAt?: Date; 
}
