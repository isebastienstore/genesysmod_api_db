import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import FactTransportResolve from './route/fact-transport-routing-resolve.service';

const factTransportRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fact-transport.component').then(m => m.FactTransportComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fact-transport-detail.component').then(m => m.FactTransportDetailComponent),
    resolve: {
      factTransport: FactTransportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fact-transport-update.component').then(m => m.FactTransportUpdateComponent),
    resolve: {
      factTransport: FactTransportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fact-transport-update.component').then(m => m.FactTransportUpdateComponent),
    resolve: {
      factTransport: FactTransportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default factTransportRoute;
