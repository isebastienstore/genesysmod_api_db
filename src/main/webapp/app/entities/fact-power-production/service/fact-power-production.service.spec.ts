import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFactPowerProduction } from '../fact-power-production.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../fact-power-production.test-samples';

import { FactPowerProductionService } from './fact-power-production.service';

const requireRestSample: IFactPowerProduction = {
  ...sampleWithRequiredData,
};

describe('FactPowerProduction Service', () => {
  let service: FactPowerProductionService;
  let httpMock: HttpTestingController;
  let expectedResult: IFactPowerProduction | IFactPowerProduction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FactPowerProductionService);
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

    it('should create a FactPowerProduction', () => {
      const factPowerProduction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(factPowerProduction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FactPowerProduction', () => {
      const factPowerProduction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(factPowerProduction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FactPowerProduction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FactPowerProduction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FactPowerProduction', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FactPowerProduction', () => {
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

    describe('addFactPowerProductionToCollectionIfMissing', () => {
      it('should add a FactPowerProduction to an empty array', () => {
        const factPowerProduction: IFactPowerProduction = sampleWithRequiredData;
        expectedResult = service.addFactPowerProductionToCollectionIfMissing([], factPowerProduction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factPowerProduction);
      });

      it('should not add a FactPowerProduction to an array that contains it', () => {
        const factPowerProduction: IFactPowerProduction = sampleWithRequiredData;
        const factPowerProductionCollection: IFactPowerProduction[] = [
          {
            ...factPowerProduction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFactPowerProductionToCollectionIfMissing(factPowerProductionCollection, factPowerProduction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FactPowerProduction to an array that doesn't contain it", () => {
        const factPowerProduction: IFactPowerProduction = sampleWithRequiredData;
        const factPowerProductionCollection: IFactPowerProduction[] = [sampleWithPartialData];
        expectedResult = service.addFactPowerProductionToCollectionIfMissing(factPowerProductionCollection, factPowerProduction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factPowerProduction);
      });

      it('should add only unique FactPowerProduction to an array', () => {
        const factPowerProductionArray: IFactPowerProduction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const factPowerProductionCollection: IFactPowerProduction[] = [sampleWithRequiredData];
        expectedResult = service.addFactPowerProductionToCollectionIfMissing(factPowerProductionCollection, ...factPowerProductionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factPowerProduction: IFactPowerProduction = sampleWithRequiredData;
        const factPowerProduction2: IFactPowerProduction = sampleWithPartialData;
        expectedResult = service.addFactPowerProductionToCollectionIfMissing([], factPowerProduction, factPowerProduction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factPowerProduction);
        expect(expectedResult).toContain(factPowerProduction2);
      });

      it('should accept null and undefined values', () => {
        const factPowerProduction: IFactPowerProduction = sampleWithRequiredData;
        expectedResult = service.addFactPowerProductionToCollectionIfMissing([], null, factPowerProduction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factPowerProduction);
      });

      it('should return initial array if no FactPowerProduction is added', () => {
        const factPowerProductionCollection: IFactPowerProduction[] = [sampleWithRequiredData];
        expectedResult = service.addFactPowerProductionToCollectionIfMissing(factPowerProductionCollection, undefined, null);
        expect(expectedResult).toEqual(factPowerProductionCollection);
      });
    });

    describe('compareFactPowerProduction', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFactPowerProduction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'a74d1cbb-33fa-4915-ba2b-f06551ad1713' };
        const entity2 = null;

        const compareResult1 = service.compareFactPowerProduction(entity1, entity2);
        const compareResult2 = service.compareFactPowerProduction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'a74d1cbb-33fa-4915-ba2b-f06551ad1713' };
        const entity2 = { id: '8d5d96eb-015c-44df-8a34-51d8b0355a39' };

        const compareResult1 = service.compareFactPowerProduction(entity1, entity2);
        const compareResult2 = service.compareFactPowerProduction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 'a74d1cbb-33fa-4915-ba2b-f06551ad1713' };
        const entity2 = { id: 'a74d1cbb-33fa-4915-ba2b-f06551ad1713' };

        const compareResult1 = service.compareFactPowerProduction(entity1, entity2);
        const compareResult2 = service.compareFactPowerProduction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
