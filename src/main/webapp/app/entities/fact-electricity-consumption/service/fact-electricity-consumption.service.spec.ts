import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFactElectricityConsumption } from '../fact-electricity-consumption.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../fact-electricity-consumption.test-samples';

import { FactElectricityConsumptionService } from './fact-electricity-consumption.service';

const requireRestSample: IFactElectricityConsumption = {
  ...sampleWithRequiredData,
};

describe('FactElectricityConsumption Service', () => {
  let service: FactElectricityConsumptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IFactElectricityConsumption | IFactElectricityConsumption[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FactElectricityConsumptionService);
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

    it('should create a FactElectricityConsumption', () => {
      const factElectricityConsumption = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(factElectricityConsumption).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FactElectricityConsumption', () => {
      const factElectricityConsumption = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(factElectricityConsumption).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FactElectricityConsumption', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FactElectricityConsumption', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FactElectricityConsumption', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FactElectricityConsumption', () => {
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

    describe('addFactElectricityConsumptionToCollectionIfMissing', () => {
      it('should add a FactElectricityConsumption to an empty array', () => {
        const factElectricityConsumption: IFactElectricityConsumption = sampleWithRequiredData;
        expectedResult = service.addFactElectricityConsumptionToCollectionIfMissing([], factElectricityConsumption);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factElectricityConsumption);
      });

      it('should not add a FactElectricityConsumption to an array that contains it', () => {
        const factElectricityConsumption: IFactElectricityConsumption = sampleWithRequiredData;
        const factElectricityConsumptionCollection: IFactElectricityConsumption[] = [
          {
            ...factElectricityConsumption,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFactElectricityConsumptionToCollectionIfMissing(
          factElectricityConsumptionCollection,
          factElectricityConsumption,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FactElectricityConsumption to an array that doesn't contain it", () => {
        const factElectricityConsumption: IFactElectricityConsumption = sampleWithRequiredData;
        const factElectricityConsumptionCollection: IFactElectricityConsumption[] = [sampleWithPartialData];
        expectedResult = service.addFactElectricityConsumptionToCollectionIfMissing(
          factElectricityConsumptionCollection,
          factElectricityConsumption,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factElectricityConsumption);
      });

      it('should add only unique FactElectricityConsumption to an array', () => {
        const factElectricityConsumptionArray: IFactElectricityConsumption[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const factElectricityConsumptionCollection: IFactElectricityConsumption[] = [sampleWithRequiredData];
        expectedResult = service.addFactElectricityConsumptionToCollectionIfMissing(
          factElectricityConsumptionCollection,
          ...factElectricityConsumptionArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factElectricityConsumption: IFactElectricityConsumption = sampleWithRequiredData;
        const factElectricityConsumption2: IFactElectricityConsumption = sampleWithPartialData;
        expectedResult = service.addFactElectricityConsumptionToCollectionIfMissing(
          [],
          factElectricityConsumption,
          factElectricityConsumption2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factElectricityConsumption);
        expect(expectedResult).toContain(factElectricityConsumption2);
      });

      it('should accept null and undefined values', () => {
        const factElectricityConsumption: IFactElectricityConsumption = sampleWithRequiredData;
        expectedResult = service.addFactElectricityConsumptionToCollectionIfMissing([], null, factElectricityConsumption, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factElectricityConsumption);
      });

      it('should return initial array if no FactElectricityConsumption is added', () => {
        const factElectricityConsumptionCollection: IFactElectricityConsumption[] = [sampleWithRequiredData];
        expectedResult = service.addFactElectricityConsumptionToCollectionIfMissing(factElectricityConsumptionCollection, undefined, null);
        expect(expectedResult).toEqual(factElectricityConsumptionCollection);
      });
    });

    describe('compareFactElectricityConsumption', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFactElectricityConsumption(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '9bbbd317-5807-45f2-b8ef-4f4cbc54f9b5' };
        const entity2 = null;

        const compareResult1 = service.compareFactElectricityConsumption(entity1, entity2);
        const compareResult2 = service.compareFactElectricityConsumption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '9bbbd317-5807-45f2-b8ef-4f4cbc54f9b5' };
        const entity2 = { id: '1f7a05ca-a240-4503-b4bc-d76a44933d59' };

        const compareResult1 = service.compareFactElectricityConsumption(entity1, entity2);
        const compareResult2 = service.compareFactElectricityConsumption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: '9bbbd317-5807-45f2-b8ef-4f4cbc54f9b5' };
        const entity2 = { id: '9bbbd317-5807-45f2-b8ef-4f4cbc54f9b5' };

        const compareResult1 = service.compareFactElectricityConsumption(entity1, entity2);
        const compareResult2 = service.compareFactElectricityConsumption(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
