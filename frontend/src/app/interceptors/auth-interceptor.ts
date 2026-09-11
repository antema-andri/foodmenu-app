import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth-service';
import { isTokenExpired } from '../utils/token-utils';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {

      const userData = authService.getUserDataFromStorage();
      const token = userData?.token;

      if (error.status === 401 && token) {
        window.location.reload();
      }

      return throwError(() => error);
    })
  );
};
