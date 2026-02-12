import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transaction } from '../../models/transaction-model';



@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  private baseUrl = 'http://localhost:8090';
  private transactionsUrl: string;

  constructor(private http: HttpClient) {
    this.transactionsUrl = `${this.baseUrl}/transactionLogs/`;
  }

  getTransactions(id: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.transactionsUrl}${id}`);
  }
}
