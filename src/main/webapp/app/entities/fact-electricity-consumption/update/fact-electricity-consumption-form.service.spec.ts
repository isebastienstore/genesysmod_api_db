import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fact-electricity-consumption.test-samples';

import { FactElectricityConsumptionFormService } from './fact-electricity-consumption-form.service';

describe('FactElectricityConsumption Form Service', () => {
  let service: FactElectricityConsumptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactElectricityConsumptionFormService);
  });

  describe('Service methods', () => {
    describe('createFactElectricityConsumptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactElectricityConsumptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            year: expect.any(Object),
            country: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });

      it('passing IFactElectricityConsumption should create a new form with FormGroup', () => {
        const formGroup = service.createFactElectricityConsumptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            year: expect.any(Object),
            country: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });
    });

    describe('getFactElectricityConsumption', () => {
      it('should return NewFactElectricityConsumption for default FactElectricityConsumption initial value', () => {
        const formGroup = service.createFactElectricityConsumptionFormGroup(sampleWithNewData);

        const factElectricityConsumption = service.getFactElectricityConsumption(formGroup) as any;

        expect(factElectricityConsumption).toMatchObject(sampleWithNewData);
      });

      it('should return NewFactElectricityConsumption for empty FactElectricityConsumption initial value', () => {
        const formGroup = service.createFactElectricityConsumptionFormGroup();

        const factElectricityConsumption = service.getFactElectricityConsumption(formGroup) as any;

        expect(factElectricityConsumption).toMatchObject({});
      });

      it('should return IFactElectricityConsumption', () => {
        const formGroup = service.createFactElectricityConsumptionFormGroup(sampleWithRequiredData);

        const factElectricityConsumption = service.getFactElectricityConsumption(formGroup) as any;

        expect(factElectricityConsumption).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFactElectricityConsumption should not enable id FormControl', () => {
        const formGroup = service.createFactElectricityConsumptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFactElectricityConsumption should disable id FormControl', () => {
        const formGroup = service.createFactElectricityConsumptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
