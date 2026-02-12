import { Injectable, signal } from '@angular/core';
import { Account } from '../../models/account-model';



@Injectable({ providedIn: 'root' })
export class AccountStore {
  account = signal<Account | null>(null);

  setAccount(account: Account) {
    this.account.set(account);
  }

  getAccount(): Account | null {
    return this.account();
  }

  clear() {
    this.account.set(null);
  }
}
