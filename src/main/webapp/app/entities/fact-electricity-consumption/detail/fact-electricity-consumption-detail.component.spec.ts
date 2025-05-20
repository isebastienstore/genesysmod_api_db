import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FactElectricityConsumptionDetailComponent } from './fact-electricity-consumption-detail.component';

describe('FactElectricityConsumption Management Detail Component', () => {
  let comp: FactElectricityConsumptionDetailComponent;
  let fixture: ComponentFixture<FactElectricityConsumptionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FactElectricityConsumptionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./fact-electricity-consumption-detail.component').then(m => m.FactElectricityConsumptionDetailComponent),
              resolve: { factElectricityConsumption: () => of({ id: '9bbbd317-5807-45f2-b8ef-4f4cbc54f9b5' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FactElectricityConsumptionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FactElectricityConsumptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load factElectricityConsumption on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FactElectricityConsumptionDetailComponent);

      // THEN
      expect(instance.factElectricityConsumption()).toEqual(expect.objectContaining({ id: '9bbbd317-5807-45f2-b8ef-4f4cbc54f9b5' }));
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
