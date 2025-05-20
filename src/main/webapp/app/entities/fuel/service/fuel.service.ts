import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IFuel, NewFuel } from '../fuel.model';

export type PartialUpdateFuel = Partial<IFuel> & Pick<IFuel, 'id'>;

export type EntityResponseType = HttpResponse<IFuel>;
export type EntityArrayResponseType = HttpResponse<IFuel[]>;

@Injectable({ providedIn: 'root' })
export class FuelService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fuels');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fuels/_search');

  create(fuel: NewFuel): Observable<EntityResponseType> {
    return this.http.post<IFuel>(this.resourceUrl, fuel, { observe: 'response' });
  }

  update(fuel: IFuel): Observable<EntityResponseType> {
    return this.http.put<IFuel>(`${this.resourceUrl}/${this.getFuelIdentifier(fuel)}`, fuel, { observe: 'response' });
  }

  partialUpdate(fuel: PartialUpdateFuel): Observable<EntityResponseType> {
    return this.http.patch<IFuel>(`${this.resourceUrl}/${this.getFuelIdentifier(fuel)}`, fuel, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFuel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFuel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFuel[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFuel[]>()], asapScheduler)));
  }

  getFuelIdentifier(fuel: Pick<IFuel, 'id'>): string {
    return fuel.id;
  }

  compareFuel(o1: Pick<IFuel, 'id'> | null, o2: Pick<IFuel, 'id'> | null): boolean {
    return o1 && o2 ? this.getFuelIdentifier(o1) === this.getFuelIdentifier(o2) : o1 === o2;
  }

  addFuelToCollectionIfMissing<Type extends Pick<IFuel, 'id'>>(
    fuelCollection: Type[],
    ...fuelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fuels: Type[] = fuelsToCheck.filter(isPresent);
    if (fuels.length > 0) {
      const fuelCollectionIdentifiers = fuelCollection.map(fuelItem => this.getFuelIdentifier(fuelItem));
      const fuelsToAdd = fuels.filter(fuelItem => {
        const fuelIdentifier = this.getFuelIdentifier(fuelItem);
        if (fuelCollectionIdentifiers.includes(fuelIdentifier)) {
          return false;
        }
        fuelCollectionIdentifiers.push(fuelIdentifier);
        return true;
      });
      return [...fuelsToAdd, ...fuelCollection];
    }
    return fuelCollection;
  }
}
