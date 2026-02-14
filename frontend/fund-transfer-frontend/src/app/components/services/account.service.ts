import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { SignupRequest, ApiResponse } from '../../models/create-account-request';
import { Account } from '../../models/account-model';


@Injectable({ providedIn: 'root' })
export class AccountService {
  // Backend API endpoints
  private baseUrl = 'http://localhost:8090';
  private meUrl = `${this.baseUrl}/api/accounts/me`;
  private balanceUrl = (accountId: string) => `${this.baseUrl}/api/accounts/${accountId}/balance`;
  private signupUrl = `${this.baseUrl}/newAccount`;
  private updateBalanceUrl = `${this.baseUrl}/accounts/updateBalance`;

  constructor(private http: HttpClient, private auth: AuthService) { }

  getMyAccount(): Observable<Account> {
    const headers = this.auth.getToken()
      ? new HttpHeaders({ Authorization: `Bearer ${this.auth.getToken()}` })
      : undefined;

    return this.http.get<Account>(this.meUrl, headers ? { headers } : {}).pipe(
      catchError(() => {
        // mock details to simple displaay for now
        const mock: Account = {
          id: -1,
          holderName: 'John Smith',
          username: 'John Smith',
          balance: 45250.0,
          status: 'ACTIVE',
          version: 0
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
  signup(data: SignupRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(this.signupUrl, data).pipe(
      catchError((error) => {
        console.error('Signup error:', error);
        throw error;
      })
    );
  }

  updateBalance(accountId: number, amount: number, action: string): Observable<any> {
    const headers = this.auth.getToken()
      ? new HttpHeaders({ Authorization: `Bearer ${this.auth.getToken()}` })
      : undefined;

    const transactionRequest = {
      accountId: accountId,
      amount: amount,
      action: action
    };

    return this.http.patch<any>(this.updateBalanceUrl, transactionRequest, { 
      headers: headers ? headers : undefined,
      responseType: 'text' as 'json'
    }).pipe(
      catchError((error) => {
        console.error('Update balance error:', error);
        throw error;
      })
    );
  }

}
