import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SectorResponseDTO } from '../../shared/models/api.models';

@Injectable({ providedIn: 'root' })
export class SectorApiService {
  private readonly API_URL = '/api/v1/sectors/tree';

  constructor(private http: HttpClient) { }

  getTree(): Observable<SectorResponseDTO[]> {
    return this.http.get<SectorResponseDTO[]>(this.API_URL);
  }
}
