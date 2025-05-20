import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IYear } from '../year.model';
import { YearService } from '../service/year.service';

const yearResolve = (route: ActivatedRouteSnapshot): Observable<null | IYear> => {
  const id = route.params.id;
  if (id) {
    return inject(YearService)
      .find(id)
      .pipe(
        mergeMap((year: HttpResponse<IYear>) => {
          if (year.body) {
            return of(year.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default yearResolve;
