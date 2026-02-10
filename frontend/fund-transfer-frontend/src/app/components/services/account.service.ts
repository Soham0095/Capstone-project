import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Account } from './account-store.service';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class AccountService {
  //should replace later 
  private meUrl = '/api/accounts/me';
  private balanceUrl = (accountId: string) => `/api/accounts/${accountId}/balance`;

  constructor(private http: HttpClient, private auth: AuthService) {}

  getMyAccount(): Observable<Account> {
    const headers = this.auth.getToken()
      ? new HttpHeaders({ Authorization: `Bearer ${this.auth.getToken()}` })
      : undefined;

    return this.http.get<Account>(this.meUrl, headers ? { headers } : {}).pipe(
      catchError(() => {
        // mock details to simple displaay for now
        const mock: Account = {
          id: 'mock-1',
          name: 'John Smith',
          accountNumber: 'XXXX-XXXX-1234',
          availableBalance: 45250.0
        };
        return of(mock);
      })
    );
  }

  getBalance(accountId: string): Observable<number> {
    const headers = this.auth.getToken()
      ? new HttpHeaders({ Authorization: `Bearer ${this.auth.getToken()}` })
      : undefined;

    return this.http.get<{ balance: number }>(this.balanceUrl(accountId), headers ? { headers } : {}).pipe(
      map((r) => (r && r.balance) ? r.balance : 0),
      catchError(() => of(0))
    );
  }

  signup(username: string, password: string, name: string): Observable<{ message: string }> {
    // Placeholder endpoint — replace with real backend URL
    const signupUrl = '/api/auth/signup';

    return this.http.post<{ message: string }>(signupUrl, { username, password, name }).pipe(
      catchError(() => {
        // Mock success for development
        return of({ message: 'Account created successfully' });
      })
    );
  }
}
