// src/app/core/auth/guards/auth.guard.ts
import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
} from '@angular/router';
import { Observable, map, take } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], {
        queryParams: { returnUrl: state.url },
      });
      return false;
    }

    const requiredRole = route.data['role'];
    if (!requiredRole || requiredRole === 'ANY') {
      return true;
    }

    const hasRole = this.authService.hasRole(requiredRole);
    if (!hasRole) {
      this.router.navigate(['/unauthorized']);
      return false;
    }

    return true;
  }

  // canActivate(
  //   route: ActivatedRouteSnapshot,
  //   state: RouterStateSnapshot
  // ): Observable<boolean> | boolean {
  //   if (this.authService.isAuthenticated()) {
  //     const requiredRole = route.data['role'];
  //     if (requiredRole) {
  //       const hasRole = this.authService.getCurrentUserRole() === requiredRole;
  //       if (!hasRole) {
  //         this.router.navigate(['/unauthorized']);
  //         return false;
  //       }
  //     }
  //     return true;
  //   }

  //   this.router.navigate(['/login'], {
  //     queryParams: { returnUrl: state.url }
  //   });
  //   return false;
  // }
}
