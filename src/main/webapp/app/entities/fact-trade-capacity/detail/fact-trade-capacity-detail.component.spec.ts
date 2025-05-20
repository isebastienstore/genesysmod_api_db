import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FactTradeCapacityDetailComponent } from './fact-trade-capacity-detail.component';

describe('FactTradeCapacity Management Detail Component', () => {
  let comp: FactTradeCapacityDetailComponent;
  let fixture: ComponentFixture<FactTradeCapacityDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FactTradeCapacityDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./fact-trade-capacity-detail.component').then(m => m.FactTradeCapacityDetailComponent),
              resolve: { factTradeCapacity: () => of({ id: 'ebd45b84-4255-4475-927b-8cf7f0a38e70' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FactTradeCapacityDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FactTradeCapacityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load factTradeCapacity on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FactTradeCapacityDetailComponent);

      // THEN
      expect(instance.factTradeCapacity()).toEqual(expect.objectContaining({ id: 'ebd45b84-4255-4475-927b-8cf7f0a38e70' }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
