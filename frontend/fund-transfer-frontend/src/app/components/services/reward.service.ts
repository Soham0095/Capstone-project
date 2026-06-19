import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RewardLog } from '../../models/reward-log-model';

@Injectable({
  providedIn: 'root',
})
export class RewardService {
  private baseUrl = 'http://localhost:8090';

  constructor(private http: HttpClient) {}

  getRewardLogs(accountId: number): Observable<RewardLog[]> {
    return this.http.get<RewardLog[]>(`${this.baseUrl}/accounts/${accountId}/rewards`);
  }

  getRewardPoints(accountId: number): Observable<{ accountId: number; rewardPoints: number }> {
    return this.http.get<{ accountId: number; rewardPoints: number }>(`${this.baseUrl}/accounts/${accountId}/reward-points`);
  }

  redeemPoints(accountId: number, points?: number): Observable<any> {
    const body = points !== undefined ? { points } : {};
    return this.http.post<any>(`${this.baseUrl}/accounts/${accountId}/redeem`, body);
  }
}
