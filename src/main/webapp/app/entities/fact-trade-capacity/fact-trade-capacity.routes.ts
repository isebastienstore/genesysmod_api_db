import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FactTradeCapacityResolve from './route/fact-trade-capacity-routing-resolve.service';

const factTradeCapacityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fact-trade-capacity.component').then(m => m.FactTradeCapacityComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fact-trade-capacity-detail.component').then(m => m.FactTradeCapacityDetailComponent),
    resolve: {
      factTradeCapacity: FactTradeCapacityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fact-trade-capacity-update.component').then(m => m.FactTradeCapacityUpdateComponent),
    resolve: {
      factTradeCapacity: FactTradeCapacityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fact-trade-capacity-update.component').then(m => m.FactTradeCapacityUpdateComponent),
    resolve: {
      factTradeCapacity: FactTradeCapacityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default factTradeCapacityRoute;
