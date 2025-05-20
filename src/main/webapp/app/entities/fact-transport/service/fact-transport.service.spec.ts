import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFactTransport } from '../fact-transport.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fact-transport.test-samples';

import { FactTransportService } from './fact-transport.service';

const requireRestSample: IFactTransport = {
  ...sampleWithRequiredData,
};

describe('FactTransport Service', () => {
  let service: FactTransportService;
  let httpMock: HttpTestingController;
  let expectedResult: IFactTransport | IFactTransport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FactTransportService);
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

    it('should create a FactTransport', () => {
      const factTransport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(factTransport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FactTransport', () => {
      const factTransport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(factTransport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FactTransport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FactTransport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FactTransport', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FactTransport', () => {
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

    describe('addFactTransportToCollectionIfMissing', () => {
      it('should add a FactTransport to an empty array', () => {
        const factTransport: IFactTransport = sampleWithRequiredData;
        expectedResult = service.addFactTransportToCollectionIfMissing([], factTransport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factTransport);
      });

      it('should not add a FactTransport to an array that contains it', () => {
        const factTransport: IFactTransport = sampleWithRequiredData;
        const factTransportCollection: IFactTransport[] = [
          {
            ...factTransport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFactTransportToCollectionIfMissing(factTransportCollection, factTransport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FactTransport to an array that doesn't contain it", () => {
        const factTransport: IFactTransport = sampleWithRequiredData;
        const factTransportCollection: IFactTransport[] = [sampleWithPartialData];
        expectedResult = service.addFactTransportToCollectionIfMissing(factTransportCollection, factTransport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factTransport);
      });

      it('should add only unique FactTransport to an array', () => {
        const factTransportArray: IFactTransport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const factTransportCollection: IFactTransport[] = [sampleWithRequiredData];
        expectedResult = service.addFactTransportToCollectionIfMissing(factTransportCollection, ...factTransportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factTransport: IFactTransport = sampleWithRequiredData;
        const factTransport2: IFactTransport = sampleWithPartialData;
        expectedResult = service.addFactTransportToCollectionIfMissing([], factTransport, factTransport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factTransport);
        expect(expectedResult).toContain(factTransport2);
      });

      it('should accept null and undefined values', () => {
        const factTransport: IFactTransport = sampleWithRequiredData;
        expectedResult = service.addFactTransportToCollectionIfMissing([], null, factTransport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factTransport);
      });

      it('should return initial array if no FactTransport is added', () => {
        const factTransportCollection: IFactTransport[] = [sampleWithRequiredData];
        expectedResult = service.addFactTransportToCollectionIfMissing(factTransportCollection, undefined, null);
        expect(expectedResult).toEqual(factTransportCollection);
      });
    });

    describe('compareFactTransport', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFactTransport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'ab1926c6-7d20-40b5-9542-b6b3e981782d' };
        const entity2 = null;

        const compareResult1 = service.compareFactTransport(entity1, entity2);
        const compareResult2 = service.compareFactTransport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'ab1926c6-7d20-40b5-9542-b6b3e981782d' };
        const entity2 = { id: '219f00fc-ea27-41fb-aec9-60805eb9d425' };

        const compareResult1 = service.compareFactTransport(entity1, entity2);
        const compareResult2 = service.compareFactTransport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 'ab1926c6-7d20-40b5-9542-b6b3e981782d' };
        const entity2 = { id: 'ab1926c6-7d20-40b5-9542-b6b3e981782d' };

        const compareResult1 = service.compareFactTransport(entity1, entity2);
        const compareResult2 = service.compareFactTransport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
