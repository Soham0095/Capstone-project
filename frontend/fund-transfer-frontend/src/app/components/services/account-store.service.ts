import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface Account {
  id?: string;
  name: string;
  accountNumber?: string;
  availableBalance?: number;
}

@Injectable({ providedIn: 'root' })
export class AccountStore {
  private accountSubject = new BehaviorSubject<Account | null>(null);
  account$: Observable<Account | null> = this.accountSubject.asObservable();

  setAccount(account: Account) {
    this.accountSubject.next(account);
  }

  getAccount(): Account | null {
    return this.accountSubject.getValue();
  }

  clear() {
    this.accountSubject.next(null);
  }
}
