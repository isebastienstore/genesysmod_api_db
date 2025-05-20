import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFactTradeCost } from '../fact-trade-cost.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fact-trade-cost.test-samples';

import { FactTradeCostService } from './fact-trade-cost.service';

const requireRestSample: IFactTradeCost = {
  ...sampleWithRequiredData,
};

describe('FactTradeCost Service', () => {
  let service: FactTradeCostService;
  let httpMock: HttpTestingController;
  let expectedResult: IFactTradeCost | IFactTradeCost[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FactTradeCostService);
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

    it('should create a FactTradeCost', () => {
      const factTradeCost = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(factTradeCost).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FactTradeCost', () => {
      const factTradeCost = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(factTradeCost).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FactTradeCost', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FactTradeCost', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FactTradeCost', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FactTradeCost', () => {
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

    describe('addFactTradeCostToCollectionIfMissing', () => {
      it('should add a FactTradeCost to an empty array', () => {
        const factTradeCost: IFactTradeCost = sampleWithRequiredData;
        expectedResult = service.addFactTradeCostToCollectionIfMissing([], factTradeCost);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factTradeCost);
      });

      it('should not add a FactTradeCost to an array that contains it', () => {
        const factTradeCost: IFactTradeCost = sampleWithRequiredData;
        const factTradeCostCollection: IFactTradeCost[] = [
          {
            ...factTradeCost,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFactTradeCostToCollectionIfMissing(factTradeCostCollection, factTradeCost);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FactTradeCost to an array that doesn't contain it", () => {
        const factTradeCost: IFactTradeCost = sampleWithRequiredData;
        const factTradeCostCollection: IFactTradeCost[] = [sampleWithPartialData];
        expectedResult = service.addFactTradeCostToCollectionIfMissing(factTradeCostCollection, factTradeCost);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factTradeCost);
      });

      it('should add only unique FactTradeCost to an array', () => {
        const factTradeCostArray: IFactTradeCost[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const factTradeCostCollection: IFactTradeCost[] = [sampleWithRequiredData];
        expectedResult = service.addFactTradeCostToCollectionIfMissing(factTradeCostCollection, ...factTradeCostArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factTradeCost: IFactTradeCost = sampleWithRequiredData;
        const factTradeCost2: IFactTradeCost = sampleWithPartialData;
        expectedResult = service.addFactTradeCostToCollectionIfMissing([], factTradeCost, factTradeCost2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factTradeCost);
        expect(expectedResult).toContain(factTradeCost2);
      });

      it('should accept null and undefined values', () => {
        const factTradeCost: IFactTradeCost = sampleWithRequiredData;
        expectedResult = service.addFactTradeCostToCollectionIfMissing([], null, factTradeCost, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factTradeCost);
      });

      it('should return initial array if no FactTradeCost is added', () => {
        const factTradeCostCollection: IFactTradeCost[] = [sampleWithRequiredData];
        expectedResult = service.addFactTradeCostToCollectionIfMissing(factTradeCostCollection, undefined, null);
        expect(expectedResult).toEqual(factTradeCostCollection);
      });
    });

    describe('compareFactTradeCost', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFactTradeCost(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'cf07c929-0eb5-4e6c-8753-8a751d3f8996' };
        const entity2 = null;

        const compareResult1 = service.compareFactTradeCost(entity1, entity2);
        const compareResult2 = service.compareFactTradeCost(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'cf07c929-0eb5-4e6c-8753-8a751d3f8996' };
        const entity2 = { id: '5a917248-626f-45b4-aca9-ee51466a0b6a' };

        const compareResult1 = service.compareFactTradeCost(entity1, entity2);
        const compareResult2 = service.compareFactTradeCost(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 'cf07c929-0eb5-4e6c-8753-8a751d3f8996' };
        const entity2 = { id: 'cf07c929-0eb5-4e6c-8753-8a751d3f8996' };

        const compareResult1 = service.compareFactTradeCost(entity1, entity2);
        const compareResult2 = service.compareFactTradeCost(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
