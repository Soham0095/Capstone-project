import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { AccountStore } from '../components/services/account-store.service';

export const authGuard: CanActivateFn = (route, state): boolean | UrlTree => {
  const router = inject(Router);
  const accountStore = inject(AccountStore);

  // Adjust based on how your store exposes the current user:
  const account = accountStore.getAccount(); // Account | null (synchronous)
  const url = state.url;

  // Root path '/' is the login page
  const isAuthPage = url === '/' || url.startsWith('/signup');

  if (!account) {
    // Unauthenticated: allow only auth pages
    return isAuthPage ? true : router.parseUrl('/');
  }

  // Authenticated: block auth pages, allow everything else
  return isAuthPage ? router.parseUrl('/dashboard') : true;
};