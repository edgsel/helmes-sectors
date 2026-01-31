import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/applications', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component')
      .then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register.component')
      .then(m => m.RegisterComponent)
  },
  {
    path: 'applications',
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/applications/application-list/application-list.component')
          .then(m => m.ApplicationListComponent)
      },
      {
        path: 'new',
        loadComponent: () => import('./features/applications/application-create/application-create.component')
          .then(m => m.ApplicationCreateComponent)
      },
      {
        path: ':id/edit',
        loadComponent: () => import('./features/applications/application-edit/application-edit.component')
          .then(m => m.ApplicationEditComponent)
      }
    ]
  },
  { path: '**', redirectTo: '/applications' }
];
