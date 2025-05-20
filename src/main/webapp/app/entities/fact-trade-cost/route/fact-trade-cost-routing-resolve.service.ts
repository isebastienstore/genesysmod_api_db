import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactTradeCost } from '../fact-trade-cost.model';
import { FactTradeCostService } from '../service/fact-trade-cost.service';

const factTradeCostResolve = (route: ActivatedRouteSnapshot): Observable<null | IFactTradeCost> => {
  const id = route.params.id;
  if (id) {
    return inject(FactTradeCostService)
      .find(id)
      .pipe(
        mergeMap((factTradeCost: HttpResponse<IFactTradeCost>) => {
          if (factTradeCost.body) {
            return of(factTradeCost.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default factTradeCostResolve;
