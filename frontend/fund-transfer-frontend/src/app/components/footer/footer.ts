import { Component, inject } from '@angular/core';
import { AccountStore } from '../services/account-store.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-footer',
  imports: [],
  templateUrl: './footer.html',
  styleUrl: './footer.css',
})
export class Footer {

  accountStore = inject(AccountStore);

  constructor(private router: Router) { }


  navigateTo(path: string) {
    this.router.navigateByUrl(path);
  }

  logout(): void {
    console.log("logging out function called");
    this.accountStore.setAccount(null);
    this.router.navigate(['/']);
  }
}
