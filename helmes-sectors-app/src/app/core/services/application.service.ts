import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  ApplicationResponseDTO,
  ApplicationSummaryResponseDTO,
  CreateApplicationDTO,
  UpdateApplicationDTO
} from '../../shared/models/api.models';

@Injectable({ providedIn: 'root' })
export class ApplicationService {
  private readonly API_URL = '/api/v1/users/applications';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<ApplicationSummaryResponseDTO[]> {
    return this.http.get<ApplicationSummaryResponseDTO[]>(this.API_URL);
  }

  getById(id: string): Observable<ApplicationResponseDTO> {
    return this.http.get<ApplicationResponseDTO>(`${this.API_URL}/${id}`);
  }

  create(data: CreateApplicationDTO): Observable<ApplicationResponseDTO> {
    return this.http.post<ApplicationResponseDTO>(this.API_URL, data);
  }

  update(id: string, data: UpdateApplicationDTO): Observable<ApplicationResponseDTO> {
    return this.http.put<ApplicationResponseDTO>(`${this.API_URL}/${id}`, data);
  }
}
