import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, of } from 'rxjs';
import { AccountStore } from './account-store.service';

interface LoginResponse {
  // token: string;
  message: string;
  ok: boolean;
  body: any;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  // private readonly tokenKey = 'auth_token';
  private baseUrl = 'http://localhost:8090';
  private loginUrl = `${this.baseUrl}/login`;

  accountStoreService = inject(AccountStore);

  constructor(private http: HttpClient, private router: Router) { }

  //to be used when jwt is implemented
  // login(username: string, password: string): Observable<LoginResponse> {
  //   // Backend endpoint is unknown — defaulting to /api/auth/login
  //   return this.http.post<LoginResponse>('/api/auth/login', { username, password }).pipe(
  //     tap((res) => {
  //       if (res && res.token) {
  //         localStorage.setItem(this.tokenKey, res.token);
  //       }
  //     })
  //   );
  // }

  // Convenience method for development/testing when backend is not available
  // mockLogin(username: string, password: string): Observable<LoginResponse> {
  //   const fakeToken = btoa(username + ':' + password + ':' + Date.now());
  //   localStorage.setItem(this.tokenKey, fakeToken);
  //   return of({ token: fakeToken });
  // }

  //Used now for simple login using password comparision
  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.loginUrl, { username, password }).pipe(
      tap((res) => {
        if (res && res.ok) {
          console.log("Login Successful!");
        }
        else {
          console.log("Login Failed!");
        }
        return res;
      })
    );
  }

  logout(): void {
    // localStorage.removeItem(this.tokenKey);
    // this.router.navigate(['/']);
  }

  getToken(): string | null {
    // return localStorage.getItem(this.tokenKey);
    return "<temp>";
  }

  isAuthenticated(): any {
    // return !!this.getToken();

  }
}
