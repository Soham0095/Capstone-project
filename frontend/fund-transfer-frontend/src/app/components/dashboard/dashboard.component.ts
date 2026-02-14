import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountStore } from '../services/account-store.service';
import { AccountService } from '../services/account.service';
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

  showModal = false;
  currentAction: 'DEPOSIT' | 'WITHDRAW' = 'DEPOSIT';
  transactionAmount: number | null = null;
  errorMessage = '';
  successMessage = '';
  showToast = false;
  toastMessage = '';
  toastType: 'success' | 'error' = 'success';
  isSubmitting = false;

  constructor(private router: Router) { }

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
        
        // Update the account store with new balance

        const currentAccount = this.accountStore.getAccount();

if (currentAccount && this.transactionAmount) {
  const updatedAccount = {
    ...currentAccount,
    balance: this.currentAction === 'DEPOSIT'
      ? currentAccount.balance + this.transactionAmount
      : currentAccount.balance - this.transactionAmount
  };

  this.accountStore.setAccount(updatedAccount);
}

        

        // Show success toast immediately
        this.toastMessage = `✅ ${this.currentAction === 'DEPOSIT' ? 'Deposit' : 'Withdrawal'} successful! Amount: ₹${this.transactionAmount}`;
        this.toastType = 'success';
        this.showToast = true;
        this.isSubmitting = false;
        
        // Hide toast after 3 seconds
        setTimeout(() => {
          this.showToast = false;
        }, 3000);
      },
      error: (error) => {
        console.error(`${this.currentAction} failed:`, error);
        const errorMsg = error?.error?.message || 'Please try again.';
        this.toastMessage = `❌ ${this.currentAction === 'DEPOSIT' ? 'Deposit' : 'Withdrawal'} failed! ${errorMsg}`;
        this.toastType = 'error';
        this.showToast = true;
        this.isSubmitting = false;

        // Hide toast after 3 seconds
        setTimeout(() => {
          this.showToast = false;
        }, 3000);
      }
    });
  }
}
