import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IFactTradeCapacity, NewFactTradeCapacity } from '../fact-trade-capacity.model';

export type PartialUpdateFactTradeCapacity = Partial<IFactTradeCapacity> & Pick<IFactTradeCapacity, 'id'>;

export type EntityResponseType = HttpResponse<IFactTradeCapacity>;
export type EntityArrayResponseType = HttpResponse<IFactTradeCapacity[]>;

@Injectable({ providedIn: 'root' })
export class FactTradeCapacityService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fact-trade-capacities');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fact-trade-capacities/_search');

  create(factTradeCapacity: NewFactTradeCapacity): Observable<EntityResponseType> {
    return this.http.post<IFactTradeCapacity>(this.resourceUrl, factTradeCapacity, { observe: 'response' });
  }

  update(factTradeCapacity: IFactTradeCapacity): Observable<EntityResponseType> {
    return this.http.put<IFactTradeCapacity>(
      `${this.resourceUrl}/${this.getFactTradeCapacityIdentifier(factTradeCapacity)}`,
      factTradeCapacity,
      { observe: 'response' },
    );
  }

  partialUpdate(factTradeCapacity: PartialUpdateFactTradeCapacity): Observable<EntityResponseType> {
    return this.http.patch<IFactTradeCapacity>(
      `${this.resourceUrl}/${this.getFactTradeCapacityIdentifier(factTradeCapacity)}`,
      factTradeCapacity,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFactTradeCapacity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactTradeCapacity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactTradeCapacity[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFactTradeCapacity[]>()], asapScheduler)));
  }

  getFactTradeCapacityIdentifier(factTradeCapacity: Pick<IFactTradeCapacity, 'id'>): string {
    return factTradeCapacity.id;
  }

  compareFactTradeCapacity(o1: Pick<IFactTradeCapacity, 'id'> | null, o2: Pick<IFactTradeCapacity, 'id'> | null): boolean {
    return o1 && o2 ? this.getFactTradeCapacityIdentifier(o1) === this.getFactTradeCapacityIdentifier(o2) : o1 === o2;
  }

  addFactTradeCapacityToCollectionIfMissing<Type extends Pick<IFactTradeCapacity, 'id'>>(
    factTradeCapacityCollection: Type[],
    ...factTradeCapacitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const factTradeCapacities: Type[] = factTradeCapacitiesToCheck.filter(isPresent);
    if (factTradeCapacities.length > 0) {
      const factTradeCapacityCollectionIdentifiers = factTradeCapacityCollection.map(factTradeCapacityItem =>
        this.getFactTradeCapacityIdentifier(factTradeCapacityItem),
      );
      const factTradeCapacitiesToAdd = factTradeCapacities.filter(factTradeCapacityItem => {
        const factTradeCapacityIdentifier = this.getFactTradeCapacityIdentifier(factTradeCapacityItem);
        if (factTradeCapacityCollectionIdentifiers.includes(factTradeCapacityIdentifier)) {
          return false;
        }
        factTradeCapacityCollectionIdentifiers.push(factTradeCapacityIdentifier);
        return true;
      });
      return [...factTradeCapacitiesToAdd, ...factTradeCapacityCollection];
    }
    return factTradeCapacityCollection;
  }
}
