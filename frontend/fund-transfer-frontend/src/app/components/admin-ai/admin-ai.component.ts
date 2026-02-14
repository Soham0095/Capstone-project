import { Component, ChangeDetectorRef, NgZone, ApplicationRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminAiService } from '../services/admin-ai.service';
import { AccountStore } from '../services/account-store.service';

@Component({
  selector: 'app-admin-ai',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-ai.component.html',
  styleUrls: ['./admin-ai.component.css']
})
export class AdminAiComponent {
  form: FormGroup;
  loading = false;
  result: any = null;
  columns: string[] = [];
  Array = Array;
  errorMessage: string | null = null;
  queryRan = false;
  showResult = false;

  private router = inject(Router);
  private accountStore = inject(AccountStore);

  constructor(
    private fb: FormBuilder,
    private ai: AdminAiService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    private appRef: ApplicationRef
  ) {
    this.form = this.fb.group({
      query: ['', [Validators.required]]
    });
  }

  logout(): void {
    this.accountStore.clear();
    this.router.navigate(['/']);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.result = null;
    this.columns = [];
    this.errorMessage = null;
    this.queryRan = false;
    this.showResult = false;

    const q = this.form.value.query;
    console.log('Sending query:', q);

    this.ai.sendQuery(q).subscribe({
      next: (res: any) => {
        // ensure changes run inside Angular zone
        this.ngZone.run(() => {
          console.log('Response received:', res);
          this.result = res;

          if (Array.isArray(res) && res.length > 0 && typeof res[0] === 'object') {
            this.columns = Object.keys(res[0]);
          } else if (res && typeof res === 'object' && !Array.isArray(res)) {
            this.columns = Object.keys(res);
          }

          this.loading = false;
          this.queryRan = true;
          this.showResult = true;

          // trigger change detection immediately and as a fallback schedule
          this.cdr.detectChanges();
          setTimeout(() => {
            try { this.cdr.detectChanges(); } catch (e) { }
            try { this.appRef.tick(); } catch (e) { }
          }, 0);
        });
      },
      error: (err: any) => {
        this.ngZone.run(() => {
          console.error('Error details:', err);
          this.loading = false;
          this.queryRan = false;

          let errorMsg = 'Failed to fetch response from AI service';
          if (err?.name === 'TimeoutError') {
            errorMsg = 'Request timed out. The backend might be slow or not responding.';
          } else if (err?.status === 0) {
            errorMsg = 'CORS error or backend server not running at localhost:8090. Check console for details.';
          } else if (err?.status === 404) {
            errorMsg = 'Backend endpoint not found. Ensure /chat endpoint exists.';
          } else if (err?.status === 400) {
            errorMsg = 'Bad request. ' + (err.error?.message || 'Check the query format.');
          } else if (err?.status >= 500) {
            errorMsg = 'Backend server error: ' + (err.error?.message || err.message || 'Unknown server error');
          } else if (err?.message) {
            errorMsg = err.message;
          }

          this.errorMessage = errorMsg;
          this.result = { error: errorMsg };

          this.cdr.detectChanges();
          setTimeout(() => {
            try { this.cdr.detectChanges(); } catch (e) { }
            try { this.appRef.tick(); } catch (e) { }
          }, 0);
        });
      }
    });
  }

  

}
