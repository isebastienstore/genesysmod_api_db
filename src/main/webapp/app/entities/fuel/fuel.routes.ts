import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FuelResolve from './route/fuel-routing-resolve.service';

const fuelRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fuel.component').then(m => m.FuelComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fuel-detail.component').then(m => m.FuelDetailComponent),
    resolve: {
      fuel: FuelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fuel-update.component').then(m => m.FuelUpdateComponent),
    resolve: {
      fuel: FuelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fuel-update.component').then(m => m.FuelUpdateComponent),
    resolve: {
      fuel: FuelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fuelRoute;
