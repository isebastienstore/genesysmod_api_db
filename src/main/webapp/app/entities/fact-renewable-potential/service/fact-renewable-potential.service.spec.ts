import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFactRenewablePotential } from '../fact-renewable-potential.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../fact-renewable-potential.test-samples';

import { FactRenewablePotentialService } from './fact-renewable-potential.service';

const requireRestSample: IFactRenewablePotential = {
  ...sampleWithRequiredData,
};

describe('FactRenewablePotential Service', () => {
  let service: FactRenewablePotentialService;
  let httpMock: HttpTestingController;
  let expectedResult: IFactRenewablePotential | IFactRenewablePotential[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FactRenewablePotentialService);
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

    it('should create a FactRenewablePotential', () => {
      const factRenewablePotential = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(factRenewablePotential).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FactRenewablePotential', () => {
      const factRenewablePotential = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(factRenewablePotential).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FactRenewablePotential', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FactRenewablePotential', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FactRenewablePotential', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FactRenewablePotential', () => {
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

    describe('addFactRenewablePotentialToCollectionIfMissing', () => {
      it('should add a FactRenewablePotential to an empty array', () => {
        const factRenewablePotential: IFactRenewablePotential = sampleWithRequiredData;
        expectedResult = service.addFactRenewablePotentialToCollectionIfMissing([], factRenewablePotential);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factRenewablePotential);
      });

      it('should not add a FactRenewablePotential to an array that contains it', () => {
        const factRenewablePotential: IFactRenewablePotential = sampleWithRequiredData;
        const factRenewablePotentialCollection: IFactRenewablePotential[] = [
          {
            ...factRenewablePotential,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFactRenewablePotentialToCollectionIfMissing(factRenewablePotentialCollection, factRenewablePotential);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FactRenewablePotential to an array that doesn't contain it", () => {
        const factRenewablePotential: IFactRenewablePotential = sampleWithRequiredData;
        const factRenewablePotentialCollection: IFactRenewablePotential[] = [sampleWithPartialData];
        expectedResult = service.addFactRenewablePotentialToCollectionIfMissing(factRenewablePotentialCollection, factRenewablePotential);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factRenewablePotential);
      });

      it('should add only unique FactRenewablePotential to an array', () => {
        const factRenewablePotentialArray: IFactRenewablePotential[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const factRenewablePotentialCollection: IFactRenewablePotential[] = [sampleWithRequiredData];
        expectedResult = service.addFactRenewablePotentialToCollectionIfMissing(
          factRenewablePotentialCollection,
          ...factRenewablePotentialArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const factRenewablePotential: IFactRenewablePotential = sampleWithRequiredData;
        const factRenewablePotential2: IFactRenewablePotential = sampleWithPartialData;
        expectedResult = service.addFactRenewablePotentialToCollectionIfMissing([], factRenewablePotential, factRenewablePotential2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(factRenewablePotential);
        expect(expectedResult).toContain(factRenewablePotential2);
      });

      it('should accept null and undefined values', () => {
        const factRenewablePotential: IFactRenewablePotential = sampleWithRequiredData;
        expectedResult = service.addFactRenewablePotentialToCollectionIfMissing([], null, factRenewablePotential, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(factRenewablePotential);
      });

      it('should return initial array if no FactRenewablePotential is added', () => {
        const factRenewablePotentialCollection: IFactRenewablePotential[] = [sampleWithRequiredData];
        expectedResult = service.addFactRenewablePotentialToCollectionIfMissing(factRenewablePotentialCollection, undefined, null);
        expect(expectedResult).toEqual(factRenewablePotentialCollection);
      });
    });

    describe('compareFactRenewablePotential', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFactRenewablePotential(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '582d4161-271b-4a20-af9a-e881eb8f3345' };
        const entity2 = null;

        const compareResult1 = service.compareFactRenewablePotential(entity1, entity2);
        const compareResult2 = service.compareFactRenewablePotential(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '582d4161-271b-4a20-af9a-e881eb8f3345' };
        const entity2 = { id: '26a44af4-c24b-4497-907c-1d2ef7f335e5' };

        const compareResult1 = service.compareFactRenewablePotential(entity1, entity2);
        const compareResult2 = service.compareFactRenewablePotential(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: '582d4161-271b-4a20-af9a-e881eb8f3345' };
        const entity2 = { id: '582d4161-271b-4a20-af9a-e881eb8f3345' };

        const compareResult1 = service.compareFactRenewablePotential(entity1, entity2);
        const compareResult2 = service.compareFactRenewablePotential(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
