import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FactElectricityConsumptionResolve from './route/fact-electricity-consumption-routing-resolve.service';

const factElectricityConsumptionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fact-electricity-consumption.component').then(m => m.FactElectricityConsumptionComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/fact-electricity-consumption-detail.component').then(m => m.FactElectricityConsumptionDetailComponent),
    resolve: {
      factElectricityConsumption: FactElectricityConsumptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/fact-electricity-consumption-update.component').then(m => m.FactElectricityConsumptionUpdateComponent),
    resolve: {
      factElectricityConsumption: FactElectricityConsumptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/fact-electricity-consumption-update.component').then(m => m.FactElectricityConsumptionUpdateComponent),
    resolve: {
      factElectricityConsumption: FactElectricityConsumptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default factElectricityConsumptionRoute;
