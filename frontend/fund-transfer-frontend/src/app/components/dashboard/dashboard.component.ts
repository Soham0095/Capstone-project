import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AccountStore } from '../services/account-store.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

  accountStore = inject(AccountStore);

  constructor(private router: Router) { }


  navigateTo(path: string) {
    this.router.navigateByUrl(path);
  }

  logout(): void {
    console.log("logging out function called");
    // this.auth.logout();
  }
}
