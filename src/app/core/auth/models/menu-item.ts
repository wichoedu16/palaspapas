export interface MenuItem {
  label: string;
  icon: string;
  route: string;
  requiredRole?: string;
  requiredPermission?: string;
  children?: MenuItem[];
  expanded?: boolean; // Para controlar la expansión de submenús
}