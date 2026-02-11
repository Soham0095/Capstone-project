import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminAiService } from '../services/admin-ai.service';

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
  Array = Array; // Expose Array to template for Array.isArray()

  constructor(private fb: FormBuilder, private ai: AdminAiService) {
    this.form = this.fb.group({
      query: ['', [Validators.required]]
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.result = null;
    this.columns = [];
    const q = this.form.value.query;
    this.ai.sendQuery(q).subscribe({
      next: (res) => {
        this.result = res;
        if (Array.isArray(res) && res.length > 0 && typeof res[0] === 'object') {
          this.columns = Object.keys(res[0]);
        } else if (res && typeof res === 'object') {
          this.columns = Object.keys(res);
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.result = { error: 'Failed to fetch response from AI service' };
      }
    });
  }
}
