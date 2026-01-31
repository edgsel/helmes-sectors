import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { ApplicationService } from '../../../core/services/application.service';
import { SectorService } from '../../../core/services/sector.service';
import { SectorTreeSelectComponent } from '../../../shared/components/sector-tree-select/sector-tree-select.component';
import { SectorResponseDTO } from '../../../shared/models/api.models';

@Component({
  selector: 'app-application-edit',
  standalone: true,
  imports: [ReactiveFormsModule, SectorTreeSelectComponent],
  templateUrl: './application-edit.component.html',
  styleUrl: './application-edit.component.scss'
})
export class ApplicationEditComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private applicationService = inject(ApplicationService);
  private sectorService = inject(SectorService);

  protected sectors = signal<SectorResponseDTO[]>([]);
  protected pageLoading = signal(true);
  protected saving = signal(false);

  private applicationId = '';

  protected form = this.fb.nonNullable.group({
    applicantName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    sectorIds: [[] as number[], [Validators.required, Validators.minLength(1)]]
  });

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
      next: () => this.router.navigate(['/applications']),
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
