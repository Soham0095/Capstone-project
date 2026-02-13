import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { AccountService } from '../services/account.service';
import { AccountStore } from '../services/account-store.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loading = signal(false);
  form!: FormGroup;

  accountStore = inject(AccountStore)
  auth = inject(AuthService)
  messageService = inject(MessageService)

  constructor(
    private fb: FormBuilder,
    private router: Router
  ) {
    this.form = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  get username() {
    return this.form.get('username');
  }

  get password() {
    return this.form.get('password');
  }

  navigateToSignup(): void {
    this.router.navigate(['/signup']);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { username, password } = this.form.value;
    this.loading.set(true);
    // Quick client-side admin shortcut: hardcoded admin credentials
    if (username === 'admin' && password === 'admin*123') {
      // set a mock token for admin so other parts of app consider user authenticated
      this.auth.login(username as string, password as string).subscribe(() => {
        this.loading.set(false);
        this.router.navigate(['/admin-ai']);
      });
      return;
    }

    this.auth.login(username as string, password as string).subscribe({
      next: (res) => {
        // // After login, fetch account details and store globally
        this.accountService.getMyAccount().subscribe((acc) => {
          this.accountStore.setAccount(acc);
          this.loading.set(false);
          this.router.navigate(['/dashboard']);
        }, () => {
          this.loading.set(false);
          this.router.navigate(['/dashboard']);
        });
        this.loading.set(false);
        if (res.ok) {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Successfully logged in' });
          this.accountStore.setAccount(res.body);
          this.router.navigate(['/dashboard']);
        }
        else {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to login' });
        }
        this.form.reset();
      },
      error: (err) => {
        this.loading.set(false);
        console.log(err);
        console.log(this.loading());
        this.messageService.add({ severity: 'error', summary: 'Error', detail: `${err.error.message}` });
        this.form.reset();
      }
    });
  }
}
