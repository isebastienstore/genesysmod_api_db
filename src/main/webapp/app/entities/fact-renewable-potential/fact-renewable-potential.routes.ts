import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FactRenewablePotentialResolve from './route/fact-renewable-potential-routing-resolve.service';

const factRenewablePotentialRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fact-renewable-potential.component').then(m => m.FactRenewablePotentialComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fact-renewable-potential-detail.component').then(m => m.FactRenewablePotentialDetailComponent),
    resolve: {
      factRenewablePotential: FactRenewablePotentialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fact-renewable-potential-update.component').then(m => m.FactRenewablePotentialUpdateComponent),
    resolve: {
      factRenewablePotential: FactRenewablePotentialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fact-renewable-potential-update.component').then(m => m.FactRenewablePotentialUpdateComponent),
    resolve: {
      factRenewablePotential: FactRenewablePotentialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default factRenewablePotentialRoute;
