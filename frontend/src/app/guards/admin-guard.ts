import { isPlatformBrowser } from '@angular/common';
import { inject, PLATFORM_ID } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { isTokenExpired } from '../utils/token-utils';

export const adminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);
  const authService= inject(AuthService);

  // Si on est côté serveur (SSR), on refuse l'accès
  if (!isPlatformBrowser(platformId)) {
    return false;
  }

  const userData = authService.getUserDataFromStorage();
  const token = userData.token;
  const user = userData.user;

  // 
  if (!token || isTokenExpired(token)) {
    authService.logout();
    // Rediriger seulement si on n'est pas déjà sur la page login
    if (!state.url.includes('/admin/login')) {
      router.navigate(['admin/login'], {
        queryParams: { returnUrl: state.url }
      });
    }
    
    return false;
  }

  return true;
};
