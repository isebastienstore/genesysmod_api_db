jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FactElectricityConsumptionService } from '../service/fact-electricity-consumption.service';

import { FactElectricityConsumptionDeleteDialogComponent } from './fact-electricity-consumption-delete-dialog.component';

describe('FactElectricityConsumption Management Delete Component', () => {
  let comp: FactElectricityConsumptionDeleteDialogComponent;
  let fixture: ComponentFixture<FactElectricityConsumptionDeleteDialogComponent>;
  let service: FactElectricityConsumptionService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FactElectricityConsumptionDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(FactElectricityConsumptionDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FactElectricityConsumptionDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FactElectricityConsumptionService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
