import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fuel.test-samples';

import { FuelFormService } from './fuel-form.service';

describe('Fuel Form Service', () => {
  let service: FuelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FuelFormService);
  });

  describe('Service methods', () => {
    describe('createFuelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFuelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IFuel should create a new form with FormGroup', () => {
        const formGroup = service.createFuelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getFuel', () => {
      it('should return NewFuel for default Fuel initial value', () => {
        const formGroup = service.createFuelFormGroup(sampleWithNewData);

        const fuel = service.getFuel(formGroup) as any;

        expect(fuel).toMatchObject(sampleWithNewData);
      });

      it('should return NewFuel for empty Fuel initial value', () => {
        const formGroup = service.createFuelFormGroup();

        const fuel = service.getFuel(formGroup) as any;

        expect(fuel).toMatchObject({});
      });

      it('should return IFuel', () => {
        const formGroup = service.createFuelFormGroup(sampleWithRequiredData);

        const fuel = service.getFuel(formGroup) as any;

        expect(fuel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFuel should not enable id FormControl', () => {
        const formGroup = service.createFuelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFuel should disable id FormControl', () => {
        const formGroup = service.createFuelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
