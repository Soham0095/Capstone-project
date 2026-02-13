import { inject, Injectable, signal, OnDestroy } from '@angular/core';
import { Account } from '../../models/account-model';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';



@Injectable({ providedIn: 'root' })
export class AccountStore implements OnDestroy {
  account = signal<Account | null>(null);
  private readonly baseUrl = 'http://localhost:8090/';
  private readonly accountUrl = `${this.baseUrl}accounts/`;
  private intervalId: any;

  messageService = inject(MessageService);


  constructor(private http: HttpClient) {
    this.startPolling();
  }

  private startPolling() {
    this.intervalId = setInterval(() => {
      this.fetchAccountDetails();
    }, 60000);
  }

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  fetchAccountDetails() {
    const currentAccount = this.account();
    if (!currentAccount || !currentAccount.id) {
      return;
    }

    this.http.get<Account>(`${this.accountUrl}${currentAccount.id}`).subscribe({
      next: (account) => {
        console.log(account);
        this.setAccount(account);
      },
      error: (error) => {
        console.error('Error fetching account details:', error);
      }
    });
  }


  setAccount(account: Account | null) {
    this.account.set(account);
  }

  getAccount(): Account | null {
    return this.account();
  }

  clear() {
    this.account.set(null);
  }
}
