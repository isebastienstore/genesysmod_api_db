import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FactPowerPlantsDetailComponent } from './fact-power-plants-detail.component';

describe('FactPowerPlants Management Detail Component', () => {
  let comp: FactPowerPlantsDetailComponent;
  let fixture: ComponentFixture<FactPowerPlantsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FactPowerPlantsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./fact-power-plants-detail.component').then(m => m.FactPowerPlantsDetailComponent),
              resolve: { factPowerPlants: () => of({ id: '9bd7289f-8607-48ac-9984-c3804adbb150' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FactPowerPlantsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FactPowerPlantsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load factPowerPlants on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FactPowerPlantsDetailComponent);

      // THEN
      expect(instance.factPowerPlants()).toEqual(expect.objectContaining({ id: '9bd7289f-8607-48ac-9984-c3804adbb150' }));
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
