import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetadata } from '../metadata.model';
import { MetadataService } from '../service/metadata.service';

const metadataResolve = (route: ActivatedRouteSnapshot): Observable<null | IMetadata> => {
  const id = route.params.id;
  if (id) {
    return inject(MetadataService)
      .find(id)
      .pipe(
        mergeMap((metadata: HttpResponse<IMetadata>) => {
          if (metadata.body) {
            return of(metadata.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default metadataResolve;
