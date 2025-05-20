import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FactPowerPlantsResolve from './route/fact-power-plants-routing-resolve.service';

const factPowerPlantsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fact-power-plants.component').then(m => m.FactPowerPlantsComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fact-power-plants-detail.component').then(m => m.FactPowerPlantsDetailComponent),
    resolve: {
      factPowerPlants: FactPowerPlantsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fact-power-plants-update.component').then(m => m.FactPowerPlantsUpdateComponent),
    resolve: {
      factPowerPlants: FactPowerPlantsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fact-power-plants-update.component').then(m => m.FactPowerPlantsUpdateComponent),
    resolve: {
      factPowerPlants: FactPowerPlantsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default factPowerPlantsRoute;
