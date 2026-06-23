import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { AccountStore } from '../components/services/account-store.service';
import { AuthService } from '../components/services/auth.service';

export const authGuard: CanActivateFn = (route, state): boolean | UrlTree => {
  const router = inject(Router);
  const accountStore = inject(AccountStore);
  const auth = inject(AuthService);

  const url = state.url;
  const isAuthPage = url === '/' || url.startsWith('/signup');

  // Check BOTH: in-memory account AND a valid JWT in localStorage.
  // If either is missing, the user must log in again.
  const hasToken = !!auth.getToken();
  const hasAccount = !!accountStore.getAccount();

  const isAuthenticated = hasToken && hasAccount;

  if (!isAuthenticated) {
    // If no token, clear any stale account state and go to login
    if (!hasToken) {
      accountStore.clear();
    }
    return isAuthPage ? true : router.parseUrl('/');
  }

  // Authenticated: block auth pages, allow everything else
  return isAuthPage ? router.parseUrl('/dashboard') : true;
};