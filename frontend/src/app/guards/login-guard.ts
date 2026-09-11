import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { isTokenExpired } from '../utils/token-utils';

export const loginGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const auth = inject(AuthService);

  const token = auth.getUserDataFromStorage()?.token;

  if (token && !isTokenExpired(token)) {
    // return router.createUrlTree(['/admin']);
    router.navigate(['admin']);
    return false;
  }

  return true;
};
