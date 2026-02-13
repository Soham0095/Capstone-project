import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { GoBackDirective } from '../../directives/go-back-directive';
import { AccountStore } from '../services/account-store.service';
import { TransferService } from '../services/transfer.service';
import { MessageService } from 'primeng/api';
import { Footer } from '../footer/footer';


@Component({
  selector: 'app-transfer',
  imports: [CommonModule, ReactiveFormsModule, GoBackDirective, Footer],
  templateUrl: './transfer.html',
  styleUrl: './transfer.css',
})
export class Transfer {

  fb = inject(FormBuilder);
  router = inject(Router);
  transferService = inject(TransferService);
  accountStore = inject(AccountStore);
  messageService = inject(MessageService);

  form = this.fb.group({
    toAccount: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
    amount: ['', [Validators.required, Validators.pattern('^[0-9]+(\\.[0-9]{1,2})?$'), Validators.min(1)]],
  });

  isLoading = signal(false);
  submitted = signal(false);

  get f() { return this.form.controls; }

  onClickCancel() {
    this.form.reset();
    this.form.markAsPristine();
    this.form.markAsUntouched();
    this.submitted.set(false);
  }

  onClickTransfer() {
    this.submitted.set(true);

    if (this.form.invalid) {
      return;
    }

    this.isLoading.set(true);
    console.log('Transfer Data:', this.form.value);
    this.transferService
      .transferMoney({
        fromAccountId: this.accountStore.getAccount()?.id,
        toAccountId: parseInt(this.form.value.toAccount!),
        amount: parseInt(this.form.value.amount!),
      })
      .subscribe({
        next: (response) => {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Transfer successful!' });
          this.isLoading.set(false);
          this.form.reset();
          this.form.markAsPristine();
          this.form.markAsUntouched();
          this.submitted.set(false);
          //fetch the account details
          this.accountStore.fetchAccountDetails();
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.isLoading.set(false);
          this.form.reset();
          this.form.markAsPristine();
          this.form.markAsUntouched();
          this.submitted.set(false);
          this.messageService.add({ severity: 'error', summary: 'Error', detail: `Error: ${error.error.message}` });
        }
      });
  }

}
