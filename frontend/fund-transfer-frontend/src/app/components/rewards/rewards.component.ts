import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountStore } from '../services/account-store.service';
import { RewardService } from '../services/reward.service';
import { RewardLog } from '../../models/reward-log-model';
import { GoBackDirective } from '../../directives/go-back-directive';
import { Footer } from '../footer/footer';

@Component({
  selector: 'app-rewards',
  standalone: true,
  imports: [CommonModule, GoBackDirective, Footer],
  templateUrl: './rewards.component.html',
  styleUrls: ['./rewards.component.css']
})
export class RewardsComponent implements OnInit {
  accountStore = inject(AccountStore);
  rewardService = inject(RewardService);
  
  rewardLogs = signal<RewardLog[]>([]);
  isLoading = signal<boolean>(true);
  errorMessage = signal<string>('');

  ngOnInit(): void {
    this.fetchRewardLogs();
  }

  fetchRewardLogs(): void {
    const currentAccount = this.accountStore.getAccount();
    if (!currentAccount || !currentAccount.id) {
      this.isLoading.set(false);
      this.errorMessage.set('No active account found.');
      return;
    }

    this.isLoading.set(true);
    this.rewardService.getRewardLogs(currentAccount.id).subscribe({
      next: (logs) => {
        this.rewardLogs.set(logs || []);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error fetching reward logs:', error);
        this.errorMessage.set('Failed to load reward history.');
        this.isLoading.set(false);
      }
    });
  }

  redeemAll(): void {
    const currentAccount = this.accountStore.getAccount();
    if (!currentAccount || !currentAccount.id) return;

    const available = currentAccount.rewardPoints || 0;
    if (available <= 0) {
      this.errorMessage.set('No reward points available to redeem.');
      return;
    }

    const confirmed = window.confirm(`Redeem all ${available} points for Rs. ${available}?`);
    if (!confirmed) return;

    this.rewardService.redeemPoints(currentAccount.id, available).subscribe({
      next: (res) => {
        // trigger account refresh from store and refresh logs
        this.accountStore.fetchAccountDetails();
        this.fetchRewardLogs();
      },
      error: (err) => {
        console.error('Redeem failed', err);
        this.errorMessage.set(err?.error?.error || 'Redeem failed.');
      }
    });
  }
}
