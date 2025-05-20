import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IFactPowerPlants, NewFactPowerPlants } from '../fact-power-plants.model';

export type PartialUpdateFactPowerPlants = Partial<IFactPowerPlants> & Pick<IFactPowerPlants, 'id'>;

export type EntityResponseType = HttpResponse<IFactPowerPlants>;
export type EntityArrayResponseType = HttpResponse<IFactPowerPlants[]>;

@Injectable({ providedIn: 'root' })
export class FactPowerPlantsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fact-power-plants');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fact-power-plants/_search');

  create(factPowerPlants: NewFactPowerPlants): Observable<EntityResponseType> {
    return this.http.post<IFactPowerPlants>(this.resourceUrl, factPowerPlants, { observe: 'response' });
  }

  update(factPowerPlants: IFactPowerPlants): Observable<EntityResponseType> {
    return this.http.put<IFactPowerPlants>(`${this.resourceUrl}/${this.getFactPowerPlantsIdentifier(factPowerPlants)}`, factPowerPlants, {
      observe: 'response',
    });
  }

  partialUpdate(factPowerPlants: PartialUpdateFactPowerPlants): Observable<EntityResponseType> {
    return this.http.patch<IFactPowerPlants>(`${this.resourceUrl}/${this.getFactPowerPlantsIdentifier(factPowerPlants)}`, factPowerPlants, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFactPowerPlants>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactPowerPlants[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactPowerPlants[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFactPowerPlants[]>()], asapScheduler)));
  }

  getFactPowerPlantsIdentifier(factPowerPlants: Pick<IFactPowerPlants, 'id'>): string {
    return factPowerPlants.id;
  }

  compareFactPowerPlants(o1: Pick<IFactPowerPlants, 'id'> | null, o2: Pick<IFactPowerPlants, 'id'> | null): boolean {
    return o1 && o2 ? this.getFactPowerPlantsIdentifier(o1) === this.getFactPowerPlantsIdentifier(o2) : o1 === o2;
  }

  addFactPowerPlantsToCollectionIfMissing<Type extends Pick<IFactPowerPlants, 'id'>>(
    factPowerPlantsCollection: Type[],
    ...factPowerPlantsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const factPowerPlants: Type[] = factPowerPlantsToCheck.filter(isPresent);
    if (factPowerPlants.length > 0) {
      const factPowerPlantsCollectionIdentifiers = factPowerPlantsCollection.map(factPowerPlantsItem =>
        this.getFactPowerPlantsIdentifier(factPowerPlantsItem),
      );
      const factPowerPlantsToAdd = factPowerPlants.filter(factPowerPlantsItem => {
        const factPowerPlantsIdentifier = this.getFactPowerPlantsIdentifier(factPowerPlantsItem);
        if (factPowerPlantsCollectionIdentifiers.includes(factPowerPlantsIdentifier)) {
          return false;
        }
        factPowerPlantsCollectionIdentifiers.push(factPowerPlantsIdentifier);
        return true;
      });
      return [...factPowerPlantsToAdd, ...factPowerPlantsCollection];
    }
    return factPowerPlantsCollection;
  }
}
