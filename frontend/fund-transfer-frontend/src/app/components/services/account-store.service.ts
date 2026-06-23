import { inject, Injectable, signal, OnDestroy, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Account } from '../../models/account-model';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class AccountStore implements OnDestroy {
  account = signal<Account | null>(null);
  rewardPoints = signal<number>(0);

  private readonly baseUrl = 'http://localhost:8090/';
  private readonly accountUrl = `${this.baseUrl}accounts/`;
  private readonly rewardsUrl = `${this.baseUrl}rewards/`;
  private intervalId: any;

  private platformId = inject(PLATFORM_ID);
  messageService = inject(MessageService);

  constructor(private http: HttpClient) {
    // Only start polling in the browser, not on the SSR server
    if (isPlatformBrowser(this.platformId)) {
      this.startPolling();
    }
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

  /** Fetches latest reward points from backend and updates the signal. */
  fetchRewardPoints() {
    const accountId = this.account()?.id;
    if (!accountId) return;

    this.http.get<{ totalPoints: number }>(`${this.rewardsUrl}${accountId}`).subscribe({
      next: (summary) => this.rewardPoints.set(summary?.totalPoints ?? 0),
      error: (e) => console.warn('Could not fetch reward points:', e)
    });
  }

  setAccount(account: Account | null) {
    this.account.set(account);
    // Automatically refresh reward points whenever account state changes
    if (account?.id) {
      this.fetchRewardPoints();
    }
  }

  getAccount(): Account | null {
    return this.account();
  }

  clear() {
    this.account.set(null);
    this.rewardPoints.set(0);
  }
}
