import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApplicationService } from '../../../core/services/application.service';
import { SectorService } from '../../../core/services/sector.service';
import { SectorTreeSelectComponent } from '../../../shared/components/sector-tree-select/sector-tree-select.component';
import { SectorResponseDTO } from '../../../shared/models/api.models';

@Component({
  selector: 'app-application-create',
  standalone: true,
  imports: [ReactiveFormsModule, SectorTreeSelectComponent],
  templateUrl: './application-create.component.html',
  styleUrl: './application-create.component.scss'
})
export class ApplicationCreateComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private applicationService = inject(ApplicationService);
  private sectorService = inject(SectorService);

  protected sectors = signal<SectorResponseDTO[]>([]);
  protected loading = signal(false);

  protected form = this.fb.nonNullable.group({
    applicantName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
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
      next: () => this.router.navigate(['/applications']),
      error: () => this.loading.set(false)
    });
  }

  protected goBack(): void {
    if (this.form.dirty && !confirm('Are you sure you want to exit?')) {
      return;
    }
    this.router.navigate(['/applications']);
  }
}
