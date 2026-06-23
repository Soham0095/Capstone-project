import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RewardSummary } from '../../models/reward-model';

@Injectable({ providedIn: 'root' })
export class RewardService {
  private baseUrl = 'http://localhost:8090';

  constructor(private http: HttpClient) {}

  getRewards(accountId: number): Observable<RewardSummary> {
    return this.http.get<RewardSummary>(`${this.baseUrl}/rewards/${accountId}`);
  }
}
