import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AccountStore, Account } from '../services/account-store.service';
import { AccountService } from '../services/account.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  balance: number | null = null;
  private sub = new Subscription();

  constructor(
    private accountStore: AccountStore,
    private accountService: AccountService,
    private auth: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.sub.add(
      this.accountStore.account$.subscribe((acc) => {
        this.account = acc;
        if (acc && acc.id) {
          this.accountService.getBalance(acc.id).subscribe((b) => (this.balance = b || acc.availableBalance || 0));
        } else if (acc && typeof acc.availableBalance === 'number') {
          this.balance = acc.availableBalance;
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  navigateTo(path: string) {
    this.router.navigateByUrl(path);
  }

  logout(): void {
    this.auth.logout();
  }
}
