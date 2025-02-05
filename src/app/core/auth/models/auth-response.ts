import { User } from "../../../features/users/model/user";

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  user?: User;
}