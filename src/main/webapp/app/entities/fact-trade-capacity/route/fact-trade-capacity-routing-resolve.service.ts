import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactTradeCapacity } from '../fact-trade-capacity.model';
import { FactTradeCapacityService } from '../service/fact-trade-capacity.service';

const factTradeCapacityResolve = (route: ActivatedRouteSnapshot): Observable<null | IFactTradeCapacity> => {
  const id = route.params.id;
  if (id) {
    return inject(FactTradeCapacityService)
      .find(id)
      .pipe(
        mergeMap((factTradeCapacity: HttpResponse<IFactTradeCapacity>) => {
          if (factTradeCapacity.body) {
            return of(factTradeCapacity.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default factTradeCapacityResolve;
