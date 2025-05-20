import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FactTradeCostResolve from './route/fact-trade-cost-routing-resolve.service';

const factTradeCostRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fact-trade-cost.component').then(m => m.FactTradeCostComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fact-trade-cost-detail.component').then(m => m.FactTradeCostDetailComponent),
    resolve: {
      factTradeCost: FactTradeCostResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fact-trade-cost-update.component').then(m => m.FactTradeCostUpdateComponent),
    resolve: {
      factTradeCost: FactTradeCostResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fact-trade-cost-update.component').then(m => m.FactTradeCostUpdateComponent),
    resolve: {
      factTradeCost: FactTradeCostResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default factTradeCostRoute;
