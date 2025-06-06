import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITechnology } from '../technology.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../technology.test-samples';

import { TechnologyService } from './technology.service';

const requireRestSample: ITechnology = {
  ...sampleWithRequiredData,
};

describe('Technology Service', () => {
  let service: TechnologyService;
  let httpMock: HttpTestingController;
  let expectedResult: ITechnology | ITechnology[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TechnologyService);
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

    it('should create a Technology', () => {
      const technology = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(technology).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Technology', () => {
      const technology = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(technology).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Technology', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Technology', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Technology', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Technology', () => {
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

    describe('addTechnologyToCollectionIfMissing', () => {
      it('should add a Technology to an empty array', () => {
        const technology: ITechnology = sampleWithRequiredData;
        expectedResult = service.addTechnologyToCollectionIfMissing([], technology);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(technology);
      });

      it('should not add a Technology to an array that contains it', () => {
        const technology: ITechnology = sampleWithRequiredData;
        const technologyCollection: ITechnology[] = [
          {
            ...technology,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTechnologyToCollectionIfMissing(technologyCollection, technology);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Technology to an array that doesn't contain it", () => {
        const technology: ITechnology = sampleWithRequiredData;
        const technologyCollection: ITechnology[] = [sampleWithPartialData];
        expectedResult = service.addTechnologyToCollectionIfMissing(technologyCollection, technology);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(technology);
      });

      it('should add only unique Technology to an array', () => {
        const technologyArray: ITechnology[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const technologyCollection: ITechnology[] = [sampleWithRequiredData];
        expectedResult = service.addTechnologyToCollectionIfMissing(technologyCollection, ...technologyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const technology: ITechnology = sampleWithRequiredData;
        const technology2: ITechnology = sampleWithPartialData;
        expectedResult = service.addTechnologyToCollectionIfMissing([], technology, technology2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(technology);
        expect(expectedResult).toContain(technology2);
      });

      it('should accept null and undefined values', () => {
        const technology: ITechnology = sampleWithRequiredData;
        expectedResult = service.addTechnologyToCollectionIfMissing([], null, technology, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(technology);
      });

      it('should return initial array if no Technology is added', () => {
        const technologyCollection: ITechnology[] = [sampleWithRequiredData];
        expectedResult = service.addTechnologyToCollectionIfMissing(technologyCollection, undefined, null);
        expect(expectedResult).toEqual(technologyCollection);
      });
    });

    describe('compareTechnology', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTechnology(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
        const entity2 = null;

        const compareResult1 = service.compareTechnology(entity1, entity2);
        const compareResult2 = service.compareTechnology(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
        const entity2 = { id: '487ab60d-d042-4abc-9586-ecb3e3b9550d' };

        const compareResult1 = service.compareTechnology(entity1, entity2);
        const compareResult2 = service.compareTechnology(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
        const entity2 = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };

        const compareResult1 = service.compareTechnology(entity1, entity2);
        const compareResult2 = service.compareTechnology(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
