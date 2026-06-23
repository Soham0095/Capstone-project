import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from '../services/account.service';
import { SignupRequest, ApiResponse } from '../../models/create-account-request';
import { MessageService } from 'primeng/api';

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

  messageService = inject(MessageService);

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
        const msg = response?.message || 'Account created successfully!';
        // Give more time to display the modal before redirecting
        this.messageService.add({ severity: 'success', summary: 'Success', detail: msg });
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.loading = false;
        let errorMsg: string;
        if (typeof error?.error === 'string') {
          errorMsg = error.error;
        } else if (error?.error?.message) {
          errorMsg = error.error.message;
        } else if (error?.message) {
          errorMsg = error.message;
        } else {
          errorMsg = 'Failed to create account. Please try again.';
        }
        this.messageService.add({ severity: 'error', summary: 'Error', detail: errorMsg });
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/']);
  }
}
