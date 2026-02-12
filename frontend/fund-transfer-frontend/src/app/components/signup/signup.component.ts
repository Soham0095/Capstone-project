import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from '../services/account.service';
import { SignupRequest, ApiResponse } from '../../models/create-account-request';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  form!: FormGroup;
  loading = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private accountService: AccountService,
    private router: Router
  ) {
    this.form = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      holderName: ['', [Validators.required]]
    });
  }

  get username() {
    return this.form.get('username');
  }

  get password() {
    return this.form.get('password');
  }

  get holderName() {
    return this.form.get('holderName');
  }

  submit(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const formData = this.form.value as SignupRequest;
    this.loading = true;

    this.accountService.signup(formData).subscribe({
      next: (response: ApiResponse) => {
        this.loading = false;
        // Response is ApiResponse object with message property
        this.successMessage = response?.message || 'Account created successfully!';
        // Give more time to display the modal before redirecting
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 3000);
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error?.error?.message || error?.error || 'Failed to create account. Please try again.';
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/']);
  }
}
