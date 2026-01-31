import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { ApplicationApiService } from '../../../core/api/application-api.service';
import { SectorApiService } from '../../../core/api/sector-api.service';
import { SectorTreeSelectComponent } from '../../../shared/components/sector-tree-select/sector-tree-select.component';
import { SectorResponseDTO } from '../../../shared/models/api.models';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-application-edit',
  standalone: true,
  imports: [ReactiveFormsModule, SectorTreeSelectComponent],
  templateUrl: './application-edit.component.html',
  styleUrl: './application-edit.component.scss'
})
export class ApplicationEditComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly applicationService = inject(ApplicationApiService);
  private readonly sectorService = inject(SectorApiService);
  private readonly notificationService = inject(NotificationService);

  protected readonly sectors = signal<SectorResponseDTO[]>([]);
  protected readonly pageLoading = signal(true);
  protected readonly saving = signal(false);
  protected readonly form = this.fb.nonNullable.group({
    applicantName: ['', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(100),
      Validators.pattern(/^[\p{L} '-]+$/u),
    ]],
    sectorIds: [[] as number[], [Validators.required, Validators.minLength(1)]]
  });

  private applicationId = '';

  ngOnInit(): void {
    this.applicationId = this.route.snapshot.params['id'];

    forkJoin({
      application: this.applicationService.getById(this.applicationId),
      sectors: this.sectorService.getTree()
    }).subscribe({
      next: ({ application, sectors }) => {
        this.sectors.set(sectors);
        this.form.patchValue({
          applicantName: application.applicantName,
          sectorIds: application.sectorIds
        });
        this.form.markAsPristine();
        this.pageLoading.set(false);
      },
      error: () => this.pageLoading.set(false)
    });
  }

  protected onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);

    this.applicationService.update(this.applicationId, this.form.getRawValue()).subscribe({
      next: () => {
        this.notificationService.showSuccess('Application updated successfully!');
        this.router.navigate(['/applications']).then();
      },
      error: () => this.saving.set(false)
    });
  }

  protected goBack(): void {
    if (this.form.dirty && !confirm('Are you sure you want to exit?')) {
      return;
    }

    this.router.navigate(['/applications']).then();
  }
}
