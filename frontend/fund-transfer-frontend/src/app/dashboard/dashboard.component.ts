import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="padding:24px">
      <h2>Dashboard</h2>
      <p>Welcome — you are logged in.</p>
      <button (click)="logout()">Logout</button>
    </div>
  `
})
export class DashboardComponent {
  constructor(private auth: AuthService) {}

  logout(): void {
    this.auth.logout();
  }
}
