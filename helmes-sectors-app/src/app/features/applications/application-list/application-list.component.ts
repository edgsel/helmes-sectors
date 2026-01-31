import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { ApplicationApiService } from '../../../core/api/application-api.service';
import { AuthService } from '../../../core/services/auth.service';
import { ApplicationSummaryResponseDTO } from '../../../shared/models/api.models';

@Component({
  selector: 'app-application-list',
  standalone: true,
  templateUrl: './application-list.component.html',
  imports: [RouterLink, DatePipe],
  styleUrl: './application-list.component.scss'
})
export class ApplicationListComponent implements OnInit {
  private applicationService = inject(ApplicationApiService);
  private authService = inject(AuthService);

  protected applications = signal<ApplicationSummaryResponseDTO[]>([]);
  protected loading = signal(true);

  ngOnInit(): void {
    this.loadApplications();
  }

  protected logout(): void {
    this.authService.logout();
  }

  private loadApplications(): void {
    this.applicationService.getAll().subscribe({
      next: (data) => {
        this.applications.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }
}
