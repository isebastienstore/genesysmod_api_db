import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactRenewablePotential } from '../fact-renewable-potential.model';
import { FactRenewablePotentialService } from '../service/fact-renewable-potential.service';

const factRenewablePotentialResolve = (route: ActivatedRouteSnapshot): Observable<null | IFactRenewablePotential> => {
  const id = route.params.id;
  if (id) {
    return inject(FactRenewablePotentialService)
      .find(id)
      .pipe(
        mergeMap((factRenewablePotential: HttpResponse<IFactRenewablePotential>) => {
          if (factRenewablePotential.body) {
            return of(factRenewablePotential.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default factRenewablePotentialResolve;
