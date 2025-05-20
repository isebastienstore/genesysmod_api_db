import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFuel } from '../fuel.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fuel.test-samples';

import { FuelService } from './fuel.service';

const requireRestSample: IFuel = {
  ...sampleWithRequiredData,
};

describe('Fuel Service', () => {
  let service: FuelService;
  let httpMock: HttpTestingController;
  let expectedResult: IFuel | IFuel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FuelService);
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

    it('should create a Fuel', () => {
      const fuel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fuel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Fuel', () => {
      const fuel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fuel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Fuel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Fuel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Fuel', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Fuel', () => {
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

    describe('addFuelToCollectionIfMissing', () => {
      it('should add a Fuel to an empty array', () => {
        const fuel: IFuel = sampleWithRequiredData;
        expectedResult = service.addFuelToCollectionIfMissing([], fuel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fuel);
      });

      it('should not add a Fuel to an array that contains it', () => {
        const fuel: IFuel = sampleWithRequiredData;
        const fuelCollection: IFuel[] = [
          {
            ...fuel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFuelToCollectionIfMissing(fuelCollection, fuel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Fuel to an array that doesn't contain it", () => {
        const fuel: IFuel = sampleWithRequiredData;
        const fuelCollection: IFuel[] = [sampleWithPartialData];
        expectedResult = service.addFuelToCollectionIfMissing(fuelCollection, fuel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fuel);
      });

      it('should add only unique Fuel to an array', () => {
        const fuelArray: IFuel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fuelCollection: IFuel[] = [sampleWithRequiredData];
        expectedResult = service.addFuelToCollectionIfMissing(fuelCollection, ...fuelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fuel: IFuel = sampleWithRequiredData;
        const fuel2: IFuel = sampleWithPartialData;
        expectedResult = service.addFuelToCollectionIfMissing([], fuel, fuel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fuel);
        expect(expectedResult).toContain(fuel2);
      });

      it('should accept null and undefined values', () => {
        const fuel: IFuel = sampleWithRequiredData;
        expectedResult = service.addFuelToCollectionIfMissing([], null, fuel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fuel);
      });

      it('should return initial array if no Fuel is added', () => {
        const fuelCollection: IFuel[] = [sampleWithRequiredData];
        expectedResult = service.addFuelToCollectionIfMissing(fuelCollection, undefined, null);
        expect(expectedResult).toEqual(fuelCollection);
      });
    });

    describe('compareFuel', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFuel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
        const entity2 = null;

        const compareResult1 = service.compareFuel(entity1, entity2);
        const compareResult2 = service.compareFuel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
        const entity2 = { id: '5eb32af1-969a-475f-9f87-eb7f9dc4de39' };

        const compareResult1 = service.compareFuel(entity1, entity2);
        const compareResult2 = service.compareFuel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
        const entity2 = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };

        const compareResult1 = service.compareFuel(entity1, entity2);
        const compareResult2 = service.compareFuel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
