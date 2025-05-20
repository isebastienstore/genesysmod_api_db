import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../year.test-samples';

import { YearFormService } from './year-form.service';

describe('Year Form Service', () => {
  let service: YearFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(YearFormService);
  });

  describe('Service methods', () => {
    describe('createYearFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createYearFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            year: expect.any(Object),
          }),
        );
      });

      it('passing IYear should create a new form with FormGroup', () => {
        const formGroup = service.createYearFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            year: expect.any(Object),
          }),
        );
      });
    });

    describe('getYear', () => {
      it('should return NewYear for default Year initial value', () => {
        const formGroup = service.createYearFormGroup(sampleWithNewData);

        const year = service.getYear(formGroup) as any;

        expect(year).toMatchObject(sampleWithNewData);
      });

      it('should return NewYear for empty Year initial value', () => {
        const formGroup = service.createYearFormGroup();

        const year = service.getYear(formGroup) as any;

        expect(year).toMatchObject({});
      });

      it('should return IYear', () => {
        const formGroup = service.createYearFormGroup(sampleWithRequiredData);

        const year = service.getYear(formGroup) as any;

        expect(year).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IYear should not enable id FormControl', () => {
        const formGroup = service.createYearFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewYear should disable id FormControl', () => {
        const formGroup = service.createYearFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
