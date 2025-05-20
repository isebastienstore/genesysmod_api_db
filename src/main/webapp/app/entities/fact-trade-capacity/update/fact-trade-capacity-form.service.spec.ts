import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fact-trade-capacity.test-samples';

import { FactTradeCapacityFormService } from './fact-trade-capacity-form.service';

describe('FactTradeCapacity Form Service', () => {
  let service: FactTradeCapacityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactTradeCapacityFormService);
  });

  describe('Service methods', () => {
    describe('createFactTradeCapacityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactTradeCapacityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            year: expect.any(Object),
            country1: expect.any(Object),
            country2: expect.any(Object),
            fuel: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });

      it('passing IFactTradeCapacity should create a new form with FormGroup', () => {
        const formGroup = service.createFactTradeCapacityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            year: expect.any(Object),
            country1: expect.any(Object),
            country2: expect.any(Object),
            fuel: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });
    });

    describe('getFactTradeCapacity', () => {
      it('should return NewFactTradeCapacity for default FactTradeCapacity initial value', () => {
        const formGroup = service.createFactTradeCapacityFormGroup(sampleWithNewData);

        const factTradeCapacity = service.getFactTradeCapacity(formGroup) as any;

        expect(factTradeCapacity).toMatchObject(sampleWithNewData);
      });

      it('should return NewFactTradeCapacity for empty FactTradeCapacity initial value', () => {
        const formGroup = service.createFactTradeCapacityFormGroup();

        const factTradeCapacity = service.getFactTradeCapacity(formGroup) as any;

        expect(factTradeCapacity).toMatchObject({});
      });

      it('should return IFactTradeCapacity', () => {
        const formGroup = service.createFactTradeCapacityFormGroup(sampleWithRequiredData);

        const factTradeCapacity = service.getFactTradeCapacity(formGroup) as any;

        expect(factTradeCapacity).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFactTradeCapacity should not enable id FormControl', () => {
        const formGroup = service.createFactTradeCapacityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFactTradeCapacity should disable id FormControl', () => {
        const formGroup = service.createFactTradeCapacityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
