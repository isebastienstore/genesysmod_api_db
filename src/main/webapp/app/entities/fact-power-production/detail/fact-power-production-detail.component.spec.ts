import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FactPowerProductionDetailComponent } from './fact-power-production-detail.component';

describe('FactPowerProduction Management Detail Component', () => {
  let comp: FactPowerProductionDetailComponent;
  let fixture: ComponentFixture<FactPowerProductionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FactPowerProductionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./fact-power-production-detail.component').then(m => m.FactPowerProductionDetailComponent),
              resolve: { factPowerProduction: () => of({ id: 'a74d1cbb-33fa-4915-ba2b-f06551ad1713' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FactPowerProductionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FactPowerProductionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load factPowerProduction on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FactPowerProductionDetailComponent);

      // THEN
      expect(instance.factPowerProduction()).toEqual(expect.objectContaining({ id: 'a74d1cbb-33fa-4915-ba2b-f06551ad1713' }));
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
