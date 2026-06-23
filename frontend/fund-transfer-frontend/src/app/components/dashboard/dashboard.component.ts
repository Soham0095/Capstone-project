import { Component, inject, signal, effect, untracked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountStore } from '../services/account-store.service';
import { AccountService } from '../services/account.service';
import { RewardService } from '../services/reward.service';
import { Footer } from '../footer/footer';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, Footer, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

  accountStore = inject(AccountStore);
  accountService = inject(AccountService);
  rewardService = inject(RewardService);

  rewardPoints = signal<number>(0);

  showModal = false;
  currentAction: 'DEPOSIT' | 'WITHDRAW' = 'DEPOSIT';
  transactionAmount: number | null = null;
  errorMessage = '';
  successMessage = '';
  showToast = false;
  toastMessage = '';
  toastType: 'success' | 'error' = 'success';
  isSubmitting = false;

  constructor(private router: Router) {
    /**
     * effect() tracks accountStore.account() as a reactive dependency.
     * Whenever the account signal changes (login, fetchAccountDetails polling,
     * or post-transfer refresh), this re-runs and fetches the latest reward points.
     * untracked() stops the HTTP subscription itself from being tracked.
     */
    effect(() => {
      const account = this.accountStore.account();
      if (account?.id) {
        untracked(() => {
          this.rewardService.getRewards(account.id!).subscribe({
            next: (summary) => this.rewardPoints.set(summary.totalPoints ?? 0),
            error: () => console.warn('Could not load reward points for dashboard badge')
          });
        });
      } else {
        this.rewardPoints.set(0);
      }
    });
  }

  navigateTo(path: string) {
    this.router.navigateByUrl(path);
  }

  openDepositModal(): void {
    this.currentAction = 'DEPOSIT';
    this.transactionAmount = null;
    this.errorMessage = '';
    this.successMessage = '';
    this.showModal = true;
  }

  openWithdrawModal(): void {
    this.currentAction = 'WITHDRAW';
    this.transactionAmount = null;
    this.errorMessage = '';
    this.successMessage = '';
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.errorMessage = '';
    this.successMessage = '';
  }

  submitTransaction(): void {
    if (this.isSubmitting) return;

    this.errorMessage = '';
    this.successMessage = '';

    if (!this.transactionAmount || this.transactionAmount <= 0) {
      this.errorMessage = '❌ Please enter a valid amount greater than 0.';
      return;
    }

    const accountId = this.accountStore.getAccount()?.id;
    if (!accountId) {
      this.errorMessage = '❌ Account ID not found!';
      return;
    }

    if (this.currentAction === 'WITHDRAW') {
      const currentBalance = this.accountStore.getAccount()?.balance || 0;
      if (this.transactionAmount > currentBalance) {
        this.errorMessage = `❌ Insufficient balance! Available: ₹${currentBalance}`;
        return;
      }
    }

    this.isSubmitting = true;
    this.closeModal();

    this.accountService.updateBalance(accountId, this.transactionAmount, this.currentAction).subscribe({
      next: (response) => {
        this.closeModal();
        console.log(`${this.currentAction} successful:`, response);

        // Update the account store with balance returned from server
        // Note: setAccount triggers the effect above which re-fetches reward points
        const currentAccount = this.accountStore.getAccount();
        if (currentAccount) {
          this.accountStore.setAccount({
            ...currentAccount,
            balance: response.newBalance
          });
        }

        this.toastMessage = `✅ ${this.currentAction === 'DEPOSIT' ? 'Deposit' : 'Withdrawal'} successful! Amount: ₹${this.transactionAmount}`;
        this.toastType = 'success';
        this.showToast = true;
        this.isSubmitting = false;

        setTimeout(() => { this.showToast = false; }, 3000);
      },
      error: (error) => {
        console.error(`${this.currentAction} failed:`, error);
        const errorMsg = error?.error?.message || 'Please try again.';
        this.toastMessage = `❌ ${this.currentAction === 'DEPOSIT' ? 'Deposit' : 'Withdrawal'} failed! ${errorMsg}`;
        this.toastType = 'error';
        this.showToast = true;
        this.isSubmitting = false;

        setTimeout(() => { this.showToast = false; }, 3000);
      }
    });
  }
}
