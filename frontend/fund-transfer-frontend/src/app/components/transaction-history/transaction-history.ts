import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { GoBackDirective } from '../../directives/go-back-directive';
interface Transaction {
  id: number;
  type: 'sent' | 'received';
  name: string;
  date: string;
  amount: string;
  status: 'Success' | 'Failed' | 'Pending';
}

@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [CommonModule, RouterLink, GoBackDirective],
  templateUrl: './transaction-history.html',
  styleUrl: './transaction-history.css',
})
export class TransactionHistory {
  filter: 'All' | 'Sent' | 'Received' = 'All';

  transactions: Transaction[] = [
    {
      id: 1,
      type: 'sent',
      name: 'Transfer to Jane Doe',
      date: 'Jan 08, 2026 • 10:30 AM',
      amount: '- ₹500.00',
      status: 'Success'
    },
    {
      id: 2,
      type: 'received',
      name: 'Received from Bob Wilson',
      date: 'Jan 07, 2026 • 03:15 PM',
      amount: '+ ₹1,200.00',
      status: 'Success'
    }, {
      id: 1,
      type: 'sent',
      name: 'Transfer to Jane Doe',
      date: 'Jan 08, 2026 • 10:30 AM',
      amount: '- ₹500.00',
      status: 'Success'
    },
    {
      id: 2,
      type: 'received',
      name: 'Received from Bob Wilson',
      date: 'Jan 07, 2026 • 03:15 PM',
      amount: '+ ₹1,200.00',
      status: 'Success'
    }, {
      id: 1,
      type: 'sent',
      name: 'Transfer to Jane Doe',
      date: 'Jan 08, 2026 • 10:30 AM',
      amount: '- ₹500.00',
      status: 'Success'
    },
    {
      id: 2,
      type: 'received',
      name: 'Received from Bob Wilson',
      date: 'Jan 07, 2026 • 03:15 PM',
      amount: '+ ₹1,200.00',
      status: 'Success'
    }, {
      id: 1,
      type: 'sent',
      name: 'Transfer to Jane Doe',
      date: 'Jan 08, 2026 • 10:30 AM',
      amount: '- ₹500.00',
      status: 'Success'
    },
    {
      id: 2,
      type: 'received',
      name: 'Received from Bob Wilson',
      date: 'Jan 07, 2026 • 03:15 PM',
      amount: '+ ₹1,200.00',
      status: 'Success'
    },
    {
      id: 3,
      type: 'sent',
      name: 'Transfer to Alice Brown',
      date: 'Jan 06, 2026 • 09:45 AM',
      amount: '- ₹750.00',
      status: 'Success'
    },
    {
      id: 4,
      type: 'received',
      name: 'Received from Mike Chen',
      date: 'Jan 05, 2026 • 11:20 AM',
      amount: '+ ₹2,500.00',
      status: 'Success'
    }
  ];

  get filteredTransactions() {
    if (this.filter === 'All') return this.transactions;
    return this.transactions.filter(t =>
      (this.filter === 'Sent' && t.type === 'sent') ||
      (this.filter === 'Received' && t.type === 'received')
    );
  }

  setFilter(filter: 'All' | 'Sent' | 'Received') {
    this.filter = filter;
  }

}
