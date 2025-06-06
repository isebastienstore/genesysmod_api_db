import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICountry } from '../country.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../country.test-samples';

import { CountryService } from './country.service';

const requireRestSample: ICountry = {
  ...sampleWithRequiredData,
};

describe('Country Service', () => {
  let service: CountryService;
  let httpMock: HttpTestingController;
  let expectedResult: ICountry | ICountry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CountryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Country', () => {
      const country = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(country).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Country', () => {
      const country = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(country).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Country', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Country', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Country', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Country', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addCountryToCollectionIfMissing', () => {
      it('should add a Country to an empty array', () => {
        const country: ICountry = sampleWithRequiredData;
        expectedResult = service.addCountryToCollectionIfMissing([], country);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(country);
      });

      it('should not add a Country to an array that contains it', () => {
        const country: ICountry = sampleWithRequiredData;
        const countryCollection: ICountry[] = [
          {
            ...country,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCountryToCollectionIfMissing(countryCollection, country);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Country to an array that doesn't contain it", () => {
        const country: ICountry = sampleWithRequiredData;
        const countryCollection: ICountry[] = [sampleWithPartialData];
        expectedResult = service.addCountryToCollectionIfMissing(countryCollection, country);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(country);
      });

      it('should add only unique Country to an array', () => {
        const countryArray: ICountry[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const countryCollection: ICountry[] = [sampleWithRequiredData];
        expectedResult = service.addCountryToCollectionIfMissing(countryCollection, ...countryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const country: ICountry = sampleWithRequiredData;
        const country2: ICountry = sampleWithPartialData;
        expectedResult = service.addCountryToCollectionIfMissing([], country, country2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(country);
        expect(expectedResult).toContain(country2);
      });

      it('should accept null and undefined values', () => {
        const country: ICountry = sampleWithRequiredData;
        expectedResult = service.addCountryToCollectionIfMissing([], null, country, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(country);
      });

      it('should return initial array if no Country is added', () => {
        const countryCollection: ICountry[] = [sampleWithRequiredData];
        expectedResult = service.addCountryToCollectionIfMissing(countryCollection, undefined, null);
        expect(expectedResult).toEqual(countryCollection);
      });
    });

    describe('compareCountry', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCountry(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
        const entity2 = null;

        const compareResult1 = service.compareCountry(entity1, entity2);
        const compareResult2 = service.compareCountry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
        const entity2 = { id: 'd8127cae-0381-4e62-bed7-eae338eaa9ae' };

        const compareResult1 = service.compareCountry(entity1, entity2);
        const compareResult2 = service.compareCountry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
        const entity2 = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };

        const compareResult1 = service.compareCountry(entity1, entity2);
        const compareResult2 = service.compareCountry(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
