import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  protected loading = signal(false);

  protected form = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(20)]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  protected onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);

    this.authService.login(this.form.getRawValue()).subscribe({
      next: () => this.router.navigate(['/applications']),
      error: () => this.loading.set(false)
    });
  }
}
