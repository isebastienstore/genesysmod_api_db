import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IFactTradeCost, NewFactTradeCost } from '../fact-trade-cost.model';

export type PartialUpdateFactTradeCost = Partial<IFactTradeCost> & Pick<IFactTradeCost, 'id'>;

export type EntityResponseType = HttpResponse<IFactTradeCost>;
export type EntityArrayResponseType = HttpResponse<IFactTradeCost[]>;

@Injectable({ providedIn: 'root' })
export class FactTradeCostService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fact-trade-costs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fact-trade-costs/_search');

  create(factTradeCost: NewFactTradeCost): Observable<EntityResponseType> {
    return this.http.post<IFactTradeCost>(this.resourceUrl, factTradeCost, { observe: 'response' });
  }

  update(factTradeCost: IFactTradeCost): Observable<EntityResponseType> {
    return this.http.put<IFactTradeCost>(`${this.resourceUrl}/${this.getFactTradeCostIdentifier(factTradeCost)}`, factTradeCost, {
      observe: 'response',
    });
  }

  partialUpdate(factTradeCost: PartialUpdateFactTradeCost): Observable<EntityResponseType> {
    return this.http.patch<IFactTradeCost>(`${this.resourceUrl}/${this.getFactTradeCostIdentifier(factTradeCost)}`, factTradeCost, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFactTradeCost>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactTradeCost[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactTradeCost[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFactTradeCost[]>()], asapScheduler)));
  }

  getFactTradeCostIdentifier(factTradeCost: Pick<IFactTradeCost, 'id'>): string {
    return factTradeCost.id;
  }

  compareFactTradeCost(o1: Pick<IFactTradeCost, 'id'> | null, o2: Pick<IFactTradeCost, 'id'> | null): boolean {
    return o1 && o2 ? this.getFactTradeCostIdentifier(o1) === this.getFactTradeCostIdentifier(o2) : o1 === o2;
  }

  addFactTradeCostToCollectionIfMissing<Type extends Pick<IFactTradeCost, 'id'>>(
    factTradeCostCollection: Type[],
    ...factTradeCostsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const factTradeCosts: Type[] = factTradeCostsToCheck.filter(isPresent);
    if (factTradeCosts.length > 0) {
      const factTradeCostCollectionIdentifiers = factTradeCostCollection.map(factTradeCostItem =>
        this.getFactTradeCostIdentifier(factTradeCostItem),
      );
      const factTradeCostsToAdd = factTradeCosts.filter(factTradeCostItem => {
        const factTradeCostIdentifier = this.getFactTradeCostIdentifier(factTradeCostItem);
        if (factTradeCostCollectionIdentifiers.includes(factTradeCostIdentifier)) {
          return false;
        }
        factTradeCostCollectionIdentifiers.push(factTradeCostIdentifier);
        return true;
      });
      return [...factTradeCostsToAdd, ...factTradeCostCollection];
    }
    return factTradeCostCollection;
  }
}
