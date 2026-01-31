import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApplicationApiService } from '../../../core/api/application-api.service';
import { SectorApiService } from '../../../core/api/sector-api.service';
import { SectorTreeSelectComponent } from '../../../shared/components/sector-tree-select/sector-tree-select.component';
import { SectorResponseDTO } from '../../../shared/models/api.models';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-application-create',
  standalone: true,
  imports: [ReactiveFormsModule, SectorTreeSelectComponent],
  templateUrl: './application-create.component.html',
  styleUrl: './application-create.component.scss'
})
export class ApplicationCreateComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly applicationService = inject(ApplicationApiService);
  private readonly sectorService = inject(SectorApiService);
  private readonly notificationService = inject(NotificationService);

  protected readonly sectors = signal<SectorResponseDTO[]>([]);
  protected readonly loading = signal(false);
  protected readonly form = this.fb.nonNullable.group({
    applicantName: ['', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(100),
      Validators.pattern(/^[\p{L} '-]+$/u),
    ]],
    sectorIds: [[] as number[], [Validators.required, Validators.minLength(1)]],
    agreedToTerms: [false, Validators.requiredTrue]
  });

  ngOnInit(): void {
    this.sectorService.getTree().subscribe({
      next: (data) => this.sectors.set(data)
    });
  }

  protected onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);

    this.applicationService.create(this.form.getRawValue()).subscribe({
      next: () => {
        this.notificationService.showSuccess('Application created successfully!');
        this.router.navigate(['/applications']).then();
      },
      error: () => this.loading.set(false)
    });
  }

  protected goBack(): void {
    if (this.form.dirty && !confirm('Are you sure you want to exit?')) {
      return;
    }
    this.router.navigate(['/applications']).then();
  }
}
