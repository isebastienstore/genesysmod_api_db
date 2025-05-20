import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFactPowerPlants } from '../fact-power-plants.model';
import { FactPowerPlantsService } from '../service/fact-power-plants.service';

const factPowerPlantsResolve = (route: ActivatedRouteSnapshot): Observable<null | IFactPowerPlants> => {
  const id = route.params.id;
  if (id) {
    return inject(FactPowerPlantsService)
      .find(id)
      .pipe(
        mergeMap((factPowerPlants: HttpResponse<IFactPowerPlants>) => {
          if (factPowerPlants.body) {
            return of(factPowerPlants.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default factPowerPlantsResolve;
