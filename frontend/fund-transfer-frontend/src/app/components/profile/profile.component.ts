import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountStore, Account } from '../services/account-store.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  account: Account | null = null;

  constructor(private accountStore: AccountStore) {}

  ngOnInit(): void {
    this.account = this.accountStore.getAccount();
  }
}
