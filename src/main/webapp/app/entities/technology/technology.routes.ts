import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import TechnologyResolve from './route/technology-routing-resolve.service';

const technologyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/technology.component').then(m => m.TechnologyComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/technology-detail.component').then(m => m.TechnologyDetailComponent),
    resolve: {
      technology: TechnologyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/technology-update.component').then(m => m.TechnologyUpdateComponent),
    resolve: {
      technology: TechnologyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/technology-update.component').then(m => m.TechnologyUpdateComponent),
    resolve: {
      technology: TechnologyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default technologyRoute;
