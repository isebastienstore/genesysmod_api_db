import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IYear, NewYear } from '../year.model';

export type PartialUpdateYear = Partial<IYear> & Pick<IYear, 'id'>;

export type EntityResponseType = HttpResponse<IYear>;
export type EntityArrayResponseType = HttpResponse<IYear[]>;

@Injectable({ providedIn: 'root' })
export class YearService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/years');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/years/_search');

  create(year: NewYear): Observable<EntityResponseType> {
    return this.http.post<IYear>(this.resourceUrl, year, { observe: 'response' });
  }

  update(year: IYear): Observable<EntityResponseType> {
    return this.http.put<IYear>(`${this.resourceUrl}/${this.getYearIdentifier(year)}`, year, { observe: 'response' });
  }

  partialUpdate(year: PartialUpdateYear): Observable<EntityResponseType> {
    return this.http.patch<IYear>(`${this.resourceUrl}/${this.getYearIdentifier(year)}`, year, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IYear>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IYear[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IYear[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IYear[]>()], asapScheduler)));
  }

  getYearIdentifier(year: Pick<IYear, 'id'>): string {
    return year.id;
  }

  compareYear(o1: Pick<IYear, 'id'> | null, o2: Pick<IYear, 'id'> | null): boolean {
    return o1 && o2 ? this.getYearIdentifier(o1) === this.getYearIdentifier(o2) : o1 === o2;
  }

  addYearToCollectionIfMissing<Type extends Pick<IYear, 'id'>>(
    yearCollection: Type[],
    ...yearsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const years: Type[] = yearsToCheck.filter(isPresent);
    if (years.length > 0) {
      const yearCollectionIdentifiers = yearCollection.map(yearItem => this.getYearIdentifier(yearItem));
      const yearsToAdd = years.filter(yearItem => {
        const yearIdentifier = this.getYearIdentifier(yearItem);
        if (yearCollectionIdentifiers.includes(yearIdentifier)) {
          return false;
        }
        yearCollectionIdentifiers.push(yearIdentifier);
        return true;
      });
      return [...yearsToAdd, ...yearCollection];
    }
    return yearCollection;
  }
}
