import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../components/services/auth.service';

/**
 * JWT HTTP Interceptor
 *
 * 1. Attaches "Authorization: Bearer <token>" to every outgoing request
 *    (except /login and /newAccount which are public).
 *
 * 2. Catches 401 Unauthorized responses — clears the token and redirects
 *    to login. This handles expired tokens or missing JWT gracefully.
 *
 * To see in Chrome DevTools → Network tab:
 *   → Click any API request (e.g. GET /accounts/1)
 *   → Headers tab → Request Headers
 *   → You will see: Authorization: Bearer eyJhbGci...
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  // Skip adding token for public endpoints
  const isPublicEndpoint =
    req.url.endsWith('/login') ||
    req.url.endsWith('/newAccount');

  const token = auth.getToken();

  // Attach token to request if available
  const authReq = (token && !isPublicEndpoint)
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        // Token is missing, expired, or invalid → force re-login
        console.warn('JWT expired or invalid. Redirecting to login...');
        auth.logout();           // clears localStorage token + account store
        router.navigate(['/']); // go back to login page
      }
      return throwError(() => error);
    })
  );
};
