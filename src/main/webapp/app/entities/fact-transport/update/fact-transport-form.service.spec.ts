import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fact-transport.test-samples';

import { FactTransportFormService } from './fact-transport-form.service';

describe('FactTransport Form Service', () => {
  let service: FactTransportFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactTransportFormService);
  });

  describe('Service methods', () => {
    describe('createFactTransportFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactTransportFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            typeOfMobility: expect.any(Object),
            year: expect.any(Object),
            country: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });

      it('passing IFactTransport should create a new form with FormGroup', () => {
        const formGroup = service.createFactTransportFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            value: expect.any(Object),
            typeOfMobility: expect.any(Object),
            year: expect.any(Object),
            country: expect.any(Object),
            metadata: expect.any(Object),
          }),
        );
      });
    });

    describe('getFactTransport', () => {
      it('should return NewFactTransport for default FactTransport initial value', () => {
        const formGroup = service.createFactTransportFormGroup(sampleWithNewData);

        const factTransport = service.getFactTransport(formGroup) as any;

        expect(factTransport).toMatchObject(sampleWithNewData);
      });

      it('should return NewFactTransport for empty FactTransport initial value', () => {
        const formGroup = service.createFactTransportFormGroup();

        const factTransport = service.getFactTransport(formGroup) as any;

        expect(factTransport).toMatchObject({});
      });

      it('should return IFactTransport', () => {
        const formGroup = service.createFactTransportFormGroup(sampleWithRequiredData);

        const factTransport = service.getFactTransport(formGroup) as any;

        expect(factTransport).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFactTransport should not enable id FormControl', () => {
        const formGroup = service.createFactTransportFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFactTransport should disable id FormControl', () => {
        const formGroup = service.createFactTransportFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
