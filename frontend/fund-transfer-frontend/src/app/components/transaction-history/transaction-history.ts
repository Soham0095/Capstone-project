import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { GoBackDirective } from '../../directives/go-back-directive';
import { TransactionService } from '../services/transaction.service';
import { AccountStore } from '../services/account-store.service';
import { Transaction } from '../../models/transaction-model';
import { Footer } from '../footer/footer';



@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [CommonModule, RouterLink, GoBackDirective, Footer],
  templateUrl: './transaction-history.html',
  styleUrl: './transaction-history.css',
})
export class TransactionHistory {
  filter = signal<'All' | 'Sent' | 'Received'>('All');
  transactions = signal<Transaction[]>([]);

  //mock data for UI
  // transactions: Transaction[] = [
  //   {
  //     id: 1,
  //     type: 'sent',
  //     name: 'Transfer to Jane Doe',
  //     date: 'Jan 08, 2026 • 10:30 AM',
  //     amount: '- ₹500.00',
  //     status: 'Success'
  //   },
  //   {
  //     id: 2,
  //     type: 'received',
  //     name: 'Received from Bob Wilson',
  //     date: 'Jan 07, 2026 • 03:15 PM',
  //     amount: '+ ₹1,200.00',
  //     status: 'Success'
  //   }, {
  //     id: 1,
  //     type: 'sent',
  //     name: 'Transfer to Jane Doe',
  //     date: 'Jan 08, 2026 • 10:30 AM',
  //     amount: '- ₹500.00',
  //     status: 'Success'
  //   },
  //   {
  //     id: 2,
  //     type: 'received',
  //     name: 'Received from Bob Wilson',
  //     date: 'Jan 07, 2026 • 03:15 PM',
  //     amount: '+ ₹1,200.00',
  //     status: 'Success'
  //   }, {
  //     id: 1,
  //     type: 'sent',
  //     name: 'Transfer to Jane Doe',
  //     date: 'Jan 08, 2026 • 10:30 AM',
  //     amount: '- ₹500.00',
  //     status: 'Success'
  //   },
  //   {
  //     id: 2,
  //     type: 'received',
  //     name: 'Received from Bob Wilson',
  //     date: 'Jan 07, 2026 • 03:15 PM',
  //     amount: '+ ₹1,200.00',
  //     status: 'Success'
  //   }, {
  //     id: 1,
  //     type: 'sent',
  //     name: 'Transfer to Jane Doe',
  //     date: 'Jan 08, 2026 • 10:30 AM',
  //     amount: '- ₹500.00',
  //     status: 'Success'
  //   },
  //   {
  //     id: 2,
  //     type: 'received',
  //     name: 'Received from Bob Wilson',
  //     date: 'Jan 07, 2026 • 03:15 PM',
  //     amount: '+ ₹1,200.00',
  //     status: 'Success'
  //   },
  //   {
  //     id: 3,
  //     type: 'sent',
  //     name: 'Transfer to Alice Brown',
  //     date: 'Jan 06, 2026 • 09:45 AM',
  //     amount: '- ₹750.00',
  //     status: 'Success'
  //   },
  //   {
  //     id: 4,
  //     type: 'received',
  //     name: 'Received from Mike Chen',
  //     date: 'Jan 05, 2026 • 11:20 AM',
  //     amount: '+ ₹2,500.00',
  //     status: 'Success'
  //   }
  // ];

  transactionService = inject(TransactionService);
  accountStore = inject(AccountStore);

  ngOnInit() {
    this.transactionService.getTransactions(this.accountStore.getAccount()!.id).subscribe((transactions: Transaction[]) => {
      this.transactions.set(transactions.reverse());
    });
  }


  get filteredTransactions() {
    const currentFilter = this.filter();
    const transactions = this.transactions();

    if (currentFilter === 'All') return transactions;

    return transactions.filter(t => {
      return (currentFilter === 'Sent' && t.fromAccountId === this.accountStore.getAccount()!.id) ||
        (currentFilter === 'Received' && t.toAccountId === this.accountStore.getAccount()!.id);
    });
  }

  setFilter(filter: 'All' | 'Sent' | 'Received') {
    this.filter.set(filter);
    
  }

}
