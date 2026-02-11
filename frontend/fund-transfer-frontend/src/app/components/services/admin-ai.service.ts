import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AdminAiService {
  constructor(private http: HttpClient) {}

  sendQuery(query: string): Observable<any> {
    // TODO: Wire to backend Llama endpoint, e.g. POST /api/admin/ai/query
    // return this.http.post('/api/admin/ai/query', { query });

    // Mock response for now (array of objects example)
    const mock = [
      { accountId: 101, name: 'Alice Johnson', balance: 0 },
      { accountId: 203, name: 'Bob Smith', balance: 0 }
    ];
    return of(mock);
  }
}
