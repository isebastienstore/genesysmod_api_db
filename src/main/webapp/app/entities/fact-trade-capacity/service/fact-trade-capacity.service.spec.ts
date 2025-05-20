import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFactTradeCapacity } from '../fact-trade-capacity.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fact-trade-capacity.test-samples';

import { FactTradeCapacityService } from './fact-trade-capacity.service';

const requireRestSample: IFactTradeCapacity = {
  ...sampleWithRequiredData,
};

describe('FactTradeCapacity Service', () => {
  let service: FactTradeCapacityService;
  let httpMock: HttpTestingController;
  let expectedResult: IFactTradeCapacity | IFactTradeCapacity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FactTradeCapacityService);
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

    it('should create a FactTradeCapacity', () => {
      const factTradeCapacity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(factTradeCapacity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FactTradeCapacity', () => {
      const factTradeCapacity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(factTradeCapacity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FactTradeCapacity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FactTradeCapacity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FactTradeCapacity', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FactTradeCapacity', () => {
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

    describe('addFactTradeCapacityToCollectionIfMissing', () => {
      it('should add a FactTradeCapacity to an empty array', () => {
        const factTradeCapacity: IFactTradeCapacity = sampleWithRequiredData;
        expectedResult = service.addFactTradeCapacityToCollectionIfMissing([], factTradeCapacity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factTradeCapacity);
      });

      it('should not add a FactTradeCapacity to an array that contains it', () => {
        const factTradeCapacity: IFactTradeCapacity = sampleWithRequiredData;
        const factTradeCapacityCollection: IFactTradeCapacity[] = [
          {
            ...factTradeCapacity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFactTradeCapacityToCollectionIfMissing(factTradeCapacityCollection, factTradeCapacity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FactTradeCapacity to an array that doesn't contain it", () => {
        const factTradeCapacity: IFactTradeCapacity = sampleWithRequiredData;
        const factTradeCapacityCollection: IFactTradeCapacity[] = [sampleWithPartialData];
        expectedResult = service.addFactTradeCapacityToCollectionIfMissing(factTradeCapacityCollection, factTradeCapacity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factTradeCapacity);
      });

      it('should add only unique FactTradeCapacity to an array', () => {
        const factTradeCapacityArray: IFactTradeCapacity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const factTradeCapacityCollection: IFactTradeCapacity[] = [sampleWithRequiredData];
        expectedResult = service.addFactTradeCapacityToCollectionIfMissing(factTradeCapacityCollection, ...factTradeCapacityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factTradeCapacity: IFactTradeCapacity = sampleWithRequiredData;
        const factTradeCapacity2: IFactTradeCapacity = sampleWithPartialData;
        expectedResult = service.addFactTradeCapacityToCollectionIfMissing([], factTradeCapacity, factTradeCapacity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factTradeCapacity);
        expect(expectedResult).toContain(factTradeCapacity2);
      });

      it('should accept null and undefined values', () => {
        const factTradeCapacity: IFactTradeCapacity = sampleWithRequiredData;
        expectedResult = service.addFactTradeCapacityToCollectionIfMissing([], null, factTradeCapacity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factTradeCapacity);
      });

      it('should return initial array if no FactTradeCapacity is added', () => {
        const factTradeCapacityCollection: IFactTradeCapacity[] = [sampleWithRequiredData];
        expectedResult = service.addFactTradeCapacityToCollectionIfMissing(factTradeCapacityCollection, undefined, null);
        expect(expectedResult).toEqual(factTradeCapacityCollection);
      });
    });

    describe('compareFactTradeCapacity', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFactTradeCapacity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'ebd45b84-4255-4475-927b-8cf7f0a38e70' };
        const entity2 = null;

        const compareResult1 = service.compareFactTradeCapacity(entity1, entity2);
        const compareResult2 = service.compareFactTradeCapacity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'ebd45b84-4255-4475-927b-8cf7f0a38e70' };
        const entity2 = { id: '9ba6855f-5fa4-4323-b118-f92810f2a9ec' };

        const compareResult1 = service.compareFactTradeCapacity(entity1, entity2);
        const compareResult2 = service.compareFactTradeCapacity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 'ebd45b84-4255-4475-927b-8cf7f0a38e70' };
        const entity2 = { id: 'ebd45b84-4255-4475-927b-8cf7f0a38e70' };

        const compareResult1 = service.compareFactTradeCapacity(entity1, entity2);
        const compareResult2 = service.compareFactTradeCapacity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
