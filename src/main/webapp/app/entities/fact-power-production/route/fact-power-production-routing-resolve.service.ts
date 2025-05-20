import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactPowerProduction } from '../fact-power-production.model';
import { FactPowerProductionService } from '../service/fact-power-production.service';

const factPowerProductionResolve = (route: ActivatedRouteSnapshot): Observable<null | IFactPowerProduction> => {
  const id = route.params.id;
  if (id) {
    return inject(FactPowerProductionService)
      .find(id)
      .pipe(
        mergeMap((factPowerProduction: HttpResponse<IFactPowerProduction>) => {
          if (factPowerProduction.body) {
            return of(factPowerProduction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default factPowerProductionResolve;
