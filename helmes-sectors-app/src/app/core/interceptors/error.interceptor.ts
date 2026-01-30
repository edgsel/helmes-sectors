import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { NotificationService } from '../services/notification.service';
import { ErrorResponseDTO } from '../../shared/models/api.models';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const notificationService = inject(NotificationService);
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !includesAuthUrl(req.url)) {
        authService.logout();
        return throwError(() => error);
      }

      const apiError = error.error as ErrorResponseDTO;
      const message = apiError?.description || `Error: ${error.status} - ${error.message}`;

      notificationService.showError(message);

      return throwError(() => error);
    })
  )
};

// check later
const includesAuthUrl = (reqUrl: string): boolean => {
  return reqUrl.includes('/login') || reqUrl.includes('/register');
}
