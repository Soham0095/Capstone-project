import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { AccountService } from '../services/account.service';
import { AccountStore } from '../services/account-store.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  errorMessage: string | null = null;
  loading = false;
  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private accountService: AccountService,
    private accountStore: AccountStore,
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
    this.errorMessage = null;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { username, password } = this.form.value;
    this.loading = true;

    // Try real backend; if it errors (e.g., 404 during dev), fallback to mockLogin
    this.auth.login(username as string, password as string).subscribe({
      next: () => {
        // After login, fetch account details and store globally
        this.accountService.getMyAccount().subscribe((acc) => {
          this.accountStore.setAccount(acc);
          this.loading = false;
          this.router.navigate(['/dashboard']);
        }, () => {
          this.loading = false;
          this.router.navigate(['/dashboard']);
        });
      },
      error: () => {
        // Fallback to mock login so frontend development can continue
        this.auth.mockLogin(username as string, password as string).subscribe(() => {
          this.accountService.getMyAccount().subscribe((acc) => {
            this.accountStore.setAccount(acc);
            this.loading = false;
            this.router.navigate(['/dashboard']);
          }, () => {
            this.loading = false;
            this.router.navigate(['/dashboard']);
          });
        });
      }
    });
  }
}
