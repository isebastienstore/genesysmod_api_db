jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FactTradeCapacityService } from '../service/fact-trade-capacity.service';

import { FactTradeCapacityDeleteDialogComponent } from './fact-trade-capacity-delete-dialog.component';

describe('FactTradeCapacity Management Delete Component', () => {
  let comp: FactTradeCapacityDeleteDialogComponent;
  let fixture: ComponentFixture<FactTradeCapacityDeleteDialogComponent>;
  let service: FactTradeCapacityService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FactTradeCapacityDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(FactTradeCapacityDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FactTradeCapacityDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FactTradeCapacityService);
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
