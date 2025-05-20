import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IFactElectricityConsumption, NewFactElectricityConsumption } from '../fact-electricity-consumption.model';

export type PartialUpdateFactElectricityConsumption = Partial<IFactElectricityConsumption> & Pick<IFactElectricityConsumption, 'id'>;

export type EntityResponseType = HttpResponse<IFactElectricityConsumption>;
export type EntityArrayResponseType = HttpResponse<IFactElectricityConsumption[]>;

@Injectable({ providedIn: 'root' })
export class FactElectricityConsumptionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fact-electricity-consumptions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fact-electricity-consumptions/_search');

  create(factElectricityConsumption: NewFactElectricityConsumption): Observable<EntityResponseType> {
    return this.http.post<IFactElectricityConsumption>(this.resourceUrl, factElectricityConsumption, { observe: 'response' });
  }

  update(factElectricityConsumption: IFactElectricityConsumption): Observable<EntityResponseType> {
    return this.http.put<IFactElectricityConsumption>(
      `${this.resourceUrl}/${this.getFactElectricityConsumptionIdentifier(factElectricityConsumption)}`,
      factElectricityConsumption,
      { observe: 'response' },
    );
  }

  partialUpdate(factElectricityConsumption: PartialUpdateFactElectricityConsumption): Observable<EntityResponseType> {
    return this.http.patch<IFactElectricityConsumption>(
      `${this.resourceUrl}/${this.getFactElectricityConsumptionIdentifier(factElectricityConsumption)}`,
      factElectricityConsumption,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFactElectricityConsumption>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactElectricityConsumption[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactElectricityConsumption[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFactElectricityConsumption[]>()], asapScheduler)));
  }

  getFactElectricityConsumptionIdentifier(factElectricityConsumption: Pick<IFactElectricityConsumption, 'id'>): string {
    return factElectricityConsumption.id;
  }

  compareFactElectricityConsumption(
    o1: Pick<IFactElectricityConsumption, 'id'> | null,
    o2: Pick<IFactElectricityConsumption, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getFactElectricityConsumptionIdentifier(o1) === this.getFactElectricityConsumptionIdentifier(o2) : o1 === o2;
  }

  addFactElectricityConsumptionToCollectionIfMissing<Type extends Pick<IFactElectricityConsumption, 'id'>>(
    factElectricityConsumptionCollection: Type[],
    ...factElectricityConsumptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const factElectricityConsumptions: Type[] = factElectricityConsumptionsToCheck.filter(isPresent);
    if (factElectricityConsumptions.length > 0) {
      const factElectricityConsumptionCollectionIdentifiers = factElectricityConsumptionCollection.map(factElectricityConsumptionItem =>
        this.getFactElectricityConsumptionIdentifier(factElectricityConsumptionItem),
      );
      const factElectricityConsumptionsToAdd = factElectricityConsumptions.filter(factElectricityConsumptionItem => {
        const factElectricityConsumptionIdentifier = this.getFactElectricityConsumptionIdentifier(factElectricityConsumptionItem);
        if (factElectricityConsumptionCollectionIdentifiers.includes(factElectricityConsumptionIdentifier)) {
          return false;
        }
        factElectricityConsumptionCollectionIdentifiers.push(factElectricityConsumptionIdentifier);
        return true;
      });
      return [...factElectricityConsumptionsToAdd, ...factElectricityConsumptionCollection];
    }
    return factElectricityConsumptionCollection;
  }
}
