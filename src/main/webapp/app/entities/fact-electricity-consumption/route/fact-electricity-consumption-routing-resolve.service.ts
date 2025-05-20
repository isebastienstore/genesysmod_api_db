import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactElectricityConsumption } from '../fact-electricity-consumption.model';
import { FactElectricityConsumptionService } from '../service/fact-electricity-consumption.service';

const factElectricityConsumptionResolve = (route: ActivatedRouteSnapshot): Observable<null | IFactElectricityConsumption> => {
  const id = route.params.id;
  if (id) {
    return inject(FactElectricityConsumptionService)
      .find(id)
      .pipe(
        mergeMap((factElectricityConsumption: HttpResponse<IFactElectricityConsumption>) => {
          if (factElectricityConsumption.body) {
            return of(factElectricityConsumption.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default factElectricityConsumptionResolve;
