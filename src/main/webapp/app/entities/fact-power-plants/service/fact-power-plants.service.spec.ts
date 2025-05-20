import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFactPowerPlants } from '../fact-power-plants.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fact-power-plants.test-samples';

import { FactPowerPlantsService } from './fact-power-plants.service';

const requireRestSample: IFactPowerPlants = {
  ...sampleWithRequiredData,
};

describe('FactPowerPlants Service', () => {
  let service: FactPowerPlantsService;
  let httpMock: HttpTestingController;
  let expectedResult: IFactPowerPlants | IFactPowerPlants[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FactPowerPlantsService);
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

    it('should create a FactPowerPlants', () => {
      const factPowerPlants = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(factPowerPlants).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FactPowerPlants', () => {
      const factPowerPlants = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(factPowerPlants).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FactPowerPlants', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FactPowerPlants', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FactPowerPlants', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FactPowerPlants', () => {
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

    describe('addFactPowerPlantsToCollectionIfMissing', () => {
      it('should add a FactPowerPlants to an empty array', () => {
        const factPowerPlants: IFactPowerPlants = sampleWithRequiredData;
        expectedResult = service.addFactPowerPlantsToCollectionIfMissing([], factPowerPlants);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factPowerPlants);
      });

      it('should not add a FactPowerPlants to an array that contains it', () => {
        const factPowerPlants: IFactPowerPlants = sampleWithRequiredData;
        const factPowerPlantsCollection: IFactPowerPlants[] = [
          {
            ...factPowerPlants,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFactPowerPlantsToCollectionIfMissing(factPowerPlantsCollection, factPowerPlants);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FactPowerPlants to an array that doesn't contain it", () => {
        const factPowerPlants: IFactPowerPlants = sampleWithRequiredData;
        const factPowerPlantsCollection: IFactPowerPlants[] = [sampleWithPartialData];
        expectedResult = service.addFactPowerPlantsToCollectionIfMissing(factPowerPlantsCollection, factPowerPlants);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factPowerPlants);
      });

      it('should add only unique FactPowerPlants to an array', () => {
        const factPowerPlantsArray: IFactPowerPlants[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const factPowerPlantsCollection: IFactPowerPlants[] = [sampleWithRequiredData];
        expectedResult = service.addFactPowerPlantsToCollectionIfMissing(factPowerPlantsCollection, ...factPowerPlantsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factPowerPlants: IFactPowerPlants = sampleWithRequiredData;
        const factPowerPlants2: IFactPowerPlants = sampleWithPartialData;
        expectedResult = service.addFactPowerPlantsToCollectionIfMissing([], factPowerPlants, factPowerPlants2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factPowerPlants);
        expect(expectedResult).toContain(factPowerPlants2);
      });

      it('should accept null and undefined values', () => {
        const factPowerPlants: IFactPowerPlants = sampleWithRequiredData;
        expectedResult = service.addFactPowerPlantsToCollectionIfMissing([], null, factPowerPlants, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factPowerPlants);
      });

      it('should return initial array if no FactPowerPlants is added', () => {
        const factPowerPlantsCollection: IFactPowerPlants[] = [sampleWithRequiredData];
        expectedResult = service.addFactPowerPlantsToCollectionIfMissing(factPowerPlantsCollection, undefined, null);
        expect(expectedResult).toEqual(factPowerPlantsCollection);
      });
    });

    describe('compareFactPowerPlants', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFactPowerPlants(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '9bd7289f-8607-48ac-9984-c3804adbb150' };
        const entity2 = null;

        const compareResult1 = service.compareFactPowerPlants(entity1, entity2);
        const compareResult2 = service.compareFactPowerPlants(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '9bd7289f-8607-48ac-9984-c3804adbb150' };
        const entity2 = { id: 'd4810c1a-a1fd-4d59-912c-efae2e48d473' };

        const compareResult1 = service.compareFactPowerPlants(entity1, entity2);
        const compareResult2 = service.compareFactPowerPlants(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: '9bd7289f-8607-48ac-9984-c3804adbb150' };
        const entity2 = { id: '9bd7289f-8607-48ac-9984-c3804adbb150' };

        const compareResult1 = service.compareFactPowerPlants(entity1, entity2);
        const compareResult2 = service.compareFactPowerPlants(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
