import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root',
})
export class TransferService {

  private baseUrl = 'http://localhost:8090';
  private transferUrl = `${this.baseUrl}/transfer`;

  constructor(private http: HttpClient) { }

  transferMoney(data: any) {
    return this.http.post(this.transferUrl, data);
  }
}
