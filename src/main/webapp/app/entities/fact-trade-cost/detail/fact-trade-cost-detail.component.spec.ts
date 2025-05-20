import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FactTradeCostDetailComponent } from './fact-trade-cost-detail.component';

describe('FactTradeCost Management Detail Component', () => {
  let comp: FactTradeCostDetailComponent;
  let fixture: ComponentFixture<FactTradeCostDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FactTradeCostDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./fact-trade-cost-detail.component').then(m => m.FactTradeCostDetailComponent),
              resolve: { factTradeCost: () => of({ id: 'cf07c929-0eb5-4e6c-8753-8a751d3f8996' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FactTradeCostDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FactTradeCostDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load factTradeCost on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FactTradeCostDetailComponent);

      // THEN
      expect(instance.factTradeCost()).toEqual(expect.objectContaining({ id: 'cf07c929-0eb5-4e6c-8753-8a751d3f8996' }));
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
