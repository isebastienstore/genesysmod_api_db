import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fact-renewable-potential.test-samples';

import { FactRenewablePotentialFormService } from './fact-renewable-potential-form.service';

describe('FactRenewablePotential Form Service', () => {
  let service: FactRenewablePotentialFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactRenewablePotentialFormService);
  });

  describe('Service methods', () => {
    describe('createFactRenewablePotentialFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactRenewablePotentialFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            maxCapacity: expect.any(Object),
            availableCapacity: expect.any(Object),
            minCapacity: expect.any(Object),
            country: expect.any(Object),
            year: expect.any(Object),
            technology: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });

      it('passing IFactRenewablePotential should create a new form with FormGroup', () => {
        const formGroup = service.createFactRenewablePotentialFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            maxCapacity: expect.any(Object),
            availableCapacity: expect.any(Object),
            minCapacity: expect.any(Object),
            country: expect.any(Object),
            year: expect.any(Object),
            technology: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });
    });

    describe('getFactRenewablePotential', () => {
      it('should return NewFactRenewablePotential for default FactRenewablePotential initial value', () => {
        const formGroup = service.createFactRenewablePotentialFormGroup(sampleWithNewData);

        const factRenewablePotential = service.getFactRenewablePotential(formGroup) as any;

        expect(factRenewablePotential).toMatchObject(sampleWithNewData);
      });

      it('should return NewFactRenewablePotential for empty FactRenewablePotential initial value', () => {
        const formGroup = service.createFactRenewablePotentialFormGroup();

        const factRenewablePotential = service.getFactRenewablePotential(formGroup) as any;

        expect(factRenewablePotential).toMatchObject({});
      });

      it('should return IFactRenewablePotential', () => {
        const formGroup = service.createFactRenewablePotentialFormGroup(sampleWithRequiredData);

        const factRenewablePotential = service.getFactRenewablePotential(formGroup) as any;

        expect(factRenewablePotential).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFactRenewablePotential should not enable id FormControl', () => {
        const formGroup = service.createFactRenewablePotentialFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFactRenewablePotential should disable id FormControl', () => {
        const formGroup = service.createFactRenewablePotentialFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
