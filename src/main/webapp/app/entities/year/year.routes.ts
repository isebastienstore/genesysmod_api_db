import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import YearResolve from './route/year-routing-resolve.service';

const yearRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/year.component').then(m => m.YearComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/year-detail.component').then(m => m.YearDetailComponent),
    resolve: {
      year: YearResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/year-update.component').then(m => m.YearUpdateComponent),
    resolve: {
      year: YearResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/year-update.component').then(m => m.YearUpdateComponent),
    resolve: {
      year: YearResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default yearRoute;
