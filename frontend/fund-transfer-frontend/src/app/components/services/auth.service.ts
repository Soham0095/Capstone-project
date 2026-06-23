import { inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AccountStore } from './account-store.service';

interface LoginResponse {
  token: string;
  message: string;
  ok: boolean;
  body: any;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenKey = 'auth_token';
  private baseUrl = 'http://localhost:8090';
  private loginUrl = `${this.baseUrl}/login`;

  // Inject platform id to detect browser vs SSR server
  private platformId = inject(PLATFORM_ID);
  accountStoreService = inject(AccountStore);

  constructor(private http: HttpClient, private router: Router) { }

  /**
   * POST /login — validates credentials, receives JWT, stores it in localStorage.
   * Network tab: Look at POST /login → Response → you will see the "token" field.
   */
  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.loginUrl, { username, password }).pipe(
      tap((res) => {
        if (res && res.ok && res.token) {
          this.setToken(res.token);
          console.log('JWT token saved to localStorage');
        } else {
          console.log('Login Failed!');
        }
      })
    );
  }

  logout(): void {
    this.removeToken();
    this.accountStoreService.clear();
    this.router.navigate(['/']);
  }

  /** Returns the stored JWT token, or null if not logged in / running on SSR server. */
  getToken(): string | null {
    if (!isPlatformBrowser(this.platformId)) {
      return null; // localStorage does not exist on the SSR server
    }
    return localStorage.getItem(this.tokenKey);
  }

  /** True if a real JWT token exists in localStorage (browser only). */
  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  // --- Private helpers ---

  private setToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(this.tokenKey, token);
    }
  }

  private removeToken(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.tokenKey);
    }
  }
}
