import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFuel } from '../fuel.model';
import { FuelService } from '../service/fuel.service';

const fuelResolve = (route: ActivatedRouteSnapshot): Observable<null | IFuel> => {
  const id = route.params.id;
  if (id) {
    return inject(FuelService)
      .find(id)
      .pipe(
        mergeMap((fuel: HttpResponse<IFuel>) => {
          if (fuel.body) {
            return of(fuel.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default fuelResolve;
