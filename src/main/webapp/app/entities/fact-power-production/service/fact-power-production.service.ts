import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IFactPowerProduction, NewFactPowerProduction } from '../fact-power-production.model';

export type PartialUpdateFactPowerProduction = Partial<IFactPowerProduction> & Pick<IFactPowerProduction, 'id'>;

export type EntityResponseType = HttpResponse<IFactPowerProduction>;
export type EntityArrayResponseType = HttpResponse<IFactPowerProduction[]>;

@Injectable({ providedIn: 'root' })
export class FactPowerProductionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fact-power-productions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fact-power-productions/_search');

  create(factPowerProduction: NewFactPowerProduction): Observable<EntityResponseType> {
    return this.http.post<IFactPowerProduction>(this.resourceUrl, factPowerProduction, { observe: 'response' });
  }

  update(factPowerProduction: IFactPowerProduction): Observable<EntityResponseType> {
    return this.http.put<IFactPowerProduction>(
      `${this.resourceUrl}/${this.getFactPowerProductionIdentifier(factPowerProduction)}`,
      factPowerProduction,
      { observe: 'response' },
    );
  }

  partialUpdate(factPowerProduction: PartialUpdateFactPowerProduction): Observable<EntityResponseType> {
    return this.http.patch<IFactPowerProduction>(
      `${this.resourceUrl}/${this.getFactPowerProductionIdentifier(factPowerProduction)}`,
      factPowerProduction,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFactPowerProduction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactPowerProduction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactPowerProduction[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFactPowerProduction[]>()], asapScheduler)));
  }

  getFactPowerProductionIdentifier(factPowerProduction: Pick<IFactPowerProduction, 'id'>): string {
    return factPowerProduction.id;
  }

  compareFactPowerProduction(o1: Pick<IFactPowerProduction, 'id'> | null, o2: Pick<IFactPowerProduction, 'id'> | null): boolean {
    return o1 && o2 ? this.getFactPowerProductionIdentifier(o1) === this.getFactPowerProductionIdentifier(o2) : o1 === o2;
  }

  addFactPowerProductionToCollectionIfMissing<Type extends Pick<IFactPowerProduction, 'id'>>(
    factPowerProductionCollection: Type[],
    ...factPowerProductionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const factPowerProductions: Type[] = factPowerProductionsToCheck.filter(isPresent);
    if (factPowerProductions.length > 0) {
      const factPowerProductionCollectionIdentifiers = factPowerProductionCollection.map(factPowerProductionItem =>
        this.getFactPowerProductionIdentifier(factPowerProductionItem),
      );
      const factPowerProductionsToAdd = factPowerProductions.filter(factPowerProductionItem => {
        const factPowerProductionIdentifier = this.getFactPowerProductionIdentifier(factPowerProductionItem);
        if (factPowerProductionCollectionIdentifiers.includes(factPowerProductionIdentifier)) {
          return false;
        }
        factPowerProductionCollectionIdentifiers.push(factPowerProductionIdentifier);
        return true;
      });
      return [...factPowerProductionsToAdd, ...factPowerProductionCollection];
    }
    return factPowerProductionCollection;
  }
}
