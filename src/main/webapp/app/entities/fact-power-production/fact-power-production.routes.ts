import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FactPowerProductionResolve from './route/fact-power-production-routing-resolve.service';

const factPowerProductionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fact-power-production.component').then(m => m.FactPowerProductionComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fact-power-production-detail.component').then(m => m.FactPowerProductionDetailComponent),
    resolve: {
      factPowerProduction: FactPowerProductionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fact-power-production-update.component').then(m => m.FactPowerProductionUpdateComponent),
    resolve: {
      factPowerProduction: FactPowerProductionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fact-power-production-update.component').then(m => m.FactPowerProductionUpdateComponent),
    resolve: {
      factPowerProduction: FactPowerProductionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default factPowerProductionRoute;
