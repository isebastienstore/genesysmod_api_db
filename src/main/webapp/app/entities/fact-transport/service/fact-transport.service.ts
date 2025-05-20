import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IFactTransport, NewFactTransport } from '../fact-transport.model';

export type PartialUpdateFactTransport = Partial<IFactTransport> & Pick<IFactTransport, 'id'>;

export type EntityResponseType = HttpResponse<IFactTransport>;
export type EntityArrayResponseType = HttpResponse<IFactTransport[]>;

@Injectable({ providedIn: 'root' })
export class FactTransportService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fact-transports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fact-transports/_search');

  create(factTransport: NewFactTransport): Observable<EntityResponseType> {
    return this.http.post<IFactTransport>(this.resourceUrl, factTransport, { observe: 'response' });
  }

  update(factTransport: IFactTransport): Observable<EntityResponseType> {
    return this.http.put<IFactTransport>(`${this.resourceUrl}/${this.getFactTransportIdentifier(factTransport)}`, factTransport, {
      observe: 'response',
    });
  }

  partialUpdate(factTransport: PartialUpdateFactTransport): Observable<EntityResponseType> {
    return this.http.patch<IFactTransport>(`${this.resourceUrl}/${this.getFactTransportIdentifier(factTransport)}`, factTransport, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IFactTransport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFactTransport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactTransport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFactTransport[]>()], asapScheduler)));
  }

  getFactTransportIdentifier(factTransport: Pick<IFactTransport, 'id'>): string {
    return factTransport.id;
  }

  compareFactTransport(o1: Pick<IFactTransport, 'id'> | null, o2: Pick<IFactTransport, 'id'> | null): boolean {
    return o1 && o2 ? this.getFactTransportIdentifier(o1) === this.getFactTransportIdentifier(o2) : o1 === o2;
  }

  addFactTransportToCollectionIfMissing<Type extends Pick<IFactTransport, 'id'>>(
    factTransportCollection: Type[],
    ...factTransportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const factTransports: Type[] = factTransportsToCheck.filter(isPresent);
    if (factTransports.length > 0) {
      const factTransportCollectionIdentifiers = factTransportCollection.map(factTransportItem =>
        this.getFactTransportIdentifier(factTransportItem),
      );
      const factTransportsToAdd = factTransports.filter(factTransportItem => {
        const factTransportIdentifier = this.getFactTransportIdentifier(factTransportItem);
        if (factTransportCollectionIdentifiers.includes(factTransportIdentifier)) {
          return false;
        }
        factTransportCollectionIdentifiers.push(factTransportIdentifier);
        return true;
      });
      return [...factTransportsToAdd, ...factTransportCollection];
    }
    return factTransportCollection;
  }
}
