import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RewardService } from '../services/reward.service';
import { AccountStore } from '../services/account-store.service';
import { RewardLedger } from '../../models/reward-model';
import { Footer } from '../footer/footer';

@Component({
  selector: 'app-rewards',
  standalone: true,
  imports: [CommonModule, Footer],
  templateUrl: './rewards.component.html',
  styleUrls: ['./rewards.component.css']
})
export class RewardsComponent implements OnInit {

  accountStore = inject(AccountStore);
  rewardService = inject(RewardService);
  router = inject(Router);

  totalPoints = signal<number>(0);
  history = signal<RewardLedger[]>([]);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  ngOnInit(): void {
    const accountId = this.accountStore.getAccount()?.id;
    if (!accountId) {
      this.router.navigate(['/']);
      return;
    }

    this.rewardService.getRewards(accountId).subscribe({
      next: (summary) => {
        this.totalPoints.set(summary.totalPoints);
        this.history.set(summary.history);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load rewards. Please try again later.');
        this.loading.set(false);
      }
    });
  }

  get tier(): { label: string; color: string; icon: string } {
    const pts = this.totalPoints();
    if (pts >= 500) return { label: 'Platinum', color: '#b5cfe8', icon: '💎' };
    if (pts >= 200) return { label: 'Gold',     color: '#f6c90e', icon: '🥇' };
    if (pts >= 50)  return { label: 'Silver',   color: '#c0c0c0', icon: '🥈' };
    return           { label: 'Bronze',  color: '#cd7f32', icon: '🥉' };
  }
}
