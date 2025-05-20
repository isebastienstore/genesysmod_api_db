import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fact-trade-cost.test-samples';

import { FactTradeCostFormService } from './fact-trade-cost-form.service';

describe('FactTradeCost Form Service', () => {
  let service: FactTradeCostFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactTradeCostFormService);
  });

  describe('Service methods', () => {
    describe('createFactTradeCostFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactTradeCostFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fixedCost: expect.any(Object),
            variableCost: expect.any(Object),
            fuel: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });

      it('passing IFactTradeCost should create a new form with FormGroup', () => {
        const formGroup = service.createFactTradeCostFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fixedCost: expect.any(Object),
            variableCost: expect.any(Object),
            fuel: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });
    });

    describe('getFactTradeCost', () => {
      it('should return NewFactTradeCost for default FactTradeCost initial value', () => {
        const formGroup = service.createFactTradeCostFormGroup(sampleWithNewData);

        const factTradeCost = service.getFactTradeCost(formGroup) as any;

        expect(factTradeCost).toMatchObject(sampleWithNewData);
      });

      it('should return NewFactTradeCost for empty FactTradeCost initial value', () => {
        const formGroup = service.createFactTradeCostFormGroup();

        const factTradeCost = service.getFactTradeCost(formGroup) as any;

        expect(factTradeCost).toMatchObject({});
      });

      it('should return IFactTradeCost', () => {
        const formGroup = service.createFactTradeCostFormGroup(sampleWithRequiredData);

        const factTradeCost = service.getFactTradeCost(formGroup) as any;

        expect(factTradeCost).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFactTradeCost should not enable id FormControl', () => {
        const formGroup = service.createFactTradeCostFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFactTradeCost should disable id FormControl', () => {
        const formGroup = service.createFactTradeCostFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
