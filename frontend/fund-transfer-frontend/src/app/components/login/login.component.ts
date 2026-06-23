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
  accountService = inject(AccountService);

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
    let urlToNavigate: string;
    if (username === 'admin' && password === 'admin*123') { urlToNavigate = '/admin-ai'; }
    else { urlToNavigate = '/dashboard'; }

    this.auth.login(username as string, password as string).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.ok) {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Successfully logged in' });
          this.accountStore.setAccount(res.body);
          this.router.navigate([urlToNavigate]);
        }
        else {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to login' });
        }
        this.form.reset();
      },
      error: (err) => {
        this.loading.set(false);
        console.log(err);
        const detail = err?.error?.message || err?.message || 'Login failed. Please try again.';
        this.messageService.add({ severity: 'error', summary: 'Error', detail });
        this.form.reset();
      }
    });
  }
}
