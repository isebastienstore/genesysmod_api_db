import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { ICountry, NewCountry } from '../country.model';

export type PartialUpdateCountry = Partial<ICountry> & Pick<ICountry, 'id'>;

export type EntityResponseType = HttpResponse<ICountry>;
export type EntityArrayResponseType = HttpResponse<ICountry[]>;

@Injectable({ providedIn: 'root' })
export class CountryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/countries');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/countries/_search');

  create(country: NewCountry): Observable<EntityResponseType> {
    return this.http.post<ICountry>(this.resourceUrl, country, { observe: 'response' });
  }

  update(country: ICountry): Observable<EntityResponseType> {
    return this.http.put<ICountry>(`${this.resourceUrl}/${this.getCountryIdentifier(country)}`, country, { observe: 'response' });
  }

  partialUpdate(country: PartialUpdateCountry): Observable<EntityResponseType> {
    return this.http.patch<ICountry>(`${this.resourceUrl}/${this.getCountryIdentifier(country)}`, country, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICountry>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICountry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICountry[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ICountry[]>()], asapScheduler)));
  }

  getCountryIdentifier(country: Pick<ICountry, 'id'>): string {
    return country.id;
  }

  compareCountry(o1: Pick<ICountry, 'id'> | null, o2: Pick<ICountry, 'id'> | null): boolean {
    return o1 && o2 ? this.getCountryIdentifier(o1) === this.getCountryIdentifier(o2) : o1 === o2;
  }

  addCountryToCollectionIfMissing<Type extends Pick<ICountry, 'id'>>(
    countryCollection: Type[],
    ...countriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const countries: Type[] = countriesToCheck.filter(isPresent);
    if (countries.length > 0) {
      const countryCollectionIdentifiers = countryCollection.map(countryItem => this.getCountryIdentifier(countryItem));
      const countriesToAdd = countries.filter(countryItem => {
        const countryIdentifier = this.getCountryIdentifier(countryItem);
        if (countryCollectionIdentifiers.includes(countryIdentifier)) {
          return false;
        }
        countryCollectionIdentifiers.push(countryIdentifier);
        return true;
      });
      return [...countriesToAdd, ...countryCollection];
    }
    return countryCollection;
  }
}
