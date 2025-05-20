import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fact-power-production.test-samples';

import { FactPowerProductionFormService } from './fact-power-production-form.service';

describe('FactPowerProduction Form Service', () => {
  let service: FactPowerProductionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactPowerProductionFormService);
  });

  describe('Service methods', () => {
    describe('createFactPowerProductionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactPowerProductionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            year: expect.any(Object),
            country: expect.any(Object),
            technology: expect.any(Object),
            fuel: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });

      it('passing IFactPowerProduction should create a new form with FormGroup', () => {
        const formGroup = service.createFactPowerProductionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            year: expect.any(Object),
            country: expect.any(Object),
            technology: expect.any(Object),
            fuel: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });
    });

    describe('getFactPowerProduction', () => {
      it('should return NewFactPowerProduction for default FactPowerProduction initial value', () => {
        const formGroup = service.createFactPowerProductionFormGroup(sampleWithNewData);

        const factPowerProduction = service.getFactPowerProduction(formGroup) as any;

        expect(factPowerProduction).toMatchObject(sampleWithNewData);
      });

      it('should return NewFactPowerProduction for empty FactPowerProduction initial value', () => {
        const formGroup = service.createFactPowerProductionFormGroup();

        const factPowerProduction = service.getFactPowerProduction(formGroup) as any;

        expect(factPowerProduction).toMatchObject({});
      });

      it('should return IFactPowerProduction', () => {
        const formGroup = service.createFactPowerProductionFormGroup(sampleWithRequiredData);

        const factPowerProduction = service.getFactPowerProduction(formGroup) as any;

        expect(factPowerProduction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFactPowerProduction should not enable id FormControl', () => {
        const formGroup = service.createFactPowerProductionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFactPowerProduction should disable id FormControl', () => {
        const formGroup = service.createFactPowerProductionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
