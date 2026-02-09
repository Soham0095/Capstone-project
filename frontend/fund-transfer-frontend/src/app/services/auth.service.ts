import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, of } from 'rxjs';

interface LoginResponse {
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenKey = 'auth_token';

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string): Observable<LoginResponse> {
    // Backend endpoint is unknown — defaulting to /api/auth/login
    return this.http.post<LoginResponse>('/api/auth/login', { username, password }).pipe(
      tap((res) => {
        if (res && res.token) {
          localStorage.setItem(this.tokenKey, res.token);
        }
      })
    );
  }

  // Convenience method for development/testing when backend is not available
  mockLogin(username: string, password: string): Observable<LoginResponse> {
    const fakeToken = btoa(username + ':' + password + ':' + Date.now());
    localStorage.setItem(this.tokenKey, fakeToken);
    return of({ token: fakeToken });
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.router.navigate(['/']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
