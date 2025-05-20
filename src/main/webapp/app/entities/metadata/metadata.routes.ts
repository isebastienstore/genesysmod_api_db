import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import MetadataResolve from './route/metadata-routing-resolve.service';

const metadataRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/metadata.component').then(m => m.MetadataComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/metadata-detail.component').then(m => m.MetadataDetailComponent),
    resolve: {
      metadata: MetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/metadata-update.component').then(m => m.MetadataUpdateComponent),
    resolve: {
      metadata: MetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/metadata-update.component').then(m => m.MetadataUpdateComponent),
    resolve: {
      metadata: MetadataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default metadataRoute;
