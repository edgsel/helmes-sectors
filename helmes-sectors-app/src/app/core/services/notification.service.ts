import { Injectable, signal } from '@angular/core';
import { Notification } from '../../shared/models/api.models';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private AUTO_CLEAR_TIMEOUT_MS = 5000;
  private notificationSignal = signal<Notification | null>(null);

  notification = this.notificationSignal.asReadonly();

  showSuccess(message: string): void {
    this.notificationSignal.set({ message, type: 'success'});
    this.autoClear();
  }

  showError(message: string): void {
    this.notificationSignal.set({ message, type: 'error' });
    this.autoClear();
  }

  clear(): void {
    this.notificationSignal.set(null);
  }

  private autoClear(): void {
    setTimeout(() => this.clear, this.AUTO_CLEAR_TIMEOUT_MS);
  }
}
