import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactTransport } from '../fact-transport.model';
import { FactTransportService } from '../service/fact-transport.service';

const factTransportResolve = (route: ActivatedRouteSnapshot): Observable<null | IFactTransport> => {
  const id = route.params.id;
  if (id) {
    return inject(FactTransportService)
      .find(id)
      .pipe(
        mergeMap((factTransport: HttpResponse<IFactTransport>) => {
          if (factTransport.body) {
            return of(factTransport.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default factTransportResolve;
