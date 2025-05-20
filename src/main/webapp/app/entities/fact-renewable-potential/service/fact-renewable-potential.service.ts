import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IFactRenewablePotential, NewFactRenewablePotential } from '../fact-renewable-potential.model';

export type PartialUpdateFactRenewablePotential = Partial<IFactRenewablePotential> & Pick<IFactRenewablePotential, 'id'>;

export type EntityResponseType = HttpResponse<IFactRenewablePotential>;
export type EntityArrayResponseType = HttpResponse<IFactRenewablePotential[]>;

@Injectable({ providedIn: 'root' })
export class FactRenewablePotentialService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fact-renewable-potentials');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fact-renewable-potentials/_search');

  create(factRenewablePotential: NewFactRenewablePotential): Observable<EntityResponseType> {
    return this.http.post<IFactRenewablePotential>(this.resourceUrl, factRenewablePotential, { observe: 'response' });
  }

  update(factRenewablePotential: IFactRenewablePotential): Observable<EntityResponseType> {
    return this.http.put<IFactRenewablePotential>(
      `${this.resourceUrl}/${this.getFactRenewablePotentialIdentifier(factRenewablePotential)}`,
      factRenewablePotential,
      { observe: 'response' },
    );
  }

  partialUpdate(factRenewablePotential: PartialUpdateFactRenewablePotential): Observable<EntityResponseType> {
    return this.http.patch<IFactRenewablePotential>(
      `${this.resourceUrl}/${this.getFactRenewablePotentialIdentifier(factRenewablePotential)}`,
      factRenewablePotential,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFactRenewablePotential>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactRenewablePotential[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactRenewablePotential[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFactRenewablePotential[]>()], asapScheduler)));
  }

  getFactRenewablePotentialIdentifier(factRenewablePotential: Pick<IFactRenewablePotential, 'id'>): string {
    return factRenewablePotential.id;
  }

  compareFactRenewablePotential(o1: Pick<IFactRenewablePotential, 'id'> | null, o2: Pick<IFactRenewablePotential, 'id'> | null): boolean {
    return o1 && o2 ? this.getFactRenewablePotentialIdentifier(o1) === this.getFactRenewablePotentialIdentifier(o2) : o1 === o2;
  }

  addFactRenewablePotentialToCollectionIfMissing<Type extends Pick<IFactRenewablePotential, 'id'>>(
    factRenewablePotentialCollection: Type[],
    ...factRenewablePotentialsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const factRenewablePotentials: Type[] = factRenewablePotentialsToCheck.filter(isPresent);
    if (factRenewablePotentials.length > 0) {
      const factRenewablePotentialCollectionIdentifiers = factRenewablePotentialCollection.map(factRenewablePotentialItem =>
        this.getFactRenewablePotentialIdentifier(factRenewablePotentialItem),
      );
      const factRenewablePotentialsToAdd = factRenewablePotentials.filter(factRenewablePotentialItem => {
        const factRenewablePotentialIdentifier = this.getFactRenewablePotentialIdentifier(factRenewablePotentialItem);
        if (factRenewablePotentialCollectionIdentifiers.includes(factRenewablePotentialIdentifier)) {
          return false;
        }
        factRenewablePotentialCollectionIdentifiers.push(factRenewablePotentialIdentifier);
        return true;
      });
      return [...factRenewablePotentialsToAdd, ...factRenewablePotentialCollection];
    }
    return factRenewablePotentialCollection;
  }
}
