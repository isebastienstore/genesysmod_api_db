import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fact-power-plants.test-samples';

import { FactPowerPlantsFormService } from './fact-power-plants-form.service';

describe('FactPowerPlants Form Service', () => {
  let service: FactPowerPlantsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactPowerPlantsFormService);
  });

  describe('Service methods', () => {
    describe('createFactPowerPlantsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactPowerPlantsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            intalledCapacity: expect.any(Object),
            availabilityCapacity: expect.any(Object),
            status: expect.any(Object),
            commissioningDate: expect.any(Object),
            decommissioningDate: expect.any(Object),
            country: expect.any(Object),
            technology: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });

      it('passing IFactPowerPlants should create a new form with FormGroup', () => {
        const formGroup = service.createFactPowerPlantsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            intalledCapacity: expect.any(Object),
            availabilityCapacity: expect.any(Object),
            status: expect.any(Object),
            commissioningDate: expect.any(Object),
            decommissioningDate: expect.any(Object),
            country: expect.any(Object),
            technology: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });
    });

    describe('getFactPowerPlants', () => {
      it('should return NewFactPowerPlants for default FactPowerPlants initial value', () => {
        const formGroup = service.createFactPowerPlantsFormGroup(sampleWithNewData);

        const factPowerPlants = service.getFactPowerPlants(formGroup) as any;

        expect(factPowerPlants).toMatchObject(sampleWithNewData);
      });

      it('should return NewFactPowerPlants for empty FactPowerPlants initial value', () => {
        const formGroup = service.createFactPowerPlantsFormGroup();

        const factPowerPlants = service.getFactPowerPlants(formGroup) as any;

        expect(factPowerPlants).toMatchObject({});
      });

      it('should return IFactPowerPlants', () => {
        const formGroup = service.createFactPowerPlantsFormGroup(sampleWithRequiredData);

        const factPowerPlants = service.getFactPowerPlants(formGroup) as any;

        expect(factPowerPlants).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFactPowerPlants should not enable id FormControl', () => {
        const formGroup = service.createFactPowerPlantsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFactPowerPlants should disable id FormControl', () => {
        const formGroup = service.createFactPowerPlantsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
