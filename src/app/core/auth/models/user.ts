export interface User {
  id?: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  role?: {
    id: number;
    name: string;
    permissions: Array<{
      id: number;
      code: string;
      name: string;
    }>;
  };
  status: boolean;
}