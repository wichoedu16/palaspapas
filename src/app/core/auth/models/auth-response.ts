import { User } from "./user";

export interface AuthResponse {
  user: User;
  token: string;
  refreshToken?: string;
  sessionId: number;  // Añadimos sessionId para tracking
}