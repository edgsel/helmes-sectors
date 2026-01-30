import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { UserAuthDTO, AuthResponseDTO } from '../../shared/models/api.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly API_URL = '/api/v1/users';
  private readonly TOKEN_KEY = 'jwt_token';

  private tokenSignal = signal<string | null>(this.getStoredToken());
  private isAuthenticated = computed(() => !!this.tokenSignal());

  constructor(private http: HttpClient, private router: Router) {}

  register(data: UserAuthDTO): Observable<AuthResponseDTO> {
    return this.http.post<AuthResponseDTO>(`${this.API_URL}/register`, data)
      .pipe(tap(res => this.setToken(res.jwt)));
  }

  login(data: UserAuthDTO): Observable<AuthResponseDTO> {
    return this.http.post<AuthResponseDTO>(`${this.API_URL}/login`, data)
      .pipe(tap(res => this.setToken(res.jwt)));
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);

    this.tokenSignal.set(null);
    this.router.navigate(['/login']).then(_ => console.log("User logged out"));
  }

  getToken(): string | null {
    return this.tokenSignal();
  }

  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    this.tokenSignal.set(token);
  }

  private getStoredToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }
}
