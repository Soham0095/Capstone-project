import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, timeout, catchError, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AdminAiService {
  private apiUrl = 'http://localhost:8090/chat';

  constructor(private http: HttpClient) {}

  sendQuery(query: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'text/plain'
    });
    console.log('Making request to:', this.apiUrl);
    console.log('Headers:', headers.getAll('Content-Type'));
    
    return this.http.post<any>(this.apiUrl, query, { headers }).pipe(
      timeout(30000), // 30 second timeout
      catchError(error => {
        console.error('Service error:', error);
        return throwError(() => error);
      })
    );
  }
}
